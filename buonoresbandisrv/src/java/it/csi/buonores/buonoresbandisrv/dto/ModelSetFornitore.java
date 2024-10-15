/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.dto;

import java.util.List;

public class ModelSetFornitore {

	private String numeroDomanda;
	private String codiceFiscale;
	private String cognome;
	private String nome;
	private String denominazione;
	private String partitaIva;
	private String codiceFormaGiuridica;
	private String dataInizio;
	private String dataFine;
	private List<ModelAllegatoContratto> files = null;

	public ModelSetFornitore() {
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getPartitaIva() {
		return partitaIva;
	}

	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public String getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(String dataInizio) {
		this.dataInizio = dataInizio;
	}

	public String getDataFine() {
		return dataFine;
	}

	public void setDataFine(String dataFine) {
		this.dataFine = dataFine;
	}

	public String getCodiceFormaGiuridica() {
		return codiceFormaGiuridica;
	}

	public void setCodiceFormaGiuridica(String codiceFormaGiuridica) {
		this.codiceFormaGiuridica = codiceFormaGiuridica;
	}

	public String getNumeroDomanda() {
		return numeroDomanda;
	}

	public void setNumeroDomanda(String numeroDomanda) {
		this.numeroDomanda = numeroDomanda;
	}

	public List<ModelAllegatoContratto> getFiles() {
		return files;
	}

	public void setFiles(List<ModelAllegatoContratto> files) {
		this.files = files;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ModelSetFornitore [numeroDomanda=");
		builder.append(numeroDomanda);
		builder.append(", codiceFiscale=");
		builder.append(codiceFiscale);
		builder.append(", cognome=");
		builder.append(cognome);
		builder.append(", nome=");
		builder.append(nome);
		builder.append(", denominazione=");
		builder.append(denominazione);
		builder.append(", partitaIva=");
		builder.append(partitaIva);
		builder.append(", codiceFormaGiuridica=");
		builder.append(codiceFormaGiuridica);
		builder.append(", dataInizio=");
		builder.append(dataInizio);
		builder.append(", dataFine=");
		builder.append(dataFine);
		builder.append(", files=");
		builder.append(files);
		builder.append("]");
		return builder.toString();
	}

	public String toStringLog() {
		StringBuilder builder = new StringBuilder();
		builder.append("ModelSetFornitore [numeroDomanda=");
		builder.append(numeroDomanda);
		builder.append(", codiceFiscale=");
		builder.append(codiceFiscale);
		builder.append(", cognome=");
		builder.append(cognome);
		builder.append(", nome=");
		builder.append(nome);
		builder.append(", denominazione=");
		builder.append(denominazione);
		builder.append(", partitaIva=");
		builder.append(partitaIva);
		builder.append(", codiceFormaGiuridica=");
		builder.append(codiceFormaGiuridica);
		builder.append(", dataInizio=");
		builder.append(dataInizio);
		builder.append(", dataFine=");
		builder.append(dataFine);
		builder.append("]");
		return builder.toString();
	}

}
