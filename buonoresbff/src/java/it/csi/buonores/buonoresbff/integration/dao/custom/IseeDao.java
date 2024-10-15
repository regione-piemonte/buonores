/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.integration.dao.custom;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import it.csi.buonores.buonoresbff.dto.ModelIsee;
import it.csi.buonores.buonoresbff.dto.ModelIseeBuono;
import it.csi.buonores.buonoresbff.exception.DatabaseException;
import it.csi.buonores.buonoresbff.integration.dao.utils.IseeBuonoMapper;
import it.csi.buonores.buonoresbff.util.LoggerUtil;

@Repository
public class IseeDao extends LoggerUtil {

	public static final String SELECT_ISEE_CONFORME = "( select distinct "
			+ "	btbi.isee_anno from bres_t_buono_isee btbi, bres_t_buono btb , bres_t_domanda btd "
			+ " where btd.domanda_id = btb.domanda_id "
			+ "	and btd.domanda_numero = :numeroDomanda "
			+ "	and btb.buono_id = btbi.buono_id "
			+ "	and btd.data_cancellazione is null "
			+ "	and btb.data_cancellazione is null "
			+ "	and btbi.data_cancellazione is null "
			+ " and btbi.isee_conforme = true and (btbi.isee_verifica_esito_id is null or btbi.isee_verifica_esito_id in ( "
			+ " select bdbive.isee_verifica_esito_id  from bres_d_buono_isee_verifica_esito bdbive "
			+ " where bdbive.isee_verifica_esito_cod not in ('RESPINTO') and bdbive.validita_fine is null "
			+ " and bdbive.data_cancellazione is null)) "
			+ "	order by btbi.isee_anno desc ) "
			+ "	union all "
			+ "	( select distinct to_char(date_part('year', btdd.isee_data_rilascio)) "
			+ "	from bres_t_domanda btd "
			+ "	join bres_t_domanda_dettaglio btdd on btd.domanda_id = btdd.domanda_id "
			+ "	join bres_d_domanda_stato bdds on btdd.domanda_stato_id = bdds.domanda_stato_id "
			+ "	where btd.domanda_numero = :numeroDomanda "
			+ "	and bdds.domanda_stato_cod = 'IN_PAGAMENTO' "
			+ "	and btd.data_cancellazione is null "
			+ "	and btdd.data_cancellazione is null ) ";

	public static final String INSERT_ISEE_CONFORME = "insert into bres_t_buono_isee (isee_anno,utente_creazione,utente_modifica,isee_conforme, buono_id)"
			+ " values (:iseeAnno, :utenteCrea, :utenteModifica, true, ( "
			+ " select btb.buono_id from bres_t_buono btb, bres_t_domanda btd "
			+ " where btd.domanda_id = btb.domanda_id "
			+ "	and btd.domanda_numero = :numeroDomanda "
			+ "	and btd.data_cancellazione is null "
			+ "	and btb.data_cancellazione is null))";

	public static final String INSERT_ISEE_CONFORME_SELECT = "insert into bres_t_buono_isee (isee_anno, isee_conforme, isee_valore, "
			+ "isee_data_rilascio, isee_scadenza, "
			+ "isee_verifica_esito_id, isee_verificato_in_data, isee_note, buono_id,utente_creazione, utente_modifica) "
			+ "select :iseeAnno, true, isee_valore, isee_data_rilascio, isee_scadenza, "
			+ "isee_verifica_esito_id, isee_verificato_in_data, isee_note, buono_id, :utenteCrea, :utenteModifica "
			+ "from bres_t_buono_isee where isee_id= :iseeId";

