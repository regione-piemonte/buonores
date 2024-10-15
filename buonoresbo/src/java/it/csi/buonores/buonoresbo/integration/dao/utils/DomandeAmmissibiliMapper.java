/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbo.dto.ModelDomandeAmmissibili;

public class DomandeAmmissibiliMapper implements RowMapper<ModelDomandeAmmissibili> {

	@Override
	public ModelDomandeAmmissibili mapRow(ResultSet rs, int rowNum) throws SQLException {
		ModelDomandeAmmissibili domandaAmmissibile = new ModelDomandeAmmissibili();
		domandaAmmissibile.setDettId(rs.getLong("domanda_det_id"));
		domandaAmmissibile.setSportelloId(rs.getLong("sportello_id"));
		domandaAmmissibile.setNumeroDomanda(rs.getString("domanda_numero"));
		domandaAmmissibile.setRichiedenteCF(rs.getString("richiedente_cf"));
		domandaAmmissibile.setRiserva(rs.getBoolean("riserva"));
		return domandaAmmissibile;
	}

}
