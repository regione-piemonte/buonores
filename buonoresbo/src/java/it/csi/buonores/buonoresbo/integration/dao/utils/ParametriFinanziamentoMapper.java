/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbo.dto.ModelParametriFinanziamento;

public class ParametriFinanziamentoMapper implements RowMapper<ModelParametriFinanziamento> {

	@Override
	public ModelParametriFinanziamento mapRow(ResultSet rs, int rowNum) throws SQLException {
		ModelParametriFinanziamento parametro = new ModelParametriFinanziamento();
		parametro.setImportoMensile(rs.getBigDecimal("importo_mensile"));
		parametro.setMesi(rs.getByte("mesi"));
		parametro.setSoggettiFinanziati(rs.getInt("soggetti_finanziati"));
		parametro.setImportoTotale(rs.getBigDecimal("importo_totale"));
		parametro.setImportoResiduo(rs.getBigDecimal("importo_residuo"));
		parametro.setImportoDistribuibile(rs.getBigDecimal("importo_distribuito"));
		parametro.setSoggettiFinanziabili(rs.getInt("soggetti_finanziabili"));
		parametro.setAree(rs.getString("area"));
		return parametro;
	}

}
