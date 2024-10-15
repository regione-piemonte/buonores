/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.custom;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import it.csi.buonores.buonoresbo.dto.ModelArea;
import it.csi.buonores.buonoresbo.dto.ModelDescrizioneGraduatoria;
import it.csi.buonores.buonoresbo.dto.ModelDomandeAmmissibili;
import it.csi.buonores.buonoresbo.dto.ModelDomandeGraduatoria;
import it.csi.buonores.buonoresbo.dto.ModelGraduatoriaOrdinamento;
import it.csi.buonores.buonoresbo.dto.ModelNuovaGraduatoria;
import it.csi.buonores.buonoresbo.dto.ModelParametriFinanziamento;
import it.csi.buonores.buonoresbo.dto.ModelSportello;
import it.csi.buonores.buonoresbo.exception.DatabaseException;
import it.csi.buonores.buonoresbo.integration.dao.utils.AreaMapper;
import it.csi.buonores.buonoresbo.integration.dao.utils.DescrizioneGraduatoriaMapper;
import it.csi.buonores.buonoresbo.integration.dao.utils.DomandeAmmissibiliMapper;
import it.csi.buonores.buonoresbo.integration.dao.utils.DomandeInGraduatoriaMapper;
import it.csi.buonores.buonoresbo.integration.dao.utils.GraduatoriaOrdinamentoMapper;
import it.csi.buonores.buonoresbo.integration.dao.utils.ParametriFinanziamentoMapper;
import it.csi.buonores.buonoresbo.integration.dao.utils.SportelloMapper;
import it.csi.buonores.buonoresbo.util.Constants;
import it.csi.buonores.buonoresbo.util.LoggerUtil;

@Repository
public class GraduatoriaDao extends LoggerUtil {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	private static final String SELECT_ULTIMO_SPORTELLO_CHIUSO = "select bts.sportello_cod, bts.sportello_desc, false is_corrente, "
			+ " to_char(bts.validita_inizio,'YYYY-MM-DD') as validita_inizio, "
			+ " to_char(bts.validita_fine,'YYYY-MM-DD') as validita_fine, "
			+ " bts.sportello_anno "
			+ "from bres_t_sportello bts "
			+ "where bts.validita_fine::date = (select max(validita_fine) "
			+ "from bres_t_sportello " + "where data_cancellazione is null " + "and validita_fine::date < now()::date "
			+ ")::date "
			+ " and bts.data_cancellazione is null ";

	private static final String SELECT_SPORTELLI_CHIUSI = "select bts.sportello_cod, bts.sportello_desc, false is_corrente, "
			+ " to_char(bts.validita_inizio,'YYYY-MM-DD') as validita_inizio, "
			+ " to_char(bts.validita_fine,'YYYY-MM-DD') as validita_fine, "
			+ " bts.sportello_anno "
			+ " from bres_t_sportello bts "
			+ " where bts.data_cancellazione is null "
			+ " and bts.validita_fine::date < now()::date "
			+ " order by bts.validita_inizio desc";

	private static final String SELECT_GRADUATORIA_ORDINAMENTO = "select bdgo.ordinamento_id, bdgo.ordinamento_criterio, "
			+ "	bdgo.ordinamento_tabella, bdgo.ordinamento_campo, "
			+ "	bdgo.ordinamento_posizione, bdgo.ordinamento_direzione "
			+ " from bres_d_graduatoria_ordinamento bdgo "
			+ " where bdgo.data_cancellazione is null "
			+ " and now()::date between bdgo.validita_inizio::date and coalesce(bdgo.validita_fine::date, now()::date)";

	private static final String SELECT_AREE = "select area_id, area_cod, area_desc "
			+ "from bres_d_area "
			+ "where data_cancellazione is null "
			+ "and now()::date between validita_inizio::date and coalesce(validita_fine::date, now()::date) "
			+ "order by area_id ";

	private static final String INSERT_T_GRADUATORIA = "INSERT INTO bres_t_graduatoria (sportello_id, graduatoria_desc , utente_creazione, utente_modifica) "
			+ "VALUES ((select sportello_id  " + "    from bres_t_sportello "
			+ "    where data_cancellazione is null "
			+ "    and sportello_cod  = :sportelloCod), :descGraduatoria , :utenteCreazione, :utenteModifica) ";

	private static final String INSERT_R_GRADUATORIA_STATO = "INSERT INTO bres_r_graduatoria_stato (graduatoria_id, graduatoria_stato_id, utente_creazione, utente_modifica) "
			+ "values (:idGraduatoria,(select graduatoria_stato_id  from bres_d_graduatoria_stato  "
			+ "where graduatoria_stato_cod = :statoGraduatoria " + "and data_cancellazione is null  "
			+ "and now() between coalesce (validita_inizio, now()) and coalesce(validita_fine,now()) "
			+ "), :utenteCreazione, :utenteModifica)";

	private static final String INSERT_R_GRADUATORIA_ORDINAMENTO = "INSERT into bres_r_graduatoria_ordinamento (graduatoria_id, ordinamento_id, utente_creazione, utente_modifica) "
			+ "values (:idGraduatoria,:idOrdinamento, :utenteCreazione, :utenteModifica) ";

	private static final String INSERT_T_GRADUATORIA_FINANZIAMENTO = "insert into bres_t_graduatoria_finanziamento (finanziamento_desc, finanziamento_priorita, "
			+ "finanziamento_importo_totale, finanziamento_importo_mensile, finanziamento_mesi, "
			+ "finanziamento_distribuibile, graduatoria_id,area_id, utente_creazione, utente_modifica) "
			+ "values (:descrizione, :priorita, :importoTotale, :importoMensile, :mesi, "
			+ "(select case when coalesce(:idArea,0) !=0 then true else false end), "
			+ ":idGraduatoria, :idArea, :utenteCreazione, :utenteModifica) ";

