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
* Copyright (c) 2002-2013 Pentaho Corporation..  All rights reserved.
*/

package org.pentaho.database.dialect;

import junit.framework.Assert;
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
    Assert.assertEquals( dialect.getNativeDriver(), "org.gjt.mm.mysql.Driver" );
  }

  @Test
  public void testGetURL() throws Exception {
    DatabaseConnection conn = new DatabaseConnection();
    conn.setAccessType( DatabaseAccessType.NATIVE );
    Assert.assertEquals( dialect.getURL( conn ), "jdbc:mysql://null/null" );
  }

  @Test
  public void testGetUsedLibraries() {
    Assert.assertEquals( dialect.getUsedLibraries()[0], "mysql-connector-java-3.1.14-bin.jar" );
  }

  @Test
  public void testGetNativeJdbcPre() {
    Assert.assertEquals( dialect.getNativeJdbcPre(), "jdbc:mysql://" );
  }

  @Test
  public void testGetDatabaseType() {
    IDatabaseType dbType = dialect.getDatabaseType();
    Assert.assertEquals( dbType.getName(), "MySQL" );
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
  public void testSupportsSynonyms() {
    Assert.assertFalse( dialect.supportsSynonyms() );
  }

  @Test
  public void testSupportsViews() {
    Assert.assertTrue( dialect.supportsViews() );
  }

  @Test
  public void testSupportsTransactions() {
    Assert.assertFalse( dialect.supportsTransactions() );
  }

  @Test
  public void testGetExtraOptionIndicator() {
    Assert.assertEquals( dialect.getExtraOptionIndicator(), "?" );
  }

  @Test
  public void testGetExtraOptionSeparator() {
    Assert.assertEquals( dialect.getExtraOptionSeparator(), "&" );
  }

  @Test
  public void testNeedsToLockAllTables() {
    Assert.assertTrue( dialect.needsToLockAllTables() );
  }

  @Test
  public void testSupportsBooleanDataType() {
    Assert.assertFalse( dialect.supportsBooleanDataType() );
  }

  @Test
  public void testGetSQLUnlockTables() {
    Assert.assertEquals( dialect.getSQLUnlockTables( null ), "UNLOCK TABLES" );
  }

  @Test
  public void testGetEndQuote() {
    Assert.assertEquals( dialect.getEndQuote(), "`" );
  }

  @Test
  public void testGetStartQuote() {
    Assert.assertEquals( dialect.getStartQuote(), "`" );
  }
}
