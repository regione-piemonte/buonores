/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.integration.dao.custom;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import it.csi.buonores.buonoresbff.dto.DDomandaStato;
import it.csi.buonores.buonoresbff.dto.ModelAllegato;
import it.csi.buonores.buonoresbff.dto.ModelBozzaRichiesta;
import it.csi.buonores.buonoresbff.dto.ModelRichiesta;
import it.csi.buonores.buonoresbff.dto.ModelStruttura;
import it.csi.buonores.buonoresbff.dto.custom.ModelRichiestaExt;
import it.csi.buonores.buonoresbff.dto.custom.ModelRichiestaLight;
import it.csi.buonores.buonoresbff.dto.custom.ModelRichiesteExt;
import it.csi.buonores.buonoresbff.dto.custom.ModelUpdateCronologia;
import it.csi.buonores.buonoresbff.exception.DatabaseException;
import it.csi.buonores.buonoresbff.integration.dao.utils.AllegatoTipoMapper;
import it.csi.buonores.buonoresbff.integration.dao.utils.DDomandaStatoMapper;
import it.csi.buonores.buonoresbff.integration.dao.utils.DettaglioRichiestaExtMapper;
import it.csi.buonores.buonoresbff.integration.dao.utils.DettaglioRichiestaLightMapper;
import it.csi.buonores.buonoresbff.integration.dao.utils.DettaglioRichiestaMapper;
import it.csi.buonores.buonoresbff.integration.dao.utils.RichiesteMapper;
import it.csi.buonores.buonoresbff.util.LoggerUtil;
import it.csi.buonores.buonoresbff.util.Util;

@Repository
public class RichiesteDao extends LoggerUtil {

	public static final String SELECT_RICHIESTE = "select " + "	btd.domanda_numero, " + "	bdds.domanda_stato_cod, "
			+ "	bdds.domanda_stato_desc, " + "	btdd.data_creazione, " + "	btd.richiedente_cf, "
			+ "	btdd.richiedente_nome, " + "	btdd.richiedente_cognome, " + "	btdd.richiedente_nascita_data, "
			+ "	btdd.richiedente_nascita_stato, " + "	btdd.richiedente_nascita_comune, "
			+ "	btdd.richiedente_nascita_provincia, "
			+ "	convert_from(pgp_sym_decrypt_bytea(btdd.richiedente_residenza_indirizzo::bytea,'@dbKeyCrypt@'),'UTF8') as richiedente_residenza_indirizzo, "
			+ "	btdd.richiedente_residenza_comune, " + "	btdd.richiedente_residenza_provincia, "
			+ "	btd.beneficiario_cf, " + "	btdd.destinatario_nome, " + "	btdd.destinatario_cognome, "
			+ "	btdd.destinatario_nascita_data, " + "	btdd.destinatario_nascita_stato, "
			+ "	btdd.destinatario_nascita_comune, " + "	btdd.destinatario_nascita_provincia, "
			+ "	convert_from(pgp_sym_decrypt_bytea(btdd.destinatario_residenza_indirizzo::bytea,'@dbKeyCrypt@'),'UTF8') as destinatario_residenza_indirizzo, "
			+ "	btdd.destinatario_residenza_comune, " + "	btdd.destinatario_residenza_provincia, " + "	btdd.note, "
			+ "	btdd.protocollo_cod, " + "	btdd.data_protocollo, " + "	btdd.tipo_protocollo, "
			+ " case when btb.buono_id is not null then true "
			+ " else false "
			+ "    end esistebuono "
			+ "from "
			+ "	bres_t_domanda_dettaglio btdd, " + "	bres_d_domanda_stato bdds, " + "	bres_t_domanda btd "
			+ " left join bres_t_buono btb on  btb.domanda_id = btd.domanda_id and btb.sportello_id = btd.sportello_id and btb.data_cancellazione is null "
			+ "where " + "	btd.richiedente_cf = :codFiscale " + "	and " + "	btd.data_cancellazione is null "
			+ "	and  " + "	btdd.domanda_id = btd.domanda_id  " + "	and  " + "	btdd.data_cancellazione is null  "
			+ "	and  " + "	btdd.validita_inizio <= now() " + " and  "
			+ "	bdds.domanda_stato_id  =btdd.domanda_stato_id " + "	and " + "	btdd.validita_fine is null ";

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
			+ "	btdd.rsa_sede_legale_comune," + "	btdd.rsa_sede_legale_provincia," + "	btdd.titolo_privato, "
			+ "  btdd.note_richiedente " // MODIFICA TAG 003 buonodom
			+ "	from " + "	bres_t_domanda btd, " + "	bres_t_domanda_dettaglio btdd  "
			+ "	left join bres_d_rapporto_tipo bdrt on  btdd.rapporto_tipo_id = bdrt.rapporto_tipo_id "
			+ " left join bres_d_rapporto_tipo relazione on  btdd.relazione_tipo_id = relazione.rapporto_tipo_id "
			+ "	left join bres_d_domanda_stato bdds on bdds.domanda_stato_id  = btdd.domanda_stato_id "
			+ "	left join bres_d_contratto_tipo bdct on btdd.contratto_tipo_id = bdct.contratto_tipo_id "
			+ " left join bres_d_valutazione_multidimensionale bdvm on btdd.valutazione_multidimensionale_id= bdvm.valutazione_multidimensionale_id,   "
			+ "	bres_d_titolo_studio bdts, " + "	bres_d_asl bda " + "	where " + "	btd.data_cancellazione is null "
			+ "	and  " + "	btdd.domanda_id = btd.domanda_id  " + "	and  " + "	btdd.data_cancellazione is null  "
			+ "	and  " + "	btdd.validita_inizio <= now() " + "	and " + "	btdd.validita_fine is null   " + "	and  "
			+ "	btdd.titolo_studio_id=bdts.titolo_studio_id  " + "	and  " + "	btdd.asl_id = bda.asl_id 	 "
			+ "	and " + "	btd.domanda_numero= :numeroDomanda";

	// MODIFICA TAG 71 buonodom
	public static final String SELECT_ALLEGATI = "select bta.allegato_id, " + "	bdat.allegato_tipo_cod,"
			+ " bta.file_name " + "	from  " + "	bres_t_domanda_dettaglio btdd, " + "	bres_d_allegato_tipo bdat, "
			+ "	bres_t_allegato bta  " + "	where  " + "	btdd.domanda_det_id = bta.domanda_det_id " + "	and  "
			+ "	btdd.sportello_id = bta.sportello_id " + "	and  " + "	bta.allegato_tipo_id = bdat.allegato_tipo_id "
			+ "	and btdd.domanda_det_id = :idDomanda" + " and btdd.validita_inizio <= now() "
			+ " and btdd.validita_fine is null " + " and btdd.data_cancellazione is null "
			+ " and bta.data_cancellazione is null ";

	public static final String SELECT_ID_DOMANDA = "select " + " btdd.domanda_det_id   "
			+ " from bres_t_domanda_dettaglio btdd, " + " bres_t_domanda btd  " + " where  "
			+ " btdd.domanda_id = btd.domanda_id " + " and  " + " btd.domanda_numero = :numeroRichiesta "
			+ " and btd.data_cancellazione is null " + " and btdd.validita_inizio <= now() "
			+ " and btdd.validita_fine is null " + " and btdd.data_cancellazione is null";

	public static final String SELECT_DETCOD_DOMANDA = "select " + " btdd.domanda_det_cod   "
			+ " from bres_t_domanda_dettaglio btdd, " + " bres_t_domanda btd  " + " where  "
			+ " btdd.domanda_id = btd.domanda_id " + " and  " + " btd.domanda_numero = :numeroRichiesta "
			+ " and btd.data_cancellazione is null " + " and btdd.validita_inizio <= now() "
			+ " and btdd.validita_fine is null " + " and btdd.data_cancellazione is null";

	public static final String SELECT_NUMERO_DOMANDA_PRINCIPALE = "select " + " btd.domanda_numero  " + " from  "
			+ " bres_t_domanda btd  " + " where  " + " btd.domanda_id = :idDomanda"
			+ " and btd.data_cancellazione is null ";

