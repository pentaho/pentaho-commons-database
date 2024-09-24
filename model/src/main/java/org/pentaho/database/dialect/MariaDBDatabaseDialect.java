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

import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.DatabaseType;
import org.pentaho.database.model.IDatabaseType;

public class MariaDBDatabaseDialect extends MySQLDatabaseDialect {

  private static final long serialVersionUID = 8237047662096530976L;
  private static final IDatabaseType DBTYPE = new DatabaseType( "MariaDB", "MARIADB", DatabaseAccessType.getList(
      DatabaseAccessType.NATIVE, DatabaseAccessType.JNDI ), 3306,
      "https://mariadb.com/kb/en/mariadb/about-mariadb-connector-j/" );

  public MariaDBDatabaseDialect() {
  }

  public IDatabaseType getDatabaseType() {
    return DBTYPE;
  }

  public String getNativeDriver() {
    return "org.mariadb.jdbc.Driver";
  }

  public String getNativeJdbcPre() {
    return "jdbc:mariadb://";
  }

  @Override
  public String[] getUsedLibraries() {
    return new String[] { "mariadb-java-client-1.4.6.jar" };
  }

}
