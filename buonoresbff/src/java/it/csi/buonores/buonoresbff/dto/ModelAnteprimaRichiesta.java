/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.dto;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelAnteprimaRichiesta {
  // verra' utilizzata la seguente strategia serializzazione degli attributi:
  // [explicit-as-modeled]

  private ModelProtocollo protocollo = null;
  private String numero = null;
  private String stato = null;
  private Date dataAggiornamento = null;
  private ModelPersona richiedente = null;
  private ModelPersona destinatario = null;
  private String note = null;

  @JsonProperty("protocollo")
  public ModelProtocollo getProtocollo() {
    return protocollo;
  }

  public void setProtocollo(ModelProtocollo protocollo) {
    this.protocollo = protocollo;
  }

  /**
   * il numero della richiesta
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
   * data di ultimo aggiornamento della richiesta
   **/

  @JsonProperty("data_aggiornamento")

  public Date getDataAggiornamento() {
    return dataAggiornamento;
  }

  public void setDataAggiornamento(Date dataAggiornamento) {
    this.dataAggiornamento = dataAggiornamento;
  }

  @JsonProperty("richiedente")

  public ModelPersona getRichiedente() {
    return richiedente;
  }

  public void setRichiedente(ModelPersona richiedente) {
    this.richiedente = richiedente;
  }

  @JsonProperty("destinatario")

  public ModelPersona getDestinatario() {
    return destinatario;
  }

  public void setDestinatario(ModelPersona destinatario) {
    this.destinatario = destinatario;
  }

  /**
   * eventuali note (es. in caso di attesa rettifica)
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
    ModelAnteprimaRichiesta modelAnteprimaRichiesta = (ModelAnteprimaRichiesta) o;
    return Objects.equals(protocollo, modelAnteprimaRichiesta.protocollo) &&
        Objects.equals(numero, modelAnteprimaRichiesta.numero) &&
        Objects.equals(stato, modelAnteprimaRichiesta.stato) &&
        Objects.equals(dataAggiornamento, modelAnteprimaRichiesta.dataAggiornamento) &&
        Objects.equals(richiedente, modelAnteprimaRichiesta.richiedente) &&
        Objects.equals(destinatario, modelAnteprimaRichiesta.destinatario) &&
        Objects.equals(note, modelAnteprimaRichiesta.note);
  }

  @Override
  public int hashCode() {
    return Objects.hash(protocollo, numero, stato, dataAggiornamento, richiedente, destinatario, note);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModelAnteprimaRichiesta {\n");

    sb.append("    protocollo: ").append(toIndentedString(protocollo)).append("\n");
    sb.append("    numero: ").append(toIndentedString(numero)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    dataAggiornamento: ").append(toIndentedString(dataAggiornamento)).append("\n");
    sb.append("    richiedente: ").append(toIndentedString(richiedente)).append("\n");
    sb.append("    destinatario: ").append(toIndentedString(destinatario)).append("\n");
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
