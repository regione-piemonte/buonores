/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbandisrv.dto.ModelSportello;

public class SportelliMapper implements RowMapper<ModelSportello> {

	@Override
	public ModelSportello mapRow(ResultSet rs, int rowNum) throws SQLException {
		ModelSportello result = new ModelSportello();
		result.setSportelloAnno(rs.getString("sportello_anno"));
		result.setSportelloDesc(rs.getString("sportello_desc"));
		result.setSportelloCod(rs.getString("sportello_cod"));

		return result;
	}

}
