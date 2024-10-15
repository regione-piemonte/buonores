/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto.custom;

import java.util.Arrays;

public class ResponseGetAllegato {

	private byte[] allegato;
	private String fileName;

	public byte[] getAllegato() {
		return allegato;
	}

	public void setAllegato(byte[] allegato) {
		this.allegato = allegato;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		return "ResponseGetAllegato [allegato=" + Arrays.toString(allegato) + ", fileName=" + fileName + "]";
	}
}
