
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
 * Classe Java per ResultType complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="ResultType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Codice" type="{http://www.csi.it/stardas/services/StardasCommonTypes}CodiceEsitoType"/&gt;
 *         &lt;element name="Messaggio" type="{http://www.csi.it/stardas/services/StardasCommonTypes}String400Type" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResultType", propOrder = {
        "codice",
        "messaggio"
})
public class ResultType {

    @XmlElement(name = "Codice", required = true)
    protected String codice;
    @XmlElement(name = "Messaggio")
    protected String messaggio;

    /**
     * Recupera il valore della proprieta codice.
     * 
     * @return
     *         possible object is
     *         {@link String }
     * 
     */
    public String getCodice() {
        return codice;
    }

    /**
     * Imposta il valore della proprieta codice.
     * 
     * @param value
     *              allowed object is
     *              {@link String }
     * 
     */
    public void setCodice(String value) {
        this.codice = value;
    }

    /**
     * Recupera il valore della proprieta messaggio.
     * 
     * @return
     *         possible object is
     *         {@link String }
     * 
     */
    public String getMessaggio() {
        return messaggio;
    }

    /**
     * Imposta il valore della proprieta messaggio.
     * 
     * @param value
     *              allowed object is
     *              {@link String }
     * 
     */
    public void setMessaggio(String value) {
        this.messaggio = value;
    }

}
