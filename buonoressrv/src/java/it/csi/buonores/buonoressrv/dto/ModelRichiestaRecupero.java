/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.dto;

import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelRichiestaRecupero {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private String numero = null;
	private BigDecimal domandaDetId = null;
	private String note = null;

	@JsonProperty("numero")

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@JsonProperty("note")

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@JsonProperty("domanda_det_id")
	public BigDecimal getDomandaDetId() {
		return domandaDetId;
	}

	public void setDomandaDetId(BigDecimal domandaDetId) {
		this.domandaDetId = domandaDetId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(domandaDetId, numero, note);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModelRichiestaRecupero other = (ModelRichiestaRecupero) obj;
		return Objects.equals(domandaDetId, other.domandaDetId) && Objects.equals(numero, other.numero)
				&& Objects.equals(note, other.note);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ModelRichiesta {\n");
		sb.append("    numero: ").append(toIndentedString(numero)).append("\n");
		sb.append("    domandaDetId: ").append(toIndentedString(domandaDetId)).append("\n");
		sb.append("    note: ").append(toIndentedString(note)).append("\n");
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
