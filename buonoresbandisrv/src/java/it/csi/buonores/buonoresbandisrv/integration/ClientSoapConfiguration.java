/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbandisrv.integration;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientSoapConfiguration {

	// Deleghe

	/*
	 * @Value("${aura.contattodigitale.user}")
	 * private String contattoDigitaleSoapUserBe;
	 * 
	 * @Value("${aura.contattodigitale.pwd}")
	 * private String contattoDigitaleSoapPassBe;
	 * 
	 * @Value("${aura.contattodigitale.url}")
	 * private String contattoDigitaleSoapUrl;
	 * 
	 * @Bean(name="contattoDigitaleService")
	 * public ContattoDigitaleSoap generateProxyContattoDigitale() {
	 * return proxyFactoryBeanContattoDigitale().create(ContattoDigitaleSoap.class);
	 * }
	 * 
	 * 
	 * 
	 * 
	 * @Bean(name="logIn")
	 * public LoggingInInterceptor loggingInInterceptor() {
	 * return new LoggingInInterceptor(new LogEventSenderInCustom());
	 * }
	 * 
	 * @Bean(name="logOut")
	 * public LoggingOutInterceptor loggingOutInterceptor() {
	 * return new LoggingOutInterceptor(new LogEventSenderOutCustom());
	 * }
	 * 
	 * 
	 * @Bean(name="proxyFactoryBeanContattoDigitale")
	 * public JaxWsProxyFactoryBean proxyFactoryBeanContattoDigitale() {
	 * JaxWsProxyFactoryBean proxyFactory = new JaxWsProxyFactoryBean();
	 * proxyFactory.setServiceClass(ContattoDigitale_Service.class);
	 * proxyFactory.setAddress(contattoDigitaleSoapUrl);
	 * //proxyFactory.setUsername(aurafindgetUserBe);
	 * //proxyFactory.setPassword(aurafindgetPassBe);
	 * 
	 * proxyFactory.getOutInterceptors().add(loggingOutInterceptor());
	 * proxyFactory.getOutInterceptors().add(
	 * createAuraContattoDigitaleSecurityInterceptor());
	 * proxyFactory.getInInterceptors().add(loggingInInterceptor());
	 * 
	 * return proxyFactory;
	 * }
	 * 
	 * @Bean
	 * public SoapInterceptor createAuraContattoDigitaleSecurityInterceptor() {
	 * Map<String, Object> outProps = new HashMap<String, Object>();
	 * outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
	 * outProps.put(WSHandlerConstants.USER, this.contattoDigitaleSoapUserBe);
	 * outProps.put(WSHandlerConstants.PASSWORD_TYPE, "PasswordText");
	 * outProps.put(WSHandlerConstants.PW_CALLBACK_REF, new CallbackHandler() {
	 * 
	 * @Override
	 * public void handle(Callback[] callbacks) throws IOException,
	 * UnsupportedCallbackException {
	 * for(Callback callBack:callbacks){
	 * if(callBack instanceof WSPasswordCallback){
	 * ((WSPasswordCallback)callBack).setPassword(contattoDigitaleSoapPassBe);
	 * }
	 * }
	 * }
	 * });
	 * return new WSS4JOutInterceptor(outProps);
	 * }
	 */

}
