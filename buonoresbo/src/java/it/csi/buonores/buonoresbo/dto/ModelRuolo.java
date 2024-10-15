/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

import java.util.List;

public class ModelRuolo {
	String codRuolo;
	String descRuolo;
	List<ModelProfili> listaProfili;

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

	public List<ModelProfili> getListaProfili() {
		return listaProfili;
	}

	public void setListaProfili(List<ModelProfili> listaProfili) {
		this.listaProfili = listaProfili;
	}

	@Override
	public String toString() {
		return "ModelRuoloBuonodom [codRuolo=" + codRuolo + ", descRuolo=" + descRuolo + ", listaProfili="
				+ listaProfili + "]";
	}
}
