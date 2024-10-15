/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.integration.dao.custom;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import it.csi.buonores.buonoresbo.dto.ModelBuonoInformazioni;
import it.csi.buonores.buonoresbo.dto.ModelBuonoResponse;
import it.csi.buonores.buonoresbo.dto.ModelFiltriRicercaBuoni;
import it.csi.buonores.buonoresbo.dto.ModelStatoBuono;
import it.csi.buonores.buonoresbo.dto.ModelVerificaInformazioni;
import it.csi.buonores.buonoresbo.dto.custom.ModelBuono;
import it.csi.buonores.buonoresbo.dto.custom.ModelGetAllegato;
import it.csi.buonores.buonoresbo.exception.DatabaseException;
import it.csi.buonores.buonoresbo.integration.dao.utils.AllegatoGetMapper;
import it.csi.buonores.buonoresbo.integration.dao.utils.BuoniResponseMapper;
import it.csi.buonores.buonoresbo.integration.dao.utils.BuonoInformazioniMapper;
import it.csi.buonores.buonoresbo.integration.dao.utils.BuonoMapper;
import it.csi.buonores.buonoresbo.integration.dao.utils.BuonoStatoMapper;
import it.csi.buonores.buonoresbo.integration.dao.utils.VerificaInformazioniMapper;
import it.csi.buonores.buonoresbo.util.Constants;
import it.csi.buonores.buonoresbo.util.LoggerUtil;
import it.csi.buonores.buonoresbo.util.Util;

@Repository
public class BuonoDao extends LoggerUtil {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	private static final String SELECT_STATI_BUONO = "select bdbs.buono_stato_id, bdbs.buono_stato_cod, bdbs.buono_stato_desc  "
			+ "from bres_d_buono_stato bdbs " + "where bdbs.data_cancellazione is null "
			+ "and now() between bdbs.validita_inizio and coalesce(bdbs.validita_fine , now()) ";

	public static final String SELECT_ALLEGATO_BUONO = "select btba.file_name , btba.file_path "
			+ "from bres_t_buono_allegato btba " + "where btba.allegato_id = :allegatoId "
			+ "and btba.data_cancellazione is null;";

