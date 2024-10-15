/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.custom;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import it.csi.buonores.buonoresbo.dto.ModelEnteGestore;
import it.csi.buonores.buonoresbo.dto.ModelVerificaEnte;
import it.csi.buonores.buonoresbo.dto.ModelVerificheEnte;
import it.csi.buonores.buonoresbo.exception.DatabaseException;
import it.csi.buonores.buonoresbo.integration.dao.utils.EnteGestoreMapper;
import it.csi.buonores.buonoresbo.integration.dao.utils.VerificaEnteMapper;
import it.csi.buonores.buonoresbo.integration.dao.utils.VerificheEnteMapper;
import it.csi.buonores.buonoresbo.util.LoggerUtil;

@Repository
public class EntiGestoriDao extends LoggerUtil {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	private static final String SELECT_ENTI_GESTORI = "select "
			+ "bteg.ente_gestore_denominazione, bteg.ente_gestore_id " + "from bres_t_ente_gestore bteg "
			+ "order by bteg.ente_gestore_denominazione ";

	// TODO nessuna_incompatibilita a noi serve?
	public static final String SELECT_VERIFICHE_ENTE_BY_NUMERO_DOMANDA = "select "
			+ "	not btegv.nessuna_incompatibilita as presenza_altre_misure_non_compatibili, "
			+ "	btegv.verifica_note, "
			+ " btegv.verifica_data "
			+ "from "
			+ "	bres_t_domanda btd "
			+ "left join bres_t_ente_gestore_verifica btegv on "
			+ "	btd.domanda_id = btegv.domanda_id "
			+ "where "
			+ "	btegv.data_cancellazione is null "
			+ "	and btd.data_cancellazione is null "
			+ "	and now() between coalesce(btegv.validita_inizio,now()) and coalesce(btegv.validita_fine,now()) "
			+ "	and btd.domanda_numero = :domanda_numero";

	public static final String UPDATE_NOTA_ENTE_DETTAGLIO_DOMANDA = "UPDATE bres_t_ente_gestore_verifica "
			+ "SET data_modifica = now(), utente_modifica = :utenteModifica, note_regione = :notaregione "
			+ "WHERE verifica_data is null and domanda_det_id=:idDomanda";

	public static final String UPDATE_VALIDITA_FINE_ULTIMA_RICHIESTA = "UPDATE bres_t_ente_gestore_verifica "
			+ "SET data_modifica = now(), utente_modifica = :utenteModifica, validita_fine = now() "
			+ "WHERE validita_fine is null and domanda_id=:idDomanda";

