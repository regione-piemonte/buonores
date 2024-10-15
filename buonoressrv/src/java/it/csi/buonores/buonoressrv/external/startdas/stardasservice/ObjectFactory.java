
/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoressrv.external.startdas.stardasservice;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import it.csi.buonores.buonoressrv.external.startdas.services.stardascommontypes.ResponseType;

/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the it.csi.stardas.wso2.stardasservice package.
 * <p>
 * An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups. Factory methods for each of these are
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SmistaDocumentoRequest_QNAME = new QName(
            "http://www.csi.it/stardas/wso2/StardasService", "SmistaDocumentoRequest");
    private final static QName _SmistaDocumentoResponse_QNAME = new QName(
            "http://www.csi.it/stardas/wso2/StardasService", "SmistaDocumentoResponse");
    private final static QName _GetStatoRichiestaRequest_QNAME = new QName(
            "http://www.csi.it/stardas/wso2/StardasService", "GetStatoRichiestaRequest");
    private final static QName _GetStatoRichiestaResponse_QNAME = new QName(
            "http://www.csi.it/stardas/wso2/StardasService", "GetStatoRichiestaResponse");
    private final static QName _StardasServiceResponse_QNAME = new QName(
            "http://www.csi.it/stardas/wso2/StardasService", "StardasServiceResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema
     * derived classes for package: it.csi.stardas.wso2.stardasservice
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SmistaDocumentoRequestType }
     * 
     */
    public SmistaDocumentoRequestType createSmistaDocumentoRequestType() {
        return new SmistaDocumentoRequestType();
    }

    /**
     * Create an instance of {@link SmistaDocumentoResponseType }
     * 
     */
    public SmistaDocumentoResponseType createSmistaDocumentoResponseType() {
        return new SmistaDocumentoResponseType();
    }

    /**
     * Create an instance of {@link GetStatoRichiestaRequestType }
     * 
     */
    public GetStatoRichiestaRequestType createGetStatoRichiestaRequestType() {
        return new GetStatoRichiestaRequestType();
    }

    /**
     * Create an instance of {@link GetStatoRichiestaResponseType }
     * 
     */
    public GetStatoRichiestaResponseType createGetStatoRichiestaResponseType() {
        return new GetStatoRichiestaResponseType();
    }

    /**
     * Create an instance of {@link JAXBElement
     * }{@code <}{@link SmistaDocumentoRequestType }{@code >}
     * 
     * @param value
     *              Java instance representing xml element's value.
     * @return
     *         the new instance of {@link JAXBElement
     *         }{@code <}{@link SmistaDocumentoRequestType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.csi.it/stardas/wso2/StardasService", name = "SmistaDocumentoRequest")
    public JAXBElement<SmistaDocumentoRequestType> createSmistaDocumentoRequest(SmistaDocumentoRequestType value) {
        return new JAXBElement<SmistaDocumentoRequestType>(_SmistaDocumentoRequest_QNAME,
                SmistaDocumentoRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement
     * }{@code <}{@link SmistaDocumentoResponseType }{@code >}
     * 
     * @param value
     *              Java instance representing xml element's value.
     * @return
     *         the new instance of {@link JAXBElement
     *         }{@code <}{@link SmistaDocumentoResponseType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.csi.it/stardas/wso2/StardasService", name = "SmistaDocumentoResponse")
    public JAXBElement<SmistaDocumentoResponseType> createSmistaDocumentoResponse(SmistaDocumentoResponseType value) {
        return new JAXBElement<SmistaDocumentoResponseType>(_SmistaDocumentoResponse_QNAME,
                SmistaDocumentoResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement
     * }{@code <}{@link GetStatoRichiestaRequestType }{@code >}
     * 
     * @param value
     *              Java instance representing xml element's value.
     * @return
     *         the new instance of {@link JAXBElement
     *         }{@code <}{@link GetStatoRichiestaRequestType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.csi.it/stardas/wso2/StardasService", name = "GetStatoRichiestaRequest")
    public JAXBElement<GetStatoRichiestaRequestType> createGetStatoRichiestaRequest(
            GetStatoRichiestaRequestType value) {
        return new JAXBElement<GetStatoRichiestaRequestType>(_GetStatoRichiestaRequest_QNAME,
                GetStatoRichiestaRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement
     * }{@code <}{@link GetStatoRichiestaResponseType }{@code >}
     * 
     * @param value
     *              Java instance representing xml element's value.
     * @return
     *         the new instance of {@link JAXBElement
     *         }{@code <}{@link GetStatoRichiestaResponseType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.csi.it/stardas/wso2/StardasService", name = "GetStatoRichiestaResponse")
    public JAXBElement<GetStatoRichiestaResponseType> createGetStatoRichiestaResponse(
            GetStatoRichiestaResponseType value) {
        return new JAXBElement<GetStatoRichiestaResponseType>(_GetStatoRichiestaResponse_QNAME,
                GetStatoRichiestaResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResponseType
     * }{@code >}
     * 
     * @param value
     *              Java instance representing xml element's value.
     * @return
     *         the new instance of {@link JAXBElement }{@code <}{@link ResponseType
     *         }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.csi.it/stardas/wso2/StardasService", name = "StardasServiceResponse")
    public JAXBElement<ResponseType> createStardasServiceResponse(ResponseType value) {
        return new JAXBElement<ResponseType>(_StardasServiceResponse_QNAME, ResponseType.class, null, value);
    }

}
