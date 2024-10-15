/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.dto;

import javax.xml.bind.annotation.XmlElement;

public class ModelProgettoBandi {

	private String normaIncentivazione;
	private String idProgetto;
	private String codiceAsse;
	private String descrizioneAsse;
	private String codiceMisura;
	private String descrizioneMisura;
	private String descrizioneBando;
	private String destinatarioDescrizione;
	private String destinatarioDirezione;
	private String codiceIntervento;
	private String descrizioneIntervento;
	private ModelProgettoAgevolazione progettoDiAgevolazione;
	private ModelAnagraficaBeneficiario anagraficaBeneficiario;
	private ModelAnagraficaPersonaFisica anagraficaPersonaFisica;
	private ModelSpeseProgetto speseProgetto;
	private ModelFornitore fornitore;

	public ModelProgettoBandi() {
	}

	@XmlElement(name = "norma_incentivazione")
	public String getNormaIncentivazione() {
		return normaIncentivazione;
	}

	public void setNormaIncentivazione(String normaIncentivazione) {
		this.normaIncentivazione = normaIncentivazione;
	}

	@XmlElement(name = "id_progetto")
	public String getIdProgetto() {
		return idProgetto;
	}

	public void setIdProgetto(String idProgetto) {
		this.idProgetto = idProgetto;
	}

	@XmlElement(name = "codice_asse")
	public String getCodiceAsse() {
		return codiceAsse;
	}

	public void setCodiceAsse(String codiceAsse) {
		this.codiceAsse = codiceAsse;
	}

	@XmlElement(name = "descrizione_asse")
	public String getDescrizioneAsse() {
		return descrizioneAsse;
	}

	public void setDescrizioneAsse(String descrizioneAsse) {
		this.descrizioneAsse = descrizioneAsse;
	}

	@XmlElement(name = "codice_misura")
	public String getCodiceMisura() {
		return codiceMisura;
	}

	public void setCodiceMisura(String codiceMisura) {
		this.codiceMisura = codiceMisura;
	}

	@XmlElement(name = "descrizione_misura")
	public String getDescrizioneMisura() {
		return descrizioneMisura;
	}

	public void setDescrizioneMisura(String descrizioneMisura) {
		this.descrizioneMisura = descrizioneMisura;
	}

	@XmlElement(name = "descrizione_bando")
	public String getDescrizioneBando() {
		return descrizioneBando;
	}

	public void setDescrizioneBando(String descrizioneBando) {
		this.descrizioneBando = descrizioneBando;
	}

	@XmlElement(name = "destinatario_descrizione")
	public String getDestinatarioDescrizione() {
		return destinatarioDescrizione;
	}

	public void setDestinatarioDescrizione(String destinatarioDescrizione) {
		this.destinatarioDescrizione = destinatarioDescrizione;
	}

	@XmlElement(name = "destinatario_direzione")
	public String getDestinatarioDirezione() {
		return destinatarioDirezione;
	}

	public void setDestinatarioDirezione(String destinatarioDirezione) {
		this.destinatarioDirezione = destinatarioDirezione;
	}

	@XmlElement(name = "codice_intervento")
	public String getCodiceIntervento() {
		return codiceIntervento;
	}

	public void setCodiceIntervento(String codiceIntervento) {
		this.codiceIntervento = codiceIntervento;
	}

	@XmlElement(name = "descrizione_intervento")
	public String getDescrizioneIntervento() {
		return descrizioneIntervento;
	}

	public void setDescrizioneIntervento(String descrizioneIntervento) {
		this.descrizioneIntervento = descrizioneIntervento;
	}

	@XmlElement(name = "progetto_di_agevolazione")
	public ModelProgettoAgevolazione getProgettoDiAgevolazione() {
		return progettoDiAgevolazione;
	}

	public void setProgettoDiAgevolazione(ModelProgettoAgevolazione progettoDiAgevolazione) {
		this.progettoDiAgevolazione = progettoDiAgevolazione;
	}

	@XmlElement(name = "anagrafica_beneficiario")
	public ModelAnagraficaBeneficiario getAnagraficaBeneficiario() {
		return anagraficaBeneficiario;
	}

	public void setAnagraficaBeneficiario(ModelAnagraficaBeneficiario anagraficaBeneficiario) {
		this.anagraficaBeneficiario = anagraficaBeneficiario;
	}

	@XmlElement(name = "anagrafica_persona_fisica")
	public ModelAnagraficaPersonaFisica getAnagraficaPersonaFisica() {
		return anagraficaPersonaFisica;
	}

	public void setAnagraficaPersonaFisica(ModelAnagraficaPersonaFisica anagraficaPersonaFisica) {
		this.anagraficaPersonaFisica = anagraficaPersonaFisica;
	}

	@XmlElement(name = "spese_progetto")
	public ModelSpeseProgetto getSpeseProgetto() {
		return speseProgetto;
	}

	public void setSpeseProgetto(ModelSpeseProgetto speseProgetto) {
		this.speseProgetto = speseProgetto;
	}

	@XmlElement(name = "fornitore")
	public ModelFornitore getFornitore() {
		return fornitore;
	}

	public void setFornitore(ModelFornitore fornitore) {
		this.fornitore = fornitore;
	}

}
