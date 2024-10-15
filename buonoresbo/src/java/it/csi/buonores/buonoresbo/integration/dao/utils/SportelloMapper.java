/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbo.dto.ModelSportello;

public class SportelloMapper implements RowMapper<ModelSportello> {

	@Override
	public ModelSportello mapRow(ResultSet rs, int rowNum) throws SQLException {

		ModelSportello sportello = new ModelSportello();
		sportello.setCodSportello(rs.getString("sportello_cod"));
		sportello.setDescSportello(rs.getString("sportello_desc"));
		sportello.setCorrente(rs.getBoolean("is_corrente"));
		sportello.setDataFine(rs.getString("validita_fine"));
		sportello.setDataInizio(rs.getString("validita_inizio"));
		sportello.setAnno(rs.getString("sportello_anno"));
		return sportello;

	}

}