	private static final String SELECT_RICERCA_BUONI_PARTE_UNO = "select btb.buono_cod, bdbs.buono_stato_cod, btd.domanda_numero, "
			+ "	btd.richiedente_cf, btd.beneficiario_cf, "
			+ "	btdd.destinatario_nome, btdd.destinatario_cognome, btdd.richiedente_nome, btdd.richiedente_cognome,  "
			+ " to_char(btb.decorrenza_inizio,'DD/MM/YYYY') as decorrenza_inizio, "
			+ "	to_char(btb.decorrenza_fine ,'DD/MM/YYYY') as decorrenza_fine, "
			+ "	to_char(btdd.destinatario_decesso_data, 'DD/MM/YYYY') as data_decesso, "
			+ "	to_char(btdd.destinatario_residenza_extraregione, 'DD/MM/YYYY') as data_cambio_residenza, "
			+ " btbi.isee_conforme, "
			// + " case when bdbive.isee_verifica_esito_cod is null then "
			// + " (select distinct btbi.isee_conforme from bres_t_buono_isee btbi, "
			// + " bres_d_buono_isee_verifica_esito bdbive where btbi.isee_verifica_esito_id
			// = bdbive.isee_verifica_esito_id "
			// + " and isee_verifica_esito_cod='RESPINTO' and btb.buono_id = btbi.buono_id
			// and btb.data_cancellazione is null "
			// + " and bdbive.data_cancellazione is null and btbi.data_cancellazione is
			// null) "
			// + " else btbi.isee_conforme end , "
			// + " case when bdbive.isee_verifica_esito_cod is null then "
			// + " (select distinct bdbive.isee_verifica_esito_cod "
			// + " from bres_t_buono_isee btbi, bres_d_buono_isee_verifica_esito bdbive "
			// + " where btbi.isee_verifica_esito_id = bdbive.isee_verifica_esito_id "
			// + " and isee_verifica_esito_cod='RESPINTO' "
			// + " and btb.buono_id = btbi.buono_id "
			// + " and btb.data_cancellazione is null "
			// + " and bdbive.data_cancellazione is null "
			// + " and btbi.data_cancellazione is null) "
			// + " else bdbive.isee_verifica_esito_cod "
			// + " end "
			+ " bdbive.isee_verifica_esito_cod " + " from bres_r_buono_stato brbs, bres_d_buono_stato bdbs, "
			+ " bres_t_domanda btd, bres_t_domanda_dettaglio btdd,bres_t_buono btb "
			+ " left join bres_t_buono_isee btbi on btbi.buono_id = btb.buono_id "
			+ " and btbi.validita_fine is null and btbi.data_cancellazione is null and btbi.isee_anno = :anno::text "
			+ " left join bres_d_buono_isee_verifica_esito bdbive on "
			+ " bdbive.isee_verifica_esito_id = btbi.isee_verifica_esito_id and bdbive.data_cancellazione is null "
			+ " and bdbive.validita_fine is null " + "where btb.buono_id = brbs.buono_id "
			+ "and brbs.buono_stato_id = bdbs.buono_stato_id " + "and btb.domanda_id = btd.domanda_id  "
			+ "and btd.domanda_id  = btdd.domanda_id " + "and btb.data_cancellazione is null "
			+ "and brbs.data_cancellazione is null "
			+ "and now() between brbs.validita_inizio and coalesce(brbs.validita_fine , now()) "
			+ "and bdbs.data_cancellazione is null "
			+ "and now() between bdbs.validita_inizio and coalesce(bdbs.validita_fine , now()) "
			+ "and btd.data_cancellazione is null " + "and btdd.data_cancellazione is null "
			+ "and now() between btdd.validita_inizio and coalesce(btdd.validita_fine , now()) ";

	public static final String GET_BUONO = "select distinct btd.richiedente_cf richiedenteCf, btb.buono_id buonoId, bdbs.buono_stato_cod stato,"
			+ "btb.buono_cod buonoCod, bdbs.buono_stato_id buonoStatoId, btb.decorrenza_inizio, btb.decorrenza_fine from "
			+ "bres_t_domanda btd, " + "bres_t_buono btb , " + "bres_d_buono_stato bdbs, " + "bres_r_buono_stato brbs "
			+ "where " + "btd.domanda_numero = :numeroDomanda " + "and btd.domanda_id = btb.domanda_id "
			+ "and bdbs.buono_stato_id = brbs.buono_stato_id " + "and brbs.buono_id = btb.buono_id "
			+ "and btd.data_cancellazione is null " + "and btb.data_cancellazione is null "
			+ "and bdbs.data_cancellazione is null " + "and brbs.data_cancellazione is null "
			+ "and brbs.validita_inizio  <= now() " + "and brbs.validita_fine is null";

	public static final String DESCR_CODIFICA = "select distinct buono_stato_motivo_desc from bres_d_buono_stato_motivo "
			+ "where buono_stato_motivo_cod = :codMotivo";

