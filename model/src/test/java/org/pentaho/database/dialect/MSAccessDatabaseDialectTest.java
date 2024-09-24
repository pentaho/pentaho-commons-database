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

public class MSAccessDatabaseDialectTest {

  private MSAccessDatabaseDialect dialect;

  public MSAccessDatabaseDialectTest() {
    this.dialect = new MSAccessDatabaseDialect();
  }

  @Test
  public void testGetNativeDriver() {
    Assert.assertNull( dialect.getNativeDriver() );
  }

  @Test
  public void testGetURL() throws Exception {
    DatabaseConnection conn = new DatabaseConnection();
    conn.setAccessType( DatabaseAccessType.NATIVE );
    Assert.assertEquals( dialect.getURL( conn ), "jdbc:odbc:null" );
  }

  @Test
  public void testGetUsedLibraries() {
    Assert.assertTrue( dialect.getUsedLibraries().length == 0 );
  }

  @Test
  public void testGetNativeJdbcPre() {
    Assert.assertNull( dialect.getNativeJdbcPre() );
  }

  @Test
  public void testGetDatabaseType() {
    IDatabaseType dbType = dialect.getDatabaseType();
    Assert.assertEquals( dbType.getName(), "MS Access" );
  }

  @Test
  public void testGetReservedWords() {
    Assert.assertTrue( dialect.getReservedWords().length > 0 );
  }

  @Test
  public void testSupportsBitmapIndex() {
    Assert.assertFalse( dialect.supportsBitmapIndex() );
  }

  @Test
  public void testGetTruncateTableStatement() {
    String tableName = "table1";
    Assert.assertEquals( dialect.getTruncateTableStatement( tableName ), "DELETE FROM " + tableName );
  }

  @Test
  public void testSupportsGetBlob() {
    Assert.assertFalse( dialect.supportsGetBlob() );
  }

  @Test
  public void testGetEndQuote() {
    Assert.assertEquals( dialect.getEndQuote(), "]" );
  }

  @Test
  public void testGetStartQuote() {
    Assert.assertEquals( dialect.getStartQuote(), "[" );
  }

  @Test
  public void testSupportsSynonyms() {
    Assert.assertFalse( dialect.supportsSynonyms() );
  }

  @Test
  public void testSupportsViews() {
    Assert.assertFalse( dialect.supportsViews() );
  }

  @Test
  public void testSupportsSetLong() {
    Assert.assertFalse( dialect.supportsSetLong() );
  }

  @Test
  public void testSupportsTransactions() {
    Assert.assertFalse( dialect.supportsTransactions() );
  }

  @Test
  public void testGtMaxTextFieldLength() {
    Assert.assertEquals( dialect.getMaxTextFieldLength(), 65536 );
  }

  @Test
  public void testIsFetchSizeSupported() {
    Assert.assertFalse( dialect.isFetchSizeSupported() );
  }

  @Test
  public void testSupportsSetCharacterStream() {
    Assert.assertFalse( dialect.supportsSetCharacterStream() );
  }

  @Test
  public void getSchemaTableCombination() {
    String schemaName = "schema";
    String tablePart = "table";
    Assert.assertEquals( dialect.getSchemaTableCombination( schemaName, tablePart ),
        "\"" + schemaName + "\".\"" + tablePart + "\"" );
  }

}
