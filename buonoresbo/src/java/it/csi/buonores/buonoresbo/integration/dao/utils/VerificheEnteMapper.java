/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbo.dto.ModelVerificheEnte;

public class VerificheEnteMapper implements RowMapper<ModelVerificheEnte> {

	@Override
	public ModelVerificheEnte mapRow(ResultSet rs, int rowNum) throws SQLException {

		ModelVerificheEnte result = new ModelVerificheEnte();

		result.setPresenzaAltreIncompatibilita((Boolean) rs.getObject("presenza_altre_misure_non_compatibili"));
		result.setNoteEnte(rs.getString("verifica_note"));
		result.setDataVerifica(rs.getDate("verifica_data"));

		return result;
	}

}
