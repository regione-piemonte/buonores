/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "spese_progetto")
@XmlType(propOrder = { "codiceTipologiaSpesa", "descrTipologiaSpesa", "importoSpesaRichiestaFinanziata",
		"importoSpesaRichiestaNonFinanziata", "codDettIntervento", "flagIvaCosto" })
public class ModelSpeseProgetto {

	private String codiceTipologiaSpesa;
	private String descrTipologiaSpesa;
	private String importoSpesaRichiestaFinanziata;
	private String importoSpesaRichiestaNonFinanziata;
	private String codDettIntervento;
	private String flagIvaCosto;

	public ModelSpeseProgetto() {
	}

	@XmlElement(name = "codice_tipologia_spesa")
	public String getCodiceTipologiaSpesa() {
		return codiceTipologiaSpesa;
	}

	public void setCodiceTipologiaSpesa(String codiceTipologiaSpesa) {
		this.codiceTipologiaSpesa = codiceTipologiaSpesa;
	}

	@XmlElement(name = "descr_tipologia_spesa")
	public String getDescrTipologiaSpesa() {
		return descrTipologiaSpesa;
	}

	public void setDescrTipologiaSpesa(String descrTipologiaSpesa) {
		this.descrTipologiaSpesa = descrTipologiaSpesa;
	}

	@XmlElement(name = "importo_spesa_richiesta_finanziata")
	public String getImportoSpesaRichiestaFinanziata() {
		return importoSpesaRichiestaFinanziata;
	}

	public void setImportoSpesaRichiestaFinanziata(String importoSpesaRichiestaFinanziata) {
		this.importoSpesaRichiestaFinanziata = importoSpesaRichiestaFinanziata;
	}

	@XmlElement(name = "importo_spesa_richiesta_non_finanziata")
	public String getImportoSpesaRichiestaNonFinanziata() {
		return importoSpesaRichiestaNonFinanziata;
	}

	public void setImportoSpesaRichiestaNonFinanziata(String importoSpesaRichiestaNonFinanziata) {
		this.importoSpesaRichiestaNonFinanziata = importoSpesaRichiestaNonFinanziata;
	}

	@XmlElement(name = "cod_dett_intervento")
	public String getCodDettIntervento() {
		return codDettIntervento;
	}

	public void setCodDettIntervento(String codDettIntervento) {
		this.codDettIntervento = codDettIntervento;
	}

	@XmlElement(name = "flag_iva_costo")
	public String getFlagIvaCosto() {
		return flagIvaCosto;
	}

	public void setFlagIvaCosto(String flagIvaCosto) {
		this.flagIvaCosto = flagIvaCosto;
	}

}
