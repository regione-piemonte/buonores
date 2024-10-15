/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.integration.notificatore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MexPayload {
	@JsonProperty("title")
	private String title;
	@JsonProperty("body")
	private String body;
	@JsonProperty("call_to_action")
	private String calltoaction;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getCalltoaction() {
		return calltoaction;
	}

	public void setCalltoaction(String calltoaction) {
		this.calltoaction = calltoaction;
	}

}
