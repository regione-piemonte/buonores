/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.dto;

import java.util.Objects;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelRichiestaDomicilioDestinatario {
  // verra' utilizzata la seguente strategia serializzazione degli attributi:
  // [explicit-as-modeled]

  private String indirizzo = null;
  private String comune = null;
  private String provincia = null;

  /**
   * indirizzo del domicilio
   **/

  @JsonProperty("indirizzo")

  public String getIndirizzo() {
    return indirizzo;
  }

  public void setIndirizzo(String indirizzo) {
    this.indirizzo = indirizzo;
  }

  /**
   * comune del domicilio
   **/

  @JsonProperty("comune")

  public String getComune() {
    return comune;
  }

  public void setComune(String comune) {
    this.comune = comune;
  }

  /**
   * sigla della provincia (maiuscolo)
   **/

  @JsonProperty("provincia")

  @Size(min = 2, max = 2)
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
    ModelRichiestaDomicilioDestinatario modelRichiestaDomicilioDestinatario = (ModelRichiestaDomicilioDestinatario) o;
    return Objects.equals(indirizzo, modelRichiestaDomicilioDestinatario.indirizzo) &&
        Objects.equals(comune, modelRichiestaDomicilioDestinatario.comune) &&
        Objects.equals(provincia, modelRichiestaDomicilioDestinatario.provincia);
  }

  @Override
  public int hashCode() {
    return Objects.hash(indirizzo, comune, provincia);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModelRichiestaDomicilioDestinatario {\n");

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
