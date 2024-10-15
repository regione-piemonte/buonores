/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.dto;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelProtocollo {
  // verra' utilizzata la seguente strategia serializzazione degli attributi:
  // [explicit-as-modeled]

  private Date data = null;
  private String numero = null;
  private String tipo = null;

  /**
   * Data di protocollazione
   **/

  @JsonProperty("data")
  public Date getData() {
    return data;
  }

  public void setData(Date data) {
    this.data = data;
  }

  /**
   * Numero di protocollo
   **/

  @JsonProperty("numero")
  public String getNumero() {
    return numero;
  }

  public void setNumero(String numero) {
    this.numero = numero;
  }

  /**
   * Tipo di protocollo
   **/

  @JsonProperty("tipo")
  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ModelProtocollo modelProtocollo = (ModelProtocollo) o;
    return Objects.equals(data, modelProtocollo.data) &&
        Objects.equals(numero, modelProtocollo.numero) &&
        Objects.equals(tipo, modelProtocollo.tipo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, numero, tipo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModelProtocollo {\n");

    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    numero: ").append(toIndentedString(numero)).append("\n");
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
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
