/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "anagrafica_persona_fisica")
@XmlType(propOrder = { "codTipoUtente",
		"tipoUtente",
		"tipologiaEnte",
		"codiceDipartimento",
		"descrizioneDipartimento",
		"formaGiuridicaImpresa",
		"codFormaGiuridica",
		"codiceFiscaleImpresa",
		"ragioneSocialeImpresa",
		"partitaivaSedelegale",
		"codiceAteco",
		"statoSedelegale",
		"codStatoSedelegale",
		"tipologiaRegistroIscrizione",
		"iscrizioneIncorso",
		"cin",
		"abi",
		"cab",
		"iban",
		"bic",
		"nContocorrente",
		"ruoloBeneficiario",
		"classificazioneEnte",
		"cognomePersonaFisica",
		"nomePersonaFisica",
		"statoNascitaPersonaFisica",
		"provinciaNascitaPersonaFisica",
		"provinciaNascitaDescrizionePersonaFisica",
		"codiceComuneNascitaPersonaFisica",
		"comuneNascitaPersonaFisica",
		"statoEsteroNascitaPersonaFisica",
		"dataNascitaPersonaFisica",
		"codiceComuneSedelegale",
		"comuneSedelegale",
		"indirizzoSedelegale",
		"capSedelegale",
		"telefonoSedelegale",
		"emailSedelegale",
		"pecSedelegale" })

public class ModelAnagraficaBeneficiario {

	private String codTipoUtente;
	private String tipoUtente;
	private String tipologiaEnte;
	private String codiceDipartimento;
	private String descrizioneDipartimento;
	private String formaGiuridicaImpresa;
	private String codFormaGiuridica;
	private String codiceFiscaleImpresa;
	private String ragioneSocialeImpresa;
	private String partitaivaSedelegale;
	private String codiceAteco;
	private String statoSedelegale;
	private String codStatoSedelegale;
	private String tipologiaRegistroIscrizione;
	private String iscrizioneIncorso;
	private String cin;
	private String abi;
	private String cab;
	private String iban;
	private String bic;
	private String nContocorrente;
	private String ruoloBeneficiario;
	private String classificazioneEnte;
	private String cognomePersonaFisica;
	private String nomePersonaFisica;
	private String statoNascitaPersonaFisica;
	private String provinciaNascitaPersonaFisica;
	private String provinciaNascitaDescrizionePersonaFisica;
	private String codiceComuneNascitaPersonaFisica;
	private String comuneNascitaPersonaFisica;
	private String statoEsteroNascitaPersonaFisica;
	private String dataNascitaPersonaFisica;
	private String codiceComuneSedelegale;
	private String comuneSedelegale;
	private String indirizzoSedelegale;
	private String capSedelegale;
	private String telefonoSedelegale;
	private String emailSedelegale;
	private String pecSedelegale;

	public ModelAnagraficaBeneficiario() {
	}

	@XmlElement(name = "cod_tipo_utente")
	public String getCodTipoUtente() {
		return codTipoUtente;
	}

	public void setCodTipoUtente(String codTipoUtente) {
		this.codTipoUtente = codTipoUtente;
	}

	@XmlElement(name = "tipo_utente")
	public String getTipoUtente() {
		return tipoUtente;
	}

	public void setTipoUtente(String tipoUtente) {
		this.tipoUtente = tipoUtente;
	}

	@XmlElement(name = "tipologia_ente")
	public String getTipologiaEnte() {
		return tipologiaEnte;
	}

	public void setTipologiaEnte(String tipologiaEnte) {
		this.tipologiaEnte = tipologiaEnte;
	}

	@XmlElement(name = "codice_dipartimento")
	public String getCodiceDipartimento() {
		return codiceDipartimento;
	}

	public void setCodiceDipartimento(String codiceDipartimento) {
		this.codiceDipartimento = codiceDipartimento;
	}

	@XmlElement(name = "descrizione_dipartimento")
	public String getDescrizioneDipartimento() {
		return descrizioneDipartimento;
	}

	public void setDescrizioneDipartimento(String descrizioneDipartimento) {
		this.descrizioneDipartimento = descrizioneDipartimento;
	}

	@XmlElement(name = "forma_giuridica_impresa")
	public String getFormaGiuridicaImpresa() {
		return formaGiuridicaImpresa;
	}

	public void setFormaGiuridicaImpresa(String formaGiuridicaImpresa) {
		this.formaGiuridicaImpresa = formaGiuridicaImpresa;
	}

