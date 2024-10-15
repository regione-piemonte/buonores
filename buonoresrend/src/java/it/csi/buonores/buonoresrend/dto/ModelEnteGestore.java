/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresrend.dto;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelEnteGestore {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private String enteGestoreCodiceFiscale = null;
	private String enteGestoreDenominazione = null;
	private String enteGestoreIndirizzo = null;
	private String enteGestoreTipo = null;
	private String enteGestoreCodiceRegionale = null;
	private String[] comuniIstat = null;
	private String[] soggetti = null;
	
	@JsonProperty("ente_gestore_codice_fiscale")
	public String getEnteGestoreCodiceFiscale() {
		return enteGestoreCodiceFiscale;
	}
	
	public void setEnteGestoreCodiceFiscale(String enteGestoreCodiceFiscale) {
		this.enteGestoreCodiceFiscale = enteGestoreCodiceFiscale;
	}
	
	@JsonProperty("ente_gestore_denominazione")
	public String getEnteGestoreDenominazione() {
		return enteGestoreDenominazione;
	}
	
	public void setEnteGestoreDenominazione(String enteGestoreDenominazione) {
		this.enteGestoreDenominazione = enteGestoreDenominazione;
	}
	
	@JsonProperty("ente_gestore_indirizzo")
	public String getEnteGestoreIndirizzo() {
		return enteGestoreIndirizzo;
	}
	
	public void setEnteGestoreIndirizzo(String enteGestoreIndirizzo) {
		this.enteGestoreIndirizzo = enteGestoreIndirizzo;
	}
	
	@JsonProperty("ente_gestore_tipo")
	public String getEnteGestoreTipo() {
		return enteGestoreTipo;
	}
	
	public void setEnteGestoreTipo(String enteGestoreTipo) {
		this.enteGestoreTipo = enteGestoreTipo;
	}
	
	@JsonProperty("ente_gestore_codice_regionale")
	public String getEnteGestoreCodiceRegionale() {
		return enteGestoreCodiceRegionale;
	}
	
	public void setEnteGestoreCodiceRegionale(String enteGestoreCodiceRegionale) {
		this.enteGestoreCodiceRegionale = enteGestoreCodiceRegionale;
	}
	
	@JsonProperty("comuni_istat")
	public String[] getComuniIstat() {
		return comuniIstat;
	}
	
	public void setComuniIstat(String[] comuniIstat) {
		this.comuniIstat = comuniIstat;
	}
	
	@JsonProperty("soggetti")
	public String[] getSoggetti() {
		return soggetti;
	}
	
	public void setSoggetti(String[] soggetti) {
		this.soggetti = soggetti;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(comuniIstat);
		result = prime * result + Arrays.hashCode(soggetti);
		result = prime * result + Objects.hash(enteGestoreCodiceFiscale, enteGestoreCodiceRegionale,
				enteGestoreDenominazione, enteGestoreIndirizzo, enteGestoreTipo);
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModelEnteGestore other = (ModelEnteGestore) obj;
		return Arrays.equals(comuniIstat, other.comuniIstat)
				&& Objects.equals(enteGestoreCodiceFiscale, other.enteGestoreCodiceFiscale)
				&& Objects.equals(enteGestoreCodiceRegionale, other.enteGestoreCodiceRegionale)
				&& Objects.equals(enteGestoreDenominazione, other.enteGestoreDenominazione)
				&& Objects.equals(enteGestoreIndirizzo, other.enteGestoreIndirizzo)
				&& Objects.equals(enteGestoreTipo, other.enteGestoreTipo) && Arrays.equals(soggetti, other.soggetti);
	}
	
	@Override
	public String toString() {
		return "ModelEnteGestore [enteGestoreCodiceFiscale=" + enteGestoreCodiceFiscale + ", enteGestoreDenominazione="
				+ enteGestoreDenominazione + ", enteGestoreIndirizzo=" + enteGestoreIndirizzo + ", enteGestoreTipo="
				+ enteGestoreTipo + ", enteGestoreCodiceRegionale=" + enteGestoreCodiceRegionale + ", comuniIstat="
				+ Arrays.toString(comuniIstat) + ", soggetti=" + Arrays.toString(soggetti) + "]";
	}
}
