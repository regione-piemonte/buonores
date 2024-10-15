/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresstarda.integration.dao.custom;

import java.sql.Types;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import it.csi.buonores.buonoresstarda.dto.custom.StardasLog;
import it.csi.buonores.buonoresstarda.exception.DatabaseException;
import it.csi.buonores.buonoresstarda.util.LoggerUtil;

@Repository
public class StardasLogDao extends LoggerUtil {
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public static final String INSERT_STARDAS_LOG = "insert into bres_l_stardas " 
			+ " (id_log,messageUUID,idDocumentoFruitore,tipoTrattamento,esitiStep,informazioniAggiuntive,esitoTrattamento,data_esecuzione) values "
			+ " (nextval('bres_l_stardas_id_log_seq'), :messageUUID , :idDocumentoFruitore , "
			+ ":tipoTrattamento , :esitiStep ,:informazioniAggiuntive, :esitoTrattamento, now())";
	
	public long insertLogStardas(StardasLog logStartdus) throws DatabaseException{

		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
		params.addValue("messageUUID", logStartdus.getMessageUUID(), Types.VARCHAR);
		params.addValue("idDocumentoFruitore", logStartdus.getIdDocumentoFruitore(), Types.VARCHAR);
		params.addValue("tipoTrattamento", logStartdus.getTipoTrattamento(), Types.VARCHAR);
		params.addValue("esitiStep", logStartdus.getEsitiStep(), Types.VARCHAR);
		params.addValue("informazioniAggiuntive",  logStartdus.getInformazioniAggiuntive(), Types.VARCHAR);
		params.addValue("esitoTrattamento", logStartdus.getEsitoTrattamento(), Types.VARCHAR);

		jdbcTemplate.update(INSERT_STARDAS_LOG, params, keyHolder, new String[] { "id_log" });
		return keyHolder.getKey().longValue();
		}
		catch (Exception e) {
			String methodName = "UPDATE_DETTAGLIO_DOMANDA_PROTOCOLLO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

}