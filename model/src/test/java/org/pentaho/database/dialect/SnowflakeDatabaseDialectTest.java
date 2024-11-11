/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2028-08-13
 ******************************************************************************/

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
