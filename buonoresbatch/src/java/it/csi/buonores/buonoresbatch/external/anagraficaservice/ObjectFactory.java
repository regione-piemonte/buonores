/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbatch.external.anagraficaservice;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.csi.aura.auraws.services.central.interrogamefesenredd package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.csi.aura.auraws.services.central.interrogamefesenredd
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link InterrogaMefEsenredd }
     * 
     */
    public InterrogaMefEsenredd createInterrogaMefEsenredd() {
        return new InterrogaMefEsenredd();
    }

    /**
     * Create an instance of {@link InterrogaMefEsenreddResponse }
     * 
     */
    public InterrogaMefEsenreddResponse createInterrogaMefEsenreddResponse() {
        return new InterrogaMefEsenreddResponse();
    }

    /**
     * Create an instance of {@link InterrogaMefEsenreddRes }
     * 
     */
    public InterrogaMefEsenreddRes createInterrogaMefEsenreddRes() {
        return new InterrogaMefEsenreddRes();
    }

    /**
     * Create an instance of {@link InterrogaMefEsenreddResponseBody }
     * 
     */
    public InterrogaMefEsenreddResponseBody createInterrogaMefEsenreddResponseBody() {
        return new InterrogaMefEsenreddResponseBody();
    }

}
