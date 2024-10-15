/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.dto.custom;

import java.util.Date;

public class ModelBuono {
	private String buonoCod = null;
	private Long buonoId = null;
	private String richiedenteCf = null;
	private String stato = null;
	private Long buonoStatoId = null;
	private Date decorrenzaInizio = null;
	private Date decorrenzaFine = null;

	public String getBuonoCod() {
		return buonoCod;
	}

	public void setBuonoCod(String buonoCod) {
		this.buonoCod = buonoCod;
	}

	public Long getBuonoId() {
		return buonoId;
	}

	public void setBuonoId(Long buonoId) {
		this.buonoId = buonoId;
	}

	public String getRichiedenteCf() {
		return richiedenteCf;
	}

	public void setRichiedenteCf(String richiedenteCf) {
		this.richiedenteCf = richiedenteCf;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public Long getBuonoStatoId() {
		return buonoStatoId;
	}

	public void setBuonoStatoId(Long buonoStatoId) {
		this.buonoStatoId = buonoStatoId;
	}

	public Date getDecorrenzaInizio() {
		return decorrenzaInizio;
	}

	public void setDecorrenzaInizio(Date decorrenzaInizio) {
		this.decorrenzaInizio = decorrenzaInizio;
	}

	public Date getDecorrenzaFine() {
		return decorrenzaFine;
	}

	public void setDecorrenzaFine(Date decorrenzaFine) {
		this.decorrenzaFine = decorrenzaFine;
	}
}
