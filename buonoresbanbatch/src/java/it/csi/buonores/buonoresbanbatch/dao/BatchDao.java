/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbanbatch.dao;

public class BatchDao {

			
	public static final String INSERT_BATCH_ESECUZIONE_KO = "INSERT INTO bres_t_batch_esecuzione (batchesec_richiedente, batchesec_tentativo_numero, "
			+ "batchesec_fine, sportello_id, domanda_id, domanda_det_id, batch_id, "
			+ "batchesecstato_id, batchmotivo_id, "
			+ "utente_creazione, utente_modifica) values "
			+ "(?, ?, now(), ?, ?, ?, ?, ?, ?, ?, ?)";

	public static final String INSERT_BATCH_ESECUZIONE_OK ="INSERT INTO bres_s_batch_esecuzione (batchesec_richiedente, batchesec_tentativo_numero, "
			+ "batchesec_fine, sportello_id, domanda_id, domanda_det_id, batch_id, "
			+ "batchesecstato_id, batchmotivo_id, "
			+ "utente_creazione, utente_modifica) values "
			+ "(?, ?, now(), ?, ?, ?, ?, ?, ?, ?, ?)";
	
	public static final String UPDATE_BATCH_ESECUZIONE_OK ="UPDATE bres_s_batch_esecuzione SET " 
			+ "batchesec_tentativo_numero=?, data_modifica=now(), utente_modifica=? "
			+ "WHERE sportello_id = ? "
			+ "and domanda_id = ? "
			+ "and domanda_det_id = ? "
			+ "and batch_id = ? "
			+ "and batchesecstato_id = ? "
			+ "and batchmotivo_id = ? ";
	
	public static final String UPDATE_BATCH_ESECUZIONE_KO ="UPDATE bres_t_batch_esecuzione SET " 
			+ "batchesec_tentativo_numero=?, data_modifica=now(), utente_modifica=? "
			+ "WHERE sportello_id = ? "
			+ "and domanda_id = ? "
			+ "and domanda_det_id = ? "
			+ "and batch_id = ? "
			+ "and batchesecstato_id = ? "
			+ "and batchmotivo_id = ?";
	
	public static final String SELECT_BATCH_ESECUZIONE_OK ="Select batchesec_id from bres_s_batch_esecuzione " 
			+ "WHERE sportello_id = ? "
			+ "and domanda_id = ? "
			+ "and domanda_det_id = ? "
			+ "and batch_id = ? "
			+ "and batchesecstato_id = ? "
			+ "and batchmotivo_id = ? ";
	
	public static final String SELECT_BATCH_ESECUZIONE_KO ="Select batchesec_id from bres_t_batch_esecuzione " 
			+ "WHERE sportello_id = ? "
			+ "and domanda_id = ? "
			+ "and domanda_det_id = ? "
			+ "and batch_id = ? "
			+ "and batchesecstato_id = ? "
			+ "and batchmotivo_id = ?";
	
	public static final String INSERT_BATCH_ESECUZIONE_STEP_OK ="INSERT INTO bres_s_batch_esecuzione_det (batchesecdet_step, batchesecdet_note, "
			+ "batchesecdet_fine, batchesec_id,batchesecstato_id, "
			+ "utente_creazione, utente_modifica, messageuuid) "
			+ "VALUES(?, ?, now(), ? , ?, ?, ?, ?)";

	public static final String INSERT_BATCH_ESECUZIONE_STEP_KO ="INSERT INTO bres_t_batch_esecuzione_det (batchesecdet_step, batchesecdet_note, "
			+ "batchesecdet_fine, batchesec_id, batchesecstato_id, "
			+ "utente_creazione, utente_modifica) "
			+ "VALUES(?, ?, now(), ?, ?, ?, ?)";
	
	public static final String SELECT_STATO_BATCH ="SELECT batchesecstato_id FROM bres_d_batch_stato "
			+ "where batchesecstato_cod = ? "
			+ "and validita_fine is null "
			+ "and validita_inizio <= now() "
			+ "and data_cancellazione is null";
	
	public static final String SELECT_MOTIVO_BATCH ="SELECT batchmotivo_id FROM bres_d_batch_motivo "
			+ "where batchmotivo_cod = ? "
			+ "and validita_fine is null "
			+ "and validita_inizio <= now() "
			+ "and data_cancellazione is null";
	
	public static final String SELECT_BATCH ="SELECT batch_id FROM bres_d_batch a,bres_d_batch_gruppo b "
			+ "where a.batch_cod = ? "
			+ "and a.validita_fine is null "
			+ "and a.validita_inizio <= now() "
			+ "and a.data_cancellazione is null "
			+ "and b.validita_fine is null "
			+ "and b.validita_inizio <= now() "
			+ "and b.data_cancellazione is null "
			+ "and a.batchgruppo_id=b.batchgruppo_id "
			+ "and b.batchgruppo_cod='BANBATCH'";