	public static final String SELECT_NOME_CAMPO_DA_MODIFICARE = "select distinct a.riferimento_yaml_campo from "
			+ "bres_d_campo_modificabile a, " + "bres_t_dato_da_modificare b, " + "bres_t_domanda_dettaglio c, "
			+ "bres_t_domanda d " + "where c.domanda_det_id = b.domanda_det_id "
			+ "and d.domanda_numero  = :numeroRichiesta " + "and b.campo_modificabile_id = a.campo_modificabile_id "
			+ "and b.sportello_id = c.sportello_id " + "and d.domanda_id  = c.domanda_id "
			+ "and a.data_cancellazione is null " + "and b.data_cancellazione is null "
			+ "and c.data_cancellazione is null " + "and d.data_cancellazione is null "
			+ "and a.validita_inizio <= now() " + "and a.validita_fine is null " + "and c.validita_inizio <= now() "
			+ "and c.validita_fine is null " + "union " + "select distinct a.riferimento_yaml_campo from "
			+ "bres_d_allegato_tipo_modificabile a, " + "bres_t_dato_da_modificare b, " + "bres_t_domanda_dettaglio c, "
			+ "bres_t_domanda d, " + "bres_t_allegato e " + "where c.domanda_det_id = b.domanda_det_id "
			+ "and d.domanda_numero  = :numeroRichiesta " + "and b.allegato_id  = e.allegato_id " // MODIFICA TAG 71
																									// buonodom
			+ "and a.allegato_tipo_id=e.allegato_tipo_id " + "and e.domanda_det_id = c.domanda_det_id "
			+ "and b.sportello_id = c.sportello_id " + "and d.domanda_id  = c.domanda_id "
			+ "and a.data_cancellazione is null " + "and b.data_cancellazione is null "
			+ "and c.data_cancellazione is null " + "and d.data_cancellazione is null "
			+ "and e.data_cancellazione is null " + "and a.validita_inizio <= now() " + "and a.validita_fine is null "
			+ "and c.validita_inizio <= now() " + "and c.validita_fine is null";

	public static final String SELECT_ID_STATO = "select bdds.domanda_stato_id " + "from bres_d_domanda_stato bdds "
			+ "where bdds.domanda_stato_cod = :codStato " + "AND bdds.data_cancellazione is null "
			+ "and now()::date BETWEEN validita_inizio::date and COALESCE(validita_fine::date, now()::date)";

	public static final String SELECT_ID_CONTRIBUTO = "select bdct.contributo_tipo_id "
			+ "from bres_d_contributo_tipo bdct " + "where bdct.contributo_tipo_cod = :codContributo "
			+ "AND bdct.data_cancellazione is null "
			+ "and now()::date BETWEEN validita_inizio::date and COALESCE(validita_fine::date, now()::date)";

	// Modifica TAG 2v004 buonodom
	// Modifica TAG 2.1.0v002 buonodom
	public static final String DOMANDA_PRESENTE = "select distinct bdds.domanda_stato_cod not in ('DINIEGO', 'ANNULLATA', 'RINUNCIATA') "
			+ "from bres_t_domanda_dettaglio btdd, " + "bres_t_domanda btd, " + "bres_d_domanda_stato bdds  "
			+ "where  " + "btdd.domanda_id = btd.domanda_id  " + "AND "
			+ "bdds.domanda_stato_id = btdd.domanda_stato_id  " + "and " + "btd.beneficiario_cf = :cfDestinatario "
			+ "and  " + "btd.data_cancellazione is null " + "and  " + "btdd.data_cancellazione is null  " + "and "
			+ "bdds.data_cancellazione is null " + "and " + "btdd.validita_inizio <= now() " + "and  "
			+ "btdd.validita_fine is null " + "and  "
			+ "bdds.domanda_stato_cod not in ('DINIEGO', 'ANNULLATA', 'RINUNCIATA') ";

	public static final String SELECT_ID_SPORTELLI = "select " + " spo.sportello_id idSportello  "
			+ "from bres_t_sportello spo,bres_d_contributo_tipo con  "
			+ "where spo.contributo_tipo_id =con.contributo_tipo_id   " + "and spo.data_cancellazione is null   "
			+ "and con.data_cancellazione is null  " + "and con.contributo_tipo_cod = 'RES'  "
			+ "and  now()::date BETWEEN spo.validita_inizio::date and COALESCE(spo.validita_fine::date, now()::date) ";

	public static final String SELECT_ID_TITOLO = "select bdts.titolo_studio_id  " + "from "
			+ "	bres_d_titolo_studio bdts " + "where " + "	bdts.data_cancellazione is null " + "	and "
			+ "	bdts.titolo_studio_cod = :codTitolo "
			+ "and now()::date BETWEEN validita_inizio::date and COALESCE(validita_fine::date, now()::date)";

	public static final String SELECT_ID_RAPPORTO = "select bdrt.rapporto_tipo_id " + "from "
			+ "	bres_d_rapporto_tipo bdrt  " + "where " + "	bdrt.data_cancellazione is null " + "	and "
			+ "	bdrt.rapporto_tipo_cod = :codRapporto "
			+ "and now()::date BETWEEN validita_inizio::date and COALESCE(validita_fine::date, now()::date)";

	public static final String SELECT_ID_VALUTAZIONE_MULTIDIMENSIONALE = "select bdrt.valutazione_multidimensionale_id "
			+ "from " + "	bres_d_valutazione_multidimensionale bdrt  " + "where "
			+ "	bdrt.data_cancellazione is null " + "	and "
			+ "	bdrt.valutazione_multidimensionale_cod = :codValutazioneMultidimensionale "
			+ "and now()::date BETWEEN validita_inizio::date and COALESCE(validita_fine::date, now()::date)";

	public static final String SELECT_ID_CONTRATTO = "select bdrt.contratto_tipo_id " + "from "
			+ "	bres_d_contratto_tipo bdrt  " + "where " + "	bdrt.data_cancellazione is null " + "	and "
			+ "	bdrt.contratto_tipo_cod = :codContratto "
			+ "and now()::date BETWEEN validita_inizio::date and COALESCE(validita_fine::date, now()::date)";

	public static final String SELECT_ID_ASL = "	select " + "	bda.asl_id  " + "from " + "	bres_d_asl bda "
			+ "where " + "	bda.data_cancellazione is null " + "	and " + "	bda.asl_cod = :codAsl "
			+ "and now()::date BETWEEN validita_inizio::date and COALESCE(validita_fine::date, now()::date)";

	public static final String SELECT_ID_DETTAGLIO_DOMANDA = "select btdd.domanda_det_id  "
			+ "from bres_t_domanda_dettaglio btdd, " + "bres_t_domanda btd  " + "where "
			+ "btd.domanda_numero = :numeroDomanda " + "and btd.domanda_id = btdd.domanda_id  "
			+ "and btdd.validita_inizio <= now() " + "and btdd.validita_fine is null  "
			+ "and btdd.data_cancellazione is null  " + "and btd.data_cancellazione is null;";

	// non utilizzato per nuova tabella provincia
	public static final String RESIDENTE_PIEMONTE = "select :codProvincia in ((select distinct bdc.provincia_sigla_automobilistica as provincia "
			+ "from bres_d_comune bdc  " + "where bdc.data_cancellazione is null  "
			+ "and bdc.validita_inizio <= now() " + "and bdc.validita_fine is null "
			+ "and bdc.regione_istat_cod = '1'))";

	public static final String SELECT_COD_CONTRIBUTO = "select bdct.contributo_tipo_cod "
			+ "from bres_d_contributo_tipo bdct " + "where bdct.contributo_tipo_cod = :codContributo "
			+ "AND bdct.data_cancellazione is null "
			+ "and now()::date BETWEEN validita_inizio::date and COALESCE(validita_fine::date, now()::date)";

	public static final String SELECT_COD_STATO = "select bdds.domanda_stato_cod " + "from bres_d_domanda_stato bdds "
			+ "where bdds.domanda_stato_cod = :codStato " + "AND bdds.data_cancellazione is null "
			+ "and now()::date BETWEEN validita_inizio::date and COALESCE(validita_fine::date, now()::date)";

	public static final String INSERT_T_DOMANDA = "insert " + "	into " + "	bres_t_domanda " + "("
			+ "	richiedente_cf, " + "	beneficiario_cf, " + "	contributo_tipo_id, " + "	sportello_id, "
			+ "	data_creazione, " + "	data_modifica, " + "	utente_creazione, " + "	utente_modifica) " + "values("
			+ ":cfRichiedente, " + ":cfDestinatario, " + ":idTipoContributo, " + ":idSportello, " + "now(), "
			+ "now(), " + ":utenteCrea, " + ":utenteModifica)";

