
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
 * Classe Java per MetadatoType complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="MetadatoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Nome" type="{http://www.csi.it/stardas/services/StardasCommonTypes}String200Type"/&gt;
 *         &lt;element name="Valore" type="{http://www.csi.it/stardas/services/StardasCommonTypes}String1000Type"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MetadatoType", propOrder = {
        "nome",
        "valore"
})
public class MetadatoType {

    @XmlElement(name = "Nome", required = true)
    protected String nome;
    @XmlElement(name = "Valore", required = true)
    protected String valore;

    /**
     * Recupera il valore della proprieta nome.
     * 
     * @return
     *         possible object is
     *         {@link String }
     * 
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il valore della proprieta nome.
     * 
     * @param value
     *              allowed object is
     *              {@link String }
     * 
     */
    public void setNome(String value) {
        this.nome = value;
    }

    /**
     * Recupera il valore della proprieta valore.
     * 
     * @return
     *         possible object is
     *         {@link String }
     * 
     */
    public String getValore() {
        return valore;
    }

    /**
     * Imposta il valore della proprieta valore.
     * 
     * @param value
     *              allowed object is
     *              {@link String }
     * 
     */
    public void setValore(String value) {
        this.valore = value;
    }

}
