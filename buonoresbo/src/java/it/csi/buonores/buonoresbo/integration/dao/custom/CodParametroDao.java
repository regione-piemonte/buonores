/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import it.csi.buonores.buonoresbo.exception.DatabaseException;
import it.csi.buonores.buonoresbo.util.LoggerUtil;

@Repository
public class CodParametroDao extends LoggerUtil {

	public static final String SELECT_ERRORE_DESC = "SELECT a.parametro_valore FROM bres_c_parametro a,bres_c_parametro_tipo b where "
			+ "b.parametro_tipo_id = a.parametro_tipo_id and "
			+ "now()::date BETWEEN b.validita_inizio::date and COALESCE(b.validita_fine::date, now()::date) and b.data_cancellazione is null and "
			+ "now()::date BETWEEN a.validita_inizio::date and COALESCE(a.validita_fine::date, now()::date) and a.data_cancellazione is null and "
			+ "a.parametro_cod = :codice and " + "b.parametro_tipo_cod = :tipopar;";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public String selectValoreParametroFromCod(String codice, String tipopar) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codice", codice).addValue("tipopar",
				tipopar);
		try {
			return jdbcTemplate.queryForObject(SELECT_ERRORE_DESC, namedParameters, String.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "selectValoreParametroFromCod";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

}