	public static final String INSERT_T_DOMANDA_DETTAGLIO = "insert " + "	into " + "	bres_t_domanda_dettaglio "
			+ "(domanda_id, " + "	sportello_id, " + "	domanda_stato_id, " + "	richiedente_nome, "
			+ "	richiedente_cognome, " + "	richiedente_nascita_data, " + "	richiedente_nascita_stato, "
			+ "	richiedente_nascita_comune, " + "	richiedente_nascita_provincia, "
			+ "	richiedente_residenza_indirizzo, " + "	richiedente_residenza_comune, "
			+ "	richiedente_residenza_provincia, " + "	destinatario_nome, " + "	destinatario_cognome, "
			+ "	destinatario_nascita_data, " + "	destinatario_nascita_comune, "
			+ "	destinatario_nascita_provincia, " + "	destinatario_nascita_stato, "
			+ "	destinatario_residenza_indirizzo, " + "	destinatario_residenza_comune, "
			+ "	destinatario_residenza_provincia, " + "	destinatario_domicilio_indirizzo, "
			+ "	destinatario_domicilio_comune, " + "	destinatario_domicilio_provincia, "
			+ "	situazione_lavorativa_attiva, " + "	note, " + "	rapporto_tipo_id, " + "	titolo_studio_id, "
			+ "	asl_id, " + "	validita_inizio, " + "	data_creazione, " + "	data_modifica, "
			+ "	utente_creazione, " + "	utente_modifica,area_id) " + "values(:idDomanda, " + ":idSportello, "
			+ ":idStatoDomanda, " + ":nomeRichiedente, " + ":cognomeRichiedente, " + ":dataNascitaRichiedente, "
			+ ":statoNascitaRichiedente, " + ":comuneNascitaRichiedente, " + ":provinciaNascitaRichiedente, "
			+ "pgp_sym_encrypt(:indirizzoResidenzaRichiedente, '@dbKeyCrypt@')::bytea, "
			+ ":comuneResidenzaRichiedente, " + ":provinciaResidenzaRichiedente, " + ":nomeDestinatario, "
			+ ":cognomeDestinatario, " + ":dataNascitaDestinatario, " + ":comuneNascitaDestinatario, "
			+ ":provinciaNascitaDestinatario, " + ":statoNascitaDestinatario, "
			+ "pgp_sym_encrypt(:indirizzoResidenzaDestinatario, '@dbKeyCrypt@')::bytea, "
			+ ":comuneResidenzaDestinatario, " + ":provinciaResidenzaDestinatario, "
			+ "pgp_sym_encrypt(:indirizzoDomicilioDestinatario, '@dbKeyCrypt@')::bytea, "
			+ ":comuneDomicilioDestinatario, " + ":provinciaDomicilioDestinatario, " + ":situazioneLavorativa, "
			+ ":note, " + ":idTipoRapporto, " + ":idTitoloStudio, " + ":idAsl, " + "now(), " + "now(), " + "now(), "
			+ ":utenteCrea, "
			+ ":utenteModifica,(select distinct b.area_id from bres_d_comune a, bres_d_area b, bres_r_area_comune c "
			+ "where a.comune_id = c.comune_id " + "and b.area_id=c.area_id "
			+ "and upper(a.comune_desc)=upper(:comuneResidenzaDestinatario) "
			+ "and upper(a.provincia_sigla_automobilistica)=upper(:provinciaResidenzaDestinatario)))";

	public static final String UPDATE_DET_COD = "UPDATE bres_t_domanda_dettaglio " + "SET domanda_det_cod=:detCod "
			+ "WHERE domanda_det_id=:idDomanda";

	public static final String SELECT_NUMERO_RICHIESTA_COD = "select " + " btdd.domanda_det_id, "
			+ " btdd.sportello_id, " + "	btd.domanda_numero, " + " btdd.domanda_det_cod, "
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
			+ "	btdd.incompatibilita_per_contratto,  " + " relazione.rapporto_tipo_cod as relazione_destinatario,"
			+ " btdd.nessuna_incompatibilita," + " btdd.situazione_lavorativa_attiva, "
			+ " bdvm.valutazione_multidimensionale_cod, " + "	btdd.contratto_data_inizio, "
			+ "	btdd.contratto_data_fine," + "	btdd.rsa_cod," + "	btdd.rsa_arpe_cod," + "	btdd.rsa_denominazione,"
			+ "	btdd.rsa_indirizzo," + "	btdd.rsa_comune," + "	btdd.rsa_provincia," + "	btdd.rsa_cfpiva,"
			+ "	btdd.rsa_sede_legale_denominazione,"
			+ "	btdd.rsa_sede_legale_indirizzo," + "	btdd.rsa_sede_legale_comune,"
			+ "	btdd.rsa_sede_legale_provincia,	" + " btdd.titolo_privato, " + "	btdd.isee_conforme, "
			+ "  btdd.note_richiedente " // MODIFICA TAG 003 buonodom
			+ "	from " + "	bres_t_domanda btd, " + "	bres_t_domanda_dettaglio btdd  "
			+ "	left join bres_d_rapporto_tipo bdrt on  btdd.rapporto_tipo_id = bdrt.rapporto_tipo_id "
			+ " left join bres_d_rapporto_tipo relazione on  btdd.relazione_tipo_id = relazione.rapporto_tipo_id "
			+ "	left join bres_d_domanda_stato bdds on bdds.domanda_stato_id  = btdd.domanda_stato_id "
			+ "	left join bres_d_contratto_tipo bdct on btdd.contratto_tipo_id = bdct.contratto_tipo_id "
			+ " left join bres_d_valutazione_multidimensionale bdvm on btdd.valutazione_multidimensionale_id= bdvm.valutazione_multidimensionale_id, "
			+ "	bres_d_titolo_studio bdts, " + "	bres_d_asl bda " + "	where " + "	btd.data_cancellazione is null "
			+ "	and  " + "	btdd.domanda_id = btd.domanda_id  " + "	and  " + "	btdd.data_cancellazione is null  "
			+ "	and  " + "	btdd.validita_inizio <= now() " + "	and " + "	btdd.validita_fine is null   " + "	and  "
			+ "	btdd.titolo_studio_id=bdts.titolo_studio_id  " + "	and  " + "	btdd.asl_id = bda.asl_id 	 "
			+ "	and " + "	btd.domanda_numero= :numeroDomanda";

	public static final String SELECT_RICHIESTA_LIGHT = "select btdd.domanda_det_id, "
			+ " btdd.sportello_id, btdd.domanda_det_cod, btd.richiedente_cf "
			+ "	from bres_t_domanda btd, bres_t_domanda_dettaglio btdd  "
			+ "	where btd.data_cancellazione is null "
			+ "	and btd.domanda_numero= :numeroDomanda"
			+ "	and btdd.domanda_id = btd.domanda_id  "
			+ "	and btdd.data_cancellazione is null  "
			+ "	and btdd.validita_inizio <= now() "
			+ "	and btdd.validita_fine is null   ";

