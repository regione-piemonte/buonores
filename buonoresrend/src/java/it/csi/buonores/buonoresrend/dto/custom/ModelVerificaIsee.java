/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresrend.dto.custom;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelVerificaIsee {
	private String anno;
	private String stato;

	@JsonProperty("anno")
	public String getAnno() {
		return anno;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

	@JsonProperty("stato")
	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	@Override
	public int hashCode() {
		return Objects.hash(anno, stato);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModelVerificaIsee other = (ModelVerificaIsee) obj;
		return Objects.equals(anno, other.anno) && Objects.equals(stato, other.stato);
	}

	@Override
	public String toString() {
		return "ModelVerificaIsee [anno=" + anno + ", stato=" + stato + "]";
	}
}
