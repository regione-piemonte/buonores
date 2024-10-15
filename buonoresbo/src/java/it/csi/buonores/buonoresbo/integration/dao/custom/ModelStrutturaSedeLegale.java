/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.custom;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelStrutturaSedeLegale {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private String nome = null;
	private String indirizzo = null;
	private String comune = null;
	private String provincia = null;

	/**
	 * denominazione della sede legale
	 **/

	@JsonProperty("nome")
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * indirizzo della sede legale
	 **/

	@JsonProperty("indirizzo")
	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	/**
	 * comune di appartenenza della sede legale
	 **/

	@JsonProperty("comune")
	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	/**
	 * provincia di appartenza della sede legale
	 **/

	@JsonProperty("provincia")
	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ModelStrutturaSedeLegale modelStrutturaSedeLegale = (ModelStrutturaSedeLegale) o;
		return Objects.equals(nome, modelStrutturaSedeLegale.nome)
				&& Objects.equals(indirizzo, modelStrutturaSedeLegale.indirizzo)
				&& Objects.equals(comune, modelStrutturaSedeLegale.comune)
				&& Objects.equals(provincia, modelStrutturaSedeLegale.provincia);
	}

	@Override
	public int hashCode() {
		return Objects.hash(nome, indirizzo, comune, provincia);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ModelStrutturaSedeLegale {\n");

		sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
		sb.append("    indirizzo: ").append(toIndentedString(indirizzo)).append("\n");
		sb.append("    comune: ").append(toIndentedString(comune)).append("\n");
		sb.append("    provincia: ").append(toIndentedString(provincia)).append("\n");
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
