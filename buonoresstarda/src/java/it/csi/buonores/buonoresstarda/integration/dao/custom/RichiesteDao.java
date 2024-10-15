/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresstarda.integration.dao.custom;

import java.sql.Timestamp;
import java.sql.Types;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import it.csi.buonores.buonoresstarda.exception.DatabaseException;
import it.csi.buonores.buonoresstarda.util.LoggerUtil;

@Repository
public class RichiesteDao extends LoggerUtil {

	public static final String UPDATE_DETTAGLIO_DOMANDA_PROTOCOLLO = "update bres_t_domanda_dettaglio "
			+ "set data_modifica=now(), utente_modifica='STARDAS_CALLBACK', protocollo_cod= :protocollocod,data_protocollo=:dataprotocollo,tipo_protocollo=:tipoprotocollo "
            + "where domanda_det_cod || '_' || :tipoDocumento = :idDocumentoFruitore";

	public static final String SELECT_DOCUMENTO_FRUITORE = "select count(*) "
			+ " from bres_t_domanda_dettaglio btd,bres_t_allegato bta,bres_d_allegato_tipo bdat "
			+ " where  bta.domanda_det_id = btd.domanda_det_id " + " and bdat.allegato_tipo_id = bta.allegato_tipo_id "
			+ "and btd.domanda_det_cod || '_' || bdat.allegato_tipo_cod = :idDocumentoFruitore";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public long updateProtocollo(String protocollocod, Timestamp dataprotocollo, String tipoprotocollo,
			String idDocumentoFruitore, String tipoDocumento) throws DatabaseException {
		try {

			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("protocollocod", protocollocod, Types.VARCHAR);
			params.addValue("dataprotocollo", dataprotocollo, Types.TIMESTAMP);
			params.addValue("tipoprotocollo", tipoprotocollo, Types.VARCHAR);
			params.addValue("tipoDocumento", tipoDocumento, Types.VARCHAR);
			params.addValue("idDocumentoFruitore", idDocumentoFruitore, Types.VARCHAR);
			
			return jdbcTemplate.update(UPDATE_DETTAGLIO_DOMANDA_PROTOCOLLO, params);
		} catch (Exception e) {
			String methodName = "UPDATE_DETTAGLIO_DOMANDA_PROTOCOLLO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public boolean selectIdDocumentoFruitore(String idDocumentoFruitore) throws DatabaseException {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("idDocumentoFruitore",
				idDocumentoFruitore);
		int countdomanda = 0;
		try {
			countdomanda = jdbcTemplate.queryForObject(SELECT_DOCUMENTO_FRUITORE, namedParameters, Integer.class);

			return countdomanda > 0 ? true : false;

		} catch (EmptyResultDataAccessException e) {
			return false;
		} catch (Exception e) {
			String methodName = "SELECT_DOCUMENTO_FRUITORE";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}
}