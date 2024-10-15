/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelContratto {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private Integer id = null;
	private Date dataInizio = null;
	private Date dataFine = null;
	private ModelFornitore fornitore = null;
	private String tipo = null;
	private String tipoFornitore = null;
	private Date dataInvioABandi = null;

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

	@JsonProperty("data_invio_a_bandi")
	public Date getDataInvioABandi() {
		return dataInvioABandi;
	}

	public void setDataInvioABandi(Date dataInvioABandi) {
		this.dataInvioABandi = dataInvioABandi;
	}

	@JsonProperty("fornitore")
	public ModelFornitore getFornitore() {
		return fornitore;
	}

	public void setFornitore(ModelFornitore fornitore) {
		this.fornitore = fornitore;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataFine, dataInizio, fornitore, id, tipo, tipoFornitore, dataInvioABandi);
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
				&& Objects.equals(fornitore, other.fornitore) && Objects.equals(id, other.id)
				&& Objects.equals(tipo, other.tipo) && Objects.equals(tipoFornitore, other.tipoFornitore)
				&& Objects.equals(dataInvioABandi, other.dataInvioABandi);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ModelContratto {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    dataInizio: ").append(toIndentedString(dataInizio)).append("\n");
		sb.append("    dataFine: ").append(toIndentedString(dataFine)).append("\n");
		sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
		sb.append("    fornitore: ").append(toIndentedString(fornitore)).append("\n");
		sb.append("    tipo fornitore: ").append(toIndentedString(tipoFornitore)).append("\n");
		sb.append("    dataInvioABandi: ").append(toIndentedString(dataInvioABandi)).append("\n");
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
