package fi.vtt.nubomedia.jsonrpcwsandroid;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Error;
import com.thetransactioncompany.jsonrpc2.JSONRPC2ParseException;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;

public class JsonRpcResponse {
	private JsonRpcResponseError error;
	private JSONRPC2Response response;

	public JsonRpcResponse(String jsonString) {
		try {
			response = JSONRPC2Response.parse(jsonString);
			if(response.indicatesSuccess()) {
				this.error = null;
			} else {
				JSONRPC2Error error = response.getError();
				this.error = new JsonRpcResponseError(error.getCode(), error.getData());
			}
		} catch (JSONRPC2ParseException e) {
			response = null;
			e.printStackTrace();
		}
	}

	public Object getId() {
		return response.getID();
	}

	public Object getResult() {
		return response.getResult();
	}

	public boolean isSuccessful() {
		return response.indicatesSuccess();
	}

	public JsonRpcResponseError getError() {
		return error;
	}

}
