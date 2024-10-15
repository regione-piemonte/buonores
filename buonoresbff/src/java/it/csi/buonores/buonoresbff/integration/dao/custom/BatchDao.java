/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.integration.dao.custom;

import java.sql.Types;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import it.csi.buonores.buonoresbff.integration.dao.utils.RichiesteBatchMapper;
import it.csi.buonores.buonoresbff.dto.custom.ModelRichiestaBatch;
import it.csi.buonores.buonoresbff.exception.DatabaseException;
import it.csi.buonores.buonoresbff.util.LoggerUtil;

@Repository
public class BatchDao extends LoggerUtil {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public static final String INSERT_BATCH_ESECUZIONE_KO = "INSERT INTO bres_t_batch_esecuzione (batchesec_richiedente, batchesec_tentativo_numero, "
			+ "batchesec_fine, sportello_id, domanda_id, domanda_det_id, batch_id, "
			+ "batchesecstato_id, batchmotivo_id, "
			+ "utente_creazione, utente_modifica) values "
			+ "(:batchesec_richiedente, :batchesec_tentativo_numero, now(), :sportello_id, :domanda_id, :domanda_det_id, :batch_id, "
			+ ":batchesecstato_id, :batchmotivo_id, :utente_creazione, :utente_modifica)";

	public static final String INSERT_BATCH_ESECUZIONE_OK = "INSERT INTO bres_s_batch_esecuzione (batchesec_richiedente, batchesec_tentativo_numero, "
			+ "batchesec_fine, sportello_id, domanda_id, domanda_det_id, batch_id, "
			+ "batchesecstato_id, batchmotivo_id, "
			+ "utente_creazione, utente_modifica) values "
			+ "(:batchesec_richiedente, :batchesec_tentativo_numero, now(), :sportello_id, :domanda_id, :domanda_det_id, :batch_id, "
			+ ":batchesecstato_id, :batchmotivo_id, :utente_creazione, :utente_modifica)";

	public static final String UPDATE_BATCH_ESECUZIONE_OK = "UPDATE bres_s_batch_esecuzione SET "
			+ "batchesec_tentativo_numero= :batchesec_tentativo_numero, data_modifica=now(), utente_modifica= :utente_modifica "
			+ "WHERE sportello_id = :sportello_id "
			+ "and domanda_id = :domanda_id "
			+ "and domanda_det_id = :domanda_det_id "
			+ "and batch_id = :batch_id "
			+ "and batchesecstato_id = :batchesecstato_id "
			+ "and batchmotivo_id = :batchmotivo_id";

	public static final String UPDATE_BATCH_ESECUZIONE_KO = "UPDATE bres_t_batch_esecuzione SET "
			+ "batchesec_tentativo_numero= :batchesec_tentativo_numero, data_modifica=now(), utente_modifica= :utente_modifica "
			+ "WHERE sportello_id = :sportello_id "
			+ "and domanda_id = :domanda_id "
			+ "and domanda_det_id = :domanda_det_id "
			+ "and batch_id = :batch_id "
			+ "and batchesecstato_id = :batchesecstato_id "
			+ "and batchmotivo_id = :batchmotivo_id";

	public static final String SELECT_BATCH_ESECUZIONE_OK = "select batchesec_id from bres_s_batch_esecuzione "
			+ "WHERE sportello_id = :sportello_id "
			+ "and domanda_id = :domanda_id "
			+ "and domanda_det_id = :domanda_det_id "
			+ "and batch_id = :batch_id "
			+ "and batchesecstato_id = :batchesecstato_id "
			+ "and batchmotivo_id = :batchmotivo_id";

	public static final String SELECT_BATCH_ESECUZIONE_KO = "select batchesec_id from bres_t_batch_esecuzione "
			+ "WHERE sportello_id = :sportello_id "
			+ "and domanda_id = :domanda_id "
			+ "and domanda_det_id = :domanda_det_id "
			+ "and batch_id = :batch_id "
			+ "and batchesecstato_id = :batchesecstato_id "
			+ "and batchmotivo_id = :batchmotivo_id";

