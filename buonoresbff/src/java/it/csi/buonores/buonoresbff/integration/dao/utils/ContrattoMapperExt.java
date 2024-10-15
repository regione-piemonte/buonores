/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbff.dto.ModelContratto;
import it.csi.buonores.buonoresbff.dto.ModelStruttura;
import it.csi.buonores.buonoresbff.dto.ModelStrutturaSedeLegale;

public class ContrattoMapperExt implements RowMapper<ModelContratto> {

	@Override
	public ModelContratto mapRow(ResultSet rs, int rowNum) throws SQLException {
		ModelContratto result = new ModelContratto();
		result.setId(rs.getInt("contratto_id"));
		result.setDataInizio(rs.getTimestamp("contratto_data_inizio"));
		result.setDataFine(rs.getTimestamp("contratto_data_fine"));
		result.setDataInvioABandi(rs.getTimestamp("data_invio_a_bandi"));
		ModelStruttura fornitore = new ModelStruttura();
		fornitore.setId(rs.getString("rsa_cod"));
		fornitore.setIdArpe(rs.getString("rsa_cod_arpe"));
		fornitore.setNome(rs.getString("rsa_denominazione"));
		fornitore.setIndirizzo(rs.getString("rsa_indirizzo"));
		fornitore.setComune(rs.getString("rsa_comune"));
		fornitore.setProvincia(rs.getString("rsa_provincia"));
		fornitore.setPiva(rs.getString("rsa_cfpiva"));
		ModelStrutturaSedeLegale sedeLegale = new ModelStrutturaSedeLegale();
		sedeLegale.setComune(rs.getString("rsa_sede_legale_denominazione"));
		sedeLegale.setIndirizzo(rs.getString("rsa_sede_legale_indirizzo"));
		sedeLegale.setComune(rs.getString("rsa_sede_legale_comune"));
		sedeLegale.setProvincia(rs.getString("rsa_sede_legale_provincia"));
		fornitore.setSedeLegale(sedeLegale);
		result.setStruttura(fornitore);

		result.setTipoFornitore(rs.getString("fornitore_tipo_cod"));
		result.setTipo(rs.getString("contratto_tipo_cod"));
		result.setDataCreazione(rs.getTimestamp("data_creazione"));

		return result;
	}

}
