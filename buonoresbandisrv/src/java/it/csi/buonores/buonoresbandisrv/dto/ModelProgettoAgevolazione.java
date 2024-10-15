/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "anagrafica_persona_fisica")
@XmlType(propOrder = { "dataPresentazione",
		"oraPresentazione",
		"partitaivaUnitalocale",
		"sedeNonAttivataInPiemonte",
		"codiceAteco",
		"descrizioneAttivitaUnitalocale",
		"codiceSettoreUnitalocale",
		"descrizioneSettoreUnitalocale",
		"statoUnitalocale",
		"codStatoUnitalocale",
		"capUnitalocale",
		"codiceComuneUnitalocale",
		"comuneUnitalocale",
		"provinciaUnitalocale",
		"modalitaRegistrazione",
		"indirizzoUnitalocale",
		"titoloProgetto",
		"descrizioneProgetto",
		"abstractProgetto",
		"contributoRichiesto",
		"finanziamentoRichiesto",
		"spesaAmmessa",
		"finanziamentoBancario",
		"codSportello",
		"codAreaTerritoriale",
		"dataConcessione" })

public class ModelProgettoAgevolazione {

	private String dataPresentazione;
	private String oraPresentazione;
	private String partitaivaUnitalocale;
	private String sedeNonAttivataInPiemonte;
	private String codiceAteco;
	private String descrizioneAttivitaUnitalocale;
	private String codiceSettoreUnitalocale;
	private String descrizioneSettoreUnitalocale;
	private String statoUnitalocale;
	private String codStatoUnitalocale;
	private String capUnitalocale;
	private String codiceComuneUnitalocale;
	private String comuneUnitalocale;
	private String provinciaUnitalocale;
	private String modalitaRegistrazione;
	private String indirizzoUnitalocale;
	private String titoloProgetto;
	private String descrizioneProgetto;
	private String abstractProgetto;
	private String contributoRichiesto;
	private String finanziamentoRichiesto;
	private String spesaAmmessa;
	private String finanziamentoBancario;
	private String codSportello;
	private String codAreaTerritoriale;
	private String dataConcessione;

	public ModelProgettoAgevolazione() {
	}

	@XmlElement(name = "cod_sportello")
	public String getCodSportello() {
		return codSportello;
	}

	public void setCodSportello(String codSportello) {
		this.codSportello = codSportello;
	}

	@XmlElement(name = "cod_area_territoriale")
	public String getCodAreaTerritoriale() {
		return codAreaTerritoriale;
	}

	public void setCodAreaTerritoriale(String codAreaTerritoriale) {
		this.codAreaTerritoriale = codAreaTerritoriale;
	}

	@XmlElement(name = "data_presentazione")
	public String getDataPresentazione() {
		return dataPresentazione;
	}

	public void setDataPresentazione(String dataPresentazione) {
		this.dataPresentazione = dataPresentazione;
	}

	@XmlElement(name = "ora_presentazione")
	public String getOraPresentazione() {
		return oraPresentazione;
	}

	public void setOraPresentazione(String oraPresentazione) {
		this.oraPresentazione = oraPresentazione;
	}

	@XmlElement(name = "partitaiva_unitalocale")
	public String getPartitaivaUnitalocale() {
		return partitaivaUnitalocale;
	}

	public void setPartitaivaUnitalocale(String partitaivaUnitalocale) {
		this.partitaivaUnitalocale = partitaivaUnitalocale;
	}

	@XmlElement(name = "sede_non_attivata_in_piemonte")
	public String getSedeNonAttivataInPiemonte() {
		return sedeNonAttivataInPiemonte;
	}

	public void setSedeNonAttivataInPiemonte(String sedeNonAttivataInPiemonte) {
		this.sedeNonAttivataInPiemonte = sedeNonAttivataInPiemonte;
	}

	@XmlElement(name = "codice_ateco")
	public String getCodiceAteco() {
		return codiceAteco;
	}

	public void setCodiceAteco(String codiceAteco) {
		this.codiceAteco = codiceAteco;
	}

	@XmlElement(name = "descrizione_attivita_unitalocale")
	public String getDescrizioneAttivitaUnitalocale() {
		return descrizioneAttivitaUnitalocale;
	}

	public void setDescrizioneAttivitaUnitalocale(String descrizioneAttivitaUnitalocale) {
		this.descrizioneAttivitaUnitalocale = descrizioneAttivitaUnitalocale;
	}

	@XmlElement(name = "codice_settore_unitalocale")
	public String getCodiceSettoreUnitalocale() {
		return codiceSettoreUnitalocale;
	}

	public void setCodiceSettoreUnitalocale(String codiceSettoreUnitalocale) {
		this.codiceSettoreUnitalocale = codiceSettoreUnitalocale;
	}

