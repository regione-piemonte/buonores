/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbanbatch.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import it.csi.buonores.buonoresbanbatch.dto.ModelRichiestaBatch;
import it.csi.buonores.buonoresbanbatch.logger.BatchLoggerFactory;

public class BuonoresbanBatchLogDAO {

	private Connection conn;
	private PreparedStatement ps;
	
	public BuonoresbanBatchLogDAO(Connection conn) {
		this.conn = conn;
	}
		
	public void commit() {
		try {
			this.conn.commit();
		} catch (SQLException e) {
			BatchLoggerFactory.getLogger(this.getClass()).error("ERROR WHILE COMMITTING: " ,e);
		}
	}

	public void rollback() {
		try {
			this.conn.rollback();
		} catch (SQLException e1) {
			BatchLoggerFactory.getLogger(this.getClass()).error("ERROR WHILE ROLLBACKING: " ,e1);
		}
	}

	public void closeAll() {
		try {
			if (this.ps != null)
				ps.close();

			if (this.conn != null)
				this.conn.close();

		} catch (SQLException e) {
			BatchLoggerFactory.getLogger(this.getClass()).error("ERROR WHILE CLOSING CONNECTION: " ,e);
		}
	}
	
	
	public Long inserisciBatchOK(String numeroDomanda, String batchCode, String motivoCod, String utente, String statoBatch) throws SQLException {
		Long result = null;
		try {
		//prendo i dati della domanda
			ModelRichiestaBatch modelRichiestaBatch = new ModelRichiestaBatch(); 
			modelRichiestaBatch = selectDomandaBatch(numeroDomanda);
			if (modelRichiestaBatch!=null) {
				//verifico i tentativi con batch code
			//	Integer tentativi = selectBatchPrametro(batchCode, "NUMERO_TENTATIVI");
				//select numero dela batch
				Integer numtentativi = selectNumeroTentativiOK(modelRichiestaBatch,motivoCod,batchCode);
				if (numtentativi==0) {
					result = insertBatchEsecuzioneOK(batchCode, numtentativi+1, modelRichiestaBatch, selectBatch(batchCode), 
							selectStatoBatch(statoBatch), selectMotivoBatch(motivoCod), utente);
							commit();
				}
				else {
					//devi aggiornare incrementando i tentativi
				//if (numtentativi < tentativi) {
					//tentativi fatti minore del massimo faccio insert nella tabella esecuzione
					result = SelectBatchEsecuzioneOK(modelRichiestaBatch, selectBatch(batchCode), 
							selectStatoBatch(statoBatch), selectMotivoBatch(motivoCod));
					UpdateBatchEsecuzioneOK(numtentativi+1, modelRichiestaBatch, selectBatch(batchCode), 
					selectStatoBatch(statoBatch), selectMotivoBatch(motivoCod),utente);
					commit();
			//	}
			}
			}
		} 
		catch (SQLException e) {
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. inserisciBatchOK. Elaborazione Batch terminata con errori ="
							,e);
			rollback();
			throw new SQLException();
		} 
		return result;
	}
	
	public Long inserisciBatchKO(String numeroDomanda, String batchCode, String motivoCod, String utente, String statoBatch) throws SQLException {
		Long result = null;
		
		try {
		//prendo i dati della domanda
			ModelRichiestaBatch modelRichiestaBatch = new ModelRichiestaBatch(); 
			modelRichiestaBatch = selectDomandaBatch(numeroDomanda);
			if (modelRichiestaBatch!=null) {
				//verifico i tentativi con batch code
				Integer tentativi = selectBatchPrametro(batchCode, "NUMERO_TENTATIVI");
				//select numero dela batch
				Integer numtentativi = selectNumeroTentativiKO(modelRichiestaBatch,motivoCod,batchCode);
				if (numtentativi==0) {
					result = insertBatchEsecuzioneKO(batchCode, numtentativi+1, modelRichiestaBatch, selectBatch(batchCode), 
							selectStatoBatch(statoBatch), selectMotivoBatch(motivoCod), utente);
					commit();
				}
				else {
				if (numtentativi < tentativi) {
					//tentativi fatti minore del massimo faccio insert nella tabella esecuzione
					//prelevo id batch seq da aggiornare
					result = SelectBatchEsecuzioneKO(modelRichiestaBatch, selectBatch(batchCode), 
							selectStatoBatch(statoBatch), selectMotivoBatch(motivoCod));
					UpdateBatchEsecuzioneKO(numtentativi+1, modelRichiestaBatch, selectBatch(batchCode), 
							selectStatoBatch(statoBatch), selectMotivoBatch(motivoCod),utente);
					commit();
				}
				}
			}
		} 
		catch (SQLException e) {
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL.inserisciBatchKO. Elaborazione Batch terminata con errori ="
							,e);
			rollback();
			throw new SQLException();
		} 
		return result;
	}
	
	public Long inserisciBatchStepOK(String batchesecdetStep, String batchesecdetNote, Long batchesecId,String utente, String messageuuid) throws SQLException {
		
		Long result = null;
		try {
					result = insertBatchEsecuzioneStepOK(batchesecdetStep, batchesecdetNote, batchesecId, 
					selectStatoBatch("STATO_OK"), utente, messageuuid);
					commit();
			}
		catch (SQLException e) {
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL.inserisciBatchStepOK. Elaborazione Batch terminata con errori ="
							,e);
							rollback();
			throw new SQLException();
		} 
		return result;
	}
	
	public Long inserisciBatchStepKO(String batchesecdetStep, String batchesecdetNote,Long batchesecId, String utente)  throws SQLException{
		
		Long result = null;
		try {
					result = insertBatchEsecuzioneStepKO(batchesecdetStep, batchesecdetNote, batchesecId, 
					selectStatoBatch("STATO_KO"), utente);
					commit();
			}
		catch (SQLException e) {
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL.inserisciBatchStepKO. Elaborazione Batch terminata con errori ="
							,e);
			rollback();
			throw new SQLException();
		} 
		return result;
	}
	
	public Long selectEventoKo(String numeroDomanda,String batcheCodStato, String batchCod, String motivoCod, String utente)  throws SQLException{
		
		Long result = null;
		try {
			ModelRichiestaBatch modelRichiestaBatch = new ModelRichiestaBatch(); 
			modelRichiestaBatch = selectDomandaBatch(numeroDomanda);
			if (modelRichiestaBatch!=null) {
					result = selectEsisteAvviatoKo(batcheCodStato,modelRichiestaBatch,batchCod);
					if (result==0) {
						result = inserisciBatchKO(numeroDomanda, batchCod,motivoCod, utente, batcheCodStato);
					}
			}
		}
		catch (SQLException e) {
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL.selectEventoKo. Elaborazione Batch terminata con errori ="
							,e);
			throw new SQLException();
		} 
		return result;
	}
	
	public Long selectEventoOk(String numeroDomanda,String batcheCodStato, String batchCod, String motivoCod, String utente)  throws SQLException{
		
		Long result = null;
		try {
			ModelRichiestaBatch modelRichiestaBatch = new ModelRichiestaBatch(); 
			modelRichiestaBatch = selectDomandaBatch(numeroDomanda);
			if (modelRichiestaBatch!=null) {
					result = selectEsisteAvviatoOk(batcheCodStato,modelRichiestaBatch,batchCod);
					if (result==0) {
						result = inserisciBatchOK(numeroDomanda, batchCod,motivoCod, utente, batcheCodStato);
				}
			}
		}
		catch (SQLException e) {
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL.selectEventoOk. Elaborazione Batch terminata con errori ="
							,e);
			throw new SQLException();
		} 
		return result;
	}
	
	public int selectEsitoPositivo(String numeroDomanda, String batchesecdetStep)  throws SQLException{
		
		int result = 0;
		try {
			ModelRichiestaBatch modelRichiestaBatch = new ModelRichiestaBatch(); 
			modelRichiestaBatch = selectDomandaBatch(numeroDomanda);
			if (modelRichiestaBatch!=null) {
					result = selectEsitoPositivo(modelRichiestaBatch,batchesecdetStep);
			}
		}
		catch (SQLException e) {
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL.selectEsitoPositivo. Elaborazione Batch terminata con errori ="
							,e);
			throw new SQLException();
		} 
		return result;
	}
	
	public String selectStatoDomanda(String numeroDomanda)  throws SQLException{
		
		String result = null;
		try {
			ModelRichiestaBatch modelRichiestaBatch = new ModelRichiestaBatch(); 
			modelRichiestaBatch = selectDomandaBatch(numeroDomanda);
			if (modelRichiestaBatch!=null) {
					result = modelRichiestaBatch.getStato();
			}
		}
		catch (SQLException e) {
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL.selectStatoDomanda. Elaborazione Batch terminata con errori ="
							,e);
			throw new SQLException();
		} 
		return result;
	}
	
	public void deleteEsitoVuotoOk(Long batchesecId)  throws SQLException{
		
		try {
					int resultOk = selectEsitoOk(batchesecId);
					if (resultOk == 0) {
						//cancello la riga del batch sec tabella s tutti esiti sono positivi
						DeleteRecordOk(batchesecId);
						commit();
					}
		}
		catch (SQLException e) {
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL.deleteEsitoVuotoOk. Elaborazione Batch terminata con errori ="
							,e);
			rollback();
			throw new SQLException();
		} 
		
	}
	
	public void deleteEsitoVuotoKo(Long batchesecId)  throws SQLException{
		
		try {
					int resultKo = selectEsitoKo(batchesecId);
					if (resultKo == 0) {
						//cancello la riga del batch sec tabella t tutti esiti sono positivi
						DeleteRecordKo(batchesecId);
						commit();
					}
		}
		catch (SQLException e) {
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL.deleteEsitoVuotoKo. Elaborazione Batch terminata con errori ="
							,e);
			rollback();
			throw new SQLException();
		} 
		
	}
	
	public Integer DeleteRecordOk(Long batchesecId) throws SQLException {
	
		try {
			this.ps = this.conn.prepareStatement(BatchDao.DELETE_RECORD_OK);
			this.ps.setLong(1, batchesecId);
			return this.ps.executeUpdate();
		} catch (SQLException e) {
			String methodName = "DELETE_RECORD_OK";
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. " + methodName +". Elaborazione Batch terminata con errori ="
							,e);
			rollback();
			throw new SQLException(e);
		} finally {
			this.ps.close();
		}
	}

	public Integer DeleteRecordKo(Long batchesecId) throws SQLException {

		try {
			this.ps = this.conn.prepareStatement(BatchDao.DELETE_RECORD_KO);
			this.ps.setLong(1, batchesecId);
			return this.ps.executeUpdate();
		} catch (SQLException e) {
			String methodName = "DELETE_RECORD_KO";
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. " + methodName +". Elaborazione Batch terminata con errori ="
							,e);
			rollback();
			throw new SQLException(e);
		} finally {
			this.ps.close();
		}
	}
	
	public int selectEsitoKo(Long batchesecId) throws SQLException {
		try {
			this.ps = this.conn.prepareStatement(BatchDao.SELECT_ESISTE_RECORD_KO);
			this.ps.setLong(1, batchesecId);
			ResultSet rs = this.ps.executeQuery();
			while (rs.next()) {
				return rs.getInt(1);
			}	
		}	 catch (SQLException e) {
			String methodName = "SELECT_ESISTE_RECORD_KO";
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. " + methodName +". Elaborazione Batch terminata con errori ="
							,e);
			throw new SQLException(e);
		}finally {
			this.ps.close();
		}
		return 0;

	}
	
	public int selectEsitoPositivo(ModelRichiestaBatch richiesta,String batchesecdetStep) throws SQLException {
		try {
			this.ps = this.conn.prepareStatement(BatchDao.SELECT_ESITO_POSITIVO);
			this.ps.setLong(1, richiesta.getSportelloId());
			this.ps.setLong(2, richiesta.getDomandaId());
			this.ps.setLong(3, richiesta.getDomandaDetId());
			this.ps.setString(4, batchesecdetStep);
			ResultSet rs = this.ps.executeQuery();
			while (rs.next()) {
				return rs.getInt(1);
			}	
		}	 catch (SQLException e) {
			String methodName = "SELECT_ESITO_POSITIVO";
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. " + methodName +". Elaborazione Batch terminata con errori ="
							,e);
			throw new SQLException(e);
		}
		finally {
			this.ps.close();
		}
		return 0;
	}
	
	public int selectEsitoOk(Long batchesecId) throws SQLException {
		try {
			this.ps = this.conn.prepareStatement(BatchDao.SELECT_ESISTE_RECORD_OK);
			this.ps.setLong(1, batchesecId);
			ResultSet rs = this.ps.executeQuery();
			while (rs.next()) {
				return rs.getInt(1);
			}	
		} catch (SQLException e) {
			String methodName = "SELECT_ESISTE_RECORD_OK";
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. " + methodName +". Elaborazione Batch terminata con errori ="
							,e);
			throw new SQLException(e);
		} finally {
			this.ps.close();
		}
		return 0;
	}
	
	public long selectEsisteAvviatoKo(String batchesecstatoCod,ModelRichiestaBatch richiesta, String batchCod) throws SQLException {
		try {
			this.ps = this.conn.prepareStatement(BatchDao.SELECT_ESISTE_RECORD_AVVIATO_KO);
			this.ps.setString(1, batchesecstatoCod);
			this.ps.setLong(2, richiesta.getSportelloId());
			this.ps.setLong(3, richiesta.getDomandaId());
			this.ps.setLong(4, richiesta.getDomandaDetId());
			this.ps.setString(5, batchCod);
			ResultSet rs = this.ps.executeQuery();
			while (rs.next()) {
				return rs.getInt(1);
			}	
		}	 catch (Exception e) {
			String methodName = "SELECT_ESISTE_RECORD_AVVIATO_KO";
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. " + methodName +". Elaborazione Batch terminata con errori ="
							,e);
			throw new SQLException(e);
		}
		finally {
			this.ps.close();
		}
		return 0;
	}
	
	public long selectEsisteAvviatoOk(String batchesecstatoCod,ModelRichiestaBatch richiesta, String batchCod) throws SQLException {

		try {
			this.ps = this.conn.prepareStatement(BatchDao.SELECT_ESISTE_RECORD_AVVIATO_OK);
			this.ps.setString(1, batchesecstatoCod);
			this.ps.setLong(2, richiesta.getSportelloId());
			this.ps.setLong(3, richiesta.getDomandaId());
			this.ps.setLong(4, richiesta.getDomandaDetId());
			this.ps.setString(5, batchCod);
			ResultSet rs = this.ps.executeQuery();
			while (rs.next()) {
				return rs.getInt(1);
			}	
		}	
		catch (SQLException e) {
			String methodName = "SELECT_ESISTE_RECORD_AVVIATO_OK";
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. " + methodName +". Elaborazione Batch terminata con errori ="
							,e);
			throw new SQLException(e);
		}
		finally {
			this.ps.close();
		}
		return 0;
	}
	
	public long selectStatoBatch(String batchesecstatoCod) throws SQLException {

		try {
			this.ps = this.conn.prepareStatement(BatchDao.SELECT_STATO_BATCH);
			this.ps.setString(1, batchesecstatoCod);
			ResultSet rs = this.ps.executeQuery();
			while (rs.next()) {
				return rs.getInt(1);
			}	
			
		}  catch (SQLException e) {
			String methodName = "SELECT_STATO_BATCH";
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. " + methodName +". Elaborazione Batch terminata con errori ="
							,e);
			throw new SQLException(e);
		}
		finally {
			this.ps.close();
		}
		return 0;
	}
	
	public long selectMotivoBatch(String batchmotivoCod) throws SQLException {

		try {
			this.ps = this.conn.prepareStatement(BatchDao.SELECT_MOTIVO_BATCH);
			this.ps.setString(1, batchmotivoCod);
			ResultSet rs = this.ps.executeQuery();
			while (rs.next()) {
				return rs.getInt(1);
			}	
		} catch (SQLException e) {
			String methodName = "SELECT_MOTIVO_BATCH";
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. " + methodName +". Elaborazione Batch terminata con errori ="
							,e);
			throw new SQLException(e);
		}
		finally {
			this.ps.close();
		}
		return 0;
	}
	
	public long selectBatch(String batchCod) throws SQLException {

		try {
			this.ps = this.conn.prepareStatement(BatchDao.SELECT_BATCH);
			this.ps.setString(1, batchCod);
			ResultSet rs = this.ps.executeQuery();
			while (rs.next()) {
				return rs.getInt(1);
			}	
		
		} catch (SQLException e) {
			String methodName = "SELECT_BATCH";
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. " + methodName +". Elaborazione Batch terminata con errori ="
							,e);
			throw new SQLException(e);
		}
		finally {
			this.ps.close();
		}
		return 0;
	}
	
	public int selectBatchPrametro(String batchCod, String batchparamCod ) throws SQLException {
		
		try {
			this.ps = this.conn.prepareStatement(BatchDao.SELECT_BATCH_PARAMETRO);
			this.ps.setString(1, batchCod);
			this.ps.setString(2, batchparamCod);
			ResultSet rs = this.ps.executeQuery();
			while (rs.next()) {
				return Integer.parseInt(rs.getString(1));
			}	
		}	 catch (SQLException e) {
			String methodName = "SELECT_BATCH_PARAMETRO";
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. " + methodName +". Elaborazione Batch terminata con errori ="
							,e);
			throw new SQLException(e);
		}
		finally {
			this.ps.close();
		}
		return 0;
	}
	
	public int selectNumeroTentativiKO(ModelRichiestaBatch richiesta,String motivo, String batchCode) throws SQLException {
		try {
			this.ps = this.conn.prepareStatement(BatchDao.SELECT_NUMERO_TENTATIVI_KO);
			this.ps.setLong(1, richiesta.getSportelloId());
			this.ps.setLong(2, richiesta.getDomandaId());
			this.ps.setLong(3, richiesta.getDomandaDetId());
			this.ps.setString(4, motivo);
			this.ps.setString(5, batchCode);
			ResultSet rs = this.ps.executeQuery();
			while (rs.next()) {
				return rs.getInt(1);
			}	
			
		}  catch (SQLException e) {
			String methodName = "SELECT_NUMERO_TENTATIVI_KO";
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. " + methodName +". Elaborazione Batch terminata con errori ="
							,e);
			throw new SQLException(e);
		}
		finally {
			this.ps.close();
		}
		return 0;
	}
	
	public int selectNumeroTentativiOK(ModelRichiestaBatch richiesta,String motivo, String batchCode) throws SQLException {
		try {
			this.ps = this.conn.prepareStatement(BatchDao.SELECT_NUMERO_TENTATIVI_OK);
			this.ps.setLong(1, richiesta.getSportelloId());
			this.ps.setLong(2, richiesta.getDomandaId());
			this.ps.setLong(3, richiesta.getDomandaDetId());
			this.ps.setString(4, motivo);
			this.ps.setString(5, batchCode);
			ResultSet rs = this.ps.executeQuery();
			while (rs.next()) {
				return rs.getInt(1);
			}	
			
		}  catch (SQLException e) {
			String methodName = "SELECT_NUMERO_TENTATIVI_OK";
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. " + methodName +". Elaborazione Batch terminata con errori ="
							,e);
			throw new SQLException(e);
		}
		finally {
			this.ps.close();
		}
		return 0;
	}
	
	public ModelRichiestaBatch selectDomandaBatch(String numeroDomanda) throws SQLException {

		try {
			ModelRichiestaBatch richiesta = new ModelRichiestaBatch();
			this.ps = this.conn.prepareStatement(BatchDao.SELECT_DATI_DOMANDA_BATCH);
			this.ps.setString(1, numeroDomanda);
			ResultSet rs = this.ps.executeQuery();
			while (rs.next()) {
				richiesta.setSportelloId(rs.getLong(1));
				richiesta.setDomandaId(rs.getLong(2));
				richiesta.setDomandaDetId(rs.getLong(3));
				richiesta.setStato(rs.getString(4));
			}	
			return richiesta;
		}	 catch (SQLException e) {
			String methodName = "SELECT_DATI_DOMANDA_BATCH";
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. " + methodName +". Elaborazione Batch terminata con errori ="
							,e);
			throw new SQLException(e);
		}
		finally {
			this.ps.close();
		}
	}
	
	public void UpdateBatchEsecuzioneOK(int tentativi,ModelRichiestaBatch datiDomanda, Long batchId, Long statoId, Long motivoId, String utente) throws SQLException{
		
		try {
			this.ps = this.conn.prepareStatement(BatchDao.UPDATE_BATCH_ESECUZIONE_OK);
			this.ps.setInt(1, tentativi);
			this.ps.setString(2, utente);
			this.ps.setLong(3, datiDomanda.getSportelloId());
			this.ps.setLong(4, datiDomanda.getDomandaId());
			this.ps.setLong(5, datiDomanda.getDomandaDetId());
			this.ps.setLong(6, batchId);
			this.ps.setLong(7, statoId);
			this.ps.setLong(8, motivoId);
			this.ps.executeUpdate();
			commit();
		} catch (SQLException e) {
			String methodName = "UPDATE_BATCH_ESECUZIONE_OK";
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. " + methodName +". Elaborazione Batch terminata con errori ="
							,e);
			rollback();
			throw new SQLException(e);
		}
		finally {
			this.ps.close();
		}
	}
	
   public void UpdateBatchEsecuzioneKO(int tentativi,ModelRichiestaBatch datiDomanda, Long batchId, Long statoId, Long motivoId, String utente) throws SQLException{
		
		try {
			this.ps = this.conn.prepareStatement(BatchDao.UPDATE_BATCH_ESECUZIONE_KO);
			this.ps.setInt(1, tentativi);
			this.ps.setString(2, utente);
			this.ps.setLong(3, datiDomanda.getSportelloId());
			this.ps.setLong(4, datiDomanda.getDomandaId());
			this.ps.setLong(5, datiDomanda.getDomandaDetId());
			this.ps.setLong(6, batchId);
			this.ps.setLong(7, statoId);
			this.ps.setLong(8, motivoId);
			this.ps.executeUpdate();			
			commit();
		} catch (SQLException e) {
			String methodName = "UPDATE_BATCH_ESECUZIONE_OK";
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. " + methodName +". Elaborazione Batch terminata con errori ="
							,e);
			rollback();
			throw new SQLException(e);
		}
		finally {
			this.ps.close();
		}
	}

   public long SelectBatchEsecuzioneKO(ModelRichiestaBatch datiDomanda, Long batchId, Long statoId, Long motivoId) throws SQLException{
		
		try {
			this.ps = this.conn.prepareStatement(BatchDao.SELECT_BATCH_ESECUZIONE_KO);
			this.ps.setLong(1, datiDomanda.getSportelloId());
			this.ps.setLong(2, datiDomanda.getDomandaId());
			this.ps.setLong(3, datiDomanda.getDomandaDetId());
			this.ps.setLong(4, batchId);
			this.ps.setLong(5, statoId);
			this.ps.setLong(6, motivoId);
			ResultSet rs = this.ps.executeQuery();
			while (rs.next()) {
				return rs.getLong(1);
			}	
		} catch (SQLException e) {
			String methodName = "SELECT_BATCH_ESECUZIONE_KO";
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. " + methodName +". Elaborazione Batch terminata con errori ="
							,e);
			rollback();
			throw new SQLException(e);
		}
		finally {
			this.ps.close();
		}
		return 0;
	}
   
   public long SelectBatchEsecuzioneOK(ModelRichiestaBatch datiDomanda, Long batchId, Long statoId, Long motivoId) throws SQLException{
		
		try {
			this.ps = this.conn.prepareStatement(BatchDao.SELECT_BATCH_ESECUZIONE_OK);
			this.ps.setLong(1, datiDomanda.getSportelloId());
			this.ps.setLong(2, datiDomanda.getDomandaId());
			this.ps.setLong(3, datiDomanda.getDomandaDetId());
			this.ps.setLong(4, batchId);
			this.ps.setLong(5, statoId);
			this.ps.setLong(6, motivoId);
			ResultSet rs = this.ps.executeQuery();
			while (rs.next()) {
				return rs.getLong(1);
			}	
		} catch (SQLException e) {
			String methodName = "SELECT_BATCH_ESECUZIONE_OK";
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. " + methodName +". Elaborazione Batch terminata con errori ="
							,e);
			rollback();
			throw new SQLException(e);
		}
		finally {
			this.ps.close();
		}
		return 0;
	}
	public long insertBatchEsecuzioneOK(String richiedente, int tentativi, ModelRichiestaBatch datiDomanda,
			Long batchId, Long statoId, Long motivoId, String utente) throws SQLException{
		long idnew = 0;
		ResultSet rs = null;
		try {
			this.ps = this.conn.prepareStatement(BatchDao.INSERT_BATCH_ESECUZIONE_OK,Statement.RETURN_GENERATED_KEYS);
			this.ps.setString(1, richiedente);
			this.ps.setInt(2, tentativi);
			this.ps.setLong(3, datiDomanda.getSportelloId());
			this.ps.setLong(4, datiDomanda.getDomandaId());
			this.ps.setLong(5, datiDomanda.getDomandaDetId());
			this.ps.setLong(6, batchId);
			this.ps.setLong(7, statoId);
			this.ps.setLong(8, motivoId);
			this.ps.setString(9, utente);
			this.ps.setString(10, utente);
			int num = this.ps.executeUpdate();
			if (num>0) {
				rs = ps.getGeneratedKeys();
				 if(rs != null && rs.next()){
					 idnew = rs.getLong(1);
				 }
			return idnew;
			}
		} catch (SQLException e) {
			String methodName = "INSERT_BATCH_ESECUZIONE_OK";
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. " + methodName +". Elaborazione Batch terminata con errori ="
							,e);
			rollback();
			throw new SQLException(e);
		}
		finally {
			this.ps.close();
		}
		return idnew;
	}

	public long insertBatchEsecuzioneKO(String richiedente, int tentativi, ModelRichiestaBatch datiDomanda,
			Long batchId, Long statoId, Long motivoId, String utente) throws SQLException{
		ResultSet rs = null;
		long idnew = 0;
		try {
			this.ps = this.conn.prepareStatement(BatchDao.INSERT_BATCH_ESECUZIONE_KO,Statement.RETURN_GENERATED_KEYS);
			this.ps.setString(1, richiedente);
			this.ps.setInt(2, tentativi);
			this.ps.setLong(3, datiDomanda.getSportelloId());
			this.ps.setLong(4, datiDomanda.getDomandaId());
			this.ps.setLong(5, datiDomanda.getDomandaDetId());
			this.ps.setLong(6, batchId);
			this.ps.setLong(7, statoId);
			this.ps.setLong(8, motivoId);
			this.ps.setString(9, utente);
			this.ps.setString(10, utente);
			int num = this.ps.executeUpdate();
			if (num>0) {
				rs = ps.getGeneratedKeys();
				 if(rs != null && rs.next()){
					 idnew = rs.getLong(1);
				 }
			return idnew;
			}
		} catch (SQLException e) {
			String methodName = "INSERT_BATCH_ESECUZIONE_KO";
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. " + methodName +". Elaborazione Batch terminata con errori ="
							,e);
			rollback();
			throw new SQLException(e);
		}
		finally {
			this.ps.close();
		}
		return idnew;
	}

	public long insertBatchEsecuzioneStepOK(String batchesecdetStep, String batchesecdetNote,
			Long batchesecId, Long batchesecstatoId, String utente, String messageuuid) throws SQLException{
		long idnew = 0;
		ResultSet rs = null;
		try {
			this.ps = this.conn.prepareStatement(BatchDao.INSERT_BATCH_ESECUZIONE_STEP_OK,Statement.RETURN_GENERATED_KEYS);
			this.ps.setString(1, batchesecdetStep);
			this.ps.setString(2, batchesecdetNote);
			this.ps.setLong(3,batchesecId);
			this.ps.setLong(4, batchesecstatoId);
			this.ps.setString(5, utente);
			this.ps.setString(6, utente);
			this.ps.setString(7, messageuuid);
			int num = this.ps.executeUpdate();
			if (num>0) {
				rs = ps.getGeneratedKeys();
				 if(rs != null && rs.next()){
					 idnew = rs.getLong(1);
				 }
			return idnew;
			}
			
			} catch (SQLException e) {
			String methodName = "INSERT_BATCH_ESECUZIONE_STEP_OK";
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. " + methodName +". Elaborazione Batch terminata con errori ="
							,e);
			rollback();
			throw new SQLException(e);
		}
		finally {
			this.ps.close();
		}
		return idnew;
	}

	public long insertBatchEsecuzioneStepKO(String batchesecdetStep, String batchesecdetNote,
			Long batchesecId, Long batchesecstatoId, String utente) throws SQLException{
		ResultSet rs = null;
		long idnew = 0;
		try {
			this.ps = this.conn.prepareStatement(BatchDao.INSERT_BATCH_ESECUZIONE_STEP_KO,Statement.RETURN_GENERATED_KEYS);
			this.ps.setString(1, batchesecdetStep);
			this.ps.setString(2, batchesecdetNote);
			this.ps.setLong(3,batchesecId);
			this.ps.setLong(4, batchesecstatoId);
			this.ps.setString(5, utente);
			this.ps.setString(6, utente);
			int num = this.ps.executeUpdate();
			if (num>0) {
				rs = ps.getGeneratedKeys();
				 if(rs != null && rs.next()){
					 idnew = rs.getLong(1);
				 }
			return idnew;
			}

		} catch (SQLException e) {
			String methodName = "INSERT_BATCH_ESECUZIONE_STEP_KO";
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. " + methodName +". Elaborazione Batch terminata con errori ="
							,e);
			rollback();
			throw new SQLException(e);
		}
		finally {
			this.ps.close();
		}
		return idnew;
	}
	
	public void DeleteRecordDaCancellareDuplicati() throws SQLException {
		try {
			this.ps = this.conn.prepareStatement(BatchDao.SELECT_RECORD_DUPLICATI_KO);
			ResultSet rs = this.ps.executeQuery();
			while (rs.next()) {
				DeleteRecordDuplicatiKoStep(rs.getLong(1));
				DeleteRecordDuplicatiKo(rs.getLong(1));
				commit();
			}	
		}	 catch (SQLException e) {
			String methodName = "SELECT_RECORD_DUPLICATI_KO";
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. " + methodName +". Elaborazione Batch terminata con errori ="
							,e);
			rollback();
			throw new SQLException(e);
		}finally {
			this.ps.close();
		}
	}
	
	public Integer DeleteRecordDuplicatiKoStep(Long batchesecId) throws SQLException {

		try {
			this.ps = this.conn.prepareStatement(BatchDao.DELETE_RECORD_DUPLICATI_KO_STEP);
			this.ps.setLong(1, batchesecId);
			return this.ps.executeUpdate();
		} catch (SQLException e) {
			String methodName = "DELETE_RECORD_DUPLICATI_KO_STEP";
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. " + methodName +". Elaborazione Batch terminata con errori ="
							,e);
			rollback();
			throw new SQLException(e);
		} finally {
			this.ps.close();
		}
	}
	
	public Integer DeleteRecordDuplicatiKo(Long batchesecId) throws SQLException {

		try {
			this.ps = this.conn.prepareStatement(BatchDao.DELETE_RECORD_DUPLICATI_KO);
			this.ps.setLong(1, batchesecId);
			return this.ps.executeUpdate();
		} catch (SQLException e) {
			String methodName = "DELETE_RECORD_DUPLICATI_KO";
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. " + methodName +". Elaborazione Batch terminata con errori ="
							,e);
			rollback();
			throw new SQLException(e);
		} finally {
			this.ps.close();
		}
	}
	
	public String selectUuidBandi(String batchstep)  throws SQLException{
	
		try {
			this.ps = this.conn.prepareStatement(BatchDao.GET_UUID_BANDI);
			this.ps.setString(1, batchstep);
			ResultSet rs = this.ps.executeQuery();
			while (rs.next()) {
				return rs.getString(1);
			}	
		}
		catch (SQLException e) {
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL.get bandi uuid. Elaborazione Batch terminata con errori ="
							,e);
			throw new SQLException();
		} 
		return null;
	}

	public Integer DeleteRecordSenzaStepOk() throws SQLException {
		
		try {
			this.ps = this.conn.prepareStatement(BatchDao.DELETE_RECORD_SENZA_STEP_OK);
			int esecuzione = this.ps.executeUpdate();
			commit();
			return esecuzione;
		} catch (SQLException e) {
			String methodName = "DELETE_RECORD_SENZA_STEP_OK";
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. " + methodName +". Elaborazione Batch terminata con errori ="
							,e);
			rollback();
			throw new SQLException(e);
		} finally {
			this.ps.close();
		}
	}
	
	public Integer DeleteRecordSenzaStepKo() throws SQLException {
		
		try {
			this.ps = this.conn.prepareStatement(BatchDao.DELETE_RECORD_SENZA_STEP_KO);
			int esecuzione = this.ps.executeUpdate();
			commit();
			return esecuzione;
		} catch (SQLException e) {
			String methodName = "DELETE_RECORD_SENZA_STEP_KO";
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. " + methodName +". Elaborazione Batch terminata con errori ="
							,e);
			rollback();
			throw new SQLException(e);
		} finally {
			this.ps.close();
		}
	}
	
	public int countBatchExecutionErrors() throws SQLException {
		try {
			this.ps = this.conn.prepareStatement(BatchDao.COUNT_BATCH_EXECUTION_ERRORS);
			ResultSet rs = this.ps.executeQuery();
			while (rs.next()) {
				return rs.getInt(1);
			}

		} catch (SQLException e) {
			String methodName = "COUNT_BATCH_EXECUTION_ERRORS";
			BatchLoggerFactory.getLogger(this).info("Si e' verificato un errore SQL. " + methodName
					+ ". Elaborazione Batch terminata con errori =" + e);
			throw new SQLException(e);
		} finally {
			try {
				this.ps.close();
			} catch (SQLException e) {
				BatchLoggerFactory.getLogger(this).info("countBatchExecutionErrors - "
						+ "Si e' verificato un errore nella chiusura del PreparatedStatement. " + e);
			}
		}
		return -1;
	}
}
