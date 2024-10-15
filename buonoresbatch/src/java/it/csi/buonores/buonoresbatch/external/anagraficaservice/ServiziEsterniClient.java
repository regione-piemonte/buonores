/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbatch.external.anagraficaservice;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.Handler;

import it.csi.buonores.buonoresbatch.configuration.Configuration;
import it.csi.buonores.buonoresbatch.dao.BuonoresBatchDAO;
import it.csi.buonores.buonoresbatch.logger.BatchLoggerFactory;

public class ServiziEsterniClient {
	
	private String usernameInterroga = Configuration.get("interrogaMefUserBe");
	private String passwordInterroga= Configuration.get("interrogaMefPassBe");
	private String urlInterroga= Configuration.get("interrogaMefServiceUrl");

	private static ServiziEsterniClient instance;
	

	private ServiziEsterniClient() {
	}
	
	public static ServiziEsterniClient getInstance(BuonoresBatchDAO dao) {
		if (instance == null) {
			instance = new ServiziEsterniClient();
		}
		return instance;
	}
	
	public static ServiziEsterniClient getInstance() {
		return instance;
	}

	public InterrogaMefEsenredd1Soap creaServizio () {
	    URL url = null;
	    try {
	    	url = new URL(getUrlInterroga() + "?wsdl");
	    } catch (MalformedURLException e) {
	    	BatchLoggerFactory.getLogger(this)
				.error("--- creaServizio for url - " + getUrlInterroga() + "?wsdl");
	    }
	    
	    InterrogaMefEsenredd1 service = new InterrogaMefEsenredd1(url);
		
		InterrogaMefEsenredd1Soap port = service.getInterrogaMefEsenredd1Soap();
	
		setWSSecurity((BindingProvider) port);
		return port;
	}
	
	public InterrogaMefEsenreddRes chiamaGetInterrogaMef(String codicefiscale,InterrogaMefEsenredd1Soap port) throws Exception {
		InterrogaMefEsenreddRes dati = null;

		try {
			System.out.println("--- Request getInterroga CF " + codicefiscale );
			dati = port.interrogaMefEsenredd(codicefiscale);
			if(dati==null) {
				BatchLoggerFactory.getLogger(this)
				.error("--- Request getInterroga CF - "+codicefiscale +" esito response ---> "+dati);
			}	else {
				System.out.println("--- Response getInterroga ---"+dati);
				JAXBContext jaxRes = JAXBContext.newInstance(InterrogaMefEsenreddRes.class);
				Marshaller jaxMres = jaxRes.createMarshaller();
				jaxMres.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				jaxMres.marshal(dati, System.out);
				System.out.println("---------------------------\n");
			}
		} catch (WebServiceException ws) {
			BatchLoggerFactory.getLogger(this)
				.error("Errore nella chiamata a InterrogaMef - " + ws);
			throw ws;
		} catch (Exception e) {
			BatchLoggerFactory.getLogger(this)
				.error("Errore generico nella chiamata a InterrogaMef - " + e);
			throw e;
		}

		return dati;
	}

    @SuppressWarnings("rawtypes")
	private void setWSSecurity(BindingProvider prov) {
		prov.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, this.getUrlInterroga());
		
		if(this.usernameInterroga!=null &&  this.passwordInterroga!=null) {
			List<Handler> handlerChain = new ArrayList<Handler>();
			handlerChain.add(new WSSecurityHeaderSOAPHandler(this.usernameInterroga,  this.passwordInterroga));
			prov.getBinding().setHandlerChain(handlerChain);
		}
	}
	
	public String getUsernameInterroga() {
		return usernameInterroga;
	}

	public void setUsernameInterroga(String usernameInterroga) {
		this.usernameInterroga = usernameInterroga;
	}

	public String getPasswordInterroga() {
		return passwordInterroga;
	}

	public void setPasswordInterroga(String passwordInterroga) {
		this.passwordInterroga = passwordInterroga;
	}

	public String getUrlInterroga() {
		return urlInterroga;
	}

	public void setUrlInterroga(String urlInterroga) {
		this.urlInterroga = urlInterroga;
	}


}
