/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbatch.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import it.csi.buonores.buonoresbatch.configuration.Configuration;
import it.csi.buonores.buonoresbatch.dto.RichiestaDto;
import it.csi.buonores.buonoresbatch.logger.BatchLoggerFactory;
import it.csi.buonores.buonoresbatch.util.Constants;

public class BuonoresBatchDAO {

	private Connection conn;
	private PreparedStatement ps;
	private String dbKeyCrypt = null;

	public BuonoresBatchDAO(Connection conn) {
		this.conn = conn;
		dbKeyCrypt = Configuration.get("dbKeyCrypt");
	}

	public void commit() {
		try {
			this.conn.commit();
		} catch (SQLException e) {
			BatchLoggerFactory.getLogger(this.getClass()).error("ERROR WHILE COMMITTING: ", e);
		}
	}

	public void rollback() {
		try {
			this.conn.rollback();
		} catch (SQLException e1) {
			BatchLoggerFactory.getLogger(this.getClass()).error("ERROR WHILE ROLLBACKING: ", e1);
		}
	}

	public void closeAll() {
		try {
			if (this.ps != null)
				ps.close();

			if (this.conn != null)
				this.conn.close();

		} catch (SQLException e) {
			BatchLoggerFactory.getLogger(this.getClass()).error("ERROR WHILE CLOSING CONNECTION: ", e);
		}
	}

	public List<RichiestaDto> getRichieste() throws SQLException {
		try {
			this.ps = this.conn.prepareStatement(SQLStatements.GETRICHIESTE);
			ResultSet rs = this.ps.executeQuery();
			List<RichiestaDto> listaric = new ArrayList<RichiestaDto>();
			while (rs.next()) {
				RichiestaDto richiesta = new RichiestaDto();
				richiesta.setCf(rs.getString(1));
				richiesta.setDomandaDetId(rs.getLong(2));
				richiesta.setStato(rs.getString(3));
				richiesta.setInizioValidita(rs.getDate(4));
				richiesta.setGiorni(rs.getInt(5));
				richiesta.setRichiedenteCf(rs.getString(6));
				richiesta.setDomandaNumero(rs.getString(7));
				richiesta.setSportelloId(rs.getLong(8));
				richiesta.setDomandaId(rs.getLong(9));
				richiesta.setDataDecesso(rs.getDate(10));
				richiesta.setDataExtraRegione(rs.getDate(11));
				listaric.add(richiesta);
			}
			return listaric;
		} catch (SQLException sqEx) {
			BatchLoggerFactory.getLogger(this)
					.error("Si e' verificato un errore SQL. Get Richieste. Elaborazione Batch terminata con errori =", sqEx);
			throw new SQLException(sqEx);
		} finally {
			this.ps.close();
		}
	}
	

