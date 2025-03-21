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


package org.pentaho.database.dialect;

import junit.framework.Assert;
import org.junit.Test;
import org.pentaho.database.IValueMeta;
import org.pentaho.database.ThinValueMeta;
import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.DatabaseConnection;
import org.pentaho.database.model.IDatabaseType;

public class TeradataDatabaseDialectTest {

  private TeradataDatabaseDialect dialect;

  public TeradataDatabaseDialectTest() {
    this.dialect = new TeradataDatabaseDialect();
  }

  @Test
  public void testGetReservedWords() {
    Assert.assertTrue( dialect.getReservedWords().length > 0 );
  }

  @Test
  public void testGetNativeDriver() {
    Assert.assertEquals( dialect.getNativeDriver(), "com.teradata.jdbc.TeraDriver" );
  }

  @Test
  public void testGetDatabaseType() {
    IDatabaseType dbType = dialect.getDatabaseType();
    Assert.assertEquals( dbType.getName(), "Teradata" );
  }

  @Test
  public void testGetExtraOptionIndicator() {
    Assert.assertEquals( dialect.getExtraOptionIndicator(), "/" );
  }

  @Test
  public void testGetExtraOptionValueSeparator() {
    Assert.assertEquals( dialect.getExtraOptionValueSeparator(), "=" );
  }

  @Test
  public void testGetExtraOptionSeparator() {
    Assert.assertEquals( dialect.getExtraOptionSeparator(), "," );
  }

  @Test
  public void testSupportsBitmapIndex() {
    Assert.assertEquals( dialect.supportsBitmapIndex(), false );
  }

  @Test
  public void testNeedsToLockAllTables() {
    Assert.assertEquals( dialect.needsToLockAllTables(), false );
  }

  @Test
  public void testNeedsPlaceHolder() {
    Assert.assertEquals( dialect.needsPlaceHolder(), false );
  }

  @Test
  public void testGetNativeJdbcPre() {
    Assert.assertEquals( dialect.getNativeJdbcPre(), "jdbc:teradata://" );
  }

  @Test
  public void testGetSynonymTypes() {
    Assert.assertTrue( dialect.getSynonymTypes() != null );
  }

  @Test
  public void testGetTruncateTableStatement() {
    String tableName = "table";
    Assert.assertEquals( dialect.getTruncateTableStatement( tableName ), "DELETE FROM " + tableName );
  }

  @Test
  public void testGetSQLTableExists() {
    String tableName = "table";
    Assert.assertEquals( dialect.getSQLTableExists( tableName ), "show table " + tableName );
  }

  @Test
  public void testGetLimitClause() {
    int rows = 5;
    Assert.assertEquals( dialect.getLimitClause( rows ), " sample " + Integer.toString( rows ) );
  }

  @Test
  public void testGetSQLQueryFields() {
    String tableName = "table";
    Assert.assertEquals( dialect.getSQLQueryFields( tableName ), "SELECT * FROM " + tableName + " sample 1" );
  }

  @Test
  public void testGetUsedLibraries() {
    Assert.assertEquals( dialect.getUsedLibraries()[0], "terajdbc4.jar" );
    Assert.assertEquals( dialect.getUsedLibraries()[1], "tdgssjava.jar" );
  }

  @Test
  public void testGetSQLColumnExists() {
    String columnName = "column1";
    String tableName = "table1";
    Assert.assertEquals( dialect.getSQLColumnExists( columnName, tableName ),
        "SELECT * FROM DBC.columns WHERE tablename =" + tableName + " AND columnname =" + columnName );
  }

  @Test
  public void testGetSQLQueryColumnFields() {
    String columnName = "column1";
    String tableName = "table1";
    Assert.assertEquals( dialect.getSQLQueryColumnFields( columnName, tableName ),
        "SELECT " + columnName + " FROM " + tableName + " sample 1" );
  }

  @Test
  public void testGetModifyColumnStatement() {
    String table = "table";
    String meta = "meta";
    IValueMeta valueMeta = new ThinValueMeta();
    valueMeta.setName( meta );
    Assert.assertEquals( dialect.getModifyColumnStatement( table, valueMeta, null, false, null, false ),
        "ALTER TABLE " + table + " MODIFY " + meta + "  UNKNOWN" );
  }

  @Test
  public void testGetAddColumnStatement() {
    String table = "table";
    String meta = "meta";
    IValueMeta valueMeta = new ThinValueMeta();
    valueMeta.setName( meta );
    Assert.assertEquals( dialect.getAddColumnStatement( table, valueMeta, null, false, null, false ),
        "ALTER TABLE " + table + " ADD " + meta + "  UNKNOWN" );

  }

  @Test
  public void testGetURL() throws Exception {
    DatabaseConnection conn = new DatabaseConnection();

    conn.setAccessType( DatabaseAccessType.NATIVE );
    Assert.assertEquals( dialect.getURL( conn ), "jdbc:teradata://null" );
  }

}
