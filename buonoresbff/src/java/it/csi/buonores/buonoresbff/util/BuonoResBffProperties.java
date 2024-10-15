/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BuonoResBffProperties {

	@Value("${fileKeyCrypt}")
	private String fileKeyCrypt;

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

	@Value("${interrogaMefUserBe}")
	private String interrogaMefUserBe;

	@Value("${interrogaMefPassBe}")
	private String interrogaMefPassBe;

	@Value("${interrogaMefServiceUrl}")
	private String interrogaMefServiceUrl;

	@Value("${jndi.name}")
	private String jndiName;

	public String getFileKeyCrypt() {
		return fileKeyCrypt;
	}

	public void setFileKeyCrypt(String fileKeyCrypt) {
		this.fileKeyCrypt = fileKeyCrypt;
	}

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

	public String getInterrogaMefUserBe() {
		return interrogaMefUserBe;
	}

	public void setInterrogaMefUserBe(String interrogaMefUserBe) {
		this.interrogaMefUserBe = interrogaMefUserBe;
	}

	public String getInterrogaMefPassBe() {
		return interrogaMefPassBe;
	}

	public void setInterrogaMefPassBe(String interrogaMefPassBe) {
		this.interrogaMefPassBe = interrogaMefPassBe;
	}

	public String getInterrogaMefServiceUrl() {
		return interrogaMefServiceUrl;
	}

	public void setInterrogaMefServiceUrl(String interrogaMefServiceUrl) {
		this.interrogaMefServiceUrl = interrogaMefServiceUrl;
	}

	public String getJndiName() {
		return jndiName;
	}

	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}
}