	public static final String SELECT_BUONO_INFORMAZIONI = "select bdbs.buono_stato_cod,  "
			+ "    to_char(brbs.validita_fine, 'DD/MM/YYYY') as validita_fine, "
			+ "    to_char(brbs.rinuncia_data_decorrenza, 'DD/MM/YYYY') as rinuncia_data_decorrenza, "
			+ "    to_char(brbs.revoca_data_decorrenza, 'DD/MM/YYYY') as revoca_data_decorrenza, "
			+ "    brbs.buono_stato_nota " + "from bres_t_buono btb, bres_r_buono_stato brbs, bres_d_buono_stato bdbs "
			+ "where btb.buono_cod = ( " + "    select btb2.buono_cod  " + "    from bres_t_buono btb2  "
			+ "    where btb2.domanda_id = ( " + "        select btd.domanda_id " + "        from bres_t_domanda btd  "
			+ "        where btd.domanda_numero = :numeroDomanda " + "        and btd.data_cancellazione is null "
			+ "    ) " + "    and btb2.data_cancellazione is null " + ") " + "and btb.buono_id = brbs.buono_id  "
			+ "and brbs.buono_stato_id = bdbs.buono_stato_id " + "and btb.data_cancellazione is null "
			+ "and brbs.data_cancellazione is null "
			+ "and now() between brbs.validita_inizio and coalesce (brbs.validita_fine, now()) "
			+ "and bdbs.data_cancellazione is null "
			+ "and now() between bdbs.validita_inizio and coalesce (bdbs.validita_fine, now())";

	public static final String SELECT_VERIFICA_INFORMAZIONI = "select btegv.nessuna_incompatibilita, btegv.verifica_note, to_char(btegv.verifica_data, 'DD/MM/YYYY') as data_verifica  "
			+ "from bres_t_ente_gestore_verifica btegv  " + "where btegv.domanda_id = ( " + "    select btd.domanda_id "
			+ "    from bres_t_domanda btd  " + "    where btd.domanda_numero = :numeroDomanda " + ") "
			+ "and btegv.data_cancellazione is null "
			+ "and now() between btegv.validita_inizio and coalesce (btegv.validita_fine, now())";

	public static final String SELECT_RESPINTO = "select distinct btb.buono_cod "
			+ "from  bres_t_buono_isee btbi, bres_d_buono_isee_verifica_esito bdbive, bres_t_buono btb "
			+ "where btbi.isee_verifica_esito_id = bdbive.isee_verifica_esito_id "
			+ "and isee_verifica_esito_cod='RESPINTO' " + "and btb.buono_id = btbi.buono_id "
			+ "and btb.data_cancellazione is null " + "and bdbive.data_cancellazione is null "
			+ "and btbi.data_cancellazione is null " + "order by btb.buono_cod DESC";

