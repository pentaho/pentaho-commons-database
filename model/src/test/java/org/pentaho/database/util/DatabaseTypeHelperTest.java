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


package org.pentaho.database.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.pentaho.database.dialect.MSSQLServerDatabaseDialect;
import org.pentaho.database.dialect.MSSQLServerNativeDatabaseDialect;
import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.DatabaseType;
import org.pentaho.database.model.IDatabaseType;


public class DatabaseTypeHelperTest {

  @Test
  public void getDatabaseTypeByShortName() {
    MSSQLServerNativeDatabaseDialect mssqlNATIVE  = Mockito.spy( new MSSQLServerNativeDatabaseDialect() );
    Mockito.doReturn( new DatabaseType( "MS SQL Server (Native)", "MSSQLNATIVE",
        DatabaseAccessType.getList( DatabaseAccessType.NATIVE, DatabaseAccessType.ODBC, DatabaseAccessType.JNDI ), 1433,
        "http://msdn.microsoft.com/en-us/library/ms378428.aspx" ) ).when( mssqlNATIVE ).getDatabaseType();
    List<IDatabaseType> databaseTypes = new ArrayList<IDatabaseType>();
    databaseTypes.add( ( new MSSQLServerDatabaseDialect() ).getDatabaseType() );
    databaseTypes.add( mssqlNATIVE.getDatabaseType() );
    DatabaseTypeHelper dbt = new DatabaseTypeHelper( databaseTypes );
    Assert.assertNotNull( "MSSQLNATIVE", dbt.getDatabaseTypeByShortName( "MSSQLNATIVE" ) );
    Assert.assertNotNull( "MSSQLNATIVE", dbt.getDatabaseTypeByShortName( "MSSQLNative" ) );

    MSSQLServerNativeDatabaseDialect mssqlNative  = Mockito.spy( new MSSQLServerNativeDatabaseDialect() );
    Mockito.doReturn( new DatabaseType( "MS SQL Server (Native)", "MSSQLNative",
        DatabaseAccessType.getList( DatabaseAccessType.NATIVE, DatabaseAccessType.ODBC, DatabaseAccessType.JNDI ), 1433,
        "http://msdn.microsoft.com/en-us/library/ms378428.aspx" ) ).when( mssqlNative ).getDatabaseType();
    databaseTypes = new ArrayList<IDatabaseType>();
    databaseTypes.add( ( new MSSQLServerDatabaseDialect() ).getDatabaseType() );
    databaseTypes.add( mssqlNative.getDatabaseType() );
    dbt = new DatabaseTypeHelper( databaseTypes );
    Assert.assertNotNull( "MSSQLNative", dbt.getDatabaseTypeByShortName( "MSSQLNATIVE" ) );
    Assert.assertNotNull( "MSSQLNative", dbt.getDatabaseTypeByShortName( "MSSQLNative" ) );
  }
}
