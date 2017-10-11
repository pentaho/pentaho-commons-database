/*!
* This program is free software; you can redistribute it and/or modify it under the
* terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
* Foundation.
*
* You should have received a copy of the GNU Lesser General Public License along with this
* program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
* or from the Free Software Foundation, Inc.,
* 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
* without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
* See the GNU Lesser General Public License for more details.
*
* Copyright (c) 2002-2017 Hitachi Vantara..  All rights reserved.
*/

package org.pentaho.database.service;

import org.junit.Assert;
import org.junit.Test;
import org.pentaho.database.dialect.GenericDatabaseDialect;
import org.pentaho.database.dialect.MSSQLServerNativeDatabaseDialect;
import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.util.DatabaseTypeHelper;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.database.DatabaseMeta;

@SuppressWarnings( "nls" )
public class DatabaseConnectionServiceIT {

  @Test
  public void testMSSQL() throws Exception {
    KettleEnvironment.init( false );
    DatabaseMeta dbmeta = new DatabaseMeta();
    dbmeta.setDatabaseType( "MSSQL" );
    dbmeta.setHostname( "localhost" );
    dbmeta.setDBName( "test" );
    System.out.println( dbmeta.getURL() );
    dbmeta.setServername( "test" ); //(true);
  }

  @Test
  public void testCreateGenericConnection() throws Exception {
    DatabaseDialectService dialectService = new DatabaseDialectService();
    DatabaseConnectionService connectionService = new DatabaseConnectionService();
    DatabaseTypeHelper helper = new DatabaseTypeHelper( dialectService.getDatabaseTypes() );

    IDatabaseConnection conn = connectionService.createDatabaseConnection(
        "org.mysql.Driver", "jdbc:mysql://localhost:1234/testdb" );

    Assert.assertNotNull( conn );
    Assert.assertEquals( DatabaseAccessType.NATIVE, conn.getAccessType() );
    Assert.assertEquals( helper.getDatabaseTypeByShortName( "GENERIC" ), conn.getDatabaseType() );
    Assert.assertEquals( "org.mysql.Driver",
        conn.getAttributes().get( GenericDatabaseDialect.ATTRIBUTE_CUSTOM_DRIVER_CLASS ) );
    Assert.assertEquals( "jdbc:mysql://localhost:1234/testdb",
        conn.getAttributes().get( GenericDatabaseDialect.ATTRIBUTE_CUSTOM_URL ) );

    conn.addExtraOption( conn.getDatabaseType().getShortName(), "test", "true" );

    String urlString = dialectService.getDialect( conn ).getURLWithExtraOptions( conn );
    Assert.assertEquals( "jdbc:mysql://localhost:1234/testdb", urlString );
  }