	public static final String INSERT_BATCH_ESECUZIONE_STEP_OK = "INSERT INTO bres_s_batch_esecuzione_det (batchesecdet_step, batchesecdet_note, "
			+ "batchesecdet_fine, batchesec_id,batchesecstato_id, "
			+ "utente_creazione, utente_modifica, messageuuid) "
			+ "VALUES(:batchesecdet_step, :batchesecdet_note, now(), :batchesec_id, :batchesecstato_id, :utente_creazione, :utente_modifica, :messageuuid)";

	public static final String INSERT_BATCH_ESECUZIONE_STEP_KO = "INSERT INTO bres_t_batch_esecuzione_det (batchesecdet_step, batchesecdet_note, "
			+ "batchesecdet_fine, batchesec_id, batchesecstato_id, "
			+ "utente_creazione, utente_modifica) "
			+ "VALUES(:batchesecdet_step, :batchesecdet_note, now(), :batchesec_id, :batchesecstato_id, :utente_creazione, :utente_modifica)";

	public static final String SELECT_STATO_BATCH = "SELECT batchesecstato_id FROM bres_d_batch_stato "
			+ "where batchesecstato_cod = :batchesecstato_cod "
			+ "and validita_fine is null "
			+ "and validita_inizio <= now() "
			+ "and data_cancellazione is null";

	public static final String SELECT_MOTIVO_BATCH = "SELECT batchmotivo_id FROM bres_d_batch_motivo "
			+ "where batchmotivo_cod = :batchmotivo_cod "
			+ "and validita_fine is null "
			+ "and validita_inizio <= now() "
			+ "and data_cancellazione is null";

	public static final String SELECT_BATCH = "SELECT batch_id FROM bres_d_batch a,bres_d_batch_gruppo b "
			+ "where a.batch_cod = :batch_cod "
			+ "and a.validita_fine is null "
			+ "and a.validita_inizio <= now() "
			+ "and a.data_cancellazione is null "
			+ "and b.validita_fine is null "
			+ "and b.validita_inizio <= now() "
			+ "and b.data_cancellazione is null "
			+ "and a.batchgruppo_id=b.batchgruppo_id "
			+ "and b.batchgruppo_cod='SERVIZIO_BFF'";

	public static final String SELECT_BATCH_PARAMETRO = "SELECT batchparam_valore "
			+ "FROM bres_d_batch_parametro a,bres_d_batch b,bres_d_batch_gruppo c "
			+ "where a.batch_id = b.batch_id "
			+ "and b.batch_cod = :batch_cod "
			+ "and batchparam_cod = :batchparam_cod "
			+ "and a.validita_fine is null "
			+ "and a.validita_inizio <= now() "
			+ "and a.data_cancellazione is null "
			+ "and b.validita_fine is null "
			+ "and b.validita_inizio <= now() "
			+ "and b.data_cancellazione is null "
			+ "and c.validita_fine is null "
			+ "and c.validita_inizio <= now() "
			+ "and c.data_cancellazione is null "
			+ "and b.batchgruppo_id=c.batchgruppo_id "
			+ "and c.batchgruppo_cod='SERVIZIO_BFF'";

	public static final String SELECT_DATI_DOMANDA_BATCH = "select btdd.sportello_id,btdd.domanda_id,btdd.domanda_det_id,bdds.domanda_stato_cod from "
			+ "bres_t_domanda btd, "
			+ "bres_t_domanda_dettaglio btdd, bres_d_domanda_stato bdds "
			+ "where btd.data_cancellazione is null "
			+ "and bdds.domanda_stato_id = btdd.domanda_stato_id "
			+ "and btdd.domanda_id = btd.domanda_id "
			+ "and btdd.data_cancellazione is null "
			+ "and btdd.validita_inizio <= now() "
			+ "and btdd.validita_fine is null "
			+ "and btd.domanda_numero= :domanda_numero";

