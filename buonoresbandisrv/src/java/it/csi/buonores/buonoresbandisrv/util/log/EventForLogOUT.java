/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.util.log;

public class EventForLogOUT {
	String eventType;
	String address;
	String httpMethod;
	String contentType;
	String exchangeId;
	String serviceName;
	String portName;
	String portTypeName;
	String headers;
	String payload;

	public EventForLogOUT() {
		super();
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getExchangeId() {
		return exchangeId;
	}

	public void setExchangeId(String exchangeId) {
		this.exchangeId = exchangeId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getPortName() {
		return portName;
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

	public String getPortTypeName() {
		return portTypeName;
	}

	public void setPortTypeName(String portTypeName) {
		this.portTypeName = portTypeName;
	}

	public String getHeaders() {
		return headers;
	}

	public void setHeaders(String headers) {
		this.headers = headers;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public EventForLogOUT(String eventType, String address, String httpMethod, String contentType, String exchangeId,
			String serviceName, String portName, String portTypeName, String headers, String payload) {
		super();
		this.eventType = eventType;
		this.address = address;
		this.httpMethod = httpMethod;
		this.contentType = contentType;
		this.exchangeId = exchangeId;
		this.serviceName = serviceName;
		this.portName = portName;
		this.portTypeName = portTypeName;
		this.headers = headers;
		this.payload = payload;
	}

	@Override
	public String toString() {
		return "EventForLog [EventType:" + eventType + System.lineSeparator() +
				"\tAddress:" + address + System.lineSeparator() +
				"\tHttpMethod:" + httpMethod + System.lineSeparator() +
				"\tContentType:" + contentType + System.lineSeparator() +
				"\tExchangeId:" + exchangeId + System.lineSeparator() +
				"\tServiceName:" + serviceName + System.lineSeparator() +
				"\tPortName:" + portName + System.lineSeparator() +
				"\tPortTypeName:" + portTypeName + System.lineSeparator() +
				"\tHeaders:" + headers + System.lineSeparator() +
				"\tPayload:" + payload + "]" + System.lineSeparator();
	}
}
