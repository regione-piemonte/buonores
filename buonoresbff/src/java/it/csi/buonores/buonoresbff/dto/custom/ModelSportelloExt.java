/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.dto.custom;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelSportelloExt {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private String sportelloCod = null;
	private String sportelloAnno = null;
	private String sportelloDesc = null;

	@JsonProperty("sportello_anno")
	public String getSportelloAnno() {
		return sportelloAnno;
	}

	public void setSportelloAnno(String sportelloAnno) {
		this.sportelloAnno = sportelloAnno;
	}

	@JsonProperty("sportello_desc")
	public String getSportelloDesc() {
		return sportelloDesc;
	}

	public void setSportelloDesc(String sportelloDesc) {
		this.sportelloDesc = sportelloDesc;
	}

	@JsonProperty("sportello_cod")
	public String getSportelloCod() {
		return sportelloCod;
	}

	public void setSportelloCod(String sportelloCod) {
		this.sportelloCod = sportelloCod;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ModelSportello [sportelloCod=");
		builder.append(sportelloCod);
		builder.append(", sportelloAnno=");
		builder.append(sportelloAnno);
		builder.append(", sportelloDesc=");
		builder.append(sportelloDesc);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(sportelloAnno, sportelloCod, sportelloDesc);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModelSportelloExt other = (ModelSportelloExt) obj;
		return Objects.equals(sportelloAnno, other.sportelloAnno)
				&& Objects.equals(sportelloCod, other.sportelloCod)
				&& Objects.equals(sportelloDesc, other.sportelloDesc);
	}
}