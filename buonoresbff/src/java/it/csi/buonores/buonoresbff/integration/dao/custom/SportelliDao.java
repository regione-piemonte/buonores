/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.integration.dao.custom;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import it.csi.buonores.buonoresbff.dto.ModelSportello;
import it.csi.buonores.buonoresbff.dto.custom.ModelSportelloExt;
import it.csi.buonores.buonoresbff.exception.DatabaseException;
import it.csi.buonores.buonoresbff.integration.dao.utils.SportelliExtMapper;
import it.csi.buonores.buonoresbff.integration.dao.utils.SportelliMapper;
import it.csi.buonores.buonoresbff.util.LoggerUtil;

@Repository
public class SportelliDao extends LoggerUtil {

	public static final String SELECT_SPORTELLI = "select spo.validita_inizio::date as dataInizio,spo.validita_fine::date as dataFine, "
			+ "case  "
			+ "	when now()::date BETWEEN spo.validita_inizio::date and COALESCE(spo.validita_fine::date, now()::date) then true  "
			+ "	else false "
			+ "end inCorso "
			+ "from bres_t_sportello spo,bres_d_contributo_tipo con "
			+ "where spo.contributo_tipo_id =con.contributo_tipo_id  "
			+ "and spo.data_cancellazione is null  "
			+ "and con.data_cancellazione is null "
			+ "and con.contributo_tipo_cod = 'RES' "
			+ "and now()::date <= spo.validita_fine::date; ";

	public static final String SELECT_SPORTELLI_COD = "select sportello_cod,sportello_anno ,sportello_desc "
			+ "from bres_t_sportello "
			+ "where data_cancellazione is null "
			+ "and sportello_id=:sportelloId";

	// Modifica TAG 2.2.0v002 buonodom
	public static final String SPORTELLI_IS_IN_CORSO = "select "
			+ "    case "
			+ "        when now()::date between spo.validita_inizio::date and coalesce(spo.validita_fine::date, now()::date) then true "
			+ "        else false "
			+ "    end inCorso "
			+ "from "
			+ "    bres_t_sportello spo, "
			+ "    bres_d_contributo_tipo con, "
			+ "    bres_t_domanda btd "
			+ "where "
			+ "    spo.contributo_tipo_id = con.contributo_tipo_id "
			+ "    and spo.data_cancellazione is null "
			+ "    and con.data_cancellazione is null "
			+ "    and btd.data_cancellazione is null "
			+ "    and con.contributo_tipo_cod = 'RES' "
			+ "    and spo.sportello_id = btd.sportello_id "
			+ "    and btd.domanda_numero = :numeroDomanda";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public List<ModelSportello> selectSportelli() throws DatabaseException {
		List<ModelSportello> sportello = new ArrayList<ModelSportello>();
		try {
			sportello = jdbcTemplate.query(SELECT_SPORTELLI, new SportelliMapper());
			return sportello;
		} catch (Exception e) {
			generateDatabaseException("SELECT_SPORTELLI", e);
		}

		return sportello;
	}

	public ModelSportelloExt selectSportelliCod(BigDecimal sportelloId) throws DatabaseException {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("sportelloId", sportelloId);
		ModelSportelloExt sportello = new ModelSportelloExt();
		try {
			sportello = jdbcTemplate.queryForObject(SELECT_SPORTELLI_COD, namedParameters, new SportelliExtMapper());
			return sportello;
		} catch (Exception e) {
			generateDatabaseException("SELECT_SPORTELLI_COD", e);
		}

		return sportello;
	}

	// Modifica TAG 2.2.0v002 buonodom
	public boolean isSportelliCorrente(String numeroDomanda) throws DatabaseException {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda);
		boolean inCorso = false;
		try {
			inCorso = jdbcTemplate.queryForObject(SPORTELLI_IS_IN_CORSO, namedParameters, Boolean.class);
			return inCorso;
		} catch (EmptyResultDataAccessException e) {
			return false;
		} catch (Exception e) {
			generateDatabaseException("SPORTELLI_IS_IN_CORSO", e);
		}

		return inCorso;
	}
}
