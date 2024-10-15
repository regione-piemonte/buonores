/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelDocumento {

	private String nomefile = null;
	private String tipologia = null;
	private String path = null;

	/**
	 * codice utilizzato internamente
	 **/
	@JsonProperty("Nomefile")
	public String getNomefile() {
		return nomefile;
	}

	public void setNomefile(String nomefile) {
		this.nomefile = nomefile;
	}

	@JsonProperty("Tipologia")
	public String getTipologia() {
		return tipologia;
	}

	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
