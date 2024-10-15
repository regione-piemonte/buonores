/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import it.csi.buonores.buonoresbo.business.be.service.base.BaseService;
import it.csi.buonores.buonoresbo.dto.ModelSportello;
import it.csi.buonores.buonoresbo.exception.DatabaseException;

@Repository
public class OperatoreRegionaleDao extends BaseService {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public static final String INSERT_NEW_SPORTELLO = "insert into bres_t_sportello "
			+ "	(sportello_cod, sportello_desc, "
			+ "validita_inizio, validita_fine, "
			+ "sportello_anno, contributo_tipo_id, "
			+ "utente_creazione, utente_modifica) "
			+ "values(:sportelloCod, :sportelloDesc, :dataInizio::date, "
			+ ":dataFine::date, :anno, "
			+ "(select contributo_tipo_id from bres_d_contributo_tipo where contributo_tipo_cod='RES'), "
			+ ":utenteCreazione, :utenteModifica) ";

	public long creaSportello(ModelSportello nSportello, String utente) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("sportelloCod", nSportello.getCodSportello())
				.addValue("sportelloDesc", nSportello.getDescSportello())
				.addValue("dataInizio", nSportello.getDataInizio()).addValue("dataFine", nSportello.getDataFine())
				.addValue("anno", nSportello.getAnno()).addValue("utenteCreazione", utente)
				.addValue("utenteModifica", utente);

		try {
			return jdbcTemplate.update(INSERT_NEW_SPORTELLO, namedParameters);
		} catch (Exception e) {
			String methodName = "INSERT_NEW_SPORTELLO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

}