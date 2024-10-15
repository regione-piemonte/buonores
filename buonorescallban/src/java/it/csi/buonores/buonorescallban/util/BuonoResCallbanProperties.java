/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonorescallban.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BuonoResCallbanProperties {

	@Value("${jndi.name}") 
	private String jndiName;
	
	@Value("${dbKeyCrypt}")
	private String dbKeyCrypt;
	
	@Value("${buonoressrvurl}")
	private String buonoressrvurl;
	
	@Value("${buonoressrvusername}")
	private String buonoressrvusername;
	
	@Value("${buonoressrvpassword}")
	private String buonoressrvpassword;
	
	public String getJndiName() {
		return jndiName;
	}

	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}
	
	public String getDbKeyCrypt() {
		return dbKeyCrypt;
	}

	public void setDbKeyCrypt(String dbKeyCrypt) {
		this.dbKeyCrypt = dbKeyCrypt;
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
}
