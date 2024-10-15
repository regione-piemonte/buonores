/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

public class ModelStoricoRendicontazioni {

	private String monthData;
	private Integer anno;
	private String mese;
	private String esito;
	private String note;
	private String dataDichiarazione;
	private String dataIstruttoria;

	public String getMonthData() {
		return monthData;
	}

	public void setMonthData(String monthData) {
		this.monthData = monthData;
	}

	public Integer getAnno() {
		return anno;
	}

	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	public String getMese() {
		return mese;
	}

	public void setMese(String mese) {
		this.mese = mese;
	}

	public String getEsito() {
		return esito;
	}

	public void setEsito(String esito) {
		this.esito = esito;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getDataDichiarazione() {
		return dataDichiarazione;
	}

	public void setDataDichiarazione(String dataDichiarazione) {
		this.dataDichiarazione = dataDichiarazione;
	}

	public String getDataIstruttoria() {
		return dataIstruttoria;
	}

	public void setDataIstruttoria(String dataIstruttoria) {
		this.dataIstruttoria = dataIstruttoria;
	}

	@Override
	public String toString() {
		return "ModelStoricoRendicontazioni [monthData=" + monthData + ", anno=" + anno + ", mese=" + mese + ", esito="
				+ esito + ", note=" + note + ", dataDichiarazione=" + dataDichiarazione + ", dataIstruttoria="
				+ dataIstruttoria + "]";
	}

}
