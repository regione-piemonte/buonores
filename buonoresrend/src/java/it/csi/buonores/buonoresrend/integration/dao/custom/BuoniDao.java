/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresrend.integration.dao.custom;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import it.csi.buonores.buonoresrend.dto.ModelBuono;
import it.csi.buonores.buonoresrend.exception.DatabaseException;
import it.csi.buonores.buonoresrend.integration.dao.utils.ListaBuoniMapper;
import it.csi.buonores.buonoresrend.util.LoggerUtil;

@Repository
public class BuoniDao extends LoggerUtil {

	//Prima versione query per estrazione buoni per servizio gestrutture
	// SECONDA VERSIONe query buoni eliminata la data_creazione legata al buono creato introdotto caso base per data rendicontazione
	public static final String SELECT_BUONI_ATTIVI_OLD = "	select buono.buono_id, "
			+ "				buono.buono_cod, "
			+ "				buono.sportello_id, "
			+ "				domanda.domanda_numero, "
			+ "				domanda.beneficiario_cf, "
			+ "				to_char((btdd.validita_inizio + interval '1 months'),'yyyy-mm-01') as data_rendicontazione, "
			+ "					CASE "
			+ "				     WHEN brbs.buono_stato_id = (select buono_stato_id  from bres_d_buono_stato where data_cancellazione is null and  buono_stato_cod IN ('REVOCATO','REVOCATO_PRESA_VISIONE')) THEN brbs.data_creazione  "
			+ "				     when brbs.buono_stato_id = (select buono_stato_id from bres_d_buono_stato where data_cancellazione is null and buono_stato_cod='RINUNCIATO') THEN brbs.data_creazione  "
			+ "				     ELSE null "
			+ "				   END data_fine_validita, "
			+ "				buonostato.buono_stato_cod, "
			+ "				btbf.rsa_cod,  "
			+ "				btbf.rsa_cfpiva, "
			+ "				btc.contratto_data_inizio, "
			+ "				btc.contratto_data_fine "
			+ "				from bres_t_buono buono  "
			+ "				join bres_r_buono_stato brbs on buono.buono_id =brbs.buono_id  "
			+ "				join bres_d_buono_stato buonostato on brbs.buono_stato_id =buonostato.buono_stato_id "
			+ "				join bres_t_domanda domanda on buono.domanda_id =domanda.domanda_id  "
			+ "				join bres_t_buono_fornitore btbf on btbf.buono_id =buono.buono_id "
			+ "				join bres_t_contratto btc on btc.fornitore_id =btbf.fornitore_id "
			+ "				join bres_t_domanda_dettaglio btdd  on btdd.domanda_det_id = buono.domanda_det_id  "
			+ "				where "
			+ "				buono.data_cancellazione is null  "
			+ "				and buonostato.data_cancellazione is null  "
			+ "				and brbs.data_cancellazione is null "
			+ "				and brbs.validita_fine is null "
			+ "				and domanda.data_cancellazione is null "
			+ "				and btbf.data_cancellazione is null  "
			+ "				and btc.data_cancellazione is null "
			+ "				and btdd.data_cancellazione is null ";
	
