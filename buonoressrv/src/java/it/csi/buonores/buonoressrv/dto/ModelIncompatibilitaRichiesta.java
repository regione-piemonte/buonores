/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
//TAG PROD DOM 2.1.0-001
public class ModelIncompatibilitaRichiesta   {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [explicit-as-modeled] 
  
  private Boolean incompatibilitaPerContratto = null;
  private Boolean nessunaIncompatibilita = null;
  

  @JsonProperty("nessuna_incompatibilita") 
 
  public Boolean isNessunaIncompatibilita() {
    return nessunaIncompatibilita;
  }
  public void setNessunaIncompatibilita(Boolean nessunaIncompatibilita) {
    this.nessunaIncompatibilita = nessunaIncompatibilita;
  }
  
  @JsonProperty("incompatibilita_per_contratto") 
  
  public Boolean isIncompatibilitaPerContratto() {
    return incompatibilitaPerContratto;
  }
  public void setIncompatibilitaPerContratto(Boolean incompatibilitaPerContratto) {
    this.incompatibilitaPerContratto = incompatibilitaPerContratto;
  }
  
@Override
public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("ModelIncompatibilitaRichiesta [incompatibilitaPerContratto=");
	builder.append(incompatibilitaPerContratto);
	builder.append(", nessunaIncompatibilita=");
	builder.append(nessunaIncompatibilita);
	builder.append("]");
	return builder.toString();
}

}

