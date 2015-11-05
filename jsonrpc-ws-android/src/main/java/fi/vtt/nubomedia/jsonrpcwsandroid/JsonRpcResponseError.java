package fi.vtt.nubomedia.jsonrpcwsandroid;

/**
 *
 */
public class JsonRpcResponseError {
	private int code;
	private Object data;

	/**
	 *
	 * @param code
	 * @param data
	 */
	public JsonRpcResponseError(int code, Object data) {
		this.code = code;
		this.data = data;
	}

	/**
	 *
	 * @return
	 */
	public Object getData() {
		return data;
	}

	/**
	 *
	 * @return
	 */
	public int getCode() {
		return code;
	}
}
