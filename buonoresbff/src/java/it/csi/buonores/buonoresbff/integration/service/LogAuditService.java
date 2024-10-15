/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.integration.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.buonores.buonoresbff.business.be.service.base.BaseService;
import it.csi.buonores.buonoresbff.dto.custom.LogAudit;
import it.csi.buonores.buonoresbff.exception.DatabaseException;
import it.csi.buonores.buonoresbff.integration.dao.custom.LogAuditDao;

@Service
public class LogAuditService extends BaseService {

	@Autowired
	LogAuditDao logAuditDao;

	public void insertLogAudit(String idApp, String ipAddress, String utente, String operazione, String oggOper,
			String keyOper, String uuid, String requestPayload,
			String responsePayload, Integer esitoChiamata, HttpServletRequest httpRequest) {
		try {
			LogAudit logaudit = new LogAudit();
			logaudit.setIdApp(idApp);
			logaudit.setIpAddress(ipAddress);
			logaudit.setUtente(utente);
			logaudit.setOperazione(operazione);
			logaudit.setOggOper(oggOper);
			logaudit.setKeyOper(keyOper);
			logaudit.setUuid(uuid);
			logaudit.setRequestPayload(requestPayload);
			logaudit.setResponsePayload(responsePayload);
			logaudit.setEsitoChiamata(esitoChiamata);
			logAuditDao.insertLogAudit(logaudit);
		} catch (DatabaseException e) {
			logError("insertLogAudit", e.getMessage(), e);
		}
	}

	public void insertLogAudit(LogAudit logaudit) {
		try {
			logAuditDao.insertLogAudit(logaudit);
		} catch (Exception e) {
			logError("insertLogAudit by LogAudit", e.getMessage(), e);
		}
	}
}
