/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbo.dto.ModelProfili;

public class ProfiliMapper implements RowMapper<ModelProfili> {

	@Override
	public ModelProfili mapRow(ResultSet rs, int rowNum) throws SQLException {

		ModelProfili profilo = new ModelProfili();
		profilo.setCodProfilo(rs.getString("profilo_cod"));
		profilo.setDescProfilo(rs.getString("profilo_desc"));

		return profilo;

	}

}
