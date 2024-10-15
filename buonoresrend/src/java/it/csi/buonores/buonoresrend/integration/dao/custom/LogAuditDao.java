/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresrend.integration.dao.custom;

import java.sql.Types;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import it.csi.buonores.buonoresrend.dto.custom.LogAudit;
import it.csi.buonores.buonoresrend.exception.DatabaseException;
import it.csi.buonores.buonoresrend.util.LoggerUtil;

@Repository
public class LogAuditDao extends LoggerUtil {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public static final String INSERT_LOG_AUDIT = "insert into csi_log_audit "
			+ " (audit_id,data_ora,id_app,ip_address,utente,operazione,ogg_oper,key_oper,uuid,request_payload,response_payload,esito_chiamata) values "
			+ " (nextval('csi_log_audit_audit_id_seq'),now(), :id_app , :ip_address , "
			+ ":utente , :operazione ,:ogg_oper, :key_oper, :uuid, "
			+ "pgp_sym_encrypt(:request_payload, '@dbKeyCrypt@')::bytea, pgp_sym_encrypt(:response_payload, '@dbKeyCrypt@')::bytea, :esito_chiamata)";

	public long insertLogAudit(LogAudit logAudit) throws DatabaseException {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue("id_app", logAudit.getIdApp(), Types.VARCHAR);
		params.addValue("ip_address", logAudit.getIpAddress(), Types.VARCHAR);
		params.addValue("utente", logAudit.getUtente(), Types.VARCHAR);
		params.addValue("operazione", logAudit.getOperazione(), Types.VARCHAR);
		params.addValue("ogg_oper", logAudit.getOggOper(), Types.VARCHAR);

		params.addValue("key_oper", logAudit.getKeyOper(), Types.VARCHAR);
		params.addValue("uuid", logAudit.getUuid(), Types.VARCHAR);
		params.addValue("request_payload", logAudit.getRequestPayload(), Types.VARCHAR);
		params.addValue("response_payload", logAudit.getResponsePayload(), Types.VARCHAR);
		params.addValue("esito_chiamata", logAudit.getEsitoChiamata(), Types.BIGINT);

		jdbcTemplate.update(INSERT_LOG_AUDIT, params, keyHolder, new String[] { "audit_id" });
		return keyHolder.getKey().longValue();
	}

}