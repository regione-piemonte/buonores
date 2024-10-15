
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
 * Classe Java per DocumentoElettronicoType complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="DocumentoElettronicoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="NomeFile" type="{http://www.csi.it/stardas/services/StardasCommonTypes}String1000Type"/&gt;
 *         &lt;choice&gt;
 *           &lt;element name="ContenutoBinario" type="{http://www.csi.it/stardas/services/StardasCommonTypes}EmbeddedBinaryType"/&gt;
 *           &lt;element name="RiferimentoECM" type="{http://www.csi.it/stardas/services/StardasCommonTypes}RiferimentoECMType"/&gt;
 *         &lt;/choice&gt;
 *         &lt;element name="DocumentoFirmato" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="MimeType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentoElettronicoType", propOrder = {
        "nomeFile",
        "contenutoBinario",
        "riferimentoECM",
        "documentoFirmato",
        "mimeType"
})
public class DocumentoElettronicoType {

    @XmlElement(name = "NomeFile", required = true)
    protected String nomeFile;
    @XmlElement(name = "ContenutoBinario")
    protected EmbeddedBinaryType contenutoBinario;
    @XmlElement(name = "RiferimentoECM")
    protected RiferimentoECMType riferimentoECM;
    @XmlElement(name = "DocumentoFirmato")
    protected boolean documentoFirmato;
    @XmlElement(name = "MimeType", required = true)
    protected String mimeType;

    /**
     * Recupera il valore della proprieta nomeFile.
     * 
     * @return
     *         possible object is
     *         {@link String }
     * 
     */
    public String getNomeFile() {
        return nomeFile;
    }

    /**
     * Imposta il valore della proprieta nomeFile.
     * 
     * @param value
     *              allowed object is
     *              {@link String }
     * 
     */
    public void setNomeFile(String value) {
        this.nomeFile = value;
    }

    /**
     * Recupera il valore della proprieta contenutoBinario.
     * 
     * @return
     *         possible object is
     *         {@link EmbeddedBinaryType }
     * 
     */
    public EmbeddedBinaryType getContenutoBinario() {
        return contenutoBinario;
    }

    /**
     * Imposta il valore della proprieta contenutoBinario.
     * 
     * @param value
     *              allowed object is
     *              {@link EmbeddedBinaryType }
     * 
     */
    public void setContenutoBinario(EmbeddedBinaryType value) {
        this.contenutoBinario = value;
    }

    /**
     * Recupera il valore della proprieta riferimentoECM.
     * 
     * @return
     *         possible object is
     *         {@link RiferimentoECMType }
     * 
     */
    public RiferimentoECMType getRiferimentoECM() {
        return riferimentoECM;
    }

    /**
     * Imposta il valore della proprieta riferimentoECM.
     * 
     * @param value
     *              allowed object is
     *              {@link RiferimentoECMType }
     * 
     */
    public void setRiferimentoECM(RiferimentoECMType value) {
        this.riferimentoECM = value;
    }

    /**
     * Recupera il valore della proprieta documentoFirmato.
     * 
     */
    public boolean isDocumentoFirmato() {
        return documentoFirmato;
    }

    /**
     * Imposta il valore della proprieta documentoFirmato.
     * 
     */
    public void setDocumentoFirmato(boolean value) {
        this.documentoFirmato = value;
    }

    /**
     * Recupera il valore della proprieta mimeType.
     * 
     * @return
     *         possible object is
     *         {@link String }
     * 
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * Imposta il valore della proprieta mimeType.
     * 
     * @param value
     *              allowed object is
     *              {@link String }
     * 
     */
    public void setMimeType(String value) {
        this.mimeType = value;
    }

}
