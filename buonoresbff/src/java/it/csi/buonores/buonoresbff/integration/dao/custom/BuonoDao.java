/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.integration.dao.custom;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import it.csi.buonores.buonoresbff.dto.ModelCronologiaBuono;
import it.csi.buonores.buonoresbff.dto.custom.ModelBuono;
import it.csi.buonores.buonoresbff.dto.custom.ModelBuonoAltro;
import it.csi.buonores.buonoresbff.exception.DatabaseException;
import it.csi.buonores.buonoresbff.integration.dao.utils.BuonoAltroMapper;
import it.csi.buonores.buonoresbff.integration.dao.utils.BuonoMapper;
import it.csi.buonores.buonoresbff.integration.dao.utils.CronologiaBuonoMapper;
import it.csi.buonores.buonoresbff.util.LoggerUtil;

@Repository
public class BuonoDao extends LoggerUtil {

	public static final String SELECT_CRONOLOGIA_BUONO = "select "
			+ "btb.buono_cod as numero, "
			+ "bdbs.buono_stato_cod as codStato, "
			+ "brbs.validita_inizio as dataCreazione, brbs.buono_stato_nota note, "
			+ "coalesce(brbs.rinuncia_data_decorrenza,brbs.revoca_data_decorrenza) as data_decorrenza "
			+ "from "
			+ "bres_t_buono btb , "
			+ "bres_d_buono_stato bdbs, "
			+ "bres_r_buono_stato brbs "
			+ "where btb.buono_id = :buonoId "
			+ "and bdbs.buono_stato_id = brbs.buono_stato_id "
			+ "and brbs.buono_id = btb.buono_id "
			+ "and btb.data_cancellazione is null "
			+ "and bdbs.data_cancellazione is null "
			+ "and brbs.data_cancellazione is null "
			+ "order by brbs.validita_inizio";

	public static final String UPDATE_BUONO_STATO = "update bres_r_buono_stato set validita_fine = now(), "
			+ "utente_modifica = :utente_modifica, data_modifica = now() "
			+ "where buono_id = :buonoId and validita_fine is null "
			+ "and buono_stato_id=:buonoStatoId";

	public static final String INSERT_INTO_BUONO_STATO_CON_MOTIVO = "insert into bres_r_buono_stato (buono_id,buono_stato_id, "
			+ "utente_creazione,utente_modifica,rinuncia_data_decorrenza, buono_stato_motivo_id, "
			+ "revoca_data_decorrenza, buono_stato_nota) values (:buonoId, "
			+ "(select buono_stato_id from bres_d_buono_stato where buono_stato_cod= :buonoStatoCod), "
			+ ":utente_creazione,:utente_modifica,:decorrenza, "
			+ "(select buono_stato_motivo_id from bres_d_buono_stato_motivo "
			+ "where buono_stato_motivo_cod= :buonoStatoMotivoCod), "
			+ ":decorrenzaRevoca, :notaRevoca)";

	public static final String ESISTE_STATO_BUONO = "select count(*) from "
			+ "bres_d_buono_stato bdbs where "
			+ "bdbs.buono_stato_cod = :buonoStatoCod "
			+ "and bdbs.data_cancellazione is null";

	public static final String GET_BUONO = "select distinct btd.richiedente_cf richiedenteCf, btb.buono_id buonoId, bdbs.buono_stato_cod stato,"
			+ "btb.buono_cod buonoCod,bdbs.buono_stato_id buonoStatoId, btb.decorrenza_inizio, "
			+ "btb.decorrenza_fine from "
			+ "bres_t_domanda btd, "
			+ "bres_t_buono btb , "
			+ "bres_d_buono_stato bdbs, "
			+ "bres_r_buono_stato brbs "
			+ "where "
			+ "btd.domanda_numero = :numeroDomanda "
			+ "and btd.domanda_id = btb.domanda_id "
			+ "and bdbs.buono_stato_id = brbs.buono_stato_id "
			+ "and brbs.buono_id = btb.buono_id "
			+ "and btd.data_cancellazione is null "
			+ "and btb.data_cancellazione is null "
			+ "and bdbs.data_cancellazione is null "
			+ "and brbs.data_cancellazione is null "
			+ "and brbs.validita_inizio  <= now() "
			+ "and brbs.validita_fine is null";

	public static final String GET_BUONO_ALTRO = "select distinct brbs.buono_stato_motivo_id,brbs.rinuncia_data_decorrenza, "
			+ "brbs.revoca_data_decorrenza, brbs.buono_stato_nota "
			+ "from "
			+ "bres_t_domanda btd, "
			+ "bres_t_buono btb , "
			+ "bres_r_buono_stato brbs "
			+ "where "
			+ "btd.domanda_numero = :numeroDomanda "
			+ "and btd.domanda_id = btb.domanda_id "
			+ "and brbs.buono_id = btb.buono_id "
			+ "and btd.data_cancellazione is null "
			+ "and btb.data_cancellazione is null "
			+ "and brbs.data_cancellazione is null "
			+ "and brbs.validita_fine is null";

