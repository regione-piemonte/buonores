/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.integration.dao.custom;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import it.csi.buonores.buonoressrv.dto.ModelDecodifica;
import it.csi.buonores.buonoressrv.dto.ModelGetAllegato;
import it.csi.buonores.buonoressrv.dto.ModelGetAllegatoExt;
import it.csi.buonores.buonoressrv.dto.ModelIncompatibilitaRichiesta;
import it.csi.buonores.buonoressrv.dto.ModelRichiesta;
import it.csi.buonores.buonoressrv.dto.ModelRichiestaRecupero;
import it.csi.buonores.buonoressrv.exception.DatabaseException;
import it.csi.buonores.buonoressrv.integration.dao.utils.AllegatoExtMapper;
import it.csi.buonores.buonoressrv.integration.dao.utils.AllegatoGetMapper;
import it.csi.buonores.buonoressrv.integration.dao.utils.DettaglioRichiestaMapper;
import it.csi.buonores.buonoressrv.integration.dao.utils.DettaglioRichiestaRecuperoMapper;
import it.csi.buonores.buonoressrv.integration.dao.utils.IncompatibilitaMapper;
import it.csi.buonores.buonoressrv.integration.dao.utils.ListaDecodificaMapper;
import it.csi.buonores.buonoressrv.util.LoggerUtil;

@Repository
public class RichiesteDao extends LoggerUtil {