	private static final String UPDATE_FINANAZIAMENTO_GRADUATORIA = "UPDATE bres_t_graduatoria_finanziamento "
			+ "SET finanziamento_id_destinazione  = (case "
			+ "    when coalesce(area_id,0) !=0 then (finanziamento_id+3-area_id) "
			+ "    else null "
			+ "end) "
			+ "where finanziamento_id = ( "
			+ "    select finanziamento_id  from bres_t_graduatoria_finanziamento btgf  "
			+ "    where graduatoria_id = :idGraduatoria "
			+ "    and coalesce(area_id,0) = coalesce(:idArea,0));"
			+ " "
			+ "UPDATE bres_t_graduatoria_finanziamento "
			+ "SET finanziamento_importo_ricevuto = (case "
			+ "    when coalesce(area_id,0) !=0 then 0 "
			+ "    else (select sum(coalesce(finanziamento_importo_distribuito ,0))  "
			+ "    from bres_t_graduatoria_finanziamento btgf  "
			+ "    WHERE graduatoria_id = :idGraduatoria "
			+ "    and coalesce(area_id,0) !=0) "
			+ "end) "
			+ "where finanziamento_id = ( "
			+ "    select finanziamento_id  from bres_t_graduatoria_finanziamento btgf  "
			+ "    where graduatoria_id = :idGraduatoria " + "    and coalesce(area_id,0) = coalesce(:idArea,0)); "
			+ " "
			+ "UPDATE bres_t_graduatoria_finanziamento " + "set finanziamento_importo_finanziabile = (case "
			+ "    when coalesce(area_id,0) !=0 then finanziamento_importo_totale "
			+ "    else (coalesce(finanziamento_importo_totale ,0)+coalesce(finanziamento_importo_ricevuto,0)) "
			+ "end) "
			+ "where finanziamento_id = ( "
			+ "    select finanziamento_id  from bres_t_graduatoria_finanziamento btgf  "
			+ "    where graduatoria_id = :idGraduatoria " + "    and coalesce(area_id,0) = coalesce(:idArea,0)); "
			+ " "
			+ "UPDATE bres_t_graduatoria_finanziamento " + "set numero_domande_finanziate  = ( " + "case  "
			+ "    when coalesce((select count(*)*(select (finanziamento_mesi*finanziamento_importo_mensile)  "
			+ "    from bres_t_graduatoria_finanziamento  "
			+ "    where graduatoria_id = :idGraduatoria "
			+ "    and coalesce(area_id,0) = coalesce(:idArea,0)) "
			+ "        from bres_t_graduatoria_dettaglio btgd  "
			+ "        where btgd.finanziamento_id = ( "
			+ "            select finanziamento_id  from bres_t_graduatoria_finanziamento btgf  "
			+ "            where graduatoria_id = :idGraduatoria "
			+ "            and coalesce(area_id,0) = coalesce(:idArea,0))),0) < finanziamento_importo_finanziabile  "
			+ "    then (select count(*) " + "        from bres_t_graduatoria_dettaglio btgd  "
			+ "        where btgd.finanziamento_id = ( "
			+ "            select finanziamento_id  from bres_t_graduatoria_finanziamento btgf  "
			+ "            where graduatoria_id = :idGraduatoria "
			+ "            and coalesce(area_id,0) = coalesce(:idArea,0))) "
			+ "    else (cast(finanziamento_importo_finanziabile as INTEGER) / cast((finanziamento_mesi*finanziamento_importo_mensile) as INTEGER)) "
			+ "end) " + "where finanziamento_id = ( "
			+ "    select finanziamento_id  from bres_t_graduatoria_finanziamento btgf  "
			+ "    where graduatoria_id = :idGraduatoria " + "    and coalesce(area_id,0) = coalesce(:idArea,0)); "
			+ " "
			+ " " + "UPDATE bres_t_graduatoria_finanziamento "
			+ "set finanziamento_importo_finanziato = (numero_domande_finanziate * finanziamento_mesi * finanziamento_importo_mensile) "
			+ "where finanziamento_id = ( "
			+ "    select finanziamento_id  from bres_t_graduatoria_finanziamento btgf  "
			+ "    where graduatoria_id = :idGraduatoria " + "    and coalesce(area_id,0) = coalesce(:idArea,0)); "
			+ " "
			+ "UPDATE bres_t_graduatoria_finanziamento " + "set finanziamento_importo_residuo=(  "
			+ "case when coalesce(finanziamento_distribuibile,false) then 0 "
			+ "else (coalesce(finanziamento_importo_finanziabile,0)-coalesce(finanziamento_importo_finanziato,0)) "
			+ "end) " + "where finanziamento_id = ( "
			+ "    select finanziamento_id  from bres_t_graduatoria_finanziamento btgf  "
			+ "    where graduatoria_id = :idGraduatoria " + "    and coalesce(area_id,0) = coalesce(:idArea,0)); "
			+ " "
			+ "UPDATE bres_t_graduatoria_finanziamento " + "set finanziamento_importo_distribuito = (case "
			+ "    when coalesce(finanziamento_distribuibile,false) then (coalesce(finanziamento_importo_finanziabile,0)-coalesce(finanziamento_importo_finanziato,0)) "
			+ "    else 0 " + "end) " + "where finanziamento_id = ( "
			+ "    select finanziamento_id  from bres_t_graduatoria_finanziamento btgf  "
			+ "    where graduatoria_id = :idGraduatoria " + "    and coalesce(area_id,0) = coalesce(:idArea,0));";

	private static final String SELECT_DOMANDE_AMMISSIBILI = "select btdd.domanda_det_id, btd.domanda_numero, btd.richiedente_cf, btdd.sportello_id, "
			+ "    case " + "        when btdd.nessuna_incompatibilita = true  "
			+ "            and btdd.incompatibilita_per_contratto = true  " + "        then true else false "
			+ "    end as riserva "
			+ "from bres_t_domanda btd, bres_t_domanda_dettaglio btdd " + "where btd.domanda_id = btdd.domanda_id  "
			+ "and btdd.domanda_stato_id = ( " + "    select bdds.domanda_stato_id  "
			+ "    from bres_d_domanda_stato bdds  " + "    where bdds.domanda_stato_cod  = (:stato) " + ") "
			+ "and btdd.sportello_id  = ( " + "    select bts.sportello_id  " + "    from bres_t_sportello bts "
			+ "    where bts.sportello_cod = :sportelloCod " + "    and bts.data_cancellazione is null  " + ") "
			+ "and btd.data_cancellazione is null  " + "and btdd.data_cancellazione is null  "
			+ "and now() between btdd.validita_inizio and coalesce(btdd.validita_fine, now());";

