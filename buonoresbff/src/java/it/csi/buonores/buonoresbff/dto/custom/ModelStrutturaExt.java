/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.dto.custom;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.csi.buonores.buonoresbff.util.Constants;
import it.csi.buonores.buonoresbff.util.Util;

public class ModelStrutturaExt {

	private Long strWelfareId;
	private String idStruttura;
	private String strResNome;
	private String strResIndirizzo;
	private String strResComune;
	private String strResProvincia;
	private String entGestCfPiva;
	private Date dataAdesione;
	private String entGestComune;
	private String entGestIndirizzo;
	private String entGestProvincia;
	private String entGestRagioneSociale;
	private ModelStrutturaCoordExt struttura;

	public ModelStrutturaExt() {
	}

	public ModelStrutturaExt(Long strWelfareId, String idStruttura, String strResNome, String strResIndirizzo,
			String strResComune, String strResProvincia, String entGestCfPiva, Date dataAdesione, String entGestComune,
			String entGestIndirizzo, String entGestProvincia, String entGestRagioneSociale,
			ModelStrutturaCoordExt struttura) {
		super();
		this.strWelfareId = strWelfareId;
		this.idStruttura = idStruttura;
		this.strResNome = strResNome;
		this.strResIndirizzo = strResIndirizzo;
		this.strResComune = strResComune;
		this.strResProvincia = strResProvincia;
		this.entGestCfPiva = entGestCfPiva;
		this.dataAdesione = dataAdesione;
		this.entGestComune = entGestComune;
		this.entGestIndirizzo = entGestIndirizzo;
		this.entGestProvincia = entGestProvincia;
		this.entGestRagioneSociale = entGestRagioneSociale;
		this.struttura = struttura;
	}

	public ModelStrutturaExt(Long strWelfareId, String idStruttura, String strResNome, String strResIndirizzo,
			String strResComune, String strResProvincia, String entGestCfPiva, String dataAdesione,
			String entGestComune,
			String entGestIndirizzo, String entGestProvincia, String entGestRagioneSociale,
			ModelStrutturaCoordExt struttura) {
		super();
		this.strWelfareId = strWelfareId;
		this.idStruttura = idStruttura;
		this.strResNome = strResNome;
		this.strResIndirizzo = strResIndirizzo;
		this.strResComune = strResComune;
		this.strResProvincia = strResProvincia;
		this.entGestCfPiva = entGestCfPiva;
		this.dataAdesione = Util.getData(dataAdesione, Constants.DATE_TIME_FORMAT_PATTERN);
		this.entGestComune = entGestComune;
		this.entGestIndirizzo = entGestIndirizzo;
		this.entGestProvincia = entGestProvincia;
		this.entGestRagioneSociale = entGestRagioneSociale;
		this.struttura = struttura;
	}

	@JsonProperty("strWelfareId")
	public Long getStrWelfareId() {
		return strWelfareId;
	}

	public void setStrWelfareId(Long strWelfareId) {
		this.strWelfareId = strWelfareId;
	}

	@JsonProperty("idStruttura")
	public String getIdStruttura() {
		return idStruttura;
	}

	public void setIdStruttura(String idStruttura) {
		this.idStruttura = idStruttura;
	}

	@JsonProperty("strResNome")
	public String getStrResNome() {
		return strResNome;
	}

	public void setStrResNome(String strResNome) {
		this.strResNome = strResNome;
	}

	@JsonProperty("strResIndirizzo")
	public String getStrResIndirizzo() {
		return strResIndirizzo;
	}

	public void setStrResIndirizzo(String strResIndirizzo) {
		this.strResIndirizzo = strResIndirizzo;
	}

	@JsonProperty("strResComune")
	public String getStrResComune() {
		return strResComune;
	}

	public void setStrResComune(String strResComune) {
		this.strResComune = strResComune;
	}

	@JsonProperty("strResProvincia")
	public String getStrResProvincia() {
		return strResProvincia;
	}

	public void setStrResProvincia(String strResProvincia) {
		this.strResProvincia = strResProvincia;
	}

	@JsonProperty("entGestCfPiva")
	public String getEntGestCfPiva() {
		return entGestCfPiva;
	}

	public void setEntGestCfPiva(String entGestCfPiva) {
		this.entGestCfPiva = entGestCfPiva;
	}

	@JsonProperty("dataAdesione")
	public Date getDataAdesione() {
		return dataAdesione;
	}

	public void setDataAdesione(Date dataAdesione) {
		this.dataAdesione = dataAdesione;
	}

	@JsonProperty("entGestComune")
	public String getEntGestComune() {
		return entGestComune;
	}

	public void setEntGestComune(String entGestComune) {
		this.entGestComune = entGestComune;
	}

	@JsonProperty("entGestIndirizzo")
	public String getEntGestIndirizzo() {
		return entGestIndirizzo;
	}

	public void setEntGestIndirizzo(String entGestIndirizzo) {
		this.entGestIndirizzo = entGestIndirizzo;
	}

	@JsonProperty("entGestProvincia")
	public String getEntGestProvincia() {
		return entGestProvincia;
	}

	public void setEntGestProvincia(String entGestProvincia) {
		this.entGestProvincia = entGestProvincia;
	}

	@JsonProperty("entGestRagioneSociale")
	public String getEntGestRagioneSociale() {
		return entGestRagioneSociale;
	}

	public void setEntGestRagioneSociale(String entGestRagioneSociale) {
		this.entGestRagioneSociale = entGestRagioneSociale;
	}

	@JsonProperty("struttura")
	public ModelStrutturaCoordExt getStruttura() {
		return struttura;
	}

	public void setStruttura(ModelStrutturaCoordExt struttura) {
		this.struttura = struttura;
	}
}
