/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.business;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@ComponentScan(basePackages = "it.csi.buonores.buonoresbff")
@PropertySource("classpath:application.properties")
public class SpringApplicationContextHelper implements ApplicationContextAware {

	private static ApplicationContext appContext;

	private static Map<String, Object> beanCache = new HashMap<>();

	// Private constructor prevents instantiation from other classes
	private SpringApplicationContextHelper() {
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		appContext = applicationContext;
	}

	public static Object getBean(String beanName, boolean cacheable) {

		if (cacheable && beanCache.containsKey(beanName)) {
			return beanCache.get(beanName);
		}

		Object bean = null;

		if (appContext.containsBean(beanName)) {
			bean = appContext.getBean(beanName);
		} else {
			bean = appContext.getBean(beanName.substring(0, 1).toLowerCase() + beanName.substring(1));
		}

		if (cacheable) {
			beanCache.put(beanName, bean);
		}

		return bean;
	}

	public static Object getBean(String beanName) {
		return getBean(beanName, true);
	}

}
