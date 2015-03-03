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
 * Copyright (c) 2002-2014 Pentaho Corporation..  All rights reserved.
 */

package org.pentaho.database.dialect;

import org.pentaho.database.DatabaseDialectException;
import org.pentaho.database.IValueMeta;
import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.DatabaseType;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.model.IDatabaseType;

import java.util.Collections;
import java.util.HashMap;

public class PDIDialect extends GenericDatabaseDialect {

  private static final long serialVersionUID = -661020279493753135L;

  private static final IDatabaseType DBTYPE =
    new DatabaseType( "Pentaho Data Integration", "KettleThin",
      DatabaseAccessType.getList( DatabaseAccessType.NATIVE, DatabaseAccessType.JNDI ),
      9080,
      "https://help.pentaho.com/Documentation/5.1/0L0/0Y0/0G0",
      "kettle",  // Default database name
      Collections.unmodifiableMap(
        new HashMap<String, String>() {
          {
            put( "KettleThin.webappname", "pentaho-di" ); // Default options
          }
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
    if ( isEmpty( connection.getDatabasePort() ) ) {
      return getNativeJdbcPre() + connection.getHostname() + "/" + connection.getDatabaseName();
    } else {
      return getNativeJdbcPre() + connection.getHostname() + ":" + connection.getDatabasePort() + "/"
        + connection.getDatabaseName();
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
