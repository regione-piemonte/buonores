
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
 * Classe Java per ConfigurazioneChiamanteType complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="ConfigurazioneChiamanteType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CodiceFiscaleEnte" type="{http://www.csi.it/stardas/services/StardasCommonTypes}String50Type"/&gt;
 *         &lt;element name="CodiceFruitore" type="{http://www.csi.it/stardas/services/StardasCommonTypes}String50Type"/&gt;
 *         &lt;element name="CodiceApplicazione" type="{http://www.csi.it/stardas/services/StardasCommonTypes}String50Type"/&gt;
 *         &lt;element name="CodiceTipoDocumento" type="{http://www.csi.it/stardas/services/StardasCommonTypes}String50Type"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConfigurazioneChiamanteType", propOrder = {
        "codiceFiscaleEnte",
        "codiceFruitore",
        "codiceApplicazione",
        "codiceTipoDocumento"
})
public class ConfigurazioneChiamanteType {

    @XmlElement(name = "CodiceFiscaleEnte", required = true)
    protected String codiceFiscaleEnte;
    @XmlElement(name = "CodiceFruitore", required = true)
    protected String codiceFruitore;
    @XmlElement(name = "CodiceApplicazione", required = true)
    protected String codiceApplicazione;
    @XmlElement(name = "CodiceTipoDocumento", required = true)
    protected String codiceTipoDocumento;

    /**
     * Recupera il valore della propriet codiceFiscaleEnte.
     * 
     * @return
     *         possible object is
     *         {@link String }
     * 
     */
    public String getCodiceFiscaleEnte() {
        return codiceFiscaleEnte;
    }

    /**
     * Imposta il valore della proprieta codiceFiscaleEnte.
     * 
     * @param value
     *              allowed object is
     *              {@link String }
     * 
     */
    public void setCodiceFiscaleEnte(String value) {
        this.codiceFiscaleEnte = value;
    }

    /**
     * Recupera il valore della proprieta codiceFruitore.
     * 
     * @return
     *         possible object is
     *         {@link String }
     * 
     */
    public String getCodiceFruitore() {
        return codiceFruitore;
    }

    /**
     * Imposta il valore della proprieta codiceFruitore.
     * 
     * @param value
     *              allowed object is
     *              {@link String }
     * 
     */
    public void setCodiceFruitore(String value) {
        this.codiceFruitore = value;
    }

    /**
     * Recupera il valore della proprieta codiceApplicazione.
     * 
     * @return
     *         possible object is
     *         {@link String }
     * 
     */
    public String getCodiceApplicazione() {
        return codiceApplicazione;
    }

    /**
     * Imposta il valore della proprieta codiceApplicazione.
     * 
     * @param value
     *              allowed object is
     *              {@link String }
     * 
     */
    public void setCodiceApplicazione(String value) {
        this.codiceApplicazione = value;
    }

    /**
     * Recupera il valore della proprieta codiceTipoDocumento.
     * 
     * @return
     *         possible object is
     *         {@link String }
     * 
     */
    public String getCodiceTipoDocumento() {
        return codiceTipoDocumento;
    }

    /**
     * Imposta il valore della proprieta codiceTipoDocumento.
     * 
     * @param value
     *              allowed object is
     *              {@link String }
     * 
     */
    public void setCodiceTipoDocumento(String value) {
        this.codiceTipoDocumento = value;
    }

}
