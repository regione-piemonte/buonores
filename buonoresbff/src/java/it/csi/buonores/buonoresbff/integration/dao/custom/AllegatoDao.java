/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.integration.dao.custom;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import it.csi.buonores.buonoresbff.dto.custom.ModelGetAllegato;
import it.csi.buonores.buonoresbff.dto.custom.ModelGetAllegatoExt;
import it.csi.buonores.buonoresbff.exception.DatabaseException;
import it.csi.buonores.buonoresbff.integration.dao.utils.AllegatoExtMapper;
import it.csi.buonores.buonoresbff.integration.dao.utils.AllegatoGetMapper;
import it.csi.buonores.buonoresbff.util.LoggerUtil;

@Repository
public class AllegatoDao extends LoggerUtil {

	public static final String SELECT_DET_COD_ALLEGATO = "select count(*) from bres_t_allegato a "
			+ " where a.sportello_id = :idSportello "
			+ " and a.domanda_det_id = :idDomanda"
			+ " and a.domanda_det_cod = :detCod"
			+ " and a.allegato_tipo_id = (Select s.allegato_tipo_id from bres_d_allegato_tipo s where s.allegato_tipo_cod = :tipo)";

	public static final String UPDATE_DOMANDA_ALLEGATO = "UPDATE bres_t_allegato a "
			+ "SET data_modifica=now(),utente_modifica=:cfmodifica, file_name=:nomefile, file_type=:tipofile, file_path=:pathfile "
			+ "from bres_t_domanda_dettaglio b "
			+ "WHERE b.domanda_det_cod = a.domanda_det_cod "
			+ "and b.domanda_det_id = a.domanda_det_id "
			+ "and a.sportello_id = b.sportello_id "
			+ "and a.domanda_det_cod =:detcod "
			+ "and a.allegato_tipo_id = (Select s.allegato_tipo_id from bres_d_allegato_tipo s where s.allegato_tipo_cod = :cod)  ";

	public static final String INSERT_DOMANDA_ALLEGATO = "INSERT INTO bres_t_allegato "
			+ "(file_name, file_type, file_path, sportello_id, "
			+ "domanda_det_id, domanda_det_cod, allegato_tipo_id, "
			+ "utente_creazione, utente_modifica) "
			+ " VALUES(:file_name, :file_type, :file_path, :sportello_id, :domanda_det_id, :domanda_det_cod, "
			+ "(select allegato_tipo_id from bres_d_allegato_tipo where allegato_tipo_cod = :allegatotipocod), "
			+ ":utente_creazione, :utente_modifica)";

	public static final String SELECT_ALLEGATO = "select bta.file_path,bta.file_name  "
			+ " from bres_t_allegato bta  "
			+ " where  "
			+ " bta.domanda_det_id = :domandaID "
			+ " and bta.domanda_det_cod = :detcoD  "
			+ " and bta.data_cancellazione is null "
			+ " and bta.allegato_tipo_id = (select bdat.allegato_tipo_id  from bres_d_allegato_tipo bdat where bdat.allegato_tipo_cod = :allegatoCod) ";

	public static final String UPDATE_ALLEGATO_DOMANDA_ID = "UPDATE bres_t_allegato bta  "
			+ " set  bta.data_modifica = now(), bta.utente_modifica = :cf, bta.data_cancellazione = now()  "
			+ " where  "
			+ " bta.domanda_det_id = :domandaId ";