	public static final String SELECT_NUMERO_TENTATIVI_OK = "select batchesec_tentativo_numero from bres_s_batch_esecuzione a, bres_d_batch_motivo b "
			+ "where sportello_id = :sportello_id and domanda_id = :domanda_id and domanda_det_id = :domanda_det_id "
			+ "and b.batchmotivo_id =a.batchmotivo_id "
			+ "and b.batchmotivo_cod = :batchmotivo_cod "
			+ "and a.batch_id = (select batch_id from bres_d_batch where batch_cod = :batchCod) "
			+ "and b.data_cancellazione is null "
			+ "and a.data_cancellazione is null";

	public static final String SELECT_NUMERO_TENTATIVI_KO = "select batchesec_tentativo_numero from bres_t_batch_esecuzione a, bres_d_batch_motivo b "
			+ "where sportello_id = :sportello_id and domanda_id = :domanda_id and domanda_det_id = :domanda_det_id "
			+ "and b.batchmotivo_id =a.batchmotivo_id "
			+ "and b.batchmotivo_cod = :batchmotivo_cod "
			+ "and a.batch_id = (select batch_id from bres_d_batch where batch_cod = :batchCod) "
			+ "and b.data_cancellazione is null "
			+ "and a.data_cancellazione is null";

	public static final String SELECT_ESITO_POSITIVO = "select count(*) from bres_s_batch_esecuzione a,bres_s_batch_esecuzione_det b "
			+ "where sportello_id = :sportello_id and domanda_id = :domanda_id and domanda_det_id = :domanda_det_id "
			+ "and a.batchesec_id = b.batchesec_id "
			+ "and a.data_cancellazione is null "
			+ "and b.data_cancellazione is null "
			+ "and b.batchesecdet_step = :batchesecdet_step";

	public static final String SELECT_ESISTE_RECORD_AVVIATO_KO = "select batchesec_id from bres_t_batch_esecuzione a, bres_d_batch_stato b,"
			+ "bres_d_batch c,bres_d_batch_gruppo d "
			+ "where a.batchesecstato_id = b.batchesecstato_id "
			+ "and b.batchesecstato_cod = :batchesecstato_cod "
			+ "and c.batch_id = a.batch_id "
			+ "and d.batchgruppo_id =c.batchgruppo_id "
			+ "and d.batchgruppo_cod ='SERVIZIO_BFF' "
			+ "and a.sportello_id = :sportello_id "
			+ "and a.domanda_id = :domanda_id "
			+ "and a.domanda_det_id = :domanda_det_id "
			+ "and a.data_cancellazione is null "
			+ "and b.validita_inizio  <= now() "
			+ "and b.validita_fine is null "
			+ "and c.batch_cod = :batch_cod "
			+ "and b.data_cancellazione is null "
			+ "and c.data_cancellazione is null "
			+ "and d.data_cancellazione is null";

	public static final String SELECT_ESISTE_RECORD_AVVIATO_OK = "select batchesec_id from bres_s_batch_esecuzione a, bres_d_batch_stato b,"
			+ "bres_d_batch c,bres_d_batch_gruppo d "
			+ "where a.batchesecstato_id = b.batchesecstato_id "
			+ "and b.batchesecstato_cod = :batchesecstato_cod "
			+ "and c.batch_id = a.batch_id "
			+ "and d.batchgruppo_id =c.batchgruppo_id "
			+ "and d.batchgruppo_cod ='SERVIZIO_BFF' "
			+ "and a.sportello_id = :sportello_id "
			+ "and a.domanda_id = :domanda_id "
			+ "and a.domanda_det_id = :domanda_det_id "
			+ "and a.data_cancellazione is null "
			+ "and b.validita_inizio  <= now() "
			+ "and b.validita_fine is null "
			+ "and c.batch_cod = :batch_cod "
			+ "and b.data_cancellazione is null "
			+ "and c.data_cancellazione is null "
			+ "and d.data_cancellazione is null";

