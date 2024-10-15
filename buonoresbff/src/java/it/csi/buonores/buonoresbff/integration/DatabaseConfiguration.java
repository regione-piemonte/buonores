/*******************************************************************************

* Copyright Regione Piemonte - 2024

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonores.buonoresbff.integration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import it.csi.buonores.buonoresbff.util.BuonoResBffProperties;

@Configuration
@EnableTransactionManagement
public class DatabaseConfiguration {

	@Autowired
	private BuonoResBffProperties properties;

	@Primary
	@Bean(name = "buonoresDataSource")
	public JndiObjectFactoryBean dataSource() {
		var jndiObjectFactoryBean = new JndiObjectFactoryBean();
		jndiObjectFactoryBean.setJndiName(properties.getJndiName());
		jndiObjectFactoryBean.setResourceRef(true);
		jndiObjectFactoryBean.setProxyInterface(DataSource.class);
		return jndiObjectFactoryBean;
	}

	@Bean(name = "namedParameterJdbcTemplate")
	@DependsOn("buonoresDataSource")
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate(
			@Qualifier("buonoresDataSource") DataSource abcDataSource) {
		return new NamedParameterJdbcTemplate(abcDataSource);
	}

	@Bean
	public PlatformTransactionManager dbTransactionManager(
			@Qualifier("buonoresDataSource") DataSource buonoresDataSource) {
		return new DataSourceTransactionManager(buonoresDataSource);
	}

}
