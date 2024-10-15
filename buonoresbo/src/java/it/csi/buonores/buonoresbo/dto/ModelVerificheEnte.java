/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

import java.util.Date;
import java.util.Objects;

public class ModelVerificheEnte {
	private Boolean presenzaAltreIncompatibilita;
	private String noteEnte;
	private Date dataVerifica;

	public Boolean getPresenzaAltreIncompatibilita() {
		return presenzaAltreIncompatibilita;
	}

	public void setPresenzaAltreIncompatibilita(Boolean presenzaAltreIncompatibilita) {
		this.presenzaAltreIncompatibilita = presenzaAltreIncompatibilita;
	}

	public String getNoteEnte() {
		return noteEnte;
	}

	public void setNoteEnte(String noteEnte) {
		this.noteEnte = noteEnte;
	}

	public Date getDataVerifica() {
		return dataVerifica;
	}

	public void setDataVerifica(Date dataVerifica) {
		this.dataVerifica = dataVerifica;
	}

	@Override
	public String toString() {
		return "ModelVerificheEnte [presenzaAltreIncopatibilita=" + presenzaAltreIncompatibilita + ", noteEnte="
				+ noteEnte + ", dataVerifica=" + dataVerifica + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataVerifica, noteEnte, presenzaAltreIncompatibilita);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModelVerificheEnte other = (ModelVerificheEnte) obj;
		return Objects.equals(dataVerifica, other.dataVerifica) && Objects.equals(noteEnte, other.noteEnte)
				&& Objects.equals(presenzaAltreIncompatibilita, other.presenzaAltreIncompatibilita);
	}
}
