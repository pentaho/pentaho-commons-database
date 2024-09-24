/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2028-08-13
 ******************************************************************************/

package org.pentaho.database.dialect;

import junit.framework.Assert;
import org.junit.Test;
import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.DatabaseConnection;
import org.pentaho.database.model.IDatabaseType;

public class InformixDatabaseDialectTest {

  private InformixDatabaseDialect dialect;

  public InformixDatabaseDialectTest() {
    this.dialect = new InformixDatabaseDialect();
  }

  @Test
  public void testGetNativeDriver() {
    Assert.assertEquals( dialect.getNativeDriver(), "com.informix.jdbc.IfxDriver" );
  }

  @Test
  public void testGetURL() throws Exception {
    DatabaseConnection conn = new DatabaseConnection();
    conn.setAccessType( DatabaseAccessType.NATIVE );
    Assert
        .assertEquals( dialect.getURL( conn ), "jdbc:informix-sqli://null:null/null:INFORMIXSERVER=null;DELIMIDENT=Y" );
  }

  @Test
  public void testGetUsedLibraries() {
    Assert.assertEquals( dialect.getUsedLibraries()[0], "ifxjdbc.jar" );
  }

  @Test
  public void testGetNativeJdbcPre() {
    Assert.assertEquals( dialect.getNativeJdbcPre(), "jdbc:informix-sqli:" );
  }

  @Test
  public void testGetDatabaseType() {
    IDatabaseType dbType = dialect.getDatabaseType();
    Assert.assertEquals( dbType.getName(), "Informix" );
  }

  @Test
  public void testNeedsToLockAllTables() {
    Assert.assertFalse( dialect.needsToLockAllTables() );
  }

  @Test
  public void testNeedsPlaceHolder() {
    Assert.assertTrue( dialect.needsPlaceHolder() );
  }

  @Test
  public void testGetSQLTableExists() {
    String tableName = "table";
    Assert.assertEquals( dialect.getSQLTableExists( tableName ), "SELECT FIRST 1 * FROM " + tableName );
  }

  @Test
  public void testGetSQLUnlockTables() {
    Assert.assertNull( dialect.getSQLUnlockTables( null ) );
  }

  @Test
  public void testGetSQLColumnExists() {
    String columnName = "column1";
    String tableName = "table1";
    Assert.assertEquals( dialect.getSQLColumnExists( columnName, tableName ),
        "SELECT FIRST 1 " + columnName + " FROM " + tableName );
  }

  @Test
  public void testGetSQLQueryFields() {
    String tableName = "table";
    Assert.assertEquals( dialect.getSQLQueryFields( tableName ), "SELECT FIRST 1 * FROM " + tableName );
  }

  @Test
  public void testGetSQLQueryColumnFields() {
    String tableName = "table1";
    Assert.assertEquals( dialect.getSQLQueryFields( tableName ), "SELECT FIRST 1 * FROM " + tableName );
  }

  @Test
  public void testGetSQLLockTables() {
    String[] tables = { "table1", "table2" };
    Assert.assertEquals( dialect.getSQLLockTables( tables ).trim(),
        "LOCK TABLE " + tables[0] + " IN EXCLUSIVE MODE; LOCK TABLE " + tables[1] + " IN EXCLUSIVE MODE;" );
  }
}
