/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

public class ModelAzione {
	String codAzione;
	String descAzione;

	public String getCodAzione() {
		return codAzione;
	}

	public void setCodAzione(String codAzione) {
		this.codAzione = codAzione;
	}

	public String getDescAzione() {
		return descAzione;
	}

	public void setDescAzione(String descAzione) {
		this.descAzione = descAzione;
	}

	@Override
	public String toString() {
		return "ModelAzione [codAzione=" + codAzione + ", descAzione=" + descAzione + "]";
	}
}
