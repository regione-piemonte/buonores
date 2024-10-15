/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbo.dto.ModelVisualizzaCronologia;

public class ModelVisualizzaCroMapper implements RowMapper<ModelVisualizzaCronologia> {

	@Override
	public ModelVisualizzaCronologia mapRow(ResultSet rs, int rowNum) throws SQLException {
		ModelVisualizzaCronologia result = new ModelVisualizzaCronologia();
		result.setNumeroDomanda(rs.getString("domanda_numero"));
		result.setStatoDomanda(rs.getString("domanda_stato_desc"));
		result.setNote(rs.getString("note"));
		result.setNotaInterna(rs.getString("nota_interna"));
		result.setDataInizioValidita(rs.getTimestamp("validita_inizio"));
		result.setUtente(rs.getString("utente"));
		result.setRichiedente(rs.getBoolean("isRichiedente"));
		result.setNotaEnte(rs.getString("note_regione"));
		return result;
	}

}
