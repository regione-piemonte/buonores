/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.DecimalMin;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelRichiesta {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private ModelProtocollo protocollo = null;
	private String numero = null;
	private String stato = null;
	private Date dataAggiornamento = null;
	private ModelPersona richiedente = null;
	private ModelPersona destinatario = null;
	private String note = null;
	private String studioDestinatario = null;
	private Boolean lavoroDestinatario = null;
	private ModelBozzaRichiestaDomicilioDestinatario domicilioDestinatario = null;
	private String aslDestinatario = null;
	private String delega = null;
	private Boolean attestazioneIsee = null;
	private BigDecimal punteggioBisognoSociale = null;
	private String valutazioneMultidimensionale = null;
	private ModelRichiestaContratto contratto = null;
	private Boolean nessunaIncompatibilita = null;
	private List<String> rettificare = new ArrayList<String>();
	private List<ModelAllegato> allegati = new ArrayList<ModelAllegato>();
	// MODIFICA TAG 03 buonodom
	private String noteRichiedente = null;

	@JsonProperty("protocollo")
	public ModelProtocollo getProtocollo() {
		return protocollo;
	}

	public void setProtocollo(ModelProtocollo protocollo) {
		this.protocollo = protocollo;
	}

	/**
	 * il numero della richiesta
	 **/

	@JsonProperty("numero")
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	/**
	 * codice stato della richiesta
	 **/

	@JsonProperty("stato")
	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	/**
	 * data di ultimo aggiornamento della richiesta
	 **/

	@JsonProperty("data_aggiornamento")
	public Date getDataAggiornamento() {
		return dataAggiornamento;
	}

	public void setDataAggiornamento(Date dataAggiornamento) {
		this.dataAggiornamento = dataAggiornamento;
	}

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
	// tipo rapporto
	public String getDelega() {
		return delega;
	}

	public void setDelega(String delega) {
		this.delega = delega;
	}

	/**
	 * ISEE inferiore a 50k (65k per minori/disabili)
	 **/

	@JsonProperty("attestazione_isee")
	public Boolean isAttestazioneIsee() {
		return attestazioneIsee;
	}

	public void setAttestazioneIsee(Boolean attestazioneIsee) {
		this.attestazioneIsee = attestazioneIsee;
	}

	/**
	 * dichiarazione di bisogno sociale pari a... minimum: 7
	 **/

	@JsonProperty("punteggio_bisogno_sociale")
	@DecimalMin("7")
	public BigDecimal getPunteggioBisognoSociale() {
		return punteggioBisognoSociale;
	}

	public void setPunteggioBisognoSociale(BigDecimal punteggioBisognoSociale) {
		this.punteggioBisognoSociale = punteggioBisognoSociale;
	}

	/**
	 * codice della tipologia valutazione multidimensionale (UVG o UMVD)
	 **/

	@JsonProperty("valutazione_multidimensionale")
	public String getValutazioneMultidimensionale() {
		return valutazioneMultidimensionale;
	}

	public void setValutazioneMultidimensionale(String valutazioneMultidimensionale) {
		this.valutazioneMultidimensionale = valutazioneMultidimensionale;
	}

	@JsonProperty("contratto")
	public ModelRichiestaContratto getContratto() {
		return contratto;
	}

	public void setContratto(ModelRichiestaContratto contratto) {
		this.contratto = contratto;
	}

	/**
	 * true nessuna incompatibilità (o impegno a rimuovere incompatibilità) false
	 * domanda non accettabile se non in bozza
	 **/

	@JsonProperty("nessuna_incompatibilita")
	public Boolean isNessunaIncompatibilita() {
		return nessunaIncompatibilita;
	}

	public void setNessunaIncompatibilita(Boolean nessunaIncompatibilita) {
		this.nessunaIncompatibilita = nessunaIncompatibilita;
	}

	/**
	 * eventuale elenco campi da rettificare
	 **/

	@JsonProperty("rettificare")
	public List<String> getRettificare() {
		return rettificare;
	}

	public void setRettificare(List<String> rettificare) {
		this.rettificare = rettificare;
	}

	@JsonProperty("allegati")
	public List<ModelAllegato> getAllegati() {
		return allegati;
	}

	public void setAllegati(List<ModelAllegato> allegati) {
		this.allegati = allegati;
	}

	@JsonProperty("note_richiedente")
	public String getNoteRichiedente() {
		return noteRichiedente;
	}

	public void setNoteRichiedente(String noteRichiedente) {
		this.noteRichiedente = noteRichiedente;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ModelRichiesta modelRichiesta = (ModelRichiesta) o;
		return Objects.equals(protocollo, modelRichiesta.protocollo) && Objects.equals(numero, modelRichiesta.numero)
				&& Objects.equals(stato, modelRichiesta.stato)
				&& Objects.equals(dataAggiornamento, modelRichiesta.dataAggiornamento)
				&& Objects.equals(richiedente, modelRichiesta.richiedente)
				&& Objects.equals(destinatario, modelRichiesta.destinatario)
				&& Objects.equals(note, modelRichiesta.note)
				&& Objects.equals(studioDestinatario, modelRichiesta.studioDestinatario)
				&& Objects.equals(lavoroDestinatario, modelRichiesta.lavoroDestinatario)
				&& Objects.equals(domicilioDestinatario, modelRichiesta.domicilioDestinatario)
				&& Objects.equals(aslDestinatario, modelRichiesta.aslDestinatario)
				&& Objects.equals(delega, modelRichiesta.delega)
				&& Objects.equals(attestazioneIsee, modelRichiesta.attestazioneIsee)
				&& Objects.equals(punteggioBisognoSociale, modelRichiesta.punteggioBisognoSociale)
				&& Objects.equals(valutazioneMultidimensionale, modelRichiesta.valutazioneMultidimensionale)
				&& Objects.equals(contratto, modelRichiesta.contratto)
				&& Objects.equals(nessunaIncompatibilita, modelRichiesta.nessunaIncompatibilita)
				&& Objects.equals(rettificare, modelRichiesta.rettificare)
				&& Objects.equals(allegati, modelRichiesta.allegati);
	}

	@Override
	public int hashCode() {
		return Objects.hash(protocollo, numero, stato, dataAggiornamento, richiedente, destinatario, note,
				studioDestinatario, lavoroDestinatario, domicilioDestinatario, aslDestinatario, delega,
				attestazioneIsee, punteggioBisognoSociale, valutazioneMultidimensionale, contratto,
				nessunaIncompatibilita, rettificare, allegati);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ModelRichiesta {\n");

		sb.append("    protocollo: ").append(toIndentedString(protocollo)).append("\n");
		sb.append("    numero: ").append(toIndentedString(numero)).append("\n");
		sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
		sb.append("    dataAggiornamento: ").append(toIndentedString(dataAggiornamento)).append("\n");
		sb.append("    richiedente: ").append(toIndentedString(richiedente)).append("\n");
		sb.append("    destinatario: ").append(toIndentedString(destinatario)).append("\n");
		sb.append("    note: ").append(toIndentedString(note)).append("\n");
		sb.append("    studioDestinatario: ").append(toIndentedString(studioDestinatario)).append("\n");
		sb.append("    lavoroDestinatario: ").append(toIndentedString(lavoroDestinatario)).append("\n");
		sb.append("    domicilioDestinatario: ").append(toIndentedString(domicilioDestinatario)).append("\n");
		sb.append("    aslDestinatario: ").append(toIndentedString(aslDestinatario)).append("\n");
		sb.append("    delega: ").append(toIndentedString(delega)).append("\n");
		sb.append("    attestazioneIsee: ").append(toIndentedString(attestazioneIsee)).append("\n");
		sb.append("    punteggioBisognoSociale: ").append(toIndentedString(punteggioBisognoSociale)).append("\n");
		sb.append("    valutazioneMultidimensionale: ").append(toIndentedString(valutazioneMultidimensionale))
				.append("\n");
		sb.append("    contratto: ").append(toIndentedString(contratto)).append("\n");
		sb.append("    nessunaIncompatibilita: ").append(toIndentedString(nessunaIncompatibilita)).append("\n");
		sb.append("    rettificare: ").append(toIndentedString(rettificare)).append("\n");
		sb.append("    allegati: ").append(toIndentedString(allegati)).append("\n");
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
