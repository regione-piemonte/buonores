/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.dto;

import java.util.Arrays;
import java.util.Objects;

public class ModelGetAllegato {

	private String filePath;
	private String fileName;
	private byte[] filebyte;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getFilebyte() {
		return filebyte;
	}

	public void setFilebyte(byte[] filebyte) {
		this.filebyte = filebyte;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(filebyte);
		result = prime * result + Objects.hash(fileName, filePath);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModelGetAllegato other = (ModelGetAllegato) obj;
		return Objects.equals(fileName, other.fileName) && Objects.equals(filePath, other.filePath)
				&& Arrays.equals(filebyte, other.filebyte);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ModelGetAllegato [filePath=");
		builder.append(filePath);
		builder.append(", fileName=");
		builder.append(fileName);
		builder.append(", filebyte=");
		builder.append(Arrays.toString(filebyte));
		builder.append("]");
		return builder.toString();
	}

}
