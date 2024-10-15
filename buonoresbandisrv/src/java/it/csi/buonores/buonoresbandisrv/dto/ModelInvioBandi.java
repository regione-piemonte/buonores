/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.dto;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "domanda_di_agevolazione")
@XmlAccessorType(XmlAccessType.FIELD)
public class ModelInvioBandi implements Serializable {

	private static final long serialVersionUID = -5023075542437442055L;

	@XmlElementWrapper(name = "progetti")
	@XmlElement(name = "progetto")
	private List<ModelProgettoBandi> progetti;

	@XmlAttribute
	private String version;
	@XmlAttribute
	private String xmlns;

	public ModelInvioBandi() {
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getXmlns() {
		return xmlns;
	}

	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}

	public List<ModelProgettoBandi> getProgetti() {
		return progetti;
	}

	public void setProgetti(List<ModelProgettoBandi> progetti) {
		this.progetti = progetti;
	}
}