	public static final String SELECT_GET_ALLEGATO = "select bta.file_path,bta.file_name  "
			+ " from bres_t_allegato bta  "
			+ " where  "
			+ " bta.domanda_det_id = :domandaID "
			+ " and bta.data_cancellazione is null "
			+ " and bta.allegato_tipo_id = (select bdat.allegato_tipo_id  from bres_d_allegato_tipo bdat where bdat.allegato_tipo_cod = :allegatoCod) ";
	// MODIFICA TAG 70 buonodom
	public static final String SELECT_ALLEGATI = "select bta.file_path,bta.file_name,bta.allegato_id,bta.sportello_id,bta.allegato_tipo_id,"
			+ " bta.file_type, a.allegato_tipo_cod "
			+ " from bres_t_allegato bta, bres_d_allegato_tipo a  "
			+ " where  "
			+ " bta.domanda_det_id = :domandaID "
			+ " and a.allegato_tipo_id=bta.allegato_tipo_id "
			+ " and bta.data_cancellazione is null "
			+ " and a.data_cancellazione is null "
			+ " and a.validita_fine is null";

	public static final String INSERT_DOMANDA_ALLEGATO_ID_TIPO_ALLEGATO = "INSERT INTO bres_t_allegato "
			+ "(file_name, file_type, file_path, sportello_id, "
			+ "domanda_det_id, domanda_det_cod, allegato_tipo_id, "
			+ "utente_creazione, utente_modifica) "
			+ " VALUES(:file_name, :file_type, :file_path, :sportello_id, :domanda_det_id, :domanda_det_cod, "
			+ ":allegatoTipoId, "
			+ ":utente_creazione, :utente_modifica)";

	public static final String UPDATE_ALLEGATO_DATA_CANCELLAZIONE_BY_ID_DOMANDA = "UPDATE bres_t_allegato  "
			+ " set  data_modifica = now(), utente_modifica = :cf, data_cancellazione = now()  "
			+ " where  "
			+ " domanda_det_id = (select t.domanda_det_id "
			+ "		from bres_t_domanda_dettaglio t where t.domanda_det_cod = :detCod) "
			+ " and allegato_tipo_id = (select t.allegato_tipo_id "
			+ "from bres_d_allegato_tipo t where t.allegato_tipo_cod = :tipoAllegato) ";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public boolean selectEsisteAllegato(String tipoAllegato, BigDecimal idSportello, BigDecimal idDomanda,
			String detCod) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("tipo", tipoAllegato)
				.addValue("idDomanda", idDomanda).addValue("idSportello", idSportello).addValue("detCod", detCod);
		try {
			Integer numallegati = jdbcTemplate.queryForObject(SELECT_DET_COD_ALLEGATO, namedParameters, Integer.class);
			return numallegati.intValue() > 0;
		} catch (Exception e) {
			generateDatabaseException("SELECT_DET_COD_ALLEGATO", e);
		}

