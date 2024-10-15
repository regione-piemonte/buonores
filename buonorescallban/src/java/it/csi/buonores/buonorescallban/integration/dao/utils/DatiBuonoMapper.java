/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonorescallban.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonorescallban.dto.DatiBuono;

public class DatiBuonoMapper implements RowMapper<DatiBuono> {

	@Override
	public DatiBuono mapRow(ResultSet rs, int rowNum) throws SQLException {

		DatiBuono buono = new DatiBuono();
		buono.setBuonoId(rs.getBigDecimal("buono_id"));
		buono.setStato(rs.getString("buono_stato_cod"));

		return buono;
	}
}
