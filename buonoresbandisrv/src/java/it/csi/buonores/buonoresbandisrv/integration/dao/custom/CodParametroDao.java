/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.integration.dao.custom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import it.csi.buonores.buonoresbandisrv.dto.ModelParametro;
import it.csi.buonores.buonoresbandisrv.exception.DatabaseException;
import it.csi.buonores.buonoresbandisrv.integration.dao.utils.ListaParametroMapper;
import it.csi.buonores.buonoresbandisrv.util.LoggerUtil;

@Repository
public class CodParametroDao extends LoggerUtil {

	public static final String SELECT_ERRORE_DESC = "SELECT a.parametro_valore FROM bres_c_parametro a,bres_c_parametro_tipo b where "
			+ "b.parametro_tipo_id = a.parametro_tipo_id and "
			+ "now()::date BETWEEN b.validita_inizio::date and COALESCE(b.validita_fine::date, now()::date) and b.data_cancellazione is null and "
			+ "now()::date BETWEEN a.validita_inizio::date and COALESCE(a.validita_fine::date, now()::date) and a.data_cancellazione is null and "
			+ "a.parametro_cod = :codice and "
			+ "b.parametro_tipo_cod = :tipopar";

	public static final String SELECT_PARAMETRO_PER_TIPO = "SELECT a.parametro_cod , a.parametro_valore FROM bres_c_parametro a,bres_c_parametro_tipo b where "
			+ "b.parametro_tipo_id = a.parametro_tipo_id and "
			+ "now()::date BETWEEN b.validita_inizio::date and COALESCE(b.validita_fine::date, now()::date) and b.data_cancellazione is null and "
			+ "now()::date BETWEEN a.validita_inizio::date and COALESCE(a.validita_fine::date, now()::date) and a.data_cancellazione is null and "
			+ "b.parametro_tipo_cod = :tipopar";

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

	public Map<String, String> selectParametriPerTipo(String tipopar) throws DatabaseException {
		List<ModelParametro> lista = new ArrayList<ModelParametro>();
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("tipopar", tipopar);
		try {
			Map<String, String> mappaParametro = new HashMap<String, String>();
			lista = jdbcTemplate.query(SELECT_PARAMETRO_PER_TIPO, namedParameters, new ListaParametroMapper());
			if (lista.size() > 0) {
				for (ModelParametro param : lista) {
					mappaParametro.put(param.getCodice(), param.getValore());
				}
			}
			return mappaParametro;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_PARAMETRO_PER_TIPO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);

		}
	}

}
