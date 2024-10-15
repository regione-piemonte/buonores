/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.integration.notificatore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PayloadNotifica {
	@JsonProperty("id")
	private String id;
	@JsonProperty("correlation_id")
	private String correlationId;
	@JsonProperty("user_id")
	private String userId;
	@JsonProperty("tag")
	private String tag;
	@JsonProperty("push")
	private PushPayload push;

	@JsonProperty("email")
	private EmailPayload email;

	@JsonProperty("mex")
	private MexPayload mex;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public EmailPayload getEmail() {
		return email;
	}

	public void setEmail(EmailPayload email) {
		this.email = email;
	}

	public PushPayload getPush() {
		return push;
	}

	public void setPush(PushPayload push) {
		this.push = push;
	}

	public MexPayload getMex() {
		return mex;
	}

	public void setMex(MexPayload mex) {
		this.mex = mex;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

}
