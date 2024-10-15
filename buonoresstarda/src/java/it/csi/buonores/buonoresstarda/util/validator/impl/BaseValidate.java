/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresstarda.util.validator.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.buonores.buonoresstarda.dto.ErroreDettaglio;
import it.csi.buonores.buonoresstarda.exception.DatabaseException;
import it.csi.buonores.buonoresstarda.integration.dao.custom.CodParametroDao;
import it.csi.buonores.buonoresstarda.util.Constants;
import it.csi.buonores.buonoresstarda.util.CreateTemplateMessage;
import it.csi.buonores.buonoresstarda.util.LoggerUtil;

public abstract class BaseValidate extends LoggerUtil {

	@Autowired
	CodParametroDao codParametroDao;

	private String getValueGeneric(String key, String type, String... param) {
		String value;
		try {
			value = codParametroDao.selectValoreParametroFromCod(key, type).trim();
		} catch (DatabaseException e) {
			return null;
		}
		if (param != null) {
			if (param.length > 0) {
				Map<String, Object> parameter = new HashMap<String, Object>();
				for (Integer i = 0; i < param.length; i++) {
					if (param[i] != null)
						parameter.put(i.toString(), param[i]);
				}
				value = CreateTemplateMessage.generateTextByTemplateAndMap(value, parameter);
			}
		}
		return value;
	}
	
	public ErroreDettaglio getValueGenericError(String key, String... param) {
		String value = getValueGeneric(key, Constants.PARAMETRO_ERRORE_TIPO, param);
		return new ErroreDettaglio(key, value);
	}

	public String getValueGenericSuccess(String key, String... param) {
		return getValueGeneric(key, Constants.PARAMETRO_MESSAGGIO_TIPO, param);
	}

	public String getValueGenericErrorStardas(String key, String... param) {
		return getValueGeneric(key, Constants.PARAMETRO_ERRORE_TIPO, param);
	}
}
