/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

import java.util.List;

public class ModelProfili {
	String codProfilo;
	String descProfilo;
	List<ModelAzione> listaAzioni;

	public String getCodProfilo() {
		return codProfilo;
	}

	public void setCodProfilo(String codProfilo) {
		this.codProfilo = codProfilo;
	}

	public String getDescProfilo() {
		return descProfilo;
	}

	public void setDescProfilo(String descProfilo) {
		this.descProfilo = descProfilo;
	}

	public List<ModelAzione> getListaAzioni() {
		return listaAzioni;
	}

	public void setListaAzioni(List<ModelAzione> listaAzioni) {
		this.listaAzioni = listaAzioni;
	}

	@Override
	public String toString() {
		return "ModelProfili [codProfilo=" + codProfilo + ", descProfilo=" + descProfilo + ", listaAzioni="
				+ listaAzioni + "]";
	}
}
