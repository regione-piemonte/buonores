
/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.external.startdas.services.stardascommontypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.transform.Source;

/**
 * <p>
 * Classe Java per EmbeddedXMLType complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="EmbeddedXMLType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Content" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EmbeddedXMLType", propOrder = {
        "content"
})
public class EmbeddedXMLType {

    @XmlElement(name = "Content", required = true)
    @XmlMimeType("text/xml")
    protected Source content;

    /**
     * Recupera il valore della proprieta content.
     * 
     * @return
     *         possible object is
     *         {@link Source }
     * 
     */
    public Source getContent() {
        return content;
    }

    /**
     * Imposta il valore della proprieta content.
     * 
     * @param value
     *              allowed object is
     *              {@link Source }
     * 
     */
    public void setContent(Source value) {
        this.content = value;
    }

}
