
/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.external.anagraficaservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per anonymous complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CF" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "cf"
})
@XmlRootElement(name = "InterrogaMefEsenredd")
public class InterrogaMefEsenredd {

    @XmlElement(name = "CF")
    protected String cf;

    /**
     * Recupera il valore della proprietà cf.
     * 
     * @return
     *         possible object is
     *         {@link String }
     * 
     */
    public String getCF() {
        return cf;
    }

    /**
     * Imposta il valore della proprietà cf.
     * 
     * @param value
     *              allowed object is
     *              {@link String }
     * 
     */
    public void setCF(String value) {
        this.cf = value;
    }

}
