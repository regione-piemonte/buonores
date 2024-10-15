/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbo.dto.ModelStatoBuono;

public class BuonoStatoMapper implements RowMapper<ModelStatoBuono> {

	@Override
	public ModelStatoBuono mapRow(ResultSet rs, int rowNum) throws SQLException {

		ModelStatoBuono statoBuono = new ModelStatoBuono();
		statoBuono.setBuonoStatoId(rs.getLong("buono_stato_id"));
		statoBuono.setBuonoStatoCod(rs.getString("buono_stato_cod"));
		statoBuono.setBuonoStatoDesc(rs.getString("buono_stato_desc"));

		return statoBuono;
	}

}
