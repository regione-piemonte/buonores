/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbanbatch.dto;

import java.util.Date;
import java.util.Objects;

public class RichiestaDto {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private String cf = null;
	private long domandaDetId = 0;
	private String domandaNumero = null;
	private String richiedenteCf = null;
	private String stato = null;
	private Date inizioValidita = null;
	private Integer giorni = null;
	private Long sportelloId = null;
	private long domandaId = 0;

	/**
	 * Codice univoco di errore interno
	 **/

	public String getCf() {
		return cf;
	}

	public void setCf(String cf) {
		this.cf = cf;
	}

	public Integer getGiorni() {
		return giorni;
	}

	public void setGiorni(Integer giorni) {
		this.giorni = giorni;
	}

	public Date getInizioValidita() {
		return inizioValidita;
	}

	public void setInizioValidita(Date inizioValidita) {
		this.inizioValidita = inizioValidita;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public long getDomandaDetId() {
		return domandaDetId;
	}

	public void setDomandaDetId(long domandaDetId) {
		this.domandaDetId = domandaDetId;
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

	public Long getSportelloId() {
		return sportelloId;
	}

	public void setSportelloId(Long sportelloId) {
		this.sportelloId = sportelloId;
	}

	public long getDomandaId() {
		return domandaId;
	}

	public void setDomandaId(long domandaId) {
		this.domandaId = domandaId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cf, domandaDetId, domandaNumero, richiedenteCf);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RichiestaDto other = (RichiestaDto) obj;
		return Objects.equals(cf, other.cf) && domandaDetId == other.domandaDetId
				&& Objects.equals(domandaNumero, other.domandaNumero)
				&& Objects.equals(richiedenteCf, other.richiedenteCf);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RichiestaDto [cf=");
		builder.append(cf);
		builder.append(", domandaDetId=");
		builder.append(domandaDetId);
		builder.append(", domandaNumero=");
		builder.append(domandaNumero);
		builder.append(", richiedenteCf=");
		builder.append(richiedenteCf);
		builder.append("]");
		return builder.toString();
	}

}