	public static final String SELECT_NUMERO_RICHIESTA = "select bdct2.contributo_tipo_desc,  "
			+ "	btdd.domanda_det_id,  " + "	btdd.sportello_id,  " + "	btd.domanda_numero,  "
			+ " btdd.domanda_det_cod,  " + "	bdds.domanda_stato_cod,  " + "	bdds.domanda_stato_desc,  "
			+ "	btdd.data_creazione ,  " + "	btd.richiedente_cf,  " + "	btdd.richiedente_nome,  "
			+ "	btdd.richiedente_cognome,  " + "	btdd.richiedente_nascita_data,  "
			+ "	btdd.richiedente_nascita_stato,  " + "	btdd.richiedente_nascita_comune,  "
			+ "	btdd.richiedente_nascita_provincia,  "
			+ "	convert_from(pgp_sym_decrypt_bytea(btdd.richiedente_residenza_indirizzo::bytea,'@dbKeyCrypt@'),'UTF8') as richiedente_residenza_indirizzo,  "
			+ "	btdd.richiedente_residenza_comune,  " + "	btdd.richiedente_residenza_provincia,  "
			+ "	btd.beneficiario_cf,  " + "	btdd.destinatario_nome,  " + "	btdd.destinatario_cognome,  "
			+ "	btdd.destinatario_nascita_data,  " + "	btdd.destinatario_nascita_stato,  "
			+ "	btdd.destinatario_nascita_comune,  " + "	btdd.destinatario_nascita_provincia,  "
			+ "	convert_from(pgp_sym_decrypt_bytea(btdd.destinatario_residenza_indirizzo::bytea,'@dbKeyCrypt@'),'UTF8') as destinatario_residenza_indirizzo,  "
			+ "	btdd.destinatario_residenza_comune,  " + "	btdd.destinatario_residenza_provincia,  "
			+ " convert_from(pgp_sym_decrypt_bytea(btdd.destinatario_domicilio_indirizzo::bytea,'@dbKeyCrypt@'),'UTF8') as destinatario_domicilio_indirizzo,  "
			+ "	btdd.destinatario_domicilio_comune,  " + "	btdd.destinatario_domicilio_provincia, "
			+ "	btdd.note,  " + "	bdts.titolo_studio_cod,  " + "	bda.asl_azienda_desc,   "
			+ "	btdd.punteggio_sociale,  " + "	bdrt.rapporto_tipo_cod as delega,  " + "	btdd.isee_valore,  "
			+ "	bdct.contratto_tipo_cod,  " + "	btdd.datore_di_lavoro_cf,  " + "	btdd.datore_di_lavoro_nome,  "
			+ "	btdd.datore_di_lavoro_cognome,  " + "	btdd.datore_di_lavoro_nascita_data,  "
			+ "	btdd.datore_di_lavoro_nascita_stato,  " + "	btdd.datore_di_lavoro_nascita_comune,  "
			+ "	btdd.datore_di_lavoro_nascita_provincia,  " + " relazione.rapporto_tipo_cod as relazione_destinatario, "
			+ " btdd.nessuna_incompatibilita, " + " btdd.situazione_lavorativa_attiva,  "
			+ " bdvm.valutazione_multidimensionale_cod,  " + "	btdd.contratto_data_inizio,  "
			+ "	btdd.contratto_data_fine, " + "	btdd.nota_interna, " + "	btdd.rsa_cod, " + "	btdd.rsa_arpe_cod, "
			+ "	btdd.rsa_denominazione, " + "	btdd.rsa_indirizzo, " + "	btdd.rsa_comune, "
			+ "	btdd.rsa_provincia, " + "	btdd.rsa_cfpiva, " + "	btdd.rsa_sede_legale_denominazione, "
			+ "	btdd.rsa_sede_legale_indirizzo, " + "	btdd.rsa_sede_legale_comune, "
			+ "	btdd.rsa_sede_legale_provincia, " + " btdd.note_richiedente, " // MODIFICA TAG 004 buonodom
			+ "	btdd.titolo_privato," + " btdd.incompatibilita_per_contratto, "
			+ " to_char((btdd.validita_inizio + interval '1 months'),'yyyy-mm-01') as data_rendicontazione "
			+ "	from bres_d_contributo_tipo bdct2,  " + "	bres_t_domanda btd,  "
			+ "	bres_t_domanda_dettaglio btdd   "
			+ "	left join bres_d_rapporto_tipo bdrt on  btdd.rapporto_tipo_id = bdrt.rapporto_tipo_id  "
			+ "	left join bres_d_rapporto_tipo relazione on  btdd.relazione_tipo_id = relazione.rapporto_tipo_id  "
			+ "	left join bres_d_domanda_stato bdds on bdds.domanda_stato_id  = btdd.domanda_stato_id  "
			+ "	left join bres_d_contratto_tipo bdct on btdd.contratto_tipo_id = bdct.contratto_tipo_id  "
			+ " left join bres_d_valutazione_multidimensionale bdvm on btdd.valutazione_multidimensionale_id= bdvm.valutazione_multidimensionale_id,    "
			+ "	bres_d_titolo_studio bdts,  " + "	bres_d_asl bda  "
			+ "	where bdct2.contributo_tipo_id = btd.contributo_tipo_id  " + "	and  "
			+ "	btd.data_cancellazione is null  " + "	and   " + "	btdd.domanda_id = btd.domanda_id   " + "	and   "
			+ "	btdd.data_cancellazione is null   " + "	and   " + "	btdd.validita_inizio <= now()  " + "	and  "
			+ "	btdd.validita_fine is null    " + "	and   " + "	btdd.titolo_studio_id=bdts.titolo_studio_id   "
			+ "	and   " + "	btdd.asl_id = bda.asl_id 	  " + "	and  " + "	btd.domanda_numero= :numeroDomanda";

	public static final String UPDATE_DOMANDA_ALLEGATO = "UPDATE bres_t_allegato a "
			+ "SET data_modifica=now(),utente_modifica=:cfmodifica, file_name=:nomefile, file_type=:tipofile, file_path=:pathfile "
			+ "from bres_t_domanda_dettaglio b, bres_d_allegato_tipo c "
			+ "WHERE b.domanda_det_cod = a.domanda_det_cod " + "and b.domanda_det_id = a.domanda_det_id "
			+ "and a.sportello_id = b.sportello_id " + "and a.domanda_det_cod =:detcod "
			+ "and c.allegato_tipo_cod =:tipodom " + "and a.allegato_tipo_id = c.allegato_tipo_id";

