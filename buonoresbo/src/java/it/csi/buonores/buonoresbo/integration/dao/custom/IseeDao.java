/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.custom;

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

import it.csi.buonores.buonoresbo.dto.ModelEsitoIsee;
import it.csi.buonores.buonoresbo.dto.ModelIseeBuono;
import it.csi.buonores.buonoresbo.exception.DatabaseException;
import it.csi.buonores.buonoresbo.integration.dao.utils.EsitiIseeMapper;
import it.csi.buonores.buonoresbo.integration.dao.utils.IseeBuonoMapper;
import it.csi.buonores.buonoresbo.util.LoggerUtil;

@Repository
public class IseeDao extends LoggerUtil {

	public static final String SELECT_ISEE_CONFORME = "select distinct btbi.isee_id, btbi.isee_anno, btbi.isee_conforme, btbi.isee_valore, "
			+ "btbi.isee_data_rilascio, btbi.isee_scadenza,bdbive.isee_verifica_esito_cod, btbi.isee_verificato_in_data, "
			+ "btbi.isee_note, btbi.buono_id,to_char(btbi.validita_inizio,'DD/MM/YYYY') validita_inizio, btbi.utente_creazione,btbi.utente_modifica,btbi.validita_fine "
			+ "from   bres_t_domanda btd ,bres_t_buono btb "
			+ "left join bres_t_buono_isee btbi on btbi.buono_id = btb.buono_id "
			+ "and btbi.validita_fine is null and btbi.data_cancellazione is null and btbi.isee_anno = :iseeAnno "
			+ "left join bres_d_buono_isee_verifica_esito bdbive on "
			+ "bdbive.isee_verifica_esito_id = btbi.isee_verifica_esito_id and bdbive.data_cancellazione is null "
			+ "and bdbive.validita_fine is null "
			+ "where btd.domanda_id = btb.domanda_id "
			+ "and btd.domanda_numero = :numeroDomanda "
			+ "and btb.buono_id = btbi.buono_id "
			+ "and btd.data_cancellazione is null "
			+ "and btb.data_cancellazione is null";

	public static final String SELECT_LISTA_ISEE = "select distinct btbi.isee_id, btbi.isee_anno, btbi.isee_conforme, btbi.isee_valore, "
			+ "btbi.isee_data_rilascio, btbi.isee_scadenza,bdbive.isee_verifica_esito_cod, btbi.isee_verificato_in_data, "
			+ "btbi.isee_note, btbi.buono_id,to_char(btbi.validita_inizio,'DD/MM/YYYY') validita_inizio, btbi.utente_creazione,btbi.utente_modifica,btbi.validita_fine "
			+ "from bres_t_domanda btd ,bres_t_buono btb "
			+ "left join bres_t_buono_isee btbi on btbi.buono_id = btb.buono_id "
			+ "and btbi.data_cancellazione is null  "
			+ "left join bres_d_buono_isee_verifica_esito bdbive on "
			+ "bdbive.isee_verifica_esito_id = btbi.isee_verifica_esito_id and bdbive.data_cancellazione is null "
			+ "and bdbive.validita_fine is null "
			+ "where btd.domanda_id = btb.domanda_id "
			+ "and btd.domanda_numero = :numeroDomanda "
			+ "and btb.buono_id = btbi.buono_id "
			+ "and btd.data_cancellazione is null "
			+ "and btb.data_cancellazione is null "
			+ "order by btbi.isee_anno,btbi.isee_id desc";

	public static final String INSERT_ISEE_CONFORME = "insert into bres_t_buono_isee (isee_anno,utente_creazione,utente_modifica,isee_valore, buono_id, "
			+ "isee_data_rilascio, isee_scadenza,isee_verificato_in_data, isee_note,isee_verifica_esito_id) "
			+ "values (:iseeAnno, :utenteCrea, :utenteModifica, :iseeValore, ( "
			+ "select btb.buono_id from bres_t_buono btb, bres_t_domanda btd "
			+ "where btd.domanda_id = btb.domanda_id "
			+ "and btd.domanda_numero = :numeroDomanda "
			+ "and btd.data_cancellazione is null "
			+ "and btb.data_cancellazione is null), "
			+ ":dataRilascio, :dataScadenza, now(),:iseeNote, "
			+ "(select isee_verifica_esito_id from bres_d_buono_isee_verifica_esito "
			+ "where data_cancellazione is null and validita_fine is null and isee_verifica_esito_cod= :codEsito))";

