/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.integration.dao.custom;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import it.csi.buonores.buonoresbandisrv.dto.ModelRichiesta;
import it.csi.buonores.buonoresbandisrv.exception.DatabaseException;
import it.csi.buonores.buonoresbandisrv.integration.dao.utils.DettaglioRichiestaMapper;
import it.csi.buonores.buonoresbandisrv.util.LoggerUtil;

@Repository
public class RichiesteDao extends LoggerUtil {

	public static final String SELECT_NUMERO_RICHIESTA = "select bdct2.contributo_tipo_desc,  "
			+ "	btdd.domanda_det_id,  "
			+ "	btdd.sportello_id,  "
			+ "	btd.domanda_numero,  "
			+ " btdd.domanda_det_cod,  "
			+ "	bdds.domanda_stato_cod,  "
			+ "	bdds.domanda_stato_desc,  "
			+ "	btdd.data_creazione ,  "
			+ "	btd.richiedente_cf,  "
			+ "	btdd.richiedente_nome,  "
			+ "	btdd.richiedente_cognome,  "
			+ "	btdd.richiedente_nascita_data,  "
			+ "	btdd.richiedente_nascita_stato,  "
			+ "	btdd.richiedente_nascita_comune,  "
			+ "	btdd.richiedente_nascita_provincia,  "
			+ "	convert_from(pgp_sym_decrypt_bytea(btdd.richiedente_residenza_indirizzo::bytea,'@dbKeyCrypt@'),'UTF8') as richiedente_residenza_indirizzo,  "
			+ "	btdd.richiedente_residenza_comune,  "
			+ "	btdd.richiedente_residenza_provincia,  "
			+ "	btd.beneficiario_cf,  "
			+ "	btdd.destinatario_nome,  "
			+ "	btdd.destinatario_cognome,  "
			+ "	btdd.destinatario_nascita_data,  "
			+ "	btdd.destinatario_nascita_stato,  "
			+ "	btdd.destinatario_nascita_comune,  "
			+ "	btdd.destinatario_nascita_provincia,  "
			+ "	convert_from(pgp_sym_decrypt_bytea(btdd.destinatario_residenza_indirizzo::bytea,'@dbKeyCrypt@'),'UTF8') as destinatario_residenza_indirizzo,  "
			+ "	btdd.destinatario_residenza_comune,  "
			+ "	btdd.destinatario_residenza_provincia,  "
			+ " convert_from(pgp_sym_decrypt_bytea(btdd.destinatario_domicilio_indirizzo::bytea,'@dbKeyCrypt@'),'UTF8') as destinatario_domicilio_indirizzo,  "
			+ "	btdd.destinatario_domicilio_comune,  "
			+ "	btdd.destinatario_domicilio_provincia, "
			+ "	btdd.note,  "
			+ "	bdts.titolo_studio_cod,  "
			+ "	bda.asl_azienda_desc,   "
			+ "	btdd.punteggio_sociale,  "
			+ "	bdrt.rapporto_tipo_cod as delega,  "
			+ "	btdd.isee_valore,  "
			+ "	bdct.contratto_tipo_cod,  "
			+ "	btdd.datore_di_lavoro_cf,  "
			+ "	btdd.datore_di_lavoro_nome,  "
			+ "	btdd.datore_di_lavoro_cognome,  "
			+ "	btdd.datore_di_lavoro_nascita_data,  "
			+ "	btdd.datore_di_lavoro_nascita_stato,  "
			+ "	btdd.datore_di_lavoro_nascita_comune,  "
			+ "	btdd.datore_di_lavoro_nascita_provincia,  "
			+ " relazione.rapporto_tipo_cod as relazione_destinatario, "
			+ " btdd.nessuna_incompatibilita, "
			+ " btdd.situazione_lavorativa_attiva,  "
			+ " bdvm.valutazione_multidimensionale_cod,  "
			+ "	btdd.contratto_data_inizio,  "
			+ "	btdd.contratto_data_fine, "
			+ "	btdd.nota_interna, "
			+ "	btdd.rsa_cod, "
			+ "	btdd.rsa_arpe_cod, "
			+ "	btdd.rsa_denominazione, "
			+ "	btdd.rsa_indirizzo, "
			+ "	btdd.rsa_comune, "
			+ "	btdd.rsa_provincia, "
			+ "	btdd.rsa_cfpiva, "
			+ "	btdd.rsa_sede_legale_denominazione, "
			+ "	btdd.rsa_sede_legale_indirizzo, "
			+ "	btdd.rsa_sede_legale_comune, "
			+ "	btdd.rsa_sede_legale_provincia, "
			+ " btdd.note_richiedente, " // MODIFICA TAG 004 buonodom
			+ "	btdd.titolo_privato,"
			+ " btdd.incompatibilita_per_contratto, "
			+ " to_char(btd.domanda_data::Date,'dd/mm/yyyy') domanda_data, "
			+ " btd.domanda_data::Time domanda_data_ora,btd.domanda_id, btdd.area_id "
			+ "	from bres_d_contributo_tipo bdct2,  "
			+ "	bres_t_domanda btd,  "
			+ "	bres_t_domanda_dettaglio btdd   "
			+ "	left join bres_d_rapporto_tipo bdrt on  btdd.rapporto_tipo_id = bdrt.rapporto_tipo_id  "
			+ "	left join bres_d_rapporto_tipo relazione on  btdd.relazione_tipo_id = relazione.rapporto_tipo_id  "
			+ "	left join bres_d_domanda_stato bdds on bdds.domanda_stato_id  = btdd.domanda_stato_id  "
			+ "	left join bres_d_contratto_tipo bdct on btdd.contratto_tipo_id = bdct.contratto_tipo_id  "
			+ " left join bres_d_valutazione_multidimensionale bdvm on btdd.valutazione_multidimensionale_id= bdvm.valutazione_multidimensionale_id,    "
			+ "	bres_d_titolo_studio bdts,  "
			+ "	bres_d_asl bda  "
			+ "	where bdct2.contributo_tipo_id = btd.contributo_tipo_id  "
			+ "	and  "
			+ "	btd.data_cancellazione is null  "
			+ "	and   "
			+ "	btdd.domanda_id = btd.domanda_id   "
			+ "	and   "
			+ "	btdd.data_cancellazione is null   "
			+ "	and   "
			+ "	btdd.validita_inizio <= now()  "
			+ "	and  "
			+ "	btdd.validita_fine is null    "
			+ "	and   "
			+ "	btdd.titolo_studio_id=bdts.titolo_studio_id   "
			+ "	and   "
			+ "	btdd.asl_id = bda.asl_id 	  "
			+ "	and  "
			+ "	btd.domanda_numero= :numeroDomanda";

