
/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.external.startdas.services.stardascommontypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per EmbeddedJSONType complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="EmbeddedJSONType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Content" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EmbeddedJSONType", propOrder = {
        "content"
})
public class EmbeddedJSONType {

    @XmlElement(name = "Content", required = true)
    protected String content;

    /**
     * Recupera il valore della proprieta content.
     * 
     * @return
     *         possible object is
     *         {@link String }
     * 
     */
    public String getContent() {
        return content;
    }

    /**
     * Imposta il valore della proprieta content.
     * 
     * @param value
     *              allowed object is
     *              {@link String }
     * 
     */
    public void setContent(String value) {
        this.content = value;
    }

}
