/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonorescallban.dto.custom;

public class ModelRichiestaBatch {
	private Long sportelloId = null;
	private Long domandaId = null;
	private Long domandaDetId = null;
	private String stato = null;

	public Long getSportelloId() {
		return sportelloId;
	}

	public void setSportelloId(Long sportelloId) {
		this.sportelloId = sportelloId;
	}

	public Long getDomandaId() {
		return domandaId;
	}

	public void setDomandaId(Long domandaId) {
		this.domandaId = domandaId;
	}

	public Long getDomandaDetId() {
		return domandaDetId;
	}

	public void setDomandaDetId(Long domandaDetId) {
		this.domandaDetId = domandaDetId;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

}
