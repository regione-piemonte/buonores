/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.business.be.service.util;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.buonores.buonoresbo.util.BuonoResBoProperties;
import it.csi.buonores.buonoresbo.util.LoggerUtil;

@Component
public class FilesEncrypt extends LoggerUtil {

	@Autowired
	BuonoResBoProperties properties;

	public byte[] creaFileDeCifratoByte(int cipherMode, byte[] inputFile) {
		final String methodName = "execDeCryptDecrypt";

		Key secretKey = new SecretKeySpec(properties.getFileKeyCrypt().getBytes(), "AES");
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES");

			cipher.init(cipherMode, secretKey);

			byte[] outputBytes = cipher.doFinal(inputFile);

			if (outputBytes != null) {
				return outputBytes;
			}
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException e) {
			logError(methodName, "Errore in decifratura: ", e);
		}
		return null;
	}
}