	public static final String INSERT_DOMANDA_ALLEGATO = "INSERT INTO bres_t_allegato "
			+ "(allegato_id, file_name, file_type, file_path, sportello_id, "
			+ "domanda_det_id, domanda_det_cod, allegato_tipo_id, data_creazione, "
			+ "data_modifica, utente_creazione, utente_modifica) "
			+ " VALUES(nextval('bres_t_allegato_allegato_id_seq'), :file_name, :file_type, :file_path, :sportello_id, :domanda_det_id, :domanda_det_cod, "
			+ "(select allegato_tipo_id from bres_d_allegato_tipo where allegato_tipo_cod = :allegatotipocod), "
			+ "now(), now(), :utente_creazione, :utente_modifica)";

	public static final String SELECT_DET_COD_ALLEGATO = "select count(*) from bres_t_allegato a, bres_d_allegato_tipo b "
			+ "where domanda_det_cod=:detcod " + "and b.allegato_tipo_cod =:tipodom "
			+ "and a.allegato_tipo_id = b.allegato_tipo_id";

	public static final String SELECT_ALLEGATI = "select bta.file_path,bta.file_name,bta.allegato_id,bta.sportello_id,bta.allegato_tipo_id,bdat.allegato_tipo_cod,"
			+ " bdat.allegato_tipo_desc, bta.file_type " + " from bres_t_allegato bta, bres_d_allegato_tipo bdat  "
			+ " where  " + " bta.domanda_det_id = :domandaID " + " and bdat.allegato_tipo_id = bta.allegato_tipo_id "
			+ " and bdat.data_cancellazione is null " + " and bta.data_cancellazione is null "
			+ " and bdat.allegato_tipo_utilizzabile = true ";

	public static final String SELECT_GET_ALLEGATO = "select bta.file_path,bta.file_name  "
			+ " from bres_t_allegato bta  " + " where  " + " bta.domanda_det_id = :domandaID "
			+ " and bta.data_cancellazione is null "
			+ " and bta.allegato_tipo_id = (select bdat.allegato_tipo_id  from bres_d_allegato_tipo bdat where bdat.allegato_tipo_cod = :allegatoCod) ";

	public static final String UPDATE_DETTAGLIO_DOMANDA_MESSAGEUUID = "update bres_t_domanda_dettaglio "
			+ "set data_modifica=now(), utente_modifica='STARDAS_SMISTADOC', "
			+ "messageuuid_protocollo=:messageuuid where domanda_det_cod = :domandadetcod";

	public static final String SELECT_PRECEDENTE_STATO = "with tab1 as ( "
			+ "select a.domanda_det_cod, max(a.validita_inizio) validita_inizio from bres_t_domanda_dettaglio a, "
			+ "bres_t_domanda b,bres_d_domanda_stato c " + "where a.domanda_id = b.domanda_id "
			+ "and b.domanda_numero  = :numeroDomanda " + "and c.domanda_stato_id = a.domanda_stato_id "
			+ "and a.validita_fine is not null " + "and a.data_cancellazione is null "
			+ "and c.data_cancellazione is null " + "and b.data_cancellazione is null " + "and c.validita_fine is null "
			+ "group by a.domanda_det_cod " + "order by validita_inizio desc " + "limit 1) "
			+ "select domanda_det_cod from tab1";
	// TAG PROD DOM 2.1.0-001
	public static final String SELECT_DATA_CONTRODEDOTTA = "select to_char(max(btdd.validita_inizio),'dd/MM/yyyy') datacontrodedotta from "
			+ "bres_t_domanda_dettaglio btdd , bres_t_domanda btd , bres_d_domanda_stato bdds "
			+ "where btdd.domanda_id = btd.domanda_id " + "and btdd.domanda_stato_id = bdds.domanda_stato_id "
			+ "and bdds.domanda_stato_cod ='CONTRODEDOTTA' " + "and btd.data_cancellazione is null "
			+ "and btdd.data_cancellazione is null " + "and btd.domanda_numero = :numeroDomanda";
	
