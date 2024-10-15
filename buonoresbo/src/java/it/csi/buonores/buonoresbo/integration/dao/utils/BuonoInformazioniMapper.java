/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbo.dto.ModelBuonoInformazioni;

public class BuonoInformazioniMapper implements RowMapper<ModelBuonoInformazioni> {

	@Override
	public ModelBuonoInformazioni mapRow(ResultSet rs, int rownum) throws SQLException {
		ModelBuonoInformazioni buonoInfo = new ModelBuonoInformazioni();
		buonoInfo.setBuonoStatoCod(rs.getString("buono_stato_cod"));
		buonoInfo.setValiditaFine(rs.getString("validita_fine"));
		buonoInfo.setRinunciaDataDecorrenza(rs.getString("rinuncia_data_decorrenza"));
		buonoInfo.setRevocaDataDecorrenza(rs.getString("revoca_data_decorrenza"));
		buonoInfo.setNotaRevoca(rs.getString("buono_stato_nota"));
		return buonoInfo;
	}

}
