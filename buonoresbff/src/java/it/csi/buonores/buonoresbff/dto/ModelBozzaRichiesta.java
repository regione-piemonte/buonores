/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelBozzaRichiesta {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private ModelPersona richiedente = null;
	private ModelPersona destinatario = null;
	private String note = null;
	private String studioDestinatario = null;
	private Boolean lavoroDestinatario = null;
	private ModelBozzaRichiestaDomicilioDestinatario domicilioDestinatario = null;
	private String aslDestinatario = null;
	private String delega = null;

	@JsonProperty("richiedente")
	public ModelPersona getRichiedente() {
		return richiedente;
	}

	public void setRichiedente(ModelPersona richiedente) {
		this.richiedente = richiedente;
	}

	@JsonProperty("destinatario")
	public ModelPersona getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(ModelPersona destinatario) {
		this.destinatario = destinatario;
	}

	/**
	 * eventuali note (es. in caso di attesa rettifica)
	 **/
	@JsonProperty("note")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * titolo di studio del destinatario (codice ottenuto dal servizio decodifiche)
	 **/
	@JsonProperty("studio_destinatario")
	public String getStudioDestinatario() {
		return studioDestinatario;
	}

	public void setStudioDestinatario(String studioDestinatario) {
		this.studioDestinatario = studioDestinatario;
	}

	/**
	 * il destinatario è lavorativamente attivo? (da popolare se l&#39;età del
	 * destinatario è compresa tra 18 e 65 anni)
	 **/
	@JsonProperty("lavoro_destinatario")
	public Boolean isLavoroDestinatario() {
		return lavoroDestinatario;
	}

	public void setLavoroDestinatario(Boolean lavoroDestinatario) {
		this.lavoroDestinatario = lavoroDestinatario;
	}

	@JsonProperty("domicilio_destinatario")
	public ModelBozzaRichiestaDomicilioDestinatario getDomicilioDestinatario() {
		return domicilioDestinatario;
	}

	public void setDomicilioDestinatario(ModelBozzaRichiestaDomicilioDestinatario domicilioDestinatario) {
		this.domicilioDestinatario = domicilioDestinatario;
	}

	/**
	 * codice ASL di appartenenza del destinatario
	 **/
	@JsonProperty("asl_destinatario")
	public String getAslDestinatario() {
		return aslDestinatario;
	}

	public void setAslDestinatario(String aslDestinatario) {
		this.aslDestinatario = aslDestinatario;
	}

	/**
	 * eventuale relazione fra richiedente e destinatario (codice ottenuto dal
	 * servizio decodifiche)
	 **/
	@JsonProperty("delega")
	public String getDelega() {
		return delega;
	}

	public void setDelega(String delega) {
		this.delega = delega;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ModelBozzaRichiesta modelBozzaRichiesta = (ModelBozzaRichiesta) o;
		return Objects.equals(richiedente, modelBozzaRichiesta.richiedente)
				&& Objects.equals(destinatario, modelBozzaRichiesta.destinatario)
				&& Objects.equals(note, modelBozzaRichiesta.note)
				&& Objects.equals(studioDestinatario, modelBozzaRichiesta.studioDestinatario)
				&& Objects.equals(lavoroDestinatario, modelBozzaRichiesta.lavoroDestinatario)
				&& Objects.equals(domicilioDestinatario, modelBozzaRichiesta.domicilioDestinatario)
				&& Objects.equals(aslDestinatario, modelBozzaRichiesta.aslDestinatario)
				&& Objects.equals(delega, modelBozzaRichiesta.delega);
	}

	@Override
	public int hashCode() {
		return Objects.hash(richiedente, destinatario, note, studioDestinatario, lavoroDestinatario,
				domicilioDestinatario, aslDestinatario, delega);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ModelBozzaRichiesta {\n");

		sb.append("    richiedente: ").append(toIndentedString(richiedente)).append("\n");
		sb.append("    destinatario: ").append(toIndentedString(destinatario)).append("\n");
		sb.append("    note: ").append(toIndentedString(note)).append("\n");
		sb.append("    studioDestinatario: ").append(toIndentedString(studioDestinatario)).append("\n");
		sb.append("    lavoroDestinatario: ").append(toIndentedString(lavoroDestinatario)).append("\n");
		sb.append("    domicilioDestinatario: ").append(toIndentedString(domicilioDestinatario)).append("\n");
		sb.append("    aslDestinatario: ").append(toIndentedString(aslDestinatario)).append("\n");
		sb.append("    delega: ").append(toIndentedString(delega)).append("\n");
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