  @Test
  public void testCreateMySQLDatabaseConnection() throws Exception {
    DatabaseDialectService dialectService = new DatabaseDialectService();
    DatabaseConnectionService connectionService = new DatabaseConnectionService();
    DatabaseTypeHelper helper = new DatabaseTypeHelper( dialectService.getDatabaseTypes() );

    IDatabaseConnection conn = connectionService.createDatabaseConnection(
        "org.gjt.mm.mysql.Driver", "jdbc:mysql://localhost:1234/testdb" );

    Assert.assertNotNull( conn );
    Assert.assertEquals( DatabaseAccessType.NATIVE, conn.getAccessType() );
    Assert.assertEquals( helper.getDatabaseTypeByName( "MySQL" ), conn.getDatabaseType() );
    Assert.assertEquals( "localhost", conn.getHostname() );
    Assert.assertEquals( "1234", conn.getDatabasePort() );
    Assert.assertEquals( "testdb", conn.getDatabaseName() );

    conn = connectionService.createDatabaseConnection(
        "org.gjt.mm.mysql.Driver", "jdbc:mysql://localhost/testdb" );

    Assert.assertNotNull( conn );
    Assert.assertEquals( DatabaseAccessType.NATIVE, conn.getAccessType() );
    Assert.assertEquals( helper.getDatabaseTypeByName( "MySQL" ), conn.getDatabaseType() );
    Assert.assertEquals( "localhost", conn.getHostname() );
    Assert.assertEquals( null, conn.getDatabasePort() );
    Assert.assertEquals( "testdb", conn.getDatabaseName() );

    conn = connectionService.createDatabaseConnection(
        "org.gjt.mm.mysql.Driver", "jdbc:mysql://testdb" );

    Assert.assertNotNull( conn );
    Assert.assertEquals( DatabaseAccessType.NATIVE, conn.getAccessType() );
    Assert.assertEquals( helper.getDatabaseTypeByName( "MySQL" ), conn.getDatabaseType() );
    Assert.assertEquals( null, conn.getHostname() );
    Assert.assertEquals( null, conn.getDatabasePort() );
    Assert.assertEquals( "testdb", conn.getDatabaseName() );

    conn = connectionService.createDatabaseConnection(
        "org.gjt.mm.mysql.Driver", "jasddbc:mysql://testdb" );
    Assert.assertEquals( DatabaseAccessType.NATIVE, conn.getAccessType() );
    Assert.assertEquals( helper.getDatabaseTypeByName( "Generic database" ), conn.getDatabaseType() );
    Assert.assertEquals( null, conn.getHostname() );
    Assert.assertEquals( null, conn.getDatabasePort() );
    Assert.assertEquals( null, conn.getDatabaseName() );
    Assert.assertEquals( null, conn.getDatabaseName() );
    Assert.assertEquals( "jasddbc:mysql://testdb",
        conn.getAttributes().get( GenericDatabaseDialect.ATTRIBUTE_CUSTOM_URL ) );
    Assert.assertEquals( "org.gjt.mm.mysql.Driver",
        conn.getAttributes().get( GenericDatabaseDialect.ATTRIBUTE_CUSTOM_DRIVER_CLASS ) );

    conn = connectionService.createDatabaseConnection(
        "org.gjt.mm.mysql.Driver", "jdbc:mysql://localhost:1234/testdb?autoCommit=true&test=FALSE" );

    Assert.assertNotNull( conn );
    Assert.assertEquals( DatabaseAccessType.NATIVE, conn.getAccessType() );
    Assert.assertEquals( helper.getDatabaseTypeByName( "MySQL" ), conn.getDatabaseType() );
    Assert.assertEquals( "localhost", conn.getHostname() );
    Assert.assertEquals( "1234", conn.getDatabasePort() );
    Assert.assertEquals( "testdb", conn.getDatabaseName() );
    Assert.assertEquals( 2, conn.getExtraOptions().size() );
    Assert.assertEquals( "true", conn.getExtraOptions().get( "MYSQL.autoCommit" ) );
    Assert.assertEquals( "FALSE", conn.getExtraOptions().get( "MYSQL.test" ) );

    String urlString = dialectService.getDialect( conn ).getURLWithExtraOptions( conn );
    Assert.assertEquals( "jdbc:mysql://localhost:1234/testdb?test=FALSE&autoCommit=true", urlString );

  }