	public static final String SELECT_ISTAT_COMUNE = "select bdc.comune_istat_cod from bres_d_comune bdc "
			+ "where bdc.comune_desc= :comuneDesc";

	public static final String SELECT_IMPORTO = "select distinct replace(trim(to_char(btgd.importo,'999999999.00')),'.',',') importo from bres_t_graduatoria_dettaglio btgd, bres_t_graduatoria_finanziamento btgf, "
			+ "bres_t_domanda btd "
			+ "where btd.domanda_numero = :numeroDomanda "
			+ "and btd.domanda_id = btgd.domanda_id "
			+ "and btgf.graduatoria_id = btgd.graduatoria_id "
			+ "and btgd.finanziamento_id = btgf.finanziamento_id "
			+ "and btd.data_cancellazione is null "
			+ "and btgd.data_cancellazione is null "
			+ "and btgf.data_cancellazione is null "
			+ "and btgd.importo!=0";

	public static final String SELECT_AREA = "select area_cod from bres_d_area "
			+ "where area_id = :areaId";

	public static final String SELECT_COD_SPORTELLO = "select sportello_cod from bres_t_sportello "
			+ "where sportello_id = :sportelloId";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public ModelRichiesta selectNumeroRichiesta(String numeroRichiesta) throws DatabaseException {
		ModelRichiesta richieste = new ModelRichiesta();

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroRichiesta);
		try {
			richieste = jdbcTemplate.queryForObject(SELECT_NUMERO_RICHIESTA, namedParameters,
					new DettaglioRichiestaMapper());
			return richieste;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_NUMERO_RICHIESTA";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public String selectIstatComune(String comune) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("comuneDesc", comune);
		try {
			return jdbcTemplate.queryForObject(SELECT_ISTAT_COMUNE, namedParameters, String.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_ISTAT_COMUNE";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public String selectImporto(String numeroDomanda) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda);
		try {
			return jdbcTemplate.queryForObject(SELECT_IMPORTO, namedParameters, String.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_IMPORTO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public String selectSportello(BigDecimal sportelloId) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("sportelloId", sportelloId);
		try {
			return jdbcTemplate.queryForObject(SELECT_COD_SPORTELLO, namedParameters, String.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_COD_SPORTELLO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public String selectArea(BigDecimal areaId) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("areaId", areaId);
		try {
			return jdbcTemplate.queryForObject(SELECT_AREA, namedParameters, String.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_AREA";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}
}
