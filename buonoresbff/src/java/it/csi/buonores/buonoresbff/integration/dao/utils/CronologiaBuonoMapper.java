/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbff.dto.ModelCronologiaBuono;
import it.csi.buonores.buonoresbff.dto.ModelStatoBuono;

public class CronologiaBuonoMapper implements RowMapper<ModelCronologiaBuono> {

	@Override
	public ModelCronologiaBuono mapRow(ResultSet rs, int rowNum) throws SQLException {
		ModelCronologiaBuono result = new ModelCronologiaBuono();
		ModelStatoBuono statobuono = new ModelStatoBuono();
		result.setNumero(rs.getString("numero"));
		statobuono.setStato(rs.getString("codStato"));
		statobuono.setDecorrenza(rs.getDate("data_decorrenza"));
		result.setStato(statobuono);
		result.setDataAggiornamento(rs.getDate("dataCreazione"));
		result.setNote(rs.getString("note"));

		return result;
	}

}
