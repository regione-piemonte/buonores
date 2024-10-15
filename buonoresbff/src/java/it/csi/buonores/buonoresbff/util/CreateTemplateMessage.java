/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class CreateTemplateMessage {

	private CreateTemplateMessage() {
		throw new IllegalStateException("Utility class");
	}

	// Dato un template e un hashmap di parametri ritorna il messaggio di testo
	public static String generateTextByTemplateAndMap(String template, Map<String, Object> mappaParametri) {
		LinkedHashMap<String, Object> mappa = new LinkedHashMap<>();
		String templateToReturn = null;
		try {
			for (Map.Entry<String, Object> entry : mappaParametri.entrySet()) {
				mappa.put(Pattern.quote("{" + entry.getKey() + "}"), entry.getValue());
			}
			var cont = 0;
			var lastCreated = 0;
			var firstCreated = 0;
			var arrayText = new String[mappa.size()];
			for (Map.Entry<String, Object> entry : mappa.entrySet()) {
				var pattern = Pattern.compile(entry.getKey(), Pattern.CASE_INSENSITIVE);
				var matcher = pattern.matcher(template);
				if (matcher.find()) {
					var value = "";
					if (entry.getValue() instanceof String) {
						value = (String) entry.getValue();
					} else if (entry.getValue() instanceof Date) {
						var sdf = new SimpleDateFormat(Constants.DATE_FORMAT_FOR_TEMPLATE);
						value = sdf.format(entry.getValue());
					} else if (entry.getValue() instanceof Integer || entry.getValue() instanceof Long
							|| entry.getValue() instanceof Float || entry.getValue() instanceof Double
							|| entry.getValue() instanceof Boolean) {
						value = entry.getValue().toString();
					}
					if (firstCreated > 0) {
						arrayText[cont] = arrayText[lastCreated].replaceAll(entry.getKey(), value);
					} else {
						arrayText[cont] = matcher.replaceAll(value);
						firstCreated++;
					}
					lastCreated = cont;
				}
				cont++;
			}
			templateToReturn = firstCreated != 0 ? arrayText[lastCreated] : template;
		} catch (Exception ex) {

			return template;
		}
		return templateToReturn;
	}
}
