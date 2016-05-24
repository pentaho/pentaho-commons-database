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

public class MSSQLServerNativeDatabaseDialectTest {

  private MSSQLServerNativeDatabaseDialect dialect;

  public MSSQLServerNativeDatabaseDialectTest() {
    this.dialect = new MSSQLServerNativeDatabaseDialect();
  }

  @Test
  public void testGetNativeDriver() {
    Assert.assertEquals( dialect.getNativeDriver(), "com.microsoft.sqlserver.jdbc.SQLServerDriver" );
  }

  @Test
  public void testGetURL() throws Exception {
    DatabaseConnection conn = new DatabaseConnection();
    conn.setAccessType( DatabaseAccessType.NATIVE );
    Assert.assertEquals( dialect.getURL( conn ),
        "jdbc:sqlserver://null:null;databaseName=null;integratedSecurity=false" );
  }

  @Test
  public void testGetUsedLibraries() {
    Assert.assertEquals( dialect.getUsedLibraries()[0], "sqljdbc.jar" );
  }

  @Test
  public void testGetNativeJdbcPre() {
    Assert.assertEquals( dialect.getNativeJdbcPre(), "jdbc:sqlserver://" );
  }

  @Test
  public void testGetDatabaseType() {
    IDatabaseType dbType = dialect.getDatabaseType();
    Assert.assertEquals( dbType.getName(), "MS SQL Server (Native)" );
  }
}
