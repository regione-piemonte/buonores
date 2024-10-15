/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbo.dto.custom.ModelGetAllegatoExt;

public class AllegatoExtMapper implements RowMapper<ModelGetAllegatoExt> {

	@Override
	public ModelGetAllegatoExt mapRow(ResultSet rs, int rowNum) throws SQLException {
		ModelGetAllegatoExt result = new ModelGetAllegatoExt();
		result.setFilePath(rs.getString("file_path"));
		result.setFileName(rs.getString("file_name"));
		result.setFileType(rs.getString("file_type"));
		result.setAllegatoId(rs.getBigDecimal("allegato_tipo_id"));
		result.setAllegatoTipoCod(rs.getString("allegato_tipo_cod"));
		result.setSportelloId(rs.getBigDecimal("sportello_id"));

		return result;
	}

}
