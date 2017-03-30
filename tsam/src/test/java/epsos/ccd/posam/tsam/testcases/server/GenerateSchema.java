package epsos.ccd.posam.tsam.testcases.server;

import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

public class GenerateSchema {

    public static void main(String[] args) throws Exception {

        ApplicationContext factory = new ClassPathXmlApplicationContext("applicationContext.xml");
        Object bean = factory.getBean("&sessionFactory");
        LocalSessionFactoryBean sfb = (LocalSessionFactoryBean) bean;
        Configuration configuration = sfb.getConfiguration();
        configuration.setProperty("hibernate.dialect", MySQL5Dialect.class.getName());
        SchemaExport exp = new SchemaExport(configuration);
        exp.setOutputFile("src\\test\\resources\\schema_mysql.sql");
        exp.setFormat(true);
        exp.setDelimiter(";");
        exp.create(true, false);
    }
}