	@XmlElement(name = "cod_forma_giuridica_impresa")
	public String getCodFormaGiuridica() {
		return codFormaGiuridica;
	}

	public void setCodFormaGiuridica(String codFormaGiuridica) {
		this.codFormaGiuridica = codFormaGiuridica;
	}

	@XmlElement(name = "codice_fiscale_impresa")
	public String getCodiceFiscaleImpresa() {
		return codiceFiscaleImpresa;
	}

	public void setCodiceFiscaleImpresa(String codiceFiscaleImpresa) {
		this.codiceFiscaleImpresa = codiceFiscaleImpresa;
	}

	@XmlElement(name = "ragione_sociale_impresa")
	public String getRagioneSocialeImpresa() {
		return ragioneSocialeImpresa;
	}

	public void setRagioneSocialeImpresa(String ragioneSocialeImpresa) {
		this.ragioneSocialeImpresa = ragioneSocialeImpresa;
	}

	@XmlElement(name = "partitaiva_sedelegale")
	public String getPartitaivaSedelegale() {
		return partitaivaSedelegale;
	}

	public void setPartitaivaSedelegale(String partitaivaSedelegale) {
		this.partitaivaSedelegale = partitaivaSedelegale;
	}

	@XmlElement(name = "codice_ateco")
	public String getCodiceAteco() {
		return codiceAteco;
	}

	public void setCodiceAteco(String codiceAteco) {
		this.codiceAteco = codiceAteco;
	}

	@XmlElement(name = "stato_sedelegale")
	public String getStatoSedelegale() {
		return statoSedelegale;
	}

	public void setStatoSedelegale(String statoSedelegale) {
		this.statoSedelegale = statoSedelegale;
	}

	@XmlElement(name = "cod_stato_sedelegale")
	public String getCodStatoSedelegale() {
		return codStatoSedelegale;
	}

	public void setCodStatoSedelegale(String codStatoSedelegale) {
		this.codStatoSedelegale = codStatoSedelegale;
	}

	@XmlElement(name = "tipologia_registro_iscrizione")
	public String getTipologiaRegistroIscrizione() {
		return tipologiaRegistroIscrizione;
	}

	public void setTipologiaRegistroIscrizione(String tipologiaRegistroIscrizione) {
		this.tipologiaRegistroIscrizione = tipologiaRegistroIscrizione;
	}

	@XmlElement(name = "iscrizione_incorso")
	public String getIscrizioneIncorso() {
		return iscrizioneIncorso;
	}

	public void setIscrizioneIncorso(String iscrizioneIncorso) {
		this.iscrizioneIncorso = iscrizioneIncorso;
	}

	@XmlElement(name = "Cin")
	public String getCin() {
		return cin;
	}

	public void setCin(String cin) {
		this.cin = cin;
	}

	@XmlElement(name = "Abi")
	public String getAbi() {
		return abi;
	}

	public void setAbi(String abi) {
		this.abi = abi;
	}

	@XmlElement(name = "Cab")
	public String getCab() {
		return cab;
	}

	public void setCab(String cab) {
		this.cab = cab;
	}

	@XmlElement(name = "Iban")
	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	@XmlElement(name = "bic")
	public String getBic() {
		return bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}

	@XmlElement(name = "n_contocorrente")
	public String getnContocorrente() {
		return nContocorrente;
	}

	public void setnContocorrente(String nContocorrente) {
		this.nContocorrente = nContocorrente;
	}

	@XmlElement(name = "ruolo_beneficiario")
	public String getRuoloBeneficiario() {
		return ruoloBeneficiario;
	}

	public void setRuoloBeneficiario(String ruoloBeneficiario) {
		this.ruoloBeneficiario = ruoloBeneficiario;
	}

	@XmlElement(name = "classificazione_ente")
	public String getClassificazioneEnte() {
		return classificazioneEnte;
	}

	public void setClassificazioneEnte(String classificazioneEnte) {
		this.classificazioneEnte = classificazioneEnte;
	}

	@XmlElement(name = "cognome_persona_fisica")
	public String getCognomePersonaFisica() {
		return cognomePersonaFisica;
	}

	public void setCognomePersonaFisica(String cognomePersonaFisica) {
		this.cognomePersonaFisica = cognomePersonaFisica;
	}

	@XmlElement(name = "nome_persona_fisica")
	public String getNomePersonaFisica() {
		return nomePersonaFisica;
	}

	public void setNomePersonaFisica(String nomePersonaFisica) {
		this.nomePersonaFisica = nomePersonaFisica;
	}

