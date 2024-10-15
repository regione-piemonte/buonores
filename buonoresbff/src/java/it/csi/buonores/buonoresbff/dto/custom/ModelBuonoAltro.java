/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.dto.custom;

import java.util.Date;

public class ModelBuonoAltro {
	private String nota = null;
	private Date dataRevoca = null;
	private Date dataRinuncia = null;
	private Integer motivoId = null;

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public Date getDataRevoca() {
		return dataRevoca;
	}

	public void setDataRevoca(Date dataRevoca) {
		this.dataRevoca = dataRevoca;
	}

	public Date getDataRinuncia() {
		return dataRinuncia;
	}

	public void setDataRinuncia(Date dataRinuncia) {
		this.dataRinuncia = dataRinuncia;
	}

	public Integer getMotivoId() {
		return motivoId;
	}

	public void setMotivoId(Integer motivoId) {
		this.motivoId = motivoId;
	}
}
