package org.pentaho.database.service;

import org.junit.Assert;
import org.junit.Test;
import org.pentaho.database.dialect.GenericDatabaseDialect;
import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.util.DatabaseTypeHelper;


public class DatabaseConnectionServiceTest {
  
  @Test
  public void testCreateGenericConnection() {
    DatabaseConnectionService service = new DatabaseConnectionService();
    DatabaseTypeHelper helper = new DatabaseTypeHelper(service.getDatabaseTypes());
    
    IDatabaseConnection conn = service.createDatabaseConnection(
        "org.mysql.Driver", "jdbc:mysql://localhost:1234/testdb");

    Assert.assertNotNull(conn);
    Assert.assertEquals(DatabaseAccessType.NATIVE, conn.getAccessType());
    Assert.assertEquals(helper.getDatabaseTypeByShortName("GENERIC"), conn.getDatabaseType());
    Assert.assertEquals("org.mysql.Driver", conn.getAttributes().get(GenericDatabaseDialect.ATTRIBUTE_CUSTOM_DRIVER_CLASS));
    Assert.assertEquals("jdbc:mysql://localhost:1234/testdb", conn.getAttributes().get(GenericDatabaseDialect.ATTRIBUTE_CUSTOM_URL));
  }
  
  @Test
  public void testCreateMySQLDatabaseConnection() {
    DatabaseConnectionService service = new DatabaseConnectionService();
    DatabaseTypeHelper helper = new DatabaseTypeHelper(service.getDatabaseTypes());
    
    IDatabaseConnection conn = service.createDatabaseConnection(
        "org.gjt.mm.mysql.Driver", "jdbc:mysql://localhost:1234/testdb");

    Assert.assertNotNull(conn);
    Assert.assertEquals(DatabaseAccessType.NATIVE, conn.getAccessType());
    Assert.assertEquals(helper.getDatabaseTypeByName("MySQL"), conn.getDatabaseType());
    Assert.assertEquals("localhost", conn.getHostname());
    Assert.assertEquals("1234", conn.getDatabasePort());
    Assert.assertEquals("testdb", conn.getDatabaseName());
    
    conn = service.createDatabaseConnection(
        "org.gjt.mm.mysql.Driver", "jdbc:mysql://localhost/testdb");

    Assert.assertNotNull(conn);
    Assert.assertEquals(DatabaseAccessType.NATIVE, conn.getAccessType());
    Assert.assertEquals(helper.getDatabaseTypeByName("MySQL"), conn.getDatabaseType());
    Assert.assertEquals("localhost", conn.getHostname());
    Assert.assertEquals(null, conn.getDatabasePort());
    Assert.assertEquals("testdb", conn.getDatabaseName());

    
    conn = service.createDatabaseConnection(
        "org.gjt.mm.mysql.Driver", "jdbc:mysql://testdb");

    Assert.assertNotNull(conn);
    Assert.assertEquals(DatabaseAccessType.NATIVE, conn.getAccessType());
    Assert.assertEquals(helper.getDatabaseTypeByName("MySQL"), conn.getDatabaseType());
    Assert.assertEquals(null, conn.getHostname());
    Assert.assertEquals(null, conn.getDatabasePort());
    Assert.assertEquals("testdb", conn.getDatabaseName());
    
    try {
      conn = service.createDatabaseConnection(
          "org.gjt.mm.mysql.Driver", "jasddbc:mysql://testdb");
      Assert.fail();
    } catch (RuntimeException e) {
      
    }
  }
  