	private static String SELECT_DOMANDE_AMMESSE_SPORTELLI = "select domanda_det_id "
			+ "from bres_t_domanda_dettaglio, bres_t_domanda "
			+ "where bres_t_domanda_dettaglio.data_cancellazione is null "
			+ "and bres_t_domanda.data_cancellazione is null "
			+ "and bres_t_domanda.domanda_id = bres_t_domanda_dettaglio.domanda_id "
			+ "and now() between bres_t_domanda_dettaglio.validita_inizio and coalesce(bres_t_domanda_dettaglio.validita_fine, now()) "
			+ "and bres_t_domanda_dettaglio.domanda_stato_id in ( " + "	select bdds.domanda_stato_id  "
			+ "	from bres_d_domanda_stato bdds "
			+ "	where bdds.data_cancellazione is null "
			+ "	and now() between bdds.validita_inizio and coalesce(bdds.validita_fine, now()) "
			+ "    and (bdds.domanda_stato_cod = :stato or bdds.domanda_stato_cod = :stato1) ) "
			+ "and bres_t_domanda_dettaglio.sportello_id in( " + "	select bts.sportello_id  "
			+ "	from bres_t_sportello bts "
			+ "	where bts.data_cancellazione is null " + "	and bts.validita_fine <= ( "
			+ "		select bts2.validita_fine " + "		from bres_t_sportello bts2 "
			+ "		where data_cancellazione is null " + "		and bts2.sportello_cod = :codSportello " + " ) "
			+ ")";

	private static final String DELETE_GRADUATORIA = "delete from bres_t_graduatoria_dettaglio "
			+ "			where data_cancellazione is null "
			+ "			and graduatoria_id  in ( "
			+ "			select graduatoria_id from bres_t_graduatoria  btg "
			+ "			where btg.data_cancellazione is null "
			+ "			and btg.sportello_id in ( "
			+ "				select bts.sportello_id "
			+ "				from bres_t_sportello bts "
			+ "				where bts.data_cancellazione is null "
			+ "				and bts.sportello_cod = :codSportello)); "
			+ " "
			+ " 		delete from bres_r_graduatoria_ordinamento "
			+ "			where data_cancellazione is null "
			+ "			and graduatoria_id  in ( "
			+ "			select graduatoria_id from bres_t_graduatoria  btg "
			+ "			where btg.data_cancellazione is null "
			+ "			and btg.sportello_id in ( "
			+ "				select bts.sportello_id "
			+ "				from bres_t_sportello bts "
			+ "				where bts.data_cancellazione is null "
			+ "				and bts.sportello_cod = :codSportello)); "
			+ " "
			+ " 		delete from bres_r_graduatoria_stato "
			+ "			where data_cancellazione is null "
			+ "			and graduatoria_id  in ( "
			+ "			select graduatoria_id from bres_t_graduatoria  btg "
			+ "			where btg.data_cancellazione is null "
			+ "			and btg.sportello_id in ( "
			+ "				select bts.sportello_id "
			+ "				from bres_t_sportello bts "
			+ "				where bts.data_cancellazione is null "
			+ "				and bts.sportello_cod = :codSportello)); "
			+ " "
			+ "         delete from bres_t_graduatoria_finanziamento "
			+ "			where data_cancellazione is null "
			+ "			and graduatoria_id  in ( "
			+ "			select graduatoria_id from bres_t_graduatoria  btg "
			+ "			where btg.data_cancellazione is null "
			+ "			and btg.sportello_id in ( "
			+ "				select bts.sportello_id "
			+ "				from bres_t_sportello bts "
			+ "				where bts.data_cancellazione is null "
			+ "				and bts.sportello_cod = :codSportello)); "
			+ " "
			+ " 		delete from bres_t_graduatoria "
			+ "			where data_cancellazione is null "
			+ "			and graduatoria_id  in ( "
			+ "			select graduatoria_id from bres_t_graduatoria  btg "
			+ "			where btg.data_cancellazione is null "
			+ "			and btg.sportello_id in ( "
			+ "				select bts.sportello_id "
			+ "				from bres_t_sportello bts "
			+ "				where bts.data_cancellazione is null "
			+ "				and bts.sportello_cod = :codSportello)) ";

	private static final String INSERT_T_GRADUATORIA_DETTAGLIO = "insert into bres_t_graduatoria_dettaglio (graduatoria_id, domanda_id, sportello_id, "
			+ "finanziamento_id, posizione, importo, "
			+ "utente_creazione, utente_modifica) "
			+ "values ((select graduatoria_id from bres_t_graduatoria btg "
			+ "			where btg.data_cancellazione is null "
			+ "			and btg.sportello_id in ( "
			+ "				select bts.sportello_id "
			+ "				from bres_t_sportello bts "
			+ "				where bts.data_cancellazione is null "
			+ "				and bts.sportello_cod = :codSportello)), "
			+ "(select domanda_id from bres_t_domanda_dettaglio btdd where btdd.domanda_det_id = :idDettaglio), "
			+ "(select sportello_id from bres_t_domanda_dettaglio btdd where btdd.domanda_det_id = :idDettaglio), "
			+ "(select finanziamento_id  "
			+ "from bres_t_graduatoria_finanziamento btgf "
			+ "where graduatoria_id = (select graduatoria_id from bres_t_graduatoria btg "
			+ "			where btg.data_cancellazione is null "
			+ "			and btg.sportello_id in ( "
			+ "				select bts.sportello_id "
			+ "				from bres_t_sportello bts "
			+ "				where bts.data_cancellazione is null "
			+ "				and bts.sportello_cod = :codSportello)) "
			+ "and coalesce(btgf.area_id, 0)  = coalesce(( "
			+ "	SELECT area_id "
			+ "	FROM bres_t_domanda_dettaglio btdd "
			+ "	WHERE btdd.domanda_det_id = :idDettaglio), 0)), "
			+ ":posizione, "
			+ ":importo, "
			+ ":utenteCreazione, "
			+ ":utenteModifica "
			+ ")";