	public Integer getGiorniStatoPrecedente(String numerodomanda, String codstato) throws SQLException {
		try {
			this.ps = this.conn.prepareStatement(SQLStatements.SELECT_PRECEDENTE_STATO);
			this.ps.setString(1, numerodomanda);
			this.ps.setString(2, codstato);
			ResultSet rs = this.ps.executeQuery();
			while (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException sqEx) {
			BatchLoggerFactory.getLogger(this)
					.error("Si e' verificato un errore SQL. Get Richieste. Elaborazione Batch terminata con errori =", sqEx);
			throw new SQLException(sqEx);
		} finally {
			this.ps.close();
		}
		return null;
	}

	public String getParametro(String codice, String tipo) throws SQLException {
		try {
			this.ps = this.conn.prepareStatement(SQLStatements.SELECT_PARAMETRO);
			this.ps.setString(1, codice);
			this.ps.setString(2, tipo);
			ResultSet rs = this.ps.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException sqEx) {
			BatchLoggerFactory.getLogger(this)
					.error("Si e' verificato un errore SQL. Get Errore. Elaborazione Batch terminata con errori =", sqEx);
			throw new SQLException(sqEx);
		} finally {
			this.ps.close();
		}
		return null;
	}

	public void executeCambioStatoDomanda(long idDomandaDet, String codStato, String notainterna, String domandaNumero)
			throws SQLException {
		BatchLoggerFactory.getLogger(this).info(
				"--- INIZIO INSERIMENTO RECORD NELLA TABELLA DETTAGLIO DOMANDA PER LA DOMANDA ---" + domandaNumero);
		try {
			ResultSet rs = null;
			long idnew = 0;
			int num = 0;
			BatchLoggerFactory.getLogger(this)
					.info("--- CHIUDO DETTAGLIO ESISTENTE NELLA TABELLA DETTAGLIO DOMANDA --- " + idDomandaDet);
			this.ps = this.conn.prepareStatement(SQLStatements.UPDATE_DETTAGLIO_DOMANDA_CHIUDI);
			this.ps.setLong(1, idDomandaDet);
			num = this.ps.executeUpdate();
			if (num > 0) {
				BatchLoggerFactory.getLogger(this).info("-- Chiuso record in tabella dettaglio " + idDomandaDet);
				BatchLoggerFactory.getLogger(this)
						.info("--- APRO DETTAGLIO NUOVO DA ESISTENTE DETTAGLIO DOMANDA --- " + idDomandaDet);
				this.ps = this.conn.prepareStatement(SQLStatements.INSERT_DETTAGLIO_DOMANDA,
						Statement.RETURN_GENERATED_KEYS);
				this.ps.setString(1, codStato);
				this.ps.setString(2, notainterna);
				this.ps.setLong(3, idDomandaDet);
				num = this.ps.executeUpdate();
				if (num > 0) {
					rs = ps.getGeneratedKeys();
					if (rs != null && rs.next()) {
						idnew = rs.getLong(1);
					}
					BatchLoggerFactory.getLogger(this)
							.info("-- Creato nuovo record in tabella record in tabella dettaglio " + idnew);
					BatchLoggerFactory.getLogger(this).info("--- Aggiorno nuovo dettaglio DOMANDA " + idnew
							+ " con il cod det " + codStato + "_" + idnew);
					this.ps = this.conn.prepareStatement(SQLStatements.UPDATE_DETTAGLIO_DOMANDA_APRI);
					this.ps.setString(1, codStato + "_" + idnew);
					this.ps.setLong(2, idnew);
					num = this.ps.executeUpdate();
					if (num > 0) {
						BatchLoggerFactory.getLogger(this).info("--- Aggiornata dettaglio DOMANDA " + idnew
								+ " con il cod det " + codStato + "_" + idnew);
						BatchLoggerFactory.getLogger(this).info("--- Inserisco Allegati DOMANDA " + idnew
								+ " con il cod det " + codStato + "_" + idnew);
						this.ps = this.conn.prepareStatement(SQLStatements.INSERT_ALLEGATO_DOMANDA);
						this.ps.setLong(1, idnew);
						this.ps.setString(2, codStato + "_" + idnew);
						this.ps.setLong(3, idDomandaDet);
						num = this.ps.executeUpdate();
						if (num > 0) {
							BatchLoggerFactory.getLogger(this).info("--- Allegati DOMANDA inseriti " + idnew
									+ " con il cod det " + codStato + "_" + idnew);
						} else {
							BatchLoggerFactory.getLogger(this).info("Nessuna riga allegato creata");
						}
					} else {
						BatchLoggerFactory.getLogger(this).info("Nessuna riga dettaglio aperto aggiornata");
					}
				} else {
					BatchLoggerFactory.getLogger(this).info("Nessuna riga domanda dettaglio inserita");
				}
			} else {
				BatchLoggerFactory.getLogger(this).info("Nessuna riga dettaglio domanda chiusa");
			}
			commit();
		} catch (SQLException sqEx) {
			BatchLoggerFactory.getLogger(this)
					.error("Si e verificato un errore. Elaborazione Batch terminata con errori =", sqEx);
		} finally {
			BatchLoggerFactory.getLogger(this).info(
					"--- FINE INSERIMENTO RECORD NELLA TABELLA DETTAGLIO DOMANDA PER LA DOMANDA ---" + domandaNumero);
			this.ps.close();
		}
	}

	public void insertAudit(String request_payload, String response_payload, Integer esito_chiamata)
			throws SQLException {
		try {
			this.ps = this.conn.prepareStatement(SQLStatements.INSERT_LOG_AUDIT);
			this.ps.setString(1, request_payload);
			this.ps.setString(2, dbKeyCrypt);
			this.ps.setString(3, response_payload);
			this.ps.setString(4, dbKeyCrypt);
			this.ps.setInt(5, esito_chiamata);

			this.ps.executeUpdate();
			commit();

		} catch (SQLException e) {
			BatchLoggerFactory.getLogger(this)
					.error("Si e' verificato un errore SQL. insertAudit. Elaborazione Batch terminata con errori =", e);
			this.conn.rollback();
			throw e;
		} finally {
			try {
				this.ps.close();
			} catch (SQLException e) {
				BatchLoggerFactory.getLogger(this).info(
						"insertAudit - " + "Si e' verificato un errore nella chiusura del PreparatedStatement. ", e);
			}
		}
	}

	public boolean IsPiemontese(String sigla) throws SQLException {
		try {
			this.ps = this.conn.prepareStatement(SQLStatements.SELECT_PROVINCIA_PIEMONTE);
			this.ps.setString(1, sigla);
			ResultSet rs = this.ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch (SQLException sqEx) {
			BatchLoggerFactory.getLogger(this)
					.error("Si e' verificato un errore SQL. Get Errore. Elaborazione Batch terminata con errori =", sqEx);
			throw new SQLException(sqEx);
		} finally {
			this.ps.close();
		}
		return false;
	}

	public Long getUltimoSportelloChiuso() throws SQLException {
		try {
			this.ps = this.conn.prepareStatement(SQLStatements.SELECT_ULTIMO_SPORTELLO_CHIUSO);
			ResultSet rs = this.ps.executeQuery();
			while (rs.next()) {
				return rs.getLong(1);
			}
		} catch (SQLException sqEx) {
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. Get ultimo sportello. Elaborazione Batch terminata con errori =", sqEx);
			throw new SQLException(sqEx);
		} finally {
			this.ps.close();
		}
		return null;
	}

	public Boolean getIncompatibilita(long idDomandaDet) throws SQLException {
		try {
			this.ps = this.conn.prepareStatement(SQLStatements.SELECT_INCOMPATIBILITA_DOMANDA);
			this.ps.setLong(1, idDomandaDet);
			ResultSet rs = this.ps.executeQuery();
			while (rs.next()) {
				return rs.getBoolean(1);
			}
		} catch (SQLException sqEx) {
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. Get incompatibilitadomanda. Elaborazione Batch terminata con errori =", sqEx);
			throw new SQLException(sqEx);
		} finally {
			this.ps.close();
		}
		return null;
	}

	public long selectGraduatoriaIdByDomandaDettaglioId(Long idDetDomanda) throws SQLException {
		try {
			this.ps = this.conn.prepareStatement(SQLStatements.SELECT_GRADUATORIA_ID_BY_DOMANDA_DETTAGLIO_ID);
			this.ps.setLong(1, idDetDomanda);
			this.ps.setLong(2, idDetDomanda);
			ResultSet rs = this.ps.executeQuery();
			while (rs.next()) {
				return rs.getLong(1);
			}
		} catch (SQLException sqEx) {
			BatchLoggerFactory.getLogger(this)
					.error("Si e' verificato un errore SQL. Get graduatoria. Elaborazione Batch terminata con errori =", sqEx);
			throw new SQLException(sqEx);
		} finally {
			this.ps.close();
		}
		return 0;
	}

	public Boolean checkStatoGraduatoria(long idGraduatoria, String stato) throws SQLException {
		try {
			this.ps = this.conn.prepareStatement(SQLStatements.CHECK_STATO_GRADUATORIA_BY_ID);
			this.ps.setLong(1, idGraduatoria);
			this.ps.setString(2, stato);
			ResultSet rs = this.ps.executeQuery();
			while (rs.next()) {
				return rs.getBoolean(1);
			}
		} catch (SQLException sqEx) {
			BatchLoggerFactory.getLogger(this)
					.error("Si e' verificato un errore SQL. Ckeck graduatoria. Elaborazione Batch terminata con errori =", sqEx);
			throw new SQLException(sqEx);
		} finally {
			this.ps.close();
		}
		return null;
	}

	public void updateRGraduatoriaStato(long idGraduatoria, String stato, String cf) throws SQLException {
		try {
			int num = 0;
			this.ps = this.conn.prepareStatement(SQLStatements.UPDATE_R_GRADUATORIA_STATO);
			this.ps.setLong(1, idGraduatoria);
			num = this.ps.executeUpdate();
			if (num > 0) {
				BatchLoggerFactory.getLogger(this).info("--- AGGIORNATA GRADUATORIA PROVVISORIA---" + idGraduatoria);
				this.ps = this.conn.prepareStatement(SQLStatements.INSERT_R_GRADUATORIA_STATO);
				this.ps.setLong(1, idGraduatoria);
				this.ps.setString(2, stato);
				this.ps.setString(3, cf);
				this.ps.setString(4, cf);
				num = this.ps.executeUpdate();
				if (num > 0) {
					BatchLoggerFactory.getLogger(this)
							.info("--- INSERT  GRADUATORIA DA AGGIORNARE ---" + idGraduatoria);
				} else
					BatchLoggerFactory.getLogger(this)
							.info("---NON INSERT GRADUATORIA DA AGGIORNARE ---" + idGraduatoria);
			} else
				BatchLoggerFactory.getLogger(this)
						.info("--- NON AGGIORNATA GRADUATORIA PROVVISORIA---" + idGraduatoria);
			commit();
		} catch (SQLException sqEx) {
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. Insert/update graduatoria. Elaborazione Batch terminata con errori =", sqEx);
			throw new SQLException(sqEx);
		} finally {
			this.ps.close();
		}
	}
	
	public List<RichiestaDto> getRichiesteDiniego() throws SQLException {
		try {
			this.ps = this.conn.prepareStatement(SQLStatements.GETRICHIESTE_DINIEGO);
			ResultSet rs = this.ps.executeQuery();
			List<RichiestaDto> listaric = new ArrayList<RichiestaDto>();
			while (rs.next()) {
				RichiestaDto richiesta = new RichiestaDto();
				richiesta.setCf(rs.getString(1));
				richiesta.setDomandaDetId(rs.getLong(2));
				richiesta.setStato(rs.getString(3));
				richiesta.setInizioValidita(rs.getDate(4));
				richiesta.setGiorni(rs.getInt(5));
				richiesta.setRichiedenteCf(rs.getString(6));
				richiesta.setDomandaNumero(rs.getString(7));
				richiesta.setSportelloId(rs.getLong(8));
				richiesta.setDomandaId(rs.getLong(9));
				richiesta.setDataDecesso(rs.getDate(10));
				richiesta.setDataExtraRegione(rs.getDate(11));
				listaric.add(richiesta);
			}
			return listaric;
		} catch (SQLException sqEx) {
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. Get Richieste. Elaborazione Batch terminata con errori = ", sqEx);
			throw new SQLException(sqEx);
		} finally {
			this.ps.close();
		}
	}

	public String selectStatoPrecedente(String numeroRichiesta) throws SQLException {
		try {
			this.ps = this.conn.prepareStatement(SQLStatements.SELECT_PRECEDENTE_STATO_COD);
			this.ps.setString(1, numeroRichiesta);
			ResultSet rs = this.ps.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException sqEx) {
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. Get stato precedente cod Errore. Elaborazione Batch terminata con errori = ",
					sqEx);
			throw new SQLException(sqEx);
		} finally {
			this.ps.close();
		}
		return null;
	}

	public boolean esisteBuono(Long sportelloId, Long domandaId, String stato) throws SQLException{		
		try {
			this.ps = this.conn.prepareStatement(SQLStatements.ESISTE_BUONO);
			this.ps.setLong(1, domandaId);	
			this.ps.setLong(2, sportelloId);	
			this.ps.setString(3, stato);	
			ResultSet rs = this.ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1)>0;
			}		
		} catch (SQLException sqEx) {
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. Get Errore ESISTE_BUONO. Elaborazione Batch terminata con errori ="
							,sqEx);
			throw new SQLException();
		} finally {
			this.ps.close();
		}
		return false;
	}
	
	public String selectDescMotivo(String codMotivo) throws SQLException{		
		try {
			this.ps = this.conn.prepareStatement(SQLStatements.DESCR_CODIFICA);
			this.ps.setString(1, codMotivo);
			ResultSet rs = this.ps.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			}		
		} catch (SQLException sqEx) {
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. Get descr codifica segnalazione cod Errore. Elaborazione Batch terminata con errori ="
							,sqEx);
			throw new SQLException();
		} finally {
			this.ps.close();
		}
		return null;
	}
	
	public void updateDataDecessoRes(long id, Date datavariazione, String tipo) throws SQLException {
		try {
			int num=0;
			if (tipo.equalsIgnoreCase(Constants.DEC)) {
				this.ps = this.conn.prepareStatement(SQLStatements.INSERT_DATA_DECESSO);
				this.ps.setDate(1, datavariazione);
				this.ps.setLong(2, id);
				num = this.ps.executeUpdate();
			}
			else if (tipo.equalsIgnoreCase(Constants.CR)) {
				this.ps = this.conn.prepareStatement(SQLStatements.INSERT_DATA_RESIDENZA);
				this.ps.setDate(1, datavariazione);
				this.ps.setLong(2, id);
				num = this.ps.executeUpdate();
			}
			if (num>0) 
				BatchLoggerFactory.getLogger(this).info("--- AGGIORNATA DATA VARIZIONE --- " + tipo);
			else
				BatchLoggerFactory.getLogger(this).info("--- NON AGGIORNATA DATA VARIAZIONE--- " + tipo);
			commit();
		} 
			catch (SQLException sqEx) {
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. update data variazione. Elaborazione Batch terminata con errori ="
							,sqEx);
			throw new SQLException();
		} 
		finally {
			this.ps.close();
		}
	}
	
	
}
