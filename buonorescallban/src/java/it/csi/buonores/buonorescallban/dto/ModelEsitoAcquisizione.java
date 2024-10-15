/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonorescallban.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelEsitoAcquisizione {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private String uuid = null;
	private String esito = null;
	private Esito errore = null;

	@JsonProperty("uuid")

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * esito pupo esssere OK o KO in caso di KO verrà vaòorizzato il tag errore
	 **/

	@JsonProperty("esito")

	public String getEsito() {
		return esito;
	}

	public void setEsito(String esito) {
		this.esito = esito;
	}

	@JsonProperty("errore")

	public Esito getErrore() {
		return errore;
	}

	public void setErrore(Esito errore) {
		this.errore = errore;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ModelEsitoAcquisizione modelEsitoAcquisizione = (ModelEsitoAcquisizione) o;
		return Objects.equals(uuid, modelEsitoAcquisizione.uuid) && Objects.equals(esito, modelEsitoAcquisizione.esito)
				&& Objects.equals(errore, modelEsitoAcquisizione.errore);
	}

	@Override
	public int hashCode() {
		return Objects.hash(uuid, esito, errore);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ModelEsitoAcquisizione {\n");

		sb.append("    uuid: ").append(toIndentedString(uuid)).append("\n");
		sb.append("    esito: ").append(toIndentedString(esito)).append("\n");
		sb.append("    errore: ").append(toIndentedString(errore)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}
