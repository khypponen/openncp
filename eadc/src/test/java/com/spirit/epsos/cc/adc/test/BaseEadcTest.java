package com.spirit.epsos.cc.adc.test;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public abstract class BaseEadcTest {
    protected static final String DS_NAME = "jdbc/TestDS";
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
        System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");    
        System.setProperty("EPSOS_PROPS_PATH", ".");

        InitialContext ic = new InitialContext();

        ic.createSubcontext("java:");
        ic.createSubcontext("java:/comp");
        ic.createSubcontext("java:/comp/env");
        ic.createSubcontext("java:/comp/env/jdbc");
  
        JdbcConnectionPool ds = JdbcConnectionPool.create(
                "jdbc:h2:file:target/temp-database/h2db;FILE_LOCK=NO;MVCC=TRUE;DB_CLOSE_ON_EXIT=TRUE", "sa", "");
        
        ic.bind("java:/comp/env/jdbc/TestDS", ds);
    }

    @AfterClass
    public static void destroyClass() throws Exception {
        InitialContext ic = new InitialContext();

        ic.unbind("java:/comp/env/jdbc/TestDS");
        
        ic.destroySubcontext("java:/comp/env/jdbc");
        ic.destroySubcontext("java:/comp/env");
        ic.destroySubcontext("java:/comp");
        ic.destroySubcontext("java:");
    }        
}
