/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.custom;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import it.csi.buonores.buonoresbo.dto.ModelAllegatoBuono;
import it.csi.buonores.buonoresbo.dto.ModelContratto;
import it.csi.buonores.buonoresbo.dto.ModelContrattoAllegati;
import it.csi.buonores.buonoresbo.dto.ModelDecorrenzaBuono;
import it.csi.buonores.buonoresbo.exception.DatabaseException;
import it.csi.buonores.buonoresbo.integration.dao.utils.AllegatoBuonoMapper;
import it.csi.buonores.buonoresbo.integration.dao.utils.ContrattoMapper;
import it.csi.buonores.buonoresbo.integration.dao.utils.DecorrenzaBuonoMapper;
import it.csi.buonores.buonoresbo.util.LoggerUtil;

@Repository
public class BuonoDettaglioDao extends LoggerUtil {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public static final String SELECT_CONTRATTI_BY_BUONO_COD = "select  "
			+ "			 btc.contratto_id,  "
			+ "			 btc.contratto_data_inizio,  "
			+ "			 btc.contratto_data_fine,  "
			+ "			 btc.data_invio_a_bandi, "
			+ "			 btbf.rsa_cod,  "
			+ "			 btbf.rsa_cod_arpe,  "
			+ "			 btbf.rsa_denominazione,  "
			+ "			 btbf.rsa_indirizzo,  "
			+ "			 btbf.rsa_comune,  "
			+ "			 btbf.rsa_provincia,  "
			+ "			 btbf.rsa_sede_legale_denominazione,  "
			+ "			 btbf.rsa_sede_legale_indirizzo,  "
			+ "			 btbf.rsa_sede_legale_comune,  "
			+ "			 btbf.rsa_sede_legale_provincia,  "
			+ "			 bdft.fornitore_tipo_cod,  "
			+ "			 btc.fornitore_piva,  "
			+ "			 bdct.contratto_tipo_cod   "
			+ "from  "
			+ "			 bres_d_contratto_tipo bdct,    "
			+ "			 bres_d_fornitore_tipo bdft,  "
			+ "			 bres_t_buono_fornitore btbf,  "
			+ "			 bres_t_buono btb, "
			+ "			 bres_t_contratto btc "
			+ "where  "
			+ "			 btc.contratto_tipo_id = bdct.contratto_tipo_id "
			+ "	and btc.fornitore_tipo_id = bdft.fornitore_tipo_id "
			+ "	and btbf.fornitore_id = btc.fornitore_id "
			+ "	and btbf.buono_id = btb.buono_id "
			+ "	and btc.buono_id = btb.buono_id "
			+ "	and btc.data_cancellazione is null "
			+ "	and bdct.data_cancellazione is null "
			+ "	and bdft.data_cancellazione is null "
			+ "	and btbf.data_cancellazione is null "
			+ "	and btb.data_cancellazione is null "
			+ "	and btc.data_creazione between coalesce(bdct.validita_inizio, "
			+ "	btc.data_creazione) and coalesce(bdct.validita_fine , "
			+ "	btc.data_creazione) "
			+ "	and btc.data_creazione between coalesce(bdft.validita_inizio, "
			+ "	btc.data_creazione) and coalesce(bdft.validita_fine , "
			+ "	btc.data_creazione) "
			+ "	and btb.buono_cod =:buono_cod;";

	public static final String SELECT_ALLEGATI_CONTRATTO = "select "
			+ "	btba.allegato_id , "
			+ "	btba.file_name, "
			+ "	bdbat.allegato_tipo_cod "
			+ "from "
			+ "	bres_t_contratto btc, "
			+ "	bres_r_contratto_allegato brca, "
			+ "	bres_t_buono_allegato btba, "
			+ "	bres_d_buono_allegato_tipo bdbat "
			+ "where "
			+ "	btba.allegato_id  = brca.allegato_id   "
			+ "	and brca.contratto_id = btc.contratto_id "
			+ "	and btba.allegato_tipo_id = bdbat.allegato_tipo_id "
			+ "	and btba.data_cancellazione is null "
			+ "	and brca.data_cancellazione is null "
			+ "	and btba.data_cancellazione is null "
			+ " and btba.data_creazione BETWEEN COALESCE(bdbat.validita_inizio, btba.data_creazione) AND COALESCE(bdbat.validita_fine, btba.data_creazione) "
			+ "	and btc.contratto_id = :contratto_id;";

	public static final String SELECT_DECORRENZA_BUONO = "select  "
			+ "	btb.decorrenza_inizio , "
			+ "	btb.decorrenza_fine  "
			+ "from bres_t_buono btb "
			+ "where  "
			+ "btb.data_cancellazione is null and "
			+ "btb.buono_cod = :buono_cod;";

	public static final String UPDATE_DECORRENZA_BUONO = "UPDATE bres_t_buono "
			+ "SET decorrenza_inizio = :decorrenza_inizio, "
			+ "    decorrenza_fine = :decorrenza_fine, "
			+ "    utente_modifica = :utente_modifica, "
			+ "    data_modifica = now() "
			+ "WHERE data_cancellazione is null "
			+ "and buono_cod = :buono_cod;";

