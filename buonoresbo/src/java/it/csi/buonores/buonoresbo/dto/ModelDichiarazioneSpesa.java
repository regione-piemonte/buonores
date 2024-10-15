/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

import java.util.Date;
import java.util.List;

public class ModelDichiarazioneSpesa {
	private long idDicSpesa;
	private String dicSpesaPeriodoDesc;
	private Date dicSpesaData;
	private String dicSpesaCodBandi;
	private Date periodoInizio;
	private Date periodoFine;
	private String statoCod;
	private String statoDesc;
	private Date dataInvio;
	private List<ModelAllegatoBuono> allegatiDichiarazioneSpesa;
	private List<ModelDocumentoSpesa> documentiSpesa;

	public long getIdDicSpesa() {
		return idDicSpesa;
	}

	public void setIdDicSpesa(long idDicSpesa) {
		this.idDicSpesa = idDicSpesa;
	}

	public String getDicSpesaPeriodoDesc() {
		return dicSpesaPeriodoDesc;
	}

	public void setDicSpesaPeriodoDesc(String dicSpesaPeriodoDesc) {
		this.dicSpesaPeriodoDesc = dicSpesaPeriodoDesc;
	}

	public Date getDicSpesaData() {
		return dicSpesaData;
	}

	public void setDicSpesaData(Date dicSpesaData) {
		this.dicSpesaData = dicSpesaData;
	}

	public String getDicSpesaCodBandi() {
		return dicSpesaCodBandi;
	}

	public void setDicSpesaCodBandi(String dicSpesaCodBandi) {
		this.dicSpesaCodBandi = dicSpesaCodBandi;
	}

	public Date getPeriodoInizio() {
		return periodoInizio;
	}

	public void setPeriodoInizio(Date periodoInizio) {
		this.periodoInizio = periodoInizio;
	}

	public Date getPeriodoFine() {
		return periodoFine;
	}

	public void setPeriodoFine(Date periodoFine) {
		this.periodoFine = periodoFine;
	}

	public List<ModelDocumentoSpesa> getDocumentiSpesa() {
		return documentiSpesa;
	}

	public void setDocumentiSpesa(List<ModelDocumentoSpesa> documentiSpesa) {
		this.documentiSpesa = documentiSpesa;
	}

	public List<ModelAllegatoBuono> getAllegatiDichiarazioneSpesa() {
		return allegatiDichiarazioneSpesa;
	}

	public void setAllegatiDichiarazioneSpesa(List<ModelAllegatoBuono> allegatiDichiarazioneSpesa) {
		this.allegatiDichiarazioneSpesa = allegatiDichiarazioneSpesa;
	}

	public String getStatoCod() {
		return statoCod;
	}

	public void setStatoCod(String statoCod) {
		this.statoCod = statoCod;
	}

	public String getStatoDesc() {
		return statoDesc;
	}

	public void setStatoDesc(String statoDesc) {
		this.statoDesc = statoDesc;
	}

	public Date getDataInvio() {
		return dataInvio;
	}

	public void setDataInvio(Date dataInvio) {
		this.dataInvio = dataInvio;
	}

	@Override
	public String toString() {
		return "ModelDichiarazioneSpesa [idDicSpesa=" + idDicSpesa + ", dicSpesaPeriodoDesc=" + dicSpesaPeriodoDesc
				+ ", dicSpesaData=" + dicSpesaData + ", dicSpesaCodBandi=" + dicSpesaCodBandi + ", periodoInizio="
				+ periodoInizio + ", periodoFine=" + periodoFine + ", statoCod=" + statoCod + ", statoDesc=" + statoDesc
				+ ", dataInvio=" + dataInvio + ", allegatiDichiarazioneSpesa=" + allegatiDichiarazioneSpesa
				+ ", documentiSpesa=" + documentiSpesa + "]";
	}

}