  @Test
  public void testCreateMSSQLNativeDatabaseConnection() throws Exception {
    DatabaseDialectService dialectService = new DatabaseDialectService( false );
    DatabaseConnectionService connectionService = new DatabaseConnectionService( dialectService );
    DatabaseTypeHelper helper = new DatabaseTypeHelper( dialectService.getDatabaseTypes() );

    IDatabaseConnection conn = connectionService.createDatabaseConnection(
        "com.microsoft.sqlserver.jdbc.SQLServerDriver",
        "jdbc:sqlserver://localhost:1234;databaseName=testdb;integratedSecurity=false" );

    Assert.assertNotNull( conn );
    Assert.assertEquals( DatabaseAccessType.NATIVE, conn.getAccessType() );
    Assert.assertEquals( helper.getDatabaseTypeByName( "MS SQL Server (Native)" ), conn.getDatabaseType() );
    Assert.assertEquals( "localhost", conn.getHostname() );
    Assert.assertEquals( "1234", conn.getDatabasePort() );
    Assert.assertEquals( "testdb", conn.getDatabaseName() );
    Assert.assertEquals( "false",
        conn.getAttributes().get( MSSQLServerNativeDatabaseDialect.ATTRIBUTE_USE_INTEGRATED_SECURITY ) );

    conn = connectionService.createDatabaseConnection(
        "com.microsoft.sqlserver.jdbc.SQLServerDriver", "jdbc:sqlserver://localhost;databaseName=testdb" );

    Assert.assertNotNull( conn );
    Assert.assertEquals( DatabaseAccessType.NATIVE, conn.getAccessType() );
    Assert.assertEquals( helper.getDatabaseTypeByName( "MS SQL Server (Native)" ), conn.getDatabaseType() );
    Assert.assertEquals( "localhost", conn.getHostname() );
    Assert.assertEquals( null, conn.getDatabasePort() );
    Assert.assertEquals( "testdb", conn.getDatabaseName() );

    conn = connectionService.createDatabaseConnection(
        "com.microsoft.sqlserver.jdbc.SQLServerDriver", "jdbc:sqlserver://testdb" );

    Assert.assertNotNull( conn );
    Assert.assertEquals( DatabaseAccessType.NATIVE, conn.getAccessType() );
    Assert.assertEquals( helper.getDatabaseTypeByName( "MS SQL Server (Native)" ), conn.getDatabaseType() );
    Assert.assertEquals( "testdb", conn.getHostname() );
    Assert.assertEquals( null, conn.getDatabasePort() );
    Assert.assertEquals( null, conn.getDatabaseName() );

    conn = connectionService.createDatabaseConnection(
        "com.microsoft.sqlserver.jdbc.SQLServerDriver", "jasddbc:mysql://testdb" );
    Assert.assertEquals( DatabaseAccessType.NATIVE, conn.getAccessType() );
    Assert.assertEquals( helper.getDatabaseTypeByName( "Generic database" ), conn.getDatabaseType() );
    Assert.assertEquals( null, conn.getHostname() );
    Assert.assertEquals( null, conn.getDatabasePort() );
    Assert.assertEquals( null, conn.getDatabaseName() );
    Assert.assertEquals( null, conn.getDatabaseName() );
    Assert.assertEquals( "jasddbc:mysql://testdb",
        conn.getAttributes().get( GenericDatabaseDialect.ATTRIBUTE_CUSTOM_URL ) );
    Assert.assertEquals( "com.microsoft.sqlserver.jdbc.SQLServerDriver",
        conn.getAttributes().get( GenericDatabaseDialect.ATTRIBUTE_CUSTOM_DRIVER_CLASS ) );

    conn = connectionService.createDatabaseConnection(
        "com.microsoft.sqlserver.jdbc.SQLServerDriver",
        "jdbc:sqlserver://localhost:1234;databaseName=testdb;autoCommit=true;test=FALSE" );

    Assert.assertNotNull( conn );
    Assert.assertEquals( DatabaseAccessType.NATIVE, conn.getAccessType() );
    Assert.assertEquals( helper.getDatabaseTypeByName( "MS SQL Server (Native)" ), conn.getDatabaseType() );
    Assert.assertEquals( "localhost", conn.getHostname() );
    Assert.assertEquals( "1234", conn.getDatabasePort() );
    Assert.assertEquals( "testdb", conn.getDatabaseName() );
    Assert.assertEquals( 2, conn.getExtraOptions().size() );
    Assert.assertEquals( "true", conn.getExtraOptions().get( "MSSQLNative.autoCommit" ) );
    Assert.assertEquals( "FALSE", conn.getExtraOptions().get( "MSSQLNative.test" ) );

    String urlString = dialectService.getDialect( conn ).getURLWithExtraOptions( conn );
    Assert.assertEquals(
        "jdbc:sqlserver://localhost:1234;databaseName=testdb;integratedSecurity=false;test=FALSE;autoCommit=true",
        urlString );

  }

