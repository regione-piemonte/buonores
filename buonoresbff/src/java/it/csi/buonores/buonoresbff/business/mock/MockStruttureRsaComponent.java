/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.business.mock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import it.csi.buonores.buonoresbff.business.be.service.base.BaseService;
import it.csi.buonores.buonoresbff.dto.ModelStruttura;
import it.csi.buonores.buonoresbff.dto.ModelStrutturaSedeLegale;

@Component
public class MockStruttureRsaComponent extends BaseService {

	public List<ModelStruttura> getStruttureByComune(String comune) {

		List<ModelStruttura> strutture = getStruttureMock();
		List<ModelStruttura> stRes = strutture.stream()
				.filter(st -> comune.trim().equalsIgnoreCase(st.getComune().trim())).collect(Collectors.toList());
		return stRes;
	}

	public List<ModelStruttura> getStruttureMock() {
		List<ModelStruttura> records = new ArrayList<>();
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();

		try (InputStream is = classloader.getResourceAsStream("/resources/strutture.csv");
				BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));) {

			String line = "";
			String[] tempArr;
			int index = 0;
			while ((line = br.readLine()) != null) {
				if (index != 0) {
					tempArr = line.split(";");
					records.add(getRecordFromLine(tempArr));
				}
				index++;
			}
		} catch (IOException e) {
			logError("getStruttureMock", e.getMessage(), e);
		}
		return records;
	}

	// 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18
	// ADESIONE;ASL;CAP;CODICE_STRUTTURA;CODICE_TIPO_ASSISTENZA;COMUNE;DENOMINAZIONE_STRUTTURA;DESC_TIPO_ASSISTENZA;DESC_TIPO_STRUTTURA;EMAIL;EPSG;INDIRIZZO;NATURA;PROVINCIA;SITO_WEB;TELEFONO;TIPOLOGIA_STRUTTURA;X_COORDINATE;Y_COORDINATE
	private ModelStruttura getRecordFromLine(String[] tempArr) {
		ModelStruttura ms = new ModelStruttura();
		ms.setId(tempArr[3]);
		ms.setIdArpe(tempArr[3]);
		ms.setIndirizzo(tempArr[11]);
		ms.setNome(tempArr[6]);
		ms.setComune(tempArr[5]);
		ms.setProvincia(tempArr[13]);
		ms.setPiva("mockRes" + tempArr[3]);
		ModelStrutturaSedeLegale sl = new ModelStrutturaSedeLegale();
		sl.setIndirizzo(tempArr[11]);
		sl.setNome("SL " + tempArr[6]);
		sl.setComune(tempArr[5]);
		sl.setProvincia(tempArr[13]);
		ms.setSedeLegale(sl);
		return ms;
	}
}
