/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbo.dto.ModelVerificaInformazioni;

public class VerificaInformazioniMapper implements RowMapper<ModelVerificaInformazioni> {

	@Override
	public ModelVerificaInformazioni mapRow(ResultSet rs, int rowNum) throws SQLException {
		ModelVerificaInformazioni verificaInfo = new ModelVerificaInformazioni();
		verificaInfo.setNessunaIncompatibilita((Boolean) rs.getObject("nessuna_incompatibilita"));
		verificaInfo.setVerificaNote(rs.getString("verifica_note"));
		verificaInfo.setVerificaData(rs.getString("data_verifica"));
		return verificaInfo;
	}

}
