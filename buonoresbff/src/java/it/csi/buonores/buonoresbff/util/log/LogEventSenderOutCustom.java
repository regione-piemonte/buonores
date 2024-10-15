/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.util.log;

import org.apache.cxf.ext.logging.event.LogEvent;
import org.apache.cxf.ext.logging.event.LogEventSender;

import it.csi.buonores.buonoresbff.util.LoggerUtil;

public class LogEventSenderOutCustom extends LoggerUtil implements LogEventSender {

	@Override
	public void send(LogEvent event) {
		String eventType = event.getType().toString();
		String address = event.getAddress();
		String httpMethod = event.getHttpMethod();
		String contentType = event.getContentType();
		String exchangeId = event.getExchangeId();
		String serviceName = event.getServiceName().getLocalPart();
		String portName = event.getPortName().getLocalPart();
		String portTypeName = event.getPortTypeName().getLocalPart();
		String headers = event.getHeaders().toString();
		String payload = event.getPayload();
		EventForLogOUT ev = new EventForLogOUT(eventType, address, httpMethod, contentType, exchangeId, serviceName,
				portName, portTypeName, headers, payload);
		logInfo("LogEventSenderOutCustom", " - " + ev.toString());
	}
}