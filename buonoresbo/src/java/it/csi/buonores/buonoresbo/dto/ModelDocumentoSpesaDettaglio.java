/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ModelDocumentoSpesaDettaglio {
	private long idDocSpesaDettaglio;
	private Date documentoSpesaDettaglioData;
	private String importo;
	private List<ModelAllegatoBuono> allegati;

	public long getIdDocSpesaDettaglio() {
		return idDocSpesaDettaglio;
	}

	public void setIdDocSpesaDettaglio(long idDocSpesaDettaglio) {
		this.idDocSpesaDettaglio = idDocSpesaDettaglio;
	}

	public Date getDocumentoSpesaDettaglioData() {
		return documentoSpesaDettaglioData;
	}

	public void setDocumentoSpesaDettaglioData(Date documentoSpesaDettaglioData) {
		this.documentoSpesaDettaglioData = documentoSpesaDettaglioData;
	}

	public String getImporto() {
		return importo;
	}

	public void setImporto(String importo) {
		this.importo = importo;
	}

	public List<ModelAllegatoBuono> getAllegati() {
		return allegati;
	}

	public void setAllegati(List<ModelAllegatoBuono> allegati) {
		this.allegati = allegati;
	}

	@Override
	public String toString() {
		return "ModelDocumentoSpesaDettaglio [idDocSpesaDettaglio=" + idDocSpesaDettaglio
				+ ", documentoSpesaDettaglioData=" + documentoSpesaDettaglioData + ", importo=" + importo
				+ ", allegati=" + allegati + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(allegati, documentoSpesaDettaglioData, idDocSpesaDettaglio, importo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModelDocumentoSpesaDettaglio other = (ModelDocumentoSpesaDettaglio) obj;
		return Objects.equals(allegati, other.allegati)
				&& Objects.equals(documentoSpesaDettaglioData, other.documentoSpesaDettaglioData)
				&& idDocSpesaDettaglio == other.idDocSpesaDettaglio && Objects.equals(importo, other.importo);
	}
}
