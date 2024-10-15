/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbatch.util.rest;
import java.util.HashMap;

public class ResponseRest {
	private String json;
	private int statusCode;
	private HashMap<String, String> headers;
	
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public HashMap<String, String> getHeaders() {
		return headers;
	}
	public void addHeaders(String key, String value) {
		if(this.headers ==null) {
			headers = new HashMap<String,String>();
		}
		headers.put(key, value);
	}
	@Override
	public String toString() {
		return "ResponseRest - json: " + ((json != null) ? json : "")
			+ " - statusCode: " + statusCode ;
	}
	
}
