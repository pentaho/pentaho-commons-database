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

import org.pentaho.database.DatabaseDialectException;
import org.pentaho.database.IValueMeta;
import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.DatabaseType;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.model.IDatabaseType;

public class SnowflakeDatabaseDialect extends AbstractDatabaseDialect {

  public static final String WAREHOUSE = "warehouse";

  public static final IDatabaseType DBTYPE = new DatabaseType( "Snowflake", "SNOWFLAKEHV", DatabaseAccessType.getList(
    DatabaseAccessType.NATIVE, DatabaseAccessType.ODBC, DatabaseAccessType.JNDI ), 443,
    "https://docs.snowflake.net/manuals/user-guide/jdbc-configure.html#jdbc-driver-connection-string" );

  @Override public String getURL( IDatabaseConnection connection ) throws DatabaseDialectException {
    if ( connection.getAccessType() == DatabaseAccessType.ODBC ) {
      return "jdbc:odbc:" + connection.getDatabaseName();
    } else {
      return getNativeJdbcPre() + connection.getHostname() + ":" + connection.getDatabasePort() + "/?db=" + connection.getDatabaseName()
        + "&warehouse=" + connection.getAttributes().get( WAREHOUSE );
    }
  }

  @Override public String getExtraOptionSeparator() {
    return "&";
  }

  @Override
  public String getAddColumnStatement( String tablename, IValueMeta v, String tk, boolean useAutoinc, String pk,
                                       boolean semicolon ) {
    return null;
  }

  @Override
  public String getModifyColumnStatement( String tablename, IValueMeta v, String tk, boolean useAutoinc, String pk,
                                          boolean semicolon ) {
    return null;
  }

  @Override
  public String getFieldDefinition( IValueMeta v, String tk, String pk, boolean useAutoinc, boolean addFieldname,
                                    boolean addCr ) {
    return null;
  }

  @Override public String[] getUsedLibraries() {
    return new String[] { "snowflake-jdbc-3.6.28.jar" };
  }

  @Override public String getNativeDriver() {
    return "net.snowflake.client.jdbc.SnowflakeDriver";
  }

  @Override public IDatabaseType getDatabaseType() {
    return DBTYPE;
  }

  @Override public String getEndQuote() {
    return "";
  }

  @Override public String getStartQuote() {
    return "";
  }

  @Override public String getNativeJdbcPre() {
    return "jdbc:snowflake://";
  }
}
