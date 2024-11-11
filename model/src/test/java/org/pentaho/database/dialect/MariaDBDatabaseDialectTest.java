/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2029-07-20
 ******************************************************************************/


package org.pentaho.database.dialect;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.DatabaseConnection;
import org.pentaho.database.model.IDatabaseType;

public class MariaDBDatabaseDialectTest extends MySQLDatabaseDialectTest {

  private MariaDBDatabaseDialect dialect;

  public MariaDBDatabaseDialectTest() {
    this.dialect = new MariaDBDatabaseDialect();
  }

  @Test
  public void testGetNativeDriver() {
    assertEquals( dialect.getNativeDriver(), "org.mariadb.jdbc.Driver" );
  }

  @Test
  public void testGetURL() throws Exception {
    DatabaseConnection conn = new DatabaseConnection();
    conn.setAccessType( DatabaseAccessType.NATIVE );
    assertEquals( dialect.getURL( conn ), "jdbc:mariadb://null/null" );
  }

  @Test
  public void testGetUsedLibraries() {
    assertEquals( dialect.getUsedLibraries()[0], "mariadb-java-client-1.4.6.jar" );
  }

  @Test
  public void testGetNativeJdbcPre() {
    assertEquals( dialect.getNativeJdbcPre(), "jdbc:mariadb://" );
  }

  @Test
  public void testGetDatabaseType() {
    IDatabaseType dbType = dialect.getDatabaseType();
    assertEquals( dbType.getName(), "MariaDB" );
  }

}