	public static final String SELECT_ISEE = "select distinct "
			+ "    btbi.isee_id,btbi.isee_conforme,bdbive.isee_verifica_esito_cod  "
			+ " from bres_t_domanda btd , bres_t_buono btb "
			+ " left join bres_t_buono_isee btbi on btbi.buono_id = btb.buono_id "
			+ " and btbi.validita_fine is null and btbi.data_cancellazione is null and btbi.isee_anno = :iseeAnno "
			+ " left join bres_d_buono_isee_verifica_esito bdbive on "
			+ " bdbive.isee_verifica_esito_id = btbi.isee_verifica_esito_id and bdbive.data_cancellazione is null "
			+ " and bdbive.validita_fine is null "
			+ " where btd.domanda_id = btb.domanda_id "
			+ "    and btd.domanda_numero = :numeroDomanda "
			+ "    and btb.buono_id = btbi.buono_id "
			+ "    and btd.data_cancellazione is null "
			+ "    and btb.data_cancellazione is null";

	public static final String UPDATE_ISEE_CONFORME = "UPDATE bres_t_buono_isee "
			+ "SET validita_fine=now(), data_modifica=now() "
			+ "WHERE isee_id = :idIsee";

	public static final String UPDATE_ISEE_CONFORME_CITTADINO = "UPDATE bres_t_buono_isee "
			+ "SET isee_conforme=true,  data_modifica=now() "
			+ "WHERE isee_id = :idIsee";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public List<ModelIsee> selectIsee(String numeroDomanda) throws DatabaseException {
		String methodName = "SELECT_ISEE_CONFORME";
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda);
		List<ModelIsee> isee = new ArrayList<ModelIsee>();
		List<String> anni = new ArrayList<String>();
		try {
			anni = jdbcTemplate.queryForList(SELECT_ISEE_CONFORME, namedParameters, String.class);
			for (String anno : anni) {
				ModelIsee iseesin = new ModelIsee();
				iseesin.setAnno(anno);
				isee.add(iseesin);
			}
			return isee;
		} catch (EmptyResultDataAccessException e) {
			logError(methodName, e.getMessage());
			return null;
		} catch (Exception e) {
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public long insertIseeConforme(String iseeAnno, String numeroDomanda, String codFiscale) throws DatabaseException {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue("iseeAnno", iseeAnno, Types.VARCHAR);
		params.addValue("numeroDomanda", numeroDomanda, Types.VARCHAR);
		params.addValue("utenteCrea", codFiscale, Types.VARCHAR);
		params.addValue("utenteModifica", codFiscale, Types.VARCHAR);

		jdbcTemplate.update(INSERT_ISEE_CONFORME, params, keyHolder, new String[] { "isee_id" });
		long idIsee = keyHolder.getKey().longValue();
		return idIsee;
	}

	public long insertIseeConformeSelect(String iseeAnno, Long iseeId, String codFiscale) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue("iseeAnno", iseeAnno, Types.VARCHAR);
		params.addValue("iseeId", iseeId, Types.BIGINT);
		params.addValue("utenteCrea", codFiscale, Types.VARCHAR);
		params.addValue("utenteModifica", codFiscale, Types.VARCHAR);

		return jdbcTemplate.update(INSERT_ISEE_CONFORME_SELECT, params);
	}

	public long updateIseeConforme(long idIsee) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("idIsee", idIsee, Types.BIGINT);
		return jdbcTemplate.update(UPDATE_ISEE_CONFORME, params);
	}

	public long updateIseeConformeCittadino(long idIsee) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("idIsee", idIsee, Types.BIGINT);
		return jdbcTemplate.update(UPDATE_ISEE_CONFORME_CITTADINO, params);
	}

	public ModelIseeBuono selectIsee(String numeroDomanda, String iseeAnno) throws DatabaseException {
		String methodName = "SELECT_ISEE";
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda)
				.addValue("iseeAnno", iseeAnno);
		ModelIseeBuono isee = null;
		try {
			isee = jdbcTemplate.queryForObject(SELECT_ISEE, namedParameters, new IseeBuonoMapper());
			return isee;
		} catch (EmptyResultDataAccessException e) {
			logInfo(methodName + " - Nessun record trovato per domanda " + numeroDomanda + " per anno " + iseeAnno,
					e.getMessage());
			return null;
		} catch (Exception e) {
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}
}
