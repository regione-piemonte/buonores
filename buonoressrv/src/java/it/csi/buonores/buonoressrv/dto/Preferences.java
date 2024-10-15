/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Preferences {

	private String userId;
	private String serviceName;
	private String uuid;
	private String channels = null;

	@JsonProperty("user_id")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@JsonProperty("uuid")
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@JsonProperty("service_name")
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	@JsonProperty("channels")
	public String getChannels() {
		return channels;
	}

	public void setChannels(String channels) {
		this.channels = channels;
	}

	public Preferences() {
		super();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Preferences [userId=");
		builder.append(userId);
		builder.append(", serviceName=");
		builder.append(serviceName);
		builder.append(", uuid=");
		builder.append(uuid);
		builder.append(", channels=");
		builder.append(channels);
		builder.append("]");
		return builder.toString();
	}
}
