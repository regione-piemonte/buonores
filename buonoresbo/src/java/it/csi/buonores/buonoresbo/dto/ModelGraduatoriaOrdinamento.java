/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbo.dto;

public class ModelGraduatoriaOrdinamento {
	private Long ordinamentoId;
	private String ordinamentoCriterio;
	private String ordinamentoTabella;
	private String ordinamentoCampo;
	private Long ordinamentoPosizione;
	private String ordinamentoDirezione;

	public Long getOrdinamentoId() {
		return ordinamentoId;
	}

	public void setOrdinamentoId(Long ordinamentoId) {
		this.ordinamentoId = ordinamentoId;
	}

	public String getOrdinamentoCriterio() {
		return ordinamentoCriterio;
	}

	public void setOrdinamentoCriterio(String ordinamentoCriterio) {
		this.ordinamentoCriterio = ordinamentoCriterio;
	}

	public String getOrdinamentoTabella() {
		return ordinamentoTabella;
	}

	public void setOrdinamentoTabella(String ordinamentoTabella) {
		this.ordinamentoTabella = ordinamentoTabella;
	}

	public String getOrdinamentoCampo() {
		return ordinamentoCampo;
	}

	public void setOrdinamentoCampo(String ordinamentoCampo) {
		this.ordinamentoCampo = ordinamentoCampo;
	}

	public Long getOrdinamentoPosizione() {
		return ordinamentoPosizione;
	}

	public void setOrdinamentoPosizione(Long ordinamentoPosizione) {
		this.ordinamentoPosizione = ordinamentoPosizione;
	}

	public String getOrdinamentoDirezione() {
		return ordinamentoDirezione;
	}

	public void setOrdinamentoDirezione(String ordinamentoDirezione) {
		this.ordinamentoDirezione = ordinamentoDirezione;
	}

	@Override
	public String toString() {
		return "ModelGraduatoriaOrdinamento [ordinamentoId=" + ordinamentoId + ", ordinamentoCriterio="
				+ ordinamentoCriterio + ", ordinamentoTabella=" + ordinamentoTabella + ", ordinamentoCampo="
				+ ordinamentoCampo + ", ordinamentoPosizione=" + ordinamentoPosizione + ", ordinamentoDirezione="
				+ ordinamentoDirezione + "]";
	}

}
