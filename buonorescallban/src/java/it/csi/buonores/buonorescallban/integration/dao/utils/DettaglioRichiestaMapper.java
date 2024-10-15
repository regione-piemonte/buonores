/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonorescallban.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonorescallban.dto.custom.ModelDomandaExt;

public class DettaglioRichiestaMapper implements RowMapper<ModelDomandaExt> {

	@Override
	public ModelDomandaExt mapRow(ResultSet rs, int rowNum) throws SQLException {

		// Richiesta
		ModelDomandaExt richiesta = new ModelDomandaExt();
		richiesta.setNumeroDomanda(rs.getString("domanda_numero"));
		richiesta.setCfRichiedente(rs.getString("richiedente_cf"));
		richiesta.setCfBeneficiario(rs.getString("beneficiario_cf"));

		return richiesta;

	}

}
