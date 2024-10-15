/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "fornitore")
@XmlType(propOrder = { "codiceFiscaleFornitore", "partitaIvaFornitore", "cognomeFornitore", "nomeFornitore",
		"qualificaFornitore", "dataInizioContratto", "dataFineContratto", "formaGiuridica", "ragioneSociale" })
public class ModelFornitore {

	private String codiceFiscaleFornitore;
	private String partitaIvaFornitore;
	private String cognomeFornitore;
	private String nomeFornitore;
	private String qualificaFornitore;
	private String dataInizioContratto;
	private String dataFineContratto;
	private String formaGiuridica;
	private String ragioneSociale;

	public ModelFornitore() {
	}

	@XmlElement(name = "CodiceFiscaleFornitore")
	public String getCodiceFiscaleFornitore() {
		return codiceFiscaleFornitore;
	}

	public void setCodiceFiscaleFornitore(String codiceFiscaleFornitore) {
		this.codiceFiscaleFornitore = codiceFiscaleFornitore;
	}

	@XmlElement(name = "PartitaIvaFornitore")
	public String getPartitaIvaFornitore() {
		return partitaIvaFornitore;
	}

	public void setPartitaIvaFornitore(String partitaIvaFornitore) {
		this.partitaIvaFornitore = partitaIvaFornitore;
	}

	@XmlElement(name = "CognomeFornitore")
	public String getCognomeFornitore() {
		return cognomeFornitore;
	}

	public void setCognomeFornitore(String cognomeFornitore) {
		this.cognomeFornitore = cognomeFornitore;
	}

	@XmlElement(name = "NomeFornitore")
	public String getNomeFornitore() {
		return nomeFornitore;
	}

	public void setNomeFornitore(String nomeFornitore) {
		this.nomeFornitore = nomeFornitore;
	}

	@XmlElement(name = "QualificaFornitore")
	public String getQualificaFornitore() {
		return qualificaFornitore;
	}

	public void setQualificaFornitore(String qualificaFornitore) {
		this.qualificaFornitore = qualificaFornitore;
	}

	@XmlElement(name = "dataInizioContratto")
	public String getDataInizioContratto() {
		return dataInizioContratto;
	}

	public void setDataInizioContratto(String dataInizioContratto) {
		this.dataInizioContratto = dataInizioContratto;
	}

	@XmlElement(name = "dataFineContratto")
	public String getDataFineContratto() {
		return dataFineContratto;
	}

	public void setDataFineContratto(String dataFineContratto) {
		this.dataFineContratto = dataFineContratto;
	}

	@XmlElement(name = "formaGiuridica")
	public String getFormaGiuridica() {
		return formaGiuridica;
	}

	public void setFormaGiuridica(String formaGiuridica) {
		this.formaGiuridica = formaGiuridica;
	}

	@XmlElement(name = "ragioneSociale")
	public String getRagioneSociale() {
		return ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}
}
