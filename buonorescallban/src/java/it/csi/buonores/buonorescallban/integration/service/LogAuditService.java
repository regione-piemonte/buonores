/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonorescallban.integration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.buonores.buonorescallban.business.be.service.base.BaseService;
import it.csi.buonores.buonorescallban.dto.custom.LogAudit;
import it.csi.buonores.buonorescallban.integration.dao.custom.LogAuditDao;

@Service
public class LogAuditService extends BaseService {

	@Autowired
	LogAuditDao logAuditDao;

	public void insertLogAudit(LogAudit logaudit) {
		try {
			logAuditDao.insertLogAudit(logaudit);
		} catch (Exception e) {
			log.error("LogAuditService:insertLogAudit ERROR", e);
		}

	}
}
