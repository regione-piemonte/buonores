/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonorescallban.integration.dao.custom;

import java.math.BigDecimal;
import java.sql.Types;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import it.csi.buonores.buonorescallban.dto.DatiBuono;
import it.csi.buonores.buonorescallban.dto.custom.ModelDomandaExt;
import it.csi.buonores.buonorescallban.exception.DatabaseException;
import it.csi.buonores.buonorescallban.integration.dao.utils.DatiBuonoMapper;
import it.csi.buonores.buonorescallban.integration.dao.utils.DettaglioRichiestaMapper;
import it.csi.buonores.buonorescallban.util.LoggerUtil;

@Repository
public class LogBandiDao extends LoggerUtil {
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public static final String INSERT_LOG_BANDI = "insert into bres_l_bandi_buono " 
			+ "(domanda_numero,buono_id,messageuuid_bandi,operazione,esito,errore_cod,errore_messaggio,utente_creazione,utente_modifica,xml) values "
			+ "(:numeroDomanda, :buonoId, :messageUuid, :operazione, :esito, :errore, "
			+ ":erroreMessaggio, 'BUONORESCALLBACK','BUONORESCALLBACK', pgp_sym_encrypt(:xml, '@dbKeyCrypt@')::bytea)";

	public static final String SELECT_BUONO = "select distinct btb.buono_id, bdbs.buono_stato_cod "
			+ "from bres_t_buono btb, bres_r_buono_stato brbs,bres_d_buono_stato bdbs , "
			+ "bres_t_domanda_dettaglio btdd, bres_t_domanda btd "
			+ "where btb.domanda_det_id = btdd.domanda_det_id and btb.sportello_id = btdd.sportello_id "
			+ "and btb.domanda_id = btdd.domanda_id "
			+ "and btb.data_cancellazione is null "
			+ "and btd.data_cancellazione is null "
			+ "and btdd.domanda_id = btd.domanda_id "
			+ "and btdd.data_cancellazione is null "
			+ "and btdd.validita_inizio <= now() "
			+ "and btdd.validita_fine is null "
			+ "and bdbs.data_cancellazione is null "
			+ "and bdbs.validita_fine is null "
			+ "and bdbs.validita_inizio <= now() "
			+ "and bdbs.buono_stato_id = brbs.buono_stato_id "
			+ "and bdbs.buono_stato_cod in ( %s ) "
			+ "and brbs.buono_id = btb.buono_id "
			+ "and brbs.validita_inizio  <= now() "
			+ "and brbs.validita_fine is null "
			+ "and brbs.data_cancellazione is null "
			+ "and btd.domanda_numero= :numeroDomanda";
	
	public static final String UPDATE_BUONO_UUID = "update bres_t_buono set messageuuid_bandi = :messageuuidBandi, "
            + "utente_modifica = 'BUONORESCALLBACK', data_modifica = now() "
			+ "from bres_t_domanda_dettaglio btdd, "
			+ "bres_t_domanda btd "
			+ "where "
			+ "bres_t_buono.buono_id  = :buonoId and  "
			+ "bres_t_buono.domanda_det_id = btdd.domanda_det_id and bres_t_buono.sportello_id = btdd.sportello_id "
			+ "and bres_t_buono.domanda_id = btdd.domanda_id "
			+ "and btd.data_cancellazione is null "
			+ "and "
			+ "btdd.domanda_id = btd.domanda_id "
			+ "and "
			+ "btdd.data_cancellazione is null "
			+ "and "
			+ "btdd.validita_inizio <= now() "
			+ "and "
			+ "btdd.validita_fine is null "
			+ "and "
			+ "btd.domanda_numero= :numeroDomanda";
	
	public static final String UPDATE_STATO_BUONO = "update bres_r_buono_stato set validita_fine = now(), "
            + "utente_modifica = 'BUONORESCALLBACK', data_modifica = now() "
			+ "where buono_id = :buonoId "
			+ "and validita_fine is null "
			+ "and buono_stato_id = (select buono_stato_id from bres_d_buono_stato where buono_stato_cod = :stato)";
	
	public static final String INSERT_STATO_BUONO = "insert into bres_r_buono_stato (buono_id,buono_stato_id, "
			+ "utente_creazione,utente_modifica, revoca_data_decorrenza, buono_stato_nota) "
			+ "VALUES (:buonoId,(select buono_stato_id from bres_d_buono_stato where buono_stato_cod = :stato), "
			+ "'BUONORESCALLBACK','BUONORESCALLBACK', :dataRevoca, :notaRevoca)";
	
