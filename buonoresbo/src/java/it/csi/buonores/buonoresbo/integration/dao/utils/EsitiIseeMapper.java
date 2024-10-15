/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbo.dto.ModelEsitoIsee;

public class EsitiIseeMapper implements RowMapper<ModelEsitoIsee> {

	@Override
	public ModelEsitoIsee mapRow(ResultSet rs, int rowNum) throws SQLException {
		ModelEsitoIsee stato = new ModelEsitoIsee();
		stato.setCodEsito(rs.getString("isee_verifica_esito_cod"));
		stato.setDescEsito(rs.getString("isee_verifica_esito_desc"));
		return stato;
	}
}
