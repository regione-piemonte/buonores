/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonores.buonoresbff.dto.ModelDecodificaRapportoAllegato;

public class ListaModelAllegatoRapportoMapper implements RowMapper<ModelDecodificaRapportoAllegato> {

	@Override
	public ModelDecodificaRapportoAllegato mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		ModelDecodificaRapportoAllegato result = new ModelDecodificaRapportoAllegato();

		result.setAllegatoObbligatorio((Boolean) resultSet.getObject("allegato_obbligatorio"));
		result.setAllegatoTipoCod(resultSet.getString("allegato_tipo_cod"));

		return result;
	}

}
