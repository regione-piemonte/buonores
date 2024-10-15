/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

import java.util.Objects;

public class ModelAllegatoBuono {
	private long allegatoId;
	private String allegatoTipo;
	private String allegatoTipoCod;
	private String allegatoTipoDesc;
	private String allegatoFilename;

	public long getAllegatoId() {
		return allegatoId;
	}

	public void setAllegatoId(long allegatoId) {
		this.allegatoId = allegatoId;
	}

	public String getAllegatoTipo() {
		return allegatoTipo;
	}

	public void setAllegatoTipo(String allegatoTipo) {
		this.allegatoTipo = allegatoTipo;
	}

	public String getAllegatoTipoCod() {
		return allegatoTipoCod;
	}

	public void setAllegatoTipoCod(String allegatoTipoCod) {
		this.allegatoTipoCod = allegatoTipoCod;
	}

	public String getAllegatoTipoDesc() {
		return allegatoTipoDesc;
	}

	public void setAllegatoTipoDesc(String allegatoTipoDesc) {
		this.allegatoTipoDesc = allegatoTipoDesc;
	}

	public String getAllegatoFilename() {
		return allegatoFilename;
	}

	public void setAllegatoFilename(String allegatoFilename) {
		this.allegatoFilename = allegatoFilename;
	}

	@Override
	public String toString() {
		return "ModelAllegatoBuono [allegatoId=" + allegatoId + ", allegatoTipo=" + allegatoTipo + ", allegatoTipoCod="
				+ allegatoTipoCod + ", allegatoTipoDesc=" + allegatoTipoDesc + ", allegatoFilename=" + allegatoFilename
				+ "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(allegatoFilename, allegatoId, allegatoTipo, allegatoTipoCod, allegatoTipoDesc);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModelAllegatoBuono other = (ModelAllegatoBuono) obj;
		return Objects.equals(allegatoFilename, other.allegatoFilename) && allegatoId == other.allegatoId
				&& Objects.equals(allegatoTipo, other.allegatoTipo)
				&& Objects.equals(allegatoTipoCod, other.allegatoTipoCod)
				&& Objects.equals(allegatoTipoDesc, other.allegatoTipoDesc);
	}
}
