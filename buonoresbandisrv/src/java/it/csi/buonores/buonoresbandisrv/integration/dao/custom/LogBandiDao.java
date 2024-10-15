/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.integration.dao.custom;

import java.math.BigDecimal;
import java.sql.Types;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import it.csi.buonores.buonoresbandisrv.exception.DatabaseException;
import it.csi.buonores.buonoresbandisrv.util.LoggerUtil;

@Repository
public class LogBandiDao extends LoggerUtil {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public static final String INSERT_LOG_BANDI = "insert into bres_l_bandi_buono "
			+ "(domanda_numero,buono_id,messageuuid_bandi,operazione,xml,esito,errore_cod,errore_messaggio,utente_creazione,utente_modifica) values "
			+ "(:numeroDomanda, :buonoId, :messageUuid, :operazione, pgp_sym_encrypt(:xml, '@dbKeyCrypt@')::bytea, :esito, :errore, "
			+ ":erroreMessaggio, 'BUONORESBANDISRV','BUONORESBANDISRV')";

	public long insertLogBandi(String numeroDomanda, BigDecimal buonoId, String messageUuid, String xml, String esito,
			String errore, String erroreMessaggio, String operazione) throws DatabaseException {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue("numeroDomanda", numeroDomanda, Types.VARCHAR);
		params.addValue("buonoId", buonoId, Types.BIGINT);
		params.addValue("messageUuid", messageUuid, Types.VARCHAR);
		params.addValue("xml", xml, Types.VARCHAR);
		params.addValue("esito", esito, Types.VARCHAR);
		params.addValue("errore", errore, Types.VARCHAR);
		params.addValue("erroreMessaggio", erroreMessaggio, Types.VARCHAR);
		params.addValue("operazione", operazione, Types.VARCHAR);

		jdbcTemplate.update(INSERT_LOG_BANDI, params, keyHolder, new String[] { "bandi_buono_id" });
		return keyHolder.getKey().longValue();
	}
}