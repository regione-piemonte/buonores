/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.integration.dao.custom;

import java.sql.Types;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import it.csi.buonores.buonoresbff.dto.ModelAllegato;
import it.csi.buonores.buonoresbff.dto.custom.ModelGetAllegato;
import it.csi.buonores.buonoresbff.exception.DatabaseException;
import it.csi.buonores.buonoresbff.integration.dao.utils.AllegatoGetMapper;
import it.csi.buonores.buonoresbff.integration.dao.utils.AllegatoTipoMapper;
import it.csi.buonores.buonoresbff.util.LoggerUtil;

@Repository
public class AllegatoBuonoDao extends LoggerUtil {
	public static final String SELECT_ALLEGATO_BUONO = "select btba.file_name , btba.file_path "
			+ "from bres_t_buono_allegato btba " + "where btba.allegato_id = :allegatoId "
			+ "and btba.data_cancellazione is null;";

	public static final String SELECT_CF_RICHIEDENTE_FROM_ALLEGATO = "select btd.richiedente_cf  "
			+ "from bres_t_domanda btd, bres_t_contratto btc, bres_t_buono btb, "
			+ "	bres_t_buono_allegato btba, bres_r_contratto_allegato brca  " + "where  btba.allegato_id = :allegatoId "
			+ "and brca.allegato_id = btba.allegato_id " + "and brca.contratto_id = btc.contratto_id  "
			+ "and btc.buono_id = btb.buono_id  " + "and btb.domanda_id = btd.domanda_id "
			+ "and brca.data_cancellazione is null " + "and btba.data_cancellazione is null "
			+ "and btd.data_cancellazione is null " + "and btb.data_cancellazione is null "
			+ "and btc.data_cancellazione is null " + "group by btd.richiedente_cf ;";

	public static final String SELECT_CF_RICHIEDENTE_NUMERO_DOMANDA = "select btd.richiedente_cf  "
			+ "from bres_t_domanda btd " + "where btd.domanda_numero  = :numeroDomanda "
			+ "and btd.data_cancellazione is null;";

	public static final String SELECT_SPORTELLO_COD_FROM_NUMERO_DOMANDA = "select bts.sportello_cod  "
			+ "from bres_t_domanda btd, bres_t_sportello bts  " + "where btd.domanda_numero = :numeroDomanda "
			+ "and btd.sportello_id = bts.sportello_id " + "and btd.data_cancellazione is null "
			+ "and bts.data_cancellazione is null;";

	public static final String SELECT_BUONO_COD_FROM_NUMERO_DOMANDA = "select distinct btb.buono_cod from bres_t_buono btb, bres_r_buono_stato brbs,bres_d_buono_stato bdbs , "
			+ "bres_t_domanda_dettaglio btdd, bres_t_domanda btd "
			+ "where btb.domanda_det_id = btdd.domanda_det_id and btb.sportello_id = btdd.sportello_id "
			+ "and btb.domanda_id = btdd.domanda_id " + "and btb.data_cancellazione is null "
			+ "and btd.data_cancellazione is null " + "and btdd.domanda_id = btd.domanda_id "
			+ "and btdd.data_cancellazione is null " + "and btdd.validita_inizio <= now() "
			+ "and btdd.validita_fine is null " + "and bdbs.data_cancellazione is null "
			+ "and bdbs.validita_fine is null " + "and bdbs.validita_inizio <= now() "
			+ "and bdbs.buono_stato_id = brbs.buono_stato_id "
			+ "and brbs.buono_id = btb.buono_id " + "and brbs.validita_inizio  <= now() "
			+ "and brbs.validita_fine is null " + "and brbs.data_cancellazione is null "
			+ "and btd.domanda_numero= :numeroDomanda";

	public static final String CHECK_ESISTENZA_ALLEGATO = "select count(*) " + "from bres_t_buono_allegato btba "
			+ "where btba.file_path like '%'||REPLACE(:filePath, '\\', '\\\\')||'%' "
			+ "and btba.allegato_tipo_id  = ( " + "	select bdbat.allegato_tipo_id  "
			+ "	from bres_d_buono_allegato_tipo bdbat " + "	where bdbat.allegato_tipo_cod = :allegatoTipo "
			+ "	and bdbat.data_cancellazione is null " + ") " + "and btba.data_cancellazione is null;";

