/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresrend.integration.dao.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import it.csi.buonores.buonoresrend.dto.ModelRichiesta;
import it.csi.buonores.buonoresrend.exception.DatabaseException;
import it.csi.buonores.buonoresrend.integration.dao.utils.DettaglioRichiestaMapper;
import it.csi.buonores.buonoresrend.util.LoggerUtil;

@Repository
public class RichiesteDao extends LoggerUtil {



	public static final String SELECT_NUMERO_RICHIESTA = "select " + "	btd.domanda_numero, "
			+ "	bdds.domanda_stato_cod, " + "	bdds.domanda_stato_desc, " + "	btdd.data_creazione , "
			+ "	btd.richiedente_cf, " + "	btdd.richiedente_nome, " + "	btdd.richiedente_cognome, "
			+ "	btdd.richiedente_nascita_data, " + "	btdd.richiedente_nascita_stato, "
			+ "	btdd.richiedente_nascita_comune, " + "	btdd.richiedente_nascita_provincia, "
			+ "	convert_from(pgp_sym_decrypt_bytea(btdd.richiedente_residenza_indirizzo::bytea,'@dbKeyCrypt@'),'UTF8') as richiedente_residenza_indirizzo, "
			+ "	btdd.richiedente_residenza_comune, " + "	btdd.richiedente_residenza_provincia, "
			+ "	btd.beneficiario_cf, " + "	btdd.destinatario_nome, " + "	btdd.destinatario_cognome, "
			+ "	btdd.destinatario_nascita_data, " + "	btdd.destinatario_nascita_stato, "
			+ "	btdd.destinatario_nascita_comune, " + "	btdd.destinatario_nascita_provincia, "
			+ "	convert_from(pgp_sym_decrypt_bytea(btdd.destinatario_residenza_indirizzo::bytea,'@dbKeyCrypt@'),'UTF8') as destinatario_residenza_indirizzo, "
			+ "	btdd.destinatario_residenza_comune, " + "	btdd.destinatario_residenza_provincia, "
			+ " convert_from(pgp_sym_decrypt_bytea(btdd.destinatario_domicilio_indirizzo::bytea,'@dbKeyCrypt@'),'UTF8') as destinatario_domicilio_indirizzo, "
			+ "	btdd.destinatario_domicilio_comune, " + "	btdd.destinatario_domicilio_provincia," + "	btdd.note, "
			+ "	bdts.titolo_studio_cod, " + "	bda.asl_cod,  " + "	btdd.punteggio_sociale, "
			+ "	bdrt.rapporto_tipo_cod as delega, " + "	btdd.isee_valore, " + "	bdct.contratto_tipo_cod, "
			+ "	btdd.datore_di_lavoro_cf, " + "	btdd.datore_di_lavoro_nome, " + "	btdd.datore_di_lavoro_cognome, "
			+ "	btdd.datore_di_lavoro_nascita_data, " + "	btdd.datore_di_lavoro_nascita_stato, "
			+ "	btdd.datore_di_lavoro_nascita_comune, " + "	btdd.datore_di_lavoro_nascita_provincia, "
			+ "	btdd.incompatibilita_per_contratto, " + "	btdd.isee_conforme, "
			+ " relazione.rapporto_tipo_cod as relazione_destinatario," + " btdd.nessuna_incompatibilita,"
			+ " btdd.situazione_lavorativa_attiva, " + " bdvm.valutazione_multidimensionale_cod, "
			+ "	btdd.contratto_data_inizio, " + "	btdd.contratto_data_fine, " + "	btdd.protocollo_cod, "
			+ "	btdd.data_protocollo, " + "	btdd.tipo_protocollo, " + "	btdd.rsa_cod," + "	btdd.rsa_arpe_cod,"
			+ "	btdd.rsa_denominazione," + "	btdd.rsa_indirizzo," + "	btdd.rsa_comune," + "	btdd.rsa_provincia,"
			+ "	btdd.rsa_cfpiva," + "	btdd.rsa_sede_legale_denominazione," + "	btdd.rsa_sede_legale_indirizzo,"
			+ "	btdd.rsa_sede_legale_comune," + "	btdd.rsa_sede_legale_provincia," + "	btdd.titolo_privato, " + "  btdd.note_richiedente " 
			+ "	from " + "	bres_t_domanda btd, " + "	bres_t_domanda_dettaglio btdd  "
			+ "	left join bres_d_rapporto_tipo bdrt on  btdd.rapporto_tipo_id = bdrt.rapporto_tipo_id "
			+ " left join bres_d_rapporto_tipo relazione on  btdd.relazione_tipo_id = relazione.rapporto_tipo_id "
			+ "	left join bres_d_domanda_stato bdds on bdds.domanda_stato_id  = btdd.domanda_stato_id "
			+ "	left join bres_d_contratto_tipo bdct on btdd.contratto_tipo_id = bdct.contratto_tipo_id "
			+ " left join bres_d_valutazione_multidimensionale bdvm on btdd.valutazione_multidimensionale_id= bdvm.valutazione_multidimensionale_id,   "
			+ "	bres_d_titolo_studio bdts, " + "	bres_d_asl bda " 
			+ "	where " 
			+ "	btd.data_cancellazione is null "
			+ "	and  " 
			+ "	btdd.domanda_id = btd.domanda_id  " 
			+ "	and  " 
			+ "	btdd.data_cancellazione is null  "
			+ "	and  " 
			+ "	btdd.validita_inizio <= now() " 
			+ "	and " 
			+ "	btdd.validita_fine is null   " 
			+ "	and  "
			+ "	btdd.titolo_studio_id=bdts.titolo_studio_id  " 
			+ "	and  " 
			+ "	btdd.asl_id = bda.asl_id 	 "
			+ "	and "
			+ "	btd.domanda_numero= :numeroDomanda";

