/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbo.dto.ModelDatiDaModificare;

public class ListaDatiDaModificareMapper implements RowMapper<ModelDatiDaModificare> {

	@Override
	public ModelDatiDaModificare mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		ModelDatiDaModificare result = new ModelDatiDaModificare();

		result.setCodice(resultSet.getString("codice"));
		result.setDescrizione(resultSet.getString("descrizione"));
		result.setNometabella(resultSet.getString("nome_tabella"));
		result.setId(resultSet.getBigDecimal("id"));

		return result;
	}

}