  @Test
  public void testCreateHypersonicDatabaseConnection() throws Exception {
    DatabaseDialectService dialectService = new DatabaseDialectService();
    DatabaseConnectionService connectionService = new DatabaseConnectionService();
    DatabaseTypeHelper helper = new DatabaseTypeHelper( dialectService.getDatabaseTypes() );

    IDatabaseConnection conn = connectionService.createDatabaseConnection(
        "org.hsqldb.jdbcDriver", "jdbc:hsqldb:hsql://localhost:1234/testdb" );

    Assert.assertNotNull( conn );
    Assert.assertEquals( DatabaseAccessType.NATIVE, conn.getAccessType() );
    Assert.assertEquals( helper.getDatabaseTypeByName( "Hypersonic" ), conn.getDatabaseType() );
    Assert.assertEquals( "localhost", conn.getHostname() );
    Assert.assertEquals( "1234", conn.getDatabasePort() );
    Assert.assertEquals( "testdb", conn.getDatabaseName() );

    conn = connectionService.createDatabaseConnection(
        "org.hsqldb.jdbcDriver", "jdbc:hsqldb:hsql://localhost/testdb" );

    Assert.assertNotNull( conn );
    Assert.assertEquals( DatabaseAccessType.NATIVE, conn.getAccessType() );
    Assert.assertEquals( helper.getDatabaseTypeByName( "Hypersonic" ), conn.getDatabaseType() );
    Assert.assertEquals( "localhost", conn.getHostname() );
    Assert.assertEquals( null, conn.getDatabasePort() );
    Assert.assertEquals( "testdb", conn.getDatabaseName() );

    conn = connectionService.createDatabaseConnection(
        "org.hsqldb.jdbcDriver", "jdbc:hsqldb:hsql://testdb" );

    Assert.assertNotNull( conn );
    Assert.assertEquals( DatabaseAccessType.NATIVE, conn.getAccessType() );
    Assert.assertEquals( helper.getDatabaseTypeByName( "Hypersonic" ), conn.getDatabaseType() );
    Assert.assertEquals( null, conn.getHostname() );
    Assert.assertEquals( null, conn.getDatabasePort() );
    Assert.assertEquals( "testdb", conn.getDatabaseName() );

    conn = connectionService.createDatabaseConnection(
        "org.hsqldb.jdbcDriver", "jdbc:hsqldb:file:testdb" );

    Assert.assertNotNull( conn );
    Assert.assertEquals( DatabaseAccessType.NATIVE, conn.getAccessType() );
    Assert.assertEquals( helper.getDatabaseTypeByName( "Hypersonic" ), conn.getDatabaseType() );
    Assert.assertEquals( null, conn.getHostname() );
    Assert.assertEquals( null, conn.getDatabasePort() );
    Assert.assertEquals( "file:testdb", conn.getDatabaseName() );

    conn = connectionService.createDatabaseConnection(
        "org.hsqldb.jdbcDriver", "jdbc:hsqasdldb:testdb" );
    Assert.assertEquals( DatabaseAccessType.NATIVE, conn.getAccessType() );
    Assert.assertEquals( helper.getDatabaseTypeByName( "Generic database" ), conn.getDatabaseType() );
    Assert.assertEquals( null, conn.getHostname() );
    Assert.assertEquals( null, conn.getDatabasePort() );
    Assert.assertEquals( null, conn.getDatabaseName() );
    Assert.assertEquals( null, conn.getDatabaseName() );
    Assert.assertEquals( "jdbc:hsqasdldb:testdb",
        conn.getAttributes().get( GenericDatabaseDialect.ATTRIBUTE_CUSTOM_URL ) );
    Assert.assertEquals( "org.hsqldb.jdbcDriver",
        conn.getAttributes().get( GenericDatabaseDialect.ATTRIBUTE_CUSTOM_DRIVER_CLASS ) );

    // test URL parameters
    conn = connectionService.createDatabaseConnection(
        "org.hsqldb.jdbcDriver", "jdbc:hsqldb:hsql://testdb;ifexists=true;test=false" );

    Assert.assertNotNull( conn );
    Assert.assertEquals( DatabaseAccessType.NATIVE, conn.getAccessType() );
    Assert.assertEquals( helper.getDatabaseTypeByName( "Hypersonic" ), conn.getDatabaseType() );
    Assert.assertEquals( null, conn.getHostname() );
    Assert.assertEquals( null, conn.getDatabasePort() );
    Assert.assertEquals( "testdb", conn.getDatabaseName() );
    Assert.assertEquals( 2, conn.getExtraOptions().size() );
    Assert.assertEquals( "true", conn.getExtraOptions().get( "HYPERSONIC.ifexists" ) );
    Assert.assertEquals( "false", conn.getExtraOptions().get( "HYPERSONIC.test" ) );

    conn = connectionService.createDatabaseConnection(
        "org.hsqldb.jdbcDriver", "jdbc:hsqldb:hsql://testdb;ifexists=true;test=false;" );

    Assert.assertNotNull( conn );
    Assert.assertEquals( DatabaseAccessType.NATIVE, conn.getAccessType() );
    Assert.assertEquals( helper.getDatabaseTypeByName( "Hypersonic" ), conn.getDatabaseType() );
    Assert.assertEquals( null, conn.getHostname() );
    Assert.assertEquals( null, conn.getDatabasePort() );
    Assert.assertEquals( "testdb", conn.getDatabaseName() );
    Assert.assertEquals( 2, conn.getExtraOptions().size() );
    Assert.assertEquals( "true", conn.getExtraOptions().get( "HYPERSONIC.ifexists" ) );
    Assert.assertEquals( "false", conn.getExtraOptions().get( "HYPERSONIC.test" ) );

    String urlString = dialectService.getDialect( conn ).getURLWithExtraOptions( conn );
    Assert.assertEquals( "jdbc:hsqldb:testdb;ifexists=true;test=false", urlString );

    conn = connectionService.createDatabaseConnection(
        "org.hsqldb.jdbcDriver", "jdbc:hsqldb:hsql://testdb;" );

    Assert.assertNotNull( conn );
    Assert.assertEquals( DatabaseAccessType.NATIVE, conn.getAccessType() );
    Assert.assertEquals( helper.getDatabaseTypeByName( "Hypersonic" ), conn.getDatabaseType() );
    Assert.assertEquals( null, conn.getHostname() );
    Assert.assertEquals( null, conn.getDatabasePort() );
    Assert.assertEquals( "testdb", conn.getDatabaseName() );
    Assert.assertEquals( 0, conn.getExtraOptions().size() );

  }

}
