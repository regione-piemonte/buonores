/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbo.dto.ModelDomandeGraduatoria;
import it.csi.buonores.buonoresbo.dto.ModelVerifiche;

public class DomandeInGraduatoriaMapper implements RowMapper<ModelDomandeGraduatoria> {

	@Override
	public ModelDomandeGraduatoria mapRow(ResultSet rs, int rowNum) throws SQLException {
		ModelDomandeGraduatoria domandaInGraduatoria = new ModelDomandeGraduatoria();
		domandaInGraduatoria.setPosizioneGraduatoria(rs.getLong("posizione"));
		domandaInGraduatoria.setNumeroDomanda(rs.getString("domanda_numero"));
		domandaInGraduatoria.setDestinatarioNome(rs.getString("destinatario_nome"));
		domandaInGraduatoria.setDestinatarioCognome(rs.getString("destinatario_cognome"));
		domandaInGraduatoria.setDestinatarioCF(rs.getString("beneficiario_cf"));
		domandaInGraduatoria.setPunteggioSociale(rs.getLong("punteggio_sociale"));
		domandaInGraduatoria.setIsee(rs.getBigDecimal("isee_valore"));
		domandaInGraduatoria.setDataInvioDomanda(rs.getString("domanda_data"));
		domandaInGraduatoria.setStatoDomanda(rs.getString("domanda_stato_desc"));
		domandaInGraduatoria.setImportoTotale(rs.getBigDecimal("importo"));
		domandaInGraduatoria.setAreaInterna(rs.getString("area"));
		ModelVerifiche verifiche = new ModelVerifiche();
		verifiche.setVerificaEgRichiesta((Boolean) rs.getObject("verifica_eg_richiesta"));
		verifiche.setVerificaEgInCorso((Boolean) rs.getObject("verifica_eg_in_corso"));
		verifiche.setVerificaEgConclusa((Boolean) rs.getObject("verifica_eg_conclusa"));
		domandaInGraduatoria.setVerifiche(verifiche);
		return domandaInGraduatoria;
	}

}
