/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbff.dto.ModelAllegato;

public class AllegatoContrattoMapper implements RowMapper<ModelAllegato> {

	@Override
	public ModelAllegato mapRow(ResultSet rs, int rowNum) throws SQLException {
		ModelAllegato result = new ModelAllegato();
		result.setId(rs.getLong("allegato_id"));
		result.setTipo(rs.getString("allegato_tipo_cod"));
		result.setFilename(rs.getString("file_name"));

		return result;
	}

}
