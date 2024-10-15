/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

public class ModelFiltroDateIntegrazione {
	private String meseInizio;
	private String meseFine;

	public String getMeseInizio() {
		return meseInizio;
	}

	public void setMeseInizio(String meseInizio) {
		this.meseInizio = meseInizio;
	}

	public String getMeseFine() {
		return meseFine;
	}

	public void setMeseFine(String meseFine) {
		this.meseFine = meseFine;
	}

	@Override
	public String toString() {
		return "ModelFiltroDateIntegrazione [meseInizio="
				+ meseInizio
				+ ", meseFine="
				+ meseFine
				+ "]";
	}

}