	public static final String SELECT_BUONI_ATTIVI="select buono.buono_id, "
			+ "						buono.buono_cod, "
			+ "						buono.sportello_id, "
			+ "						domanda.domanda_numero," 
			+ "						domanda.beneficiario_cf, "
			+ "						buono.decorrenza_inizio as data_rendicontazione, "
			+ "						case "
			+ "						when buono.decorrenza_fine  <= coalesce(brbs.rinuncia_data_decorrenza , buono.decorrenza_fine) and buono.decorrenza_fine <= coalesce(brbs.revoca_data_decorrenza , buono.decorrenza_fine) then buono.decorrenza_fine "
			+ "						when brbs.rinuncia_data_decorrenza <= coalesce(brbs.revoca_data_decorrenza, brbs.rinuncia_data_decorrenza) then brbs.rinuncia_data_decorrenza "
			+ "						else brbs.revoca_data_decorrenza "
			+ "						end as data_fine_validita, "
			+ "						buonostato.buono_stato_cod, "
			+ "						btbf.rsa_cod, "
			+ "						btbf.rsa_cfpiva,"
			+ "						btc.contratto_data_inizio, "
			+ "						btc.contratto_data_fine, "
			+ "						btc.data_invio_a_bandi "
			+ "						from bres_t_buono buono "
			+ "						join bres_r_buono_stato brbs on buono.buono_id =brbs.buono_id "
			+ "						join bres_d_buono_stato buonostato on brbs.buono_stato_id =buonostato.buono_stato_id"
			+ "						join bres_t_domanda domanda on buono.domanda_id =domanda.domanda_id "
			+ "						join bres_t_buono_fornitore btbf on btbf.buono_id =buono.buono_id "
			+ "						join bres_t_contratto btc on btc.fornitore_id =btbf.fornitore_id "
			+ "						join bres_t_domanda_dettaglio btdd  on btdd.domanda_det_id = buono.domanda_det_id "
			+ "						where"
			+ "						buono.data_cancellazione is null "
			+ "						and buonostato.data_cancellazione is null "
			+ "						and brbs.data_cancellazione is null "
			+ "						and brbs.validita_fine is null "
			+ "						and domanda.data_cancellazione is null "
			+ "						and btbf.data_cancellazione is null "
			+ "						and btc.data_cancellazione is null "
			+ "						and btdd.data_cancellazione is null "
			+ "						order by buono.buono_id,btc.contratto_id ";
	