	public static final String INSERT_ISEE_CONFORME_SELECT = "insert into bres_t_buono_isee (isee_anno, isee_conforme, isee_valore, "
			+ "isee_data_rilascio, isee_scadenza, "
			+ "isee_verifica_esito_id, isee_verificato_in_data, isee_note, buono_id,utente_creazione, utente_modifica) "
			+ "select :iseeAnno, isee_conforme, :iseeValore, :dataRilascio, :dataScadenza, "
			+ "(select isee_verifica_esito_id from bres_d_buono_isee_verifica_esito "
			+ "where data_cancellazione is null and validita_fine is null and isee_verifica_esito_cod= :codEsito), "
			+ "now(), :iseeNote, buono_id, :utenteCrea, :utenteModifica "
			+ "from bres_t_buono_isee where isee_id= :iseeId";

	public static final String SELECT_ID_ISEE_CONFORME = "select distinct "
			+ "	btbi.isee_id from bres_t_buono_isee btbi, bres_t_buono btb , bres_t_domanda btd "
			+ " where btd.domanda_id = btb.domanda_id "
			+ "	and btd.domanda_numero = :numeroDomanda "
			+ "	and btb.buono_id = btbi.buono_id "
			+ "	and btd.data_cancellazione is null "
			+ "	and btb.data_cancellazione is null "
			+ "	and btbi.data_cancellazione is null "
			+ "	and btbi.validita_fine is null "
			+ "	and btbi.isee_conforme = true "
			+ "	and btbi.isee_verificaesito_id = null "
			+ " and btbi.isee_anno = :iseeAnno";

	public static final String UPDATE_ISEE_CONFORME = "UPDATE bres_t_buono_isee "
			+ "SET utente_modifica = :utenteModifica, data_modifica=now(), isee_valore = :iseeValore, "
			+ "isee_data_rilascio = :dataRilascio, isee_scadenza = :dataScadenza, "
			+ "isee_verificato_in_data = now(), isee_note = :iseeNote, "
			+ "isee_verifica_esito_id = (select isee_verifica_esito_id from bres_d_buono_isee_verifica_esito "
			+ "where data_cancellazione is null and validita_fine is null and isee_verifica_esito_cod = :codEsito) "
			+ "WHERE isee_id = :idIsee";

	public static final String UPDATE_CHIUSURA = "UPDATE bres_t_buono_isee "
			+ "SET validita_fine=now(), utente_modifica = :utenteModifica, data_modifica=now() "
			+ "WHERE isee_id = :idIsee";

	public static final String SELECT_LISTA_ESITI_ISEE = "select distinct "
			+ "	isee_verifica_esito_cod,isee_verifica_esito_desc from bres_d_buono_isee_verifica_esito "
			+ " where "
			+ "	data_cancellazione is null "
			+ "	and validita_fine is null order by isee_verifica_esito_desc";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public ModelIseeBuono selectIsee(String numeroDomanda, String iseeAnno) throws DatabaseException {
		String methodName = "SELECT_ISEE_CONFORME";
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda)
				.addValue("iseeAnno", iseeAnno);
		ModelIseeBuono isee = new ModelIseeBuono();

