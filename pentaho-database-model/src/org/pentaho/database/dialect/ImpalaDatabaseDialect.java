/*
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
 * Copyright 2008-2013 Pentaho Corporation.  All rights reserved.
 *
 */
package org.pentaho.database.dialect;

import org.pentaho.database.DatabaseDialectException;
import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.DatabaseConnection;
import org.pentaho.database.model.DatabaseType;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.model.IDatabaseType;

public class ImpalaDatabaseDialect extends Hive2DatabaseDialect {

  public ImpalaDatabaseDialect() {
    super();
  }

  /**
   * UID for serialization
   */
  private static final long serialVersionUID = -6685869374136347923L;

  private static final int DEFAULT_PORT = 21050;

  private static final IDatabaseType DBTYPE =
    new DatabaseType( "Impala", "IMPALA", DatabaseAccessType.getList( DatabaseAccessType.NATIVE,
      DatabaseAccessType.JNDI ), DEFAULT_PORT,
      "http://www.cloudera.com/content/support/en/documentation/cloudera-impala/cloudera-impala-documentation-v1"
        + "-latest.html" );

  public IDatabaseType getDatabaseType() {
    return DBTYPE;
  }

  @Override
  public String getNativeDriver() {
    return "org.apache.hive.jdbc.ImpalaDriver";
  }

  @Override
  public String getURL( IDatabaseConnection connection ) throws DatabaseDialectException {
    StringBuffer urlBuffer = new StringBuffer( getNativeJdbcPre() );
    /*
     * String username = connection.getUsername(); if(username != null && !"".equals(username)) {
     * urlBuffer.append(username); String password = connection.getPassword(); if(password != null &&
     * !"".equals(password)) { urlBuffer.append(":"); urlBuffer.append(password); } urlBuffer.append("@"); }
     */
    urlBuffer.append( connection.getHostname() );
    urlBuffer.append( ":" );
    urlBuffer.append( connection.getDatabasePort() );
    urlBuffer.append( "/" );
    urlBuffer.append( connection.getDatabaseName() );

    String principalPropertyName = getDatabaseType().getShortName() + ".principal";
    String principal = connection.getExtraOptions().get( principalPropertyName );
    String extraPrincipal =
      connection.getAttributes().get( DatabaseConnection.ATTRIBUTE_PREFIX_EXTRA_OPTION + principalPropertyName );
    if ( principal != null || extraPrincipal != null ) {
      return urlBuffer.toString();
    }

    urlBuffer.append( ";auth=noSasl" );
    return urlBuffer.toString();
  }

  @Override
  public String getNativeJdbcPre() {
    return "jdbc:hive2://";
  }

  @Override
  public String[] getUsedLibraries() {
    return new String[] { "pentaho-hadoop-hive-jdbc-shim-1.4-SNAPSHOT.jar" };
  }

  @Override
  public int getDefaultDatabasePort() {
    return DEFAULT_PORT;
  }

}