	public List<ModelBuonoResponse> selectRicercaBuoni(ModelFiltriRicercaBuoni filtri) throws DatabaseException {
		String methodName = "SELECT_RICERCA_BUONI";
		List<ModelBuonoResponse> buoni = new ArrayList<ModelBuonoResponse>();
		Calendar cal = new GregorianCalendar();
		int anno = cal.get(Calendar.YEAR);

		String query = "";
		query += SELECT_RICERCA_BUONI_PARTE_UNO;

		// Aggiungo casistica filtri
		if (filtri.getNumeroDomanda() != null) {
			query += "and btd.domanda_id in ( " + "	select btd1.domanda_id " + "	from bres_t_domanda btd1 "
					+ "	where btd1.domanda_numero LIKE '%'||UPPER(:domandaNumero)||'%' " + ")";
		}
		if (filtri.getNumeroBuono() != null) {
			query += "and UPPER(btb.buono_cod) LIKE UPPER(:buonoCod) ";
		}
		if (filtri.getStatoBuono() != null) {
			query += "and bdbs.buono_stato_cod = :buonoStato ";
		}
		if (filtri.getDestinatario() != null) {
			query += "and (UPPER(btdd.destinatario_nome) LIKE UPPER(:destinatario) "
					+ "or UPPER(btdd.destinatario_cognome) LIKE UPPER(:destinatario) "
					+ "or UPPER(btdd.destinatario_nome || ' ' || btdd.destinatario_cognome) LIKE UPPER(:destinatario) "
					+ "or UPPER(btdd.destinatario_cognome || ' ' || btdd.destinatario_nome) LIKE UPPER(:destinatario) "
					+ "or UPPER(btd.beneficiario_cf) LIKE UPPER(:destinatario)) ";
		}
		if (filtri.getRichiedente() != null) {
			query += "and (UPPER(btdd.richiedente_nome) LIKE UPPER(:richiedente) "
					+ "or UPPER(btdd.richiedente_cognome) LIKE UPPER(:richiedente) "
					+ "or UPPER(btdd.richiedente_nome || ' ' || btdd.richiedente_cognome) LIKE UPPER(:richiedente) "
					+ "or UPPER(btdd.richiedente_cognome || ' ' || btdd.richiedente_nome) LIKE UPPER(:richiedente) "
					+ "or UPPER(btd.richiedente_cf) LIKE UPPER(:richiedente)) ";
		}
		if (filtri.getDecessoResidenza() != null) {
			if (filtri.getDecessoResidenza().equalsIgnoreCase("decesso")) {
				query += " and btdd.destinatario_decesso_data is not null ";
			} else if (filtri.getDecessoResidenza().equalsIgnoreCase("Fuori Regione")) {
				query += " and btdd.destinatario_residenza_extraregione is not null ";
			}
		}

		if (Util.isValorizzato(filtri.getIseeConforme()) && !Util.isValorizzato(filtri.getIseeVerificaConforme())) {
			if (filtri.getIseeConforme().equalsIgnoreCase(Constants.SI)) {
				query += " and btbi.isee_conforme = true ";
			} else {
				query += " and btbi.isee_conforme is null ";
			}
		} else if (!Util.isValorizzato(filtri.getIseeConforme())
				&& Util.isValorizzato(filtri.getIseeVerificaConforme())) {
			if (!filtri.getIseeVerificaConforme().equalsIgnoreCase(Constants.VERISEENONINSERITA)) {
				query += " and bdbive.isee_verifica_esito_cod = '" + filtri.getIseeVerificaConforme() + "' ";
			} else {
				query += " and bdbive.isee_verifica_esito_cod is null ";
			}
		} else if (Util.isValorizzato(filtri.getIseeConforme())
				&& Util.isValorizzato(filtri.getIseeVerificaConforme())) {
			if (filtri.getIseeConforme().equalsIgnoreCase(Constants.SI)
					&& !filtri.getIseeVerificaConforme().equalsIgnoreCase(Constants.VERISEENONINSERITA)) {
				query += " and btbi.isee_conforme = true and bdbive.isee_verifica_esito_cod = '"
						+ filtri.getIseeVerificaConforme() + "' ";
			} else if (filtri.getIseeConforme().equalsIgnoreCase(Constants.SI)
					&& filtri.getIseeVerificaConforme().equalsIgnoreCase(Constants.VERISEENONINSERITA)) {
				query += " and btbi.isee_conforme = true and bdbive.isee_verifica_esito_cod is null ";
			} else if (!filtri.getIseeConforme().equalsIgnoreCase(Constants.SI)
					&& !filtri.getIseeVerificaConforme().equalsIgnoreCase(Constants.VERISEENONINSERITA)) {
				query += " and btbi.isee_conforme is null and bdbive.isee_verifica_esito_cod = '"
						+ filtri.getIseeVerificaConforme() + "' ";
			} else if (!filtri.getIseeConforme().equalsIgnoreCase(Constants.SI)
					&& filtri.getIseeVerificaConforme().equalsIgnoreCase(Constants.VERISEENONINSERITA)) {
				query += " and btbi.isee_conforme is null and bdbive.isee_verifica_esito_cod is null ";
			}
		}

		query += " order by btb.buono_cod DESC ";

		/*
		 * Aggiungo i parametri alla query
		 */
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("anno", anno);
		if (filtri.getNumeroDomanda() != null) {
			params.addValue("domandaNumero", filtri.getNumeroDomanda());
		}
		if (filtri.getNumeroBuono() != null) {
			params.addValue("buonoCod", "%" + filtri.getNumeroBuono() + "%");
		}
		if (filtri.getStatoBuono() != null) {
			params.addValue("buonoStato", filtri.getStatoBuono());
		}
		if (filtri.getDestinatario() != null) {
			params.addValue("destinatario", "%" + filtri.getDestinatario() + "%");
		}
		if (filtri.getRichiedente() != null) {
			params.addValue("richiedente", "%" + filtri.getRichiedente() + "%");
		}

		try {
			buoni = jdbcTemplate.query(query, params, new BuoniResponseMapper());

			// List<ModelBuonoResponse> buonidef = new ArrayList<ModelBuonoResponse>();
			// if (Util.isValorizzato(filtri.getIseeConforme()) &&
			// Util.isValorizzato(filtri.getIseeVerificaConforme())
			// &&
			// filtri.getIseeVerificaConforme().equalsIgnoreCase(IseeStatoEnum.RESPINTO.getStato()))
			// {
			// for (ModelBuonoResponse buono : buoni) {
			// if (Util.isValorizzato(buono.getIseeConforme()) &&
			// buono.getIseeConforme().equalsIgnoreCase("Si")
			// && Util.isValorizzato(buono.getIseeVerificaEsitoCod())
			// &&
			// buono.getIseeVerificaEsitoCod().equalsIgnoreCase(IseeStatoEnum.RESPINTO.getStato()))
			// {
			// buonidef.add(buono);
			// }
			// }
			// return buonidef;
			// } else if (!Util.isValorizzato(filtri.getIseeConforme())
			// && Util.isValorizzato(filtri.getIseeVerificaConforme())
			// &&
			// filtri.getIseeVerificaConforme().equalsIgnoreCase(IseeStatoEnum.RESPINTO.getStato()))
			// {
			// for (ModelBuonoResponse buono : buoni) {
			// if (Util.isValorizzato(buono.getIseeVerificaEsitoCod())
			// &&
			// buono.getIseeVerificaEsitoCod().equalsIgnoreCase(IseeStatoEnum.RESPINTO.getStato()))
			// {
			// buonidef.add(buono);
			// }
			// }
			// return buonidef;
			// }

			return buoni;
		} catch (EmptyResultDataAccessException e) {
			logError(methodName, e.getMessage());
			return null;
		} catch (Exception e) {
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public List<ModelStatoBuono> selectStatiBuono() throws DatabaseException {
		List<ModelStatoBuono> statiBuono = new ArrayList<ModelStatoBuono>();
		try {
			statiBuono = jdbcTemplate.query(SELECT_STATI_BUONO, new BuonoStatoMapper());
			return statiBuono;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_STATI_BUONO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public ModelGetAllegato selectAllegatoBuono(Integer idAllegato) throws DatabaseException {

		ModelGetAllegato allegato = new ModelGetAllegato();
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("allegatoId", idAllegato);
		try {
			allegato = jdbcTemplate.queryForObject(SELECT_ALLEGATO_BUONO, namedParameters, new AllegatoGetMapper());
			return allegato;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_ALLEGATO_BUONO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public ModelBuono selectBuono(String numeroRichiesta) throws DatabaseException {
		String methodName = "GET_BUONO";
		ModelBuono buono = new ModelBuono();
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroRichiesta);
		try {
			buono = jdbcTemplate.queryForObject(GET_BUONO, namedParameters, new BuonoMapper());
			return buono;
		} catch (EmptyResultDataAccessException e) {
			logError(methodName, e.getMessage());
			return null;
		} catch (Exception e) {
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public String selectDescrMotivoSegnalazione(String codMotivo) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codMotivo", codMotivo);
		try {
			return jdbcTemplate.queryForObject(DESCR_CODIFICA, namedParameters, String.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "selectDescrMotivoSegnalazione";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public ModelBuonoInformazioni selectBuonoInformazioni(String numeroDomanda) throws DatabaseException {
		ModelBuonoInformazioni buonoInfo = new ModelBuonoInformazioni();
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda);
		try {
			buonoInfo = jdbcTemplate.queryForObject(SELECT_BUONO_INFORMAZIONI, namedParameters,
					new BuonoInformazioniMapper());
			return buonoInfo;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_BUONO_INFORMAZIONI";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public ModelVerificaInformazioni selectVerificaInformazioni(String numeroDomanda) throws DatabaseException {
		ModelVerificaInformazioni verificaInfo = new ModelVerificaInformazioni();
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda);
		try {
			verificaInfo = jdbcTemplate.queryForObject(SELECT_VERIFICA_INFORMAZIONI, namedParameters,
					new VerificaInformazioniMapper());
			return verificaInfo;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_BUONO_INFORMAZIONI";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	// TODO
	// public List<ModelStoricoRendicontazioni> selectStoricoRendicontazioni(String
	// numeroBuono) throws DatabaseException {
	// List<ModelStoricoRendicontazioni> listaStoricoRendicontazioniIstr = new
	// ArrayList<ModelStoricoRendicontazioni>();
	// List<ModelStoricoRendicontazioni> listaStoricoRendicontazioni = new
	// ArrayList<ModelStoricoRendicontazioni>();
	// List<ModelStoricoRendicontazioni> listaStoricoRendicontazioniFinale = new
	// ArrayList<ModelStoricoRendicontazioni>();
	// SqlParameterSource namedParameters = new
	// MapSqlParameterSource().addValue("buonoCod", numeroBuono);
	// try {
	// listaStoricoRendicontazioniIstr =
	// jdbcTemplate.query(SELECT_STORICO_RENDICONTAZIONI_ISTRUTTORIA,
	// namedParameters, new StoricoRendicontazioniMapper());
	// listaStoricoRendicontazioni =
	// jdbcTemplate.query(SELECT_STORICO_RENDICONTAZIONI, namedParameters, new
	// StoricoRendicontazioniMapper());
	// //ModelStoricoRendicontazioni elencoprec = new ModelStoricoRendicontazioni();
	// for (ModelStoricoRendicontazioni elenco : listaStoricoRendicontazioni) {
	// boolean trovato = false;
	// //if (elencoprec.getMese()!=null &&
	// elencoprec.getAnno().equals(elenco.getAnno())
	// // && elencoprec.getMese().trim().equalsIgnoreCase(elenco.getMese().trim())
	// // &&
	// elencoprec.getMonthData().trim().equalsIgnoreCase(elenco.getMonthData().trim()))
	// {
	// //continue;
	// //}
	// for (ModelStoricoRendicontazioni elencoIst : listaStoricoRendicontazioniIstr)
	// {
	// if(elenco.getAnno().equals(elencoIst.getAnno()) &&
	// elenco.getMese().trim().equalsIgnoreCase(elencoIst.getMese().trim())
	// &&
	// elenco.getMonthData().trim().equalsIgnoreCase(elencoIst.getMonthData().trim()))
	// {
	// trovato = true;
	// if (elencoIst.getEsito().equalsIgnoreCase("REINVIATO")) {
	// elencoIst.setDataDichiarazione(elencoIst.getDataIstruttoria());
	// elencoIst.setDataIstruttoria(null);
	// }
	// listaStoricoRendicontazioniFinale.add(elencoIst);
	// break;
	// }
	// }
	// //elencoprec = elenco;
	// if (!trovato) {
	// if (Util.isValorizzato(elenco.getEsito()) &&
	// elenco.getEsito().equalsIgnoreCase("CARICATA"))
	// elenco.setDataDichiarazione(null);
	// listaStoricoRendicontazioniFinale.add(elenco);
	// }
	// }
	//
	// return listaStoricoRendicontazioniFinale;
	// } catch (EmptyResultDataAccessException e) {
	// return null;
	// } catch (Exception e) {
	// String methodName = "SELECT_BUONO_INFORMAZIONI";
	// logError(methodName, e.getMessage());
	// throw new DatabaseException(e);
	// }
	// }

}
