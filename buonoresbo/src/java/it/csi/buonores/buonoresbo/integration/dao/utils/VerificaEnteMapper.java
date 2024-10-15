/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbo.dto.ModelVerificaEnte;

public class VerificaEnteMapper implements RowMapper<ModelVerificaEnte> {

	@Override
	public ModelVerificaEnte mapRow(ResultSet rs, int rowNum) throws SQLException {
		ModelVerificaEnte verificaEnte = new ModelVerificaEnte();
		verificaEnte.setVerificaId(rs.getLong("verifica_id"));
		verificaEnte.setDataVerifica(rs.getString("data_verifica"));
		verificaEnte.setVerificaNote(rs.getString("verifica_note"));
		verificaEnte.setNessunaIncompatibilita((Boolean) rs.getObject("nessuna_incompatibilita"));
		verificaEnte.setEnteGestoreId(rs.getLong("ente_gestore_id"));
		verificaEnte.setSportelloId(rs.getLong("sportello_id"));
		verificaEnte.setDomandaId(rs.getLong("domanda_id"));
		verificaEnte.setDomandaDetId(rs.getLong("domanda_det_id"));
		verificaEnte.setUtenteCreazione(rs.getString("utente_creazione"));

		verificaEnte.setDataVerificaRichiesta(rs.getString("data_richiesta_verifica"));
		verificaEnte.setVerificaEgRichiesta((Boolean) rs.getObject("verifica_eg_richiesta"));
		verificaEnte.setVerificaEgInCorso((Boolean) rs.getObject("verifica_eg_in_corso"));
		verificaEnte.setVerificaEgConclusa((Boolean) rs.getObject("verifica_eg_conclusa"));

		return verificaEnte;
	}

}