	public static final String SELECT_ESISTE_RECORD_KO = "select count(*) from bres_t_batch_esecuzione a, bres_t_batch_esecuzione_det b "
			+ "where a.batchesec_id = b.batchesec_id "
			+ "and a.batchesec_id = :batchesec_id";

	public static final String SELECT_ESISTE_RECORD_OK = "select count(*) from bres_s_batch_esecuzione a, bres_s_batch_esecuzione_det b "
			+ "where a.batchesec_id = b.batchesec_id "
			+ "and a.batchesec_id = :batchesec_id";

	public static final String DELETE_RECORD_OK = "delete from bres_s_batch_esecuzione "
			+ "where batchesec_id = :batchesec_id";

	public static final String DELETE_RECORD_KO = "delete from bres_t_batch_esecuzione "
			+ "where batchesec_id = :batchesec_id";

	public static final String SELECT_RECORD_DUPLICATI_KO = "SELECT bsbe1.batchesec_id FROM bres_t_batch_esecuzione bsbe1 "
			+ "where bsbe1.batchesec_id in ( "
			+ "select distinct bsbe1.batchesec_id  from bres_s_batch_esecuzione bsbe , bres_s_batch_esecuzione_det bsbed, "
			+ "bres_t_batch_esecuzione bsbe1 , bres_t_batch_esecuzione_det bsbed1 "
			+ "where bsbe.batchesec_id = bsbed.batchesec_id "
			+ "and bsbe1.batchesec_id = bsbed1.batchesec_id "
			+ "and bsbe.data_cancellazione is null "
			+ "and bsbed.data_cancellazione is null "
			+ "and bsbe1.data_cancellazione is null "
			+ "and bsbed1.data_cancellazione is null "
			+ "and bsbe.batchesec_richiedente = bsbe1.batchesec_richiedente "
			+ "and bsbe.sportello_id = bsbe1.sportello_id "
			+ "and bsbe.domanda_id = bsbe1.domanda_id "
			+ "and bsbe.domanda_det_id = bsbe1.domanda_det_id "
			+ "and bsbe.batch_id = bsbe1.batch_id "
			+ "and bsbe.batchmotivo_id = bsbe1.batchmotivo_id "
			+ "and bsbed.batchesecdet_step = bsbed1.batchesecdet_step)";

	public static final String DELETE_RECORD_DUPLICATI_KO_STEP = "DELETE FROM bres_t_batch_esecuzione_det bsbed1 "
			+ "WHERE bsbed1.batchesec_id = :batchesec_id";

	public static final String DELETE_RECORD_DUPLICATI_KO = "delete FROM bres_t_batch_esecuzione bsbe1 "
			+ "where bsbe1.batchesec_id = :batchesec_id";