	public static final String SELECT_BATCH_PARAMETRO ="SELECT batchparam_valore "
			+ "FROM bres_d_batch_parametro a,bres_d_batch b,bres_d_batch_gruppo c "
			+ "where a.batch_id = b.batch_id "
			+ "and b.batch_cod = ? "
			+ "and batchparam_cod = ? "
			+ "and a.validita_fine is null "
			+ "and a.validita_inizio <= now() "
			+ "and a.data_cancellazione is null "
			+ "and b.validita_fine is null "
			+ "and b.validita_inizio <= now() "
			+ "and b.data_cancellazione is null "
			+ "and c.validita_fine is null "
			+ "and c.validita_inizio <= now() " 
			+ "and c.data_cancellazione is null "
			+ "and b.batchgruppo_id=c.batchgruppo_id "
			+ "and c.batchgruppo_cod='BANBATCH'";
	
	public static final String SELECT_DATI_DOMANDA_BATCH ="select btdd.sportello_id,btdd.domanda_id,btdd.domanda_det_id,bdds.domanda_stato_cod from "
			+ "bres_t_domanda btd, "
			+ "bres_t_domanda_dettaglio btdd, bres_d_domanda_stato bdds "  
			+ "where btd.data_cancellazione is null "
			+ "and bdds.domanda_stato_id = btdd.domanda_stato_id "
			+ "and btdd.domanda_id = btd.domanda_id "
			+ "and btdd.data_cancellazione is null "
			+ "and btdd.validita_inizio <= now() "
			+ "and btdd.validita_fine is null "
			+ "and btd.domanda_numero= ?";
	
	public static final String SELECT_NUMERO_TENTATIVI_OK ="select batchesec_tentativo_numero from bres_s_batch_esecuzione a, bres_d_batch_motivo b "
			+"where a.sportello_id = ? and a.domanda_id = ? and a.domanda_det_id = ? "
			+"and b.batchmotivo_id =a.batchmotivo_id "
			+"and b.batchmotivo_cod = ? "
			+"and a.batch_id = (select batch_id from bres_d_batch where batch_cod = ?) "
			+"and b.data_cancellazione is null "
			+"and a.data_cancellazione is null";
		
	public static final String SELECT_NUMERO_TENTATIVI_KO ="select batchesec_tentativo_numero from bres_t_batch_esecuzione a, bres_d_batch_motivo b "
			+"where sportello_id = ? and domanda_id = ? and domanda_det_id = ? "
			+"and b.batchmotivo_id =a.batchmotivo_id "
			+"and b.batchmotivo_cod = ? "
			+"and a.batch_id = (select batch_id from bres_d_batch where batch_cod = ?) "
			+"and b.data_cancellazione is null "
			+"and a.data_cancellazione is null";
	
	public static final String SELECT_ESITO_POSITIVO ="select count(*) from bres_s_batch_esecuzione a,bres_s_batch_esecuzione_det b "
			+ "where sportello_id = ? and domanda_id = ? and domanda_det_id = ? "
			+ "and a.batchesec_id = b.batchesec_id "
			+ "and a.data_cancellazione is null "
			+ "and b.data_cancellazione is null "
			+ "and b.batchesecdet_step = ?";
	
	public static final String SELECT_ESISTE_RECORD_AVVIATO_KO ="select batchesec_id from bres_t_batch_esecuzione a, bres_d_batch_stato b,"
			+ "bres_d_batch c,bres_d_batch_gruppo d "
			+ "where a.batchesecstato_id = b.batchesecstato_id "
			+ "and b.batchesecstato_cod = ? "
			+ "and c.batch_id = a.batch_id "
			+ "and d.batchgruppo_id =c.batchgruppo_id " 
			+ "and d.batchgruppo_cod ='BANBATCH' "
			+ "and a.sportello_id = ? "
			+ "and a.domanda_id = ? "
			+ "and a.domanda_det_id = ? "
			+ "and a.data_cancellazione is null "
			+ "and b.validita_inizio  <= now() "
			+ "and b.validita_fine is null "
			+ "and c.batch_cod = ? "
			+ "and b.data_cancellazione is null "
			+ "and c.data_cancellazione is null "
			+ "and d.data_cancellazione is null";
	
