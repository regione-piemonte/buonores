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

import it.csi.buonores.buonoresbo.dto.ModelAzione;
import it.csi.buonores.buonoresbo.dto.ModelEnteGestore;
import it.csi.buonores.buonoresbo.dto.ModelProfili;
import it.csi.buonores.buonoresbo.dto.ModelUserRuolo;
import it.csi.buonores.buonoresbo.exception.DatabaseException;
import it.csi.buonores.buonoresbo.integration.dao.utils.AzioniMapper;
import it.csi.buonores.buonoresbo.integration.dao.utils.EnteGestoreMapper;
import it.csi.buonores.buonoresbo.integration.dao.utils.ProfiliMapper;
import it.csi.buonores.buonoresbo.integration.dao.utils.UserRuoloMapper;
import it.csi.buonores.buonoresbo.util.LoggerUtil;

@Repository
public class LoginDao extends LoggerUtil {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	private static final String SELECT_RUOLI_BY_CF = "select bdr.ruolo_cod, bdr.ruolo_desc,bts.soggetto_cf, bts.soggetto_nome, bts.soggetto_cognome "
			+ "from bres_d_ruolo bdr, bres_r_soggetto_ruolo brsr, bres_t_soggetto bts  "
			+ "where bdr.ruolo_id = brsr.ruolo_id  "
			+ "and brsr.soggetto_id = bts.soggetto_id  "
			+ "and bts.soggetto_cf = :codFiscale "
			+ "and bts.data_cancellazione is null  "
			+ "and brsr.data_cancellazione is null  "
			+ "and now()::date between brsr.validita_inizio::date and coalesce(brsr.validita_fine::date, now()::date) "
			+ "and bdr.data_cancellazione is null  "
			+ "and now()::date between bdr.validita_inizio::date and coalesce(bdr.validita_fine::date, now()::date) "
			+ "order by bdr.ruolo_cod";

	private static final String SELECT_ENTI_GESTORE_BY_CF = "select bteg.ente_gestore_denominazione, bteg.ente_gestore_id "
			+ "from bres_t_soggetto bts ,bres_r_soggetto_ente_gestore brseg , bres_t_ente_gestore bteg "
			+ "where bts.data_cancellazione is null "
			+ "and brseg.data_cancellazione is null "
			+ "and now()::date between brseg.validita_inizio::date and coalesce(brseg.validita_fine::date, now()::date) "
			+ "and bteg.data_cancellazione is null "
			+ "and now()::date between bteg.validita_inizio::date and coalesce(bteg.validita_fine::date, now()::date) "
			+ "and bts.soggetto_id = brseg.soggetto_id "
			+ "and brseg.ente_gestore_id = bteg.ente_gestore_id "
			+ "and bts.soggetto_cf  = :codFiscale "
			+ "order by bteg.ente_gestore_denominazione ";

	private static final String SELECT_PROFILI_BY_COD_RUOLO = "select "
			+ "	bdp.profilo_cod, "
			+ "	bdp.profilo_desc "
			+ "from "
			+ "	bres_d_profilo bdp, "
			+ "	bres_d_ruolo bdr, "
			+ "	bres_r_ruolo_profilo brrp "
			+ "where "
			+ "	bdr.ruolo_cod = :codRuolo "
			+ "	and  "
			+ "bdr.ruolo_id = brrp.ruolo_id "
			+ "	and  "
			+ "brrp.profilo_id = bdp.profilo_id "
			+ "	and  "
			+ "bdr.data_cancellazione is null "
			+ "	and "
			+ "bdp.data_cancellazione is null "
			+ "	and  "
			+ "now()::date between bdp.validita_inizio::date and coalesce(bdp.validita_fine::date, now()::date) "
			+ "	and  "
			+ "brrp.data_cancellazione is null "
			+ "	and  "
			+ "now()::date between brrp.validita_inizio::date and coalesce(brrp.validita_fine::date, now()::date) "
			+ "order by "
			+ "	bdp.profilo_cod";

	private static final String SELECT_AZIONI_BY_COD_PROFILO = "select "
			+ "	bta.azione_cod, "
			+ "	bta.azione_desc "
			+ "from "
			+ "	bres_t_azione bta, "
			+ "	bres_d_profilo bdp, "
			+ "	bres_r_profilo_azione brpa "
			+ "where "
			+ "	bdp.profilo_cod = :codProfilo "
			+ "	and bta.azione_id = brpa.azione_id "
			+ "	and bdp.profilo_id = brpa.profilo_id "
			+ " and bdp.data_cancellazione is null "
			+ " and bta.data_cancellazione is null "
			+ "	and  "
			+ " now()::date between bdp.validita_inizio::date and coalesce(bdp.validita_fine::date, now()::date) "
			+ " and brpa.data_cancellazione is null "
			+ "	and  "
			+ " now()::date between brpa.validita_inizio::date and coalesce(brpa.validita_fine::date, now()::date) "
			+ " order by "
			+ "	bta.azione_cod";

	public List<ModelUserRuolo> selectRuoli(String codFiscale) throws DatabaseException {
		List<ModelUserRuolo> ruoli = new ArrayList<ModelUserRuolo>();

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codFiscale", codFiscale);
		try {
			ruoli = jdbcTemplate.query(SELECT_RUOLI_BY_CF, namedParameters, new UserRuoloMapper());
			return ruoli;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_RUOLI_BY_CF";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public List<ModelEnteGestore> selectEntiGestore(String codFiscale) throws DatabaseException {
		List<ModelEnteGestore> enti = new ArrayList<ModelEnteGestore>();

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codFiscale", codFiscale);
		try {
			enti = jdbcTemplate.query(SELECT_ENTI_GESTORE_BY_CF, namedParameters, new EnteGestoreMapper() {
			});
			return enti;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_ENTI_GESTORE_BY_CF";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public List<ModelProfili> selectProfili(String codRuolo) throws DatabaseException {
		List<ModelProfili> profili = new ArrayList<ModelProfili>();

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codRuolo", codRuolo);
		try {
			profili = jdbcTemplate.query(SELECT_PROFILI_BY_COD_RUOLO, namedParameters, new ProfiliMapper());
			return profili;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_PROFILI_BY_COD_RUOLO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public List<ModelAzione> selectAzioni(String codProfilo) throws DatabaseException {
		List<ModelAzione> azioni = new ArrayList<ModelAzione>();

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codProfilo", codProfilo);
		try {
			azioni = jdbcTemplate.query(SELECT_AZIONI_BY_COD_PROFILO, namedParameters, new AzioniMapper());
			return azioni;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_AZIONI_BY_COD_PROFILO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}
}
