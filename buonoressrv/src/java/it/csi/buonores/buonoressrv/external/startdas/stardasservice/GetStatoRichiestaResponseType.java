
/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.external.startdas.stardasservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import it.csi.buonores.buonoressrv.external.startdas.services.stardascommontypes.ResponseType;
import it.csi.buonores.buonoressrv.external.startdas.services.stardascommontypes.StatoRichiestaType;

/**
 * <p>
 * Classe Java per GetStatoRichiestaResponseType complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="GetStatoRichiestaResponseType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.csi.it/stardas/services/StardasCommonTypes}ResponseType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="StatoRichiesta" type="{http://www.csi.it/stardas/services/StardasCommonTypes}StatoRichiestaType" minOccurs="0"/&gt;
 *         &lt;element name="CodiceEsitoRichiesta" type="{http://www.csi.it/stardas/services/StardasCommonTypes}CodiceEsitoType" minOccurs="0"/&gt;
 *         &lt;element name="DettaglioEsitoRichiesta" type="{http://www.csi.it/stardas/services/StardasCommonTypes}String400Type" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetStatoRichiestaResponseType", propOrder = {
        "statoRichiesta",
        "codiceEsitoRichiesta",
        "dettaglioEsitoRichiesta"
})
public class GetStatoRichiestaResponseType
        extends ResponseType {

    @XmlElement(name = "StatoRichiesta")
    @XmlSchemaType(name = "string")
    protected StatoRichiestaType statoRichiesta;
    @XmlElement(name = "CodiceEsitoRichiesta")
    protected String codiceEsitoRichiesta;
    @XmlElement(name = "DettaglioEsitoRichiesta")
    protected String dettaglioEsitoRichiesta;

    /**
     * Recupera il valore della proprieta statoRichiesta.
     * 
     * @return
     *         possible object is
     *         {@link StatoRichiestaType }
     * 
     */
    public StatoRichiestaType getStatoRichiesta() {
        return statoRichiesta;
    }

    /**
     * Imposta il valore della proprieta statoRichiesta.
     * 
     * @param value
     *              allowed object is
     *              {@link StatoRichiestaType }
     * 
     */
    public void setStatoRichiesta(StatoRichiestaType value) {
        this.statoRichiesta = value;
    }

    /**
     * Recupera il valore della proprieta codiceEsitoRichiesta.
     * 
     * @return
     *         possible object is
     *         {@link String }
     * 
     */
    public String getCodiceEsitoRichiesta() {
        return codiceEsitoRichiesta;
    }

    /**
     * Imposta il valore della proprieta codiceEsitoRichiesta.
     * 
     * @param value
     *              allowed object is
     *              {@link String }
     * 
     */
    public void setCodiceEsitoRichiesta(String value) {
        this.codiceEsitoRichiesta = value;
    }

    /**
     * Recupera il valore della proprieta dettaglioEsitoRichiesta.
     * 
     * @return
     *         possible object is
     *         {@link String }
     * 
     */
    public String getDettaglioEsitoRichiesta() {
        return dettaglioEsitoRichiesta;
    }

    /**
     * Imposta il valore della proprieta dettaglioEsitoRichiesta.
     * 
     * @param value
     *              allowed object is
     *              {@link String }
     * 
     */
    public void setDettaglioEsitoRichiesta(String value) {
        this.dettaglioEsitoRichiesta = value;
    }

}
