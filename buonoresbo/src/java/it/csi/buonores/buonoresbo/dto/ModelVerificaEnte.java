/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

public class ModelVerificaEnte {
	private Long verificaId;
	private String dataVerifica;
	private String verificaNote;
	private Boolean nessunaIncompatibilita; // TODO a noi serve?
	private Long enteGestoreId;
	private Long sportelloId;
	private Long domandaId;
	private Long domandaDetId;
	private String utenteCreazione;
	private String dataVerificaRichiesta;
	private Boolean verificaEgRichiesta;
	private Boolean verificaEgInCorso;
	private Boolean verificaEgConclusa;

	public Long getVerificaId() {
		return verificaId;
	}

	public void setVerificaId(Long verificaId) {
		this.verificaId = verificaId;
	}

	public String getDataVerifica() {
		return dataVerifica;
	}

	public void setDataVerifica(String dataVerifica) {
		this.dataVerifica = dataVerifica;
	}

	public String getVerificaNote() {
		return verificaNote;
	}

	public void setVerificaNote(String verificaNote) {
		this.verificaNote = verificaNote;
	}

	public Boolean getNessunaIncompatibilita() {
		return nessunaIncompatibilita;
	}

	public void setNessunaIncompatibilita(Boolean nessunaIncompatibilita) {
		this.nessunaIncompatibilita = nessunaIncompatibilita;
	}

	public Long getEnteGestoreId() {
		return enteGestoreId;
	}

	public void setEnteGestoreId(Long enteGestoreId) {
		this.enteGestoreId = enteGestoreId;
	}

	public Long getSportelloId() {
		return sportelloId;
	}

	public void setSportelloId(Long sportelloId) {
		this.sportelloId = sportelloId;
	}

	public Long getDomandaId() {
		return domandaId;
	}

	public void setDomandaId(Long domandaId) {
		this.domandaId = domandaId;
	}

	public Long getDomandaDetId() {
		return domandaDetId;
	}

	public void setDomandaDetId(Long domandaDetId) {
		this.domandaDetId = domandaDetId;
	}

	public String getUtenteCreazione() {
		return utenteCreazione;
	}

	public void setUtenteCreazione(String utenteCreazione) {
		this.utenteCreazione = utenteCreazione;
	}

	public String getDataVerificaRichiesta() {
		return dataVerificaRichiesta;
	}

	public void setDataVerificaRichiesta(String dataVerificaRichiesta) {
		this.dataVerificaRichiesta = dataVerificaRichiesta;
	}

	public Boolean getVerificaEgRichiesta() {
		return verificaEgRichiesta;
	}

	public void setVerificaEgRichiesta(Boolean verificaEgRichiesta) {
		this.verificaEgRichiesta = verificaEgRichiesta;
	}

	public Boolean getVerificaEgInCorso() {
		return verificaEgInCorso;
	}

	public void setVerificaEgInCorso(Boolean verificaEgInCorso) {
		this.verificaEgInCorso = verificaEgInCorso;
	}

	public Boolean getVerificaEgConclusa() {
		return verificaEgConclusa;
	}

	public void setVerificaEgConclusa(Boolean verificaEgConclusa) {
		this.verificaEgConclusa = verificaEgConclusa;
	}

	@Override
	public String toString() {
		return "ModelVerificaEnte [verificaId=" + verificaId + ", dataVerifica=" + dataVerifica + ", verificaNote="
				+ verificaNote + ", nessunaIncompatibilita=" + nessunaIncompatibilita + ", enteGestoreId="
				+ enteGestoreId + ", sportelloId=" + sportelloId + ", domandaId=" + domandaId + ", domandaDetId="
				+ domandaDetId + ", utenteCreazione=" + utenteCreazione + ", dataVerificaRichiesta="
				+ dataVerificaRichiesta + ", verificaEgRichiesta=" + verificaEgRichiesta + ", verificaEgInCorso="
				+ verificaEgInCorso + ", verificaEgConclusa=" + verificaEgConclusa + "]";
	}

}