	public static final String CHECK_ESISTENZA_TIPO_ALLEGATO = "select count(bdbat.allegato_tipo_id)  "
			+ "from bres_d_buono_allegato_tipo bdbat " + "where bdbat.allegato_tipo_cod = :allegatoTipo "
			+ "and bdbat.data_cancellazione is null";

	public static final String SELECT_ALLEGATO = "select btba.file_name, btba.file_type as allegato_tipo_cod, btba.allegato_id  "
			+ "from bres_t_buono_allegato btba  "
			+ "where btba.file_path like '%'||REPLACE(:filePath, '\\', '\\\\')||'%' "
			+ "and btba.allegato_tipo_id  = ( " + "	select bdbat.allegato_tipo_id  "
			+ "	from bres_d_buono_allegato_tipo bdbat " + "	where bdbat.allegato_tipo_cod = :allegatoTipo "
			+ "	and bdbat.data_cancellazione is null " + ") " + "and btba.data_cancellazione is null;";

	public static final String UPDATE_CONTRATTO_ALLEGATO = "update bres_t_buono_allegato set  "
			+ "	file_name =:nomefile,file_type =:tipofile,file_path =:pathfile, "
			+ "	allegato_tipo_id = (select bdbat.allegato_tipo_id "
			+ "	from bres_d_buono_allegato_tipo bdbat where bdbat.allegato_tipo_cod = :allegatoTipo "
			+ "	and bdbat.data_cancellazione is null), data_modifica = now(), "
			+ "	utente_modifica = :cfmodifica  where allegato_id = :allegatoId;";

	public static final String INSERT_CONTRATTO_ALLEGATO = "insert into bres_t_buono_allegato   "
			+ "(file_name,	file_type, 	file_path, 	allegato_tipo_id, " + "	utente_creazione, utente_modifica) "
			+ "values(:file_name,:file_type, "
			+ ":file_path, (select bdbat.allegato_tipo_id from bres_d_buono_allegato_tipo bdbat "
			+ "	where bdbat.allegato_tipo_cod = :allegatoTipo and bdbat.data_cancellazione is null), "
			+ ":utente_creazione,:utente_modifica);";

