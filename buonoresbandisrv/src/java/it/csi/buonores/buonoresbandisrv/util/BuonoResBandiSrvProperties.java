/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BuonoResBandiSrvProperties {

	@Value("${buonoressrvurl}")
	private String buonoressrvurl;

	@Value("${buonoressrvusername}")
	private String buonoressrvusername;

	@Value("${buonoressrvpassword}")
	private String buonoressrvpassword;

	@Value("${bandiacquisizionedomandeurl}")
	private String bandiacquisizionedomandeurl;

	@Value("${jndi.name}")
	private String jndiName;

	@Value("${consumerKey}")
	private String consumerKey;

	@Value("${consumerSecret}")
	private String consumerSecret;

	@Value("${apiManagerUrl}")
	private String apiManagerUrl;

	@Value("${bandiacquisizionedomandeuser}")
	private String bandiacquisizionedomandeuser;

	@Value("${bandiacquisizionedomandepassword}")
	private String bandiacquisizionedomandepassword;

	@Value("${fileKeyCrypt}")
	private String fileKeyCrypt;

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

	public String getBandiacquisizionedomandeurl() {
		return bandiacquisizionedomandeurl;
	}

	public void setBandiacquisizionedomandeurl(String bandiacquisizionedomandeurl) {
		this.bandiacquisizionedomandeurl = bandiacquisizionedomandeurl;
	}

	public String getJndiName() {
		return jndiName;
	}

	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
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

	public String getBandiacquisizionedomandeuser() {
		return bandiacquisizionedomandeuser;
	}

	public void setBandiacquisizionedomandeuser(String bandiacquisizionedomandeuser) {
		this.bandiacquisizionedomandeuser = bandiacquisizionedomandeuser;
	}

	public String getBandiacquisizionedomandepassword() {
		return bandiacquisizionedomandepassword;
	}

	public void setBandiacquisizionedomandepassword(String bandiacquisizionedomandepassword) {
		this.bandiacquisizionedomandepassword = bandiacquisizionedomandepassword;
	}

	public String getFileKeyCrypt() {
		return fileKeyCrypt;
	}

	public void setFileKeyCrypt(String fileKeyCrypt) {
		this.fileKeyCrypt = fileKeyCrypt;
	}
}