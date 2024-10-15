/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

public class ModelStatoBuono {
	private Long buonoStatoId;
	private String buonoStatoCod;
	private String buonoStatoDesc;

	public Long getBuonoStatoId() {
		return buonoStatoId;
	}

	public void setBuonoStatoId(Long buonoStatoId) {
		this.buonoStatoId = buonoStatoId;
	}

	public String getBuonoStatoCod() {
		return buonoStatoCod;
	}

	public void setBuonoStatoCod(String buonoStatoCod) {
		this.buonoStatoCod = buonoStatoCod;
	}

	public String getBuonoStatoDesc() {
		return buonoStatoDesc;
	}

	public void setBuonoStatoDesc(String buonoStatoDesc) {
		this.buonoStatoDesc = buonoStatoDesc;
	}

	@Override
	public String toString() {
		return "ModelStatoBuono [buonoStatoId=" + buonoStatoId + ", buonoStatoCod=" + buonoStatoCod
				+ ", buonoStatoDesc=" + buonoStatoDesc + "]";
	}

}
