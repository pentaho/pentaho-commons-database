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
