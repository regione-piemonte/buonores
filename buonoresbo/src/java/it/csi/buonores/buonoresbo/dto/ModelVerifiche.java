/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelVerifiche {
	Boolean verificaEgRichiesta;
	Boolean verificaEgInCorso;
	Boolean verificaEgConclusa;
	String verificaEgPunteggio;
	Boolean verificaEgIncompatibilita;
	String noteEnteGestore;

	@JsonProperty("note_ente_gestore")
	public String getNoteEnteGestore() {
		return noteEnteGestore;
	}

	public void setNoteEnteGestore(String noteEnteGestore) {
		this.noteEnteGestore = noteEnteGestore;
	}

	@JsonProperty("verifica_eg_richiesta")

	public Boolean isVerificaEgRichiesta() {
		return verificaEgRichiesta;
	}

	public void setVerificaEgRichiesta(Boolean verificaEgRichiesta) {
		this.verificaEgRichiesta = verificaEgRichiesta;
	}

	@JsonProperty("verifica_eg_in_corso")

	public Boolean isVerificaEgInCorso() {
		return verificaEgInCorso;
	}

	public void setVerificaEgInCorso(Boolean verificaEgInCorso) {
		this.verificaEgInCorso = verificaEgInCorso;
	}

	@JsonProperty("verifica_eg_conclusa")

	public Boolean isVerificaEgConclusa() {
		return verificaEgConclusa;
	}

	public void setVerificaEgConclusa(Boolean verificaEgConclusa) {
		this.verificaEgConclusa = verificaEgConclusa;
	}

	@JsonProperty("verifica_eg_punteggio_sociale")
	public String getVerificaEgPunteggio() {
		return verificaEgPunteggio;
	}

	public void setVerificaEgPunteggio(String verificaEgPunteggio) {
		this.verificaEgPunteggio = verificaEgPunteggio;
	}

	@JsonProperty("verifica_eg_incompatibilita")

	public Boolean isVerificaEgIncompatibilita() {
		return verificaEgIncompatibilita;
	}

	public void setVerificaEgIncompatibilita(Boolean verificaEgIncompatibilita) {
		this.verificaEgIncompatibilita = verificaEgIncompatibilita;
	}

	@Override
	public int hashCode() {
		return Objects.hash(verificaEgConclusa, verificaEgInCorso, verificaEgIncompatibilita, verificaEgPunteggio,
				verificaEgRichiesta);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModelVerifiche other = (ModelVerifiche) obj;
		return Objects.equals(verificaEgConclusa, other.verificaEgConclusa)
				&& Objects.equals(verificaEgInCorso, other.verificaEgInCorso)
				&& Objects.equals(verificaEgIncompatibilita, other.verificaEgIncompatibilita)
				&& Objects.equals(verificaEgPunteggio, other.verificaEgPunteggio)
				&& Objects.equals(verificaEgRichiesta, other.verificaEgRichiesta);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ModelVerifiche [verificaEgRichiesta=");
		builder.append(verificaEgRichiesta);
		builder.append(", verificaEgInCorso=");
		builder.append(verificaEgInCorso);
		builder.append(", verificaEgConclusa=");
		builder.append(verificaEgConclusa);
		builder.append(", verificaEgPunteggio=");
		builder.append(verificaEgPunteggio);
		builder.append(", verificaEgIncompatibilita=");
		builder.append(verificaEgIncompatibilita);
		builder.append("]");
		return builder.toString();
	}

}