  @Test
  public void testCreateOracleDatabaseConnection() {
    DatabaseConnectionService service = new DatabaseConnectionService();
    DatabaseTypeHelper helper = new DatabaseTypeHelper(service.getDatabaseTypes());
    
    IDatabaseConnection conn = service.createDatabaseConnection(
        "oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@localhost:1234:testdb");

    Assert.assertNotNull(conn);
    Assert.assertEquals(DatabaseAccessType.NATIVE, conn.getAccessType());
    Assert.assertEquals(helper.getDatabaseTypeByName("Oracle"), conn.getDatabaseType());
    Assert.assertEquals("localhost", conn.getHostname());
    Assert.assertEquals("1234", conn.getDatabasePort());
    Assert.assertEquals("testdb", conn.getDatabaseName());
    
    conn = service.createDatabaseConnection(
        "oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@localhost:testdb");

    Assert.assertNotNull(conn);
    Assert.assertEquals(DatabaseAccessType.NATIVE, conn.getAccessType());
    Assert.assertEquals(helper.getDatabaseTypeByName("Oracle"), conn.getDatabaseType());
    Assert.assertEquals("localhost", conn.getHostname());
    Assert.assertEquals("testdb", conn.getDatabasePort());
    Assert.assertEquals(null, conn.getDatabaseName());

    
    conn = service.createDatabaseConnection(
        "oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@testdb");

    Assert.assertNotNull(conn);
    Assert.assertEquals(DatabaseAccessType.NATIVE, conn.getAccessType());
    Assert.assertEquals(helper.getDatabaseTypeByName("Oracle"), conn.getDatabaseType());
    Assert.assertEquals(null, conn.getHostname());
    Assert.assertEquals(null, conn.getDatabasePort());
    Assert.assertEquals("testdb", conn.getDatabaseName());
    
    try {
      conn = service.createDatabaseConnection(
          "oracle.jdbc.driver.OracleDriver", "jdbc:oraasdfcle:thin:@testdb");
      Assert.fail();
    } catch (RuntimeException e) {
      
    }
  }

  @Test
  public void testCreateHypersonicDatabaseConnection() {
    DatabaseConnectionService service = new DatabaseConnectionService();
    DatabaseTypeHelper helper = new DatabaseTypeHelper(service.getDatabaseTypes());
    
    IDatabaseConnection conn = service.createDatabaseConnection(
        "org.hsqldb.jdbcDriver", "jdbc:hsqldb:hsql://localhost:1234/testdb");

    Assert.assertNotNull(conn);
    Assert.assertEquals(DatabaseAccessType.NATIVE, conn.getAccessType());
    Assert.assertEquals(helper.getDatabaseTypeByName("Hypersonic"), conn.getDatabaseType());
    Assert.assertEquals("localhost", conn.getHostname());
    Assert.assertEquals("1234", conn.getDatabasePort());
    Assert.assertEquals("testdb", conn.getDatabaseName());
    
    conn = service.createDatabaseConnection(
        "org.hsqldb.jdbcDriver", "jdbc:hsqldb:hsql://localhost/testdb");

    Assert.assertNotNull(conn);
    Assert.assertEquals(DatabaseAccessType.NATIVE, conn.getAccessType());
    Assert.assertEquals(helper.getDatabaseTypeByName("Hypersonic"), conn.getDatabaseType());
    Assert.assertEquals("localhost", conn.getHostname());
    Assert.assertEquals(null, conn.getDatabasePort());
    Assert.assertEquals("testdb", conn.getDatabaseName());

    
    conn = service.createDatabaseConnection(
        "org.hsqldb.jdbcDriver", "jdbc:hsqldb:hsql://testdb");

    Assert.assertNotNull(conn);
    Assert.assertEquals(DatabaseAccessType.NATIVE, conn.getAccessType());
    Assert.assertEquals(helper.getDatabaseTypeByName("Hypersonic"), conn.getDatabaseType());
    Assert.assertEquals(null, conn.getHostname());
    Assert.assertEquals(null, conn.getDatabasePort());
    Assert.assertEquals("testdb", conn.getDatabaseName());
    
    conn = service.createDatabaseConnection(
        "org.hsqldb.jdbcDriver", "jdbc:hsqldb:file:testdb");

    Assert.assertNotNull(conn);
    Assert.assertEquals(DatabaseAccessType.NATIVE, conn.getAccessType());
    Assert.assertEquals(helper.getDatabaseTypeByName("Hypersonic"), conn.getDatabaseType());
    Assert.assertEquals(null, conn.getHostname());
    Assert.assertEquals(null, conn.getDatabasePort());
    Assert.assertEquals("file:testdb", conn.getDatabaseName());
    
    try {
      conn = service.createDatabaseConnection(
          "org.hsqldb.jdbcDriver", "jdbc:hsqasdldb:testdb");
      Assert.fail();
    } catch (RuntimeException e) {
      
    }
  }
  
}
