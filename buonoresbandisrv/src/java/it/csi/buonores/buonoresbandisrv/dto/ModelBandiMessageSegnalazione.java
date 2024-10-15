/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.dto;

public class ModelBandiMessageSegnalazione {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private String messaggioErrore = null;
	private String esitoServizio = null;
	private String codiceErrore = null;

	public String getMessaggioErrore() {
		return messaggioErrore;
	}

	public void setMessaggioErrore(String messaggioErrore) {
		this.messaggioErrore = messaggioErrore;
	}

	public String getEsitoServizio() {
		return esitoServizio;
	}

	public void setEsitoServizio(String esitoServizio) {
		this.esitoServizio = esitoServizio;
	}

	public String getCodiceErrore() {
		return codiceErrore;
	}

	public void setCodiceErrore(String codiceErrore) {
		this.codiceErrore = codiceErrore;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ModelBandiMessage [messaggioErrore=");
		builder.append(messaggioErrore);
		builder.append(", esitoServizio=");
		builder.append(esitoServizio);
		builder.append(", codiceErrore=");
		builder.append(codiceErrore);
		builder.append("]");
		return builder.toString();
	}

}