	public static final String SELECT_NOTA_ISEE = "select btbi.isee_note "
			+ "from bres_t_buono_isee btbi, bres_t_buono btb, bres_t_domanda btd "
			+ "where btbi.buono_id = btb.buono_id "
			+ "and btb.domanda_id = btd.domanda_id "
			+ "and btd.domanda_numero = :numeroDomanda "
			+ "and btbi.data_cancellazione is null "
			+ "and btb.data_cancellazione is null "
			+ "and btd.data_cancellazione is null "
			+ "and btbi.validita_fine is null ";

	public static final String SELECT_INCOMPATIBILITA_DOMANDA = "select nvl(btdd.incompatibilita_per_contratto,false) incompatibilita_per_contratto ,btdd.nessuna_incompatibilita "
			+ "from bres_t_domanda_dettaglio btdd , bres_t_domanda btd " + "where btdd.domanda_id = btd.domanda_id "
			+ "and btd.data_cancellazione is null " + "and btdd.data_cancellazione is null "
			+ "and btdd.validita_fine is null " + "and btd.domanda_numero = :numeroDomanda ";

	public static final String SELECT_PENULTIMO_STATO = "with tab1 as ( "
			+ "select a.domanda_det_cod, max(a.validita_inizio) validita_inizio from bres_t_domanda_dettaglio a, "
			+ "bres_t_domanda b,bres_d_domanda_stato c " + "where a.domanda_id = b.domanda_id "
			+ "and b.domanda_numero  = :numeroDomanda " + "and c.domanda_stato_id = a.domanda_stato_id "
			+ "and a.validita_fine is not null " + "and a.data_cancellazione is null "
			+ "and c.data_cancellazione is null " + "and b.data_cancellazione is null " + "and c.validita_fine is null "
			+ "group by a.domanda_det_cod " + "order by validita_inizio desc " + "limit 2) "
			+ "select domanda_det_cod from tab1 " + "order by row_number() OVER () desc " + "limit 1";

	public static final String SELECT_ALLEGATI_PROT_PARTENZA = "select bta.file_path,bta.file_name,bta.allegato_id,bta.sportello_id,bta.allegato_tipo_id,bdat.allegato_tipo_cod,"
			+ " bdat.allegato_tipo_desc, bta.file_type " + " from bres_t_allegato bta, bres_d_allegato_tipo bdat  "
			+ " where  " + " bta.domanda_det_id = :domandaID " + " and bdat.allegato_tipo_id = bta.allegato_tipo_id "
			+ " and bdat.data_cancellazione is null " + " and bta.data_cancellazione is null "
			+ " and bdat.allegato_tipo_cod = :tipoLettera ";
	
    public static final String SELECT_NUMERO_RICHIESTA_RECUPERO_STARDAS_PARTENZA = "select btd.domanda_numero,btdd.domanda_det_id,btdd.note "
            + "    from bres_d_contributo_tipo bdct2, "
            + "    bres_t_domanda btd, "
            + "    bres_t_domanda_dettaglio btdd  "
            + "    left join bres_d_rapporto_tipo bdrt on  btdd.rapporto_tipo_id = bdrt.rapporto_tipo_id "
            + " left join bres_d_rapporto_tipo relazione on  btdd.relazione_tipo_id = relazione.rapporto_tipo_id "
            + "    left join bres_d_domanda_stato bdds on bdds.domanda_stato_id  = btdd.domanda_stato_id "
            + "    left join bres_d_contratto_tipo bdct on btdd.contratto_tipo_id = bdct.contratto_tipo_id "
            + " left join bres_d_assistente_tipo bdat on btdd.assistente_tipo_id= bdat.assistente_tipo_id "
            + " left join bres_d_valutazione_multidimensionale bdvm on btdd.valutazione_multidimensionale_id= bdvm.valutazione_multidimensionale_id,   "
            + "    bres_d_titolo_studio bdts, "
            + "    bres_d_asl bda "
            + "    where bdct2.contributo_tipo_id = btd.contributo_tipo_id "
            + "    and "
            + "    btd.data_cancellazione is null "
            + "    and  "
            + "    btdd.domanda_id = btd.domanda_id  "
            + "    and  "
            + "    btdd.data_cancellazione is null  "
            + "    and  "
            + "    btdd.validita_inizio <= now() "
            + "    and "
            + "    btdd.validita_fine is null   "
            + "    and  "
            + "    btdd.titolo_studio_id=bdts.titolo_studio_id  "
            + "    and  "
            + "    btdd.asl_id = bda.asl_id "
            + " and bdds.domanda_stato_cod = :domanda_stato_cod "
            + " and btdd.protocollo_cod is null";

