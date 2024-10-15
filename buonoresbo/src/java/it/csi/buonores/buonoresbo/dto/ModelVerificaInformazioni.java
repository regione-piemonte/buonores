/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

public class ModelVerificaInformazioni {

	private Boolean nessunaIncompatibilita;
	private String verificaNote;
	private String verificaData;

	public Boolean getNessunaIncompatibilita() {
		return nessunaIncompatibilita;
	}

	public void setNessunaIncompatibilita(Boolean nessunaIncompatibilita) {
		this.nessunaIncompatibilita = nessunaIncompatibilita;
	}

	public String getVerificaNote() {
		return verificaNote;
	}

	public void setVerificaNote(String verificaNote) {
		this.verificaNote = verificaNote;
	}

	public String getVerificaData() {
		return verificaData;
	}

	public void setVerificaData(String verificaData) {
		this.verificaData = verificaData;
	}

	@Override
	public String toString() {
		return "ModelVerificaInformazioni [nessunaIncompatibilita=" + nessunaIncompatibilita + ", verificaNote="
				+ verificaNote + ", verificaData=" + verificaData + "]";
	}

}
