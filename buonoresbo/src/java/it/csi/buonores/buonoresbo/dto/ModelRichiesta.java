/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

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
	private String studioDestinatariodesc = null;
	private String lavoroDestinatario = null;
	private ModelBozzaRichiestaDomicilioDestinatario domicilioDestinatario = null;
	private String aslDestinatario = null;
	private String aslDestinatarioDesc = null;
	private String delegaCod = null;
	private String delegaDesc = null;
	private Boolean attestazioneIsee = null;
	private BigDecimal punteggioBisognoSociale = null;
	private String valutazioneMultidimensionale = null;
	private ModelRichiestaContratto contratto = null;
	private Boolean nessunaIncompatibilita = null;
	private List<String> rettificare = new ArrayList<String>();
	private List<ModelAllegato> allegati = new ArrayList<ModelAllegato>();
	private String contributoTipoDesc = null;
	private String domandaStatoDesc = null;
	private String areaDesc = null;
	private ModelIsee isee = null;
	private ModelVerifiche verifiche = null;
	private String noteInterna = null;
	private BigDecimal domandaDetId = null;
	private BigDecimal sportelloId = null;
	private Contact contatto = null;
	private String noteRichiedente = null;
	private Date dataDecesso = null;
	private Date dataResidenzaExtraregione = null;

	@JsonProperty("note_richiedente")
	public String getNoteRichiedente() {
		return noteRichiedente;
	}

	public void setNoteRichiedente(String noteRichiedente) {
		this.noteRichiedente = noteRichiedente;
	}

	@JsonProperty("contatto")
	public Contact getContatto() {
		return contatto;
	}

	public void setContatto(Contact contatto) {
		this.contatto = contatto;
	}

	@JsonProperty("domanda_det_id")
	public BigDecimal getDomandaDetId() {
		return domandaDetId;
	}

	public void setDomandaDetId(BigDecimal domandaDetId) {
		this.domandaDetId = domandaDetId;
	}

	@JsonProperty("verifiche")

	public ModelVerifiche getVerifiche() {
		return verifiche;
	}

	public void setVerifiche(ModelVerifiche verifiche) {
		this.verifiche = verifiche;
	}

	@JsonProperty("protocollo")

	public ModelProtocollo getProtocollo() {
		return protocollo;
	}

	public void setProtocollo(ModelProtocollo protocollo) {
		this.protocollo = protocollo;
	}

	@JsonProperty("contributo_tipo_desc")
	public String getContributoTipoDesc() {
		return contributoTipoDesc;
	}

	public void setContributoTipoDesc(String contributoTipoDesc) {
		this.contributoTipoDesc = contributoTipoDesc;
	}

	@JsonProperty("domanda_stato_desc")
	public String getDomandaStatoDesc() {
		return domandaStatoDesc;
	}

	public void setDomandaStatoDesc(String domandaStatoDesc) {
		this.domandaStatoDesc = domandaStatoDesc;
	}

	@JsonProperty("area_desc")
	public String getAreaDesc() {
		return areaDesc;
	}

	public void setAreaDesc(String areaDesc) {
		this.areaDesc = areaDesc;
	}

	@JsonProperty("isee")
	public ModelIsee getIsee() {
		return isee;
	}

	public void setIsee(ModelIsee isee) {
		this.isee = isee;
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

	/**
	 **/

	@JsonProperty("richiedente")

	public ModelPersona getRichiedente() {
		return richiedente;
	}

	public void setRichiedente(ModelPersona richiedente) {
		this.richiedente = richiedente;
	}

	/**
	 **/

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

	@JsonProperty("studio_destinatario_desc")

	public String getStudioDestinatarioDesc() {
		return studioDestinatariodesc;
	}

	public void setStudioDestinatarioDesc(String studioDestinatarioDesc) {
		this.studioDestinatariodesc = studioDestinatarioDesc;
	}

	/**
	 * il destinatario è lavorativamente attivo? (da popolare se l&#39;età del
	 * destinatario è compresa tra 18 e 65 anni)
	 **/

	@JsonProperty("lavoro_destinatario")

	public String getLavoroDestinatario() {
		return lavoroDestinatario;
	}

	public void setLavoroDestinatario(String lavoroDestinatario) {
		this.lavoroDestinatario = lavoroDestinatario;
	}

	/**
	 **/

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

	@JsonProperty("asl_destinatario_desc")

	public String getAslDestinatarioDesc() {
		return aslDestinatarioDesc;
	}

	public void setAslDestinatarioDesc(String aslDestinatarioDesc) {
		this.aslDestinatarioDesc = aslDestinatarioDesc;
	}

	/**
	 * eventuale relazione fra richiedente e destinatario (codice ottenuto dal
	 * servizio decodifiche)
	 **/

	@JsonProperty("delega_cod")

	public String getDelegaCod() {
		return delegaCod;
	}

	public void setDelegaCod(String delegaCod) {
		this.delegaCod = delegaCod;
	}

	@JsonProperty("delega_desc")

	public String getDelegaDesc() {
		return delegaDesc;
	}

	public void setDelegaDesc(String delegaDesc) {
		this.delegaDesc = delegaDesc;
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

	/**
	 **/

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

	/**
	 **/

	@JsonProperty("allegati")

	public List<ModelAllegato> getAllegati() {
		return allegati;
	}

	public void setAllegati(List<ModelAllegato> allegati) {
		this.allegati = allegati;
	}

	@JsonProperty("nota_interna")
	public String getNoteInterna() {
		return noteInterna;
	}

	public void setNoteInterna(String noteInterna) {
		this.noteInterna = noteInterna;
	}

	public BigDecimal getSportelloId() {
		return sportelloId;
	}

	public void setSportelloId(BigDecimal sportelloId) {
		this.sportelloId = sportelloId;
	}

	public Date getDataDecesso() {
		return dataDecesso;
	}

	public void setDataDecesso(Date dataDecesso) {
		this.dataDecesso = dataDecesso;
	}

	public Date getDataResidenzaExtraregione() {
		return dataResidenzaExtraregione;
	}

	public void setDataResidenzaExtraregione(Date dataResidenzaExtraregione) {
		this.dataResidenzaExtraregione = dataResidenzaExtraregione;
	}

	@Override
	public int hashCode() {
		return Objects.hash(allegati, areaDesc, aslDestinatario, attestazioneIsee, contratto, contributoTipoDesc,
				dataAggiornamento, delegaCod, delegaDesc, destinatario, domandaStatoDesc, domicilioDestinatario, isee,
				lavoroDestinatario, nessunaIncompatibilita, note, noteInterna, numero, protocollo,
				punteggioBisognoSociale, rettificare, richiedente, stato, studioDestinatario,
				valutazioneMultidimensionale, verifiche, dataDecesso, dataResidenzaExtraregione);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModelRichiesta other = (ModelRichiesta) obj;
		return Objects.equals(allegati, other.allegati) && Objects.equals(areaDesc, other.areaDesc)
				&& Objects.equals(aslDestinatario, other.aslDestinatario)
				&& Objects.equals(attestazioneIsee, other.attestazioneIsee)
				&& Objects.equals(contratto, other.contratto)
				&& Objects.equals(contributoTipoDesc, other.contributoTipoDesc)
				&& Objects.equals(dataAggiornamento, other.dataAggiornamento)
				&& Objects.equals(delegaCod, other.delegaCod) && Objects.equals(delegaDesc, other.delegaDesc)
				&& Objects.equals(destinatario, other.destinatario)
				&& Objects.equals(domandaStatoDesc, other.domandaStatoDesc)
				&& Objects.equals(domicilioDestinatario, other.domicilioDestinatario)
				&& Objects.equals(isee, other.isee) && Objects.equals(lavoroDestinatario, other.lavoroDestinatario)
				&& Objects.equals(nessunaIncompatibilita, other.nessunaIncompatibilita)
				&& Objects.equals(note, other.note) && Objects.equals(noteInterna, other.noteInterna)
				&& Objects.equals(numero, other.numero) && Objects.equals(protocollo, other.protocollo)
				&& Objects.equals(punteggioBisognoSociale, other.punteggioBisognoSociale)
				&& Objects.equals(rettificare, other.rettificare) && Objects.equals(richiedente, other.richiedente)
				&& Objects.equals(stato, other.stato) && Objects.equals(studioDestinatario, other.studioDestinatario)
				&& Objects.equals(valutazioneMultidimensionale, other.valutazioneMultidimensionale)
				&& Objects.equals(verifiche, other.verifiche)
				&& Objects.equals(dataDecesso, other.dataDecesso)
				&& Objects.equals(dataResidenzaExtraregione, other.dataResidenzaExtraregione);
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
		sb.append("    delegaCod: ").append(toIndentedString(delegaCod)).append("\n");
		sb.append("    delegaDesc: ").append(toIndentedString(delegaDesc)).append("\n");
		sb.append("    attestazioneIsee: ").append(toIndentedString(attestazioneIsee)).append("\n");
		sb.append("    punteggioBisognoSociale: ").append(toIndentedString(punteggioBisognoSociale)).append("\n");
		sb.append("    valutazioneMultidimensionale: ").append(toIndentedString(valutazioneMultidimensionale))
				.append("\n");
		sb.append("    contratto: ").append(toIndentedString(contratto)).append("\n");
		sb.append("    nessunaIncompatibilita: ").append(toIndentedString(nessunaIncompatibilita)).append("\n");
		sb.append("    rettificare: ").append(toIndentedString(rettificare)).append("\n");
		sb.append("    contributoTipoDesc: ").append(toIndentedString(contributoTipoDesc)).append("\n");
		sb.append("    domandaStatoDesc: ").append(toIndentedString(domandaStatoDesc)).append("\n");
		sb.append("    areaDesc: ").append(toIndentedString(areaDesc)).append("\n");
		sb.append("    isee: ").append(toIndentedString(isee)).append("\n");
		sb.append("    verifiche: ").append(toIndentedString(verifiche)).append("\n");
		sb.append("    noteInterna: ").append(toIndentedString(noteInterna)).append("\n");
		sb.append("    dataDecesso: ").append(toIndentedString(dataDecesso)).append("\n");
		sb.append("    dataResidenzaExtraregione: ").append(toIndentedString(dataResidenzaExtraregione)).append("\n");
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
