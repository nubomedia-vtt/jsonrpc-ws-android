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

import com.thetransactioncompany.jsonrpc2.JSONRPC2Notification;

import java.util.List;
import java.util.Map;

public class JsonRpcNotification {
	private String method;
	private Map<String, Object> namedParams;
	private List<Object> positionalParams;

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
		if(namedParams!=null) {
			setPositionalParams(null);
		}
	}

	public List<Object> getPositionalParams() {
		return positionalParams;
	}

	public void setPositionalParams(List<Object> positionalParams) {
		this.positionalParams = positionalParams;
		if(positionalParams != null) {
			setNamedParams(null);
		}
	}

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