    private static final String SELECT_TIPI_ALLEGATI = "select allegato_tipo_cod as codice ,allegato_tipo_desc as descrizione "
            + "from bres_d_allegato_tipo "
            + "where now()::date BETWEEN validita_inizio::date and COALESCE(validita_fine::date, now()::date) and data_cancellazione is null";
    
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
			String methodName = "SELECT_RICHIESTE_DETTAGLIO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}
	
	// TAG PROD DOM 2.1.0-001
	public ModelIncompatibilitaRichiesta selectIncompatibilitaRichiesta(String numeroRichiesta)
			throws DatabaseException {
		ModelIncompatibilitaRichiesta incompatibilita = new ModelIncompatibilitaRichiesta();

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroRichiesta);
		try {
			incompatibilita = jdbcTemplate.queryForObject(SELECT_INCOMPATIBILITA_DOMANDA, namedParameters,
					new IncompatibilitaMapper());
			return incompatibilita;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_INCOMPATIBILITA_DOMANDA";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public boolean selectEsisteAllegato(String detcod, String tipodom) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("detcod", detcod).addValue("tipodom",
				tipodom);
		try {
			Integer numallegati = jdbcTemplate.queryForObject(SELECT_DET_COD_ALLEGATO, namedParameters, Integer.class);
			if (numallegati.intValue() > 0)
				return true;
			else
				return false;
		} catch (EmptyResultDataAccessException e) {
			return false;
		} catch (Exception e) {
			String methodName = "SELECT_DET_COD_ALLEGATO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public long updateAllegato(String detcod, String cfmodifica, String nomefile, String tipofile, String pathfile,
			String tipodom) throws DatabaseException {

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("cfmodifica", cfmodifica, Types.VARCHAR).addValue("detcod", detcod, Types.VARCHAR)
				.addValue("nomefile", nomefile, Types.VARCHAR).addValue("tipofile", tipofile, Types.VARCHAR)
				.addValue("pathfile", pathfile, Types.VARCHAR).addValue("tipodom", tipodom);
		return jdbcTemplate.update(UPDATE_DOMANDA_ALLEGATO, params);
	}

	public long insertAllegato(String nomefile, String tipofile, String pathfile, BigDecimal sportelloid,
			BigDecimal domandadetid, String domandadetcod, String allegatotipocod, String cfinserisci,
			String cfmodifica) throws DatabaseException {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue("file_name", nomefile, Types.VARCHAR);
		params.addValue("file_type", tipofile, Types.VARCHAR);
		params.addValue("file_path", pathfile, Types.VARCHAR);
		params.addValue("sportello_id", sportelloid, Types.BIGINT);
		params.addValue("domanda_det_id", domandadetid, Types.BIGINT);

		params.addValue("domanda_det_cod", domandadetcod, Types.VARCHAR);
		params.addValue("allegatotipocod", allegatotipocod, Types.VARCHAR);
		params.addValue("utente_creazione", cfinserisci, Types.VARCHAR);
		params.addValue("utente_modifica", cfmodifica, Types.VARCHAR);

		jdbcTemplate.update(INSERT_DOMANDA_ALLEGATO, params, keyHolder, new String[] { "allegato_id" });
		return keyHolder.getKey().longValue();
	}

