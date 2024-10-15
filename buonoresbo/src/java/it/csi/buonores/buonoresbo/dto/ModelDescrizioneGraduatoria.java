/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

public class ModelDescrizioneGraduatoria {
	private String descrizioneGraduatoria;
	private String codiceGraduatoria;
	private String statoGraduatoria;

	public String getDescrizioneGraduatoria() {
		return descrizioneGraduatoria;
	}

	public void setDescrizioneGraduatoria(String descrizioneGraduatoria) {
		this.descrizioneGraduatoria = descrizioneGraduatoria;
	}

	public String getCodiceGraduatoria() {
		return codiceGraduatoria;
	}

	public void setCodiceGraduatoria(String codiceGraduatoria) {
		this.codiceGraduatoria = codiceGraduatoria;
	}

	public String getStatoGraduatoria() {
		return statoGraduatoria;
	}

	public void setStatoGraduatoria(String statoGraduatoria) {
		this.statoGraduatoria = statoGraduatoria;
	}

	@Override
	public String toString() {
		return "ModelDescrizioneGraduatoria [descrizioneGraduatoria=" + descrizioneGraduatoria + ", codiceGraduatoria="
				+ codiceGraduatoria + ", statoGraduatoria=" + statoGraduatoria + "]";
	}
}
