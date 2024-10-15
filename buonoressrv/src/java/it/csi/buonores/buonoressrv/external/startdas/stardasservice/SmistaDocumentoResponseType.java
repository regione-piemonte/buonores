
/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.external.startdas.stardasservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import it.csi.buonores.buonoressrv.external.startdas.services.stardascommontypes.ResponseType;

/**
 * <p>
 * Classe Java per SmistaDocumentoResponseType complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="SmistaDocumentoResponseType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.csi.it/stardas/services/StardasCommonTypes}ResponseType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="MessageUUID" type="{http://www.csi.it/stardas/services/StardasCommonTypes}String50Type"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SmistaDocumentoResponseType", propOrder = {
        "messageUUID"
})
@XmlRootElement
public class SmistaDocumentoResponseType
        extends ResponseType {

    @XmlElement(name = "MessageUUID", required = true)
    protected String messageUUID;

    /**
     * Recupera il valore della proprieta messageUUID.
     * 
     * @return
     *         possible object is
     *         {@link String }
     * 
     */
    public String getMessageUUID() {
        return messageUUID;
    }

    /**
     * Imposta il valore della proprieta messageUUID.
     * 
     * @param value
     *              allowed object is
     *              {@link String }
     * 
     */
    public void setMessageUUID(String value) {
        this.messageUUID = value;
    }

}
