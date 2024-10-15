/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresrend.integration.dao.custom;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import it.csi.buonores.buonoresrend.dto.ModelEnteGestore;
import it.csi.buonores.buonoresrend.exception.DatabaseException;
import it.csi.buonores.buonoresrend.integration.dao.utils.ListaEntiGestoriMapper;
import it.csi.buonores.buonoresrend.util.LoggerUtil;

@Repository
public class EntiGestoriDao extends LoggerUtil {

	//Prima versione query per estrazione enti gestori per servizio gestrutture
	public static final String SELECT_ENTI_GESTORI = "select "
			+ "bteg.ente_gestore_codice_fiscale, "
			+ "bteg.ente_gestore_denominazione, "
			+ "bteg.ente_gestore_indirizzo, "
			+ "bteg.ente_gestore_tipo, "
			+ "bteg.ente_gestore_codice_regionale, "
			+ "STRING_AGG(distinct bdc.comune_istat_cod, ',') as comuni_istat, "
			+ "STRING_AGG(distinct bts.soggetto_cf, ',') as soggetti "
			+ "from bres_t_ente_gestore bteg, bres_r_ente_gestore_comune bregc, bres_d_comune bdc, "
			+ "bres_r_soggetto_ente_gestore brseg, bres_t_soggetto bts "
			+ "where bteg.ente_gestore_id = bregc.ente_gestore_id "
			+ "and bregc.comune_id = bdc.comune_id "
			+ "and brseg.ente_gestore_id = bteg.ente_gestore_id "
			+ "and brseg.soggetto_id = bts.soggetto_id "
			+ "and bteg.data_cancellazione is null "
			+ "and bteg.validita_fine is null "
			+ "and bregc.data_cancellazione is null "
			+ "and bdc.data_cancellazione is null "
			+ "and brseg.data_cancellazione is null "
			+ "and brseg.validita_fine is null "
			+ "and bts.data_cancellazione is null "
			+ "group by ente_gestore_codice_fiscale, ente_gestore_denominazione, ente_gestore_indirizzo, ente_gestore_tipo, ente_gestore_codice_regionale "
			+ "order by ente_gestore_codice_fiscale";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public List<ModelEnteGestore> getEntiGestori() throws DatabaseException {
		List<ModelEnteGestore> entiGestori = null;
		
		try {
			entiGestori = jdbcTemplate.query(SELECT_ENTI_GESTORI, new ListaEntiGestoriMapper());
			return entiGestori;
		} catch (Exception e) {
			generateDatabaseException("SELECT_ENTI_GESTORI", e);
		}
		
		return entiGestori;
	}
}