		return false;

	}

	public long updateAllegato(String detcod, String cfmodifica, String nomefile, String tipofile, String pathfile,
			String tipoAllegato) {

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("cfmodifica", cfmodifica, Types.VARCHAR).addValue("detcod", detcod, Types.VARCHAR)
				.addValue("nomefile", nomefile, Types.VARCHAR).addValue("tipofile", tipofile, Types.VARCHAR)
				.addValue("pathfile", pathfile, Types.VARCHAR).addValue("cod", tipoAllegato);
		return jdbcTemplate.update(UPDATE_DOMANDA_ALLEGATO, params);
	}

	public long insertAllegato(String nomefile, String tipofile, String pathfile, BigDecimal sportelloid,
			BigDecimal domandadetid, String domandadetcod,
			String allegatotipocod, String cfinserisci, String cfmodifica) throws DatabaseException {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue("file_name", nomefile, Types.VARCHAR);
		params.addValue("file_type", tipofile, Types.VARCHAR);
		params.addValue("file_path", pathfile, Types.VARCHAR);
		params.addValue("sportello_id", sportelloid, Types.BIGINT);
		params.addValue("domanda_det_id", domandadetid, Types.BIGINT);

		params.addValue("domanda_det_cod", domandadetcod, Types.VARCHAR);
		params.addValue("allegatotipocod", allegatotipocod, Types.VARCHAR);
		params.addValue("utente_creazione", cfinserisci, Types.VARCHAR);
		params.addValue("utente_modifica", cfmodifica, Types.VARCHAR);

		jdbcTemplate.update(INSERT_DOMANDA_ALLEGATO, params, keyHolder, new String[] { "allegato_id" });
		return keyHolder.getKey().longValue();
	}

	public ModelGetAllegato selectAllegato(String detcod, String tipoAllegato, BigDecimal idDomanda)
			throws DatabaseException {

		ModelGetAllegato allegato = new ModelGetAllegato();
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("domandaID", idDomanda)
				.addValue("detcoD", detcod).addValue("allegatoCod", tipoAllegato);
		try {
			allegato = jdbcTemplate.queryForObject(SELECT_ALLEGATO, namedParameters, new AllegatoGetMapper());

			return allegato;
		} catch (Exception e) {
			generateDatabaseException("SELECT_ALLEGATO", e);
		}

		return allegato;
	}

	public long updateDomandaAllegato(BigDecimal idDomanda, String cfmodifica) throws DatabaseException {

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("domandaId", idDomanda, Types.BIGINT).addValue("cf", cfmodifica, Types.VARCHAR);
		return jdbcTemplate.update(UPDATE_ALLEGATO_DOMANDA_ID, params);
	}

	public ModelGetAllegato selectGetAllegato(String tipoAllegato, BigDecimal idDomanda) throws DatabaseException {

		ModelGetAllegato allegato = new ModelGetAllegato();
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("domandaID", idDomanda)
				.addValue("allegatoCod", tipoAllegato);
		try {
			allegato = jdbcTemplate.queryForObject(SELECT_GET_ALLEGATO, namedParameters, new AllegatoGetMapper());
			return allegato;
		} catch (Exception e) {
			generateDatabaseException("SELECT_GET_ALLEGATO", e);
		}
		return allegato;
	}

	public long updateDomandaAllegatoDataCancellazione(String detCod, String tipoAllegato, String cfmodifica)
			throws DatabaseException {

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("cf", cfmodifica).addValue("detCod", detCod).addValue("tipoAllegato", tipoAllegato);
		return jdbcTemplate.update(UPDATE_ALLEGATO_DATA_CANCELLAZIONE_BY_ID_DOMANDA, params);
	}

	public List<ModelGetAllegatoExt> selectAllegati(BigDecimal idDomanda) throws DatabaseException {

		List<ModelGetAllegatoExt> allegati = new ArrayList<>();
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("domandaID", idDomanda);
		try {
			allegati = jdbcTemplate.query(SELECT_ALLEGATI, namedParameters, new AllegatoExtMapper());
			return allegati;
		} catch (Exception e) {
			generateDatabaseException("SELECT_ALLEGATI", e);
		}

		return Collections.emptyList();
	}

	public long insertAllegatoConAllegatoTipoId(String nomefile, String tipofile, String pathfile,
			BigDecimal sportelloid, BigDecimal domandadetid, String domandadetcod,
			BigDecimal allegatotipoId, String cfinserisci, String cfmodifica) {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue("file_name", nomefile, Types.VARCHAR);
		params.addValue("file_type", tipofile, Types.VARCHAR);
		params.addValue("file_path", pathfile, Types.VARCHAR);
		params.addValue("sportello_id", sportelloid, Types.BIGINT);
		params.addValue("domanda_det_id", domandadetid, Types.BIGINT);

		params.addValue("domanda_det_cod", domandadetcod, Types.VARCHAR);
		params.addValue("allegatoTipoId", allegatotipoId, Types.BIGINT);
		params.addValue("utente_creazione", cfinserisci, Types.VARCHAR);
		params.addValue("utente_modifica", cfmodifica, Types.VARCHAR);

		jdbcTemplate.update(INSERT_DOMANDA_ALLEGATO_ID_TIPO_ALLEGATO, params, keyHolder,
				new String[] { "allegato_id" });
		return keyHolder.getKey().longValue();
	}
}
