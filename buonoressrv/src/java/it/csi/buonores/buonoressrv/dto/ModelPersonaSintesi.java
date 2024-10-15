/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.dto;

import java.util.Date;
import java.util.Objects;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelPersonaSintesi {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private String cf = null;
	private String nome = null;
	private String cognome = null;
	private Date dataNascita = null;
	private String statoNascita = null;
	private String comuneNascita = null;
	private String provinciaNascita = null;

	/**
	 * codice fiscale
	 **/

	@JsonProperty("cf")
	public String getCf() {
		return cf;
	}

	public void setCf(String cf) {
		this.cf = cf;
	}

	/**
	 * nome
	 **/

	@JsonProperty("nome")
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * cognome
	 **/

	@JsonProperty("cognome")
	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	@JsonProperty("data_nascita")
	public Date getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(Date dataNascita) {
		this.dataNascita = dataNascita;
	}

	/**
	 * nome nazione (maiuscolo)
	 **/

	@JsonProperty("stato_nascita")
	public String getStatoNascita() {
		return statoNascita;
	}

	public void setStatoNascita(String statoNascita) {
		this.statoNascita = statoNascita;
	}

	/**
	 * comune di nascita (maiuscolo)
	 **/

	@JsonProperty("comune_nascita")
	public String getComuneNascita() {
		return comuneNascita;
	}

	public void setComuneNascita(String comuneNascita) {
		this.comuneNascita = comuneNascita;
	}

	/**
	 * sigla della provincia (maiuscolo)
	 **/

	@JsonProperty("provincia_nascita")
	@Size(min = 2, max = 2)
	public String getProvinciaNascita() {
		return provinciaNascita;
	}

	public void setProvinciaNascita(String provinciaNascita) {
		this.provinciaNascita = provinciaNascita;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ModelPersonaSintesi modelPersonaSintesi = (ModelPersonaSintesi) o;
		return Objects.equals(cf, modelPersonaSintesi.cf) && Objects.equals(nome, modelPersonaSintesi.nome)
				&& Objects.equals(cognome, modelPersonaSintesi.cognome)
				&& Objects.equals(dataNascita, modelPersonaSintesi.dataNascita)
				&& Objects.equals(statoNascita, modelPersonaSintesi.statoNascita)
				&& Objects.equals(comuneNascita, modelPersonaSintesi.comuneNascita)
				&& Objects.equals(provinciaNascita, modelPersonaSintesi.provinciaNascita);
	}

	@Override
	public int hashCode() {
		return Objects.hash(cf, nome, cognome, dataNascita, statoNascita, comuneNascita, provinciaNascita);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ModelPersonaSintesi {\n");

		sb.append("    cf: ").append(toIndentedString(cf)).append("\n");
		sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
		sb.append("    cognome: ").append(toIndentedString(cognome)).append("\n");
		sb.append("    dataNascita: ").append(toIndentedString(dataNascita)).append("\n");
		sb.append("    statoNascita: ").append(toIndentedString(statoNascita)).append("\n");
		sb.append("    comuneNascita: ").append(toIndentedString(comuneNascita)).append("\n");
		sb.append("    provinciaNascita: ").append(toIndentedString(provinciaNascita)).append("\n");
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