	public static final String UPDATE_T_DETTAGLIO_DOMANDA = "update buonores.bres_t_domanda_dettaglio " + "set "
			+ "	punteggio_sociale = :punteggioSociale, "
			+ "	nessuna_incompatibilita = :nessunaIncompatibilita, "
			+ "	incompatibilita_per_contratto = :incompatibilitaPerContratto, "
			+ "	destinatario_domicilio_indirizzo = pgp_sym_encrypt(:indirizzoDomicilioDestinatario, '@dbKeyCrypt@')::bytea, "
			+ "	destinatario_domicilio_comune = :comuneDomicilioDestinatario, "
			+ "	destinatario_domicilio_provincia = :provinciaDomicilioDestinatario, "
			+ "	situazione_lavorativa_attiva = :situazioneLavorativa, "
			+ "	datore_di_lavoro_nome = :datoreNome, "
			+ "	datore_di_lavoro_cognome = :datoreCognome, "
			+ "	datore_di_lavoro_cf = :datoreCf, "
			+ "	datore_di_lavoro_nascita_data = :datoreNascitaData, "
			+ "	datore_di_lavoro_nascita_comune = :datoreNascitaComune, "
			+ "	datore_di_lavoro_nascita_provincia = :datoreNascitaProvincia, "
			+ "	datore_di_lavoro_nascita_stato = :datoreNascitaStato, "
			+ "	note = :note, "
			+ "	note_richiedente = :noterichiedente, " // MODIFICA TAG 003 buonodom
			+ "	contratto_tipo_id = :idContratto, "
			+ "	rapporto_tipo_id = :idRapporto, "
			+ "	titolo_studio_id = :idTitoloStudio, "
			+ "	asl_id = :idAsl, "
			+ "	data_modifica = now(), "
			+ "	utente_modifica = :utenteModifica, "
			+ "	relazione_tipo_id = :idRelazione, "
			+ "	isee_conforme = :iseeConforme, "
			+ "	valutazione_multidimensionale_id = :idValutazione, "
			+ "	contratto_data_inizio = :dataInizioContratto, "
			+ "	contratto_data_fine = :dataFineContratto, "
			+ " titolo_privato = :titoloPrivato, "
			+ " rsa_cod = :rsaCod,"
			+ " rsa_arpe_cod =  :rsaArpeCod ,"
			+ " rsa_denominazione = :rsaDenominazione ,"
			+ " rsa_indirizzo = :rsaIndirizzo ,"
			+ " rsa_comune = :rsaComune ,"
			+ " rsa_provincia = :rsaProvincia ,"
			+ " rsa_cfpiva = :rsaCfPiva ,"
			+ " rsa_sede_legale_denominazione = :rsaSedeLegaleDenominazione ,"
			+ " rsa_sede_legale_indirizzo = :rsaSedeLegaleIndirizzo ,"
			+ " rsa_sede_legale_comune= :rsaSedeLegaleComune ,"
			+ " rsa_sede_legale_provincia= :rsaSedeLegaleProvincia "
			+ " where "
			+ "	domanda_det_id = :idDettaglio;";

	public static final String UPDATE_DATA_FINE_VAL_DETTAGLIO_DOMANDA = "UPDATE bres_t_domanda_dettaglio "
			+ "SET validita_fine = now() " + "WHERE domanda_det_id=:idDomanda";

	public static final String UPDATE_DATA_DOMANDA_VAL_DOMANDA = "UPDATE bres_t_domanda " + "SET domanda_data = now(),"
			+ " data_modifica = now(), " + " utente_modifica = :codFiscale " + "WHERE domanda_numero=:numeroRichiesta";

	private static final String SELECT_PROVINCIA_PIEMONTE = "select count(*) from bres_d_provincia "
			+ "where provincia_sigla_automobilistica = :sigla " + "and data_cancellazione is null "
			+ "and now()::date BETWEEN validita_inizio::date and COALESCE(validita_fine::date, now()::date)";

	// MODIFICA TAG 003 buonodom
	// Modifica TAG 2.1.0v002 buonodom
	public static final String INSERT_DETTAGLIO_DOMANDA = "INSERT INTO bres_t_domanda_dettaglio (domanda_id, sportello_id, domanda_stato_id, isee_valore, isee_data_rilascio, "
			+ "isee_scadenza, punteggio_sociale, nessuna_incompatibilita, incompatibilita_per_contratto, richiedente_nome, richiedente_cognome, richiedente_nascita_data, "
			+ "richiedente_nascita_stato, richiedente_nascita_comune, richiedente_nascita_provincia, richiedente_residenza_indirizzo, richiedente_residenza_comune, "
			+ "richiedente_residenza_provincia, destinatario_nome, destinatario_cognome, destinatario_nascita_data, destinatario_nascita_comune, "
			+ "destinatario_nascita_provincia, destinatario_nascita_stato, destinatario_residenza_indirizzo, destinatario_residenza_comune, "
			+ "destinatario_residenza_provincia, destinatario_domicilio_indirizzo, destinatario_domicilio_comune, destinatario_domicilio_provincia, "
			+ "situazione_lavorativa_attiva, datore_di_lavoro_nome, datore_di_lavoro_cognome, datore_di_lavoro_cf, datore_di_lavoro_nascita_data, "
			+ "datore_di_lavoro_nascita_comune, datore_di_lavoro_nascita_provincia, datore_di_lavoro_nascita_stato, "
			+ "protocollo_cod, note, contratto_tipo_id, rapporto_tipo_id, titolo_studio_id, area_id, asl_id, ruolo_cod, "
			+ "verifica_eg_richiesta, verifica_eg_in_corso, verifica_eg_conclusa, verifica_eg_punteggio_sociale, verifica_eg_incompatibilita, "
			+ "relazione_tipo_id, isee_conforme, valutazione_multidimensionale_id, data_protocollo, tipo_protocollo, contratto_data_inizio, contratto_data_fine, "
			+ "messageuuid_protocollo, nota_interna,isee_verificato_conforme,isee_verificato_in_data, rsa_cod, rsa_arpe_cod, rsa_denominazione, rsa_indirizzo, rsa_comune, "
			+ "rsa_provincia, rsa_cfpiva, rsa_sede_legale_denominazione, rsa_sede_legale_indirizzo, rsa_sede_legale_comune, "
			+ "rsa_sede_legale_provincia, "
			+ "validita_inizio, validita_fine, data_creazione, data_modifica, data_cancellazione, utente_creazione, utente_modifica, "
			+ "utente_cancellazione, titolo_privato, note_richiedente, verifica_eg_data, note_regione, note_ente_gestore, destinatario_decesso_data, destinatario_residenza_extraregione) "
			+ "select domanda_id, sportello_id, (select domanda_stato_id  from bres_d_domanda_stato "
			+ "where domanda_stato_cod =:domandastatocod), isee_valore, isee_data_rilascio, "
			+ "isee_scadenza, punteggio_sociale, nessuna_incompatibilita, incompatibilita_per_contratto, richiedente_nome, richiedente_cognome, richiedente_nascita_data, "
			+ "richiedente_nascita_stato, richiedente_nascita_comune, richiedente_nascita_provincia, richiedente_residenza_indirizzo, richiedente_residenza_comune, "
			+ "richiedente_residenza_provincia, destinatario_nome, destinatario_cognome, destinatario_nascita_data, destinatario_nascita_comune, "
			+ "destinatario_nascita_provincia, destinatario_nascita_stato, destinatario_residenza_indirizzo, destinatario_residenza_comune, "
			+ "destinatario_residenza_provincia, destinatario_domicilio_indirizzo, destinatario_domicilio_comune, destinatario_domicilio_provincia, "
			+ "situazione_lavorativa_attiva, datore_di_lavoro_nome, datore_di_lavoro_cognome, datore_di_lavoro_cf, datore_di_lavoro_nascita_data, "
			+ "datore_di_lavoro_nascita_comune, datore_di_lavoro_nascita_provincia, datore_di_lavoro_nascita_stato, "
			+ "null, null, contratto_tipo_id, rapporto_tipo_id, titolo_studio_id, area_id, asl_id, ruolo_cod, "
			+ "verifica_eg_richiesta, verifica_eg_in_corso, verifica_eg_conclusa, verifica_eg_punteggio_sociale, verifica_eg_incompatibilita, "
			+ "relazione_tipo_id, isee_conforme, valutazione_multidimensionale_id, null, null, contratto_data_inizio, contratto_data_fine, "
			+ "null, null, isee_verificato_conforme,isee_verificato_in_data, rsa_cod, rsa_arpe_cod, rsa_denominazione, rsa_indirizzo, rsa_comune, "
			+ "rsa_provincia, rsa_cfpiva, rsa_sede_legale_denominazione, rsa_sede_legale_indirizzo, rsa_sede_legale_comune, "
			+ "rsa_sede_legale_provincia, "
			+ "now(), validita_fine, now(), now(), data_cancellazione, :utenteCreazione, :utenteModifica, "
			+ "utente_cancellazione, titolo_privato, note_richiedente, "
			+ "verifica_eg_data, null, note_ente_gestore, destinatario_decesso_data, destinatario_residenza_extraregione "
			+ "from bres_t_domanda_dettaglio where domanda_det_id =:domandadetid";

