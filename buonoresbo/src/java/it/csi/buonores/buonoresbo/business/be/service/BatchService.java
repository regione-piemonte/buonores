/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.business.be.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.buonores.buonoresbo.business.be.service.base.BaseService;
import it.csi.buonores.buonoresbo.dto.custom.ModelRichiestaBatch;
import it.csi.buonores.buonoresbo.exception.DatabaseException;
import it.csi.buonores.buonoresbo.integration.dao.custom.BatchDao;

@Service
public class BatchService extends BaseService {

	@Autowired
	BatchDao batchDao;

	public Long inserisciBatchOK(String numeroDomanda, String batchCode, String motivoCod, String utente,
			String statoBatch) {
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		log.info(metodo + " - Inizio ");
		Long result = null;
		try {
			// prendo i dati della domanda
			ModelRichiestaBatch modelRichiestaBatch = new ModelRichiestaBatch();
			modelRichiestaBatch = batchDao.selectDomandaBatch(numeroDomanda);
			if (modelRichiestaBatch != null) {
				// verifico i tentativi con batch code
				// Integer tentativi = batchDao.selectBatchPrametro(batchCode,
				// "NUMERO_TENTATIVI");
				// select numero dela batch
				Integer numtentativi = batchDao.selectNumeroTentativiOK(modelRichiestaBatch, motivoCod, batchCode);
				if (numtentativi == 0) {
					result = batchDao.insertBatchEsecuzioneOK(batchCode, numtentativi + 1, modelRichiestaBatch,
							batchDao.selectBatch(batchCode), batchDao.selectStatoBatch(statoBatch),
							batchDao.selectMotivoBatch(motivoCod), utente);
				} else {
					// devi aggiornare incrementando i tentativi
					// tentativi fatti minore del massimo faccio insert nella tabella esecuzione
					result = batchDao.SelectBatchEsecuzioneOK(modelRichiestaBatch, batchDao.selectBatch(batchCode),
							batchDao.selectStatoBatch(statoBatch), batchDao.selectMotivoBatch(motivoCod));
					batchDao.UpdateBatchEsecuzioneOK(numtentativi + 1, modelRichiestaBatch,
							batchDao.selectBatch(batchCode), batchDao.selectStatoBatch(statoBatch),
							batchDao.selectMotivoBatch(motivoCod), utente);
				}
			}
			log.info(metodo + " - Fine ");
		} catch (DatabaseException e) {
			handleDatabaseException(metodo, e);
		}
		return result;
	}

	public Long inserisciBatchKO(String numeroDomanda, String batchCode, String motivoCod, String utente,
			String statoBatch) {
		Long result = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		log.info(metodo + " - Inizio ");
		try {
			ModelRichiestaBatch modelRichiestaBatch = new ModelRichiestaBatch();
			modelRichiestaBatch = batchDao.selectDomandaBatch(numeroDomanda);
			if (modelRichiestaBatch != null) {
				// verifico i tentativi con batch code
				Integer tentativi = batchDao.selectBatchPrametro(batchCode, "NUMERO_TENTATIVI");
				// select numero dela batch
				Integer numtentativi = batchDao.selectNumeroTentativiKO(modelRichiestaBatch, motivoCod, batchCode);
				if (numtentativi == 0) {
					result = batchDao.insertBatchEsecuzioneKO(batchCode, numtentativi + 1, modelRichiestaBatch,
							batchDao.selectBatch(batchCode), batchDao.selectStatoBatch(statoBatch),
							batchDao.selectMotivoBatch(motivoCod), utente);
				} else {
					if (numtentativi < tentativi) {
						// tentativi fatti minore del massimo faccio insert nella tabella esecuzione
						// prelevo id batch seq da aggiornare
						result = batchDao.SelectBatchEsecuzioneKO(modelRichiestaBatch, batchDao.selectBatch(batchCode),
								batchDao.selectStatoBatch(statoBatch), batchDao.selectMotivoBatch(motivoCod));
						batchDao.UpdateBatchEsecuzioneKO(numtentativi + 1, modelRichiestaBatch,
								batchDao.selectBatch(batchCode), batchDao.selectStatoBatch(statoBatch),
								batchDao.selectMotivoBatch(motivoCod), utente);
					}
				}
			}
			log.info(metodo + " - Fine ");
		} catch (DatabaseException e) {
			handleDatabaseException(metodo, e);
		}
		return result;
	}

	public Long inserisciBatchStepOK(String batchesecdetStep, String batchesecdetNote, Long batchesecId,
			String utente, String messageuuid) {
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		log.info(metodo + " - Inizio ");
		Long result = null;
		try {
			result = batchDao.insertBatchEsecuzioneStepOK(batchesecdetStep, batchesecdetNote, batchesecId,
					batchDao.selectStatoBatch("STATO_OK"), utente, messageuuid);
			log.info(metodo + " - Fine ");
		} catch (DatabaseException e) {
			handleDatabaseException(metodo, e);
		}
		return result;
	}

