
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
 * Classe Java per RiferimentoECMType complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="RiferimentoECMType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="EcmUuid" type="{http://www.csi.it/stardas/services/StardasCommonTypes}String400Type"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RiferimentoECMType", propOrder = {
        "ecmUuid"
})
public class RiferimentoECMType {

    @XmlElement(name = "EcmUuid", required = true)
    protected String ecmUuid;

    /**
     * Recupera il valore della proprieta ecmUuid.
     * 
     * @return
     *         possible object is
     *         {@link String }
     * 
     */
    public String getEcmUuid() {
        return ecmUuid;
    }

    /**
     * Imposta il valore della proprieta ecmUuid.
     * 
     * @param value
     *              allowed object is
     *              {@link String }
     * 
     */
    public void setEcmUuid(String value) {
        this.ecmUuid = value;
    }

}
