/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbo.dto.ModelAllegatoBuono;

public class AllegatoBuonoMapper implements RowMapper<ModelAllegatoBuono> {
	@Override
	public ModelAllegatoBuono mapRow(ResultSet rs, int rowNum) throws SQLException {
		ModelAllegatoBuono result = new ModelAllegatoBuono();

		result.setAllegatoId(rs.getLong("allegato_id"));
		result.setAllegatoTipo(rs.getString("allegato_tipo_cod"));
		result.setAllegatoFilename(rs.getString("file_name"));
		return result;
	}
}
