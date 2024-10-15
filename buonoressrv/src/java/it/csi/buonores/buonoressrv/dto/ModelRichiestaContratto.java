/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.dto;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelRichiestaContratto {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private String tipo = null;
	// RIMOZIONE_INTESTATARIO_DATA_FINE POST_DEMO 14_04_2023
//	private ModelPersonaSintesi intestatario = null;
//	private String relazioneDestinatario = null;
	private Date dataInizio = null;
//	private Date dataFine = null;
	private ModelStruttura struttura = null;
	private Boolean titoloPrivato = null;
	private Boolean incompatibilitaPerContratto = null;

	/**
	 * tipologia di contratto (codice ottenuto da servizio decodifiche)
	 **/

	@JsonProperty("tipo")
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

//	@JsonProperty("intestatario")
//	public ModelPersonaSintesi getIntestatario() {
//		return intestatario;
//	}
//
//	public void setIntestatario(ModelPersonaSintesi intestatario) {
//		this.intestatario = intestatario;
//	}

	/**
	 * eventuale relazione tra destinatario ed intestatario del contratto (codice
	 * ottenuto da servizio decodifiche)
	 **/

//	@JsonProperty("relazione_destinatario")
//	public String getRelazioneDestinatario() {
//		return relazioneDestinatario;
//	}
//
//	public void setRelazioneDestinatario(String relazioneDestinatario) {
//		this.relazioneDestinatario = relazioneDestinatario;
//	}

	/**
	 * dal frontend viene popolato come YYYY-MM-DD non presente solo quando
	 * incompatibilita_per_contratto è true
	 **/

	@JsonProperty("data_inizio")
	public Date getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}

	/**
	 * dal frontend viene popolato come YYYY-MM-DD non presente solo quando
	 * incompatibilita_per_contratto è true
	 **/

//	@JsonProperty("data_fine")
//	public Date getDataFine() {
//		return dataFine;
//	}
//
//	public void setDataFine(Date dataFine) {
//		this.dataFine = dataFine;
//	}

	/**
	 * codice della struttura recuperato da decodifica
	 **/

	@JsonProperty("struttura")
	public ModelStruttura getStruttura() {
		return struttura;
	}

	public void setStruttura(ModelStruttura struttura) {
		this.struttura = struttura;
	}

	/**
	 * il detinatario è inserito in struttura a titolo privato
	 **/

	@JsonProperty("titolo_privato")
	public Boolean isTitoloPrivato() {
		return titoloPrivato;
	}

	public void setTitoloPrivato(Boolean titoloPrivato) {
		this.titoloPrivato = titoloPrivato;
	}

	/**
	 * true se non esiste ancora un contratto con la struttura false se già presente
	 * un contratto con la struttura
	 **/

	@JsonProperty("incompatibilita_per_contratto")
	public Boolean isIncompatibilitaPerContratto() {
		return incompatibilitaPerContratto;
	}

	public void setIncompatibilitaPerContratto(Boolean incompatibilitaPerContratto) {
		this.incompatibilitaPerContratto = incompatibilitaPerContratto;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ModelRichiestaContratto modelRichiestaContratto = (ModelRichiestaContratto) o;
		return Objects.equals(tipo, modelRichiestaContratto.tipo)
//				&& Objects.equals(intestatario, modelRichiestaContratto.intestatario)
//				&& Objects.equals(relazioneDestinatario, modelRichiestaContratto.relazioneDestinatario)
				&& Objects.equals(dataInizio, modelRichiestaContratto.dataInizio)
//				&& Objects.equals(dataFine, modelRichiestaContratto.dataFine)
				&& Objects.equals(struttura, modelRichiestaContratto.struttura)
				&& Objects.equals(titoloPrivato, modelRichiestaContratto.titoloPrivato)
				&& Objects.equals(incompatibilitaPerContratto, modelRichiestaContratto.incompatibilitaPerContratto);
	}

	@Override
	public int hashCode() {
		return Objects.hash(tipo, dataInizio, struttura, titoloPrivato,
				incompatibilitaPerContratto);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ModelRichiestaContratto {\n");

		sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
		sb.append("    dataInizio: ").append(toIndentedString(dataInizio)).append("\n");
		sb.append("    struttura: ").append(toIndentedString(struttura)).append("\n");
		sb.append("    titoloPrivato: ").append(toIndentedString(titoloPrivato)).append("\n");
		sb.append("    incompatibilitaPerContratto: ").append(toIndentedString(incompatibilitaPerContratto))
				.append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}