	public List<ModelGetAllegatoExt> selectAllegati(BigDecimal idDomanda) throws DatabaseException {

		List<ModelGetAllegatoExt> allegati = new ArrayList<ModelGetAllegatoExt>();
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("domandaID", idDomanda);
		try {
			allegati = jdbcTemplate.query(SELECT_ALLEGATI, namedParameters, new AllegatoExtMapper());

			return allegati;

		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		} catch (Exception e) {
			String methodName = "SELECT_ALLEGATI";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public ModelGetAllegato selectGetAllegato(String tipoAllegato, BigDecimal idDomanda) throws DatabaseException {

		ModelGetAllegato allegato = new ModelGetAllegato();
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("domandaID", idDomanda)
				.addValue("allegatoCod", tipoAllegato);
		try {
			allegato = jdbcTemplate.queryForObject(SELECT_GET_ALLEGATO, namedParameters, new AllegatoGetMapper());

			return allegato;

		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_ALLEGATO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public ModelGetAllegatoExt selectAllegatiProtPartenza(BigDecimal idDomanda, String tipoLettera)
			throws DatabaseException {

		ModelGetAllegatoExt allegati = new ModelGetAllegatoExt();
		;
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("domandaID", idDomanda)
				.addValue("tipoLettera", tipoLettera);
		try {
			allegati = jdbcTemplate.queryForObject(SELECT_ALLEGATI_PROT_PARTENZA, namedParameters,
					new AllegatoExtMapper());

			return allegati;

		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_ALLEGATI_PROT_PARTENZA";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public long updateMessageUUID(String domandadetcod, String messageuuid) throws DatabaseException {
		try {

			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("domandadetcod", domandadetcod, Types.VARCHAR);
			params.addValue("messageuuid", messageuuid, Types.VARCHAR);

			return jdbcTemplate.update(UPDATE_DETTAGLIO_DOMANDA_MESSAGEUUID, params);
		} catch (Exception e) {
			String methodName = "UPDATE_DETTAGLIO_DOMANDA_MESSAGEUUID";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public String selectStatoPenultimo(String numeroRichiesta) throws DatabaseException {
		String dettaglio = null;

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroRichiesta);
		try {
			dettaglio = jdbcTemplate.queryForObject(SELECT_PENULTIMO_STATO, namedParameters, String.class);
			return dettaglio;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_PENULTIMO_STATO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

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

	public String selectDataControdedotta(String numeroDomanda) throws DatabaseException {
		String datacontrodedotta = null;

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda);
		try {
			datacontrodedotta = jdbcTemplate.queryForObject(SELECT_DATA_CONTRODEDOTTA, namedParameters, String.class);
			return datacontrodedotta;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_DATA_CONTRODEDOTTA";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}
	
	
	
	public List<ModelRichiestaRecupero> selectNumeroRichiesteRecupero(String stato) throws DatabaseException {
	    List<ModelRichiestaRecupero> richieste = new ArrayList<ModelRichiestaRecupero>();
	    
	    SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("domanda_stato_cod", stato);
	    try {
	        richieste = jdbcTemplate.query(SELECT_NUMERO_RICHIESTA_RECUPERO_STARDAS_PARTENZA, namedParameters,
	        		new DettaglioRichiestaRecuperoMapper());
	        return richieste;
	    } catch (EmptyResultDataAccessException e) {
	        return null; 
	    } catch (Exception e) {
	        String methodName = "SELECT_NUMERO_RICHIESTA_RECUPERO_STARDAS_PARTENZA";
	        logError(methodName, e.getMessage());
	        throw new DatabaseException(e);
	    }
	}
	
	public List<ModelDecodifica> selectTipiAllegati() throws DatabaseException {
		List<ModelDecodifica> lista = new ArrayList<ModelDecodifica>();

		try {
			lista = jdbcTemplate.query(SELECT_TIPI_ALLEGATI, new ListaDecodificaMapper());
			return lista;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_TIPI_ALLEGATI";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);

		}
	}

}
