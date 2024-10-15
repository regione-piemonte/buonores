/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.dto.custom;

public class ModelRicezioneSegnalazione {

	private String numeroDomanda = null;
	private String codiceNotifica = null;
	private String data = null;
	private String descrizioneNotifica = null;

	public String getNumeroDomanda() {
		return numeroDomanda;
	}

	public void setNumeroDomanda(String numeroDomanda) {
		this.numeroDomanda = numeroDomanda;
	}

	public String getCodiceNotifica() {
		return codiceNotifica;
	}

	public void setCodiceNotifica(String codiceNotifica) {
		this.codiceNotifica = codiceNotifica;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getDescrizioneNotifica() {
		return descrizioneNotifica;
	}

	public void setDescrizioneNotifica(String descrizioneNotifica) {
		this.descrizioneNotifica = descrizioneNotifica;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ModelRicezioneSegnalazione [numeroDomanda=");
		builder.append(numeroDomanda);
		builder.append(", codiceNotifica=");
		builder.append(codiceNotifica);
		builder.append(", data=");
		builder.append(data);
		builder.append(", descrizioneNotifica=");
		builder.append(descrizioneNotifica);
		builder.append("]");
		return builder.toString();
	}

}