	public static final String UPDATE_BUONO_DATA_DECORRENZA_FINE = "update bres_t_buono set decorrenza_fine = :decorrenzaFine, "
			+ "utente_modifica = :utente_modifica, data_modifica = now() "
			+ "where buono_id = :buonoId";

	public static final String UPDATE_BUONO_DATE_DECORRENZA = "update bres_t_buono set decorrenza_fine = :decorrenzaFine, decorrenza_inizio = :decorrenzaInizio, "
			+ "utente_modifica = :utente_modifica, data_modifica = now() "
			+ "where buono_id = :buonoId";

	public static final String DESCR_CODIFICA = "select distinct buono_stato_motivo_desc from bres_d_buono_stato_motivo "
			+ "where buono_stato_motivo_cod = :codMotivo";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public List<ModelCronologiaBuono> selectCronologiaBuono(Long buonoId) throws DatabaseException {
		List<ModelCronologiaBuono> cronologia = new ArrayList<ModelCronologiaBuono>();
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("buonoId", buonoId);
		try {
			cronologia = jdbcTemplate.query(SELECT_CRONOLOGIA_BUONO, namedParameters, new CronologiaBuonoMapper());
			return cronologia;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_CRONOLOGIA_RICHIESTE";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public long updateStatoBuono(Long buonoId, String utenteModifica, Long buonoStatoId) throws DatabaseException {

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("buonoId", buonoId, Types.BIGINT).addValue("utente_modifica", utenteModifica, Types.VARCHAR)
				.addValue("buonoStatoId", buonoStatoId, Types.BIGINT);
		return jdbcTemplate.update(UPDATE_BUONO_STATO, params);
	}

	public long insertBuonoStatoConMotivo(String buonoStatoCod,
			String codFiscale, Long buonoId, Date decorrenza, String statosegn,
			Date decorrenzaRevoca, String notaRevoca) throws DatabaseException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue("buonoStatoCod", buonoStatoCod, Types.VARCHAR);
		params.addValue("utente_creazione", codFiscale, Types.VARCHAR);
		params.addValue("utente_modifica", codFiscale, Types.VARCHAR);
		params.addValue("buonoId", buonoId, Types.BIGINT);
		params.addValue("decorrenza", decorrenza, Types.DATE);
		params.addValue("buonoStatoMotivoCod", statosegn, Types.VARCHAR);
		params.addValue("decorrenzaRevoca", decorrenzaRevoca, Types.DATE);
		params.addValue("notaRevoca", notaRevoca, Types.VARCHAR);

		jdbcTemplate.update(INSERT_INTO_BUONO_STATO_CON_MOTIVO, params, keyHolder, new String[] { "r_buono_stato_id" });
		return keyHolder.getKey().longValue();
	}

	public boolean esisteStato(String buonoStatoCod) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("buonoStatoCod", buonoStatoCod);
		try {
			int conta = jdbcTemplate.queryForObject(ESISTE_STATO_BUONO, namedParameters, Integer.class);
			return conta > 0;
		} catch (EmptyResultDataAccessException e) {
			return false;
		} catch (Exception e) {
			String methodName = "ESISTE_STATO_BUONO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public ModelBuono selectBuono(String numeroRichiesta) throws DatabaseException {
		ModelBuono buono = new ModelBuono();
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroRichiesta);
		try {
			buono = jdbcTemplate.queryForObject(GET_BUONO, namedParameters, new BuonoMapper());
			return buono;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "GET_BUONO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public String selectDescrMotivoSegnalazione(String codMotivo) throws DatabaseException {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codMotivo", codMotivo);
		try {
			return jdbcTemplate.queryForObject(DESCR_CODIFICA, namedParameters, String.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "selectDescrMotivoSegnalazione";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public ModelBuonoAltro selectBuonoAltro(String numeroDomanda) throws DatabaseException {
		ModelBuonoAltro buono = new ModelBuonoAltro();
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda);
		try {
			buono = jdbcTemplate.queryForObject(GET_BUONO_ALTRO, namedParameters, new BuonoAltroMapper());
			return buono;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "selectBuonoAltro";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public long updateDataFineDecorrenzaBuono(Long buonoId, String utenteModifica, Date decorrenzaFine)
			throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("buonoId", buonoId, Types.BIGINT).addValue("utente_modifica", utenteModifica, Types.VARCHAR)
				.addValue("decorrenzaFine", decorrenzaFine, Types.DATE);
		return jdbcTemplate.update(UPDATE_BUONO_DATA_DECORRENZA_FINE, params);
	}

	public long updateDateDecorrenzaBuono(Long buonoId, String utenteModifica, Date decorrenzaFine,
			Date decorrenzaInizio) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("buonoId", buonoId, Types.BIGINT).addValue("utente_modifica", utenteModifica, Types.VARCHAR)
				.addValue("decorrenzaFine", decorrenzaFine, Types.DATE)
				.addValue("decorrenzaInizio", decorrenzaInizio, Types.DATE);
		return jdbcTemplate.update(UPDATE_BUONO_DATE_DECORRENZA, params);
	}
}
