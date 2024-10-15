/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModelIntegrazioneBuonoDettaglio {
	private Integer invioPbandiId;
	private Integer integrazionePbandiId;
	private String dicSpesaIntegrazioneData;
	private String dicSpesaIntegrazioneNote;
	private String dicSpesaIntegrazioneCode;
	private String periodoInizio;
	private String periodoFine;
	private String dataScadenza;
	private String dataRisposta;
	private String rsaCod;
	private String rsaNome;
	private Integer dicSpesaId;
	private List<ModelAllegatoBuono> allegati = new ArrayList<ModelAllegatoBuono>();

	public String getDicSpesaIntegrazioneData() {
		return dicSpesaIntegrazioneData;
	}

	public void setDicSpesaIntegrazioneData(String dicSpesaIntegrazioneData) {
		this.dicSpesaIntegrazioneData = dicSpesaIntegrazioneData;
	}

	public String getDicSpesaIntegrazioneNote() {
		return dicSpesaIntegrazioneNote;
	}

	public void setDicSpesaIntegrazioneNote(String dicSpesaIntegrazioneNote) {
		this.dicSpesaIntegrazioneNote = dicSpesaIntegrazioneNote;
	}

	public String getDicSpesaIntegrazioneCode() {
		return dicSpesaIntegrazioneCode;
	}

	public void setDicSpesaIntegrazioneCode(String dicSpesaIntegrazioneCode) {
		this.dicSpesaIntegrazioneCode = dicSpesaIntegrazioneCode;
	}

	public String getPeriodoInizio() {
		return periodoInizio;
	}

	public void setPeriodoInizio(String periodoInizio) {
		this.periodoInizio = periodoInizio;
	}

	public String getPeriodoFine() {
		return periodoFine;
	}

	public void setPeriodoFine(String periodoFine) {
		this.periodoFine = periodoFine;
	}

	public Integer getDicSpesaId() {
		return dicSpesaId;
	}

	public void setDicSpesaId(Integer dicSpesaId) {
		this.dicSpesaId = dicSpesaId;
	}

	public List<ModelAllegatoBuono> getAllegati() {
		return allegati;
	}

	public void setAllegati(List<ModelAllegatoBuono> allegati) {
		this.allegati = allegati;
	}

	public Integer getInvioPbandiId() {
		return invioPbandiId;
	}

	public void setInvioPbandiId(Integer invioPbandiId) {
		this.invioPbandiId = invioPbandiId;
	}

	public Integer getIntegrazionePbandiId() {
		return integrazionePbandiId;
	}

	public void setIntegrazionePbandiId(Integer integrazionePbandiId) {
		this.integrazionePbandiId = integrazionePbandiId;
	}

	public String getDataScadenza() {
		return dataScadenza;
	}

	public void setDataScadenza(String dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	public String getDataRisposta() {
		return dataRisposta;
	}

	public void setDataRisposta(String dataRisposta) {
		this.dataRisposta = dataRisposta;
	}

	public String getRsaCod() {
		return rsaCod;
	}

	public void setRsaCod(String rsaCod) {
		this.rsaCod = rsaCod;
	}

	public String getRsaNome() {
		return rsaNome;
	}

	public void setRsaNome(String rsaNome) {
		this.rsaNome = rsaNome;
	}

	@Override
	public int hashCode() {
		return Objects.hash(allegati, dataRisposta, dataScadenza, dicSpesaId, dicSpesaIntegrazioneCode,
				dicSpesaIntegrazioneData, dicSpesaIntegrazioneNote, integrazionePbandiId, invioPbandiId, periodoFine,
				periodoInizio, rsaCod, rsaNome);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModelIntegrazioneBuonoDettaglio other = (ModelIntegrazioneBuonoDettaglio) obj;
		return Objects.equals(allegati, other.allegati) && Objects.equals(dataRisposta, other.dataRisposta)
				&& Objects.equals(dataScadenza, other.dataScadenza) && Objects.equals(dicSpesaId, other.dicSpesaId)
				&& Objects.equals(dicSpesaIntegrazioneCode, other.dicSpesaIntegrazioneCode)
				&& Objects.equals(dicSpesaIntegrazioneData, other.dicSpesaIntegrazioneData)
				&& Objects.equals(dicSpesaIntegrazioneNote, other.dicSpesaIntegrazioneNote)
				&& Objects.equals(integrazionePbandiId, other.integrazionePbandiId)
				&& Objects.equals(invioPbandiId, other.invioPbandiId) && Objects.equals(periodoFine, other.periodoFine)
				&& Objects.equals(periodoInizio, other.periodoInizio) && Objects.equals(rsaCod, other.rsaCod)
				&& Objects.equals(rsaNome, other.rsaNome);
	}

	@Override
	public String toString() {
		return "ModelIntegrazioneBuonoDettaglio [invioPbandiId=" + invioPbandiId + ", integrazionePbandiId="
				+ integrazionePbandiId + ", dicSpesaIntegrazioneData=" + dicSpesaIntegrazioneData
				+ ", dicSpesaIntegrazioneNote=" + dicSpesaIntegrazioneNote + ", dicSpesaIntegrazioneCode="
				+ dicSpesaIntegrazioneCode + ", periodoInizio=" + periodoInizio + ", periodoFine=" + periodoFine
				+ ", dataScadenza=" + dataScadenza + ", dataRisposta=" + dataRisposta + ", rsaCod=" + rsaCod
				+ ", rsaNome=" + rsaNome + ", dicSpesaId=" + dicSpesaId + ", allegati=" + allegati + "]";
	}

}
