/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbo.dto.ModelDomandeAperta;
import it.csi.buonores.buonoresbo.dto.ModelEnteGestore;
import it.csi.buonores.buonoresbo.dto.ModelStati;
import it.csi.buonores.buonoresbo.dto.ModelVerifiche;

public class DomandeAperteMapper implements RowMapper<ModelDomandeAperta> {

	@Override
	public ModelDomandeAperta mapRow(ResultSet rs, int rowNum) throws SQLException {

		ModelDomandeAperta domanda = new ModelDomandeAperta();
		domanda.setIdDomanda(rs.getInt("domanda_id"));
		domanda.setNumeroDomanda(rs.getString("domanda_numero"));
		domanda.setCfDestinatario(rs.getString("beneficiario_cf"));
		domanda.setNomeDestinatario(rs.getString("destinatario_nome"));
		domanda.setCognomeDestinatario(rs.getString("destinatario_cognome"));
		domanda.setCfRichiedente(rs.getString("richiedente_cf"));
		domanda.setNomeRichiedente(rs.getString("richiedente_nome"));
		domanda.setCognomeRichiedente(rs.getString("richiedente_cognome"));
		domanda.setDataDomanda(rs.getString("domanda_data"));
		ModelStati stato = new ModelStati();
		stato.setCodStato(rs.getString("domanda_stato_cod"));
		stato.setDescStato(rs.getString("domanda_stato_desc"));
		domanda.setStato(stato);
		domanda.setDataVerificaBuono(rs.getString("data_verifica"));
		domanda.setEsitoVerificaBuono((Boolean) rs.getObject("nessuna_incompatibilita"));
		ModelEnteGestore enteGestore = new ModelEnteGestore();
		enteGestore.setIdEnteGestore(rs.getInt("ente_gestore_id"));
		enteGestore.setDenominazioneEnte(rs.getString("ente_gestore_denominazione"));
		domanda.setEnteGestore(enteGestore);
		ModelVerifiche verifiche = new ModelVerifiche();
		verifiche.setVerificaEgRichiesta((Boolean) rs.getObject("verifica_eg_richiesta"));
		verifiche.setVerificaEgInCorso((Boolean) rs.getObject("verifica_eg_in_corso"));
		verifiche.setVerificaEgConclusa((Boolean) rs.getObject("verifica_eg_conclusa"));
		domanda.setVerifiche(verifiche);
		domanda.setSportelloId(rs.getInt("sportello_id"));
		domanda.setSportelloCod(rs.getString("sportello_cod"));
		domanda.setSportelloDesc(rs.getString("sportello_desc"));

		domanda.setDecorrenzaInizio(rs.getString("decorrenza_inizio"));
		domanda.setDecorrenzaFine(rs.getString("decorrenza_fine"));

		domanda.setDataDecesso(rs.getString("data_decesso"));
		domanda.setDataCambioResidenza(rs.getString("data_cambio_residenza"));

		return domanda;

	}

}
