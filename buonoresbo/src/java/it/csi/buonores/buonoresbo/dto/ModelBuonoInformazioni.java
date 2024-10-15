/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

public class ModelBuonoInformazioni {
	private String buonoStatoCod;
	private String validitaFine;
	private String rinunciaDataDecorrenza;
	private String revocaDataDecorrenza;
	private String notaRevoca;

	public String getBuonoStatoCod() {
		return buonoStatoCod;
	}

	public void setBuonoStatoCod(String buonoStatoCod) {
		this.buonoStatoCod = buonoStatoCod;
	}

	public String getValiditaFine() {
		return validitaFine;
	}

	public void setValiditaFine(String validitaFine) {
		this.validitaFine = validitaFine;
	}

	public String getRinunciaDataDecorrenza() {
		return rinunciaDataDecorrenza;
	}

	public void setRinunciaDataDecorrenza(String rinunciaDataDecorrenza) {
		this.rinunciaDataDecorrenza = rinunciaDataDecorrenza;
	}

	public String getRevocaDataDecorrenza() {
		return revocaDataDecorrenza;
	}

	public void setRevocaDataDecorrenza(String revocaDataDecorrenza) {
		this.revocaDataDecorrenza = revocaDataDecorrenza;
	}

	public String getNotaRevoca() {
		return notaRevoca;
	}

	public void setNotaRevoca(String notaRevoca) {
		this.notaRevoca = notaRevoca;
	}

	@Override
	public String toString() {
		return "ModelBuonoInformazioni [buonoStatoCod=" + buonoStatoCod + ", validitaFine=" + validitaFine
				+ ", rinunciaDataDecorrenza=" + rinunciaDataDecorrenza + ", revocaDataDecorrenza="
				+ revocaDataDecorrenza + ", notaRevoca=" + notaRevoca + "]";
	}
}
