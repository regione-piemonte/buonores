/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

import java.util.List;

public class ModelUserInfo {
	String codFisc;
	String cognome;
	String nome;
	List<ModelRuolo> listaRuoli;
	List<ModelEnteGestore> listaEntiGestore;

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

	public List<ModelRuolo> getListaRuoli() {
		return listaRuoli;
	}

	public void setListaRuoli(List<ModelRuolo> listaRuoli) {
		this.listaRuoli = listaRuoli;
	}

	public List<ModelEnteGestore> getListaEntiGestore() {
		return listaEntiGestore;
	}

	public void setListaEntiGestore(List<ModelEnteGestore> listaEntiGestore) {
		this.listaEntiGestore = listaEntiGestore;
	}

	@Override
	public String toString() {
		return "ModelUserInfoBuonodom [codFisc=" + codFisc + ", cognome=" + cognome + ", nome=" + nome + ", listaRuoli="
				+ listaRuoli + ", listaEntiGestore=" + listaEntiGestore + "]";
	}
}
