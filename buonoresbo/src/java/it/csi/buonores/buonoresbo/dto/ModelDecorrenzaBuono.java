/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

import java.util.Date;

public class ModelDecorrenzaBuono {
	private Date decorrenzaInizio;
	private Date decorrenzaFine;

	public Date getDecorrenzaInizio() {
		return decorrenzaInizio;
	}

	public void setDecorrenzaInizio(Date decorrenzaInizio) {
		this.decorrenzaInizio = decorrenzaInizio;
	}

	public Date getDecorrenzaFine() {
		return decorrenzaFine;
	}

	public void setDecorrenzaFine(Date decorrenzaFine) {
		this.decorrenzaFine = decorrenzaFine;
	}

	@Override
	public String toString() {
		return "ModelDecorrenzaBuono [decorrenzaInizio=" + decorrenzaInizio + ", decorrenzaFine=" + decorrenzaFine
				+ "]";
	}

}
