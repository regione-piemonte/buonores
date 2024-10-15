
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

import it.csi.buonores.buonoressrv.external.startdas.services.stardascommontypes.ConfigurazioneChiamanteType;
import it.csi.buonores.buonoressrv.external.startdas.services.stardascommontypes.DatiSmistaDocumentoType;

/**
 * <p>
 * Classe Java per SmistaDocumentoRequestType complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="SmistaDocumentoRequestType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ConfigurazioneChiamante" type="{http://www.csi.it/stardas/services/StardasCommonTypes}ConfigurazioneChiamanteType"/&gt;
 *         &lt;element name="DatiSmistaDocumento" type="{http://www.csi.it/stardas/services/StardasCommonTypes}DatiSmistaDocumentoType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SmistaDocumentoRequestType", propOrder = {
        "configurazioneChiamante",
        "datiSmistaDocumento"
})
@XmlRootElement
public class SmistaDocumentoRequestType {

    @XmlElement(name = "ConfigurazioneChiamante", required = true)
    protected ConfigurazioneChiamanteType configurazioneChiamante;
    @XmlElement(name = "DatiSmistaDocumento", required = true)
    protected DatiSmistaDocumentoType datiSmistaDocumento;

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
     * Recupera il valore della proprieta datiSmistaDocumento.
     * 
     * @return
     *         possible object is
     *         {@link DatiSmistaDocumentoType }
     * 
     */
    public DatiSmistaDocumentoType getDatiSmistaDocumento() {
        return datiSmistaDocumento;
    }

    /**
     * Imposta il valore della proprieta datiSmistaDocumento.
     * 
     * @param value
     *              allowed object is
     *              {@link DatiSmistaDocumentoType }
     * 
     */
    public void setDatiSmistaDocumento(DatiSmistaDocumentoType value) {
        this.datiSmistaDocumento = value;
    }

}
