/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.integration.dao.custom;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import it.csi.buonores.buonoresbff.dto.ModelCronologia;
import it.csi.buonores.buonoresbff.exception.DatabaseException;
import it.csi.buonores.buonoresbff.integration.dao.utils.CronologiaMapper;
import it.csi.buonores.buonoresbff.util.LoggerUtil;

@Repository
public class CronologiaDao extends LoggerUtil {

	public static final String SELECT_CRONOLOGIA_RICHIESTE = "select "
			+ "	btd.domanda_numero as numero, "
			+ "	bdds.domanda_stato_cod as codStato, "
			+ "	btdd.data_creazione as dataCreazione, "
			+ "	btdd.note "
			+ "from "
			+ "	bres_t_domanda btd, "
			+ "	bres_t_domanda_dettaglio btdd, "
			+ "	bres_d_domanda_stato bdds "
			+ "where "
			+ "	btd.domanda_numero = :numeroDomanda "
			+ "	and  "
			+ "	btdd.domanda_id = btd.domanda_id  "
			+ "	and  "
			+ "	btdd.data_cancellazione is null  "
			+ "	and  "
			+ "	btd.data_cancellazione is null "
			+ "	and  "
			+ "	bdds.domanda_stato_id = btdd.domanda_stato_id";

	public static final String SELECT_CF_RICHIESTE = "select btd.richiedente_cf  "
			+ "from bres_t_domanda btd, "
			+ "bres_t_domanda_dettaglio btdd  "
			+ "where btd.data_cancellazione is null  "
			+ "and btd.domanda_numero = :numeroDomanda "
			+ "and btd.domanda_id = btdd.domanda_id  "
			+ "and btdd.validita_inizio <= now() "
			+ "and btdd.validita_fine is null  "
			+ "and btdd.data_cancellazione is null ";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public List<ModelCronologia> selectCronologia(String numeroRichiesta) throws DatabaseException {
		List<ModelCronologia> cronologia = new ArrayList<ModelCronologia>();
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroRichiesta);
		try {
			cronologia = jdbcTemplate.query(SELECT_CRONOLOGIA_RICHIESTE, namedParameters, new CronologiaMapper());
			return cronologia;
		} catch (Exception e) {
			generateDatabaseException("SELECT_CRONOLOGIA_RICHIESTE", e);
		}
		return cronologia;
	}

	public String selectCf(String numeroRichiesta) throws DatabaseException {
		String cf = null;
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroRichiesta);
		try {
			cf = jdbcTemplate.queryForObject(SELECT_CF_RICHIESTE, namedParameters, String.class);
			return cf;
		} catch (Exception e) {
			generateDatabaseException("SELECT_CF_RICHIESTE", e);
		}
		return cf;
	}

}
