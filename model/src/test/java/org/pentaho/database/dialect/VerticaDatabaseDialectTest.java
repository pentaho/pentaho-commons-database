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

public class VerticaDatabaseDialectTest {

  private VerticaDatabaseDialect dialect;

  public VerticaDatabaseDialectTest() {
    this.dialect = new VerticaDatabaseDialect();
  }

  @Test
  public void testGetNativeDriver() {
    Assert.assertEquals( dialect.getNativeDriver(), "com.vertica.Driver" );
  }

  @Test
  public void testGetURL() throws Exception {
    DatabaseConnection conn = new DatabaseConnection();
    conn.setAccessType( DatabaseAccessType.NATIVE );
    Assert.assertEquals( dialect.getURL( conn ), "jdbc:vertica://null:null/null" );
  }

  @Test
  public void testGetUsedLibraries() {
    Assert.assertEquals( dialect.getUsedLibraries()[0], "vertica_2.5_jdk_5.jar" );
  }

  @Test
  public void testGetNativeJdbcPre() {
    Assert.assertEquals( dialect.getNativeJdbcPre(), "jdbc:vertica://" );
  }

  @Test
  public void testGetDatabaseType() {
    IDatabaseType dbType = dialect.getDatabaseType();
    Assert.assertEquals( dbType.getName(), "Vertica" );
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
    Assert.assertEquals( dialect.getTruncateTableStatement( tableName ), "TRUNCATE TABLE " + tableName );
  }

  @Test
  public void testSupportsTimeStampToDateConversion() {
    Assert.assertFalse( dialect.supportsTimeStampToDateConversion() );
  }

  @Test
  public void testSupportsSequences() {
    Assert.assertTrue( dialect.supportsSequences() );
  }

  @Test
  public void testGetExtraOptionSeparator() {
    Assert.assertEquals( dialect.getExtraOptionSeparator(), "&" );
  }

  @Test
  public void testGetExtraOptionIndicator() {
    Assert.assertEquals( dialect.getExtraOptionIndicator(), "?" );
  }

  @Test
  public void testRequiresCastToVariousForIsNull() {
    Assert.assertTrue( dialect.requiresCastToVariousForIsNull() );
  }

  @Test
  public void testSupportsBooleanDataType() {
    Assert.assertTrue( dialect.supportsBooleanDataType() );
  }

  @Test
  public void testSupportsAutoInc() {
    Assert.assertFalse( dialect.supportsAutoInc() );
  }

  @Test
  public void testGetMaxVARCHARLength() {
    Assert.assertEquals( dialect.getMaxVARCHARLength(), 4000 );
  }

  @Test
  public void testGetMaxTextFieldLength() {
    Assert.assertEquals( dialect.getMaxTextFieldLength(), 0 );
  }

  @Test
  public void testGetDefaultDatabasePort() {
    Assert.assertEquals( dialect.getDefaultDatabasePort(), 5433 );
  }

  @Test
  public void testSupportsRepository() {
    Assert.assertFalse( dialect.supportsRepository() );
  }

  @Test
  public void testIsFetchSizeSupported() {
    Assert.assertFalse( dialect.isFetchSizeSupported() );
  }

  @Test
  public void testGetLimitClause() {
    int nrRows = 0;
    Assert.assertEquals( dialect.getLimitClause( nrRows ), " LIMIT " + nrRows );
  }

}