	public static final String SELECT_PRECEDENTE_STATO = "with tab1 as ( "
			+ "select a.domanda_det_cod, max(a.validita_inizio) validita_inizio from bres_t_domanda_dettaglio a, "
			+ "bres_t_domanda b,bres_d_domanda_stato c "
			+ "where a.domanda_id = b.domanda_id "
			+ "and b.domanda_numero  = :numeroDomanda "
			+ "and c.domanda_stato_id = a.domanda_stato_id "
			+ "and a.validita_fine is not null "
			+ "and a.data_cancellazione is null "
			+ "and c.data_cancellazione is null "
			+ "and b.data_cancellazione is null "
			+ "and c.validita_fine is null "
			+ "group by a.domanda_det_cod "
			+ "order by validita_inizio desc "
			+ "limit 1) "
			+ "select domanda_det_cod from tab1";
    
	public static final String DATA_SPORTELLO = "select to_char((bts.validita_fine + interval '1 months'),'yyyy-mm-01') "
			+ "from bres_t_domanda btd, bres_t_sportello bts "
			+ "where btd.domanda_numero  = :numeroDomanda "
			+ "and btd.data_cancellazione is null "
			+ "and bts.data_cancellazione is null "
			+ "and bts.sportello_id = btd.sportello_id";
	
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;



	public ModelRichiesta selectNumeroRichiesta(String numeroRichiesta) throws DatabaseException {
		ModelRichiesta richieste = new ModelRichiesta();

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroRichiesta);
		try {
			richieste = jdbcTemplate.queryForObject(SELECT_NUMERO_RICHIESTA, namedParameters,
					new DettaglioRichiestaMapper());
			// prendo ultima nota
//			if (!Util.isValorizzato(richieste.getNote())) {
//				String nota = selectNotaCittadino(numeroRichiesta);
//				if (Util.isValorizzato(nota)) {
//					richieste.setNote(nota);
//				}
//			}
			return richieste;
		} catch (Exception e) {
			generateDatabaseException("SELECT_NUMERO_RICHIESTA", e);
		}
		return richieste;

	}
	
	public String selectStatoPrecedente(String numeroRichiesta) throws DatabaseException {
		String dettaglio = null;
		
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroRichiesta);
		try {
			dettaglio = jdbcTemplate.queryForObject(SELECT_PRECEDENTE_STATO, namedParameters, String.class);
			return dettaglio;
		} catch (EmptyResultDataAccessException e) {
			return null; 
		} catch (Exception e) {
			String methodName = "SELECT_PRECEDENTE_STATO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public String selectDataSportelloChiuso(String numeroRichiesta) throws DatabaseException {
		String dettaglio = null;
		
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroRichiesta);
		try {
			dettaglio = jdbcTemplate.queryForObject(DATA_SPORTELLO, namedParameters, String.class);
			return dettaglio;
		} catch (EmptyResultDataAccessException e) {
			return null; 
		} catch (Exception e) {
			String methodName = "DATA_SPORTELLO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}
}
