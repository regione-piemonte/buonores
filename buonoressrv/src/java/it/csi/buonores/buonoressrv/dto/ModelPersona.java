/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.dto;

import java.util.Date;
import java.util.Objects;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelPersona {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private String cf = null;
	private String nome = null;
	private String cognome = null;
	private Date dataNascita = null;
	private String statoNascita = null;
	private String comuneNascita = null;
	private String provinciaNascita = null;
	private Date dataDecesso = null;
	private String indirizzoResidenza = null;
	private String comuneResidenza = null;
	private String provinciaResidenza = null;

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

	/**
	 * null per le persone attualmente in vita
	 **/

	@JsonProperty("data_decesso")
	public Date getDataDecesso() {
		return dataDecesso;
	}

	public void setDataDecesso(Date dataDecesso) {
		this.dataDecesso = dataDecesso;
	}

	/**
	 * indirizzo di residenza
	 **/

	@JsonProperty("indirizzo_residenza")
	public String getIndirizzoResidenza() {
		return indirizzoResidenza;
	}

	public void setIndirizzoResidenza(String indirizzoResidenza) {
		this.indirizzoResidenza = indirizzoResidenza;
	}

	/**
	 * comune di residenza
	 **/

	@JsonProperty("comune_residenza")
	public String getComuneResidenza() {
		return comuneResidenza;
	}

	public void setComuneResidenza(String comuneResidenza) {
		this.comuneResidenza = comuneResidenza;
	}

	/**
	 * sigla della provincia (maiuscolo)
	 **/

	@JsonProperty("provincia_residenza")
	@Size(min = 2, max = 2)
	public String getProvinciaResidenza() {
		return provinciaResidenza;
	}

	public void setProvinciaResidenza(String provinciaResidenza) {
		this.provinciaResidenza = provinciaResidenza;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ModelPersona modelPersona = (ModelPersona) o;
		return Objects.equals(cf, modelPersona.cf) && Objects.equals(nome, modelPersona.nome)
				&& Objects.equals(cognome, modelPersona.cognome)
				&& Objects.equals(dataNascita, modelPersona.dataNascita)
				&& Objects.equals(statoNascita, modelPersona.statoNascita)
				&& Objects.equals(comuneNascita, modelPersona.comuneNascita)
				&& Objects.equals(provinciaNascita, modelPersona.provinciaNascita)
				&& Objects.equals(dataDecesso, modelPersona.dataDecesso)
				&& Objects.equals(indirizzoResidenza, modelPersona.indirizzoResidenza)
				&& Objects.equals(comuneResidenza, modelPersona.comuneResidenza)
				&& Objects.equals(provinciaResidenza, modelPersona.provinciaResidenza);
	}

	@Override
	public int hashCode() {
		return Objects.hash(cf, nome, cognome, dataNascita, statoNascita, comuneNascita, provinciaNascita, dataDecesso,
				indirizzoResidenza, comuneResidenza, provinciaResidenza);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ModelPersona {\n");

		sb.append("    cf: ").append(toIndentedString(cf)).append("\n");
		sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
		sb.append("    cognome: ").append(toIndentedString(cognome)).append("\n");
		sb.append("    dataNascita: ").append(toIndentedString(dataNascita)).append("\n");
		sb.append("    statoNascita: ").append(toIndentedString(statoNascita)).append("\n");
		sb.append("    comuneNascita: ").append(toIndentedString(comuneNascita)).append("\n");
		sb.append("    provinciaNascita: ").append(toIndentedString(provinciaNascita)).append("\n");
		sb.append("    dataDecesso: ").append(toIndentedString(dataDecesso)).append("\n");
		sb.append("    indirizzoResidenza: ").append(toIndentedString(indirizzoResidenza)).append("\n");
		sb.append("    comuneResidenza: ").append(toIndentedString(comuneResidenza)).append("\n");
		sb.append("    provinciaResidenza: ").append(toIndentedString(provinciaResidenza)).append("\n");
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
