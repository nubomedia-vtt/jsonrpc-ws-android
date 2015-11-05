package fi.vtt.nubomedia.jsonrpcwsandroid;


import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

import fi.vtt.nubomedia.utilitiesandroid.LooperExecutor;

/**
 *
 */
public class JsonRpcWebSocketClient extends WebSocketClient {
	private static final String TAG = "JsonRpcWebSocketClient";
	private static final int CLOSE_TIMEOUT = 1000;

	private WebSocketConnectionState connectionState;
	private WebSocketConnectionEvents events;
	private LooperExecutor executor;
	private final Object closeEventLock = new Object();
	private boolean closeEvent;

	/**
	 *
	 */
	public enum WebSocketConnectionState {
		CONNECTED, CLOSED, ERROR
	}

	/**
	 *
	 */
	public interface WebSocketConnectionEvents {
		public void onOpen(ServerHandshake handshakedata);
		public void onMessage(JsonRpcResponse response);
		public void onClose(int code, String reason, boolean remote);
		public void onError(Exception e);
	}


	/**
	 *
	 * @param serverUri
	 * @param events
	 * @param executor
	 */
	public JsonRpcWebSocketClient(URI serverUri, WebSocketConnectionEvents events, LooperExecutor executor) {
		super(serverUri, new Draft_17());

		this.connectionState = WebSocketConnectionState.CLOSED;
		this.events = events;
		this.executor = executor;
	}

	/**
	 *
	 */
	public void connect() {
		checkIfCalledOnValidThread();

		closeEvent = false;

		super.connect();
	}

	/**
	 *
	 * @param waitForComplete
	 */
	public void disconnect(boolean waitForComplete) {
		checkIfCalledOnValidThread();

		if (getConnection().isOpen()) {
			super.close();
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

	/**
	 *
	 * @param request
	 */
	public void sendRequest(JsonRpcRequest request) {
		checkIfCalledOnValidThread();

		super.send(request.toString());
	}

	/**
	 *
	 * @param notification
	 */
	public void sendNotification(JsonRpcNotification notification) {
		checkIfCalledOnValidThread();

		super.send(notification.toString());
	}

	/**
	 *
	 * @return
	 */
	public WebSocketConnectionState getConnectionState(){
		return connectionState;
	}

	/**
	 *
	 */
	private void checkIfCalledOnValidThread() {
		if (!executor.checkOnLooperThread()) {
			throw new IllegalStateException("WebSocket method is not called on valid thread");
		}
	}

	/**
	 *
	 * @param handshakedata
	 */
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

	/**
	 *
	 * @param message
	 */
	@Override
	public void onMessage(final String message) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				if (connectionState == WebSocketConnectionState.CONNECTED) {
					JsonRpcResponse response = new JsonRpcResponse(message);
					if(response != null){
						events.onMessage(response);
					}
				}
			}
		});

	}

	/**
	 *
	 * @param code
	 * @param reason
	 * @param remote
	 */
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

	/**
	 *
	 * @param e
	 */
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

}
