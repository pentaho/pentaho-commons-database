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

public class DB2DatabaseDialectTest {

  private DB2DatabaseDialect dialect;

  public DB2DatabaseDialectTest() {
    this.dialect = new DB2DatabaseDialect();
  }

  @Test
  public void testGetReservedWords() {
    Assert.assertTrue( dialect.getReservedWords().length > 0 );
  }

  @Test
  public void testGetNativeDriver() {
    Assert.assertEquals( dialect.getNativeDriver(), "com.ibm.db2.jcc.DB2Driver" );
  }

  @Test
  public void testGetDatabaseType() {
    IDatabaseType dbType = dialect.getDatabaseType();
    Assert.assertEquals( dbType.getName(), "IBM DB2" );
  }

  @Test
  public void testGetExtraOptionIndicator() {
    Assert.assertEquals( dialect.getExtraOptionIndicator(), ":" );
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
    Assert.assertEquals( dialect.supportsBitmapIndex(), true );
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
    Assert.assertEquals( dialect.getNativeJdbcPre(), "jdbc:db2://" );
  }

  @Test
  public void testGetSynonymTypes() {
    Assert.assertTrue( dialect.getSynonymTypes() != null );
  }

  @Test
  public void testGetTruncateTableStatement() {
    String tableName = "table";
    Assert.assertEquals( dialect.getTruncateTableStatement( tableName ),
        "ALTER TABLE " + tableName + " ACTIVATE NOT LOGGED INITIALLY WITH EMPTY TABLE" );
  }

  @Test
  public void testGetSQLTableExists() {
    String tableName = "table";
    Assert.assertEquals( dialect.getSQLTableExists( tableName ), "SELECT 1 FROM " + tableName );
  }

  @Test
  public void testGetLimitClause() {
    int rows = 5;
    Assert.assertEquals( dialect.getLimitClause( rows ), "" );
  }

  @Test
  public void testGetSQLQueryFields() {
    String tableName = "table";
    Assert.assertEquals( dialect.getSQLQueryFields( tableName ), "SELECT * FROM " + tableName );
  }

  @Test
  public void testGetUsedLibraries() {
    Assert.assertEquals( dialect.getUsedLibraries()[0], "db2jcc.jar" );
    Assert.assertEquals( dialect.getUsedLibraries()[1], "db2jcc_license_cu.jar" );
  }

  @Test
  public void testGetSQLColumnExists() {
    String columnName = "column1";
    String tableName = "table1";
    Assert.assertEquals( dialect.getSQLColumnExists( columnName, tableName ),
        "SELECT " + columnName + " FROM " + tableName );
  }

  @Test
  public void testGetSQLQueryColumnFields() {
    String tableName = "table1";
    Assert.assertEquals( dialect.getSQLQueryFields( tableName ), "SELECT * FROM " + tableName );
  }

  @Test
  public void testGetModifyColumnStatement() {
    String table = "table";
    String meta = "meta";
    IValueMeta valueMeta = new ThinValueMeta();
    valueMeta.setName( meta );
    Assert.assertEquals( dialect.getModifyColumnStatement( table, valueMeta, null, false, null, false ),
        "ALTER TABLE " + table + " DROP COLUMN " + meta + " ; ALTER TABLE " + table + " ADD COLUMN " + meta
            + "  UNKNOWN" );
  }

  @Test
  public void testGetAddColumnStatement() {
    String table = "table";
    String meta = "meta";
    IValueMeta valueMeta = new ThinValueMeta();
    valueMeta.setName( meta );
    Assert.assertEquals( dialect.getAddColumnStatement( table, valueMeta, null, false, null, false ),
        "ALTER TABLE " + table + " ADD COLUMN " + meta + "  UNKNOWN" );

  }

  @Test
  public void testGetURL() throws Exception {
    DatabaseConnection conn = new DatabaseConnection();
    Assert.assertEquals( dialect.getURL( conn ), "jdbc:db2://null:null/null" );

    conn.setAccessType( DatabaseAccessType.NATIVE );
    Assert.assertEquals( dialect.getURL( conn ), "jdbc:db2://null:null/null" );
  }

  @Test
  public void testSupportsSequences() {
    Assert.assertTrue( dialect.supportsSequences() );
  }

  @Test
  public void testSupportsGetBlob() {
    Assert.assertFalse( dialect.supportsGetBlob() );
  }

  @Test
  public void testSupportsBatchUpdates() {
    Assert.assertFalse( dialect.supportsBatchUpdates() );
  }

  @Test
  public void testeGetMaxVARCHARLength() {
    Assert.assertTrue( dialect.getMaxVARCHARLength() == 32672 );
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
    Assert.assertFalse( dialect.supportsSetCharacterStream() );
  }

  @Test
  public void testGetSQLNextSequenceValue() {
    String sequenceName = "seq";
    Assert.assertEquals( dialect.getSQLNextSequenceValue( sequenceName ),
        "SELECT NEXT VALUE FOR " + sequenceName + " FROM SYSIBM.SYSDUMMY1" );

  }

  @Test
  public void testGetSQLCurrentSequenceValue() {
    String sequenceName = "seq";
    Assert.assertEquals( dialect.getSQLCurrentSequenceValue( sequenceName ),
        "SELECT PREVIOUS VALUE FOR " + sequenceName + " FROM SYSIBM.SYSDUMMY1" );
  }

  @Test
  public void testGetSQLSequenceExists() {
    String sequenceName = "seq";
    Assert.assertEquals( dialect.getSQLSequenceExists( sequenceName ),
        "SELECT * FROM SYSCAT.SEQUENCES WHERE SEQNAME = '" + sequenceName.toUpperCase() + "'" );
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

}
