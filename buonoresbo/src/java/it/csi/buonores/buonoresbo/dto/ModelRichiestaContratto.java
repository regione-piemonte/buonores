/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

import java.sql.Timestamp;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelRichiestaContratto {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private String tipo = null;
	private Timestamp dataInizio = null;
	private ModelStruttura struttura = null;
	private Boolean titoloPrivato = null;
	private Boolean incompatibilitaPerContratto = null;

	/**
	 * tipologia di contratto (codice ottenuto da servizio decodifiche) NULL nel
	 * caso in cui incompatibilita_per_contratto &#x3D;&#x3D; true
	 **/

	@JsonProperty("tipo")

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	@JsonProperty("data_inizio")

	public Timestamp getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(Timestamp dataInizio) {
		this.dataInizio = dataInizio;
	}

	/**
	 * true nessun contratto in essere (ma impegno ad attivarlo) false il contratto
	 * esiste ed è indicata la tipologia nel campo \&quot;tipo\&quot;
	 **/
	@JsonProperty("incompatibilita_per_contratto")

	public Boolean isIncompatibilitaPerContratto() {
		return incompatibilitaPerContratto;
	}

	public void setIncompatibilitaPerContratto(Boolean incompatibilitaPerContratto) {
		this.incompatibilitaPerContratto = incompatibilitaPerContratto;
	}

	@JsonProperty("struttura")
	public ModelStruttura getStruttura() {
		return struttura;
	}

	public void setStruttura(ModelStruttura struttura) {
		this.struttura = struttura;
	}

	@JsonProperty("titolo_privato")
	public Boolean getTitoloPrivato() {
		return titoloPrivato;
	}

	public void setTitoloPrivato(Boolean titoloPrivato) {
		this.titoloPrivato = titoloPrivato;
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
				&& Objects.equals(dataInizio, modelRichiestaContratto.dataInizio)
				&& Objects.equals(struttura, modelRichiestaContratto.struttura)
				&& Objects.equals(titoloPrivato, modelRichiestaContratto.titoloPrivato)
				&& Objects.equals(incompatibilitaPerContratto, modelRichiestaContratto.incompatibilitaPerContratto);
	}

	@Override
	public int hashCode() {
		return Objects.hash(tipo, dataInizio, struttura, titoloPrivato, incompatibilitaPerContratto);
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
