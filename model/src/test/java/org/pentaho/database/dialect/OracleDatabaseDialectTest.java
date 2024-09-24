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

import junit.framework.Assert;
import org.junit.Test;
import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.DatabaseConnection;
import org.pentaho.database.model.IDatabaseType;

public class OracleDatabaseDialectTest {

  private OracleDatabaseDialect dialect;

  public OracleDatabaseDialectTest() {
    this.dialect = new OracleDatabaseDialect();
  }

  @Test
  public void testGetNativeDriver() {
    Assert.assertEquals( dialect.getNativeDriver(), "oracle.jdbc.driver.OracleDriver" );
  }

  @Test
  public void testGetURL() throws Exception {
    DatabaseConnection conn = new DatabaseConnection();
    conn.setAccessType( DatabaseAccessType.NATIVE );
    Assert.assertEquals( dialect.getURL( conn ), "jdbc:oracle:thin:@null" );
  }

  @Test
  public void testGetUsedLibraries() {
    Assert.assertEquals( dialect.getUsedLibraries()[0], "ojdbc14.jar" );
  }

  @Test
  public void testGetNativeJdbcPre() {
    Assert.assertEquals( dialect.getNativeJdbcPre(), "jdbc:oracle:thin:@" );
  }

  @Test
  public void testGetDatabaseType() {
    IDatabaseType dbType = dialect.getDatabaseType();
    Assert.assertEquals( dbType.getName(), "Oracle" );
  }

  @Test
  public void testGetReservedWords() {
    Assert.assertTrue( dialect.getReservedWords().length > 0 );
  }

  @Test
  public void testSupportsBitmapIndex() {
    Assert.assertTrue( dialect.supportsBitmapIndex() );
  }

  @Test
  public void testGetTruncateTableStatement() {
    String tableName = "table1";
    Assert.assertEquals( dialect.getTruncateTableStatement( tableName ), "TRUNCATE TABLE " + tableName );
  }

  @Test
  public void testGetSQLUnlockTables() {
    Assert.assertNull( dialect.getSQLUnlockTables( null ) );
  }

  @Test
  public void testGetSQLListOfProcedures() {
    Assert.assertEquals( dialect.getSQLListOfProcedures( null ),
        "SELECT DISTINCT DECODE(package_name, NULL, '', package_name||'.')||object_name FROM user_arguments" );

  }

  @Test
  public void testSupportsSynonyms() {
    Assert.assertTrue( dialect.supportsSynonyms() );
  }

  @Test
  public void testUseSchemaNameForTableList() {
    Assert.assertTrue( dialect.useSchemaNameForTableList() );
  }

  @Test
  public void testSupportsSequences() {
    Assert.assertTrue( dialect.supportsSequences() );
  }

  @Test
  public void testSupportsOptionsInURL() {
    Assert.assertFalse( dialect.supportsOptionsInURL() );
  }

  @Test
  public void testNeedsToLockAllTables() {
    Assert.assertFalse( dialect.needsToLockAllTables() );
  }

  @Test
  public void testSupportsAutoInc() {
    Assert.assertFalse( dialect.supportsAutoInc() );
  }

  @Test
  public void testGetLimitClause() {
    int nrRows = 5;
    Assert.assertEquals( dialect.getLimitClause( nrRows ), " WHERE ROWNUM <= " + nrRows );
  }

  @Test
  public void testGetSQLSequenceExists() {
    String sequenceName = "sequence";
    Assert.assertEquals( dialect.getSQLSequenceExists( sequenceName ),
        "SELECT * FROM USER_SEQUENCES WHERE SEQUENCE_NAME = '" + sequenceName.toUpperCase() + "'" );
  }

  @Test
  public void testGetSQLCurrentSequenceValue() {
    String sequenceName = "sequence";
    Assert.assertEquals( dialect.getSQLCurrentSequenceValue( sequenceName ),
        "SELECT " + sequenceName + ".currval FROM DUAL" );

  }

  @Test
  public void getSQLNextSequenceValue() {
    String sequenceName = "sequence";
    Assert.assertEquals( dialect.getSQLNextSequenceValue( sequenceName ),
        "SELECT " + sequenceName + ".nextval FROM dual" );
  }

  @Test
  public void testGetSQLNextSequenceValue() {
    String sequenceName = "sequence";
    Assert.assertEquals( dialect.getSQLNextSequenceValue( sequenceName ),
        "SELECT " + sequenceName + ".nextval FROM dual" );
  }

  @Test
  public void testGetSQLQueryFields() {
    String tableName = "table";
    Assert.assertEquals( dialect.getSQLQueryFields( tableName ),
        "SELECT /*+FIRST_ROWS*/ * FROM " + tableName + " WHERE ROWNUM < 1" );
  }

  @Test
  public void testGetSQLQueryColumnFields() {
    String tableName = "table";
    String columnName = "column";
    Assert.assertEquals( dialect.getSQLQueryColumnFields( tableName, columnName ),
        "SELECT /*+FIRST_ROWS*/ " + tableName + " FROM " + columnName + " WHERE ROWNUM < 1" );
  }
}
