/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

import java.util.ArrayList;
import java.util.List;

public class ModelPresaInCarico {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private String numero = null;
	private String stato = null;
	private List<ModelAllegato> allegati = new ArrayList<ModelAllegato>();

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public List<ModelAllegato> getAllegati() {
		return allegati;
	}

	public void setAllegati(List<ModelAllegato> allegati) {
		this.allegati = allegati;
	}

	@Override
	public String toString() {
		return "ModelPresaInCarico [numero=" + numero + ", stato=" + stato + ", allegati=" + allegati + "]";
	}
}
