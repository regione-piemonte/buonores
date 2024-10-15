/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbo.dto.ModelVerificheDomanda;

public class ModelVerificheMapper implements RowMapper<ModelVerificheDomanda> {

	@Override
	public ModelVerificheDomanda mapRow(ResultSet rs, int rowNum) throws SQLException {
		ModelVerificheDomanda result = new ModelVerificheDomanda();
		result.setNumeroDomanda(rs.getString("domanda_numero"));
		result.setDataInizioValidita(rs.getTimestamp("validita_inizio"));
		result.setMisure(rs.getString("misure"));
		result.setTipo(rs.getString("tipo"));
		result.setFonte(rs.getString("fonte"));
		result.setNote(rs.getString("note"));
		result.setNoteRichiesta(rs.getString("note_richiesta"));
		result.setDataRichiesta(rs.getString("data_richiesta"));
		return result;
	}

}
