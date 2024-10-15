/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresrend.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresrend.dto.ModelEnteGestore;

public class ListaEntiGestoriMapper implements RowMapper<ModelEnteGestore> {

	@Override
	public ModelEnteGestore mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		ModelEnteGestore result = new ModelEnteGestore();

		result.setEnteGestoreCodiceFiscale(resultSet.getString("ente_gestore_codice_fiscale"));
		result.setEnteGestoreDenominazione(resultSet.getString("ente_gestore_denominazione"));
		result.setEnteGestoreIndirizzo(resultSet.getString("ente_gestore_indirizzo"));
		result.setEnteGestoreTipo(resultSet.getString("ente_gestore_tipo"));
		result.setEnteGestoreCodiceRegionale(resultSet.getString("ente_gestore_codice_regionale"));
		result.setComuniIstat(resultSet.getString("comuni_istat").split(","));
		result.setSoggetti(resultSet.getString("soggetti").split(","));

		return result;
	}
}
