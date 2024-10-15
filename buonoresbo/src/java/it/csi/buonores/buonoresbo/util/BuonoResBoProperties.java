/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BuonoResBoProperties {

	@Value("${buonoressrvurl}")
	private String buonoressrvurl;

	@Value("${buonoressrvusername}")
	private String buonoressrvusername;

	@Value("${buonoressrvpassword}")
	private String buonoressrvpassword;

	@Value("${buonoresbandisrvurl}")
	private String buonoresbandisrvurl;

	@Value("${buonoresbandisrvusername}")
	private String buonoresbandisrvusername;

	@Value("${buonoresbandisrvpassword}")
	private String buonoresbandisrvpassword;

	@Value("${fileKeyCrypt}")
	private String fileKeyCrypt;

	@Value("${apiManagerUrl}")
	private String apiManagerUrl;

	@Value("${jndi.name}")
	private String jndiName;

	@Value("${notificatore.contact.url}")
	private String urlNotificatoreContact;

	@Value("${notificatore.contact.token}")
	private String tokenApplicativoContact;

	@Value("${notificatore.richiedente.applicazione}")
	private String richiedenteApplicazioneNotificatore;

	@Value("${consumerKey}")
	private String consumerKey;

	@Value("${consumerSecret}")
	private String consumerSecret;

	@Value("${nameServer}")
	private String serverName;

	@Value("${portServer}")
	private String serverPort;

	@Value("${indirizzoFrom}")
	private String indirizzoFrom;

	@Value("${usernameEmail}")
	private String usernameEmail;

	@Value("${passEmail}")
	private String passEmail;

	public String getBuonoressrvurl() {
		return buonoressrvurl;
	}

	public void setBuonoressrvurl(String buonoressrvurl) {
		this.buonoressrvurl = buonoressrvurl;
	}

	public String getBuonoressrvusername() {
		return buonoressrvusername;
	}

	public void setBuonoressrvusername(String buonoressrvusername) {
		this.buonoressrvusername = buonoressrvusername;
	}

	public String getBuonoressrvpassword() {
		return buonoressrvpassword;
	}

	public void setBuonoressrvpassword(String buonoressrvpassword) {
		this.buonoressrvpassword = buonoressrvpassword;
	}

	public String getBuonoresbandisrvurl() {
		return buonoresbandisrvurl;
	}

	public void setBuonoresbandisrvurl(String buonoresbandisrvurl) {
		this.buonoresbandisrvurl = buonoresbandisrvurl;
	}

	public String getBuonoresbandisrvusername() {
		return buonoresbandisrvusername;
	}

	public void setBuonoresbandisrvusername(String buonoresbandisrvusername) {
		this.buonoresbandisrvusername = buonoresbandisrvusername;
	}

	public String getBuonoresbandisrvpassword() {
		return buonoresbandisrvpassword;
	}

	public void setBuonoresbandisrvpassword(String buonoresbandisrvpassword) {
		this.buonoresbandisrvpassword = buonoresbandisrvpassword;
	}

	public String getApiManagerUrl() {
		return apiManagerUrl;
	}

	public void setApiManagerUrl(String apiManagerUrl) {
		this.apiManagerUrl = apiManagerUrl;
	}

	public String getJndiName() {
		return jndiName;
	}

	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
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

	public String getRichiedenteApplicazioneNotificatore() {
		return richiedenteApplicazioneNotificatore;
	}

	public void setRichiedenteApplicazioneNotificatore(String richiedenteApplicazioneNotificatore) {
		this.richiedenteApplicazioneNotificatore = richiedenteApplicazioneNotificatore;
	}

	public String getFileKeyCrypt() {
		return fileKeyCrypt;
	}

	public void setFileKeyCrypt(String fileKeyCrypt) {
		this.fileKeyCrypt = fileKeyCrypt;
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

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getServerPort() {
		return serverPort;
	}

	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}

	public String getIndirizzoFrom() {
		return indirizzoFrom;
	}

	public void setIndirizzoFrom(String indirizzoFrom) {
		this.indirizzoFrom = indirizzoFrom;
	}

	public String getUsernameEmail() {
		return usernameEmail;
	}

	public void setUsernameEmail(String usernameEmail) {
		this.usernameEmail = usernameEmail;
	}

	public String getPassEmail() {
		return passEmail;
	}

	public void setPassEmail(String passEmail) {
		this.passEmail = passEmail;
	}
}