	public static final String INSERT_VERIFICA_BY_RICHIESTA = "insert into bres_t_ente_gestore_verifica(  "
			+ "			 	ente_gestore_id ,  "
			+ "			 	sportello_id ,  "
			+ "			 	domanda_id ,  "
			+ "			 	domanda_det_id ,  "
			+ "			 	utente_creazione ,  "
			+ "			 	utente_modifica , "
			+ "				verifica_eg_richiesta ,"
			+ "			 	note_regione, "
			+ "			 	verifica_eg_richiesta_data "
			+ "			 ) values ("
			+ "			 	(  "
			+ "			 		select bteg.ente_gestore_id  "
			+ "			 		from bres_t_domanda btd, bres_t_domanda_dettaglio btdd,   "
			+ "			 			bres_d_comune bdc, bres_r_ente_gestore_comune bregc,  "
			+ "			 			bres_t_ente_gestore bteg   "
			+ "			 		where btd.domanda_numero = :numeroDomanda  "
			+ "			 		and btd.domanda_id = btdd.domanda_id  "
			+ "			 		and btdd.destinatario_residenza_comune = bdc.comune_desc   "
			+ "			 		and bdc.comune_id = bregc.comune_id  "
			+ "			 		and bregc.ente_gestore_id = bteg.ente_gestore_id  "
			+ "			 		and btd.data_cancellazione is null  "
			+ "			 		and btdd.data_cancellazione is null  "
			+ "			 		and now() between btdd.validita_inizio and coalesce(btdd.validita_fine, now())  "
			+ "			 		and bdc.data_cancellazione is null  "
			+ "			 		and now() between bdc.validita_inizio and coalesce(bdc.validita_fine, now())  "
			+ "			 		and bregc.data_cancellazione is null  "
			+ "			 		and now() between bregc.validita_inizio and coalesce(bregc.validita_fine, now())  "
			+ "			 		and bteg.data_cancellazione is null  "
			+ "			 		and now() between bteg.validita_inizio and coalesce(bteg.validita_fine, now())  "
			+ "			 	),  "
			+ "			 	(  "
			+ "			 		select btd.sportello_id    "
			+ "			 		from bres_t_domanda btd, bres_t_sportello bts   "
			+ "			 		where btd.domanda_numero = :numeroDomanda  "
			+ "			 		and btd.sportello_id = bts.sportello_id  "
			+ "			 		and bts.data_cancellazione is null  "
			+ "			 		and btd.data_cancellazione is null  "
			+ "			 	),  "
			+ "			 	(  "
			+ "			 		select btd.domanda_id   "
			+ "			 		from bres_t_domanda btd  "
			+ "			 		where btd.domanda_numero = :numeroDomanda  "
			+ "			 		and btd.data_cancellazione is null  "
			+ "			 	),  "
			+ "			 	(  "
			+ "			 		select btdd.domanda_det_id   "
			+ "			 		from bres_t_domanda btd, bres_t_domanda_dettaglio btdd   "
			+ "			 		where btd.domanda_numero = :numeroDomanda  "
			+ "			 		and btd.domanda_id = btdd.domanda_id  "
			+ "			 		and btd.data_cancellazione is null  "
			+ "			 		and btdd.data_cancellazione is null  "
			+ "			 		and now() between btdd.validita_inizio and coalesce(btdd.validita_fine, now())  "
			+ "			 	),  "
			+ "			 	:utenteModifica,  "
			+ "			 	:utenteModifica, "
			+ "				true, "
			+ "			 	:nota_richiesta, "
			+ "			 	now() "
			+ "			 ) ;";

	public static final String UPDATE_RETTIFICA_ENTE_DETTAGLIO_DOMANDA = "UPDATE bres_t_ente_gestore_verifica "
			+ "SET data_modifica = now(), utente_modifica = :utenteModifica, verifica_eg_richiesta = :verificaegrichiesta, "
			+ "verifica_eg_in_corso = :verificaegincorso, verifica_eg_conclusa = :verificaegconclusa "
			+ "WHERE verifica_data is null and domanda_det_id=:idDomanda";

	public static final String UPDATE_TO_VERIFICA_IN_CORSO = "update "
			+ "	bres_t_ente_gestore_verifica "
			+ "set "
			+ "	verifica_eg_richiesta = null,  "
			+ "			 verifica_eg_in_corso = true,  "
			+ "			 verifica_eg_conclusa = null,  "
			+ "			 data_modifica = now(),  "
			+ "			 utente_modifica = :codFiscale "
			+ "where "
			+ " validita_fine is null and "
			+ "	verifica_eg_richiesta = true and "
			+ "	verifica_eg_in_corso is null and"
			+ "	verifica_eg_conclusa is null and"
			+ "	domanda_id in ( "
			+ "	select "
			+ "		btdd.domanda_id "
			+ "	from "
			+ "		bres_t_domanda btd, "
			+ "		bres_t_domanda_dettaglio btdd "
			+ "	where "
			+ "		btd.data_cancellazione is null "
			+ "		and btdd.data_cancellazione is null "
			+ "		and btdd.validita_fine is null "
			+ "		and now()::date between btdd.validita_inizio::date and coalesce(btdd.validita_fine::date, "
			+ "		now()::date) "
			+ "			and btdd.domanda_id = btd.domanda_id "
			+ "			and btd.domanda_numero in ( ";

