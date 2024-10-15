/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbo.dto.ModelEmailDetId;

public class EmailDetIdMapper implements RowMapper<ModelEmailDetId> {

	@Override
	public ModelEmailDetId mapRow(ResultSet rs, int rowNum) throws SQLException {

		// Richiesta
		ModelEmailDetId richiesta = new ModelEmailDetId();
		richiesta.setDomandaId(rs.getBigDecimal("domanda_id"));
		richiesta.setEmail(rs.getString("ente_gestore_email"));
		return richiesta;

	}

}
