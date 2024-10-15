/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.dto;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelContratto {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private Integer id = null;
	private Date dataInizio = null;
	private Date dataFine = null;
	private ModelStruttura struttura = null;
	private String tipo = null;
	private String tipoFornitore = null;
	private Date dataInvioABandi = null;
	private Boolean titoloPrivato = null;
	private Date dataCreazione = null;

	@JsonProperty("id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@JsonProperty("tipo")
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	@JsonProperty("tipo_fornitore")
	public String getTipoFornitore() {
		return tipoFornitore;
	}

	public void setTipoFornitore(String tipoFornitore) {
		this.tipoFornitore = tipoFornitore;
	}

	/**
	 * eventuale relazione tra destinatario ed intestatario del contratto (codice
	 * ottenuto da servizio decodifiche) può assumere uno dei seguenti valori: -
	 * POTESTA_GENITORIALE - NUCLEO_FAMILIARE - CONIUGE - PARENTE_PRIMO_GRADO -
	 * TUTELA - CURATELA - AMMINISTRATORE_SOSTEGNO - ALTRO
	 **/

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
	@JsonProperty("data_fine")
	public Date getDataFine() {
		return dataFine;
	}

	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}

	@JsonProperty("struttura")
	public ModelStruttura getStruttura() {
		return struttura;
	}

	public void setStruttura(ModelStruttura struttura) {
		this.struttura = struttura;
	}

	@JsonProperty("data_invio_a_bandi")
	public Date getDataInvioABandi() {
		return dataInvioABandi;
	}

	public void setDataInvioABandi(Date dataInvioABandi) {
		this.dataInvioABandi = dataInvioABandi;
	}

	@JsonProperty("titolo_privato")
	public Boolean getTitoloPrivato() {
		return titoloPrivato;
	}

	public void setTitoloPrivato(Boolean titoloPrivato) {
		this.titoloPrivato = titoloPrivato;
	}

	@JsonProperty("data_creazione")
	public Date getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataFine, dataInizio, struttura, id, titoloPrivato, tipo, dataInvioABandi, dataCreazione);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModelContratto other = (ModelContratto) obj;
		return Objects.equals(dataFine, other.dataFine) && Objects.equals(dataInizio, other.dataInizio)
				&& Objects.equals(struttura, other.struttura) && Objects.equals(id, other.id)
				&& Objects.equals(titoloPrivato, other.titoloPrivato)
				&& Objects.equals(tipo, other.tipo) && Objects.equals(dataInvioABandi, other.dataInvioABandi)
				&& Objects.equals(dataCreazione, other.dataCreazione);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ModelContratto {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    dataInizio: ").append(toIndentedString(dataInizio)).append("\n");
		sb.append("    dataFine: ").append(toIndentedString(dataFine)).append("\n");
		sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
		sb.append("    fornitore: ").append(toIndentedString(struttura)).append("\n");
		sb.append("    titoloPrivato: ").append(toIndentedString(titoloPrivato)).append("\n");
		sb.append("    dataInvioABandi: ").append(toIndentedString(dataInvioABandi)).append("\n");
		sb.append("    dataCreazione: ").append(toIndentedString(dataCreazione)).append("\n");
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
