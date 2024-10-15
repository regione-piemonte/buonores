/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbo.dto.ModelArea;

public class AreaMapper implements RowMapper<ModelArea> {

	@Override
	public ModelArea mapRow(ResultSet rs, int rowNum) throws SQLException {

		ModelArea area = new ModelArea();
		area.setAreaId(rs.getLong("area_id"));
		area.setAreaCod(rs.getString("area_cod"));
		area.setAreaDesc(rs.getString("area_desc"));

		return area;
	}

}
