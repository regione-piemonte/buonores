/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelDatiDaModificare {
  // verra' utilizzata la seguente strategia serializzazione degli attributi:
  // [explicit-as-modeled]

  private String codice = null;
  private String descrizione = null;
  private BigDecimal id = null;
  private String nometabella = null;

  /**
   * codice utilizzato internamente
   **/

  @JsonProperty("id")
  public BigDecimal getId() {
    return id;
  }

  public void setId(BigDecimal id) {
    this.id = id;
  }

  @JsonProperty("nome_tabella")
  public String getNometabella() {
    return nometabella;
  }

  public void setNometabella(String nometabella) {
    this.nometabella = nometabella;
  }

  @JsonProperty("codice")

  public String getCodice() {
    return codice;
  }

  public void setCodice(String codice) {
    this.codice = codice;
  }

  /**
   * etichetta esposta all&#39;utente
   **/

  @JsonProperty("descrizione")

  public String getDescrizione() {
    return descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ModelDatiDaModificare modelDecodifica = (ModelDatiDaModificare) o;
    return Objects.equals(codice, modelDecodifica.codice) &&
        Objects.equals(descrizione, modelDecodifica.descrizione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codice, descrizione);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModelDecodifica {\n");

    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    etichetta: ").append(toIndentedString(descrizione)).append("\n");
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
