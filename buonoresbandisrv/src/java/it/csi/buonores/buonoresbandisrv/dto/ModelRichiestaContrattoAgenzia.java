/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelRichiestaContrattoAgenzia {
  // verra' utilizzata la seguente strategia serializzazione degli attributi:
  // [explicit-as-modeled]

  private String cf = null;

  /**
   * codice fiscale dell&#39;agenzia/cooperativa
   **/

  @JsonProperty("cf")

  public String getCf() {
    return cf;
  }

  public void setCf(String cf) {
    this.cf = cf;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ModelRichiestaContrattoAgenzia modelRichiestaContrattoAgenzia = (ModelRichiestaContrattoAgenzia) o;
    return Objects.equals(cf, modelRichiestaContrattoAgenzia.cf);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cf);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModelRichiestaContrattoAgenzia {\n");

    sb.append("    cf: ").append(toIndentedString(cf)).append("\n");
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
