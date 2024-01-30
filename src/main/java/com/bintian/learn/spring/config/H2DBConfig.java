package com.bintian.learn.spring.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class H2DBConfig {
    private final String dbPrefix = "tx.test.h2";

    private final String hikariDbPrefix = dbPrefix + "." + "hikari";
    private final String dbPropName = "h2Prop";

    private final String dbSessionFactory = "h2Fc";
    private final String dbDsName = "h2Ds";

    private final String dsTM = "h2TM";
    private static final String DRIVER_CLASS_NAME = "org.h2.Driver";

    private final String cfgXml = "testDbHibernate.cfg.xml";

    @Bean(name = dbPropName)
    @ConfigurationProperties(prefix = dbPrefix)
    public DataSourceProperties getDataSourceProperties() {
        DataSourceProperties properties = new DataSourceProperties();
        return properties;
    }

    @Bean(name = dbDsName)
    @ConfigurationProperties(prefix = hikariDbPrefix)
    public HikariDataSource getDataSource(@Qualifier(dbPropName) DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder()
                .driverClassName(DRIVER_CLASS_NAME)
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = dbSessionFactory)
    public SessionFactory getSessionFactory(@Qualifier(dbDsName) DataSource dataSource) {

        StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder();
        ssrb.applySetting("hibernate.connection.datasource", dataSource);
        StandardServiceRegistry ssr = ssrb.configure(cfgXml).build();
        Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();
        SessionFactory sessionFactory = meta.buildSessionFactory();
        return sessionFactory;
    }

    @Bean(name = dsTM)
    public PlatformTransactionManager getTransactionManager(@Qualifier(dbSessionFactory) SessionFactory sessionFactory) {
        HibernateTransactionManager tm = new HibernateTransactionManager();
        tm.setSessionFactory(sessionFactory);
        return tm;
    }
}
