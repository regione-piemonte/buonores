/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelFornitore {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private Integer id = null;
	private String codice = null;
	private String codiceArpe = null;
	private String nome = null;
	private String indirizzo = null;
	private String comune = null;
	private String provincia = null;
	private String piva = null;
	private ModelStrutturaSedeLegale sedeLegale = null;

	@JsonProperty("id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@JsonProperty("codice")
	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	@JsonProperty("codice_arpe")
	public String getCodiceArpe() {
		return codiceArpe;
	}

	public void setCodiceArpe(String codiceArpe) {
		this.codiceArpe = codiceArpe;
	}

	@JsonProperty("nome")
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@JsonProperty("indirizzo")
	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	@JsonProperty("comune")
	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	@JsonProperty("provincia")
	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	@JsonProperty("piva")
	public String getPiva() {
		return piva;
	}

	public void setPiva(String piva) {
		this.piva = piva;
	}

	@JsonProperty("sede_legale")
	public ModelStrutturaSedeLegale getSedeLegale() {
		return sedeLegale;
	}

	public void setSedeLegale(ModelStrutturaSedeLegale sedeLegale) {
		this.sedeLegale = sedeLegale;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codice, codiceArpe, comune, id, indirizzo, nome, piva, provincia, sedeLegale);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModelFornitore other = (ModelFornitore) obj;
		return Objects.equals(codice, other.codice) && Objects.equals(codiceArpe, other.codiceArpe)
				&& Objects.equals(comune, other.comune) && Objects.equals(id, other.id)
				&& Objects.equals(indirizzo, other.indirizzo) && Objects.equals(nome, other.nome)
				&& Objects.equals(piva, other.piva) && Objects.equals(provincia, other.provincia)
				&& Objects.equals(sedeLegale, other.sedeLegale);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ModelContratto {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
		sb.append("    codiceArpe: ").append(toIndentedString(codiceArpe)).append("\n");
		sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
		sb.append("    indirizzo: ").append(toIndentedString(indirizzo)).append("\n");
		sb.append("    comune: ").append(toIndentedString(comune)).append("\n");
		sb.append("    provincia: ").append(toIndentedString(provincia)).append("\n");
		sb.append("    piva: ").append(toIndentedString(piva)).append("\n");
		sb.append("    sedeLegale: ").append(toIndentedString(sedeLegale)).append("\n");
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
