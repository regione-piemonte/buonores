/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.integration.notificatore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NotificaCustom {
	@JsonProperty("uuid")
	private String uuid;
	@JsonProperty("expire_at")
	private String expireAt;
	@JsonProperty("payload")
	private PayloadNotifica payload;
	@JsonProperty("trusted")
	private Boolean trusted = false;

	public Boolean getTrusted() {
		return trusted;
	}

	public void setTrusted(Boolean trusted) {
		this.trusted = trusted;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getExpireAt() {
		return expireAt;
	}

	public void setExpireAt(String expireAt) {
		this.expireAt = expireAt;
	}

	public PayloadNotifica getPayload() {
		return payload;
	}

	public void setPayload(PayloadNotifica payload) {
		this.payload = payload;
	}

}
