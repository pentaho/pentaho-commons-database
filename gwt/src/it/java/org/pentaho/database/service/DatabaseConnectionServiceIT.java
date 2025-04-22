/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2029-07-20
 ******************************************************************************/


package org.pentaho.database.service;

import org.junit.BeforeClass;
import org.junit.Test;
import org.pentaho.database.DatabaseDialectException;
import org.pentaho.database.dialect.GenericDatabaseDialect;
import org.pentaho.database.dialect.MSSQLServerNativeDatabaseDialect;
import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.util.DatabaseTypeHelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DatabaseConnectionServiceIT {

  public static final String DRIVER_ORG_MYSQL = "org.mysql.Driver";
  public static final String DRIVER_MYSQL_JDBC = "com.mysql.cj.jdbc.Driver";
  public static final String DRIVER_MICROSOFT_SQLSERVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
  public static final String DRIVER_HSQLDB = "org.hsqldb.jdbcDriver";

  public static final String DB_TYPE_GENERIC = "Generic database";
  public static final String DB_TYPE_MYSQL = "MySQL";
  public static final String DB_TYPE_MS_SQL = "MS SQL Server (Native)";
  public static final String DB_TYPE_HYPERSONIC = "Hypersonic";

  public static final String TEST_HOST = "localhost";
  public static final String TEST_PORT = "1234";
  public static final String TEST_DB_NAME = "testdb";
  public static final String DATABASE_SHORTNAME_GENERIC = "GENERIC";

  static DatabaseDialectService dialectService;
  static DatabaseConnectionService connectionService;
  static DatabaseTypeHelper helper;

  @BeforeClass
  public static void setUp() {
    dialectService = new DatabaseDialectService( false );
    connectionService = new DatabaseConnectionService( dialectService );
    helper = new DatabaseTypeHelper( dialectService.getDatabaseTypes() );
  }

  @Test
  public void testCreateGenericDatabaseConnection() throws Exception {
    IDatabaseConnection conn = connectionService.createDatabaseConnection(
      DRIVER_ORG_MYSQL, "jdbc:mysql://localhost:1234/testdb" );

    assertNotNull( conn );
    assertEquals( DatabaseAccessType.NATIVE, conn.getAccessType() );
    assertEquals( helper.getDatabaseTypeByShortName( DATABASE_SHORTNAME_GENERIC ), conn.getDatabaseType() );
    assertEquals( DRIVER_ORG_MYSQL,
        conn.getAttributes().get( GenericDatabaseDialect.ATTRIBUTE_CUSTOM_DRIVER_CLASS ) );
    assertEquals( "jdbc:mysql://localhost:1234/testdb",
        conn.getAttributes().get( GenericDatabaseDialect.ATTRIBUTE_CUSTOM_URL ) );

    conn.addExtraOption( conn.getDatabaseType().getShortName(), "test", "true" );

    String urlString = dialectService.getDialect( conn ).getURLWithExtraOptions( conn );
    assertEquals( "jdbc:mysql://localhost:1234/testdb", urlString );
  }

  @Test
  public void testCreateMySQLDatabaseConnection_withPort() {
    IDatabaseConnection conn = connectionService.createDatabaseConnection(
      DRIVER_MYSQL_JDBC, "jdbc:mysql://localhost:1234/testdb" );

    assertNotNull( conn );
    assertEquals( DatabaseAccessType.NATIVE, conn.getAccessType() );
    assertEquals( helper.getDatabaseTypeByName( DB_TYPE_MYSQL ), conn.getDatabaseType() );
    assertEquals( TEST_HOST, conn.getHostname() );
    assertEquals( TEST_PORT, conn.getDatabasePort() );
    assertEquals( TEST_DB_NAME, conn.getDatabaseName() );
  }

  @Test
  public void testCreateMySQLDatabaseConnection_withoutPort() {
    IDatabaseConnection conn = connectionService.createDatabaseConnection(
      DRIVER_MYSQL_JDBC, "jdbc:mysql://localhost/testdb" );

    assertNotNull( conn );
    assertEquals( DatabaseAccessType.NATIVE, conn.getAccessType() );
    assertEquals( helper.getDatabaseTypeByName( DB_TYPE_MYSQL ), conn.getDatabaseType() );
    assertEquals( TEST_HOST, conn.getHostname() );
    assertNull( conn.getDatabasePort() );
    assertEquals( TEST_DB_NAME, conn.getDatabaseName() );
  }

  @Test
  public void testCreateMySQLDatabaseConnection_withExtraOptions() throws DatabaseDialectException {
    IDatabaseConnection conn = connectionService.createDatabaseConnection(
      DRIVER_MYSQL_JDBC, "jdbc:mysql://localhost:1234/testdb?autoCommit=true&test=FALSE" );

    assertNotNull( conn );
    assertEquals( DatabaseAccessType.NATIVE, conn.getAccessType() );
    assertEquals( helper.getDatabaseTypeByName( DB_TYPE_MYSQL ), conn.getDatabaseType() );
    assertEquals( TEST_HOST, conn.getHostname() );
    assertEquals( TEST_PORT, conn.getDatabasePort() );
    assertEquals( TEST_DB_NAME, conn.getDatabaseName() );
    assertEquals( 2, conn.getExtraOptions().size() );
    assertEquals( "true", conn.getExtraOptions().get( "MYSQL.autoCommit" ) );
    assertEquals( "FALSE", conn.getExtraOptions().get( "MYSQL.test" ) );

    String urlString = dialectService.getDialect( conn ).getURLWithExtraOptions( conn );
    assertTrue( urlString.startsWith( "jdbc:mysql://localhost:1234/testdb?" ) );
    assertTrue( urlString.contains( "test=FALSE" ) );
    assertTrue( urlString.contains( "autoCommit=true" ) );
  }

  @Test
  public void testCreateMSSQLNativeDatabaseConnection_withPort() {
    IDatabaseConnection conn = connectionService.createDatabaseConnection(
      DRIVER_MICROSOFT_SQLSERVER,
      "jdbc:sqlserver://localhost:1234;databaseName=testdb;integratedSecurity=false" );

    assertNotNull( conn );
    assertEquals( DatabaseAccessType.NATIVE, conn.getAccessType() );
    assertEquals( helper.getDatabaseTypeByName( DB_TYPE_MS_SQL ), conn.getDatabaseType() );
    assertEquals( TEST_HOST, conn.getHostname() );
    assertEquals( TEST_PORT, conn.getDatabasePort() );
    assertEquals( TEST_DB_NAME, conn.getDatabaseName() );
    assertEquals( "false",
      conn.getAttributes().get( MSSQLServerNativeDatabaseDialect.ATTRIBUTE_USE_INTEGRATED_SECURITY ) );
  }

  @Test
  public void testCreateMSSQLNativeDatabaseConnection_withoutPort() {
    IDatabaseConnection conn = connectionService.createDatabaseConnection(
      DRIVER_MICROSOFT_SQLSERVER, "jdbc:sqlserver://localhost;databaseName=testdb" );

    assertNotNull( conn );
    assertEquals( DatabaseAccessType.NATIVE, conn.getAccessType() );
    assertEquals( helper.getDatabaseTypeByName( DB_TYPE_MS_SQL ), conn.getDatabaseType() );
    assertEquals( TEST_HOST, conn.getHostname() );
    assertNull( conn.getDatabasePort() );
    assertEquals( TEST_DB_NAME, conn.getDatabaseName() );
  }

  @Test
  public void testCreateMSSQLNativeDatabaseConnection_noDatabaseName() {
    IDatabaseConnection conn = connectionService.createDatabaseConnection(
      DRIVER_MICROSOFT_SQLSERVER, "jdbc:sqlserver://testdb" );

    assertNotNull( conn );
    assertEquals( DatabaseAccessType.NATIVE, conn.getAccessType() );
    assertEquals( helper.getDatabaseTypeByName( DB_TYPE_MS_SQL ), conn.getDatabaseType() );
    assertEquals( TEST_DB_NAME, conn.getHostname() );
    assertNull( conn.getDatabasePort() );
    assertNull( conn.getDatabaseName() );
  }

  @Test
  public void testCreateMSSQLNativeDatabaseConnection_genericDatabase() {
    IDatabaseConnection conn = connectionService.createDatabaseConnection(
      DRIVER_MICROSOFT_SQLSERVER, "jasddbc:mysql://testdb" );

    assertEquals( DatabaseAccessType.NATIVE, conn.getAccessType() );
    assertEquals( helper.getDatabaseTypeByName( DB_TYPE_GENERIC ), conn.getDatabaseType() );
    assertNull( conn.getHostname() );
    assertNull( conn.getDatabasePort() );
    assertNull( conn.getDatabaseName() );
    assertNull( conn.getDatabaseName() );
    assertEquals( "jasddbc:mysql://testdb",
      conn.getAttributes().get( GenericDatabaseDialect.ATTRIBUTE_CUSTOM_URL ) );
    assertEquals( DRIVER_MICROSOFT_SQLSERVER,
      conn.getAttributes().get( GenericDatabaseDialect.ATTRIBUTE_CUSTOM_DRIVER_CLASS ) );
  }

  @Test
  public void testCreateMSSQLNativeDatabaseConnection_withExtraOptions() throws DatabaseDialectException {
    IDatabaseConnection conn = connectionService.createDatabaseConnection(
      DRIVER_MICROSOFT_SQLSERVER,
      "jdbc:sqlserver://localhost:1234;databaseName=testdb;autoCommit=true;test=FALSE" );

    assertNotNull( conn );
    assertEquals( DatabaseAccessType.NATIVE, conn.getAccessType() );
    assertEquals( helper.getDatabaseTypeByName( DB_TYPE_MS_SQL ), conn.getDatabaseType() );
    assertEquals( TEST_HOST, conn.getHostname() );
    assertEquals( TEST_PORT, conn.getDatabasePort() );
    assertEquals( TEST_DB_NAME, conn.getDatabaseName() );
    assertEquals( 2, conn.getExtraOptions().size() );
    assertEquals( "true", conn.getExtraOptions().get( "MSSQLNative.autoCommit" ) );
    assertEquals( "FALSE", conn.getExtraOptions().get( "MSSQLNative.test" ) );

    String urlString = dialectService.getDialect( conn ).getURLWithExtraOptions( conn );
    assertTrue( urlString.startsWith( "jdbc:sqlserver://localhost:1234;databaseName=testdb;integratedSecurity=false;" ) );
    assertTrue( urlString.contains( "test=FALSE" ) );
    assertTrue( urlString.contains( "autoCommit=true" ) );
  }

  @Test
  public void testCreateHypersonicDatabaseConnection_withPort() {
    IDatabaseConnection conn = connectionService.createDatabaseConnection(
      DRIVER_HSQLDB, "jdbc:hsqldb:hsql://localhost:1234/testdb" );

    assertNotNull( conn );
    assertEquals( DatabaseAccessType.NATIVE, conn.getAccessType() );
    assertEquals( helper.getDatabaseTypeByName( DB_TYPE_HYPERSONIC ), conn.getDatabaseType() );
    assertEquals( TEST_HOST, conn.getHostname() );
    assertEquals( TEST_PORT, conn.getDatabasePort() );
    assertEquals( TEST_DB_NAME, conn.getDatabaseName() );
  }

  @Test
  public void testCreateHypersonicDatabaseConnection_withoutPort() {
    IDatabaseConnection conn = connectionService.createDatabaseConnection(
      DRIVER_HSQLDB, "jdbc:hsqldb:hsql://localhost/testdb" );

    assertNotNull( conn );
    assertEquals( DatabaseAccessType.NATIVE, conn.getAccessType() );
    assertEquals( helper.getDatabaseTypeByName( DB_TYPE_HYPERSONIC ), conn.getDatabaseType() );
    assertEquals( TEST_HOST, conn.getHostname() );
    assertNull( conn.getDatabasePort() );
    assertEquals( TEST_DB_NAME, conn.getDatabaseName() );
  }

  @Test
  public void testCreateHypersonicDatabaseConnection_withoutHost() {
    IDatabaseConnection conn = connectionService.createDatabaseConnection(
      DRIVER_HSQLDB, "jdbc:hsqldb:hsql://testdb" );

    assertNotNull( conn );
    assertEquals( DatabaseAccessType.NATIVE, conn.getAccessType() );
    assertEquals( helper.getDatabaseTypeByName( DB_TYPE_HYPERSONIC ), conn.getDatabaseType() );
    assertNull( conn.getHostname() );
    assertNull( conn.getDatabasePort() );
    assertEquals( TEST_DB_NAME, conn.getDatabaseName() );
  }

  @Test
  public void testCreateHypersonicDatabaseConnection_withFile() {
    IDatabaseConnection conn = connectionService.createDatabaseConnection(
      DRIVER_HSQLDB, "jdbc:hsqldb:file:testdb" );

    assertNotNull( conn );
    assertEquals( DatabaseAccessType.NATIVE, conn.getAccessType() );
    assertEquals( helper.getDatabaseTypeByName( DB_TYPE_HYPERSONIC ), conn.getDatabaseType() );
    assertNull( conn.getHostname() );
    assertNull( conn.getDatabasePort() );
    assertEquals( "file:testdb", conn.getDatabaseName() );
  }

  @Test
  public void testCreateHypersonicDatabaseConnection_genericDatabase() {
    IDatabaseConnection conn = connectionService.createDatabaseConnection(
      DRIVER_HSQLDB, "jdbc:hsqasdldb:testdb" );

    assertEquals( DatabaseAccessType.NATIVE, conn.getAccessType() );
    assertEquals( helper.getDatabaseTypeByName( DB_TYPE_GENERIC ), conn.getDatabaseType() );
    assertNull( conn.getHostname() );
    assertNull( conn.getDatabasePort() );
    assertNull( conn.getDatabaseName() );
    assertNull( conn.getDatabaseName() );
    assertEquals( "jdbc:hsqasdldb:testdb",
        conn.getAttributes().get( GenericDatabaseDialect.ATTRIBUTE_CUSTOM_URL ) );
    assertEquals( DRIVER_HSQLDB,
        conn.getAttributes().get( GenericDatabaseDialect.ATTRIBUTE_CUSTOM_DRIVER_CLASS ) );
  }

  @Test
  public void testCreateHypersonicDatabaseConnection_withExtraOptions() throws DatabaseDialectException {
    IDatabaseConnection conn = connectionService.createDatabaseConnection(
      DRIVER_HSQLDB, "jdbc:hsqldb:hsql://testdb;ifexists=true;test=false;" );

    assertNotNull( conn );
    assertEquals( DatabaseAccessType.NATIVE, conn.getAccessType() );
    assertEquals( helper.getDatabaseTypeByName( DB_TYPE_HYPERSONIC ), conn.getDatabaseType() );
    assertNull( conn.getHostname() );
    assertNull( conn.getDatabasePort() );
    assertEquals( TEST_DB_NAME, conn.getDatabaseName() );
    assertEquals( 2, conn.getExtraOptions().size() );
    assertEquals( "true", conn.getExtraOptions().get( "HYPERSONIC.ifexists" ) );
    assertEquals( "false", conn.getExtraOptions().get( "HYPERSONIC.test" ) );

    String urlString = dialectService.getDialect( conn ).getURLWithExtraOptions( conn );
    assertTrue( urlString.startsWith( "jdbc:hsqldb:testdb;" ) );
    assertTrue( urlString.contains( ";ifexists=true" ) );
    assertTrue( urlString.contains( ";test=false" ) );
  }

  @Test
  public void testCreateHypersonicDatabaseConnection_noExtraOptions() {
    IDatabaseConnection conn = connectionService.createDatabaseConnection(
      DRIVER_HSQLDB, "jdbc:hsqldb:hsql://testdb;" );

    assertNotNull( conn );
    assertEquals( DatabaseAccessType.NATIVE, conn.getAccessType() );
    assertEquals( helper.getDatabaseTypeByName( DB_TYPE_HYPERSONIC ), conn.getDatabaseType() );
    assertNull( conn.getHostname() );
    assertNull( conn.getDatabasePort() );
    assertEquals( TEST_DB_NAME, conn.getDatabaseName() );
    assertTrue( conn.getExtraOptions().isEmpty() );
  }

}
