package fi.vtt.nubomedia.jsonrpcwsandroid;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class JsonRpcRequest {
	private Object id;
	private String method;
	private Map<String, Object> namedParams;
	private List<Object> positionalParams;

	/**
	 *
	 * @return id
	 */
	public Object getId() {
		return id;
	}

	/**
	 *
	 * @param id
	 */
	public void setId(Object id) {
		this.id = id;
	}

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
		// to prevent a recursive loop with setPositionalParams
		if(namedParams!=null) {
			setPositionalParams(null);
		}
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
		// to prevent a recursive loop with setNamedParams
		if(positionalParams!=null) {
			setNamedParams(null);
		}
	}

	/**
	 *
	 * @return
	 */
	public String toString() {
		JSONRPC2Request request;
		if (namedParams != null) {
			request = new JSONRPC2Request(method, namedParams, id);
		} else if (positionalParams != null) {
			request = new JSONRPC2Request(method, positionalParams, id);
		} else {
			request = new JSONRPC2Request(method, id);
		}

		return request.toString();
	}
}
