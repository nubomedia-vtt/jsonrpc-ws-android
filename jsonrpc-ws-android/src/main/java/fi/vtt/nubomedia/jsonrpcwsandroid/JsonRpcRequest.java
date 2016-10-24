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

import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;

import java.util.List;
import java.util.Map;

public class JsonRpcRequest {
	private Object id;
	private String method;
	private Map<String, Object> namedParams;
	private List<Object> positionalParams;

	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Map<String, Object> getNamedParams() {
		return namedParams;
	}

	public void setNamedParams(Map<String, Object> namedParams) {
		this.namedParams = namedParams;
		// to prevent a recursive loop with setPositionalParams
		if(namedParams!=null) {
			setPositionalParams(null);
		}
	}

	public List<Object> getPositionalParams() {
		return positionalParams;
	}

	public void setPositionalParams(List<Object> positionalParams) {
		this.positionalParams = positionalParams;
		// to prevent a recursive loop with setNamedParams
		if(positionalParams!=null) {
			setNamedParams(null);
		}
	}

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
