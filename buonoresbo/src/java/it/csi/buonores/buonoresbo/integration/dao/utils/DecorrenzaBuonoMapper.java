/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbo.dto.ModelDecorrenzaBuono;

public class DecorrenzaBuonoMapper implements RowMapper<ModelDecorrenzaBuono> {

	@Override
	public ModelDecorrenzaBuono mapRow(ResultSet rs, int rowNum) throws SQLException {
		ModelDecorrenzaBuono result = new ModelDecorrenzaBuono();

		result.setDecorrenzaInizio(rs.getDate("decorrenza_inizio"));
		result.setDecorrenzaFine(rs.getDate("decorrenza_fine"));
		return result;
	}

}
