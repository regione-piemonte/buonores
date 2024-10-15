/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbo.dto.ModelGraduatoriaOrdinamento;

public class GraduatoriaOrdinamentoMapper implements RowMapper<ModelGraduatoriaOrdinamento> {

	@Override
	public ModelGraduatoriaOrdinamento mapRow(ResultSet rs, int rowNum) throws SQLException {

		ModelGraduatoriaOrdinamento ordinamento = new ModelGraduatoriaOrdinamento();
		ordinamento.setOrdinamentoId(rs.getLong("ordinamento_id"));
		ordinamento.setOrdinamentoCriterio(rs.getString("ordinamento_criterio"));
		ordinamento.setOrdinamentoTabella(rs.getString("ordinamento_tabella"));
		ordinamento.setOrdinamentoCampo(rs.getString("ordinamento_campo"));
		ordinamento.setOrdinamentoPosizione(rs.getLong("ordinamento_posizione"));
		ordinamento.setOrdinamentoDirezione(rs.getString("ordinamento_direzione"));

		return ordinamento;
	}

}
