/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoressrv.dto.ModelIncompatibilitaRichiesta;

//TAG PROD DOM 2.1.0-001
public class IncompatibilitaMapper implements RowMapper<ModelIncompatibilitaRichiesta> {

	@Override
	public ModelIncompatibilitaRichiesta mapRow(ResultSet rs, int rowNum) throws SQLException {
	
	ModelIncompatibilitaRichiesta incompatibilita = new ModelIncompatibilitaRichiesta();
	incompatibilita.setNessunaIncompatibilita((Boolean) rs.getObject("nessuna_incompatibilita"));
	incompatibilita.setIncompatibilitaPerContratto((Boolean) rs.getObject("incompatibilita_per_contratto"));
	return incompatibilita;
	
	}

}
