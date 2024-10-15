/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

public class ModelDomandeAmmissibili {

	private Long dettId;
	private Long sportelloId;
	private String numeroDomanda;
	private String richiedenteCF;
	private boolean riserva;

	public Long getDettId() {
		return dettId;
	}

	public void setDettId(Long dettId) {
		this.dettId = dettId;
	}

	public boolean isRiserva() {
		return riserva;
	}

	public void setRiserva(boolean riserva) {
		this.riserva = riserva;
	}

	public String getNumeroDomanda() {
		return numeroDomanda;
	}

	public void setNumeroDomanda(String numeroDomanda) {
		this.numeroDomanda = numeroDomanda;
	}

	public String getRichiedenteCF() {
		return richiedenteCF;
	}

	public void setRichiedenteCF(String richiedenteCF) {
		this.richiedenteCF = richiedenteCF;
	}

	public Long getSportelloId() {
		return sportelloId;
	}

	public void setSportelloId(Long sportelloId) {
		this.sportelloId = sportelloId;
	}

	@Override
	public String toString() {
		return "ModelDomandeAmmissibili [dettId=" + dettId + ", sportelloId=" + sportelloId + ", numeroDomanda="
				+ numeroDomanda + ", richiedenteCF=" + richiedenteCF + ", riserva=" + riserva + "]";
	}
}