	private static final String SELECT_DOMANDE_IN_GRADUATORIA = " select btgd.posizione, btd.domanda_numero,   "
			+ " btdd.destinatario_nome, btdd.destinatario_cognome, btd.beneficiario_cf,   "
			+ " coalesce(CAST(btdd.verifica_eg_punteggio_sociale AS numeric), btdd.punteggio_sociale) as punteggio_sociale,  "
			+ " btdd.isee_valore, to_char(btd.domanda_data,'DD/MM/YYYY') as domanda_data,   "
			+ " bdds.domanda_stato_desc,btgd.importo, "
			+ " btdd.verifica_eg_richiesta, btdd.verifica_eg_in_corso, btdd.verifica_eg_conclusa, "
			+ " case    "
			+ "     when btdd.area_id is not null  "
			+ " 	then ( "
			+ "			select bda.area_desc  "
			+ "         from bres_d_area bda "
			+ "         where bda.area_id = btdd.area_id) "
			+ "     when btdd.area_id is null  "
			+ "     then null  "
			+ "     else null   "
			+ " end as area   "
			+ " from bres_t_graduatoria_dettaglio btgd, bres_t_domanda btd,    "
			+ " bres_t_domanda_dettaglio btdd, bres_d_domanda_stato bdds    "
			+ " where graduatoria_id = (   "
			+ " 	select graduatoria_id from bres_t_graduatoria btg   "
			+ "     where btg.data_cancellazione is null   "
			+ "     and btg.sportello_id in (   "
			+ "     	select bts.sportello_id   "
			+ "         from bres_t_sportello bts   "
			+ "         where bts.data_cancellazione is null   "
			+ "         and bts.sportello_cod = :codSportello)   "
			+ "     )   "
			+ " and btgd.domanda_id = btd.domanda_id   "
			+ " and btdd.domanda_id  = btd.domanda_id   "
			+ " and btdd.domanda_stato_id = bdds.domanda_stato_id   "
			+ " and btdd.data_cancellazione is null   "
			+ " and btd.data_cancellazione is null   "
			+ " and now() between btdd.validita_inizio and coalesce(btdd.validita_fine, now()) "
			+ " order by posizione  ";

	private static final String SELECT_PARAMETRI_FINANZIAMENTO_GRADUATORIA = "select btgf.finanziamento_importo_mensile as importo_mensile, finanziamento_mesi as mesi,     "
			+ "     btgf.numero_domande_finanziate as soggetti_finanziati, finanziamento_importo_totale as importo_totale,     "
			+ "     btgf.finanziamento_importo_residuo as importo_residuo, btgf.finanziamento_importo_distribuito as importo_distribuito,  "
			+ "  (select count(*) from bres_t_graduatoria_dettaglio btgd2 where btgd2.graduatoria_id = (     "
			+ "     select btg.graduatoria_id   "
			+ "     from bres_t_graduatoria btg, bres_r_graduatoria_stato brgs   "
			+ "     where btg.graduatoria_id = brgs.graduatoria_id  "
			+ "     and btg.data_cancellazione is null  "
			+ "     and brgs.data_cancellazione is null   "
			+ "     and now() between coalesce(brgs.validita_inizio, now()) and coalesce(brgs.validita_fine,now())  "
			+ "     and btg.sportello_id in (  "
			+ "         select bts.sportello_id  "
			+ "         from bres_t_sportello bts  "
			+ "         where bts.data_cancellazione is null  "
			+ "         and bts.sportello_cod = :codSportello)   "
			+ " ) and btgd2.finanziamento_id = btgf.finanziamento_id) as soggetti_finanziabili,       "
			+ " case    "
			+ "    when btgf.area_id is not null  "
			+ "    then ( "
			+ "            select bda.area_desc  "
			+ "            from bres_d_area bda "
			+ "            where bda.area_id = btgf.area_id "
			+ "    )   "
			+ "    when btgf.area_id is null  "
			+ "    then null  "
			+ "    else null   "
			+ "end as area "
			+ " from bres_t_graduatoria_finanziamento btgf  "
			+ " where btgf.graduatoria_id  = (     "
			+ "     select btg.graduatoria_id   "
			+ "     from bres_t_graduatoria btg, bres_r_graduatoria_stato brgs   "
			+ "     where btg.graduatoria_id = brgs.graduatoria_id  "
			+ "     and btg.data_cancellazione is null  "
			+ "     and brgs.data_cancellazione is null   "
			+ "     and now() between coalesce(brgs.validita_inizio, now()) and coalesce(brgs.validita_fine,now())  "
			+ "     and btg.sportello_id in (  "
			+ "         select bts.sportello_id  "
			+ "         from bres_t_sportello bts  "
			+ "         where bts.data_cancellazione is null  "
			+ "         and bts.sportello_cod = :codSportello)   "
			+ " )     "
			+ " and btgf.data_cancellazione is null     "
			+ " group by btgf.finanziamento_id  "
			+ " order by coalesce(btgf.area_id,0) ASC ";

	/*
	 * Controllo stato graduatoria:
	 * - true, se lo stato della graduatoria e' uguale allo stato controllato
	 */
	private static final String CHECK_STATO_GRADUATORIA = "select ( "
			+ "    case  "
			+ "        when ( "
			+ "            select bdgs.graduatoria_stato_cod  "
			+ "            from bres_r_graduatoria_stato brgs, bres_d_graduatoria_stato bdgs "
			+ "            where bdgs.graduatoria_stato_id  = brgs.graduatoria_stato_id  "
			+ "			   and bdgs.data_cancellazione is null "
			+ "            and now() between coalesce(brgs.validita_inizio, now()) and coalesce(brgs.validita_fine,now()) "
			+ "            and brgs.graduatoria_id  = ( "
			+ "                select btg.graduatoria_id  "
			+ "                from bres_t_graduatoria btg, bres_r_graduatoria_stato brgs  "
			+ "                where btg.graduatoria_id = brgs.graduatoria_id "
			+ "                and btg.data_cancellazione is null "
			+ "                and brgs.data_cancellazione is null  "
			+ "                and now() between coalesce(brgs.validita_inizio, now()) and coalesce(brgs.validita_fine,now()) "
			+ "                and btg.sportello_id in ( "
			+ "                    select bts.sportello_id "
			+ "                    from bres_t_sportello bts "
			+ "                    where bts.data_cancellazione is null "
			+ "                    and bts.sportello_cod = :codSportello) "
			+ "            ) "
			+ "        ) = :statoGraduatoria "
			+ "        then true "
			+ "        else false "
			+ "    end "
			+ ") as controllo";

	private static final String CHECK_STATO_GRADUATORIA_BY_ID = "select ( "
			+ "	case  "
			+ "		when ( "
			+ "			select bdgs.graduatoria_stato_cod  "
			+ "			from bres_r_graduatoria_stato brgs, bres_d_graduatoria_stato bdgs "
			+ "			where bdgs.graduatoria_stato_id  = brgs.graduatoria_stato_id "
			+ " 		and now() between coalesce(brgs.validita_inizio, now()) and coalesce(brgs.validita_fine,now())"
			+ "			and brgs.graduatoria_id  = :idGraduatoria "
			+ "		) = :statoGraduatoria "
			+ "		then true "
			+ "		else false "
			+ "	end " + ") as controllo ";

