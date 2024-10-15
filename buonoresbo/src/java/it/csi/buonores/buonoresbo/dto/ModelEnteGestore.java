/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

public class ModelEnteGestore {
	private Integer idEnteGestore;
	private String denominazioneEnte;

	public Integer getIdEnteGestore() {
		return idEnteGestore;
	}

	public void setIdEnteGestore(Integer idEnteGestore) {
		this.idEnteGestore = idEnteGestore;
	}

	public String getDenominazioneEnte() {
		return denominazioneEnte;
	}

	public void setDenominazioneEnte(String denominazioneEnte) {
		this.denominazioneEnte = denominazioneEnte;
	}

	@Override
	public String toString() {
		return "ModelEnteGestore [idEnteGestore=" + idEnteGestore + ", denominazioneEnte=" + denominazioneEnte + "]";
	}

}
