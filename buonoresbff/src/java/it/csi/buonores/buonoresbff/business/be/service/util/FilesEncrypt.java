/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.business.be.service.util;

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

import it.csi.buonores.buonoresbff.util.BuonoResBffProperties;
import it.csi.buonores.buonoresbff.util.Constants;
import it.csi.buonores.buonoresbff.util.LoggerUtil;

@Component
public class FilesEncrypt extends LoggerUtil {

	@Autowired
	private BuonoResBffProperties properties;

	public byte[] creaFileCifratoByte(int cipherMode, byte[] inputFile) {
		final String methodName = "execCryptDecrypt";
		byte[] outputBytes = null;
		try {
			Key secretKey = new SecretKeySpec(properties.getFileKeyCrypt().getBytes(), Constants.AES);
			Cipher cipher = Cipher.getInstance(Constants.AES);
			cipher.init(cipherMode, secretKey);

			outputBytes = cipher.doFinal(inputFile);
			logInfo("file cifrato ", methodName);
			return outputBytes;
		} catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException e) {
			logError(methodName, "Errore in cifratura: ", e);
			return null;
		}
	}

	public byte[] creaFileDeCifratoByte(int cipherMode, byte[] inputFile) {
		final String methodName = "execDeCryptDecrypt";

		Key secretKey = new SecretKeySpec(properties.getFileKeyCrypt().getBytes(), Constants.AES);
		Cipher cipher;
		try {
			cipher = Cipher.getInstance(Constants.AES);
			cipher.init(cipherMode, secretKey);
			byte[] outputBytes = cipher.doFinal(inputFile);
			if (outputBytes != null) {
				return outputBytes;
			}
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
				| BadPaddingException | IllegalBlockSizeException e) {
			logError(methodName, "Errore in decifratura: ", e);
		}
		return null;
	}

}
