/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelStruttura {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private String id = null;
	private String idArpe = null;
	private String nome = null;
	private String indirizzo = null;
	private String comune = null;
	private String provincia = null;
	private String piva = null;
	private ModelStrutturaSedeLegale sedeLegale = null;

	/**
	 * riferimento utilizzato internamente
	 **/

	@JsonProperty("id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * riferimento utilizzato internamente
	 **/

	@JsonProperty("id_arpe")
	public String getIdArpe() {
		return idArpe;
	}

	public void setIdArpe(String idArpe) {
		this.idArpe = idArpe;
	}

	/**
	 * etichetta esposta all&#39;utente
	 **/

	@JsonProperty("nome")
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * indirizzo della stuttura
	 **/

	@JsonProperty("indirizzo")
	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	/**
	 * comune di appartenenza della stuttura
	 **/

	@JsonProperty("comune")
	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	/**
	 * provincia di appartenza della struttura
	 **/

	@JsonProperty("provincia")
	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	/**
	 * partita iva del gestore della stuttura
	 **/

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
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ModelStruttura modelStruttura = (ModelStruttura) o;
		return Objects.equals(id, modelStruttura.id) && Objects.equals(idArpe, modelStruttura.idArpe)
				&& Objects.equals(nome, modelStruttura.nome) && Objects.equals(indirizzo, modelStruttura.indirizzo)
				&& Objects.equals(comune, modelStruttura.comune) && Objects.equals(provincia, modelStruttura.provincia)
				&& Objects.equals(piva, modelStruttura.piva) && Objects.equals(sedeLegale, modelStruttura.sedeLegale);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, idArpe, nome, indirizzo, comune, provincia, piva, sedeLegale);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ModelStruttura {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    idArpe: ").append(toIndentedString(idArpe)).append("\n");
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
