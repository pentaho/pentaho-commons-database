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

public class MSSQLServerDatabaseDialectTest {

  private MSSQLServerDatabaseDialect dialect;

  public MSSQLServerDatabaseDialectTest() {
    this.dialect = new MSSQLServerDatabaseDialect();
  }

  @Test
  public void testGetReservedWords() {
    Assert.assertTrue( dialect.getReservedWords().length > 0 );
  }

  @Test
  public void testGetNativeDriver() {
    Assert.assertEquals( dialect.getNativeDriver(), "net.sourceforge.jtds.jdbc.Driver" );
  }

  @Test
  public void testGetDatabaseType() {
    IDatabaseType dbType = dialect.getDatabaseType();
    Assert.assertEquals( dbType.getName(), "MS SQL Server" );
  }

  @Test
  public void testGetExtraOptionIndicator() {
    Assert.assertEquals( dialect.getExtraOptionIndicator(), ";" );
  }

  @Test
  public void testGetExtraOptionValueSeparator() {
    Assert.assertEquals( dialect.getExtraOptionValueSeparator(), "=" );
  }

  @Test
  public void testGetExtraOptionSeparator() {
    Assert.assertEquals( dialect.getExtraOptionSeparator(), ";" );
  }

  @Test
  public void testSupportsBitmapIndex() {
    Assert.assertEquals( dialect.supportsBitmapIndex(), false );
  }

  @Test
  public void testNeedsToLockAllTables() {
    Assert.assertEquals( dialect.needsToLockAllTables(), true );
  }

  @Test
  public void testNeedsPlaceHolder() {
    Assert.assertEquals( dialect.needsPlaceHolder(), false );
  }

  @Test
  public void testGetNativeJdbcPre() {
    Assert.assertEquals( dialect.getNativeJdbcPre(), "jdbc:jtds:sqlserver://" );
  }

  @Test
  public void testGetSynonymTypes() {
    Assert.assertTrue( dialect.getSynonymTypes() != null );
  }

  @Test
  public void testGetTruncateTableStatement() {
    String tableName = "table";
    Assert.assertEquals( dialect.getTruncateTableStatement( tableName ), "TRUNCATE TABLE " + tableName );
  }

  @Test
  public void testGetSQLTableExists() {
    String tableName = "table";
    Assert.assertEquals( dialect.getSQLTableExists( tableName ), "SELECT TOP 1 * FROM " + tableName );
  }

  @Test
  public void testGetLimitClause() {
    int rows = 5;
    Assert.assertEquals( dialect.getLimitClause( rows ), "" );
  }

  @Test
  public void testGetSQLQueryFields() {
    String tableName = "table";
    Assert.assertEquals( dialect.getSQLQueryFields( tableName ), "SELECT TOP 1 * FROM " + tableName );
  }

  @Test
  public void testGetUsedLibraries() {
    Assert.assertEquals( dialect.getUsedLibraries()[0], "jtds-1.2.jar" );
  }

  @Test
  public void testGetSQLColumnExists() {
    String columnName = "column1";
    String tableName = "table1";
    Assert.assertEquals( dialect.getSQLColumnExists( columnName, tableName ),
        "SELECT TOP 1 " + columnName + " FROM " + tableName );
  }

  @Test
  public void testGetSQLQueryColumnFields() {
    String tableName = "table1";
    Assert.assertEquals( dialect.getSQLQueryFields( tableName ), "SELECT TOP 1 * FROM " + tableName );
  }

  @Test
  public void testGetModifyColumnStatement() {
    String table = "table";
    String meta = "meta";
    IValueMeta valueMeta = new ThinValueMeta();
    valueMeta.setName( meta );
    Assert.assertEquals( dialect.getModifyColumnStatement( table, valueMeta, null, false, null, false ),
        "ALTER TABLE " + table + " ALTER COLUMN " + meta + "  UNKNOWN" );
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
    Assert.assertEquals( dialect.getURL( conn ), "jdbc:jtds:sqlserver://null:null/null" );
  }

  @Test
  public void testSupportsSequences() {
    Assert.assertFalse( dialect.supportsSequences() );
  }

  @Test
  public void testSupportsGetBlob() {
    Assert.assertTrue( dialect.supportsGetBlob() );
  }

  @Test
  public void testSupportsBatchUpdates() {
    Assert.assertTrue( dialect.supportsBatchUpdates() );
  }

  @Test
  public void testeGetMaxVARCHARLength() {
    Assert.assertTrue( dialect.getMaxVARCHARLength() == 9999999 );
  }

  @Test
  public void testGetSQLUnlockTables() {
    Assert.assertNull( dialect.getSQLUnlockTables( null ) );
  }

  @Test
  public void testSupportsSchemas() {
    Assert.assertTrue( dialect.supportsSchemas() );
  }

  @Test
  public void testSupportsSetCharacterStream() {
    Assert.assertTrue( dialect.supportsSetCharacterStream() );
  }

  @Test
  public void testGetSQLNextSequenceValue() {
    String sequenceName = "seq";
    Assert.assertEquals( dialect.getSQLNextSequenceValue( sequenceName ), "" );

  }

  @Test
  public void testGetSQLCurrentSequenceValue() {
    String sequenceName = "seq";
    Assert.assertEquals( dialect.getSQLCurrentSequenceValue( sequenceName ), "" );
  }

  @Test
  public void testGetSQLSequenceExists() {
    String sequenceName = "seq";
    Assert.assertEquals( dialect.getSQLSequenceExists( sequenceName ), "" );
  }

  @Test
  public void testGetDropColumnStatement() {
    String tableName = "table1";
    String meta = "meta1";
    IValueMeta valueMeta = new ThinValueMeta();
    valueMeta.setName( meta );
    Assert.assertEquals( dialect.getDropColumnStatement( tableName, valueMeta, null, false, null, false ),
        "ALTER TABLE " + tableName + " DROP COLUMN " + valueMeta.getName() + " " );
  }

  @Test
  public void testSupportsSynonyms() {
    Assert.assertFalse( dialect.supportsSynonyms() );
  }

  @Test
  public void testSupportsCatalogs() {
    Assert.assertFalse( dialect.supportsCatalogs() );
  }
}
