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

import org.pentaho.database.DatabaseDialectException;
import org.pentaho.database.IValueMeta;
import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.DatabaseType;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.model.IDatabaseType;

public class SnowflakeDatabaseDialect extends AbstractDatabaseDialect {

  public static final String WAREHOUSE = "warehouse";

  public static final IDatabaseType DBTYPE = new DatabaseType( "Snowflake", "SNOWFLAKEHV", DatabaseAccessType.getList(
    DatabaseAccessType.NATIVE, DatabaseAccessType.JNDI ), 443,
    "https://docs.snowflake.net/manuals/user-guide/jdbc-configure.html#jdbc-driver-connection-string" );

  @Override public String getURL( IDatabaseConnection connection ) throws DatabaseDialectException {
    return getNativeJdbcPre() + connection.getHostname() + ":" + connection.getDatabasePort() + "/?db="
      + connection.getDatabaseName()
      + "&warehouse=" + connection.getAttributes().get( WAREHOUSE );
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
