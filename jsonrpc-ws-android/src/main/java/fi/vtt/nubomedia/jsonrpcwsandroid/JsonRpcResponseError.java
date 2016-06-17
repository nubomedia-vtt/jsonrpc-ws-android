package fi.vtt.nubomedia.jsonrpcwsandroid;

public class JsonRpcResponseError {
	private int code;
	private Object data;

	public JsonRpcResponseError(int code, Object data) {
		this.code = code;
		this.data = data;
	}

	public Object getData() {
		return data;
	}

	public int getCode() {
		return code;
	}
}