	/*
	 * Nuova parte verifica da parte dell'ente gestore
	 */
	public static String SELECT_VERIFICA_BY_NUMERO_DOMANDA = "select btegv.verifica_id, to_char(btegv.verifica_data, 'DD/MM/YYYY') as data_verifica,  "
			+ "	btegv.verifica_note, btegv.nessuna_incompatibilita, "
			+ "	btegv.ente_gestore_id, btegv.sportello_id, btegv.domanda_id,  "
			+ "	btegv.domanda_det_id, btegv.utente_creazione, "
			+ "	btegv.verifica_eg_richiesta, btegv.verifica_eg_in_corso, btegv.verifica_eg_conclusa, "
			+ "	to_char(btegv.verifica_eg_richiesta_data, 'DD/MM/YYYY HH24:MI:SS') as data_richiesta_verifica "
			+ "from bres_t_ente_gestore_verifica btegv, bres_t_domanda btd "
			+ "where btd.domanda_numero = :numeroDomanda "
			+ "and btegv.domanda_id = btd.domanda_id "
			+ "and btd.data_cancellazione is null "
			+ "and btegv.data_cancellazione is null "
			+ "and now() between btegv.validita_inizio and coalesce(btegv.validita_fine, now());";

	public static String UPDATE_VERIFICA_ENTE = "update bres_t_ente_gestore_verifica "
			+ "set validita_fine = now(),  "
			+ "	utente_modifica = :utenteModifica "
			+ "where verifica_id = :verificaId ";

	public static String UPDATE_VERIFICA_ENTE_TO_CONCLUSA = "update bres_t_ente_gestore_verifica "
			+ "set verifica_data = now(), "
			+ " data_modifica = now(), "
			+ " verifica_note = :noteEnte, "
			+ " nessuna_incompatibilita = :nessuna_incompatibilita, "
			+ " verifica_eg_richiesta = null, "
			+ " verifica_eg_in_corso = null, "
			+ " verifica_eg_conclusa = true, "
			+ "	utente_modifica = :utenteModifica "
			+ "where verifica_id = :verificaId ";

	public static String INSERT_VERIFICA_ENTE = "insert into bres_t_ente_gestore_verifica(  "
			+ "	verifica_data ,  "
			+ " verifica_eg_richiesta , "
			+ "	verifica_eg_in_corso , "
			+ "	verifica_eg_conclusa , "
			+ "	verifica_note ,  "
			+ "	nessuna_incompatibilita ,  "
			+ "	ente_gestore_id ,  "
			+ "	sportello_id ,  "
			+ "	domanda_id ,  "
			+ "	domanda_det_id ,  "
			+ "	utente_creazione ,  "
			+ "	utente_modifica   "
			+ ") values (  "
			+ "	now() ,  "
			+ "	:verificaEgRichiesta ,"
			+ "	:verificaEgInCorso , "
			+ "	:verificaEgConclusa , "
			+ "	:verificaNote , "
			+ "	:nessunaIncompatibilita ,  "
			+ "( "
			+ "		select bteg.ente_gestore_id "
			+ "		from bres_t_domanda btd, bres_t_domanda_dettaglio btdd, "
			+ "			bres_d_comune bdc, bres_r_ente_gestore_comune bregc, "
			+ "			bres_t_ente_gestore bteg "
			+ "		where btd.domanda_numero = :numeroDomanda "
			+ "		and btd.domanda_id = btdd.domanda_id "
			+ "		and btdd.destinatario_residenza_comune = bdc.comune_desc  "
			+ "		and bdc.comune_id = bregc.comune_id "
			+ "		and bregc.ente_gestore_id = bteg.ente_gestore_id "
			+ "		and btd.data_cancellazione is null "
			+ "		and btdd.data_cancellazione is null "
			+ "		and now() between btdd.validita_inizio and coalesce(btdd.validita_fine, now()) "
			+ "		and bdc.data_cancellazione is null "
			+ "		and now() between bdc.validita_inizio and coalesce(bdc.validita_fine, now()) "
			+ "		and bregc.data_cancellazione is null "
			+ "		and now() between bregc.validita_inizio and coalesce(bregc.validita_fine, now()) "
			+ "		and bteg.data_cancellazione is null "
			+ "		and now() between bteg.validita_inizio and coalesce(bteg.validita_fine, now()) "
			+ "), "
			+ "( "
			+ "		select btd.sportello_id "
			+ "		from bres_t_domanda btd, bres_t_sportello bts "
			+ "		where btd.domanda_numero = :numeroDomanda "
			+ "		and btd.sportello_id = bts.sportello_id "
			+ "		and bts.data_cancellazione is null "
			+ "		and btd.data_cancellazione is null "
			+ "), "
			+ "( "
			+ "		select btd.domanda_id   "
			+ "		from bres_t_domanda btd  "
			+ "		where btd.domanda_numero = :numeroDomanda  "
			+ "		and btd.data_cancellazione is null  "
			+ "), "
			+ "( "
			+ "		select btdd.domanda_det_id   "
			+ "		from bres_t_domanda btd, bres_t_domanda_dettaglio btdd "
			+ "		where btd.domanda_numero = :numeroDomanda "
			+ "		and btd.domanda_id = btdd.domanda_id "
			+ "		and btd.data_cancellazione is null "
			+ "		and btdd.data_cancellazione is null "
			+ "		and now() between btdd.validita_inizio and coalesce(btdd.validita_fine, now()) "
			+ "), "
			+ "	:utenteCreazione, "
			+ "	:utenteModifica "
			+ ");";

