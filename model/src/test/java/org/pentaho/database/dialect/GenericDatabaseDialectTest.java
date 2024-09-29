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
import org.pentaho.database.model.DatabaseConnection;
import org.pentaho.database.model.IDatabaseType;

public class GenericDatabaseDialectTest {

  private GenericDatabaseDialect dialect;

  public GenericDatabaseDialectTest() {
    this.dialect = new GenericDatabaseDialect();
  }

  @Test
  public void testGetNativeDriver() {
    Assert.assertNull( dialect.getNativeDriver() );
  }

  @Test
  public void testGetURL() throws Exception {
    DatabaseConnection conn = new DatabaseConnection();
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
    Assert.assertEquals( dbType.getName(), "Generic database" );
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
  public void testSupportsRepository() {
    Assert.assertFalse( dialect.supportsRepository() );
  }

  @Test
  public void testIsFetchSizeSupported() {
    Assert.assertFalse( dialect.isFetchSizeSupported() );
  }

  @Test
  public void testSupportsOptionsInURL() {
    Assert.assertFalse( dialect.supportsOptionsInURL() );
  }
}
