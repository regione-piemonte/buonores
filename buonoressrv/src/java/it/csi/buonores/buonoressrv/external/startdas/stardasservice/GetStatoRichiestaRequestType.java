
/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.external.startdas.stardasservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import it.csi.buonores.buonoressrv.external.startdas.services.stardascommontypes.ConfigurazioneChiamanteType;

/**
 * <p>
 * Classe Java per GetStatoRichiestaRequestType complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="GetStatoRichiestaRequestType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ConfigurazioneChiamante" type="{http://www.csi.it/stardas/services/StardasCommonTypes}ConfigurazioneChiamanteType"/&gt;
 *         &lt;element name="MessageUUID" type="{http://www.csi.it/stardas/services/StardasCommonTypes}String50Type"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetStatoRichiestaRequestType", propOrder = {
        "configurazioneChiamante",
        "messageUUID"
})
public class GetStatoRichiestaRequestType {

    @XmlElement(name = "ConfigurazioneChiamante", required = true)
    protected ConfigurazioneChiamanteType configurazioneChiamante;
    @XmlElement(name = "MessageUUID", required = true)
    protected String messageUUID;

    /**
     * Recupera il valore della proprieta configurazioneChiamante.
     * 
     * @return
     *         possible object is
     *         {@link ConfigurazioneChiamanteType }
     * 
     */
    public ConfigurazioneChiamanteType getConfigurazioneChiamante() {
        return configurazioneChiamante;
    }

    /**
     * Imposta il valore della proprieta configurazioneChiamante.
     * 
     * @param value
     *              allowed object is
     *              {@link ConfigurazioneChiamanteType }
     * 
     */
    public void setConfigurazioneChiamante(ConfigurazioneChiamanteType value) {
        this.configurazioneChiamante = value;
    }

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
