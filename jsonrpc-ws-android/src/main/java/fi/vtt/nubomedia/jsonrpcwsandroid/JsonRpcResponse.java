/*
 * (C) Copyright 2016 VTT (http://www.vtt.fi)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

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
