/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.dto;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelInfoServizio {
  // verra' utilizzata la seguente strategia serializzazione degli attributi:
  // [explicit-as-modeled]

  private String nome = null;
  private String descrizione = null;
  private Date data = null;
  private Boolean servizioAttivo = null;

  /**
   * nome del servizio
   **/

  @JsonProperty("nome")
  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  /**
   * descrizione del servizio
   **/

  @JsonProperty("descrizione")
  public String getDescrizione() {
    return descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  @JsonProperty("data")
  public Date getData() {
    return data;
  }

  public void setData(Date data) {
    this.data = data;
  }

  /**
   * boolean di utilita per eventuali test
   **/

  @JsonProperty("servizio_attivo")
  public Boolean isServizioAttivo() {
    return servizioAttivo;
  }

  public void setServizioAttivo(Boolean servizioAttivo) {
    this.servizioAttivo = servizioAttivo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ModelInfoServizio modelInfoServizio = (ModelInfoServizio) o;
    return Objects.equals(nome, modelInfoServizio.nome) &&
        Objects.equals(descrizione, modelInfoServizio.descrizione) &&
        Objects.equals(data, modelInfoServizio.data) &&
        Objects.equals(servizioAttivo, modelInfoServizio.servizioAttivo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nome, descrizione, data, servizioAttivo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModelInfoServizio {\n");

    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    servizioAttivo: ").append(toIndentedString(servizioAttivo)).append("\n");
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