	private static final String SELECT_GRADUATORIA_DESC = "select btg.graduatoria_desc as descrizioneGraduatoria, btg.graduatoria_cod as codiceGraduatoria, "
			+ "	bdgs.graduatoria_stato_desc as statoGraduatoria "
			+ "from bres_t_graduatoria btg, bres_r_graduatoria_stato brgs2, bres_d_graduatoria_stato bdgs "
			+ "where btg.data_cancellazione is null    "
			+ "and btg.graduatoria_id  = ( "
			+ "		select btg.graduatoria_id from bres_t_graduatoria btg, bres_r_graduatoria_stato brgs  "
			+ "		where btg.graduatoria_id = brgs.graduatoria_id "
			+ "		and btg.data_cancellazione is null "
			+ "		and brgs.data_cancellazione is null  "
			+ "		and now() between coalesce(brgs.validita_inizio, now()) and coalesce(brgs.validita_fine,now()) "
			+ "		and btg.sportello_id in ( "
			+ "			select bts.sportello_id "
			+ "			from bres_t_sportello bts "
			+ "			where bts.data_cancellazione is null "
			+ "			and bts.sportello_cod = :codSportello) "
			+ ") "
			+ "and brgs2.graduatoria_id  = btg.graduatoria_id "
			+ "and bdgs.graduatoria_stato_id = brgs2.graduatoria_stato_id "
			+ "and now() between coalesce(brgs2.validita_inizio, now()) and coalesce(brgs2.validita_fine,now()) ";

	private static final String CHECK_PUBBLICAZIONE_GRADUATORIA = "select ( "
			+ "    case  "
			+ "        when ( "
			+ "            select count(*) as numero_domande "
			+ "            from bres_t_domanda_dettaglio btdd, bres_t_domanda btd  "
			+ "            where btdd.domanda_stato_id in ( "
			+ "                select bdds.domanda_stato_id  "
			+ "                from bres_d_domanda_stato bdds "
			+ "                where bdds.domanda_stato_cod in ( "
			+ "                    'INVIATA', 'PRESA_IN_CARICO', 'DA_RETTIFICARE',  "
			+ "                    'IN_RETTIFICA', 'RETTIFICATA',  "
			+ "                    'PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA', "
			+ "                    'AMMISSIBILE', 'NON_AMMISSIBILE', 'CONTRODEDOTTA' "
			+ "                ) "
			+ "            ) "
			+ "            and btdd.sportello_id = ( "
			+ "                select bts.sportello_id  "
			+ "                from bres_t_sportello bts  "
			+ "                where bts.sportello_cod = :sportelloCod "
			+ "            ) "
			+ "            and btd.domanda_id = btdd.domanda_id "
			+ "            and btd.data_cancellazione is null "
			+ "            and btdd.data_cancellazione is null "
			+ "            and now() between btdd.validita_inizio and coalesce (btdd.validita_fine, now()) "
			+ "            ) > 0 "
			+ "        then false "
			+ "        else true "
			+ "    end "
			+ ") ";

	private static final String UPDATE_R_GRADUATORIA_STATO = "UPDATE bres_r_graduatoria_stato   "
			+ "set validita_fine = now() "
			+ "where now() between coalesce (validita_inizio, now()) and coalesce(validita_fine,now()) "
			+ "and data_cancellazione is null "
			+ "and graduatoria_id = :idGraduatoria; " + "				 "
			+ "INSERT INTO bres_r_graduatoria_stato (graduatoria_id, graduatoria_stato_id, utente_creazione, utente_modifica) "
			+ "values (:idGraduatoria, "
			+ "(select graduatoria_stato_id  from bres_d_graduatoria_stato  "
			+ "where graduatoria_stato_cod = :statoGraduatoria " + "and data_cancellazione is null  "
			+ "and now() between coalesce (validita_inizio, now()) and coalesce(validita_fine,now()) "
			+ "), :utenteCreazione, :utenteModifica) ";

	private static final String SELECT_DOMANDE_PUBBLICA_GRADUATORIA = "select "
			+ "	btdd.domanda_det_id, "
			+ "	btd.domanda_numero, "
			+ "	btd.richiedente_cf, "
			+ "	btdd.sportello_id, "
			+ "	case "
			+ "		when btdd.domanda_stato_id = (  "
			+ "		select bdds.domanda_stato_id "
			+ "		from bres_d_domanda_stato bdds  "
			+ "		where bdds.domanda_stato_cod = (:stato1)  "
			+ "		)   "
			+ "		then true  "
			+ "		else false  "
			+ "	end as riserva "
			+ "from "
			+ "	bres_t_domanda btd, "
			+ "	bres_t_domanda_dettaglio btdd, "
			+ "	bres_t_graduatoria_dettaglio btgd2 "
			+ "where "
			+ "	btd.domanda_id = btdd.domanda_id "
			+ "	and btdd.domanda_stato_id in (  "
			+ "		select bdds.domanda_stato_id "
			+ "		from bres_d_domanda_stato bdds  "
			+ "		where bdds.domanda_stato_cod = (:stato) "
			+ "			or bdds.domanda_stato_cod = (:stato1)   "
			+ "		)  "
			+ "	and btdd.domanda_id in ( "
			+ "		select btgd.domanda_id  "
			+ "		from bres_t_graduatoria_dettaglio btgd "
			+ "		where btgd.graduatoria_id = ( "
			+ "			select btg.graduatoria_id "
			+ "			from bres_t_graduatoria btg, "
			+ "				bres_r_graduatoria_stato brgs "
			+ "			where btg.graduatoria_id = brgs.graduatoria_id "
			+ "			and btg.data_cancellazione is null  "
			+ "			and brgs.data_cancellazione is null "
			+ "			and now() between coalesce(brgs.validita_inizio, now()) and coalesce(brgs.validita_fine, now()) "
			+ "			and btg.sportello_id in (  "
			+ "				select bts.sportello_id "
			+ "				from bres_t_sportello bts  "
			+ "				where bts.data_cancellazione is null and bts.sportello_cod = :codSportello)   "
			+ "			) "
			+ "		and btgd.data_cancellazione is null   "
			+ "	)  "
			+ "	and btgd2.finanziamento_id = ( "
			+ "			select finanziamento_id   "
			+ "			from bres_t_graduatoria_finanziamento btgf  "
			+ "			where graduatoria_id = ( "
			+ "			select btg.graduatoria_id "
			+ "			from bres_t_graduatoria btg, "
			+ "				bres_r_graduatoria_stato brgs "
			+ "			where btg.graduatoria_id = brgs.graduatoria_id "
			+ "			and btg.data_cancellazione is null  "
			+ "			and brgs.data_cancellazione is null "
			+ "			and now() between coalesce(brgs.validita_inizio, now()) and coalesce(brgs.validita_fine, now()) "
			+ "			and btg.sportello_id in (  "
			+ "				select bts.sportello_id "
			+ "				from bres_t_sportello bts  "
			+ "				where bts.data_cancellazione is null and bts.sportello_cod = :codSportello)   "
			+ "			) "
			+ "			and coalesce(area_id,0) = coalesce(:idArea,0) "
			+ "	) "
			+ "	and btgd2.domanda_id  = btdd.domanda_id "
			+ "	and btd.data_cancellazione is null "
			+ "	and btdd.data_cancellazione is null "
			+ "	and now() between btdd.validita_inizio and coalesce(btdd.validita_fine, now()) "
			+ "	and btgd2.data_cancellazione is null "
			+ "	order by btgd2.posizione; ";

