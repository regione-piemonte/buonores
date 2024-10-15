/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbo.dto.custom.ModelGetAllegato;

public class AllegatoGetMapper implements RowMapper<ModelGetAllegato> {

	@Override
	public ModelGetAllegato mapRow(ResultSet rs, int rowNum) throws SQLException {
		ModelGetAllegato result = new ModelGetAllegato();
		result.setFilePath(rs.getString("file_path"));
		result.setFileName(rs.getString("file_name"));

		return result;
	}

}