	public long DeleteRecordOk(Long batchesecId) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("batchesec_id", batchesecId);
		try {
			return jdbcTemplate.update(DELETE_RECORD_OK, namedParameters);
		} catch (Exception e) {
			String methodName = "DELETE_RECORD_OK";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public long DeleteRecordKo(Long batchesecId) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("batchesec_id", batchesecId);
		try {
			return jdbcTemplate.update(DELETE_RECORD_KO, namedParameters);
		} catch (Exception e) {
			String methodName = "DELETE_RECORD_KO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public int selectEsitoKo(Long batchesecId) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("batchesec_id", batchesecId);
		try {
			return jdbcTemplate.queryForObject(SELECT_ESISTE_RECORD_KO, namedParameters, Integer.class);
		} catch (EmptyResultDataAccessException e) {
			return 0;
		} catch (Exception e) {
			String methodName = "SELECT_ESISTE_RECORD_KO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public Integer selectEsitoPositivo(ModelRichiestaBatch richiesta, String batchesecdetStep)
			throws DatabaseException {
		Integer valorint = 0;
		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("sportello_id", richiesta.getSportelloId())
				.addValue("domanda_id", richiesta.getDomandaId())
				.addValue("domanda_det_id", richiesta.getDomandaDetId())
				.addValue("batchesecdet_step", batchesecdetStep);
		try {
			valorint = jdbcTemplate.queryForObject(SELECT_ESITO_POSITIVO, namedParameters, Integer.class);
			return valorint;
		} catch (EmptyResultDataAccessException e) {
			return 0;
		} catch (Exception e) {
			String methodName = "SELECT_ESITO_POSITIVO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public int selectEsitoOk(Long batchesecId) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("batchesec_id", batchesecId);
		try {
			return jdbcTemplate.queryForObject(SELECT_ESISTE_RECORD_OK, namedParameters, Integer.class);
		} catch (EmptyResultDataAccessException e) {
			return 0;
		} catch (Exception e) {
			String methodName = "SELECT_ESISTE_RECORD_OK";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public long selectEsisteAvviatoKo(String batchesecstatoCod, ModelRichiestaBatch richiesta, String batchCod)
			throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("batchesecstato_cod", batchesecstatoCod)
				.addValue("sportello_id", richiesta.getSportelloId())
				.addValue("domanda_id", richiesta.getDomandaId())
				.addValue("domanda_det_id", richiesta.getDomandaDetId())
				.addValue("batch_cod", batchCod);
		try {
			return jdbcTemplate.queryForObject(SELECT_ESISTE_RECORD_AVVIATO_KO, namedParameters, Long.class);
		} catch (EmptyResultDataAccessException e) {
			return 0;
		} catch (Exception e) {
			String methodName = "SELECT_ESISTE_RECORD_AVVIATO_KO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public long selectEsisteAvviatoOk(String batchesecstatoCod, ModelRichiestaBatch richiesta, String batchCod)
			throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("batchesecstato_cod", batchesecstatoCod)
				.addValue("sportello_id", richiesta.getSportelloId())
				.addValue("domanda_id", richiesta.getDomandaId())
				.addValue("domanda_det_id", richiesta.getDomandaDetId())
				.addValue("batch_cod", batchCod);
		try {
			return jdbcTemplate.queryForObject(SELECT_ESISTE_RECORD_AVVIATO_OK, namedParameters, Long.class);
		} catch (EmptyResultDataAccessException e) {
			return 0;
		} catch (Exception e) {
			String methodName = "SELECT_ESISTE_RECORD_AVVIATO_OK";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public long selectStatoBatch(String batchesecstatoCod) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("batchesecstato_cod",
				batchesecstatoCod);
		try {
			return jdbcTemplate.queryForObject(SELECT_STATO_BATCH, namedParameters, Long.class);
		} catch (EmptyResultDataAccessException e) {
			return 0;
		} catch (Exception e) {
			String methodName = "SELECT_STATO_BATCH";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public long selectMotivoBatch(String batchmotivoCod) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("batchmotivo_cod", batchmotivoCod);
		try {
			return jdbcTemplate.queryForObject(SELECT_MOTIVO_BATCH, namedParameters, Long.class);
		} catch (EmptyResultDataAccessException e) {
			return 0;
		} catch (Exception e) {
			String methodName = "SELECT_MOTIVO_BATCH";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public long selectBatch(String batchCod) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("batch_cod", batchCod);
		try {
			return jdbcTemplate.queryForObject(SELECT_BATCH, namedParameters, Long.class);
		} catch (EmptyResultDataAccessException e) {
			return 0;
		} catch (Exception e) {
			String methodName = "SELECT_BATCH";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public Integer selectBatchPrametro(String batchCod, String batchparamCod) throws DatabaseException {
		String result = null;
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("batch_cod", batchCod)
				.addValue("batchparam_cod", batchparamCod);
		try {
			result = jdbcTemplate.queryForObject(SELECT_BATCH_PARAMETRO, namedParameters, String.class);
			Integer valorint = Integer.parseInt(result);
			return valorint;
		} catch (EmptyResultDataAccessException e) {
			return 0;
		} catch (Exception e) {
			String methodName = "SELECT_BATCH_PARAMETRO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public Integer selectNumeroTentativiKO(ModelRichiestaBatch richiesta, String batchmotivoCod, String batchCod)
			throws DatabaseException {
		Integer valorint = 0;
		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("sportello_id", richiesta.getSportelloId())
				.addValue("domanda_id", richiesta.getDomandaId())
				.addValue("domanda_det_id", richiesta.getDomandaDetId()).addValue("batchmotivo_cod", batchmotivoCod)
				.addValue("batchCod", batchCod);

		try {
			valorint = jdbcTemplate.queryForObject(SELECT_NUMERO_TENTATIVI_KO, namedParameters, Integer.class);
			return valorint;
		} catch (EmptyResultDataAccessException e) {
			return 0;
		} catch (Exception e) {
			String methodName = "SELECT_NUMERO_TENTATIVI_KO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public Integer selectNumeroTentativiOK(ModelRichiestaBatch richiesta, String batchmotivoCod, String batchCod)
			throws DatabaseException {
		Integer valorint = 0;
		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("sportello_id", richiesta.getSportelloId())
				.addValue("domanda_id", richiesta.getDomandaId())
				.addValue("domanda_det_id", richiesta.getDomandaDetId()).addValue("batchmotivo_cod", batchmotivoCod)
				.addValue("batchCod", batchCod);
		try {
			valorint = jdbcTemplate.queryForObject(SELECT_NUMERO_TENTATIVI_OK, namedParameters, Integer.class);
			return valorint;
		} catch (EmptyResultDataAccessException e) {
			return 0;
		} catch (Exception e) {
			String methodName = "SELECT_NUMERO_TENTATIVI_OK";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public ModelRichiestaBatch selectDomandaBatch(String numeroDomanda) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("domanda_numero", numeroDomanda);
		try {
			ModelRichiestaBatch richiesta = new ModelRichiestaBatch();
			richiesta = jdbcTemplate.queryForObject(SELECT_DATI_DOMANDA_BATCH, namedParameters,
					new RichiesteBatchMapper());
			return richiesta;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_DATI_DOMANDA_BATCH";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public void UpdateBatchEsecuzioneOK(int tentativi, ModelRichiestaBatch datiDomanda, Long batchId, Long statoId,
			Long motivoId, String utente) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("batchesec_tentativo_numero", tentativi).addValue("utente_modifica", utente)
				.addValue("sportello_id", datiDomanda.getSportelloId())
				.addValue("domanda_id", datiDomanda.getDomandaId())
				.addValue("domanda_det_id", datiDomanda.getDomandaDetId())
				.addValue("batch_id", batchId).addValue("batchesecstato_id", statoId)
				.addValue("batchmotivo_id", motivoId);
		try {
			jdbcTemplate.update(UPDATE_BATCH_ESECUZIONE_OK, namedParameters);

		} catch (Exception e) {
			String methodName = "UPDATE_BATCH_ESECUZIONE_OK";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public void UpdateBatchEsecuzioneKO(int tentativi, ModelRichiestaBatch datiDomanda, Long batchId, Long statoId,
			Long motivoId, String utente) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("batchesec_tentativo_numero", tentativi).addValue("utente_modifica", utente)
				.addValue("sportello_id", datiDomanda.getSportelloId())
				.addValue("domanda_id", datiDomanda.getDomandaId())
				.addValue("domanda_det_id", datiDomanda.getDomandaDetId())
				.addValue("batch_id", batchId).addValue("batchesecstato_id", statoId)
				.addValue("batchmotivo_id", motivoId);
		try {
			jdbcTemplate.update(UPDATE_BATCH_ESECUZIONE_KO, namedParameters);

		} catch (Exception e) {
			String methodName = "UPDATE_BATCH_ESECUZIONE_KO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public long SelectBatchEsecuzioneOK(ModelRichiestaBatch datiDomanda, Long batchId, Long statoId, Long motivoId)
			throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("sportello_id", datiDomanda.getSportelloId())
				.addValue("domanda_id", datiDomanda.getDomandaId())
				.addValue("domanda_det_id", datiDomanda.getDomandaDetId())
				.addValue("batch_id", batchId).addValue("batchesecstato_id", statoId)
				.addValue("batchmotivo_id", motivoId);

		try {
			return jdbcTemplate.queryForObject(SELECT_BATCH_ESECUZIONE_OK, namedParameters, Long.class);

		} catch (EmptyResultDataAccessException e) {
			return 0;
		} catch (Exception e) {
			String methodName = "SELECT_BATCH_ESECUZIONE_OK";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public long SelectBatchEsecuzioneKO(ModelRichiestaBatch datiDomanda, Long batchId, Long statoId, Long motivoId)
			throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("sportello_id", datiDomanda.getSportelloId())
				.addValue("domanda_id", datiDomanda.getDomandaId())
				.addValue("domanda_det_id", datiDomanda.getDomandaDetId())
				.addValue("batch_id", batchId).addValue("batchesecstato_id", statoId)
				.addValue("batchmotivo_id", motivoId);

		try {
			return jdbcTemplate.queryForObject(SELECT_BATCH_ESECUZIONE_KO, namedParameters, Long.class);

		} catch (EmptyResultDataAccessException e) {
			return 0;
		} catch (Exception e) {
			String methodName = "SELECT_BATCH_ESECUZIONE_KO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public long insertBatchEsecuzioneOK(String richiedente, int tentativi, ModelRichiestaBatch datiDomanda,
			Long batchId, Long statoId, Long motivoId, String utente) throws DatabaseException {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("batchesec_richiedente", richiedente, Types.VARCHAR);
			params.addValue("batchesec_tentativo_numero", tentativi, Types.BIGINT);
			params.addValue("sportello_id", datiDomanda.getSportelloId(), Types.BIGINT);
			params.addValue("domanda_id", datiDomanda.getDomandaId(), Types.BIGINT);
			params.addValue("domanda_det_id", datiDomanda.getDomandaDetId(), Types.BIGINT);
			params.addValue("batch_id", batchId, Types.BIGINT);
			params.addValue("batchesecstato_id", statoId, Types.BIGINT);
			params.addValue("batchmotivo_id", motivoId, Types.BIGINT);
			params.addValue("utente_creazione", utente, Types.VARCHAR);
			params.addValue("utente_modifica", utente, Types.VARCHAR);

			jdbcTemplate.update(INSERT_BATCH_ESECUZIONE_OK, params, keyHolder, new String[] { "batchesec_id" });

			return keyHolder.getKey().longValue();
		} catch (Exception e) {
			String methodName = "INSERT_BATCH_ESECUZIONE_OK";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public long insertBatchEsecuzioneKO(String richiedente, int tentativi, ModelRichiestaBatch datiDomanda,
			Long batchId, Long statoId, Long motivoId, String utente) throws DatabaseException {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("batchesec_richiedente", richiedente, Types.VARCHAR);
			params.addValue("batchesec_tentativo_numero", tentativi, Types.BIGINT);
			params.addValue("sportello_id", datiDomanda.getSportelloId(), Types.BIGINT);
			params.addValue("domanda_id", datiDomanda.getDomandaId(), Types.BIGINT);
			params.addValue("domanda_det_id", datiDomanda.getDomandaDetId(), Types.BIGINT);
			params.addValue("batch_id", batchId, Types.BIGINT);
			params.addValue("batchesecstato_id", statoId, Types.BIGINT);
			params.addValue("batchmotivo_id", motivoId, Types.BIGINT);
			params.addValue("utente_creazione", utente, Types.VARCHAR);
			params.addValue("utente_modifica", utente, Types.VARCHAR);

			jdbcTemplate.update(INSERT_BATCH_ESECUZIONE_KO, params, keyHolder, new String[] { "batchesec_id" });
			return keyHolder.getKey().longValue();
		} catch (Exception e) {
			String methodName = "INSERT_BATCH_ESECUZIONE_KO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public long insertBatchEsecuzioneStepOK(String batchesecdetStep, String batchesecdetNote,
			Long batchesecId, Long batchesecstatoId, String utente, String messageuuid) throws DatabaseException {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("batchesecdet_step", batchesecdetStep, Types.VARCHAR);
			params.addValue("batchesecdet_note", batchesecdetNote, Types.VARCHAR);
			params.addValue("batchesec_id", batchesecId, Types.BIGINT);
			params.addValue("batchesecstato_id", batchesecstatoId, Types.BIGINT);
			params.addValue("utente_creazione", utente, Types.VARCHAR);
			params.addValue("utente_modifica", utente, Types.VARCHAR);
			params.addValue("messageuuid", messageuuid, Types.VARCHAR);

			jdbcTemplate.update(INSERT_BATCH_ESECUZIONE_STEP_OK, params, keyHolder, new String[] { "batchesecdet_id" });

			return keyHolder.getKey().longValue();
		} catch (Exception e) {
			String methodName = "INSERT_BATCH_ESECUZIONE_STEP_OK";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public long insertBatchEsecuzioneStepKO(String batchesecdetStep, String batchesecdetNote,
			Long batchesecId, Long batchesecstatoId, String utente) throws DatabaseException {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("batchesecdet_step", batchesecdetStep, Types.VARCHAR);
			params.addValue("batchesecdet_note", batchesecdetNote, Types.VARCHAR);
			params.addValue("batchesec_id", batchesecId, Types.BIGINT);
			params.addValue("batchesecstato_id", batchesecstatoId, Types.BIGINT);
			params.addValue("utente_creazione", utente, Types.VARCHAR);
			params.addValue("utente_modifica", utente, Types.VARCHAR);

			jdbcTemplate.update(INSERT_BATCH_ESECUZIONE_STEP_KO, params, keyHolder, new String[] { "batchesecdet_id" });
			return keyHolder.getKey().longValue();
		} catch (Exception e) {
			String methodName = "INSERT_BATCH_ESECUZIONE_STEP_KO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public void DeleteRecordDaCancellareDuplicati() throws DatabaseException {
		SqlParameterSource namedParameters = new MapSqlParameterSource();
		try {

			List<Long> elencobatchid = jdbcTemplate.queryForList(SELECT_RECORD_DUPLICATI_KO, namedParameters,
					Long.class);
			for (Long batchid : elencobatchid) {
				DeleteRecordDuplicatiKoStep(batchid);
				DeleteRecordDuplicatiKo(batchid);
			}
		} catch (Exception e) {
			String methodName = "SELECT_BATCH_ESECUZIONE_KO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public int DeleteRecordDuplicatiKoStep(Long batchesecId) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("batchesec_id", batchesecId);
		try {
			return jdbcTemplate.update(DELETE_RECORD_DUPLICATI_KO_STEP, namedParameters);
		} catch (Exception e) {
			String methodName = "DELETE_RECORD_DUPLICATI_KO_STEP";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public int DeleteRecordDuplicatiKo(Long batchesecId) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("batchesec_id", batchesecId);
		try {
			return jdbcTemplate.update(DELETE_RECORD_DUPLICATI_KO, namedParameters);
		} catch (Exception e) {
			String methodName = "DELETE_RECORD_DUPLICATI_KO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}
}