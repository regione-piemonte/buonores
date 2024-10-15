/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbo.dto.ModelIseeBuono;

public class IseeBuonoMapper implements RowMapper<ModelIseeBuono> {

	@Override
	public ModelIseeBuono mapRow(ResultSet rs, int rowNum) throws SQLException {
		ModelIseeBuono isee = new ModelIseeBuono();

		isee.setBuonoId(rs.getLong("buono_id"));
		isee.setIseeAnno(rs.getString("isee_anno"));
		isee.setIseeConforme((Boolean) rs.getObject("isee_conforme"));
		isee.setIseeDataRilascio(rs.getDate("isee_data_rilascio"));
		isee.setIseeId(rs.getLong("isee_id"));
		isee.setIseeNote(rs.getString("isee_note"));
		isee.setIseeScadenza(rs.getDate("isee_scadenza"));
		isee.setIseeValore(rs.getBigDecimal("isee_valore"));
		isee.setIseeVerificaEsitoCod(rs.getString("isee_verifica_esito_cod"));
		isee.setIseeVerificatoInData(rs.getDate("isee_verificato_in_data"));
		isee.setValiditaInizio(rs.getString("validita_inizio"));
		isee.setUtenteCreazione(rs.getString("utente_creazione"));
		isee.setUtenteModifica(rs.getString("utente_modifica"));
		isee.setValiditaFine(rs.getDate("validita_fine"));
		return isee;
	}

}
