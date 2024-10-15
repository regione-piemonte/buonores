/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.dto.custom;

import java.math.BigDecimal;
import java.util.Objects;

public class ModelGetAllegatoExt extends ModelGetAllegato {

	private BigDecimal sportelloId;
	private BigDecimal allegatoId;
	private String fileType;
	private BigDecimal domandaId;
	// MODIFICA TAG 70 buonodom
	private String allegatoTipoCod;

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

	public String getAllegatoTipoCod() {
		return allegatoTipoCod;
	}

	public void setAllegatoTipoCod(String allegatoTipoCod) {
		this.allegatoTipoCod = allegatoTipoCod;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(allegatoId, allegatoTipoCod, domandaId, fileType, sportelloId);
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
		return Objects.equals(allegatoId, other.allegatoId) && Objects.equals(allegatoTipoCod, other.allegatoTipoCod)
				&& Objects.equals(domandaId, other.domandaId) && Objects.equals(fileType, other.fileType)
				&& Objects.equals(sportelloId, other.sportelloId);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ModelGetAllegatoExt [sportelloId=");
		builder.append(sportelloId);
		builder.append(", allegatoId=");
		builder.append(allegatoId);
		builder.append(", fileType=");
		builder.append(fileType);
		builder.append(", domandaId=");
		builder.append(domandaId);
		builder.append(", allegatoTipoCod=");
		builder.append(allegatoTipoCod);
		builder.append("]");
		return builder.toString();
	}

}
