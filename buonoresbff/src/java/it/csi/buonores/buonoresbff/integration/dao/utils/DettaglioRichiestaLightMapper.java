/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbff.dto.custom.ModelRichiestaLight;

public class DettaglioRichiestaLightMapper implements RowMapper<ModelRichiestaLight> {

	@Override
	public ModelRichiestaLight mapRow(ResultSet rs, int rowNum) throws SQLException {

		// Richiesta
		ModelRichiestaLight richiesta = new ModelRichiestaLight();
		richiesta.setDomandaDetCod(rs.getString("domanda_det_cod"));
		richiesta.setSportelloId(rs.getBigDecimal("sportello_id"));
		richiesta.setDomandaDetId(rs.getBigDecimal("domanda_det_id"));
		richiesta.setRichiedenteCf(rs.getString("richiedente_cf"));

		return richiesta;

	}

}
