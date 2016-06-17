package fi.vtt.nubomedia.jsonrpcwsandroid;


import android.util.Log;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Message;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Notification;
import com.thetransactioncompany.jsonrpc2.JSONRPC2ParseException;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;

import org.java_websocket.WebSocketFactory;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.client.WebSocketClient.WebSocketClientFactory;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

import fi.vtt.nubomedia.utilitiesandroid.LooperExecutor;

public class JsonRpcWebSocketClient {

	private class ExtendedWebSocketClient extends WebSocketClient {

		public ExtendedWebSocketClient(URI serverUri, WebSocketConnectionEvents events) {
			super(serverUri, new Draft_17());
		}

		@Override
		public void onOpen(final ServerHandshake handshakedata) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					connectionState = WebSocketConnectionState.CONNECTED;
					events.onOpen(handshakedata);
				}
			});
		}

		@Override
		public void onClose(final int code, final String reason, final boolean remote) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					if (connectionState != WebSocketConnectionState.CLOSED) {
						connectionState = WebSocketConnectionState.CLOSED;
						events.onClose(code, reason, remote);
					}
				}
			});
		}

		@Override
		public void onError(final Exception e) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					if (connectionState != WebSocketConnectionState.ERROR) {
						connectionState = WebSocketConnectionState.ERROR;
						events.onError(e);
					}
				}
			});
		}

		@Override
		public void onMessage(final String message) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					if (connectionState == WebSocketConnectionState.CONNECTED) {
						try {
							JSONRPC2Message msg = JSONRPC2Message.parse(message);

							if (msg instanceof JSONRPC2Request) {
								JsonRpcRequest request = new JsonRpcRequest();
								request.setId(((JSONRPC2Request) msg).getID());
								request.setMethod(((JSONRPC2Request) msg).getMethod());
								request.setNamedParams(((JSONRPC2Request) msg).getNamedParams());
								request.setPositionalParams(((JSONRPC2Request) msg).getPositionalParams());
								events.onRequest(request);
							} else if (msg instanceof JSONRPC2Notification) {
								JsonRpcNotification notification = new JsonRpcNotification();
								notification.setMethod(((JSONRPC2Notification) msg).getMethod());
								notification.setNamedParams(((JSONRPC2Notification) msg).getNamedParams());
								notification.setPositionalParams(((JSONRPC2Notification) msg).getPositionalParams());
								events.onNotification(notification);
							} else if (msg instanceof JSONRPC2Response) {
								JsonRpcResponse notification = new JsonRpcResponse(message);
								events.onResponse(notification);
							}
						} catch (JSONRPC2ParseException e) {
							// TODO: Handle exception
						}
					}
				}
			});
		}
	}

	private static final String TAG = "JsonRpcWebSocketClient";
	private static final int CLOSE_TIMEOUT = 1000;

	private WebSocketConnectionState connectionState;
	private WebSocketConnectionEvents events;
	private ExtendedWebSocketClient client;
	private LooperExecutor executor;
	private final Object closeEventLock = new Object();
	private boolean closeEvent;

	public enum WebSocketConnectionState {
		CONNECTED, CLOSED, ERROR
	}

	public interface WebSocketConnectionEvents {
		public void onOpen(ServerHandshake handshakedata);
		public void onRequest(JsonRpcRequest request);
		public void onResponse(JsonRpcResponse response);
		public void onNotification(JsonRpcNotification notification);
		public void onClose(int code, String reason, boolean remote);
		public void onError(Exception e);
	}

	public JsonRpcWebSocketClient(URI serverUri, WebSocketConnectionEvents events, LooperExecutor executor) {
		this.connectionState = WebSocketConnectionState.CLOSED;
		this.events = events;
		this.executor = executor;
		this.client = new ExtendedWebSocketClient(serverUri, events);
	}

	public void connect() {
		checkIfCalledOnValidThread();
		closeEvent = false;
		client.connect();
	}

	public void disconnect(boolean waitForComplete) {
		checkIfCalledOnValidThread();

		if (client.getConnection().isOpen()) {
			client.close();
			connectionState = WebSocketConnectionState.CLOSED;

			if (waitForComplete) {
				synchronized (closeEventLock) {
					while (!closeEvent) {
						try {
							closeEventLock.wait(CLOSE_TIMEOUT);
							break;
						} catch (InterruptedException e) {
							Log.e(TAG, "WebSocket wait error: " + e.toString());
						}
					}
				}
			}
		}
	}

	public void sendRequest(JsonRpcRequest request) {
		checkIfCalledOnValidThread();
		client.send(request.toString());
	}

	public void sendNotification(JsonRpcNotification notification) {
		checkIfCalledOnValidThread();
		client.send(notification.toString());
	}

	public WebSocketConnectionState getConnectionState(){
		return connectionState;
	}

	private void checkIfCalledOnValidThread() {
		if (!executor.checkOnLooperThread()) {
			throw new IllegalStateException("WebSocket method is not called on valid thread");
		}
	}

	public final void setWebSocketFactory( WebSocketClientFactory wsf ) {
		client.setWebSocketFactory(wsf);
	}

	public final WebSocketFactory getWebSocketFactory() {
		return client.getWebSocketFactory();
	}

}
