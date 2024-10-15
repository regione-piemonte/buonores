/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

public class ModelDomandeAperta {
	Integer idDomanda;
	String numeroDomanda;
	String cfDestinatario;
	String nomeDestinatario;
	String cognomeDestinatario;
	String cfRichiedente;
	String nomeRichiedente;
	String cognomeRichiedente;
	ModelStati stato;
	String dataDomanda;
	ModelEnteGestore enteGestore;
	ModelVerifiche verifiche;
	String dataVerificaBuono;
	Boolean esitoVerificaBuono;
	Integer sportelloId;
	String sportelloCod;
	String sportelloDesc;
	String decorrenzaInizio;
	String decorrenzaFine;
	String dataDecesso;
	String dataCambioResidenza;

	public Integer getIdDomanda() {
		return idDomanda;
	}

	public void setIdDomanda(Integer idDomanda) {
		this.idDomanda = idDomanda;
	}

	public String getNumeroDomanda() {
		return numeroDomanda;
	}

	public void setNumeroDomanda(String numeroDomanda) {
		this.numeroDomanda = numeroDomanda;
	}

	public String getCfDestinatario() {
		return cfDestinatario;
	}

	public void setCfDestinatario(String cfDestinatario) {
		this.cfDestinatario = cfDestinatario;
	}

	public String getNomeDestinatario() {
		return nomeDestinatario;
	}

	public void setNomeDestinatario(String nomeDestinatario) {
		this.nomeDestinatario = nomeDestinatario;
	}

	public String getCognomeDestinatario() {
		return cognomeDestinatario;
	}

	public void setCognomeDestinatario(String cognomeDestinatario) {
		this.cognomeDestinatario = cognomeDestinatario;
	}

	public String getCfRichiedente() {
		return cfRichiedente;
	}

	public void setCfRichiedente(String cfRichiedente) {
		this.cfRichiedente = cfRichiedente;
	}

	public String getNomeRichiedente() {
		return nomeRichiedente;
	}

	public void setNomeRichiedente(String nomeRichiedente) {
		this.nomeRichiedente = nomeRichiedente;
	}

	public String getCognomeRichiedente() {
		return cognomeRichiedente;
	}

	public void setCognomeRichiedente(String cognomeRichiedente) {
		this.cognomeRichiedente = cognomeRichiedente;
	}

	public ModelStati getStato() {
		return stato;
	}

	public void setStato(ModelStati stato) {
		this.stato = stato;
	}

	public String getDataDomanda() {
		return dataDomanda;
	}

	public void setDataDomanda(String dataDomanda) {
		this.dataDomanda = dataDomanda;
	}

	public ModelVerifiche getVerifiche() {
		return verifiche;
	}

	public void setVerifiche(ModelVerifiche verifiche) {
		this.verifiche = verifiche;
	}

	public ModelEnteGestore getEnteGestore() {
		return enteGestore;
	}

	public void setEnteGestore(ModelEnteGestore enteGestore) {
		this.enteGestore = enteGestore;
	}

	public String getDataVerificaBuono() {
		return dataVerificaBuono;
	}

	public void setDataVerificaBuono(String dataVerificaBuono) {
		this.dataVerificaBuono = dataVerificaBuono;
	}

	public Boolean getEsitoVerificaBuono() {
		return esitoVerificaBuono;
	}

	public void setEsitoVerificaBuono(Boolean esitoVerificaBuono) {
		this.esitoVerificaBuono = esitoVerificaBuono;
	}

	public Integer getSportelloId() {
		return sportelloId;
	}

	public void setSportelloId(Integer sportelloId) {
		this.sportelloId = sportelloId;
	}

	public String getSportelloCod() {
		return sportelloCod;
	}

	public void setSportelloCod(String sportelloCod) {
		this.sportelloCod = sportelloCod;
	}

	public String getSportelloDesc() {
		return sportelloDesc;
	}

	public void setSportelloDesc(String sportelloDesc) {
		this.sportelloDesc = sportelloDesc;
	}

	public String getDecorrenzaInizio() {
		return decorrenzaInizio;
	}

	public void setDecorrenzaInizio(String decorrenzaInizio) {
		this.decorrenzaInizio = decorrenzaInizio;
	}

	public String getDecorrenzaFine() {
		return decorrenzaFine;
	}

	public void setDecorrenzaFine(String decorrenzaFine) {
		this.decorrenzaFine = decorrenzaFine;
	}

	public String getDataDecesso() {
		return dataDecesso;
	}

	public void setDataDecesso(String dataDecesso) {
		this.dataDecesso = dataDecesso;
	}

	public String getDataCambioResidenza() {
		return dataCambioResidenza;
	}

	public void setDataCambioResidenza(String dataCambioResidenza) {
		this.dataCambioResidenza = dataCambioResidenza;
	}

	@Override
	public String toString() {
		return "ModelDomandeAperta [idDomanda="
				+ idDomanda
				+ ", numeroDomanda="
				+ numeroDomanda
				+ ", cfDestinatario="
				+ cfDestinatario
				+ ", nomeDestinatario="
				+ nomeDestinatario
				+ ", cognomeDestinatario="
				+ cognomeDestinatario
				+ ", cfRichiedente="
				+ cfRichiedente
				+ ", nomeRichiedente="
				+ nomeRichiedente
				+ ", cognomeRichiedente="
				+ cognomeRichiedente
				+ ", stato="
				+ stato
				+ ", dataDomanda="
				+ dataDomanda
				+ ", enteGestore="
				+ enteGestore
				+ ", dataVerificaBuono="
				+ dataVerificaBuono
				+ ", esitoVerificaBuono="
				+ esitoVerificaBuono
				+ ", verifiche="
				+ verifiche
				+ ", sportelloId="
				+ sportelloId
				+ ", sportelloCod="
				+ sportelloCod
				+ ", sportelloDesc="
				+ sportelloDesc
				+ ", decorrenzaInizio="
				+ decorrenzaInizio
				+ ", decorrenzaFine="
				+ decorrenzaFine
				+ ", dataDecesso="
				+ dataDecesso
				+ ", dataCambioResidenza="
				+ dataCambioResidenza
				+ "]";
	}
}
