/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.integration.dao.custom;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import it.csi.buonores.buonoresbandisrv.dto.DatiBuono;
import it.csi.buonores.buonoresbandisrv.dto.ModelAllegatoContratto;
import it.csi.buonores.buonoresbandisrv.dto.ModelDocumentoAllegato;
import it.csi.buonores.buonoresbandisrv.dto.ModelSetFornitore;
import it.csi.buonores.buonoresbandisrv.exception.DatabaseException;
import it.csi.buonores.buonoresbandisrv.integration.dao.utils.DatiBuonoMapper;
import it.csi.buonores.buonoresbandisrv.integration.dao.utils.DocumentoPathAllegatoMapper;
import it.csi.buonores.buonoresbandisrv.integration.dao.utils.SetFornitoreMapper;
import it.csi.buonores.buonoresbandisrv.util.LoggerUtil;

@Repository
public class BuoniDao extends LoggerUtil {

	public static final String SELECT_BUONO = "select distinct btb.buono_id, btb.decorrenza_inizio "
			+ "from bres_t_buono btb, bres_r_buono_stato brbs, bres_d_buono_stato bdbs "
			+ "where btb.domanda_id = :domandaId and btb.sportello_id = :sportelloId "
			+ "and btb.data_cancellazione is null "
			+ "and bdbs.data_cancellazione is null "
			+ "and bdbs.validita_fine is null "
			+ "and bdbs.validita_inizio <= now() "
			+ "and bdbs.buono_stato_id = brbs.buono_stato_id "
			+ "and bdbs.buono_stato_cod in ( %s ) "
			+ "and brbs.buono_id = btb.buono_id "
			+ "and brbs.validita_inizio  <= now() "
			+ "and brbs.validita_fine is null "
			+ "and brbs.data_cancellazione is null";

	public static final String SELECT_BUONO_COD_FROM_NUMERO_DOMANDA = "select distinct btb.buono_cod from bres_t_buono btb, bres_r_buono_stato brbs, bres_d_buono_stato bdbs , "
			+ "bres_t_domanda_dettaglio btdd, bres_t_domanda btd "
			+ "where btb.domanda_det_id = btdd.domanda_det_id and btb.sportello_id = btdd.sportello_id "
			+ "and btb.domanda_id = btdd.domanda_id " + "and btb.data_cancellazione is null "
			+ "and btd.data_cancellazione is null " + "and btdd.domanda_id = btd.domanda_id "
			+ "and btdd.data_cancellazione is null " + "and btdd.validita_inizio <= now() "
			+ "and btdd.validita_fine is null " + "and bdbs.data_cancellazione is null "
			+ "and bdbs.validita_fine is null " + "and bdbs.validita_inizio <= now() "
			+ "and bdbs.buono_stato_id = brbs.buono_stato_id "
			+ "and bdbs.buono_stato_cod not in ('REVOCATO','REVOCATO_PRESA_VISIONE') "
			+ "and brbs.buono_id = btb.buono_id " + "and brbs.validita_inizio  <= now() "
			+ "and brbs.validita_fine is null " + "and brbs.data_cancellazione is null "
			+ "and btd.domanda_numero= :numeroDomanda";

	public static final String SELECT_CONTRATTO = "select distinct b.contratto_id "
			+ "from bres_t_buono_fornitore a, bres_t_contratto b "
			+ "where a.fornitore_id = :fornitoreId "
			+ "and b.fornitore_id = a.fornitore_id";

	public static final String SELECT_SETFORNITORE = "select distinct rsa_cfpiva, rsa_denominazione, "
			+ "to_char(b.contratto_data_inizio,'yyyy-mm-dd') contratto_data_inizio,to_char(b.contratto_data_fine,'yyyy-mm-dd') contratto_data_fine "
			+ "from bres_t_buono_fornitore a, bres_t_contratto b, bres_d_contratto_tipo bdct "
			+ "where b.fornitore_id = :fornitoreId "
			+ "and b.fornitore_id = a.fornitore_id "
			+ "and b.contratto_id = :contrattoId "
			+ "and b.contratto_tipo_id = bdct.contratto_tipo_id "
			+ "and bdct.contratto_tipo_cod = 'CONTRATTO_RSA'";

	public static final String SELECT_ALLEGATI_CONTRATTO_ID = "select distinct "
			+ "d.file_name, e.allegato_tipo_cod, d.file_path, d.allegato_id "
			+ "from bres_t_buono_fornitore a, bres_t_contratto b, bres_r_contratto_allegato c, bres_t_buono_allegato d, "
			+ "bres_d_buono_allegato_tipo e "
			+ "where a.fornitore_id = :fornitoreId "
			+ "and b.fornitore_id = a.fornitore_id "
			+ "and c.contratto_id = b.contratto_id "
			+ "and b.contratto_id = :contrattoId "
			+ "and d.allegato_id = c.allegato_id "
			+ "and e.allegato_tipo_id = d.allegato_tipo_id";

