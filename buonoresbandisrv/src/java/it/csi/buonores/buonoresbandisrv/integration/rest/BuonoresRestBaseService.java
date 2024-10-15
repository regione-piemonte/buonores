/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.integration.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.buonores.buonoresbandisrv.util.BuonoResBandiSrvProperties;
import okhttp3.OkHttpClient;

@Component
public class BuonoresRestBaseService extends RestBaseService {
	private OkHttpClient okHttpClient;

	@Autowired
	BuonoResBandiSrvProperties properties;

	@Override
	protected OkHttpClient getHttpClient() {
		if (okHttpClient == null) {
			okHttpClient = new OkHttpClient.Builder()
					.addInterceptor(new BasicAuthInterceptor(properties.getBuonoressrvusername(),
							properties.getBuonoressrvpassword()))
					.build();
		}

		return okHttpClient;
	}

}