	public List<ModelEnteGestore> selectEntiGestori() throws DatabaseException {
		List<ModelEnteGestore> entiGestori = new ArrayList<ModelEnteGestore>();
		try {
			entiGestori = jdbcTemplate.query(SELECT_ENTI_GESTORI, new EnteGestoreMapper());
			return entiGestori;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "selectEntiGestori";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public ModelVerificheEnte selectVerificheEnteByNumeroDomanda(String domanda_numero)
			throws DatabaseException {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("domanda_numero", domanda_numero);
		try {
			return jdbcTemplate.queryForObject(SELECT_VERIFICHE_ENTE_BY_NUMERO_DOMANDA, namedParameters,
					new VerificheEnteMapper());
		} catch (EmptyResultDataAccessException e) {
			return new ModelVerificheEnte();
		} catch (Exception e) {
			String methodName = "selectVerificheEnteByNumeroDomanda";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public long updateRettificaDettaglio(BigDecimal idDettaglio, String utenteModifica, Boolean verificaegrichiesta,
			Boolean verificaegincorso, Boolean verificaegconclusa) throws DatabaseException {

		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("idDomanda", idDettaglio, Types.BIGINT)
					.addValue("utenteModifica", utenteModifica, Types.VARCHAR)
					.addValue("verificaegrichiesta", verificaegrichiesta, Types.BOOLEAN)
					.addValue("verificaegincorso", verificaegincorso, Types.BOOLEAN)
					.addValue("verificaegconclusa", verificaegconclusa, Types.BOOLEAN);

			return jdbcTemplate.update(UPDATE_RETTIFICA_ENTE_DETTAGLIO_DOMANDA, params);
		} catch (Exception e) {
			String methodName = "updateRettificaDettaglio";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public long updateNoteEnteDettaglio(BigDecimal idDettaglio, String utenteModifica, String notaregione)
			throws DatabaseException {

		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("idDomanda", idDettaglio, Types.BIGINT).addValue("notaregione", notaregione, Types.VARCHAR)
					.addValue("utenteModifica", utenteModifica, Types.VARCHAR);
			return jdbcTemplate.update(UPDATE_NOTA_ENTE_DETTAGLIO_DOMANDA, params);
		} catch (Exception e) {
			String methodName = "updateNoteEnteDettaglio";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public long updateValiditaFineUltimaVerifica(BigDecimal idDomanda, String utenteModifica)
			throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("idDomanda", idDomanda, Types.BIGINT).addValue("utenteModifica", utenteModifica,
					Types.VARCHAR);
			return jdbcTemplate.update(UPDATE_VALIDITA_FINE_ULTIMA_RICHIESTA, params);
		} catch (Exception e) {
			String methodName = "updateValiditaFineUltimaVerifica";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public long insertVerificaByRichiesta(String numeroDomanda, String utenteModifica, String notaregione)
			throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("numeroDomanda", numeroDomanda, Types.VARCHAR)
					.addValue("nota_richiesta", notaregione, Types.VARCHAR)
					.addValue("utenteModifica", utenteModifica, Types.VARCHAR);
			return jdbcTemplate.update(INSERT_VERIFICA_BY_RICHIESTA, params);
		} catch (Exception e) {
			String methodName = "insertVerificaByRichiesta";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public long updateToVerificaInCorso(String numeroDomande[], String codFiscale) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("codFiscale", codFiscale, Types.VARCHAR);
			String query = "";
			query += UPDATE_TO_VERIFICA_IN_CORSO;
			for (int i = 0; i < numeroDomande.length; i++) {
				if (i != (numeroDomande.length - 1)) {
					query += "'" + numeroDomande[i] + "', ";
				} else {
					query += "'" + numeroDomande[i] + "'";
				}
			}
			query += " )) ";
			return jdbcTemplate.update(query, params);
		} catch (Exception e) {
			String methodName = "updateToVerificaInCorso";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	/*
	 * Nuova parte verifica da parte dell'ente gestore
	 */
	public ModelVerificaEnte selectVerificaByNumeroDomanda(String numeroDomanda) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("numeroDomanda", numeroDomanda, Types.VARCHAR);
			return jdbcTemplate.queryForObject(SELECT_VERIFICA_BY_NUMERO_DOMANDA, params, new VerificaEnteMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "selectVerificaByNumeroDomanda";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public Integer updateVerificaEnte(Long verificaId, String utenteModifica) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("verificaId", verificaId, Types.BIGINT)
					.addValue("utenteModifica", utenteModifica, Types.VARCHAR);
			return jdbcTemplate.update(UPDATE_VERIFICA_ENTE, params);
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "updateVerificaEnte";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public Integer insertVerificaEnte(String numeroDomanda, String verificaNote, Boolean nessunaIncompatibilita,
			String utenteCreazione, String utenteModifica, Boolean verificaEgRichiesta, Boolean verificaEgInCorso,
			Boolean verificaEgConclusa, String verificaDataRichiesta) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("numeroDomanda", numeroDomanda, Types.VARCHAR)
					.addValue("verificaNote", verificaNote, Types.VARCHAR)
					.addValue("nessunaIncompatibilita", nessunaIncompatibilita, Types.BOOLEAN)
					.addValue("utenteCreazione", utenteCreazione, Types.VARCHAR)
					.addValue("utenteModifica", utenteModifica, Types.VARCHAR)
					.addValue("verificaEgRichiesta", verificaEgRichiesta, Types.BOOLEAN)
					.addValue("verificaEgInCorso", verificaEgInCorso, Types.BOOLEAN)
					.addValue("verificaEgConclusa", verificaEgConclusa, Types.BOOLEAN)
					.addValue("verificaRichiestaData", verificaDataRichiesta, Types.VARCHAR);

			return jdbcTemplate.update(INSERT_VERIFICA_ENTE, params);
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "insertVerificaEnte";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public Integer updateVerificaEnteToConclusa(Long verificaId, String utenteModifica, String verificaNote,
			Boolean presenzaAltreIncopatibilita) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("verificaId", verificaId, Types.BIGINT)
					.addValue("utenteModifica", utenteModifica, Types.VARCHAR)
					.addValue("noteEnte", verificaNote, Types.VARCHAR)
					.addValue("nessuna_incompatibilita", !presenzaAltreIncopatibilita, Types.BOOLEAN);
			return jdbcTemplate.update(UPDATE_VERIFICA_ENTE_TO_CONCLUSA, params);
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "updateVerificaEnteToConclusa";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}
}
