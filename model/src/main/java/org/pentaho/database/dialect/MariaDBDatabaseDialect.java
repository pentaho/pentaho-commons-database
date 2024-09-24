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

import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.DatabaseType;
import org.pentaho.database.model.IDatabaseType;

public class MariaDBDatabaseDialect extends MySQLDatabaseDialect {

  private static final long serialVersionUID = 8237047662096530976L;
  private static final IDatabaseType DBTYPE = new DatabaseType( "MariaDB", "MARIADB", DatabaseAccessType.getList(
      DatabaseAccessType.NATIVE, DatabaseAccessType.ODBC, DatabaseAccessType.JNDI ), 3306,
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
