/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonorescallban.util.rest.validator;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.buonores.buonorescallban.dto.ErroreDettaglio;
import it.csi.buonores.buonorescallban.exception.DatabaseException;
import it.csi.buonores.buonorescallban.integration.dao.custom.CodParametroDao;
import it.csi.buonores.buonorescallban.util.Constants;
import it.csi.buonores.buonorescallban.util.CreateTemplateMessage;
import it.csi.buonores.buonorescallban.util.LoggerUtil;

public abstract class BaseValidate extends LoggerUtil {

	@Autowired
	CodParametroDao codParametroDao;

	public ErroreDettaglio getValueGenericError(String key, String... param) {
		String value;
		try {
			value = codParametroDao.selectValoreParametroFromCod(key, Constants.PARAMETRO_ERRORE_TIPO).trim();
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
		return new ErroreDettaglio(key, value);
	}
}