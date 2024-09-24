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
    Assert.assertEquals( dialect.getURL( conn ), "jdbc:odbc:null" );

    conn.setAccessType( DatabaseAccessType.NATIVE );
    Assert.assertEquals( dialect.getURL( conn ), "jdbc:teradata://null" );
  }

}
