/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbanbatch.dto;

import java.util.Objects;

public class ContrattoDto {

	private long contrattoId = 0;
	private String domandaNumero = null;
	private String richiedenteCf = null;
	private String beneficiarioCf= null;
	
	public long getContrattoId() {
		return contrattoId;
	}
	public void setContrattoId(long contrattoId) {
		this.contrattoId = contrattoId;
	}
	public String getDomandaNumero() {
		return domandaNumero;
	}
	public void setDomandaNumero(String domandaNumero) {
		this.domandaNumero = domandaNumero;
	}
	public String getRichiedenteCf() {
		return richiedenteCf;
	}
	public void setRichiedenteCf(String richiedenteCf) {
		this.richiedenteCf = richiedenteCf;
	}
	public String getBeneficiarioCf() {
		return beneficiarioCf;
	}
	public void setBeneficiarioCf(String beneficiarioCf) {
		this.beneficiarioCf = beneficiarioCf;
	}
	@Override
	public int hashCode() {
		return Objects.hash(beneficiarioCf, contrattoId, domandaNumero, richiedenteCf);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContrattoDto other = (ContrattoDto) obj;
		return Objects.equals(beneficiarioCf, other.beneficiarioCf) && contrattoId == other.contrattoId
				&& Objects.equals(domandaNumero, other.domandaNumero)
				&& Objects.equals(richiedenteCf, other.richiedenteCf);
	}
	@Override
	public String toString() {
		return "ContrattoDto [contrattoId=" + contrattoId + ", domandaNumero=" + domandaNumero + ", richiedenteCf="
				+ richiedenteCf + ", beneficiarioCf=" + beneficiarioCf + "]";
	}



}
