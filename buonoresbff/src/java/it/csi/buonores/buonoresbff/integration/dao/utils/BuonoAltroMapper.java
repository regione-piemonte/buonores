/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbff.dto.custom.ModelBuonoAltro;

public class BuonoAltroMapper implements RowMapper<ModelBuonoAltro> {

	@Override
	public ModelBuonoAltro mapRow(ResultSet rs, int rowNum) throws SQLException {
		ModelBuonoAltro result = new ModelBuonoAltro();
		result.setMotivoId((Integer) rs.getObject("buono_stato_motivo_id"));
		result.setNota(rs.getString("buono_stato_nota"));
		result.setDataRevoca(rs.getDate("revoca_data_decorrenza"));
		result.setDataRinuncia(rs.getDate("rinuncia_data_decorrenza"));
		return result;
	}

}
