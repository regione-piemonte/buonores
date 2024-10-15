/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.util.validator.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.buonores.buonoresbff.dto.ErroreDettaglio;
import it.csi.buonores.buonoresbff.exception.DatabaseException;
import it.csi.buonores.buonoresbff.integration.dao.custom.CodParametroDao;
import it.csi.buonores.buonoresbff.util.Constants;
import it.csi.buonores.buonoresbff.util.CreateTemplateMessage;
import it.csi.buonores.buonoresbff.util.LoggerUtil;
import it.csi.buonores.buonoresbff.util.Util;

public abstract class BaseValidate extends LoggerUtil {

	@Autowired
	CodParametroDao codParametroDao;

	private String getValue(String key, String[] param, String type) {
		String value;
		try {
			value = codParametroDao.selectValoreParametroFromCod(key, type).trim();
			if (value == null) {
				return null;
			}
		} catch (DatabaseException e) {
			return null;
		}
		if (param != null && param.length > 0) {
			Map<String, Object> parameter = new HashMap<String, Object>();

			for (Integer i = 0; i < param.length; i++) {
				if (param[i] != null) {
					parameter.put(i.toString(), param[i]);
				}
			}
			value = CreateTemplateMessage.generateTextByTemplateAndMap(value, parameter);
		}

		return value;
	}

	public ErroreDettaglio getValueGenericError(String key, String... params) {
		String value = getValue(key, params, Constants.PARAMETRO_ERRORE_TIPO);
		return new ErroreDettaglio(key, value);
	}

	protected boolean formalCheckCitId(String citId) {
		if (Util.isValorizzato(citId)) {
			if (citId.length() == 11 || citId.length() == 16) {
				return true;
			}
		}
		return false;
	}
}