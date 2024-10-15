/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BuonoResSrvProperties {

	@Value("${notificatore.richiedente.applicazione}")
	private String nomeApplicazione;
	
	@Value("${notificatore.richiedente.url}")
	private String urlNotificatore;
	
	@Value("${notificatore.richiedente.token}")
	private String tokenApplicativo;
	
	@Value("${notificatore.richiedente.tag}")
	private String tag;
	
	@Value("${notificatore.richiedente.templateId}")
	private String templateId;
	
	@Value("${notificatore.contact.url}")
	private String urlNotificatoreContact;
	
	@Value("${notificatore.contact.token}")
	private String tokenApplicativoContact;

	@Value("${consumerKey}")
	private String consumerKey;
	
	@Value("${consumerSecret}")
	private String consumerSecret;
	
	@Value("${apiManagerUrl}")
	private String apiManagerUrl;
	
	@Value("${stardasUrl}")
	private String stardasUrl;

	@Value("${fileKeyCrypt}")
	private String fileKeyCrypt;
	
	@Value("${jndi.name}") 
	String jndiName;
	
	@Value("${base.url.gestruttureres}")
	private String baseUrlGestruttureres;
	
	@Value("${username.gestruttureres}")
	private String usernameGestruttureres;
	
	@Value("${password.gestruttureres}")
	private String passwordGestruttureres;
	

	public String getNomeApplicazione() {
		return nomeApplicazione;
	}

	public void setNomeApplicazione(String nomeApplicazione) {
		this.nomeApplicazione = nomeApplicazione;
	}

	public String getUrlNotificatore() {
		return urlNotificatore;
	}

	public void setUrlNotificatore(String urlNotificatore) {
		this.urlNotificatore = urlNotificatore;
	}

	public String getTokenApplicativo() {
		return tokenApplicativo;
	}

	public void setTokenApplicativo(String tokenApplicativo) {
		this.tokenApplicativo = tokenApplicativo;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getUrlNotificatoreContact() {
		return urlNotificatoreContact;
	}

	public void setUrlNotificatoreContact(String urlNotificatoreContact) {
		this.urlNotificatoreContact = urlNotificatoreContact;
	}

	public String getTokenApplicativoContact() {
		return tokenApplicativoContact;
	}

	public void setTokenApplicativoContact(String tokenApplicativoContact) {
		this.tokenApplicativoContact = tokenApplicativoContact;
	}

	public String getConsumerKey() {
		return consumerKey;
	}

	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}

	public String getConsumerSecret() {
		return consumerSecret;
	}

	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}

	public String getApiManagerUrl() {
		return apiManagerUrl;
	}

	public void setApiManagerUrl(String apiManagerUrl) {
		this.apiManagerUrl = apiManagerUrl;
	}

	public String getStardasUrl() {
		return stardasUrl;
	}

	public void setStardasUrl(String stardasUrl) {
		this.stardasUrl = stardasUrl;
	}

	public String getFileKeyCrypt() {
		return fileKeyCrypt;
	}

	public void setFileKeyCrypt(String fileKeyCrypt) {
		this.fileKeyCrypt = fileKeyCrypt;
	}

	public String getJndiName() {
		return jndiName;
	}

	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}

	public String getBaseUrlGestruttureres() {
		return baseUrlGestruttureres;
	}

	public void setBaseUrlGestruttureres(String baseUrlGestruttureres) {
		this.baseUrlGestruttureres = baseUrlGestruttureres;
	}

	public String getUsernameGestruttureres() {
		return usernameGestruttureres;
	}

	public void setUsernameGestruttureres(String usernameGestruttureres) {
		this.usernameGestruttureres = usernameGestruttureres;
	}

	public String getPasswordGestruttureres() {
		return passwordGestruttureres;
	}

	public void setPasswordGestruttureres(String passwordGestruttureres) {
		this.passwordGestruttureres = passwordGestruttureres;
	}
}
