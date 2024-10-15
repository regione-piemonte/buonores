/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto.custom;

import java.sql.Timestamp;

public class LogAudit {

	private long auditId;
	private Timestamp dataOra;
	private String idApp;
	private String ipAddress;
	private String utente;
	private String operazione;
	private String oggOper;
	private String keyOper;
	private String uuid;
	private String requestPayload;
	private String responsePayload;
	private Integer esitoChiamata;

	public long getAuditId() {
		return auditId;
	}

	public void setAuditId(long auditId) {
		this.auditId = auditId;
	}

	public Timestamp getDataOra() {
		return dataOra;
	}

	public void setDataOra(Timestamp dataOra) {
		this.dataOra = dataOra;
	}

	public String getIdApp() {
		return idApp;
	}

	public void setIdApp(String idApp) {
		this.idApp = idApp;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getUtente() {
		return utente;
	}

	public void setUtente(String utente) {
		this.utente = utente;
	}

	public String getOperazione() {
		return operazione;
	}

	public void setOperazione(String operazione) {
		this.operazione = operazione;
	}

	public String getOggOper() {
		return oggOper;
	}

	public void setOggOper(String oggOper) {
		this.oggOper = oggOper;
	}

	public String getKeyOper() {
		return keyOper;
	}

	public void setKeyOper(String keyOper) {
		this.keyOper = keyOper;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getRequestPayload() {
		return requestPayload;
	}

	public void setRequestPayload(String requestPayload) {
		this.requestPayload = requestPayload;
	}

	public String getResponsePayload() {
		return responsePayload;
	}

	public void setResponsePayload(String responsePayload) {
		this.responsePayload = responsePayload;
	}

	public Integer getEsitoChiamata() {
		return esitoChiamata;
	}

	public void setEsitoChiamata(Integer esitoChiamata) {
		this.esitoChiamata = esitoChiamata;
	}

	@Override
	public String toString() {
		return "LogAudit [auditId=" + auditId + ", dataOra=" + dataOra + ", idApp=" + idApp + ", ipAddress=" + ipAddress
				+ ", utente=" + utente + ", operazione=" + operazione + ", oggOper=" + oggOper + ", keyOper=" + keyOper
				+ ", uuid=" + uuid + ", requestPayload=" + requestPayload + ", responsePayload=" + responsePayload
				+ ", esitoChiamata=" + esitoChiamata + "]";
	}

}