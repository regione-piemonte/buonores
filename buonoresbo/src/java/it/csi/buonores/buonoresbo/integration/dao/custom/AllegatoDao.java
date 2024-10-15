/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.custom;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import it.csi.buonores.buonoresbo.dto.custom.ModelGetAllegato;
import it.csi.buonores.buonoresbo.dto.custom.ModelGetAllegatoExt;
import it.csi.buonores.buonoresbo.exception.DatabaseException;
import it.csi.buonores.buonoresbo.integration.dao.utils.AllegatoExtMapper;
import it.csi.buonores.buonoresbo.integration.dao.utils.AllegatoGetMapper;
import it.csi.buonores.buonoresbo.util.LoggerUtil;

@Repository
public class AllegatoDao extends LoggerUtil {

	public static final String SELECT_GET_ALLEGATO = "select bta.file_path, bta.file_name  "
			+ " from bres_t_allegato bta  " + " where  " + " bta.domanda_det_id = :domandaID "
			+ " and bta.data_cancellazione is null "
			+ " and bta.allegato_tipo_id = (select bdat.allegato_tipo_id  from bres_d_allegato_tipo bdat where bdat.allegato_tipo_cod = :allegatoCod) ";

	public static final String INSERT_DOMANDA_ALLEGATO_ID_TIPO_ALLEGATO = "INSERT INTO bres_t_allegato "
			+ "(allegato_id, file_name, file_type, file_path, sportello_id, "
			+ "domanda_det_id, domanda_det_cod, allegato_tipo_id, data_creazione, "
			+ "data_modifica, utente_creazione, utente_modifica) "
			+ " VALUES(nextval('bres_t_allegato_allegato_id_seq'), :file_name, :file_type, :file_path, :sportello_id, :domanda_det_id, :domanda_det_cod, "
			+ ":allegatoTipoId, " + "now(), now(), :utente_creazione, :utente_modifica)";

	public static final String SELECT_ALLEGATI = "select bta.file_path,bta.file_name,bta.allegato_id,bta.sportello_id,bta.allegato_tipo_id,"
			+ " bta.file_type, a.allegato_tipo_cod " + " from bres_t_allegato bta, bres_d_allegato_tipo a  "
			+ " where  " + " bta.domanda_det_id = :domandaID " + " and a.allegato_tipo_id=bta.allegato_tipo_id "
			+ " and bta.data_cancellazione is null " + " and a.data_cancellazione is null "
			+ " and a.validita_fine is null";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public ModelGetAllegato selectGetAllegato(String tipoAllegato, BigDecimal idDomanda) throws DatabaseException {

		ModelGetAllegato allegato = new ModelGetAllegato();
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("domandaID", idDomanda)
				.addValue("allegatoCod", tipoAllegato);
		try {
			allegato = jdbcTemplate.queryForObject(SELECT_GET_ALLEGATO, namedParameters, new AllegatoGetMapper());

			return allegato;

		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_ALLEGATO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public long insertAllegatoConAllegatoTipoId(String nomefile, String tipofile, String pathfile,
			BigDecimal sportelloid, BigDecimal domandadetid, String domandadetcod, BigDecimal allegatotipoId,
			String cfinserisci, String cfmodifica) {

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

	public List<ModelGetAllegatoExt> selectAllegati(BigDecimal idDomanda) throws DatabaseException {

		List<ModelGetAllegatoExt> allegati = new ArrayList<>();
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("domandaID", idDomanda);
		try {
			allegati = jdbcTemplate.query(SELECT_ALLEGATI, namedParameters, new AllegatoExtMapper());

			return allegati;

		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		} catch (Exception e) {
			String methodName = "SELECT_ALLEGATO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

}
