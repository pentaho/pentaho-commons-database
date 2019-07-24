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
 * Copyright (c) 2019 Hitachi Vantara..  All rights reserved.
 */
package org.pentaho.database.dialect;

import org.junit.Test;
import org.pentaho.database.DatabaseDialectException;
import org.pentaho.database.model.DatabaseConnection;
import org.pentaho.database.model.IDatabaseConnection;

import static org.junit.Assert.assertEquals;

public class SnowflakeDatabaseDialectTest {
  @Test
  public void databaseOptionsUsesCorrectSeparator() throws DatabaseDialectException {
    SnowflakeDatabaseDialect dialect = new SnowflakeDatabaseDialect();
    IDatabaseConnection conn = new DatabaseConnection();
    conn.setDatabaseType( SnowflakeDatabaseDialect.DBTYPE );
    conn.setHostname( "abc.snowflake.com" );
    conn.setDatabasePort( "123" );
    conn.setDatabaseName( "words" );
    conn.addExtraOption( SnowflakeDatabaseDialect.DBTYPE.getShortName(), "CLIENT_SESSION_KEEP_ALIVE", "true" );
    assertEquals( "jdbc:snowflake://abc.snowflake.com:123/?db=words&warehouse=null&CLIENT_SESSION_KEEP_ALIVE=true",
      dialect.getURLWithExtraOptions( conn ) );
  }
}