	public static final String SELECT_D_DOMANDA_STATO_BY_COD = "select bdds.domanda_stato_id, bdds.domanda_stato_cod "
			+ "from bres_d_domanda_stato bdds " + "where bdds.domanda_stato_cod = :codStato "
			+ "AND bdds.data_cancellazione is null "
			+ "and now()::date BETWEEN validita_inizio::date and COALESCE(validita_fine::date, now()::date)";

	public static final String UPDATE_CAMPO_DA_MODIFICARE = "update bres_t_dato_da_modificare set "
			+ "domanda_det_id = :domandadetidnew " + "where domanda_det_id = :domandadetidold ";

	public static final String UPDATE_ALLEGATO_ID_DA_MODIFICARE = "update bres_t_dato_da_modificare set "
			+ "allegato_id = :allegatoidnew "
			+ "where allegato_id = :allegatoidold and domanda_det_id = :domandadetidnew";

	public static final String SELECT_NOTA_CITTADINO = "with tab1 as (select btdd.note,btdd.domanda_det_id  from "
			+ "bres_t_domanda btd, " + "bres_t_domanda_dettaglio btdd " + "where btd.domanda_numero = :numeroDomanda "
			+ "and btd.domanda_id = btdd.domanda_id " + "and note is not null " + "order by 2 desc "
			+ "limit 1) select note from tab1";

	private static final String SELECT_GRADUATORIA_ID_BY_SPORTELLO_ID = "select (case when (select count(btg.graduatoria_id) from bres_t_graduatoria btg, bres_r_graduatoria_stato brgs  "
			+ "            where btg.graduatoria_id = brgs.graduatoria_id "
			+ "            and btg.data_cancellazione is null "
			+ "            and brgs.data_cancellazione is null  "
			+ "            and now() between coalesce(brgs.validita_inizio, now()) and coalesce(brgs.validita_fine,now()) "
			+ "            and btg.sportello_id = :idSportello) > 0 then "
			+ "(select btg.graduatoria_id from bres_t_graduatoria btg, bres_r_graduatoria_stato brgs  "
			+ "            where btg.graduatoria_id = brgs.graduatoria_id "
			+ "            and btg.data_cancellazione is null "
			+ "            and brgs.data_cancellazione is null  "
			+ "            and now() between coalesce(brgs.validita_inizio, now()) and coalesce(brgs.validita_fine,now()) "
			+ "            and btg.sportello_id = :idSportello) "
			+ "else 0 end) ";

	private static final String CHECK_STATO_GRADUATORIA_BY_ID = "select ( "
			+ "    case  "
			+ "        when ( "
			+ "            select bdgs.graduatoria_stato_cod  "
			+ "            from bres_r_graduatoria_stato brgs, bres_d_graduatoria_stato bdgs "
			+ "            where bdgs.graduatoria_stato_id  = brgs.graduatoria_stato_id "
			+ "         and now() between coalesce(brgs.validita_inizio, now()) and coalesce(brgs.validita_fine,now())"
			+ "            and brgs.graduatoria_id  = :idGraduatoria "
			+ "        ) = :statoGraduatoria "
			+ "        then true "
			+ "        else false "
			+ "    end " + ") as controllo ";

	private static final String UPDATE_R_GRADUATORIA_STATO = "UPDATE bres_r_graduatoria_stato   "
			+ "set validita_fine = now() "
			+ "where now() between coalesce (validita_inizio, now()) and coalesce(validita_fine,now()) "
			+ "and data_cancellazione is null "
			+ "and graduatoria_id = :idGraduatoria; " + "                 "
			+ "INSERT INTO bres_r_graduatoria_stato (graduatoria_id, graduatoria_stato_id, utente_creazione, utente_modifica) "
			+ "values (:idGraduatoria, "
			+ "(select graduatoria_stato_id  from bres_d_graduatoria_stato  "
			+ "where graduatoria_stato_cod = :statoGraduatoria " + "and data_cancellazione is null  "
			+ "and now() between coalesce (validita_inizio, now()) and coalesce(validita_fine,now()) "
			+ "), :utenteCreazione, :utenteModifica) ";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public List<ModelRichiesteExt> selectRichieste(String codFiscale) throws DatabaseException {
		List<ModelRichiesteExt> richieste = new ArrayList<ModelRichiesteExt>();

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codFiscale", codFiscale);
		try {
			richieste = jdbcTemplate.query(SELECT_RICHIESTE, namedParameters, new RichiesteMapper());
			return richieste;
		} catch (Exception e) {
			generateDatabaseException("SELECT_RICHIESTE", e);
		}
		return richieste;

	}

