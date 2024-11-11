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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.DatabaseConnection;
import org.pentaho.database.model.IDatabaseType;

public class MySQLDatabaseDialectTest {

  private MySQLDatabaseDialect dialect;

  public MySQLDatabaseDialectTest() {
    this.dialect = new MySQLDatabaseDialect();
  }

  @Test
  public void testGetNativeDriver() {
    assertEquals( dialect.getNativeDriver(), "com.mysql.jdbc.Driver" );
  }

  @Test
  public void testGetURL() throws Exception {
    DatabaseConnection conn = new DatabaseConnection();
    conn.setAccessType( DatabaseAccessType.NATIVE );
    assertEquals( dialect.getURL( conn ), "jdbc:mysql://null/null" );
  }

  @Test
  public void testGetUsedLibraries() {
    assertEquals( dialect.getUsedLibraries()[0], "mysql-connector-java-3.1.14-bin.jar" );
  }

  @Test
  public void testGetNativeJdbcPre() {
    assertEquals( dialect.getNativeJdbcPre(), "jdbc:mysql://" );
  }

  @Test
  public void testGetDatabaseType() {
    IDatabaseType dbType = dialect.getDatabaseType();
    assertEquals( dbType.getName(), "MySQL" );
  }

  @Test
  public void testGetReservedWords() {
    assertTrue( dialect.getReservedWords().length > 0 );
  }

  @Test
  public void testSupportsBitmapIndex() {
    assertFalse( dialect.supportsBitmapIndex() );
  }

  @Test
  public void testGetTruncateTableStatement() {
    String tableName = "table1";
    assertEquals( dialect.getTruncateTableStatement( tableName ), "TRUNCATE TABLE " + tableName );
  }

  @Test
  public void testSupportsSynonyms() {
    assertFalse( dialect.supportsSynonyms() );
  }

  @Test
  public void testSupportsViews() {
    assertTrue( dialect.supportsViews() );
  }

  @Test
  public void testSupportsTransactions() {
    assertFalse( dialect.supportsTransactions() );
  }

  @Test
  public void testGetExtraOptionIndicator() {
    assertEquals( dialect.getExtraOptionIndicator(), "?" );
  }

  @Test
  public void testGetExtraOptionSeparator() {
    assertEquals( dialect.getExtraOptionSeparator(), "&" );
  }

  @Test
  public void testNeedsToLockAllTables() {
    assertTrue( dialect.needsToLockAllTables() );
  }

  @Test
  public void testSupportsBooleanDataType() {
    assertFalse( dialect.supportsBooleanDataType() );
  }

  @Test
  public void testGetSQLUnlockTables() {
    assertEquals( dialect.getSQLUnlockTables( null ), "UNLOCK TABLES" );
  }

  @Test
  public void testGetEndQuote() {
    assertEquals( dialect.getEndQuote(), "`" );
  }

  @Test
  public void testGetStartQuote() {
    assertEquals( dialect.getStartQuote(), "`" );
  }
}
