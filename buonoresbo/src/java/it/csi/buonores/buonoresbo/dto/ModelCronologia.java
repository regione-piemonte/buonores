/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelCronologia {
  // verra' utilizzata la seguente strategia serializzazione degli attributi:
  // [explicit-as-modeled]

  private String numero = null;
  private String stato = null;
  private Date dataAggiornamento = null;
  private String note = null;

  /**
   * numero della richiesta
   **/

  @JsonProperty("numero")

  public String getNumero() {
    return numero;
  }

  public void setNumero(String numero) {
    this.numero = numero;
  }

  /**
   * codice stato della richiesta
   **/

  @JsonProperty("stato")

  public String getStato() {
    return stato;
  }

  public void setStato(String stato) {
    this.stato = stato;
  }

  /**
   * data di aggiornamento dello stato della richiesta
   **/

  @JsonProperty("data_aggiornamento")

  public Date getDataAggiornamento() {
    return dataAggiornamento;
  }

  public void setDataAggiornamento(Date dataAggiornamento) {
    this.dataAggiornamento = dataAggiornamento;
  }

  /**
   * eventuali note
   **/

  @JsonProperty("note")

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ModelCronologia modelCronologia = (ModelCronologia) o;
    return Objects.equals(numero, modelCronologia.numero) &&
        Objects.equals(stato, modelCronologia.stato) &&
        Objects.equals(dataAggiornamento, modelCronologia.dataAggiornamento) &&
        Objects.equals(note, modelCronologia.note);
  }

  @Override
  public int hashCode() {
    return Objects.hash(numero, stato, dataAggiornamento, note);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModelCronologia {\n");

    sb.append("    numero: ").append(toIndentedString(numero)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    dataAggiornamento: ").append(toIndentedString(dataAggiornamento)).append("\n");
    sb.append("    note: ").append(toIndentedString(note)).append("\n");
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
