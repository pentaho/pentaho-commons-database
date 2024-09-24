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
 * Copyright (c) 2002-2019 Hitachi Vantara..  All rights reserved.
 */

package org.pentaho.database.dialect;

import java.io.Serializable;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.database.DatabaseDialectException;
import org.pentaho.database.IDatabaseDialect;
import org.pentaho.database.IDriverLocator;
import org.pentaho.database.IValueMeta;
import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.DatabaseConnection;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.util.ClassUtil;

public abstract class AbstractDatabaseDialect implements IDatabaseDialect, IDriverLocator, Serializable {
  private static final long serialVersionUID = 4949841921392501602L;

  /**
   * Use this length in a String value to indicate that you want to use a CLOB in stead of a normal text field.
   */
  public static final int CLOB_LENGTH = 9999999;

  /**
   * CR: operating systems specific Carriage Return
   */
  public static final String CR = " ";

  /*
   * *******************************************************************************
   * DEFAULT SETTINGS FOR ALL DATABASES ********************************************************************************
   */

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#getDefaultDatabasePort()
   */
  public int getDefaultDatabasePort() {
    return getDatabaseType().getDefaultDatabasePort();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#supportsSetCharacterStream()
   */
  public boolean supportsSetCharacterStream() {
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#supportsAutoInc()
   */
  public boolean supportsAutoInc() {
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#getLimitClause(int)
   */
  public String getLimitClause( int nrRows ) {
    return "";
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#getNotFoundTK(boolean)
   */
  public int getNotFoundTK( boolean useAutoInc ) {
    return 0;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#getSQLNextSequenceValue(java.lang.String)
   */
  public String getSQLNextSequenceValue( String sequenceName ) {
    return "";
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#getSQLCurrentSequenceValue(java.lang.String)
   */
  public String getSQLCurrentSequenceValue( String sequenceName ) {
    return "";
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#getSQLSequenceExists(java.lang.String)
   */
  public String getSQLSequenceExists( String sequenceName ) {
    return "";
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#isFetchSizeSupported()
   */
  public boolean isFetchSizeSupported() {
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#needsPlaceHolder()
   */
  public boolean needsPlaceHolder() {
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#supportsSchemas()
   */
  public boolean supportsSchemas() {
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#supportsCatalogs()
   */
  public boolean supportsCatalogs() {
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#supportsEmptyTransactions()
   */
  public boolean supportsEmptyTransactions() {
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#getFunctionSum()
   */
  public String getFunctionSum() {
    return "SUM";
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#getFunctionAverage()
   */
  public String getFunctionAverage() {
    return "AVG";
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#getFunctionMinimum()
   */
  public String getFunctionMinimum() {
    return "MIN";
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#getFunctionMaximum()
   */
  public String getFunctionMaximum() {
    return "MAX";
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#getFunctionCount()
   */
  public String getFunctionCount() {
    return "COUNT";
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#getSchemaTableCombination(java.lang.String, java.lang.String)
   */
  public String getSchemaTableCombination( String schemaName, String tablePart ) {
    return schemaName + "." + tablePart;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#getMaxTextFieldLength()
   */
  public int getMaxTextFieldLength() {
    return CLOB_LENGTH;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#getMaxVARCHARLength()
   */
  public int getMaxVARCHARLength() {
    return CLOB_LENGTH;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#supportsTransactions()
   */
  public boolean supportsTransactions() {
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#supportsSequences()
   */
  public boolean supportsSequences() {
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#supportsBitmapIndex()
   */
  public boolean supportsBitmapIndex() {
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#supportsSetLong()
   */
  public boolean supportsSetLong() {
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#getDropColumnStatement(java.lang.String,
   * org.pentaho.di.core.row.ValueMetaInterface, java.lang.String, boolean, java.lang.String, boolean)
   */
  public String getDropColumnStatement( String tablename, IValueMeta v, String tk, boolean useAutoinc, String pk,
      boolean semicolon ) {
    return "ALTER TABLE " + tablename + " DROP " + v.getName() + CR;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#getReservedWords()
   */
  public String[] getReservedWords() {
    return new String[] {};
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#quoteReservedWords()
   */
  public boolean quoteReservedWords() {
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#getStartQuote()
   */
  public String getStartQuote() {
    return "\"";
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#getEndQuote()
   */
  public String getEndQuote() {
    return "\"";
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#supportsRepository()
   */
  public boolean supportsRepository() {
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#getTableTypes()
   */
  public String[] getTableTypes() {
    return new String[] { "TABLE" };
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#getViewTypes()
   */
  public String[] getViewTypes() {
    return new String[] { "VIEW" };
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#getSynonymTypes()
   */
  public String[] getSynonymTypes() {
    return new String[] { "SYNONYM" };
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#useSchemaNameForTableList()
   */
  public boolean useSchemaNameForTableList() {
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#supportsViews()
   */
  public boolean supportsViews() {
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#supportsSynonyms()
   */
  public boolean supportsSynonyms() {
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#getSQLListOfProcedures()
   */
  public String getSQLListOfProcedures( IDatabaseConnection connection ) {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#getTruncateTableStatement(java.lang.String)
   */
  public String getTruncateTableStatement( String tableName ) {
    return "TRUNCATE TABLE " + tableName;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#getSQLQueryFields(java.lang.String)
   */
  public String getSQLQueryFields( String tableName ) {
    return "SELECT * FROM " + tableName;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#supportsFloatRoundingOnUpdate()
   */
  public boolean supportsFloatRoundingOnUpdate() {
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#getSQLLockTables(java.lang.String[])
   */
  public String getSQLLockTables( String[] tableNames ) {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#getSQLUnlockTables(java.lang.String[])
   */
  public String getSQLUnlockTables( String[] tableNames ) {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#supportsTimeStampToDateConversion()
   */
  public boolean supportsTimeStampToDateConversion() {
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#supportsBatchUpdates()
   */
  public boolean supportsBatchUpdates() {
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#supportsBooleanDataType()
   */
  public boolean supportsBooleanDataType() {
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#isDefaultingToUppercase()
   */
  public boolean isDefaultingToUppercase() {
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#supportsSetMaxRows()
   */
  public boolean supportsSetMaxRows() {
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#getSQLTableExists(java.lang.String)
   */
  public String getSQLTableExists( String tablename ) {
    return "SELECT 1 FROM " + tablename;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#getSQLColumnExists(java.lang.String, java.lang.String)
   */
  public String getSQLColumnExists( String columnname, String tablename ) {
    return "SELECT " + columnname + " FROM " + tablename;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#needsToLockAllTables()
   */
  public boolean needsToLockAllTables() {
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#isRequiringTransactionsOnQueries()
   */
  public boolean isRequiringTransactionsOnQueries() {
    return true;
  }

  public String getURLWithExtraOptions( IDatabaseConnection connection ) throws DatabaseDialectException {
    StringBuilder url = new StringBuilder( getURL( connection ) );
    if ( supportsOptionsInURL() ) {
      // OK, now add all the options...
      String optionIndicator = getExtraOptionIndicator();
      String optionSeparator = getExtraOptionSeparator();
      String valueSeparator = getExtraOptionValueSeparator();

      Map<String, String> map = connection.getExtraOptions();
      putOptionalOptions( connection, map );
      if ( map.size() > 0 ) {
        Iterator<String> iterator = map.keySet().iterator();
        boolean first = true;
        while ( iterator.hasNext() ) {
          String typedParameter = iterator.next();
          int dotIndex = typedParameter.indexOf( '.' );
          if ( dotIndex >= 0 ) {
            String typeCode = typedParameter.substring( 0, dotIndex );
            String parameter = typedParameter.substring( dotIndex + 1 );
            String value = map.get( typedParameter );

            // Only add to the URL if it's the same database type code...
            //
            if ( connection.getDatabaseType().getShortName().equals( typeCode ) ) {
              if ( first && url.indexOf( valueSeparator ) == -1 ) {
                url.append( optionIndicator );
              } else {
                url.append( optionSeparator );
              }

              url.append( parameter );
              if ( !isEmpty( value ) ) {
                url.append( valueSeparator ).append( value );
              }
              first = false;
            }
          }
        }
      }
    }
    return url.toString();
  }

  protected void putOptionalOptions( IDatabaseConnection connection, Map<String, String> map ) { }

  public String getExtraOptionsHelpText() {
    return getDatabaseType().getExtraOptionsHelpUrl();
  }

  /**
   * @return true if the database supports connection options in the URL, false if they are put in a Properties object.
   */
  public boolean supportsOptionsInURL() {
    return true;
  }

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

    // hostname:port/dbname
    // hostname:port
    // hostname/dbname
    // dbname

    if ( str.indexOf( ':' ) >= 0 ) {
      hostname = str.substring( 0, str.indexOf( ':' ) );
      str = str.substring( str.indexOf( ':' ) + 1 );
      if ( str.indexOf( '/' ) >= 0 ) {
        port = str.substring( 0, str.indexOf( '/' ) );
        databaseNameAndParams = str.substring( str.indexOf( '/' ) + 1 );
      } else {
        port = str;
      }
    } else {
      if ( str.indexOf( '/' ) >= 0 ) {
        hostname = str.substring( 0, str.indexOf( '/' ) );
        databaseNameAndParams = str.substring( str.indexOf( '/' ) + 1 );
      } else {
        databaseNameAndParams = str;
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

  protected void setDatabaseNameAndParams( DatabaseConnection dbconn, String databaseNameAndParams ) {
    if ( supportsOptionsInURL() ) {
      int paramIndex = databaseNameAndParams.indexOf( getExtraOptionIndicator() );
      if ( paramIndex >= 0 ) {
        String params = databaseNameAndParams.substring( paramIndex + 1 );
        databaseNameAndParams = databaseNameAndParams.substring( 0, paramIndex );
        String[] paramData = params.split( getExtraOptionSeparator() );
        for ( String param : paramData ) {
          String[] nameAndValue = param.split( getExtraOptionValueSeparator() );
          if ( nameAndValue[0] != null && nameAndValue[0].trim().length() > 0 ) {
            if ( nameAndValue.length == 1 ) {
              dbconn.addExtraOption( dbconn.getDatabaseType().getShortName(), nameAndValue[0], "" );
            } else {
              dbconn.addExtraOption( dbconn.getDatabaseType().getShortName(), nameAndValue[0], nameAndValue[1] );
            }
          }
        }
      }
    }
    dbconn.setDatabaseName( databaseNameAndParams );
  }

  public String getDriverClass( IDatabaseConnection connection ) {
    return getNativeDriver();
  }

  /**
   * @return true if the database JDBC databaseType supports getBlob on the resultset. If not we must use getBytes() to
   *         get the data.
   */
  public boolean supportsGetBlob() {
    return true;
  }

  /**
   * @return The extra option separator in database URL for this platform (usually this is semicolon ; )
   */
  public String getExtraOptionSeparator() {
    return ";";
  }

  /**
   * @return The extra option value separator in database URL for this platform (usually this is the equal sign = )
   */
  public String getExtraOptionValueSeparator() {
    return "=";
  }

  /**
   * @return This indicator separates the normal URL from the options
   */
  public String getExtraOptionIndicator() {
    return ";";
  }

  /**
   * Check if the string supplied is empty. A String is empty when it is null or when the length is 0
   * 
   * @param string
   *          The string to check
   * @return true if the string supplied is empty
   */
  public static final boolean isEmpty( String string ) {
    return string == null || string.length() == 0;
  }

  /**
   * Convert a String into an integer. If the conversion fails, assign a default value.
   * 
   * @param str
   *          The String to convert to an integer
   * @param def
   *          The default value
   * @return The converted value or the default.
   */
  public static final int toInt( String str, int def ) {
    int retval;
    try {
      retval = Integer.parseInt( str );
    } catch ( Exception e ) {
      retval = def;
    }
    return retval;
  }

  @Override public boolean isUsable() {
    return initialize( getNativeDriver() );
  }

  @Override public boolean initialize( String classname ) {
    return ClassUtil.canLoadClass( classname );
  }

  @Override public Driver getDriver( String url ) {
    try {
      return DriverManager.getDriver( url );
    } catch ( SQLException e ) {
      Log logger = LogFactory.getLog( IDatabaseDialect.class );
      if ( logger.isDebugEnabled() ) {
        logger.debug( "Unable to get driver for url " + url, e );
      }
    }
    return null;
  }
}