	public static final String SELECT_ALLEGATO_BUONO_PERTIPO = "select btba.file_name , btba.file_path "
			+ "from bres_t_buono_allegato btba,bres_d_buono_allegato_tipo bdbat  where btba.allegato_id = :allegatoId "
			+ "and btba.data_cancellazione is null " + "and bdbat.allegato_tipo_id = btba.allegato_tipo_id "
			+ "and bdbat.allegato_tipo_cod = :allegatoTipoCod";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public ModelGetAllegato selectAllegatoBuono(Integer idAllegato) throws DatabaseException {

		ModelGetAllegato allegato = new ModelGetAllegato();
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("allegatoId", idAllegato);
		try {
			allegato = jdbcTemplate.queryForObject(SELECT_ALLEGATO_BUONO, namedParameters, new AllegatoGetMapper());
			return allegato;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_ALLEGATO_CONTRATTO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public String selectCfRichiedenteFromAllegato(Integer idAllegato) throws DatabaseException {
		String cf = "";
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("allegatoId", idAllegato);
		try {
			cf = jdbcTemplate.queryForObject(SELECT_CF_RICHIEDENTE_FROM_ALLEGATO, namedParameters,
					(rs, rowNum) -> rs.getString("richiedente_cf"));

			return cf;

		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_CF_RICHIEDENTE_FROM_CONTRATTO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public String selectCfRichiedenteNumeroDomanda(String numeroDomanda) throws DatabaseException {
		String cf = "";
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda);
		try {
			cf = jdbcTemplate.queryForObject(SELECT_CF_RICHIEDENTE_NUMERO_DOMANDA, namedParameters,
					(rs, rowNum) -> rs.getString("richiedente_cf"));

			return cf;

		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_CF_RICHIEDENTE_FROM_CONTRATTO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public String selectSportelloCodFromNumeroDomanda(String numeroDomanda) throws DatabaseException {
		String sportelloCod = "";
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda);
		try {
			sportelloCod = jdbcTemplate.queryForObject(SELECT_SPORTELLO_COD_FROM_NUMERO_DOMANDA, namedParameters,
					(rs, rowNum) -> rs.getString("sportello_cod"));

			return sportelloCod;

		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_CF_RICHIEDENTE_FROM_CONTRATTO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public String selectBuonoCodFromNumeroDomanda(String numeroDomanda) throws DatabaseException {
		String buonoCod = "";
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda);
		try {
			buonoCod = jdbcTemplate.queryForObject(SELECT_BUONO_COD_FROM_NUMERO_DOMANDA, namedParameters,
					(rs, rowNum) -> rs.getString("buono_cod"));

			return buonoCod;

		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_CF_RICHIEDENTE_FROM_CONTRATTO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public boolean selectEsisteAllegato(String filePath, String allegatoTipo) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("filePath", filePath)
				.addValue("allegatoTipo", allegatoTipo);
		try {
			Integer numallegati = jdbcTemplate.queryForObject(CHECK_ESISTENZA_ALLEGATO, namedParameters, Integer.class);
			return numallegati.intValue() > 0;

		} catch (EmptyResultDataAccessException e) {
			return false;
		} catch (Exception e) {
			String methodName = "SELECT_DET_COD_ALLEGATO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public boolean checkEsistenzaTipoAllegato(String allegatoTipo) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("allegatoTipo", allegatoTipo);
		try {
			Integer numallegati = jdbcTemplate.queryForObject(CHECK_ESISTENZA_TIPO_ALLEGATO, namedParameters,
					Integer.class);
			return numallegati.intValue() > 0;

		} catch (EmptyResultDataAccessException e) {
			return false;
		} catch (Exception e) {
			String methodName = "SELECT_DET_COD_ALLEGATO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public ModelAllegato selectAllegatoByPathFileType(String filePath, String allegatoTipo) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("filePath", filePath)
				.addValue("allegatoTipo", allegatoTipo);
		try {
			ModelAllegato allegatoResponse = jdbcTemplate.queryForObject(SELECT_ALLEGATO, namedParameters,
					new AllegatoTipoMapper());
			return allegatoResponse;

		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_DET_COD_ALLEGATO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public long updateAllegato(String nomefile, String tipofile, String pathfile, String tipoAllegato,
			String cfmodifica, Long allegatoId) {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("nomefile", nomefile, Types.VARCHAR).addValue("tipofile", tipofile, Types.VARCHAR)
				.addValue("pathfile", pathfile, Types.VARCHAR).addValue("allegatoTipo", tipoAllegato, Types.VARCHAR)
				.addValue("cfmodifica", cfmodifica, Types.VARCHAR).addValue("allegatoId", allegatoId, Types.BIGINT);
		jdbcTemplate.update(UPDATE_CONTRATTO_ALLEGATO, params, keyHolder, new String[] { "allegato_id" });
		return keyHolder.getKey().longValue();
	}

	public long insertAllegato(String nomefile, String tipofile, String pathfile, String tipoAllegato,
			String cfinserisci, String cfmodifica) throws DatabaseException {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue("file_name", nomefile, Types.VARCHAR);
		params.addValue("file_type", tipofile, Types.VARCHAR);
		params.addValue("file_path", pathfile, Types.VARCHAR);
		params.addValue("allegatoTipo", tipoAllegato, Types.VARCHAR);
		params.addValue("utente_creazione", cfinserisci, Types.VARCHAR);
		params.addValue("utente_modifica", cfmodifica, Types.VARCHAR);

		jdbcTemplate.update(INSERT_CONTRATTO_ALLEGATO, params, keyHolder, new String[] { "allegato_id" });
		return keyHolder.getKey().longValue();
	}

	public ModelGetAllegato selectAllegatoBuonoPerTipo(Integer idAllegato, String allegatoTipoCod)
			throws DatabaseException {

		ModelGetAllegato allegato = new ModelGetAllegato();
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("allegatoId", idAllegato)
				.addValue("allegatoTipoCod", allegatoTipoCod);
		try {
			allegato = jdbcTemplate.queryForObject(SELECT_ALLEGATO_BUONO_PERTIPO, namedParameters,
					new AllegatoGetMapper());

			return allegato;

		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_ALLEGATO_BUONO_PERTIPO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}
}
