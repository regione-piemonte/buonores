/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbff.dto.DDomandaStato;

public class DDomandaStatoMapper implements RowMapper<DDomandaStato> {

	@Override
	public DDomandaStato mapRow(ResultSet rs, int rowNum) throws SQLException {
		DDomandaStato result = new DDomandaStato();
		result.setIdStato(rs.getLong("domanda_stato_id"));
		result.setCodStato(rs.getString("domanda_stato_cod"));
		return result;
	}

}
