/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbatch.external.anagraficaservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per InterrogaMefEsenreddResponseBody complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="InterrogaMefEsenreddResponseBody"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="capResidenza" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="codicefiscale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="cognome" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="comuneResidenza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="comunenasc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="datadecesso" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="datanascita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="findType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idComunenascita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idStatonascita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="indirizzoResidenza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nazionenasc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nome" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="provinciaResidenza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="provincianasc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="provnasc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="sceltaInDeroga" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="sesso" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="privacy" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InterrogaMefEsenreddResponseBody", propOrder = {
    "capResidenza",
    "codicefiscale",
    "cognome",
    "comuneResidenza",
    "comunenasc",
    "datadecesso",
    "datanascita",
    "findType",
    "idComunenascita",
    "idStatonascita",
    "indirizzoResidenza",
    "nazionenasc",
    "nome",
    "provinciaResidenza",
    "provincianasc",
    "provnasc",
    "sceltaInDeroga",
    "sesso",
    "privacy"
})
@XmlRootElement
public class InterrogaMefEsenreddResponseBody {

    protected Long capResidenza;
    protected String codicefiscale;
    protected String cognome;
    protected String comuneResidenza;
    protected String comunenasc;
    protected String datadecesso;
    protected String datanascita;
    protected String findType;
    protected String idComunenascita;
    protected String idStatonascita;
    protected String indirizzoResidenza;
    protected String nazionenasc;
    protected String nome;
    protected String provinciaResidenza;
    protected String provincianasc;
    protected String provnasc;
    protected String sceltaInDeroga;
    protected String sesso;
    protected String privacy;

    /**
     * Recupera il valore della proprietà capResidenza.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCapResidenza() {
        return capResidenza;
    }

    /**
     * Imposta il valore della proprietà capResidenza.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCapResidenza(Long value) {
        this.capResidenza = value;
    }

    /**
     * Recupera il valore della proprietà codicefiscale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodicefiscale() {
        return codicefiscale;
    }

    /**
     * Imposta il valore della proprietà codicefiscale.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodicefiscale(String value) {
        this.codicefiscale = value;
    }

    /**
     * Recupera il valore della proprietà cognome.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Imposta il valore della proprietà cognome.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCognome(String value) {
        this.cognome = value;
    }

    /**
     * Recupera il valore della proprietà comuneResidenza.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComuneResidenza() {
        return comuneResidenza;
    }

    /**
     * Imposta il valore della proprietà comuneResidenza.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComuneResidenza(String value) {
        this.comuneResidenza = value;
    }

    /**
     * Recupera il valore della proprietà comunenasc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComunenasc() {
        return comunenasc;
    }

    /**
     * Imposta il valore della proprietà comunenasc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComunenasc(String value) {
        this.comunenasc = value;
    }

    /**
     * Recupera il valore della proprietà datadecesso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatadecesso() {
        return datadecesso;
    }

    /**
     * Imposta il valore della proprietà datadecesso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatadecesso(String value) {
        this.datadecesso = value;
    }

    /**
     * Recupera il valore della proprietà datanascita.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatanascita() {
        return datanascita;
    }

    /**
     * Imposta il valore della proprietà datanascita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatanascita(String value) {
        this.datanascita = value;
    }

    /**
     * Recupera il valore della proprietà findType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFindType() {
        return findType;
    }

    /**
     * Imposta il valore della proprietà findType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFindType(String value) {
        this.findType = value;
    }

    /**
     * Recupera il valore della proprietà idComunenascita.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdComunenascita() {
        return idComunenascita;
    }

    /**
     * Imposta il valore della proprietà idComunenascita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdComunenascita(String value) {
        this.idComunenascita = value;
    }

    /**
     * Recupera il valore della proprietà idStatonascita.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdStatonascita() {
        return idStatonascita;
    }

    /**
     * Imposta il valore della proprietà idStatonascita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdStatonascita(String value) {
        this.idStatonascita = value;
    }

    /**
     * Recupera il valore della proprietà indirizzoResidenza.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndirizzoResidenza() {
        return indirizzoResidenza;
    }

    /**
     * Imposta il valore della proprietà indirizzoResidenza.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndirizzoResidenza(String value) {
        this.indirizzoResidenza = value;
    }

    /**
     * Recupera il valore della proprietà nazionenasc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNazionenasc() {
        return nazionenasc;
    }

    /**
     * Imposta il valore della proprietà nazionenasc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNazionenasc(String value) {
        this.nazionenasc = value;
    }

    /**
     * Recupera il valore della proprietà nome.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il valore della proprietà nome.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNome(String value) {
        this.nome = value;
    }

    /**
     * Recupera il valore della proprietà provinciaResidenza.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProvinciaResidenza() {
        return provinciaResidenza;
    }

    /**
     * Imposta il valore della proprietà provinciaResidenza.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProvinciaResidenza(String value) {
        this.provinciaResidenza = value;
    }

    /**
     * Recupera il valore della proprietà provincianasc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProvincianasc() {
        return provincianasc;
    }

    /**
     * Imposta il valore della proprietà provincianasc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProvincianasc(String value) {
        this.provincianasc = value;
    }

    /**
     * Recupera il valore della proprietà provnasc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProvnasc() {
        return provnasc;
    }

    /**
     * Imposta il valore della proprietà provnasc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProvnasc(String value) {
        this.provnasc = value;
    }

    /**
     * Recupera il valore della proprietà sceltaInDeroga.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSceltaInDeroga() {
        return sceltaInDeroga;
    }

    /**
     * Imposta il valore della proprietà sceltaInDeroga.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSceltaInDeroga(String value) {
        this.sceltaInDeroga = value;
    }

    /**
     * Recupera il valore della proprietà sesso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSesso() {
        return sesso;
    }

    /**
     * Imposta il valore della proprietà sesso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSesso(String value) {
        this.sesso = value;
    }

    /**
     * Recupera il valore della proprietà privacy.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrivacy() {
        return privacy;
    }

    /**
     * Imposta il valore della proprietà privacy.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrivacy(String value) {
        this.privacy = value;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InterrogaMefEsenreddResponseBody [capResidenza=");
		builder.append(capResidenza);
		builder.append(", codicefiscale=");
		builder.append(codicefiscale);
		builder.append(", cognome=");
		builder.append(cognome);
		builder.append(", comuneResidenza=");
		builder.append(comuneResidenza);
		builder.append(", comunenasc=");
		builder.append(comunenasc);
		builder.append(", datadecesso=");
		builder.append(datadecesso);
		builder.append(", datanascita=");
		builder.append(datanascita);
		builder.append(", idComunenascita=");
		builder.append(idComunenascita);
		builder.append(", idStatonascita=");
		builder.append(idStatonascita);
		builder.append(", indirizzoResidenza=");
		builder.append(indirizzoResidenza);
		builder.append(", nazionenasc=");
		builder.append(nazionenasc);
		builder.append(", nome=");
		builder.append(nome);
		builder.append(", provinciaResidenza=");
		builder.append(provinciaResidenza);
		builder.append(", provincianasc=");
		builder.append(provincianasc);
		builder.append(", provnasc=");
		builder.append(provnasc);
		builder.append(", sesso=");
		builder.append(sesso);
		builder.append("]");
		return builder.toString();
	}
}