	public static final String SELECT_MESI_BANDO = " select distinct btgf.finanziamento_mesi "
			+ "	 from bres_t_graduatoria_dettaglio btgd, "
			+ "	 bres_t_graduatoria_finanziamento btgf,  "
			+ "	 bres_t_buono btb "
			+ "	 where btb.buono_cod  = :buono_cod  "
			+ "	 and btb.domanda_id = btgd.domanda_id   "
			+ "	 and btgf.graduatoria_id = btgd.graduatoria_id  "
			+ "	 and btgd.finanziamento_id = btgf.finanziamento_id  "
			+ "	 and btb.data_cancellazione is null  "
			+ "	 and btgd.data_cancellazione is null  "
			+ "	 and btgf.data_cancellazione is null  "
			+ "	 and btgd.importo!=0;";

	public static final String DATA_IN_PAGAMENTO = "select distinct "
			+ "	to_char((btdd.validita_inizio + interval '1 months'), "
			+ "	'yyyy-mm-01') "
			+ "from "
			+ "	bres_t_domanda btd, "
			+ "	bres_t_domanda_dettaglio btdd, "
			+ "	bres_d_domanda_stato bdds  "
			+ "where "
			+ "	btd.domanda_numero = :numeroDomanda "
			+ "	and btd.data_cancellazione is null "
			+ "	and btdd.data_cancellazione is null "
			+ "	and bdds.data_cancellazione is null "
			+ "	and bdds.domanda_stato_cod = 'IN_PAGAMENTO' "
			+ "	and bdds.domanda_stato_id = btdd.domanda_stato_id  "
			+ "	and btdd.domanda_id  = btd.domanda_id ;";

	public List<ModelContrattoAllegati> selectContrattiByBuonoCod(String buono_cod) throws DatabaseException {
		List<ModelContratto> contratti = new ArrayList<ModelContratto>();
		List<ModelContrattoAllegati> contrattiAllegati = new ArrayList<ModelContrattoAllegati>();
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("buono_cod", buono_cod);

		try {
			contratti = jdbcTemplate.query(SELECT_CONTRATTI_BY_BUONO_COD, namedParameters, new ContrattoMapper());
			for (int i = 0; i < contratti.size(); i++) {
				ModelContrattoAllegati contrattoAllegati = new ModelContrattoAllegati();
				contrattoAllegati.setContratto(contratti.get(i));
				contrattoAllegati.setAllegati(selectAllegatiContratto(contratti.get(i).getId()));
				contrattiAllegati.add(contrattoAllegati);
			}
			return contrattiAllegati;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "selectContrattiByBuonoCod";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	private List<ModelAllegatoBuono> selectAllegatiContratto(Integer idContratto) throws DatabaseException {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("contratto_id", idContratto);
		List<ModelAllegatoBuono> allegati = new ArrayList<ModelAllegatoBuono>();
		try {
			allegati = jdbcTemplate.query(SELECT_ALLEGATI_CONTRATTO, namedParameters, new AllegatoBuonoMapper());
			return allegati;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "selectAllegatiContratto";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public ModelDecorrenzaBuono selectDecorrenzaBuono(String buono_cod) throws DatabaseException {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("buono_cod", buono_cod);
		ModelDecorrenzaBuono decorrenzaBuono = new ModelDecorrenzaBuono();
		try {
			decorrenzaBuono = jdbcTemplate.queryForObject(SELECT_DECORRENZA_BUONO, namedParameters,
					new DecorrenzaBuonoMapper());
			return decorrenzaBuono;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "selectDecorrenzaBuono";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public int updateDecorrenzaBuono(String buono_cod, ModelDecorrenzaBuono decorrenza, String utente_modifica)
			throws DatabaseException {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("buono_cod", buono_cod)
				.addValue("decorrenza_inizio", decorrenza.getDecorrenzaInizio())
				.addValue("decorrenza_fine", decorrenza.getDecorrenzaFine())
				.addValue("utente_modifica", utente_modifica);
		try {
			return jdbcTemplate.update(UPDATE_DECORRENZA_BUONO, namedParameters);

		} catch (EmptyResultDataAccessException e) {
			return 0;
		} catch (Exception e) {
			String methodName = "updateDecorrenzaBuono";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public int selectMesiBando(String buono_cod) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("buono_cod", buono_cod);
		try {
			return jdbcTemplate.queryForObject(SELECT_MESI_BANDO, namedParameters, Integer.class);
		} catch (EmptyResultDataAccessException e) {
			return 0;
		} catch (Exception e) {
			String methodName = "selectMesiBando";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public String selectDataInPagamento(String numeroRichiesta) throws DatabaseException {
		String dettaglio = null;

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroRichiesta);
		try {
			dettaglio = jdbcTemplate.queryForObject(DATA_IN_PAGAMENTO, namedParameters, String.class);
			return dettaglio;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "selectDataInPagamento";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

}