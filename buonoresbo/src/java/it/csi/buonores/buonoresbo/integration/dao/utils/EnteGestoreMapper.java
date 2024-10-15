/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbo.dto.ModelEnteGestore;

public class EnteGestoreMapper implements RowMapper<ModelEnteGestore> {

	@Override
	public ModelEnteGestore mapRow(ResultSet rs, int rowNum) throws SQLException {
		ModelEnteGestore entiGestori = new ModelEnteGestore();
		entiGestori.setIdEnteGestore(rs.getInt("ente_gestore_id"));
		entiGestori.setDenominazioneEnte(rs.getString("ente_gestore_denominazione"));
		return entiGestori;
	}

}