	public Long inserisciBatchStepKO(String batchesecdetStep, String batchesecdetNote, Long batchesecId,
			String utente) {
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		log.info(metodo + " - Inizio ");
		Long result = null;
		try {
			result = batchDao.insertBatchEsecuzioneStepKO(batchesecdetStep, batchesecdetNote, batchesecId,
					batchDao.selectStatoBatch("STATO_KO"), utente);
			log.info(metodo + " - Fine ");
		} catch (DatabaseException e) {
			handleDatabaseException(metodo, e);
		}
		return result;
	}

	public Long selectEventoKo(String numeroDomanda, String batcheCodStato, String batchCod) {
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		log.info(metodo + " - Inizio ");
		Long result = null;
		try {
			ModelRichiestaBatch modelRichiestaBatch = new ModelRichiestaBatch();
			modelRichiestaBatch = batchDao.selectDomandaBatch(numeroDomanda);
			if (modelRichiestaBatch != null) {
				result = batchDao.selectEsisteAvviatoKo(batcheCodStato, modelRichiestaBatch, batchCod);
				log.info(metodo + " - Fine ");
			}
		} catch (DatabaseException e) {
			handleDatabaseException(metodo, e);
		}
		return result;
	}

	public Long selectEventoOk(String numeroDomanda, String batcheCodStato, String batchCod) {
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		log.info(metodo + " - Inizio ");
		Long result = null;
		try {
			ModelRichiestaBatch modelRichiestaBatch = new ModelRichiestaBatch();
			modelRichiestaBatch = batchDao.selectDomandaBatch(numeroDomanda);
			if (modelRichiestaBatch != null) {
				result = batchDao.selectEsisteAvviatoOk(batcheCodStato, modelRichiestaBatch, batchCod);
				log.info(metodo + " - Fine ");
			}
		} catch (DatabaseException e) {
			handleDatabaseException(metodo, e);
		}
		return result;
	}

	public int selectEsitoPositivo(String numeroDomanda, String batchesecdetStep) {
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		log.info(metodo + " - Inizio ");
		int result = 0;
		try {
			ModelRichiestaBatch modelRichiestaBatch = new ModelRichiestaBatch();
			modelRichiestaBatch = batchDao.selectDomandaBatch(numeroDomanda);
			if (modelRichiestaBatch != null) {
				result = batchDao.selectEsitoPositivo(modelRichiestaBatch, batchesecdetStep);
				log.info(metodo + " - Fine ");
			}
		} catch (DatabaseException e) {
			handleDatabaseException(metodo, e);
		}
		return result;
	}

	public String selectStatoDomanda(String numeroDomanda) {
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		log.info(metodo + " - Inizio ");
		String result = null;
		try {
			ModelRichiestaBatch modelRichiestaBatch = new ModelRichiestaBatch();
			modelRichiestaBatch = batchDao.selectDomandaBatch(numeroDomanda);
			if (modelRichiestaBatch != null) {
				result = modelRichiestaBatch.getStato();
				log.info(metodo + " - Fine ");
			}
		} catch (DatabaseException e) {
			handleDatabaseException(metodo, e);
		}
		return result;
	}

	public void deleteEsitoVuotoOk(Long batchesecId) {
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		log.info(metodo + " - Inizio ");
		try {
			int resultOk = batchDao.selectEsitoOk(batchesecId);
			if (resultOk == 0) {
				// cancello la riga del batch sec tabella s tutti esiti sono positivi
				batchDao.DeleteRecordOk(batchesecId);
			}

			log.info(metodo + " - Fine ");
		} catch (DatabaseException e) {
			handleDatabaseException(metodo, e);
		}

	}

	public void deleteEsitoVuotoKo(Long batchesecId) {
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		log.info(metodo + " - Inizio ");
		try {
			int resultKo = batchDao.selectEsitoKo(batchesecId);
			if (resultKo == 0) {
				// cancello la riga del batch sec tabella t tutti esiti sono positivi
				batchDao.DeleteRecordKo(batchesecId);
			}

			log.info(metodo + " - Fine ");
		} catch (DatabaseException e) {
			handleDatabaseException(metodo, e);
		}

	}

	public void DeleteRecordDaCancellareDuplicati() {
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		log.info(metodo + " - Inizio ");
		try {
			batchDao.DeleteRecordDaCancellareDuplicati();
			;

			log.info(metodo + " - Fine ");
		} catch (DatabaseException e) {
			handleDatabaseException(metodo, e);
		}
	}
}
