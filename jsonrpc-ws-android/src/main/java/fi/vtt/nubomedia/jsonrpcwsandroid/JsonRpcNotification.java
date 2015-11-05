package fi.vtt.nubomedia.jsonrpcwsandroid;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Notification;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class JsonRpcNotification {
	private String method;
	private Map<String, Object> namedParams;
	private List<Object> positionalParams;

	/**
	 *
	 * @return
	 */
	public String getMethod() {
		return method;
	}

	/**
	 *
	 * @param method
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 *
	 * @return
	 */
	public Map<String, Object> getNamedParams() {
		return namedParams;
	}

	/**
	 *
	 * @param namedParams
	 */
	public void setNamedParams(Map<String, Object> namedParams) {
		this.namedParams = namedParams;
		setPositionalParams(null);
	}

	/**
	 *
	 * @return
	 */
	public List<Object> getPositionalParams() {
		return positionalParams;
	}

	/**
	 *
	 * @param positionalParams
	 */
	public void setPositionalParams(List<Object> positionalParams) {
		this.positionalParams = positionalParams;
		setNamedParams(null);
	}

	/**
	 *
	 * @return
	 */
	public String toString() {
		JSONRPC2Notification notification;
		if (namedParams != null) {
			notification = new JSONRPC2Notification(method, namedParams);
		} else if (positionalParams != null) {
			notification = new JSONRPC2Notification(method, positionalParams);
		} else {
			notification = new JSONRPC2Notification(method);
		}

		return notification.toString();
	}
}
