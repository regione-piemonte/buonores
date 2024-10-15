/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CambioStatoPopUp {

	private String numerodomanda = null;
	private List<ModelDatiDaModificare> datidamodificare = new ArrayList<ModelDatiDaModificare>();
	private String notaCittadino = null;
	private String notaInterna = null;
	private String notaEnte = null;

	@JsonProperty("numerodomanda")
	public String getNumerodomanda() {
		return numerodomanda;
	}

	public void setNumerodomanda(String numerodomanda) {
		this.numerodomanda = numerodomanda;
	}

	@JsonProperty("datidamodificare")
	public List<ModelDatiDaModificare> getDatidamodificare() {
		return datidamodificare;
	}

	public void setDatidamodificare(List<ModelDatiDaModificare> datidamodificare) {
		this.datidamodificare = datidamodificare;
	}

	@JsonProperty("notaCittadino")
	public String getNotaCittadino() {
		return notaCittadino;
	}

	public void setNotaCittadino(String notaCittadino) {
		this.notaCittadino = notaCittadino;
	}

	@JsonProperty("notaInterna")
	public String getNotaInterna() {
		return notaInterna;
	}

	public void setNotaInterna(String notaInterna) {
		this.notaInterna = notaInterna;
	}

	@JsonProperty("notaEnte")
	public String getNotaEnte() {
		return notaEnte;
	}

	public void setNotaEnte(String notaEnte) {
		this.notaEnte = notaEnte;
	}

	@Override
	public String toString() {
		return "CambioStatoPopUp [numerodomanda=" + numerodomanda + ", datidamodificare=" + datidamodificare
				+ ", notaCittadino=" + notaCittadino + ", notaInterna=" + notaInterna + ", notaEnte=" + notaEnte + "]";
	}
}