	@XmlElement(name = "descrizione_settore_unitalocale")
	public String getDescrizioneSettoreUnitalocale() {
		return descrizioneSettoreUnitalocale;
	}

	public void setDescrizioneSettoreUnitalocale(String descrizioneSettoreUnitalocale) {
		this.descrizioneSettoreUnitalocale = descrizioneSettoreUnitalocale;
	}

	@XmlElement(name = "stato_unitalocale")
	public String getStatoUnitalocale() {
		return statoUnitalocale;
	}

	public void setStatoUnitalocale(String statoUnitalocale) {
		this.statoUnitalocale = statoUnitalocale;
	}

	@XmlElement(name = "cod_stato_unitalocale")
	public String getCodStatoUnitalocale() {
		return codStatoUnitalocale;
	}

	public void setCodStatoUnitalocale(String codStatoUnitalocale) {
		this.codStatoUnitalocale = codStatoUnitalocale;
	}

	@XmlElement(name = "cap_unitalocale")
	public String getCapUnitalocale() {
		return capUnitalocale;
	}

	public void setCapUnitalocale(String capUnitalocale) {
		this.capUnitalocale = capUnitalocale;
	}

	@XmlElement(name = "codice_comune_unitalocale")
	public String getCodiceComuneUnitalocale() {
		return codiceComuneUnitalocale;
	}

	public void setCodiceComuneUnitalocale(String codiceComuneUnitalocale) {
		this.codiceComuneUnitalocale = codiceComuneUnitalocale;
	}

	@XmlElement(name = "comune_unitalocale")
	public String getComuneUnitalocale() {
		return comuneUnitalocale;
	}

	public void setComuneUnitalocale(String comuneUnitalocale) {
		this.comuneUnitalocale = comuneUnitalocale;
	}

	@XmlElement(name = "provincia_unitalocale")
	public String getProvinciaUnitalocale() {
		return provinciaUnitalocale;
	}

	public void setProvinciaUnitalocale(String provinciaUnitalocale) {
		this.provinciaUnitalocale = provinciaUnitalocale;
	}

	@XmlElement(name = "modalita_registrazione")
	public String getModalitaRegistrazione() {
		return modalitaRegistrazione;
	}

	public void setModalitaRegistrazione(String modalitaRegistrazione) {
		this.modalitaRegistrazione = modalitaRegistrazione;
	}

	@XmlElement(name = "indirizzo_unitalocale")
	public String getIndirizzoUnitalocale() {
		return indirizzoUnitalocale;
	}

	public void setIndirizzoUnitalocale(String indirizzoUnitalocale) {
		this.indirizzoUnitalocale = indirizzoUnitalocale;
	}

	@XmlElement(name = "titolo_progetto")
	public String getTitoloProgetto() {
		return titoloProgetto;
	}

	public void setTitoloProgetto(String titoloProgetto) {
		this.titoloProgetto = titoloProgetto;
	}

	@XmlElement(name = "descrizione_progetto")
	public String getDescrizioneProgetto() {
		return descrizioneProgetto;
	}

	public void setDescrizioneProgetto(String descrizioneProgetto) {
		this.descrizioneProgetto = descrizioneProgetto;
	}

	@XmlElement(name = "abstract_progetto")
	public String getAbstractProgetto() {
		return abstractProgetto;
	}

	public void setAbstractProgetto(String abstractProgetto) {
		this.abstractProgetto = abstractProgetto;
	}

	@XmlElement(name = "contributo_richiesto")
	public String getContributoRichiesto() {
		return contributoRichiesto;
	}

	public void setContributoRichiesto(String contributoRichiesto) {
		this.contributoRichiesto = contributoRichiesto;
	}

	@XmlElement(name = "finanziamento_richiesto")
	public String getFinanziamentoRichiesto() {
		return finanziamentoRichiesto;
	}

	public void setFinanziamentoRichiesto(String finanziamentoRichiesto) {
		this.finanziamentoRichiesto = finanziamentoRichiesto;
	}

	@XmlElement(name = "spesa_ammessa")
	public String getSpesaAmmessa() {
		return spesaAmmessa;
	}

	public void setSpesaAmmessa(String spesaAmmessa) {
		this.spesaAmmessa = spesaAmmessa;
	}

	@XmlElement(name = "finanziamento_bancario")
	public String getFinanziamentoBancario() {
		return finanziamentoBancario;
	}

	public void setFinanziamentoBancario(String finanziamentoBancario) {
		this.finanziamentoBancario = finanziamentoBancario;
	}

	@XmlElement(name = "data_concessione")
	public String getDataConcessione() {
		return dataConcessione;
	}

	public void setDataConcessione(String dataConcessione) {
		this.dataConcessione = dataConcessione;
	}
}
