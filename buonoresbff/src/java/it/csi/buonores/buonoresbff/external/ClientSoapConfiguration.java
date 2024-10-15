/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.external;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.cxf.binding.soap.interceptor.SoapInterceptor;
import org.apache.cxf.ext.logging.LoggingInInterceptor;
import org.apache.cxf.ext.logging.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import it.csi.buonores.buonoresbff.external.anagraficaservice.InterrogaMefEsenredd1Soap;
import it.csi.buonores.buonoresbff.util.BuonoResBffProperties;
import it.csi.buonores.buonoresbff.util.log.LogEventSenderInCustom;
import it.csi.buonores.buonoresbff.util.log.LogEventSenderOutCustom;

@Configuration
public class ClientSoapConfiguration {

	// Interroga Mef

	@Autowired
	private BuonoResBffProperties properties;

	@Bean(name = "interrogaMefWsBean")
	public InterrogaMefEsenredd1Soap generateProxyInterrogaMef() {
		return proxyFactoryBeanInterrogaMef().create(InterrogaMefEsenredd1Soap.class);
	}

	@Bean(name = "logIn")
	public LoggingInInterceptor loggingInInterceptor() {
		return new LoggingInInterceptor(new LogEventSenderInCustom());
	}

	@Bean(name = "logOut")
	public LoggingOutInterceptor loggingOutInterceptor() {
		return new LoggingOutInterceptor(new LogEventSenderOutCustom());
	}

	@Bean(name = "proxyFactoryBeanIntterogaMef")
	public JaxWsProxyFactoryBean proxyFactoryBeanInterrogaMef() {
		JaxWsProxyFactoryBean proxyFactory = new JaxWsProxyFactoryBean();
		proxyFactory.setServiceClass(InterrogaMefEsenredd1Soap.class);
		proxyFactory.setAddress(properties.getInterrogaMefServiceUrl());

		proxyFactory.getOutInterceptors().add(loggingOutInterceptor());
		proxyFactory.getOutInterceptors().add(createInterrogaMefSecurityInterceptor());
		proxyFactory.getInInterceptors().add(loggingInInterceptor());
		proxyFactory.getInFaultInterceptors().add(loggingInInterceptor());
		proxyFactory.getOutFaultInterceptors().add(loggingOutInterceptor());

		return proxyFactory;
	}

	@Bean
	public SoapInterceptor createInterrogaMefSecurityInterceptor() {
		Map<String, Object> outProps = new HashMap<String, Object>();
		outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
		outProps.put(WSHandlerConstants.USER, properties.getInterrogaMefUserBe());
		outProps.put(WSHandlerConstants.PASSWORD_TYPE, "PasswordText");
		outProps.put(WSHandlerConstants.PW_CALLBACK_REF, new CallbackHandler() {

			@Override
			public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
				for (Callback callBack : callbacks) {
					if (callBack instanceof WSPasswordCallback) {
						((WSPasswordCallback) callBack).setPassword(properties.getInterrogaMefPassBe());
					}
				}
			}
		});
		return new WSS4JOutInterceptor(outProps);
	}

}
