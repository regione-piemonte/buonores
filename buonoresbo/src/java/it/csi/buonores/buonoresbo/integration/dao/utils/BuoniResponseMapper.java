/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbo.dto.ModelBuonoResponse;

public class BuoniResponseMapper implements RowMapper<ModelBuonoResponse> {

	@Override
	public ModelBuonoResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
		ModelBuonoResponse buono = new ModelBuonoResponse();
		buono.setBuonoCod(rs.getString("buono_cod"));
		buono.setBuonoStatoCod(rs.getString("buono_stato_cod"));
		buono.setNumeroDomanda(rs.getString("domanda_numero"));
		buono.setRichiedenteCf(rs.getString("richiedente_cf"));
		buono.setDestinatarioCf(rs.getString("beneficiario_cf"));
		buono.setNomeDestinatario(rs.getString("destinatario_nome"));
		buono.setCognomeDestinatario(rs.getString("destinatario_cognome"));
		buono.setNomeRichiedente(rs.getString("richiedente_nome"));
		buono.setCognomeRichiedente(rs.getString("richiedente_cognome"));
		buono.setDecorrenzaInizio(rs.getString("decorrenza_inizio"));
		buono.setDecorrenzaFine(rs.getString("decorrenza_fine"));
		buono.setDataDecesso(rs.getString("data_decesso"));
		buono.setDataCambioResidenza(rs.getString("data_cambio_residenza"));

		Boolean iseeconf = (Boolean) rs.getObject("isee_conforme");
		if (iseeconf == null) {
			buono.setIseeConforme(null);
		} else {
			buono.setIseeConforme(iseeconf ? "Si" : "No");
		}
		buono.setIseeVerificaEsitoCod(rs.getString("isee_verifica_esito_cod"));

		return buono;
	}

}