	private static final String SELECT_GRADUATORIA_ID_BY_SPORTELLO_COD = "select btg.graduatoria_id from bres_t_graduatoria btg, bres_r_graduatoria_stato brgs  "
			+ "			where btg.graduatoria_id = brgs.graduatoria_id "
			+ "			and btg.data_cancellazione is null "
			+ "			and brgs.data_cancellazione is null  "
			+ "			and now() between coalesce(brgs.validita_inizio, now()) and coalesce(brgs.validita_fine,now()) "
			+ "			and btg.sportello_id in ( "
			+ "				select bts.sportello_id "
			+ "				from bres_t_sportello bts "
			+ "				where bts.data_cancellazione is null "
			+ "				and bts.sportello_cod = :codSportello) ";

	private static final String SELECT_GRADUATORIA_ID_BY_SPORTELLO_ID = "select (case when (select count(btg.graduatoria_id) from bres_t_graduatoria btg, bres_r_graduatoria_stato brgs  "
			+ "			where btg.graduatoria_id = brgs.graduatoria_id "
			+ "			and btg.data_cancellazione is null "
			+ "			and brgs.data_cancellazione is null  "
			+ "			and now() between coalesce(brgs.validita_inizio, now()) and coalesce(brgs.validita_fine,now()) "
			+ "			and btg.sportello_id = :idSportello) > 0 then "
			+ "(select btg.graduatoria_id from bres_t_graduatoria btg, bres_r_graduatoria_stato brgs  "
			+ "			where btg.graduatoria_id = brgs.graduatoria_id "
			+ "			and btg.data_cancellazione is null "
			+ "			and brgs.data_cancellazione is null  "
			+ "			and now() between coalesce(brgs.validita_inizio, now()) and coalesce(brgs.validita_fine,now()) "
			+ "			and btg.sportello_id = :idSportello) "
			+ "else 0 end) ";

	private static final String SELECT_GRADUATORIA_ID_BY_DOMANDA_DETTAGLIO_ID = "select (case when ((select count(*)  from bres_t_graduatoria btg, bres_t_graduatoria_dettaglio btgd, bres_t_domanda_dettaglio btdd  "
			+ "	where btg.graduatoria_id = btgd.graduatoria_id  "
			+ "	and btdd.domanda_id = btgd.domanda_id "
			+ "	and btg.data_cancellazione is null "
			+ "	and btgd.data_cancellazione is null "
			+ "	and btdd.data_cancellazione is null "
			+ "	and btg.data_creazione = (select max(btg2.data_creazione) from bres_t_graduatoria btg2 where btg2.data_cancellazione is null) "
			+ "	and btdd.domanda_det_id  =:idDetDomanda "
			+ "	) > 0) then (select btg.graduatoria_id  from bres_t_graduatoria btg, bres_t_graduatoria_dettaglio btgd, bres_t_domanda_dettaglio btdd  "
			+ "	where btg.graduatoria_id = btgd.graduatoria_id  "
			+ "	and btdd.domanda_id = btgd.domanda_id "
			+ "	and btg.data_cancellazione is null "
			+ "	and btgd.data_cancellazione is null "
			+ "	and btdd.data_cancellazione is null "
			+ "	and btg.data_creazione = (select max(btg2.data_creazione) from bres_t_graduatoria btg2 where btg2.data_cancellazione is null) "
			+ "	and btdd.domanda_det_id  =:idDetDomanda "
			+ "	) "
			+ "else 0 end ) ";

	private static final String SELECT_IMPORTO_TOTALE_MENSILE_GRADUATORIA = "select (finanziamento_importo_mensile*finanziamento_mesi)as importo from bres_t_graduatoria_finanziamento btgf  "
			+ "where graduatoria_id = :idGraduatoria "
			+ "and area_id is null;";

	private static final String DELETE_GRADUATORIA_DETTAGLIO = "delete from bres_t_graduatoria_dettaglio "
			+ "			where data_cancellazione is null and graduatoria_id  = :idGraduatoria  ; ";

	/*
	 * true, se la graduatoria dello sportello selezionato esiste gia'
	 */
	private static final String CHECK_ESISTENZA_GRADUATORIA = "select "
			+ "	case "
			+ "		when ( "
			+ "		select count(*) "
			+ "		from bres_t_graduatoria btg "
			+ "		where btg.sportello_id = ( "
			+ "			select bts.sportello_id "
			+ "			from bres_t_sportello bts "
			+ "			where bts.sportello_cod = :codSportello  "
			+ "			) "
			+ "		and btg.data_cancellazione is null "
			+ "		) = 0  "
			+ "		then false "
			+ "		else true "
			+ "	end as controllo ";

	private static final String UPDATE_IMPORTO_DOMANDE_GRADUATORIA1 = "update bres_t_graduatoria_dettaglio  "
			+ "set importo = 0 "
			+ "where graduatoria_id = :idGraduatoria; ";

	private static final String UPDATE_IMPORTO_DOMANDE_GRADUATORIA2 = " update bres_t_graduatoria_dettaglio  "
			+ "set importo = :importo  "
			+ "where domanda_id = (select btdd.domanda_id  from bres_t_domanda_dettaglio btdd where btdd.domanda_det_id = :idDettaglio) "
			+ "and graduatoria_id = :idGraduatoria ";

