/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbo.dto.ModelUserRuolo;

public class UserRuoloMapper implements RowMapper<ModelUserRuolo> {

	@Override
	public ModelUserRuolo mapRow(ResultSet rs, int rowNum) throws SQLException {

		ModelUserRuolo user = new ModelUserRuolo();
		user.setCodRuolo(rs.getString("ruolo_cod"));
		user.setDescRuolo(rs.getString("ruolo_desc"));
		user.setCodFisc(rs.getString("soggetto_cf"));
		user.setNome(rs.getString("soggetto_nome"));
		user.setCognome(rs.getString("soggetto_cognome"));

		return user;

	}

}
