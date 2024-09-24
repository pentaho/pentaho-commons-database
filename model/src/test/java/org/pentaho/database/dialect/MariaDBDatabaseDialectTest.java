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

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.DatabaseConnection;
import org.pentaho.database.model.IDatabaseType;

public class MariaDBDatabaseDialectTest extends MySQLDatabaseDialectTest {

  private MariaDBDatabaseDialect dialect;

  public MariaDBDatabaseDialectTest() {
    this.dialect = new MariaDBDatabaseDialect();
  }

  @Test
  public void testGetNativeDriver() {
    assertEquals( dialect.getNativeDriver(), "org.mariadb.jdbc.Driver" );
  }

  @Test
  public void testGetURL() throws Exception {
    DatabaseConnection conn = new DatabaseConnection();
    conn.setAccessType( DatabaseAccessType.NATIVE );
    assertEquals( dialect.getURL( conn ), "jdbc:mariadb://null/null" );
  }

  @Test
  public void testGetUsedLibraries() {
    assertEquals( dialect.getUsedLibraries()[0], "mariadb-java-client-1.4.6.jar" );
  }

  @Test
  public void testGetNativeJdbcPre() {
    assertEquals( dialect.getNativeJdbcPre(), "jdbc:mariadb://" );
  }

  @Test
  public void testGetDatabaseType() {
    IDatabaseType dbType = dialect.getDatabaseType();
    assertEquals( dbType.getName(), "MariaDB" );
  }

}
