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
