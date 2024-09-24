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

import java.util.Collections;
import java.util.HashMap;
import org.pentaho.database.DatabaseDialectException;
import org.pentaho.database.IValueMeta;
import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.DatabaseType;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.model.IDatabaseType;

public class PDIDialect extends GenericDatabaseDialect {

  private static final long serialVersionUID = -661020279493753135L;

  private static final String URI_PATH = "kettle";
  private static final String EXTRA_OPTION_WEB_APPLICATION_NAME = "KettleThin.webappname";

  private static final IDatabaseType DBTYPE =
    new DatabaseType( "Pentaho Data Services", "KettleThin",
      DatabaseAccessType.getList( DatabaseAccessType.NATIVE, DatabaseAccessType.JNDI ),
      8080,
      "https://help.pentaho.com/Documentation/7.0/0L0/0Y0/090",
      "pentaho",  // Default database name
      Collections.unmodifiableMap(
        new HashMap<String, String>() {
          private static final long serialVersionUID = 6526668749527213238L;
        } ) );

  public PDIDialect() {

  }

  public IDatabaseType getDatabaseType() {
    return DBTYPE;
  }

  public String getNativeDriver() {
    return "org.pentaho.di.trans.dataservice.jdbc.ThinDriver";
  }

  @Override
  public boolean supportsOptionsInURL() {
    return true;
  }

  /**
   * @return This indicator separates the normal URL from the options
   */
  public String getExtraOptionIndicator() {
    return "?";
  }

  @Override
  public String getExtraOptionSeparator() {
    return "&";
  }

  @Override
  public String getModifyColumnStatement( String tablename, IValueMeta v, String tk, boolean use_autoinc, String pk,
                                          boolean semicolon ) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getAddColumnStatement( String tablename, IValueMeta v, String tk, boolean use_autoinc, String pk,
                                       boolean semicolon ) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getURL( IDatabaseConnection connection ) throws DatabaseDialectException {
    String path;

    if ( !connection.getExtraOptions().containsKey( EXTRA_OPTION_WEB_APPLICATION_NAME ) ) {
      path = connection.getDatabaseName() + "/" + URI_PATH;
    } else {
      path = URI_PATH;
    }
    if ( isEmpty( connection.getDatabasePort() ) ) {
      return getNativeJdbcPre() + connection.getHostname() + "/" + path;
    } else {
      return getNativeJdbcPre() + connection.getHostname() + ":" + connection.getDatabasePort() + "/" + path;
    }
  }

  @Override
  public String getNativeJdbcPre() {
    return "jdbc:pdi://";
  }

  @Override
  public String getSQLLockTables( String[] tableNames ) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getSQLUnlockTables( String[] tableName ) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean supportsBooleanDataType() {
    return true;
  }

  @Override
  public boolean supportsViews() {
    return false;
  }

  @Override
  public boolean supportsSynonyms() {
    return false;
  }
}
