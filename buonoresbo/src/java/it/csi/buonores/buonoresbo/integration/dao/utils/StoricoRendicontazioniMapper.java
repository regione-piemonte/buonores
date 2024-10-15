/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbo.dto.ModelStoricoRendicontazioni;

public class StoricoRendicontazioniMapper implements RowMapper<ModelStoricoRendicontazioni> {

	@Override
	public ModelStoricoRendicontazioni mapRow(ResultSet rs, int rowNum) throws SQLException {
		ModelStoricoRendicontazioni storicoRend = new ModelStoricoRendicontazioni();
		storicoRend.setMonthData(rs.getString("month_data"));
		storicoRend.setAnno(rs.getInt("anno"));
		storicoRend.setMese(rs.getString("mese"));
		storicoRend.setEsito(rs.getString("esito"));
		storicoRend.setNote(rs.getString("note"));
		storicoRend.setDataDichiarazione(rs.getString("data_dichiarazione"));
		storicoRend.setDataIstruttoria(rs.getString("data_istruttoria"));
		return storicoRend;
	}

}
