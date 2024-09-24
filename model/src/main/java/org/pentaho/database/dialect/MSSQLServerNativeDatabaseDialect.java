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

import org.pentaho.database.DatabaseDialectException;
import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.DatabaseConnection;
import org.pentaho.database.model.DatabaseType;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.model.IDatabaseType;

public class MSSQLServerNativeDatabaseDialect extends MSSQLServerDatabaseDialect {

  /**
   * 
   */
  private static final long serialVersionUID = -1959677681143958923L;

  // TODO: Implement Integrated Security configuration
  public static final String ATTRIBUTE_USE_INTEGRATED_SECURITY = "MSSQLUseIntegratedSecurity"; //$NON-NLS-1$

  private static final IDatabaseType DBTYPE = new DatabaseType( "MS SQL Server (Native)", "MSSQLNative",
      DatabaseAccessType.getList( DatabaseAccessType.NATIVE, DatabaseAccessType.JNDI ), 1433,
      "http://msdn.microsoft.com/en-us/library/ms378428.aspx" );

  public MSSQLServerNativeDatabaseDialect() {

  }

  public IDatabaseType getDatabaseType() {
    return DBTYPE;
  }

  @Override
  public String getNativeDriver() {
    return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
  }

  @Override
  public String getNativeJdbcPre() {
    return "jdbc:sqlserver://";
  }

  @Override
  public String getURL( IDatabaseConnection connection ) throws DatabaseDialectException {
    String useIntegratedSecurity = "false";
    Object value = connection.getAttributes().get( ATTRIBUTE_USE_INTEGRATED_SECURITY );
    if ( value != null && value instanceof String ) {
      useIntegratedSecurity = (String) value;
      // Check if the String can be parsed into a boolean
      try {
        Boolean.parseBoolean( useIntegratedSecurity );
      } catch ( IllegalArgumentException e ) {
        useIntegratedSecurity = "false"; //$NON-NLS-1$
      }
    }
    return getNativeJdbcPre() + connection.getHostname() + ":" + connection.getDatabasePort() + ";databaseName="
      + connection.getDatabaseName() + ";integratedSecurity=" + useIntegratedSecurity;
  }

  @Override
  public IDatabaseConnection createNativeConnection( String jdbcUrl ) {
    if ( !jdbcUrl.startsWith( getNativeJdbcPre() ) ) {
      throw new RuntimeException( "JDBC URL " + jdbcUrl + " does not start with " + getNativeJdbcPre() );
    }
    DatabaseConnection dbconn = new DatabaseConnection();
    dbconn.setDatabaseType( getDatabaseType() );
    dbconn.setAccessType( DatabaseAccessType.NATIVE );
    String str = jdbcUrl.substring( getNativeJdbcPre().length() );
    String hostname = null;
    String port = null;
    String databaseNameAndParams = null;

    // hostname:port;databaseName=dbname;integratedSecurity=security

    // hostname:port
    // hostname/dbname
    // dbname

    if ( str.indexOf( ":" ) >= 0 ) {
      hostname = str.substring( 0, str.indexOf( ":" ) );
      str = str.substring( str.indexOf( ":" ) + 1 );
      if ( str.indexOf( getExtraOptionIndicator() ) >= 0 ) {
        port = str.substring( 0, str.indexOf( ";" ) );
        databaseNameAndParams = str.substring( str.indexOf( getExtraOptionIndicator() ) + 1 );
      } else {
        port = str;
      }
    } else {
      if ( str.indexOf( getExtraOptionIndicator() ) >= 0 ) {
        hostname = str.substring( 0, str.indexOf( getExtraOptionIndicator() ) );
        databaseNameAndParams = str.substring( str.indexOf( getExtraOptionIndicator() ) + 1 );
      } else {
        hostname = str;
      }
    }
    if ( hostname != null ) {
      dbconn.setHostname( hostname );
    }
    if ( port != null ) {
      dbconn.setDatabasePort( port );
    }

    // parse parameters out of URL
    if ( databaseNameAndParams != null ) {
      setDatabaseNameAndParams( dbconn, databaseNameAndParams );
    }
    return dbconn;
  }

  @Override
  protected void setDatabaseNameAndParams( DatabaseConnection dbconn, String databaseNameAndParams ) {
    String[] paramData = databaseNameAndParams.split( getExtraOptionSeparator() );
    for ( String param : paramData ) {
      String[] nameAndValue = param.split( getExtraOptionValueSeparator() );
      if ( nameAndValue[0] != null && nameAndValue[0].trim().length() > 0 ) {
        if ( nameAndValue.length == 1 ) {
          if ( nameAndValue[0].equals( "databaseName" ) ) {
            dbconn.setDatabaseName( "" );
          } else if ( nameAndValue[0].equals( "integratedSecurity" ) ) {
            dbconn.getAttributes().put( ATTRIBUTE_USE_INTEGRATED_SECURITY, "false" );
          } else {
            dbconn.addExtraOption( dbconn.getDatabaseType().getShortName(), nameAndValue[0], "" );
          }
        } else {
          if ( nameAndValue[0].equals( "databaseName" ) ) {
            dbconn.setDatabaseName( nameAndValue[1] );
          } else if ( nameAndValue[0].equals( "integratedSecurity" ) ) {
            dbconn.getAttributes().put( ATTRIBUTE_USE_INTEGRATED_SECURITY, nameAndValue[1] );
          } else {
            dbconn.addExtraOption( dbconn.getDatabaseType().getShortName(), nameAndValue[0], nameAndValue[1] );
          }
        }
      }
    }

  }

  @Override
  public String[] getUsedLibraries() {
    return new String[] { "sqljdbc.jar" };
  }

}