	public ModelSportello getUltimoSportelloChiuso() throws DatabaseException {
		try {
			return jdbcTemplate.query(SELECT_ULTIMO_SPORTELLO_CHIUSO, new SportelloMapper()).get(0);
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "getUltimoSportelloChiuso";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public List<ModelSportello> getSportelliChiusi() throws DatabaseException {
		try {
			return jdbcTemplate.query(SELECT_SPORTELLI_CHIUSI, new SportelloMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "getSportelliChiusi";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public List<ModelGraduatoriaOrdinamento> getGraduatoriaOrdinamento() throws DatabaseException {
		try {
			return jdbcTemplate.query(SELECT_GRADUATORIA_ORDINAMENTO, new GraduatoriaOrdinamentoMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "getGraduatoriaOrdinamento";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public List<ModelArea> getAree() throws DatabaseException {
		try {
			return jdbcTemplate.query(SELECT_AREE, new AreaMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "getAree";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public long insertTGraduatoria(String codSportello, String descGraduatoria, String utente)
			throws DatabaseException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("sportelloCod", codSportello);
			params.addValue("descGraduatoria", descGraduatoria);
			params.addValue("utenteCreazione", utente);
			params.addValue("utenteModifica", utente);

			jdbcTemplate.update(INSERT_T_GRADUATORIA, params, keyHolder,
					new String[] { "graduatoria_id" });
			return keyHolder.getKey().longValue();
		} catch (Exception e) {
			String methodName = "insertTGraduatoria";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public int insertRGraduatoriaStato(long graduatoriaId, String stato, String utente) throws DatabaseException {

		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("idGraduatoria", graduatoriaId, Types.BIGINT);
			params.addValue("statoGraduatoria", stato);
			params.addValue("utenteCreazione", utente);
			params.addValue("utenteModifica", utente);

			return jdbcTemplate.update(INSERT_R_GRADUATORIA_STATO, params);
		} catch (Exception e) {
			String methodName = "insertRGraduatoriaStato";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public int insertRGraduatoriaOrdinamento(long graduatoriaId, long idOrdinamento, String utente)
			throws DatabaseException {

		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("idGraduatoria", graduatoriaId, Types.BIGINT);
			params.addValue("idOrdinamento", idOrdinamento, Types.BIGINT);
			params.addValue("utenteCreazione", utente);
			params.addValue("utenteModifica", utente);

			return jdbcTemplate.update(INSERT_R_GRADUATORIA_ORDINAMENTO, params);
		} catch (Exception e) {
			String methodName = "insertRGraduatoriaOrdinamento";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public void insertTGraduatoriaFinanziamento(ModelNuovaGraduatoria nGraduatoria, Long graduatoriaId,
			List<ModelArea> aree, String cf) throws DatabaseException {
		try {
			for (int i = 0; i < aree.size() + 1; i++) {
				MapSqlParameterSource params = new MapSqlParameterSource();
				params.addValue("descrizione", nGraduatoria.getDescrizione());
				params.addValue("importoMensile", nGraduatoria.getImportoMensile(), Types.BIGINT);
				params.addValue("mesi", nGraduatoria.getNumeroMesi(), Types.BIGINT);
				params.addValue("idGraduatoria", graduatoriaId, Types.BIGINT);
				params.addValue("priorita", (i + 1), Types.BIGINT);
				params.addValue("utenteCreazione", cf);
				params.addValue("utenteModifica", cf);
				params.addValue("finanziamentoRicevuto", 0, Types.DECIMAL);
				switch (i) {
					case 0:
						params.addValue("importoTotale", nGraduatoria.getImportoA1(), Types.DECIMAL);
						params.addValue("idArea", aree.get(i).getAreaId(), Types.BIGINT);
						break;
					case 1:
						params.addValue("importoTotale", nGraduatoria.getImportoA2(), Types.DECIMAL);
						params.addValue("idArea", aree.get(i).getAreaId(), Types.BIGINT);
						break;
					default:
						params.addValue("importoTotale", nGraduatoria.getImportoGenerale(), Types.DECIMAL);
						params.addValue("idArea", null, Types.BIGINT);
						break;
				}
				jdbcTemplate.update(INSERT_T_GRADUATORIA_FINANZIAMENTO, params);
			}
			return;
		} catch (Exception e) {
			String methodName = "INSERT_T_DATO_DA_MODIFICARE";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public List<ModelDomandeAmmissibili> getDomandeAmmissibili(String codSportello) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("sportelloCod", codSportello);
			params.addValue("stato", Constants.AMMISSIBILE);
			return jdbcTemplate.query(SELECT_DOMANDE_AMMISSIBILI, params, new DomandeAmmissibiliMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "getDomandeAmmissibili";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public List<Long> getDomandeAmmesseSportelli(String codSportello, List<ModelGraduatoriaOrdinamento> ordinamenti)
			throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		String query = SELECT_DOMANDE_AMMESSE_SPORTELLI;

		try {
			query += "order by ";
			for (int i = 0; i < ordinamenti.size(); i++) {
				if (ordinamenti.get(i).getOrdinamentoCampo().equals("domanda_data")) {
					query += " " + ordinamenti.get(i).getOrdinamentoTabella() + "."
							+ ordinamenti.get(i).getOrdinamentoCampo() + " "
							+ ordinamenti.get(i).getOrdinamentoDirezione();
				} else if (ordinamenti.get(i).getOrdinamentoCampo().equals("punteggio_sociale")) {
					query += "  coalesce(CAST(" + ordinamenti.get(i).getOrdinamentoTabella()
							+ ".verifica_eg_punteggio_sociale AS numeric),"
							+ "coalesce( " + ordinamenti.get(i).getOrdinamentoTabella() + "."
							+ ordinamenti.get(i).getOrdinamentoCampo() + ", 0) ) "
							+ ordinamenti.get(i).getOrdinamentoDirezione();
				} else {
					query += "  coalesce(" + ordinamenti.get(i).getOrdinamentoTabella() + "."
							+ ordinamenti.get(i).getOrdinamentoCampo() + ",0) "
							+ ordinamenti.get(i).getOrdinamentoDirezione();
				}

				if (i < ordinamenti.size() - 1) {
					query += ", ";
				} else {
					query += " ";
				}
			}
			params.addValue("codSportello", codSportello);
			params.addValue("stato", Constants.AMMESSA);
			params.addValue("stato1", Constants.AMMESSA_RISERVA);
			return jdbcTemplate.query(query, params, (rs, rowNum) -> rs.getLong("domanda_det_id"));
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "getDomandeAmmesseSportelli";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public void deleteGraduatoria(String codSportello) throws DatabaseException {

		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("codSportello", codSportello);
			jdbcTemplate.update(DELETE_GRADUATORIA, params);
			return;
		} catch (Exception e) {
			String methodName = "deleteGraduatoria";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public int[] insertDomandeGraduatoria(String sportelloCod, List<Long> dettIds, String cf) throws DatabaseException {
		SqlParameterSource[] batchArgs = new SqlParameterSource[dettIds.size()];

		for (int i = 0; i < dettIds.size(); i++) {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("idDettaglio", dettIds.get(i));
			params.addValue("posizione", (i + 1));
			params.addValue("importo", 0);
			params.addValue("utenteCreazione", cf);
			params.addValue("utenteModifica", cf);
			params.addValue("codSportello", sportelloCod);
			batchArgs[i] = params;
		}
		try {
			return jdbcTemplate.batchUpdate(INSERT_T_GRADUATORIA_DETTAGLIO, batchArgs);
		} catch (Exception e) {
			String methodName = "getDomandeGraduatoria";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public void updateGraduatoriaFinanziamento(long graduatoriaId, List<ModelArea> aree) throws DatabaseException {
		try {
			for (int i = 0; i < aree.size(); i++) {
				MapSqlParameterSource params = new MapSqlParameterSource();
				params.addValue("idGraduatoria", graduatoriaId, Types.BIGINT);
				params.addValue("idArea", aree.get(i).getAreaId(), Types.BIGINT);
				jdbcTemplate.update(UPDATE_FINANAZIAMENTO_GRADUATORIA, params);
			}
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("idGraduatoria", graduatoriaId, Types.BIGINT);
			params.addValue("idArea", null, Types.BIGINT);
			jdbcTemplate.update(UPDATE_FINANAZIAMENTO_GRADUATORIA, params);
			return;
		} catch (Exception e) {
			String methodName = "updateGraduatoriaFinanziamento";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public List<ModelDomandeGraduatoria> getDomandeGraduatoria(String sportelloCod) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("codSportello", sportelloCod);
			return jdbcTemplate.query(SELECT_DOMANDE_IN_GRADUATORIA, params, new DomandeInGraduatoriaMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "getDomandeGraduatoria";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public List<ModelParametriFinanziamento> getParametriFinanziamento(String sportelloCod) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("codSportello", sportelloCod);
			return jdbcTemplate.query(SELECT_PARAMETRI_FINANZIAMENTO_GRADUATORIA, params,
					new ParametriFinanziamentoMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "getDomandeGraduatoria";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public Boolean checkStatoGraduatoria(String sportelloCod, String stato) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("codSportello", sportelloCod);
			params.addValue("statoGraduatoria", stato);
			return jdbcTemplate.queryForObject(CHECK_STATO_GRADUATORIA, params, Boolean.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "checkStatoGraduatoria";
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

	public ModelDescrizioneGraduatoria selectGraduatoriaDesc(String sportelloCod) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("codSportello", sportelloCod);
			return jdbcTemplate.queryForObject(SELECT_GRADUATORIA_DESC, params, new DescrizioneGraduatoriaMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "selectGraduatoriaDesc";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public Boolean checkPubblicazioneGraduatoria(String sportelloCod) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("sportelloCod", sportelloCod);
			return jdbcTemplate.queryForObject(CHECK_PUBBLICAZIONE_GRADUATORIA, params, Boolean.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "getDomandeGraduatoria";
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

	public List<ModelDomandeAmmissibili> getDomandePubblicaGraduatoria(String codSportello, long idArea)
			throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("codSportello", codSportello);
			params.addValue("stato", Constants.AMMESSA);
			params.addValue("stato1", Constants.AMMESSA_RISERVA);
			params.addValue("idArea", idArea);

			return jdbcTemplate.query(SELECT_DOMANDE_PUBBLICA_GRADUATORIA, params, new DomandeAmmissibiliMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "getDomandeAmmissibili";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public Long selectGraduatoriaIdBySportelloCod(String sportelloCod) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("codSportello", sportelloCod);
		try {
			return jdbcTemplate.queryForObject(SELECT_GRADUATORIA_ID_BY_SPORTELLO_COD, params, Long.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "selectGraduatoriaIdBySportelloCod";
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

	public Long selectGraduatoriaIdByDomandaDettaglioId(BigDecimal idDetDomanda) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("idDetDomanda", idDetDomanda);
		try {
			return jdbcTemplate.queryForObject(SELECT_GRADUATORIA_ID_BY_DOMANDA_DETTAGLIO_ID, params, Long.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "selectGraduatoriaIdBySportelloCod";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public Double selectImportoTotaleMensileByIdGraduatoria(long idGraduatoria) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("idGraduatoria", idGraduatoria);
		try {
			return jdbcTemplate.queryForObject(SELECT_IMPORTO_TOTALE_MENSILE_GRADUATORIA, params, Double.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "selectGraduatoriaIdBySportelloCod";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public int deleteGraduatoriaDettaglio(long idGraduatoria) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("idGraduatoria", idGraduatoria);
		try {
			return jdbcTemplate.update(DELETE_GRADUATORIA_DETTAGLIO, params);
		} catch (Exception e) {
			String methodName = "deleteGraduatoriaDettaglio";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public Boolean checkEsistenzaGraduatoria(String sportelloCod) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("codSportello", sportelloCod);
		try {
			return jdbcTemplate.queryForObject(CHECK_ESISTENZA_GRADUATORIA, params, Boolean.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "deleteGraduatoriaDettaglio";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public int[] updateImportoDomandeGraduatoria(double importo, List<ModelDomandeAmmissibili> domandeGraduatoria,
			Long gradId)
			throws DatabaseException {
		MapSqlParameterSource params1 = new MapSqlParameterSource();
		params1.addValue("idGraduatoria", gradId);

		SqlParameterSource[] batchArgs = new SqlParameterSource[domandeGraduatoria.size()];
		for (int i = 0; i < domandeGraduatoria.size(); i++) {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("idDettaglio", domandeGraduatoria.get(i).getDettId());
			params.addValue("importo", importo);
			params.addValue("idGraduatoria", gradId);
			batchArgs[i] = params;
		}
		try {
			jdbcTemplate.update(UPDATE_IMPORTO_DOMANDE_GRADUATORIA1, params1);
			return jdbcTemplate.batchUpdate(UPDATE_IMPORTO_DOMANDE_GRADUATORIA2, batchArgs);
		} catch (Exception e) {
			String methodName = "getDomandeGraduatoria";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}
}