	private static final String SELECT_BUONI_ATTIVI_ISEE=" with statoisee as ( "
			+ "			(select distinct on (buono.buono_id, btbi.isee_anno) buono.buono_id, btbi.isee_anno, btbi.isee_id, "
			+ "			case "
			+ "			when bdbive.isee_verifica_esito_cod ='CONFORME' or bdbive.isee_verifica_esito_cod ='NON CONFORME' then bdbive.isee_verifica_esito_cod "
			+ "			else null "
			+ "			end as isee_esito "
			+ "			from bres_t_buono buono "
			+ "			left join bres_t_buono_isee btbi on buono.buono_id = btbi.buono_id "
			+ "			left join bres_d_buono_isee_verifica_esito bdbive on btbi.isee_verifica_esito_id =bdbive.isee_verifica_esito_id "
			+ "			where "
			+ "			buono.data_cancellazione is null "
			+ "			and btbi.data_cancellazione is null "
			+ "			and bdbive.data_cancellazione is null "
			+ "			and ( bdbive.isee_verifica_esito_cod in ('CONFORME','NON CONFORME')"
			+ "			) order by buono.buono_id, btbi.isee_anno, btbi.isee_id desc "
			+ "			)  "
			+ "			union all "
			+ "			(select  buono_id, to_char(date_part('year', btdd.isee_data_rilascio)) as isee_anno, btb.buono_id as isee_id,'CONFORME' as isee_esito "
			+ "			from bres_t_buono btb "
			+ "			join bres_t_domanda btd on btd.domanda_id = btb.domanda_id "
			+ "				join bres_t_domanda_dettaglio btdd on btd.domanda_id = btdd.domanda_id "
			+ "				join bres_d_domanda_stato bdds on btdd.domanda_stato_id = bdds.domanda_stato_id "
			+ "		    where btb.data_cancellazione is null "
			+ "			and btd.data_cancellazione is null "
			+ "			and btdd.data_cancellazione is null "
			+ "		    and bdds.domanda_stato_cod = 'IN_PAGAMENTO') order by isee_anno desc    ),"
			+ "			statofiltrato as ( "
			+ "		   		select distinct on (buono_id, isee_anno) buono_id, isee_anno, isee_id,isee_esito "
			+ "		   		from statoisee "
			+ "		   		order by buono_id, isee_anno, isee_id desc "
			+ "		   		) "
			+ "			select buono.buono_id, "
			+ "			buono.buono_cod, "
			+ "			buono.sportello_id, "
			+ "			domanda.domanda_numero, "
			+ "			domanda.beneficiario_cf, "
			+ "			buono.decorrenza_inizio as data_rendicontazione, "
			+ "			case "
			+ "			when buono.decorrenza_fine  <= coalesce(brbs.rinuncia_data_decorrenza , buono.decorrenza_fine) and buono.decorrenza_fine <= coalesce(brbs.revoca_data_decorrenza , buono.decorrenza_fine) then buono.decorrenza_fine "
			+ "			when brbs.rinuncia_data_decorrenza <= coalesce(brbs.revoca_data_decorrenza, brbs.rinuncia_data_decorrenza) then brbs.rinuncia_data_decorrenza "
			+ "			else brbs.revoca_data_decorrenza "
			+ "			end as data_fine_validita, "
			+ "			buonostato.buono_stato_cod, "
			+ "			btbf.rsa_cod, "
			+ "			btbf.rsa_cfpiva, "
			+ "			btc.contratto_data_inizio, "
			+ "			btc.contratto_data_fine, "
			+ "			btc.data_invio_a_bandi, "
			+ "			string_agg(statofiltrato.isee_anno || ':' || statofiltrato.isee_esito, ',' order by statofiltrato.isee_anno desc ) as verifica_isee_corrente "
			+ "			from bres_t_buono buono "
			+ "			join bres_r_buono_stato brbs on buono.buono_id =brbs.buono_id "
			+ "			join bres_d_buono_stato buonostato on brbs.buono_stato_id =buonostato.buono_stato_id "
			+ "			join bres_t_domanda domanda on buono.domanda_id =domanda.domanda_id "
			+ "			join bres_t_buono_fornitore btbf on btbf.buono_id =buono.buono_id "
			+ "			join bres_t_contratto btc on btc.fornitore_id =btbf.fornitore_id "
			+ "			join bres_t_domanda_dettaglio btdd  on btdd.domanda_det_id = buono.domanda_det_id "
			+ "			left join statofiltrato on statofiltrato.buono_id = buono.buono_id "
			+ "			where "
			+ "			buono.data_cancellazione is null "
			+ "			and buonostato.data_cancellazione is null "
			+ "			and brbs.data_cancellazione is null "
			+ "			and brbs.validita_fine is null "
			+ "			and domanda.data_cancellazione is null "
			+ "			and btbf.data_cancellazione is null "
			+ "			and btc.data_cancellazione is null "
			+ "			and ( btc.contratto_data_fine > buono.decorrenza_inizio or btc.contratto_data_fine is null) "
			+ "			and btc.contratto_id not in ( "
			+ "					select btc.contratto_id "
			+ "					from bres_t_contratto btc "
			+ "					where btc.data_cancellazione is null   "
			+ "					and btc.contratto_data_fine < btc.contratto_data_inizio "
			+ "				) "
			+ "			and btdd.data_cancellazione is null "
			+ "			group by buono.buono_id, "
			+ "			buono.buono_cod, "
			+ "			buono.sportello_id, "
			+ "			domanda.domanda_numero, "
			+ "			domanda.beneficiario_cf, "
			+ "			buono.decorrenza_inizio, "
			+ "			data_fine_validita, "
			+ "			buonostato.buono_stato_cod, "
			+ "			btbf.rsa_cod, "
			+ "			btbf.rsa_cfpiva, "
			+ "			btc.contratto_id, "
			+ "			btc.contratto_data_inizio, "
			+ "			btc.contratto_data_fine, "
			+ "			btc.data_invio_a_bandi "
			+ "			order by buono.buono_id,btc.contratto_id 		"
			;
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public List<ModelBuono> getBuoni() throws DatabaseException {
		List<ModelBuono> buoni = null;
		
		try {
			buoni = jdbcTemplate.query(SELECT_BUONI_ATTIVI_ISEE, new ListaBuoniMapper());
			return buoni;
		} catch (Exception e) {
			generateDatabaseException("SELECT_BUONI_ATTIVI", e);
		}
		
		return buoni;
	}
	
	
	
}
