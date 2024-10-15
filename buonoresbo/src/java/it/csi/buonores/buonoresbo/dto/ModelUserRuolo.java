/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

public class ModelUserRuolo {
	String codFisc;
	String cognome;
	String nome;
	String codRuolo;
	String descRuolo;

	public String getCodFisc() {
		return codFisc;
	}

	public void setCodFisc(String codFisc) {
		this.codFisc = codFisc;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCodRuolo() {
		return codRuolo;
	}

	public void setCodRuolo(String codRuolo) {
		this.codRuolo = codRuolo;
	}

	public String getDescRuolo() {
		return descRuolo;
	}

	public void setDescRuolo(String descRuolo) {
		this.descRuolo = descRuolo;
	}

	@Override
	public String toString() {
		return "ModelUserRuolo [codFisc=" + codFisc + ", cognome=" + cognome + ", nome=" + nome + ", codRuolo="
				+ codRuolo + ", descRuolo=" + descRuolo + "]";
	}
}
