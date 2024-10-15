/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbff.dto.custom.ModelSportelloExt;

public class SportelliExtMapper implements RowMapper<ModelSportelloExt> {

	@Override
	public ModelSportelloExt mapRow(ResultSet rs, int rowNum) throws SQLException {
		ModelSportelloExt result = new ModelSportelloExt();
		result.setSportelloAnno(rs.getString("sportello_anno"));
		result.setSportelloDesc(rs.getString("sportello_desc"));
		result.setSportelloCod(rs.getString("sportello_cod"));

		return result;
	}

}
