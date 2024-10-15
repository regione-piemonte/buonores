/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbatch.external.anagraficaservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per InterrogaMefEsenreddRes complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="InterrogaMefEsenreddRes"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="errorCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="esitomef" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="body" type="{http://InterrogaMefEsenredd.central.services.auraws.aura.csi.it}InterrogaMefEsenreddResponseBody" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InterrogaMefEsenreddRes", propOrder = {
    "errorCode",
    "esitomef",
    "body"
})
@XmlRootElement
public class InterrogaMefEsenreddRes {

    protected String errorCode;
    protected String esitomef;
    protected InterrogaMefEsenreddResponseBody body;

    /**
     * Recupera il valore della proprietà errorCode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Imposta il valore della proprietà errorCode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorCode(String value) {
        this.errorCode = value;
    }

    /**
     * Recupera il valore della proprietà esitomef.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEsitomef() {
        return esitomef;
    }

    /**
     * Imposta il valore della proprietà esitomef.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEsitomef(String value) {
        this.esitomef = value;
    }

    /**
     * Recupera il valore della proprietà body.
     * 
     * @return
     *     possible object is
     *     {@link InterrogaMefEsenreddResponseBody }
     *     
     */
    public InterrogaMefEsenreddResponseBody getBody() {
        return body;
    }

    /**
     * Imposta il valore della proprietà body.
     * 
     * @param value
     *     allowed object is
     *     {@link InterrogaMefEsenreddResponseBody }
     *     
     */
    public void setBody(InterrogaMefEsenreddResponseBody value) {
        this.body = value;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InterrogaMefEsenreddRes [errorCode=");
		builder.append(errorCode);
		builder.append(", esitomef=");
		builder.append(esitomef);
		builder.append(", body=");
		builder.append(body);
		builder.append("]");
		return builder.toString();
	}

}
