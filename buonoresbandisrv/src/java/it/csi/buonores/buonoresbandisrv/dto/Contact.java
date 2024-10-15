/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Contact {

	private String userId;
	private String sms;
	private String email;
	private String phone = null;

	@JsonProperty("user_id")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@JsonProperty("sms")
	public String getSms() {
		return sms;
	}

	public void setSms(String sms) {
		this.sms = sms;
	}

	@JsonProperty("email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@JsonProperty("phone")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Contact [userId=");
		builder.append(userId);
		builder.append(", sms=");
		builder.append(sms);
		builder.append(", email=");
		builder.append(email);
		builder.append(", phone=");
		builder.append(phone);
		// builder.append(", terms=");
		// builder.append(terms);
		builder.append("]");
		return builder.toString();
	}

	public Contact() {
		super();
	}

}
