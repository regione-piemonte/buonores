/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class ModelGetAllegatoExt extends ModelGetAllegato {

	private BigDecimal sportelloId;
	private BigDecimal allegatoId;
	private String fileType;
	private String codTipoAllegato;
	private BigDecimal domandaId;
	private String descTipoAllegato;

	public BigDecimal getSportelloId() {
		return sportelloId;
	}

	public void setSportelloId(BigDecimal sportelloId) {
		this.sportelloId = sportelloId;
	}

	public BigDecimal getAllegatoId() {
		return allegatoId;
	}

	public void setAllegatoId(BigDecimal allegatoId) {
		this.allegatoId = allegatoId;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public BigDecimal getDomandaId() {
		return domandaId;
	}

	public void setDomandaId(BigDecimal domandaId) {
		this.domandaId = domandaId;
	}

	public String getCodTipoAllegato() {
		return codTipoAllegato;
	}

	public void setCodTipoAllegato(String codTipoAllegato) {
		this.codTipoAllegato = codTipoAllegato;
	}

	public String getDescTipoAllegato() {
		return descTipoAllegato;
	}

	public void setDescTipoAllegato(String descTipoAllegato) {
		this.descTipoAllegato = descTipoAllegato;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ Objects.hash(allegatoId, codTipoAllegato, domandaId, fileType, sportelloId, descTipoAllegato);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModelGetAllegatoExt other = (ModelGetAllegatoExt) obj;
		return Objects.equals(allegatoId, other.allegatoId) && Objects.equals(codTipoAllegato, other.codTipoAllegato)
				&& Objects.equals(domandaId, other.domandaId) && Objects.equals(fileType, other.fileType)
				&& Objects.equals(sportelloId, other.sportelloId)
				&& Objects.equals(descTipoAllegato, other.descTipoAllegato);
	}

}
