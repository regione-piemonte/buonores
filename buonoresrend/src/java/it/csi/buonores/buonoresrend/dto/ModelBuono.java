/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresrend.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.csi.buonores.buonoresrend.dto.custom.ModelVerificaIsee;

public class ModelBuono implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1217583015961643880L;
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]
	
	private Integer buonoId = null;
	private String buonoCod = null;
	private String buonoStatoCod=null;
	private Integer sportelloId = null;
	private String domandaNumero = null;
	private String beneficiarioCf = null;
	private Date dataInizioRendicontazione = null;
	private Date dataFineValiditaBuono = null;
	private String rsaCode=null;
	private String rsaCfPiva=null;
	private Date contrattoDataInizio=null;
	private Date dataInvioContrattoABandi = null;
	private Date contrattoDataFine=null;
	private List<ModelVerificaIsee> verificaIseeCorrente=null;
	
	@JsonProperty("buono_id")
	public Integer getBuonoId() {
		return buonoId;
	}
	public void setBuonoId(Integer buonoId) {
		this.buonoId = buonoId;
	}
	@JsonProperty("buono_cod")
	public String getBuonoCod() {
		return buonoCod;
	}
	public void setBuonoCod(String buonoCod) {
		this.buonoCod = buonoCod;
	}
	@JsonProperty("buono_stato_cod")
	public String getBuonoStatoCod() {
		return buonoStatoCod;
	}
	public void setBuonoStatoCod(String buonoStatoCod) {
		this.buonoStatoCod = buonoStatoCod;
	}
	@JsonProperty("sportello_id")
	public Integer getSportelloId() {
		return sportelloId;
	}
	public void setSportelloId(Integer sportelloId) {
		this.sportelloId = sportelloId;
	}
	@JsonProperty("domanda_numero")
	public String getDomandaNumero() {
		return domandaNumero;
	}
	public void setDomandaNumero(String domandaNumero) {
		this.domandaNumero = domandaNumero;
	}
	@JsonProperty("beneficiario_cf")
	public String getBeneficiarioCf() {
		return beneficiarioCf;
	}
	public void setBeneficiarioCf(String beneficiarioCf) {
		this.beneficiarioCf = beneficiarioCf;
	}
	@JsonProperty("data_inizio_rendicontazione")
	public Date getDataInizioRendicontazione() {
		return dataInizioRendicontazione;
	}
	public void setDataInizioRendicontazione(Date dataInizioRendicontazione) {
		this.dataInizioRendicontazione = dataInizioRendicontazione;
	}
	@JsonProperty("data_fine_validita_buono")
	public Date getDataFineValiditaBuono() {
		return dataFineValiditaBuono;
	}
	public void setDataFineValiditaBuono(Date dataFineValiditaBuono) {
		this.dataFineValiditaBuono = dataFineValiditaBuono;
	}
	@JsonProperty("rsa_cod")
	public String getRsaCode() {
		return rsaCode;
	}
	public void setRsaCode(String rsaCode) {
		this.rsaCode = rsaCode;
	}
	@JsonProperty("rsa_cfPiva")
	public String getRsaCfPiva() {
		return rsaCfPiva;
	}
	public void setRsaCfPiva(String rsaCfPiva) {
		this.rsaCfPiva = rsaCfPiva;
	}
	@JsonProperty("contratto_data_inizio")
	public Date getContrattoDataInizio() {
		return contrattoDataInizio;
	}
	public void setContrattoDataInizio(Date contrattoDataInizio) {
		this.contrattoDataInizio = contrattoDataInizio;
	}
	@JsonProperty("contratto_data_fine")
	public Date getContrattoDataFine() {
		return contrattoDataFine;
	}
	public void setContrattoDataFine(Date contrattoDataFine) {
		this.contrattoDataFine = contrattoDataFine;
	}
	@JsonProperty("data_invio_contratto_a_bandi")
	public Date getDataInvioContrattoABandi() {
		return dataInvioContrattoABandi;
	}
	public void setDataInvioContrattoABandi(Date dataInvioContrattoABandi) {
		this.dataInvioContrattoABandi = dataInvioContrattoABandi;
	}
	@JsonProperty("verifica_isee_corrente")
	public List<ModelVerificaIsee> getVerificaIseeCorrente() {
		return verificaIseeCorrente;
	}
	public void setVerificaIseeCorrente(List<ModelVerificaIsee> verificaIseeCorrente) {
		this.verificaIseeCorrente = verificaIseeCorrente;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(beneficiarioCf, buonoCod, buonoId, buonoStatoCod, contrattoDataFine, contrattoDataInizio,
				dataFineValiditaBuono, dataInizioRendicontazione, dataInvioContrattoABandi, domandaNumero, rsaCfPiva,
				rsaCode, sportelloId, verificaIseeCorrente);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModelBuono other = (ModelBuono) obj;
		return Objects.equals(beneficiarioCf, other.beneficiarioCf) && Objects.equals(buonoCod, other.buonoCod)
				&& Objects.equals(buonoId, other.buonoId) && Objects.equals(buonoStatoCod, other.buonoStatoCod)
				&& Objects.equals(contrattoDataFine, other.contrattoDataFine)
				&& Objects.equals(contrattoDataInizio, other.contrattoDataInizio)
				&& Objects.equals(dataFineValiditaBuono, other.dataFineValiditaBuono)
				&& Objects.equals(dataInizioRendicontazione, other.dataInizioRendicontazione)
				&& Objects.equals(dataInvioContrattoABandi, other.dataInvioContrattoABandi)
				&& Objects.equals(domandaNumero, other.domandaNumero) && Objects.equals(rsaCfPiva, other.rsaCfPiva)
				&& Objects.equals(rsaCode, other.rsaCode) && Objects.equals(sportelloId, other.sportelloId)
				&& Objects.equals(verificaIseeCorrente, other.verificaIseeCorrente);
	}
	@Override
	public String toString() {
		return "ModelBuono [buonoId=" + buonoId + ", buonoCod=" + buonoCod + ", buonoStatoCod=" + buonoStatoCod
				+ ", sportelloId=" + sportelloId + ", domandaNumero=" + domandaNumero + ", beneficiarioCf="
				+ beneficiarioCf + ", dataInizioRendicontazione=" + dataInizioRendicontazione
				+ ", dataFineValiditaBuono=" + dataFineValiditaBuono + ", rsaCode=" + rsaCode + ", rsaCfPiva="
				+ rsaCfPiva + ", contrattoDataInizio=" + contrattoDataInizio + ", dataInvioContrattiABandi="
				+ dataInvioContrattoABandi + ", contrattoDataFine=" + contrattoDataFine 
				+ ", verificaIseeCorrente=" + verificaIseeCorrente + "]";
	}
	


}
