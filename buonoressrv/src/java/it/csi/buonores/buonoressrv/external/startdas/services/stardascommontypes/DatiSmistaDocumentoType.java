
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
 * Classe Java per DatiSmistaDocumentoType complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="DatiSmistaDocumentoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ResponsabileTrattamento" type="{http://www.csi.it/stardas/services/StardasCommonTypes}CodiceFiscaleType"/&gt;
 *         &lt;element name="IdDocumentoFruitore" type="{http://www.csi.it/stardas/services/StardasCommonTypes}String200Type"/&gt;
 *         &lt;element name="DocumentoElettronico" type="{http://www.csi.it/stardas/services/StardasCommonTypes}DocumentoElettronicoType"/&gt;
 *         &lt;choice minOccurs="0"&gt;
 *           &lt;element name="DatiDocumentoXML" type="{http://www.csi.it/stardas/services/StardasCommonTypes}EmbeddedXMLType"/&gt;
 *           &lt;element name="DatiDocumentoJSON" type="{http://www.csi.it/stardas/services/StardasCommonTypes}EmbeddedJSONType"/&gt;
 *         &lt;/choice&gt;
 *         &lt;element name="Metadati" type="{http://www.csi.it/stardas/services/StardasCommonTypes}MetadatiType" minOccurs="0"/&gt;
 *         &lt;element name="MessageUUIDPrincipale" type="{http://www.csi.it/stardas/services/StardasCommonTypes}String50Type" minOccurs="0"/&gt;
 *         &lt;element name="NumAllegati" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatiSmistaDocumentoType", propOrder = {
        "responsabileTrattamento",
        "idDocumentoFruitore",
        "documentoElettronico",
        "datiDocumentoXML",
        "datiDocumentoJSON",
        "metadati",
        "messageUUIDPrincipale",
        "numAllegati"
})
public class DatiSmistaDocumentoType {

    @XmlElement(name = "ResponsabileTrattamento", required = true)
    protected String responsabileTrattamento;
    @XmlElement(name = "IdDocumentoFruitore", required = true)
    protected String idDocumentoFruitore;
    @XmlElement(name = "DocumentoElettronico", required = true)
    protected DocumentoElettronicoType documentoElettronico;
    @XmlElement(name = "DatiDocumentoXML")
    protected EmbeddedXMLType datiDocumentoXML;
    @XmlElement(name = "DatiDocumentoJSON")
    protected EmbeddedJSONType datiDocumentoJSON;
    @XmlElement(name = "Metadati")
    protected MetadatiType metadati;
    @XmlElement(name = "MessageUUIDPrincipale")
    protected String messageUUIDPrincipale;
    @XmlElement(name = "NumAllegati")
    protected Integer numAllegati;

    /**
     * Recupera il valore della proprieta responsabileTrattamento.
     * 
     * @return
     *         possible object is
     *         {@link String }
     * 
     */
    public String getResponsabileTrattamento() {
        return responsabileTrattamento;
    }

    /**
     * Imposta il valore della proprieta responsabileTrattamento.
     * 
     * @param value
     *              allowed object is
     *              {@link String }
     * 
     */
    public void setResponsabileTrattamento(String value) {
        this.responsabileTrattamento = value;
    }

    /**
     * Recupera il valore della proprieta idDocumentoFruitore.
     * 
     * @return
     *         possible object is
     *         {@link String }
     * 
     */
    public String getIdDocumentoFruitore() {
        return idDocumentoFruitore;
    }

    /**
     * Imposta il valore della proprieta idDocumentoFruitore.
     * 
     * @param value
     *              allowed object is
     *              {@link String }
     * 
     */
    public void setIdDocumentoFruitore(String value) {
        this.idDocumentoFruitore = value;
    }

    /**
     * Recupera il valore della proprieta documentoElettronico.
     * 
     * @return
     *         possible object is
     *         {@link DocumentoElettronicoType }
     * 
     */
    public DocumentoElettronicoType getDocumentoElettronico() {
        return documentoElettronico;
    }

    /**
     * Imposta il valore della proprieta documentoElettronico.
     * 
     * @param value
     *              allowed object is
     *              {@link DocumentoElettronicoType }
     * 
     */
    public void setDocumentoElettronico(DocumentoElettronicoType value) {
        this.documentoElettronico = value;
    }

    /**
     * Recupera il valore della proprieta datiDocumentoXML.
     * 
     * @return
     *         possible object is
     *         {@link EmbeddedXMLType }
     * 
     */
    public EmbeddedXMLType getDatiDocumentoXML() {
        return datiDocumentoXML;
    }

    /**
     * Imposta il valore della proprieta datiDocumentoXML.
     * 
     * @param value
     *              allowed object is
     *              {@link EmbeddedXMLType }
     * 
     */
    public void setDatiDocumentoXML(EmbeddedXMLType value) {
        this.datiDocumentoXML = value;
    }

    /**
     * Recupera il valore della proprieta datiDocumentoJSON.
     * 
     * @return
     *         possible object is
     *         {@link EmbeddedJSONType }
     * 
     */
    public EmbeddedJSONType getDatiDocumentoJSON() {
        return datiDocumentoJSON;
    }

    /**
     * Imposta il valore della proprieta datiDocumentoJSON.
     * 
     * @param value
     *              allowed object is
     *              {@link EmbeddedJSONType }
     * 
     */
    public void setDatiDocumentoJSON(EmbeddedJSONType value) {
        this.datiDocumentoJSON = value;
    }

    /**
     * Recupera il valore della proprieta metadati.
     * 
     * @return
     *         possible object is
     *         {@link MetadatiType }
     * 
     */
    public MetadatiType getMetadati() {
        return metadati;
    }

    /**
     * Imposta il valore della proprieta metadati.
     * 
     * @param value
     *              allowed object is
     *              {@link MetadatiType }
     * 
     */
    public void setMetadati(MetadatiType value) {
        this.metadati = value;
    }

    /**
     * Recupera il valore della proprieta messageUUIDPrincipale.
     * 
     * @return
     *         possible object is
     *         {@link String }
     * 
     */
    public String getMessageUUIDPrincipale() {
        return messageUUIDPrincipale;
    }

    /**
     * Imposta il valore della proprieta messageUUIDPrincipale.
     * 
     * @param value
     *              allowed object is
     *              {@link String }
     * 
     */
    public void setMessageUUIDPrincipale(String value) {
        this.messageUUIDPrincipale = value;
    }

    /**
     * Recupera il valore della proprieta numAllegati.
     * 
     * @return
     *         possible object is
     *         {@link Integer }
     * 
     */
    public Integer getNumAllegati() {
        return numAllegati;
    }

    /**
     * Imposta il valore della proprieta numAllegati.
     * 
     * @param value
     *              allowed object is
     *              {@link Integer }
     * 
     */
    public void setNumAllegati(Integer value) {
        this.numAllegati = value;
    }

}
