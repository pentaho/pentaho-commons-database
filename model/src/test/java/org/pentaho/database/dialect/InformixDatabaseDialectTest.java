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

public class InformixDatabaseDialectTest {

  private InformixDatabaseDialect dialect;

  public InformixDatabaseDialectTest() {
    this.dialect = new InformixDatabaseDialect();
  }

  @Test
  public void testGetNativeDriver() {
    Assert.assertEquals( dialect.getNativeDriver(), "com.informix.jdbc.IfxDriver" );
  }

  @Test
  public void testGetURL() throws Exception {
    DatabaseConnection conn = new DatabaseConnection();
    conn.setAccessType( DatabaseAccessType.NATIVE );
    Assert
        .assertEquals( dialect.getURL( conn ), "jdbc:informix-sqli://null:null/null:INFORMIXSERVER=null;DELIMIDENT=Y" );
  }

  @Test
  public void testGetUsedLibraries() {
    Assert.assertEquals( dialect.getUsedLibraries()[0], "ifxjdbc.jar" );
  }

  @Test
  public void testGetNativeJdbcPre() {
    Assert.assertEquals( dialect.getNativeJdbcPre(), "jdbc:informix-sqli:" );
  }

  @Test
  public void testGetDatabaseType() {
    IDatabaseType dbType = dialect.getDatabaseType();
    Assert.assertEquals( dbType.getName(), "Informix" );
  }

  @Test
  public void testNeedsToLockAllTables() {
    Assert.assertFalse( dialect.needsToLockAllTables() );
  }

  @Test
  public void testNeedsPlaceHolder() {
    Assert.assertTrue( dialect.needsPlaceHolder() );
  }

  @Test
  public void testGetSQLTableExists() {
    String tableName = "table";
    Assert.assertEquals( dialect.getSQLTableExists( tableName ), "SELECT FIRST 1 * FROM " + tableName );
  }

  @Test
  public void testGetSQLUnlockTables() {
    Assert.assertNull( dialect.getSQLUnlockTables( null ) );
  }

  @Test
  public void testGetSQLColumnExists() {
    String columnName = "column1";
    String tableName = "table1";
    Assert.assertEquals( dialect.getSQLColumnExists( columnName, tableName ),
        "SELECT FIRST 1 " + columnName + " FROM " + tableName );
  }

  @Test
  public void testGetSQLQueryFields() {
    String tableName = "table";
    Assert.assertEquals( dialect.getSQLQueryFields( tableName ), "SELECT FIRST 1 * FROM " + tableName );
  }

  @Test
  public void testGetSQLQueryColumnFields() {
    String tableName = "table1";
    Assert.assertEquals( dialect.getSQLQueryFields( tableName ), "SELECT FIRST 1 * FROM " + tableName );
  }

  @Test
  public void testGetSQLLockTables() {
    String[] tables = { "table1", "table2" };
    Assert.assertEquals( dialect.getSQLLockTables( tables ).trim(),
        "LOCK TABLE " + tables[0] + " IN EXCLUSIVE MODE; LOCK TABLE " + tables[1] + " IN EXCLUSIVE MODE;" );
  }
}