    public static final String SELECT_NUMERO_RICHIESTA = "select "
            + "    btd.domanda_numero, "
            + "    btd.richiedente_cf, "
            + "    btd.beneficiario_cf "
            + "    from bres_t_domanda btd "
            + "    where "
            + "    btd.data_cancellazione is null "
            + "    and "
            + "    btd.domanda_numero= :numeroDomanda";
	
	public long insertLogBandi(String numeroDomanda, BigDecimal buonoId, String messageUuid, String esito, String errore, 
			String erroreMessaggio, String operazione, String xml) throws DatabaseException{

		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		try {
			params.addValue("numeroDomanda", numeroDomanda, Types.VARCHAR);
			params.addValue("buonoId", buonoId, Types.BIGINT);
			params.addValue("messageUuid", messageUuid, Types.VARCHAR);
			params.addValue("esito", esito, Types.VARCHAR);
			params.addValue("errore", errore, Types.VARCHAR);
			params.addValue("erroreMessaggio", erroreMessaggio, Types.VARCHAR);
	        params.addValue("operazione", operazione, Types.VARCHAR);
	        params.addValue("xml", xml, Types.VARCHAR);
		
	        jdbcTemplate.update(INSERT_LOG_BANDI, params, keyHolder, new String[] { "bandi_buono_id" });
	        return keyHolder.getKey().longValue();
		} catch(Exception e) {
			generateDatabaseException("insertLogBandi", e);
		}
		
		return 0;
	}

	public DatiBuono selectBuono(String numeroDomanda, String stato) throws DatabaseException {
		String querySelectBuono = String.format(SELECT_BUONO, stato);
		
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda);
		try {
			return jdbcTemplate.queryForObject(querySelectBuono, namedParameters, new DatiBuonoMapper());

		} catch (EmptyResultDataAccessException e) {
			return null; 
		} catch (Exception e) {
			generateDatabaseException("SELECT_BUONO", e);
		}
		
		return null;
	}
	
	public long updateBuonoConUUID(String numeroDomanda, BigDecimal buonoId,String messageuuidBandi) throws DatabaseException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("numeroDomanda", numeroDomanda, Types.VARCHAR).addValue("buonoId", buonoId, Types.BIGINT)
				.addValue("messageuuidBandi", messageuuidBandi, Types.VARCHAR);		
			return jdbcTemplate.update(UPDATE_BUONO_UUID, params);
		} catch (Exception e) {
        	generateDatabaseException("SELECT_BUONO_PRE_REVOCA", e);
        }
        
        return 0;
	}
	
	public long chiudoStatoBuono(BigDecimal buonoId, String stato) throws DatabaseException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("buonoId", buonoId, Types.BIGINT)
				.addValue("stato", stato, Types.VARCHAR);    
			return jdbcTemplate.update(UPDATE_STATO_BUONO, params);
		} catch (Exception e) {
        	generateDatabaseException("SELECT_BUONO_PRE_REVOCA", e);
        }
        
        return 0;
	}
	
	public long aproRecordBuonoStato(BigDecimal buonoId, String stato, 
			String dataRevoca, String notaRevoca) throws DatabaseException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("buonoId", buonoId, Types.BIGINT)
				.addValue("stato", stato, Types.VARCHAR)
				.addValue("dataRevoca", dataRevoca, Types.DATE)
				.addValue("notaRevoca", notaRevoca, Types.VARCHAR);	
			return jdbcTemplate.update(INSERT_STATO_BUONO, params);
		} catch (Exception e) {
        	generateDatabaseException("SELECT_BUONO_PRE_REVOCA", e);
        }
        
        return 0;
	}
	
    public ModelDomandaExt selectNumeroRichiesta(String numeroRichiesta) throws DatabaseException {
        ModelDomandaExt richieste = new ModelDomandaExt();
        
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroRichiesta);
        try {
            richieste = jdbcTemplate.queryForObject(SELECT_NUMERO_RICHIESTA, namedParameters, new DettaglioRichiestaMapper());
            return richieste;
        } catch (EmptyResultDataAccessException e) {
            return null; 
        } catch (Exception e) {
            String methodName = "SELECT_NUMERO_RICHIESTA";
            logError(methodName, e.getMessage());
            throw new DatabaseException(e);
        }

    }
}
