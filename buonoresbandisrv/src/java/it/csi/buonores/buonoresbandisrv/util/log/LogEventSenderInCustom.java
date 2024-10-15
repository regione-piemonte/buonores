/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.util.log;

import org.apache.cxf.ext.logging.event.LogEvent;
import org.apache.cxf.ext.logging.event.LogEventSender;

import it.csi.buonores.buonoresbandisrv.util.LoggerUtil;

public class LogEventSenderInCustom extends LoggerUtil implements LogEventSender {

	@Override
	public void send(LogEvent event) {
		String eventType = event.getType().toString();
		String address = event.getAddress();
		String responseCode = event.getResponseCode().toString();
		String contentType = event.getContentType();
		String exchangeId = event.getExchangeId();
		String serviceName = event.getServiceName().getLocalPart();
		String portName = event.getPortName().getLocalPart();
		String portTypeName = event.getPortTypeName().getLocalPart();
		String headers = event.getHeaders().toString();
		String payload = event.getPayload();
		EventForLogIN ev = new EventForLogIN(eventType, address, responseCode, contentType, exchangeId, serviceName,
				portName, portTypeName, headers, payload);
		logInfo("LogEventSenderInCustom", " - " + ev.toString());
	}

}