	public static final String SELECT_ESISTE_RECORD_AVVIATO_OK ="select batchesec_id from bres_s_batch_esecuzione a, bres_d_batch_stato b,"
			+ "bres_d_batch c,bres_d_batch_gruppo d "
			+ "where a.batchesecstato_id = b.batchesecstato_id "
			+ "and b.batchesecstato_cod = ? "
			+ "and c.batch_id = a.batch_id "
			+ "and d.batchgruppo_id =c.batchgruppo_id " 
			+ "and d.batchgruppo_cod ='BANBATCH' "
			+ "and a.sportello_id = ? "
			+ "and a.domanda_id = ? "
			+ "and a.domanda_det_id = ? "
			+ "and a.data_cancellazione is null "
			+ "and b.validita_inizio  <= now() "
			+ "and b.validita_fine is null "
			+ "and c.batch_cod = ? "
			+ "and b.data_cancellazione is null "
			+ "and c.data_cancellazione is null "
			+ "and d.data_cancellazione is null";
	
	public static final String SELECT_ESISTE_RECORD_KO ="select count(*) from bres_t_batch_esecuzione a, bres_t_batch_esecuzione_det b "
			+ "where a.batchesec_id = b.batchesec_id "
			+ "and a.batchesec_id = ?";
		
	public static final String SELECT_ESISTE_RECORD_OK ="select count(*) from bres_s_batch_esecuzione a, bres_s_batch_esecuzione_det b "
			+ "where a.batchesec_id = b.batchesec_id "
			+ "and a.batchesec_id = ?";
	
	public static final String DELETE_RECORD_OK ="delete from bres_s_batch_esecuzione "
			+ "where batchesec_id = ?";
	
	public static final String DELETE_RECORD_KO ="delete from bres_t_batch_esecuzione "
			+ "where batchesec_id = ?";
	
	public static final String SELECT_RECORD_DUPLICATI_KO ="SELECT bsbe1.batchesec_id FROM bres_t_batch_esecuzione bsbe1 "
			+ "where bsbe1.batchesec_id in ( "
			+ "select distinct bsbe1.batchesec_id  from bres_s_batch_esecuzione bsbe , bres_s_batch_esecuzione_det bsbed, "
			+ "bres_t_batch_esecuzione bsbe1 , bres_t_batch_esecuzione_det bsbed1 "
			+ "where bsbe.batchesec_id = bsbed.batchesec_id "
			+ "and bsbe1.batchesec_id = bsbed1.batchesec_id "
			+ "and bsbe.data_cancellazione is null "
			+ "and bsbed.data_cancellazione is null "
			+ "and bsbe1.data_cancellazione is null "
			+ "and bsbed1.data_cancellazione is null "
			+ "and bsbe.batchesec_richiedente = bsbe1.batchesec_richiedente "
			+ "and bsbe.sportello_id = bsbe1.sportello_id "
			+ "and bsbe.domanda_id = bsbe1.domanda_id "
			+ "and bsbe.domanda_det_id = bsbe1.domanda_det_id " 
			+ "and bsbe.batch_id = bsbe1.batch_id "
			+ "and bsbe.batchmotivo_id = bsbe1.batchmotivo_id "
			+ "and bsbed.batchesecdet_step = bsbed1.batchesecdet_step)";
	
	public static final String DELETE_RECORD_DUPLICATI_KO_STEP ="DELETE FROM bres_t_batch_esecuzione_det bsbed1 " 
			+ "WHERE bsbed1.batchesec_id = ?";
	
	public static final String DELETE_RECORD_DUPLICATI_KO ="delete FROM bres_t_batch_esecuzione bsbe1 "
			+ "where bsbe1.batchesec_id = ?";
	
	public static final String GET_UUID_BANDI ="select batchesecdet_note from bres_s_batch_esecuzione_det "  
			+ "where batchesecdet_step = ?";
	
	public static final String DELETE_RECORD_SENZA_STEP_OK ="delete from bres_s_batch_esecuzione bsbe "
			+ "where bsbe.batchesec_id not in (select bsbed.batchesec_id from bres_s_batch_esecuzione_det bsbed)";
			
	public static final String DELETE_RECORD_SENZA_STEP_KO ="delete from bres_t_batch_esecuzione bsbe "
					+ "where bsbe.batchesec_id not in (select bsbed.batchesec_id from bres_t_batch_esecuzione_det bsbed)";
	
	public static final String COUNT_BATCH_EXECUTION_ERRORS ="select count(*) from bres_t_batch_esecuzione a, bres_t_batch_esecuzione_det b "
						+ "where a.batchesec_id = b.batchesec_id "
						+ "and b.utente_creazione = 'BUONORESBANBATCH' "
						+ "and (b.data_creazione between (now() - interval '1 hour') and (now() + interval '1 hour')) "; 
}