		try {
			isee = jdbcTemplate.queryForObject(SELECT_ISEE_CONFORME, namedParameters, new IseeBuonoMapper());
			return isee;
		} catch (EmptyResultDataAccessException e) {
			logError(methodName, e.getMessage());
			return null;
		} catch (Exception e) {
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public List<ModelIseeBuono> selectListaIsee(String numeroDomanda) throws DatabaseException {
		String methodName = "SELECT_LISTA_ISEE";
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda);
		List<ModelIseeBuono> isee = new ArrayList<ModelIseeBuono>();

		try {
			isee = jdbcTemplate.query(SELECT_LISTA_ISEE, namedParameters, new IseeBuonoMapper());
			return isee;
		} catch (EmptyResultDataAccessException e) {
			logError(methodName, e.getMessage());
			return null;
		} catch (Exception e) {
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public List<ModelEsitoIsee> selectListaEsitoIsee() throws DatabaseException {
		String methodName = "SELECT_LISTA_ESITI_ISEE";
		List<ModelEsitoIsee> isee = new ArrayList<ModelEsitoIsee>();
		try {
			isee = jdbcTemplate.query(SELECT_LISTA_ESITI_ISEE, new EsitiIseeMapper());
			return isee;
		} catch (EmptyResultDataAccessException e) {
			logError(methodName, e.getMessage());
			return null;
		} catch (Exception e) {
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public long insertIseeConforme(ModelIseeBuono isee, String numeroDomanda) throws DatabaseException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue("iseeAnno", isee.getIseeAnno(), Types.VARCHAR);
		params.addValue("numeroDomanda", numeroDomanda, Types.VARCHAR);
		params.addValue("utenteCrea", isee.getUtenteCreazione(), Types.VARCHAR);
		params.addValue("utenteModifica", isee.getUtenteModifica(), Types.VARCHAR);

		params.addValue("iseeValore", isee.getIseeValore(), Types.NUMERIC);
		params.addValue("dataRilascio", isee.getIseeDataRilascio(), Types.DATE);
		params.addValue("dataScadenza", isee.getIseeScadenza(), Types.DATE);
		params.addValue("iseeNote", isee.getIseeNote(), Types.VARCHAR);
		params.addValue("codEsito", isee.getIseeVerificaEsitoCod(), Types.VARCHAR);

		jdbcTemplate.update(INSERT_ISEE_CONFORME, params, keyHolder, new String[] { "isee_id" });

		long idIsee = keyHolder.getKey().longValue();
		return idIsee;
	}

	public long insertIseeConformeSelect(ModelIseeBuono isee, Long iseeId, String codFiscale, String verificaesitoCod)
			throws DatabaseException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue("iseeAnno", isee.getIseeAnno(), Types.VARCHAR);
		params.addValue("iseeValore", isee.getIseeValore(), Types.NUMERIC);
		params.addValue("dataRilascio", isee.getIseeDataRilascio(), Types.DATE);
		params.addValue("dataScadenza", isee.getIseeScadenza(), Types.DATE);
		params.addValue("iseeNote", isee.getIseeNote(), Types.VARCHAR);
		params.addValue("iseeId", iseeId, Types.BIGINT);
		params.addValue("utenteCrea", codFiscale, Types.VARCHAR);
		params.addValue("utenteModifica", codFiscale, Types.VARCHAR);
		params.addValue("codEsito", verificaesitoCod, Types.VARCHAR);
		jdbcTemplate.update(INSERT_ISEE_CONFORME_SELECT, params, keyHolder, new String[] { "isee_id" });

		long idIsee = keyHolder.getKey().longValue();
		return idIsee;
	}

	public Long selectIdIsee(String numeroDomanda, String iseeAnno) throws DatabaseException {
		String methodName = "SELECT_ID_ISEE_CONFORME";
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda)
				.addValue("iseeAnno", iseeAnno);
		Long idIsee = null;
		try {
			idIsee = jdbcTemplate.queryForObject(SELECT_ID_ISEE_CONFORME, namedParameters, Long.class);

			return idIsee;
		} catch (EmptyResultDataAccessException e) {
			logError(methodName, e.getMessage());
			return null;
		} catch (Exception e) {
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public long updateIseeConforme(ModelIseeBuono isee) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("idIsee", isee.getIseeId(), Types.BIGINT)
				.addValue("utenteModifica", isee.getUtenteModifica(), Types.VARCHAR)
				.addValue("iseeValore", isee.getIseeValore(), Types.NUMERIC)
				.addValue("dataRilascio", isee.getIseeDataRilascio(), Types.DATE)
				.addValue("dataScadenza", isee.getIseeScadenza(), Types.DATE)
				.addValue("iseeNote", isee.getIseeNote(), Types.VARCHAR)
				.addValue("codEsito", isee.getIseeVerificaEsitoCod(), Types.VARCHAR);
		return jdbcTemplate.update(UPDATE_ISEE_CONFORME, params);
	}

	public long updateChiusura(Long idIsee, String utenteModifica) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("idIsee", idIsee, Types.BIGINT).addValue("utenteModifica", utenteModifica, Types.VARCHAR);
		return jdbcTemplate.update(UPDATE_CHIUSURA, params);
	}

}