	public static final String GET_FORNITORI = "select distinct btbf.fornitore_id "
			+ "from bres_t_domanda btd, bres_t_domanda_dettaglio btdd, bres_d_domanda_stato bdds, "
			+ "bres_t_buono btb,bres_t_buono_fornitore btbf "
			+ "where btd.domanda_id = btdd.domanda_id "
			+ "and btbf.buono_id = btb.buono_id "
			+ "and btd.domanda_numero = :numeroDomanda "
			+ "and bdds.domanda_stato_cod ='IN_PAGAMENTO' "
			+ "and btb.domanda_det_id = btdd.domanda_det_id "
			+ "and btb.sportello_id = btdd.sportello_id "
			+ "and btb.domanda_id = btd.domanda_id "
			+ "and btdd.validita_fine is null "
			+ "and btd.data_cancellazione is null "
			+ "and btdd.data_cancellazione is null "
			+ "and bdds.data_cancellazione is null "
			+ "and btb.data_cancellazione is null ";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public DatiBuono selectBuono(BigDecimal sportelloId, BigDecimal domandaId, String stato) throws DatabaseException {
		String methodName = "SELECT_BUONO";
		String querySelectBuono = String.format(SELECT_BUONO, stato);
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("sportelloId", sportelloId)
				.addValue("domandaId", domandaId);
		try {
			return jdbcTemplate.queryForObject(querySelectBuono, namedParameters, new DatiBuonoMapper());

		} catch (EmptyResultDataAccessException e) {
			logError(methodName, e.getMessage());
			return null;
		} catch (Exception e) {

			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public String selectBuonoCodFromNumeroDomanda(String numeroDomanda) throws DatabaseException {
		String methodName = "SELECT_CF_RICHIEDENTE_FROM_CONTRATTO";
		String buonoCod = "";
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda);
		try {
			buonoCod = jdbcTemplate.queryForObject(SELECT_BUONO_COD_FROM_NUMERO_DOMANDA, namedParameters,
					(rs, rowNum) -> rs.getString("buono_cod"));

			return buonoCod;

		} catch (EmptyResultDataAccessException e) {
			logError(methodName, e.getMessage());
			return null;
		} catch (Exception e) {
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public List<Integer> selectContratti(int fornitoreId) throws DatabaseException {
		String methodName = "SELECT_CONTRATTO";
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("fornitoreId", fornitoreId);
		try {
			List<Integer> contratti = jdbcTemplate.queryForList(SELECT_CONTRATTO, namedParameters, Integer.class);
			return contratti;
		} catch (EmptyResultDataAccessException e) {
			logError(methodName, e.getMessage());
			return null;
		} catch (Exception e) {
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public ModelSetFornitore selectSetFornitore(int contrattoId, int fornitoreId) throws DatabaseException {
		String methodName = "SELECT_SETFORNITORE e SELECT_ALLEGATI_CONTRATTO";
		ModelSetFornitore fornitore = new ModelSetFornitore();
		List<ModelDocumentoAllegato> documento = new ArrayList<ModelDocumentoAllegato>();
		List<ModelAllegatoContratto> allegati = new ArrayList<ModelAllegatoContratto>();
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("contrattoId", contrattoId)
				.addValue("fornitoreId", fornitoreId);
		try {
			fornitore = jdbcTemplate.queryForObject(SELECT_SETFORNITORE, namedParameters, new SetFornitoreMapper());
			documento = jdbcTemplate.query(SELECT_ALLEGATI_CONTRATTO_ID, namedParameters,
					new DocumentoPathAllegatoMapper());
			for (ModelDocumentoAllegato doc : documento) {
				ModelAllegatoContratto allegato = new ModelAllegatoContratto();
				allegato.setPath(doc.getPath() + File.separator + doc.getNomefile());
				allegato.setNomeFile(doc.getAllegatoId() + "_" + doc.getNomefile());
				allegati.add(allegato);
			}
			fornitore.setFiles(allegati);
			return fornitore;
		} catch (EmptyResultDataAccessException e) {
			logError(methodName, e.getMessage());
			return null;
		} catch (Exception e) {
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public List<Integer> selectFornitori(String numeroDomanda) throws DatabaseException {
		String methodName = "GET_DOCUMENTO_SPESA_FORNITORI";

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda);
		try {
			List<Integer> fornitori = jdbcTemplate.queryForList(GET_FORNITORI, namedParameters, Integer.class);
			return fornitori;
		} catch (EmptyResultDataAccessException e) {
			logError(methodName, e.getMessage());
			return null;
		} catch (Exception e) {
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

}
