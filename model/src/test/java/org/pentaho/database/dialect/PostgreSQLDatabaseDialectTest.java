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

import org.junit.Test;
import org.pentaho.database.IValueMeta;
import org.pentaho.database.ThinValueMeta;
import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.DatabaseConnection;

import junit.framework.Assert;

public class PostgreSQLDatabaseDialectTest {

  private PostgreSQLDatabaseDialect dialect;

  public PostgreSQLDatabaseDialectTest() {
    this.dialect = new PostgreSQLDatabaseDialect();
  }

  @Test
  public void testGetNativeDriver() {
    Assert.assertEquals( dialect.getNativeDriver(), "org.postgresql.Driver" );
  }

  @Test
  public void testIsDefaultingToUppercase() {
    Assert.assertFalse( dialect.isDefaultingToUppercase() );
  }

  @Test
  public void testGetSQLUnlockTables() {
    Assert.assertNull( dialect.getSQLUnlockTables( null ) );
  }

  @Test
  public void testNeedsToLockAllTables() {
    Assert.assertFalse( dialect.needsToLockAllTables() );
  }

  @Test
  public void testSupportsAutoInc() {
    Assert.assertTrue( dialect.supportsAutoInc() );
  }

  @Test
  public void testSupportsSequences() {
    Assert.assertTrue( dialect.supportsSequences() );
  }

  @Test
  public void testSupportsSynonyms() {
    Assert.assertFalse( dialect.supportsSynonyms() );
  }

  @Test
  public void testSupportsBitmapIndex() {
    Assert.assertFalse( dialect.supportsBitmapIndex() );
  }

  @Test
  public void testIsFetchSizeSupported() {
    Assert.assertTrue( dialect.isFetchSizeSupported() );
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
  public void testGetNativeJdbcPre() {
    Assert.assertEquals( dialect.getNativeJdbcPre(), "jdbc:postgresql://" );
  }

  @Test
  public void testGetSQLTableExists() {
    String table = "table";
    Assert.assertEquals( dialect.getSQLTableExists( table ), "SELECT * FROM " + table + " limit 1" );
  }

  @Test
  public void testGetSQLColumnExists() {
    String table = "table";
    String column = "column";
    Assert.assertEquals( dialect.getSQLColumnExists( table, column ), "SELECT table FROM column limit 1" );
  }

  @Test
  public void testGetUsedLibraries() {
    Assert.assertEquals( dialect.getUsedLibraries()[0], "postgresql-8.2-506.jdbc3.jar" );
  }

  @Test
  public void testGetSQLCurrentSequenceValue() {
    String seq = "test";
    Assert.assertEquals( dialect.getSQLCurrentSequenceValue( seq ), "SELECT last_value FROM " + seq );
  }

  @Test
  public void testGetLimitClause() {
    int rows = 5;
    Assert.assertEquals( dialect.getLimitClause( rows ), " limit " + rows );
  }

  @Test
  public void testGetSQLNextSequenceValue() {
    String seq = "test";
    Assert.assertEquals( dialect.getSQLNextSequenceValue( seq ), "SELECT nextval('" + seq + "')" );
  }

  @Test
  public void testGetSQLSequenceExists() {
    String seq = "test";
    Assert.assertEquals( dialect.getSQLSequenceExists( seq ),
        "SELECT relname AS sequence_name FROM pg_statio_all_sequences WHERE relname = '" + seq + "'" );
  }

  @Test
  public void testGetSQLListOfProcedures() {
    String user = "USER";
    DatabaseConnection conn = new DatabaseConnection();
    conn.setUsername( user );
    Assert.assertEquals( dialect.getSQLListOfProcedures( conn ),
        "select proname from pg_proc, pg_user where pg_user.usesysid = pg_proc.proowner and upper(pg_user.usename) = '"
            + user + "'" );
  }

  @Test
  public void testGetDropColumnStatement() {
    String table = "table";
    String meta = "meta";
    IValueMeta valueMeta = new ThinValueMeta();
    valueMeta.setName( meta );
    Assert.assertEquals( dialect.getDropColumnStatement( table, valueMeta, null, false, null, false ).trim(),
        "ALTER TABLE " + table + " DROP COLUMN " + meta );
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
  public void testGetURL() {
    DatabaseConnection conn = new DatabaseConnection();
    Assert.assertEquals( dialect.getURL( conn ), "jdbc:postgresql://null:null/null" );

    conn.setAccessType( DatabaseAccessType.ODBC );
    Assert.assertEquals( dialect.getURL( conn ), "jdbc:odbc:null" );
  }

  @Test
  public void testGetSQLLockTables() {
    String[] tables = { "table1", "table2" };
    Assert.assertEquals( dialect.getSQLLockTables( tables ).trim(),
        "LOCK TABLE " + tables[0] + " , " + tables[1] + " IN ACCESS EXCLUSIVE MODE;" );
  }

  @Test
  public void testGetModifyColumnStatement() {
    String table = "table1";
    String meta = "meta1";
    IValueMeta valueMeta = new ThinValueMeta();
    valueMeta.setName( meta );
    Assert.assertEquals( dialect.getModifyColumnStatement( table, valueMeta, null, false, null, false ),
        "ALTER TABLE " + table + " DROP COLUMN " + meta + " ; ALTER TABLE " + table + " ADD COLUMN " + meta
            + "  UNKNOWN" );
  }

  @Test
  public void testGetReservedWords() {
    Assert.assertTrue( dialect.getReservedWords().length > 0 );
  }
}
