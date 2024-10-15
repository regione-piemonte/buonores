/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.external.startdas.stardasservice;

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class WSSecurityHeaderSOAPHandler implements SOAPHandler<SOAPMessageContext> {

	private String token;

	public WSSecurityHeaderSOAPHandler(String token) {
		this.token = token;
	}

	public boolean handleMessage(SOAPMessageContext soapMessageContext) {
		
		Boolean outboundProperty = (Boolean) soapMessageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

		if (outboundProperty.booleanValue()) {

			try {
				SOAPEnvelope soapEnvelope = soapMessageContext.getMessage().getSOAPPart().getEnvelope();

				SOAPHeader header = soapEnvelope.getHeader();
				if (header == null) {
					header = soapEnvelope.addHeader();
				}
				SOAPElement headerElement = header.addChildElement("Authorization");
				headerElement.addTextNode("Bearer " + this.token);
			} catch (Exception e) {
				throw new RuntimeException("Error on wsSecurityHandler: " + e.getMessage());
			}
		}

		return true;
	}

	@Override
	public void close(MessageContext context) {
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		return true;
	}

	@Override
	public Set<QName> getHeaders() {
		return null;
	}
}