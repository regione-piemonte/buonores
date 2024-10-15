/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbff.dto.ModelCronologia;

public class CronologiaMapper implements RowMapper<ModelCronologia> {

	@Override
	public ModelCronologia mapRow(ResultSet rs, int rowNum) throws SQLException {
		ModelCronologia result = new ModelCronologia();
		result.setNumero(rs.getString("numero"));
		result.setStato(rs.getString("codStato"));
		result.setDataAggiornamento(rs.getDate("dataCreazione"));
		result.setNote(rs.getString("note"));

		return result;
	}

}
