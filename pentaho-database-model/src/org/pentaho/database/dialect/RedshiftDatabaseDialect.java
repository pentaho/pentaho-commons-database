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
 * Copyright (c) 2002-2015 Pentaho Corporation..  All rights reserved.
 * 
 * @Author: Marc Batchelor
 */
package org.pentaho.database.dialect;

import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.DatabaseType;
import org.pentaho.database.model.IDatabaseType;

public class RedshiftDatabaseDialect extends PostgreSQLDatabaseDialect {

  private static final long serialVersionUID = 7855404769773045690L;
  
  private static final IDatabaseType DBTYPE = new DatabaseType( "Redshift", "REDSHIFT", DatabaseAccessType.getList(
      DatabaseAccessType.NATIVE, DatabaseAccessType.ODBC, DatabaseAccessType.JNDI ), 5439,
      "http://http://docs.aws.amazon.com/redshift/latest/mgmt/configure-jdbc-connection.html" );
  
  public RedshiftDatabaseDialect() {
    // TODO Auto-generated constructor stub
  }
  
  public IDatabaseType getDatabaseType() {
    return DBTYPE;
  }

  @Override
  public String getNativeDriver() {
    return "com.amazon.redshift.jdbc4.Driver";
  }

  @Override
  public String getNativeJdbcPre() {
    return "jdbc:redshift://";
  }
  
  @Override
  public String[] getUsedLibraries() {
    return new String[] { "RedshiftJDBC4_1.0.10.1010.jar" };
  }
 
}
