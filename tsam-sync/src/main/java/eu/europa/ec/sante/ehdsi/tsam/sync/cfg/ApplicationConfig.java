package eu.europa.ec.sante.ehdsi.tsam.sync.cfg;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import eu.europa.ec.sante.ehdsi.tsam.sync.client.SimpleTermServerClient;
import eu.europa.ec.sante.ehdsi.tsam.sync.client.TermServerClient;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.beans.PropertyVetoException;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class ApplicationConfig {

    @Autowired
    private Environment env;

    @Bean(destroyMethod = "close")
    public ComboPooledDataSource dataSource() {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setJdbcUrl(env.getProperty("datasource.url"));
        dataSource.setUser(env.getProperty("datasource.username"));
        dataSource.setPassword(env.getProperty("datasource.password"));
        try {
            dataSource.setDriverClass(env.getProperty("datasource.driver-class-name"));
        } catch (PropertyVetoException e) {
            throw new IllegalArgumentException("Invalid driver class", e);
        }
        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("eu.europa.ec.sante.ehdsi.tsam.sync.db");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put(org.hibernate.cfg.Environment.DIALECT, env.getProperty("hibernate.dialect"));
        properties.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO, env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put(org.hibernate.cfg.Environment.SHOW_SQL, env.getProperty("hibernate.format_sql"));
        properties.put(org.hibernate.cfg.Environment.FORMAT_SQL, env.getProperty("hibernate.show_sql"));
        return properties;
    }

    @Bean
    public PlatformTransactionManager transactionManager(SessionFactory sessionFactory) {
        return new HibernateTransactionManager(sessionFactory);
    }

    @Bean
    public TermServerClient termServerSyncClient() {
        return new SimpleTermServerClient();
    }
}
