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
import org.pentaho.database.model.DatabaseType;
import org.pentaho.database.model.IDatabaseConnection;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.pentaho.database.dialect.RedshiftDatabaseDialect.IAM_ACCESS_KEY_ID;
import static org.pentaho.database.dialect.RedshiftDatabaseDialect.IAM_PROFILE_NAME;
import static org.pentaho.database.dialect.RedshiftDatabaseDialect.IAM_SECRET_ACCESS_KEY;
import static org.pentaho.database.dialect.RedshiftDatabaseDialect.IAM_SESSION_TOKEN;
import static org.pentaho.database.dialect.RedshiftDatabaseDialect.JDBC_AUTH_METHOD;

public class RedshiftDatabaseDialectTest {
  @Test
  public void urlContainsProfileOptions() throws DatabaseDialectException {
    RedshiftDatabaseDialect dialect = new RedshiftDatabaseDialect();
    IDatabaseConnection connection = redshiftConnection();
    Map<String, String> attributes = new HashMap<>();
    attributes.put( JDBC_AUTH_METHOD, RedshiftDatabaseDialect.PROFILE_CREDENTIALS );
    attributes.put( IAM_PROFILE_NAME, "root" );
    connection.setAttributes( attributes );
    assertEquals( "jdbc:redshift:iam://redshift.com:9999/FantasyFootball?Profile=root",
      dialect.getURLWithExtraOptions( connection ) );
  }

  @Test
  public void urlContainsIAMOptions() throws DatabaseDialectException {
    RedshiftDatabaseDialect dialect = new RedshiftDatabaseDialect();
    IDatabaseConnection connection = redshiftConnection();
    Map<String, String> attributes = new HashMap<>();
    attributes.put( JDBC_AUTH_METHOD, RedshiftDatabaseDialect.IAM_CREDENTIALS );
    attributes.put( IAM_SECRET_ACCESS_KEY, "secretKey" );
    attributes.put( IAM_ACCESS_KEY_ID, "id" );
    attributes.put( IAM_SESSION_TOKEN, "session" );
    connection.setAttributes( attributes );
    String resultString = dialect.getURLWithExtraOptions( connection );

    assertTrue( resultString.startsWith( "jdbc:redshift:iam://redshift.com:9999/FantasyFootball?" ) );
    assertTrue( resultString.contains( "AccessKeyID=id" ) );
    assertTrue( resultString.contains( "SecretAccessKey=secretKey" ) );
    assertTrue( resultString.contains( "SessionToken=session" ) );
  }

  private IDatabaseConnection redshiftConnection() {
    IDatabaseConnection connection = new DatabaseConnection();
    DatabaseType databaseType = new DatabaseType();
    databaseType.setShortName( "REDSHIFT" );
    connection.setDatabaseType( databaseType );
    connection.setHostname( "redshift.com" );
    connection.setDatabasePort( "9999" );
    connection.setDatabaseName( "FantasyFootball" );
    return connection;
  }
}