	@XmlElement(name = "stato_nascita_persona_fisica")
	public String getStatoNascitaPersonaFisica() {
		return statoNascitaPersonaFisica;
	}

	public void setStatoNascitaPersonaFisica(String statoNascitaPersonaFisica) {
		this.statoNascitaPersonaFisica = statoNascitaPersonaFisica;
	}

	@XmlElement(name = "provincia_nascita_persona_fisica")
	public String getProvinciaNascitaPersonaFisica() {
		return provinciaNascitaPersonaFisica;
	}

	public void setProvinciaNascitaPersonaFisica(String provinciaNascitaPersonaFisica) {
		this.provinciaNascitaPersonaFisica = provinciaNascitaPersonaFisica;
	}

	@XmlElement(name = "provincia_nascita_descrizione_persona_fisica")
	public String getProvinciaNascitaDescrizionePersonaFisica() {
		return provinciaNascitaDescrizionePersonaFisica;
	}

	public void setProvinciaNascitaDescrizionePersonaFisica(String provinciaNascitaDescrizionePersonaFisica) {
		this.provinciaNascitaDescrizionePersonaFisica = provinciaNascitaDescrizionePersonaFisica;
	}

	@XmlElement(name = "codice_comune_nascita_persona_fisica")
	public String getCodiceComuneNascitaPersonaFisica() {
		return codiceComuneNascitaPersonaFisica;
	}

	public void setCodiceComuneNascitaPersonaFisica(String codiceComuneNascitaPersonaFisica) {
		this.codiceComuneNascitaPersonaFisica = codiceComuneNascitaPersonaFisica;
	}

	@XmlElement(name = "comune_nascita_persona_fisica")
	public String getComuneNascitaPersonaFisica() {
		return comuneNascitaPersonaFisica;
	}

	public void setComuneNascitaPersonaFisica(String comuneNascitaPersonaFisica) {
		this.comuneNascitaPersonaFisica = comuneNascitaPersonaFisica;
	}

	@XmlElement(name = "stato_estero_nascita_persona_fisica")
	public String getStatoEsteroNascitaPersonaFisica() {
		return statoEsteroNascitaPersonaFisica;
	}

	public void setStatoEsteroNascitaPersonaFisica(String statoEsteroNascitaPersonaFisica) {
		this.statoEsteroNascitaPersonaFisica = statoEsteroNascitaPersonaFisica;
	}

	@XmlElement(name = "data_nascita_persona_fisica")
	public String getDataNascitaPersonaFisica() {
		return dataNascitaPersonaFisica;
	}

	public void setDataNascitaPersonaFisica(String dataNascitaPersonaFisica) {
		this.dataNascitaPersonaFisica = dataNascitaPersonaFisica;
	}

	@XmlElement(name = "codice_comune_sedelegale")
	public String getCodiceComuneSedelegale() {
		return codiceComuneSedelegale;
	}

	public void setCodiceComuneSedelegale(String codiceComuneSedelegale) {
		this.codiceComuneSedelegale = codiceComuneSedelegale;
	}

	@XmlElement(name = "comune_sedelegale")
	public String getComuneSedelegale() {
		return comuneSedelegale;
	}

	public void setComuneSedelegale(String comuneSedelegale) {
		this.comuneSedelegale = comuneSedelegale;
	}

	@XmlElement(name = "indirizzo_sedelegale")
	public String getIndirizzoSedelegale() {
		return indirizzoSedelegale;
	}

	public void setIndirizzoSedelegale(String indirizzoSedelegale) {
		this.indirizzoSedelegale = indirizzoSedelegale;
	}

	@XmlElement(name = "cap_sedelegale")
	public String getCapSedelegale() {
		return capSedelegale;
	}

	public void setCapSedelegale(String capSedelegale) {
		this.capSedelegale = capSedelegale;
	}

	@XmlElement(name = "telefono_sedelegale")
	public String getTelefonoSedelegale() {
		return telefonoSedelegale;
	}

	public void setTelefonoSedelegale(String telefonoSedelegale) {
		this.telefonoSedelegale = telefonoSedelegale;
	}

	@XmlElement(name = "email_sedelegale")
	public String getEmailSedelegale() {
		return emailSedelegale;
	}

	public void setEmailSedelegale(String emailSedelegale) {
		this.emailSedelegale = emailSedelegale;
	}

	@XmlElement(name = "pec_sedelegale")
	public String getPecSedelegale() {
		return pecSedelegale;
	}

	public void setPecSedelegale(String pecSedelegale) {
		this.pecSedelegale = pecSedelegale;
	}

}