	public ModelRichiesta selectNumeroRichiesta(String numeroRichiesta) throws DatabaseException {
		ModelRichiesta richieste = new ModelRichiesta();

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroRichiesta);
		try {
			richieste = jdbcTemplate.queryForObject(SELECT_NUMERO_RICHIESTA, namedParameters,
					new DettaglioRichiestaMapper());
			// MODIFICA TAG 72 buonodom
			// prendo ultima nota
			if (!Util.isValorizzato(richieste.getNote())) {
				String nota = selectNotaCittadino(numeroRichiesta);
				if (Util.isValorizzato(nota)) {
					richieste.setNote(nota);
				}
			}
			return richieste;
		} catch (Exception e) {
			generateDatabaseException("SELECT_NUMERO_RICHIESTA", e);
		}
		return richieste;

	}

	public List<ModelAllegato> selectAllegatiFromNumeroRichiesta(String numeroRichiesta) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroRichiesta", numeroRichiesta);
		Long idDomanda = null;
		List<ModelAllegato> allegati = new ArrayList<ModelAllegato>();
		try {
			idDomanda = jdbcTemplate.queryForObject(SELECT_ID_DOMANDA, namedParameters, Long.class);
			SqlParameterSource namedParametersAllegati = new MapSqlParameterSource().addValue("idDomanda", idDomanda);
			allegati = jdbcTemplate.query(SELECT_ALLEGATI, namedParametersAllegati, new AllegatoTipoMapper());

			return allegati;

		} catch (Exception e) {
			generateDatabaseException("SELECT_ALLEGATI", e);
		}
		return allegati;

	}

	public List<String> selectRettificareFromNumeroRichiesta(String numeroRichiesta) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroRichiesta", numeroRichiesta);
		List<String> campiModificare = new ArrayList<String>();
		try {
			campiModificare = jdbcTemplate.queryForList(SELECT_NOME_CAMPO_DA_MODIFICARE, namedParameters, String.class);

			return campiModificare;

		} catch (Exception e) {
			generateDatabaseException("SELECT_NOME_CAMPO_DA_MODIFICARE", e);
		}
		return campiModificare;

	}

	public Long selectIdStato(String codStato) throws DatabaseException {
		Long idStato = null;
		if (StringUtils.isEmpty(codStato)) {
			return idStato;
		}
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codStato", codStato);
		try {
			idStato = jdbcTemplate.queryForObject(SELECT_ID_STATO, namedParameters, Long.class);
			return idStato;
		} catch (Exception e) {
			generateDatabaseException("SELECT_ID_STATO", e);
		}
		return idStato;

	}

	public DDomandaStato selectDDomandaStatoByCod(String codStato) throws DatabaseException {
		DDomandaStato res = null;
		if (StringUtils.isEmpty(codStato)) {
			return res;
		}
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codStato", codStato);
		try {
			res = jdbcTemplate.queryForObject(SELECT_D_DOMANDA_STATO_BY_COD, namedParameters,
					new DDomandaStatoMapper());
			return res;
		} catch (Exception e) {
			generateDatabaseException("SELECT_ID_STATO", e);
		}
		return res;

	}

	public String selectCodStato(String codStato) throws DatabaseException {
		String cod = null;

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codStato", codStato);
		try {
			cod = jdbcTemplate.queryForObject(SELECT_COD_STATO, namedParameters, String.class);
			return cod;
		} catch (Exception e) {
			generateDatabaseException("SELECT_COD_STATO", e);
		}
		return cod;

	}

	public Long selectIdSportello() throws DatabaseException {
		Long idSportello = null;

		SqlParameterSource namedParameters = new MapSqlParameterSource();
		try {
			idSportello = jdbcTemplate.queryForObject(SELECT_ID_SPORTELLI, namedParameters, Long.class);
			return idSportello;
		} catch (Exception e) {
			generateDatabaseException("SELECT_ID_SPORTELLO", e);
		}
		return idSportello;

	}

	public Long selectIdContributo(String codContributo) throws DatabaseException {
		Long idContributo = null;
		if (StringUtils.isEmpty(codContributo)) {
			return idContributo;
		}
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codContributo", codContributo);
		try {
			idContributo = jdbcTemplate.queryForObject(SELECT_ID_CONTRIBUTO, namedParameters, Long.class);
			return idContributo;
		} catch (Exception e) {
			generateDatabaseException("SELECT_ID_CONTRIBUTO", e);
		}
		return idContributo;

	}

	public String selectCodContributo(String codContributo) throws DatabaseException {
		String cod = null;

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codContributo", codContributo);
		try {
			cod = jdbcTemplate.queryForObject(SELECT_COD_CONTRIBUTO, namedParameters, String.class);
			return cod;
		} catch (Exception e) {
			generateDatabaseException("SELECT_COD_CONTRIBUTO", e);
		}
		return cod;

	}

	public Long selectIdTitolo(String codTitolo) throws DatabaseException {
		Long idTitolo = null;
		if (StringUtils.isEmpty(codTitolo)) {
			return idTitolo;
		}
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codTitolo", codTitolo);
		try {
			idTitolo = jdbcTemplate.queryForObject(SELECT_ID_TITOLO, namedParameters, Long.class);
			return idTitolo;
		} catch (Exception e) {
			generateDatabaseException("SELECT_ID_TITOLO", e);
		}
		return idTitolo;

	}

	public Long selectIdRapporto(String codRapporto) throws DatabaseException {
		Long idRapporto = null;
		if (StringUtils.isEmpty(codRapporto)) {
			return idRapporto;
		}
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codRapporto", codRapporto);
		try {
			idRapporto = jdbcTemplate.queryForObject(SELECT_ID_RAPPORTO, namedParameters, Long.class);
			return idRapporto;
		} catch (Exception e) {
			generateDatabaseException("SELECT_ID_RAPPORTO", e);
		}
		return idRapporto;

	}

	public Long selectIdValutazioneMultidimensionale(String codValutazioneMultidimensionale) throws DatabaseException {
		Long idValutazioneMultidimensionale = null;
		if (StringUtils.isEmpty(codValutazioneMultidimensionale)) {
			return idValutazioneMultidimensionale;
		}
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codValutazioneMultidimensionale",
				codValutazioneMultidimensionale);
		try {
			idValutazioneMultidimensionale = jdbcTemplate.queryForObject(SELECT_ID_VALUTAZIONE_MULTIDIMENSIONALE,
					namedParameters, Long.class);
			return idValutazioneMultidimensionale;
		} catch (Exception e) {
			generateDatabaseException("SELECT_ID_VALUTAZIONE_MULTIDIMENSIONALE", e);
		}
		return idValutazioneMultidimensionale;
	}

	public Long selectIdContratto(String codContratto) throws DatabaseException {
		Long idStato = null;
		if (StringUtils.isEmpty(codContratto)) {
			return idStato;
		}
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codContratto", codContratto);
		try {
			idStato = jdbcTemplate.queryForObject(SELECT_ID_CONTRATTO, namedParameters, Long.class);
			return idStato;
		} catch (Exception e) {
			generateDatabaseException("SELECT_ID_CONTRATTO", e);
		}
		return idStato;
	}

	public Long selectIdAsl(String codAsl) throws DatabaseException {
		Long idAsl = null;
		if (StringUtils.isEmpty(codAsl)) {
			return idAsl;
		}
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codAsl", codAsl);
		try {
			idAsl = jdbcTemplate.queryForObject(SELECT_ID_ASL, namedParameters, Long.class);
			return idAsl;
		} catch (Exception e) {
			generateDatabaseException("SELECT_ID_ASL", e);
		}
		return idAsl;
	}

	public Long selectIdDettaglio(String numeroDomanda) throws DatabaseException {
		Long idDettaglio = null;
		if (StringUtils.isEmpty(numeroDomanda)) {
			return idDettaglio;
		}
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda);
		try {
			idDettaglio = jdbcTemplate.queryForObject(SELECT_ID_DETTAGLIO_DOMANDA, namedParameters, Long.class);
			return idDettaglio;
		} catch (Exception e) {
			generateDatabaseException("SELECT_ID_DETTAGLIO_DOMANDA", e);
		}
		return idDettaglio;
	}

	public Boolean presenzaDomanda(String codFiscale) throws DatabaseException {
		Boolean isPresente = null;

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("cfDestinatario", codFiscale);
		try {
			isPresente = jdbcTemplate.queryForObject(DOMANDA_PRESENTE, namedParameters, Boolean.class);
			return isPresente;
		} catch (EmptyResultDataAccessException e) {
			return false;
		} catch (Exception e) {
			generateDatabaseException("DOMANDA_PRESENTE", e);
		}
		return isPresente;

	}

	public Boolean residentePiemonte(String codProvincia) throws DatabaseException {
		Boolean isPresente = null;

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codProvincia", codProvincia);
		try {
			isPresente = jdbcTemplate.queryForObject(RESIDENTE_PIEMONTE, namedParameters, Boolean.class);
			return isPresente;
		} catch (Exception e) {
			generateDatabaseException("RESIDENTE_PIEMONTE", e);
		}
		return isPresente;
	}

	@Transactional
	public Long insertDomandaAndDettaglio(String shibIdentitaCodiceFiscale, ModelBozzaRichiesta richiesta, Long idStato,
			Long idContributo, String codStato, Long idSportello, Long idTitolo, Long idRapporto, Long idAsl)
			throws DatabaseException {
		Long idDomanda = this.insertTDomanda(richiesta, idContributo, idSportello,
				shibIdentitaCodiceFiscale);
		String codDomandaDet = codStato + "_";

		this.insertTDomandaDettaglio(richiesta, shibIdentitaCodiceFiscale, idDomanda,
				idSportello, idStato, idRapporto, idTitolo, idAsl, codDomandaDet);
		return idDomanda;
	}

	public long insertTDomanda(ModelBozzaRichiesta richiesta, Long idContributo, Long idSportello, String codFiscale)
			throws DatabaseException {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue("cfRichiedente", richiesta.getRichiedente().getCf(), Types.VARCHAR);
		params.addValue("cfDestinatario", richiesta.getDestinatario().getCf(), Types.VARCHAR);
		params.addValue("idTipoContributo", idContributo, Types.BIGINT);
		params.addValue("idSportello", idSportello, Types.BIGINT);
		params.addValue("utenteCrea", codFiscale, Types.VARCHAR);
		params.addValue("utenteModifica", codFiscale, Types.VARCHAR);

		jdbcTemplate.update(INSERT_T_DOMANDA, params, keyHolder, new String[] { "domanda_id" });
		return keyHolder.getKey().longValue();
	}

	public long insertTDomandaDettaglio(ModelBozzaRichiesta richiesta, String codFiscale, Long idDomanda,
			Long idSportello, Long idStatoDomanda, Long idTipoRapporto, Long idTitoloStudio, Long idAsl,
			String codDetDomanda) throws DatabaseException {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue("idDomanda", idDomanda, Types.BIGINT);
		params.addValue("idSportello", idSportello, Types.BIGINT);
		params.addValue("idStatoDomanda", idStatoDomanda, Types.BIGINT);
		params.addValue("nomeRichiedente", richiesta.getRichiedente().getNome(), Types.VARCHAR);
		params.addValue("cognomeRichiedente", richiesta.getRichiedente().getCognome(), Types.VARCHAR);
		params.addValue("dataNascitaRichiedente", richiesta.getRichiedente().getDataNascita(), Types.DATE);
		params.addValue("statoNascitaRichiedente", richiesta.getRichiedente().getStatoNascita(), Types.VARCHAR);
		params.addValue("comuneNascitaRichiedente", richiesta.getRichiedente().getComuneNascita(), Types.VARCHAR);
		params.addValue("provinciaNascitaRichiedente", richiesta.getRichiedente().getProvinciaNascita(), Types.VARCHAR);
		params.addValue("indirizzoResidenzaRichiedente", richiesta.getRichiedente().getIndirizzoResidenza(),
				Types.VARCHAR);
		params.addValue("comuneResidenzaRichiedente", richiesta.getRichiedente().getComuneResidenza(), Types.VARCHAR);
		params.addValue("provinciaResidenzaRichiedente", richiesta.getRichiedente().getProvinciaResidenza(),
				Types.VARCHAR);
		params.addValue("nomeDestinatario", richiesta.getDestinatario().getNome(), Types.VARCHAR);
		params.addValue("cognomeDestinatario", richiesta.getDestinatario().getCognome(), Types.VARCHAR);
		params.addValue("dataNascitaDestinatario", richiesta.getDestinatario().getDataNascita(), Types.DATE);
		params.addValue("statoNascitaDestinatario", richiesta.getDestinatario().getStatoNascita(), Types.VARCHAR);
		params.addValue("comuneNascitaDestinatario", richiesta.getDestinatario().getComuneNascita(), Types.VARCHAR);
		params.addValue("provinciaNascitaDestinatario", richiesta.getDestinatario().getProvinciaNascita(),
				Types.VARCHAR);
		params.addValue("indirizzoResidenzaDestinatario", richiesta.getDestinatario().getIndirizzoResidenza(),
				Types.VARCHAR);
		params.addValue("comuneResidenzaDestinatario", richiesta.getDestinatario().getComuneResidenza(), Types.VARCHAR);
		params.addValue("provinciaResidenzaDestinatario", richiesta.getDestinatario().getProvinciaResidenza(),
				Types.VARCHAR);
		if (richiesta.getDomicilioDestinatario() != null) {
			params.addValue("indirizzoDomicilioDestinatario", richiesta.getDomicilioDestinatario().getIndirizzo(),
					Types.VARCHAR);
			params.addValue("comuneDomicilioDestinatario", richiesta.getDomicilioDestinatario().getComune(),
					Types.VARCHAR);
			params.addValue("provinciaDomicilioDestinatario", richiesta.getDomicilioDestinatario().getProvincia(),
					Types.VARCHAR);
		} else {
			params.addValue("indirizzoDomicilioDestinatario", null, Types.VARCHAR);
			params.addValue("comuneDomicilioDestinatario", null, Types.VARCHAR);
			params.addValue("provinciaDomicilioDestinatario", null, Types.VARCHAR);
		}
		params.addValue("situazioneLavorativa", richiesta.isLavoroDestinatario(), Types.BOOLEAN);
		params.addValue("note", richiesta.getNote(), Types.VARCHAR);
		params.addValue("idTipoRapporto", idTipoRapporto, Types.BIGINT);
		params.addValue("idTitoloStudio", idTitoloStudio, Types.BIGINT);
		params.addValue("idAsl", idAsl, Types.BIGINT);
		params.addValue("utenteCrea", codFiscale, Types.VARCHAR);
		params.addValue("utenteModifica", codFiscale, Types.VARCHAR);

		jdbcTemplate.update(INSERT_T_DOMANDA_DETTAGLIO, params, keyHolder, new String[] { "domanda_det_id" });
		long iddettaglio = keyHolder.getKey().longValue();
		updateDettaglioDomanda(iddettaglio, codDetDomanda + iddettaglio);
		return iddettaglio;
	}

	public long updateDettaglioDomanda(long idDettaglio, String detCod) throws DatabaseException {

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("idDomanda", idDettaglio, Types.BIGINT).addValue("detCod", detCod, Types.VARCHAR);
		return jdbcTemplate.update(UPDATE_DET_COD, params);
	}

	public ModelRichiestaExt selectNumeroRichiestaExt(String numeroRichiesta) throws DatabaseException {
		ModelRichiestaExt richieste = new ModelRichiestaExt();

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroRichiesta);
		try {
			richieste = jdbcTemplate.queryForObject(SELECT_NUMERO_RICHIESTA_COD, namedParameters,
					new DettaglioRichiestaExtMapper());
			return richieste;
		} catch (Exception e) {
			generateDatabaseException("SELECT_NUMERO_RICHIESTA_COD", e);
		}
		return richieste;

	}

	public ModelRichiestaLight selectRichiestaLight(String numeroRichiesta) throws DatabaseException {
		ModelRichiestaLight richieste = new ModelRichiestaLight();

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroRichiesta);
		try {
			richieste = jdbcTemplate.queryForObject(SELECT_RICHIESTA_LIGHT, namedParameters,
					new DettaglioRichiestaLightMapper());
			return richieste;
		} catch (Exception e) {
			generateDatabaseException("SELECT_RICHIESTA_LIGHT", e);
		}
		return richieste;

	}

	public long updateTDettaglioDomanda(Long idDettaglio, ModelRichiesta richiesta, Long idTitolo, Long idRapporto,
			Long idAsl, Long idValutazione, Long idRelazione, Long idContratto, String codFiscale)
			throws DatabaseException {

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("idDettaglio", idDettaglio, Types.BIGINT);
		params.addValue("punteggioSociale", richiesta.getPunteggioBisognoSociale(), Types.BIGINT);
		params.addValue("nessunaIncompatibilita", richiesta.isNessunaIncompatibilita(), Types.BOOLEAN);

		params.addValue("rsaCod", null, Types.VARCHAR);
		params.addValue("rsaArpeCod", null, Types.VARCHAR);
		params.addValue("rsaDenominazione", null, Types.VARCHAR);
		params.addValue("rsaIndirizzo", null, Types.VARCHAR);
		params.addValue("rsaComune", null, Types.VARCHAR);
		params.addValue("rsaProvincia", null, Types.VARCHAR);
		params.addValue("rsaCfPiva", null, Types.VARCHAR);
		params.addValue("rsaSedeLegaleDenominazione", null, Types.VARCHAR);
		params.addValue("rsaSedeLegaleIndirizzo", null, Types.VARCHAR);
		params.addValue("rsaSedeLegaleComune", null, Types.VARCHAR);
		params.addValue("rsaSedeLegaleProvincia", null, Types.VARCHAR);
		params.addValue("datoreNome", null, Types.VARCHAR);
		params.addValue("datoreCognome", null, Types.VARCHAR);
		params.addValue("datoreCf", null, Types.VARCHAR);
		params.addValue("datoreNascitaStato", null, Types.VARCHAR);
		params.addValue("datoreNascitaComune", null, Types.VARCHAR);
		params.addValue("datoreNascitaData", null, Types.DATE);
		params.addValue("datoreNascitaProvincia", null, Types.VARCHAR);
		params.addValue("idContratto", null, Types.BIGINT);
		params.addValue("idRelazione", null, Types.BIGINT);
		params.addValue("dataInizioContratto", null, Types.TIMESTAMP);
		params.addValue("dataFineContratto", null, Types.TIMESTAMP);
		if (richiesta.getContratto() != null) {
			if (richiesta.getContratto().getStruttura() != null) {
				ModelStruttura struttura = richiesta.getContratto().getStruttura();
				params.addValue("rsaCod", struttura.getId(), Types.VARCHAR);
				params.addValue("rsaArpeCod", struttura.getIdArpe(), Types.VARCHAR);
				params.addValue("rsaDenominazione", struttura.getNome(), Types.VARCHAR);
				params.addValue("rsaIndirizzo", struttura.getIndirizzo(), Types.VARCHAR);
				params.addValue("rsaComune", struttura.getComune(), Types.VARCHAR);
				params.addValue("rsaProvincia", struttura.getProvincia(), Types.VARCHAR);
				params.addValue("rsaCfPiva", struttura.getPiva(), Types.VARCHAR);
				params.addValue("rsaSedeLegaleDenominazione", struttura.getSedeLegale().getNome(), Types.VARCHAR);
				params.addValue("rsaSedeLegaleIndirizzo", struttura.getSedeLegale().getIndirizzo(), Types.VARCHAR);
				params.addValue("rsaSedeLegaleComune", struttura.getSedeLegale().getComune(), Types.VARCHAR);
				params.addValue("rsaSedeLegaleProvincia", struttura.getSedeLegale().getProvincia(), Types.VARCHAR);
			}
			// RIMOZIONE_INTESTATARIO_DATA_FINE POST_DEMO 14_04_2023
			// if (richiesta.getContratto().getIntestatario() != null) {
			// ModelPersonaSintesi intestatario =
			// richiesta.getContratto().getIntestatario();
			// params.addValue("datoreNome", intestatario.getNome(), Types.VARCHAR);
			// params.addValue("datoreCognome", intestatario.getCognome(), Types.VARCHAR);
			// params.addValue("datoreCf", intestatario.getCf(), Types.VARCHAR);
			// params.addValue("datoreNascitaStato", intestatario.getStatoNascita(),
			// Types.VARCHAR);
			// params.addValue("datoreNascitaComune", intestatario.getComuneNascita(),
			// Types.VARCHAR);
			// params.addValue("datoreNascitaData", intestatario.getDataNascita(),
			// Types.DATE);
			// params.addValue("datoreNascitaProvincia", intestatario.getProvinciaNascita(),
			// Types.VARCHAR);
			// }

			params.addValue("idContratto", idContratto, Types.BIGINT);
			// params.addValue("idRelazione", idRelazione, Types.BIGINT);
			params.addValue("dataInizioContratto", richiesta.getContratto().getDataInizio(), Types.TIMESTAMP);
			// params.addValue("dataFineContratto", richiesta.getContratto().getDataFine(),
			// Types.TIMESTAMP);

			params.addValue("incompatibilitaPerContratto", richiesta.getContratto().isIncompatibilitaPerContratto(),
					Types.BOOLEAN);
			params.addValue("titoloPrivato", richiesta.getContratto().isTitoloPrivato(), Types.BOOLEAN);
		}
		if (richiesta.getDomicilioDestinatario() != null) {
			params.addValue("indirizzoDomicilioDestinatario", richiesta.getDomicilioDestinatario().getIndirizzo(),
					Types.VARCHAR);
			params.addValue("comuneDomicilioDestinatario", richiesta.getDomicilioDestinatario().getComune(),
					Types.VARCHAR);
			params.addValue("provinciaDomicilioDestinatario", richiesta.getDomicilioDestinatario().getProvincia(),
					Types.VARCHAR);
		} else {
			params.addValue("indirizzoDomicilioDestinatario", null, Types.VARCHAR);
			params.addValue("comuneDomicilioDestinatario", null, Types.VARCHAR);
			params.addValue("provinciaDomicilioDestinatario", null, Types.VARCHAR);
		}
		params.addValue("situazioneLavorativa", richiesta.isLavoroDestinatario(), Types.BOOLEAN);
		params.addValue("note", richiesta.getNote(), Types.VARCHAR);

		params.addValue("idRapporto", idRapporto, Types.BIGINT);
		params.addValue("idTitoloStudio", idTitolo, Types.BIGINT);
		params.addValue("idAsl", idAsl, Types.BIGINT);
		params.addValue("idValutazione", idValutazione, Types.BIGINT);
		params.addValue("utenteModifica", codFiscale, Types.VARCHAR);
		params.addValue("iseeConforme", richiesta.isAttestazioneIsee(), Types.BOOLEAN);
		params.addValue("noterichiedente", richiesta.getNoteRichiedente(), Types.VARCHAR);

		return jdbcTemplate.update(UPDATE_T_DETTAGLIO_DOMANDA, params);
	}

	public ModelUpdateCronologia insertTDettaglioDomandaCambioStato(ModelRichiestaExt richiesta, String codStato,
			String codFiscale) throws DatabaseException {

		ModelUpdateCronologia result = new ModelUpdateCronologia();
		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue("domandastatocod", codStato, Types.VARCHAR);
		params.addValue("domandadetid", richiesta.getDomandaDetId(), Types.BIGINT);
		params.addValue("utenteCreazione", codFiscale, Types.VARCHAR);
		params.addValue("utenteModifica", codFiscale, Types.VARCHAR);

		jdbcTemplate.update(INSERT_DETTAGLIO_DOMANDA, params, keyHolder, new String[] { "domanda_det_id" });
		long idDettaglio = keyHolder.getKey().longValue();
		updateDettaglioDomanda(idDettaglio, codStato + "_" + idDettaglio);
		result.setDetCod(codStato + "_" + idDettaglio);
		result.setIdDettaglio(idDettaglio);

		return result;
	}

	public long updateDataFineValDettaglioDomanda(BigDecimal idDettaglio) throws DatabaseException {

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("idDomanda", idDettaglio, Types.BIGINT);
		return jdbcTemplate.update(UPDATE_DATA_FINE_VAL_DETTAGLIO_DOMANDA, params);
	}

	public long updateDataDomandaValDomanda(String numeroRichiesta, String codFiscale) throws DatabaseException {

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("numeroRichiesta", numeroRichiesta, Types.VARCHAR);
		params.addValue("codFiscale", codFiscale, Types.VARCHAR);
		return jdbcTemplate.update(UPDATE_DATA_DOMANDA_VAL_DOMANDA, params);
	}

	public String selectNumeroDomanda(Long idDomanda) throws DatabaseException {
		String cod = null;

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("idDomanda", idDomanda);
		try {
			cod = jdbcTemplate.queryForObject(SELECT_NUMERO_DOMANDA_PRINCIPALE, namedParameters, String.class);
			return cod;
		} catch (Exception e) {
			generateDatabaseException("SELECT_NUMERO_DOMANDA_PRINCIPALE", e);
		}
		return cod;
	}

	public String selectDetCod(String numeroDomanda) throws DatabaseException {
		String cod = null;

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroRichiesta", numeroDomanda);
		try {
			cod = jdbcTemplate.queryForObject(SELECT_DETCOD_DOMANDA, namedParameters, String.class);
			return cod;
		} catch (Exception e) {
			generateDatabaseException("SELECT_DETCOD_DOMANDA", e);
		}
		return cod;
	}

	public boolean isResidenzaPiemontese(String sigla) throws DatabaseException {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("sigla", sigla);
		try {
			Integer numprovincia = jdbcTemplate.queryForObject(SELECT_PROVINCIA_PIEMONTE, namedParameters,
					Integer.class);
			return numprovincia.intValue() > 0;

		} catch (Exception e) {
			generateDatabaseException("SELECT_PROVINCIA_PIEMONTE", e);
		}
		return false;
	}

	public long updateDatiDaModificare(long domandadetidnew, BigDecimal domandadetidold) throws DatabaseException {

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("domandadetidnew", domandadetidnew, Types.BIGINT).addValue("domandadetidold", domandadetidold,
				Types.BIGINT);
		return jdbcTemplate.update(UPDATE_CAMPO_DA_MODIFICARE, params);
	}

	// MODIFICA TAG 71 buonodom
	public long updateallegatiDaModificare(long domandadetidnew, long allegatoidold, long allegatoidnew)
			throws DatabaseException {

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("domandadetidnew", domandadetidnew, Types.BIGINT)
				.addValue("allegatoidold", allegatoidold, Types.BIGINT)
				.addValue("allegatoidnew", allegatoidnew, Types.BIGINT);
		return jdbcTemplate.update(UPDATE_ALLEGATO_ID_DA_MODIFICARE, params);
	}

	// MODIFICA TAG 72 buonodom
	public String selectNotaCittadino(String numeroDomanda) throws DatabaseException {
		String nota = null;

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda);
		try {
			nota = jdbcTemplate.queryForObject(SELECT_NOTA_CITTADINO, namedParameters, String.class);
			return nota;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_NOTA_CITTADINO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public Long selectGraduatoriaIdBySportelloId(BigDecimal idSportello) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("idSportello", idSportello);
		try {
			return jdbcTemplate.queryForObject(SELECT_GRADUATORIA_ID_BY_SPORTELLO_ID, params, Long.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "selectGraduatoriaIdBySportelloCod";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public Boolean checkStatoGraduatoria(long idGraduatoria, String stato) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("idGraduatoria", idGraduatoria);
			params.addValue("statoGraduatoria", stato);
			return jdbcTemplate.queryForObject(CHECK_STATO_GRADUATORIA_BY_ID, params, Boolean.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "checkStatoGraduatoria";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public void updateRGraduatoriaStato(long idGraduatoria, String stato, String cf) throws DatabaseException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("idGraduatoria", idGraduatoria);
			params.addValue("statoGraduatoria", stato);
			params.addValue("utenteCreazione", cf);
			params.addValue("utenteModifica", cf);
			jdbcTemplate.update(UPDATE_R_GRADUATORIA_STATO, params);
			return;
		} catch (Exception e) {
			String methodName = "updateRGraduatoriaStato";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}
}
