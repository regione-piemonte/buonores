/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbandisrv.dto.ModelSetFornitore;

public class SetFornitoreMapper implements RowMapper<ModelSetFornitore> {

	@Override
	public ModelSetFornitore mapRow(ResultSet rs, int rowNum) throws SQLException {

		ModelSetFornitore fornitore = new ModelSetFornitore();
		String cfpiva = rs.getString("rsa_cfpiva") != null ? rs.getString("rsa_cfpiva") : "";
		fornitore.setCodiceFiscale(cfpiva);
		fornitore.setCognome("");
		fornitore.setNome("");
		fornitore.setDenominazione(rs.getString("rsa_denominazione"));
		fornitore.setDataInizio(rs.getString("contratto_data_inizio"));
		fornitore.setDataFine(rs.getString("contratto_data_fine"));
		if (cfpiva.length() == 11) {
			fornitore.setPartitaIva(cfpiva);
		}
		fornitore.setCodiceFormaGiuridica(null);
		return fornitore;
	}

}
