/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbo.dto.ModelContratto;
import it.csi.buonores.buonoresbo.dto.ModelFornitore;
import it.csi.buonores.buonoresbo.dto.ModelStrutturaSedeLegale;

public class ContrattoMapper implements RowMapper<ModelContratto> {

	@Override
	public ModelContratto mapRow(ResultSet rs, int rowNum) throws SQLException {

		ModelContratto result = new ModelContratto();
		result.setId(rs.getInt("contratto_id"));
		result.setDataInizio(rs.getTimestamp("contratto_data_inizio"));
		result.setDataFine(rs.getTimestamp("contratto_data_fine"));
		result.setDataInvioABandi(rs.getTimestamp("data_invio_a_bandi"));
		ModelFornitore fornitore = new ModelFornitore();
		fornitore.setCodice(rs.getString("rsa_cod"));
		fornitore.setCodiceArpe(rs.getString("rsa_cod_arpe"));
		fornitore.setNome(rs.getString("rsa_denominazione"));
		fornitore.setIndirizzo(rs.getString("rsa_indirizzo"));
		fornitore.setComune(rs.getString("rsa_comune"));
		fornitore.setProvincia(rs.getString("rsa_provincia"));
		fornitore.setPiva(rs.getString("fornitore_piva"));
		ModelStrutturaSedeLegale sedeLegale = new ModelStrutturaSedeLegale();
		sedeLegale.setComune(rs.getString("rsa_sede_legale_denominazione"));
		sedeLegale.setIndirizzo(rs.getString("rsa_sede_legale_indirizzo"));
		sedeLegale.setComune(rs.getString("rsa_sede_legale_comune"));
		sedeLegale.setProvincia(rs.getString("rsa_sede_legale_provincia"));
		fornitore.setSedeLegale(sedeLegale);
		result.setFornitore(fornitore);

		result.setTipoFornitore(rs.getString("fornitore_tipo_cod"));
		result.setTipo(rs.getString("contratto_tipo_cod"));

		return result;
	}

}
