/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.integration.dao.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import it.csi.buonores.buonoressrv.exception.DatabaseException;
import it.csi.buonores.buonoressrv.util.LoggerUtil;

@Repository
public class IseeDao extends LoggerUtil {

	public static final String SELECT_NOTA_ISEE = "select distinct btbi.isee_note "
			+ "from bres_t_buono_isee btbi, bres_t_buono btb, bres_t_domanda btd "
			+ "where btbi.buono_id = btb.buono_id "
			+ "and btb.domanda_id = btd.domanda_id "
			+ "and btd.domanda_numero = :numeroDomanda "
			+ "and btbi.data_cancellazione is null "
			+ "and btbi.validita_fine is null "
			+ "and btb.data_cancellazione is null "
			+ "and btd.data_cancellazione is null "
			+ "and btbi.validita_fine is null "
			+ "and btbi.isee_anno = :iseeAnno "
			+ "and btbi.isee_note is not null";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public String selectNotaIsee(String numeroDomanda, String iseeAnno) throws DatabaseException {
		String notaIsee = null;

		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("numeroDomanda", numeroDomanda)
				.addValue("iseeAnno", iseeAnno);
		try {
			notaIsee = jdbcTemplate.queryForObject(SELECT_NOTA_ISEE, namedParameters, String.class);
			return notaIsee;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_NOTA_ISEE";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}
}
