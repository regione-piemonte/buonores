/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "anagrafica_persona_fisica")
@XmlType(propOrder = { "codiceFiscaleImpresa", "tipoPersonaFisica", "codiceFiscaleSoggetto", "cognome", "nome",
		"dataNascita", "statoNascita",
		"codiceComuneNascita", "comuneNascita", "statoIndirizzo", "codStatoIndirizzo", "codiceComuneIndirizzo",
		"comuneIndirizzo", "capIndirizzo", "indirizzo" })
public class ModelAnagraficaPersonaFisica {

	private String codiceFiscaleImpresa;
	private String tipoPersonaFisica;
	private String codiceFiscaleSoggetto;
	private String cognome;
	private String nome;
	private String dataNascita;
	private String statoNascita;
	private String codiceComuneNascita;
	private String comuneNascita;
	private String statoIndirizzo;
	private String codStatoIndirizzo;
	private String codiceComuneIndirizzo;
	private String comuneIndirizzo;
	private String capIndirizzo;
	private String indirizzo;

	public ModelAnagraficaPersonaFisica() {
	}

	@XmlElement(name = "codice_fiscale_impresa")
	public String getCodiceFiscaleImpresa() {
		return codiceFiscaleImpresa;
	}

	public void setCodiceFiscaleImpresa(String codiceFiscaleImpresa) {
		this.codiceFiscaleImpresa = codiceFiscaleImpresa;
	}

	@XmlElement(name = "tipo_personafisica")
	public String getTipoPersonaFisica() {
		return tipoPersonaFisica;
	}

	public void setTipoPersonaFisica(String tipoPersonaFisica) {
		this.tipoPersonaFisica = tipoPersonaFisica;
	}

	@XmlElement(name = "codice_fiscale_soggetto")
	public String getCodiceFiscaleSoggetto() {
		return codiceFiscaleSoggetto;
	}

	public void setCodiceFiscaleSoggetto(String codiceFiscaleSoggetto) {
		this.codiceFiscaleSoggetto = codiceFiscaleSoggetto;
	}

	@XmlElement(name = "cognome")
	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	@XmlElement(name = "nome")
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@XmlElement(name = "data_nascita")
	public String getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(String dataNascita) {
		this.dataNascita = dataNascita;
	}

	@XmlElement(name = "stato_nascita")
	public String getStatoNascita() {
		return statoNascita;
	}

	public void setStatoNascita(String statoNascita) {
		this.statoNascita = statoNascita;
	}

	@XmlElement(name = "codice_comune_nascita")
	public String getCodiceComuneNascita() {
		return codiceComuneNascita;
	}

	public void setCodiceComuneNascita(String codiceComuneNascita) {
		this.codiceComuneNascita = codiceComuneNascita;
	}

	@XmlElement(name = "comune_nascita")
	public String getComuneNascita() {
		return comuneNascita;
	}

	public void setComuneNascita(String comuneNascita) {
		this.comuneNascita = comuneNascita;
	}

	@XmlElement(name = "stato_indirizzo")
	public String getStatoIndirizzo() {
		return statoIndirizzo;
	}

	public void setStatoIndirizzo(String statoIndirizzo) {
		this.statoIndirizzo = statoIndirizzo;
	}

	@XmlElement(name = "cod_stato_indirizzo")
	public String getCodStatoIndirizzo() {
		return codStatoIndirizzo;
	}

	public void setCodStatoIndirizzo(String codStatoIndirizzo) {
		this.codStatoIndirizzo = codStatoIndirizzo;
	}

	@XmlElement(name = "codice_comune_indirizzo")
	public String getCodiceComuneIndirizzo() {
		return codiceComuneIndirizzo;
	}

	public void setCodiceComuneIndirizzo(String codiceComuneIndirizzo) {
		this.codiceComuneIndirizzo = codiceComuneIndirizzo;
	}

	@XmlElement(name = "comune_indirizzo")
	public String getComuneIndirizzo() {
		return comuneIndirizzo;
	}

	public void setComuneIndirizzo(String comuneIndirizzo) {
		this.comuneIndirizzo = comuneIndirizzo;
	}

	@XmlElement(name = "cap_indirizzo")
	public String getCapIndirizzo() {
		return capIndirizzo;
	}

	public void setCapIndirizzo(String capIndirizzo) {
		this.capIndirizzo = capIndirizzo;
	}

	@XmlElement(name = "indirizzo")
	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

}
