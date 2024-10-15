/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbff.dto.ModelIseeBuono;

public class IseeBuonoMapper implements RowMapper<ModelIseeBuono> {

	@Override
	public ModelIseeBuono mapRow(ResultSet rs, int rowNum) throws SQLException {
		ModelIseeBuono isee = new ModelIseeBuono();

		isee.setIseeConforme((Boolean) rs.getObject("isee_conforme"));
		isee.setIseeId(rs.getLong("isee_id"));
		isee.setIseeVerificaEsitoCod(rs.getString("isee_verifica_esito_cod"));
		return isee;
	}

}
