/*******************************************************************************
 
* Copyright Regione Piemonte - 2024
 
* SPDX-License-Identifier: EUPL-1.2
 
******************************************************************************/
package it.csi.buonores.buonoresbatch.external.anagraficaservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 3.5.3
 * 2022-12-16T12:45:31.471+01:00
 * Generated source version: 3.5.3
 *
 */
@WebService(targetNamespace = "http://InterrogaMefEsenredd.central.services.auraws.aura.csi.it", name = "InterrogaMefEsenredd1Soap")
@XmlSeeAlso({ObjectFactory.class})
public interface InterrogaMefEsenredd1Soap {

    @WebMethod(operationName = "InterrogaMefEsenredd", action = "http://InterrogaMefEsenredd.central.services.auraws.aura.csi.it/AURA.WS.InterrogaMefEsenredd.InterrogaMefEsenredd")
    @RequestWrapper(localName = "InterrogaMefEsenredd", targetNamespace = "http://InterrogaMefEsenredd.central.services.auraws.aura.csi.it", className = "it.csi.buonores.buonoresbatch.external.anagraficaservice.InterrogaMefEsenredd")
    @ResponseWrapper(localName = "InterrogaMefEsenreddResponse", targetNamespace = "http://InterrogaMefEsenredd.central.services.auraws.aura.csi.it", className = "it.csi.buonores.buonoresbatch.external.anagraficaservice.InterrogaMefEsenreddResponse")
    @WebResult(name = "InterrogaMefEsenreddResult", targetNamespace = "http://InterrogaMefEsenredd.central.services.auraws.aura.csi.it")
    public InterrogaMefEsenreddRes interrogaMefEsenredd(

        @WebParam(name = "CF", targetNamespace = "http://InterrogaMefEsenredd.central.services.auraws.aura.csi.it")
        java.lang.String cf
    );
}
