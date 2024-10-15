/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbo.dto.ModelAzione;

public class AzioniMapper implements RowMapper<ModelAzione> {

	@Override
	public ModelAzione mapRow(ResultSet rs, int rowNum) throws SQLException {

		ModelAzione azione = new ModelAzione();
		azione.setCodAzione(rs.getString("azione_cod"));
		azione.setDescAzione(rs.getString("azione_desc"));

		return azione;

	}

}
