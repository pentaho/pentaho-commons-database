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

package org.pentaho.database.service;

import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import org.pentaho.database.IDatabaseDialect;
import org.pentaho.database.model.IDatabaseType;

@SuppressWarnings( "nls" ) public class DatabaseDialectServiceTest {

  @Test public void testClassExistsCheck() {
    // validated drivers check
    boolean mssqlExists = false;
    DatabaseDialectService dialectService = new DatabaseDialectService();
    for ( IDatabaseType type : dialectService.getDatabaseTypes() ) {
      if ( type.getShortName().equals( "MSSQL" ) ) {
        mssqlExists = true;
        break;
      }
    }

    Assert.assertFalse( "MSSQL jTDS Driver should not be available, because it is not on the classpath", mssqlExists );

    // skip validation on drivers
    dialectService = new DatabaseDialectService( false );
    for ( IDatabaseType type : dialectService.getDatabaseTypes() ) {
      if ( type.getShortName().equals( "MSSQL" ) ) {
        mssqlExists = true;
        break;
      }
    }

    Assert
        .assertTrue( "MSSQL jTDS Driver should be available, because we disabled checking the classpath", mssqlExists );
  }

  @Test public void testValidateJdbcDriverClass() {
    DatabaseDialectService dialectService = new DatabaseDialectService();
    boolean checkForBogusClass = dialectService.validateJdbcDriverClassExists( "this.is.a.bogus.class.Name" );
    Assert.assertFalse( "The check for a bogus class should not succeed", checkForBogusClass );
    boolean checkForRealClass = dialectService.validateJdbcDriverClassExists( this.getClass().getName() );
    Assert.assertTrue( "The check for this class should succeed since it's running.", checkForRealClass );
  }

  @Test public void testGetDatabaseTypes() {
    DatabaseDialectService dialectService = new DatabaseDialectService();
    List<IDatabaseType> dbTypes = dialectService.getDatabaseTypes();
    boolean foundGeneric = false;
    for ( IDatabaseType dbType : dbTypes ) {
      if ( dbType.getShortName().equals( "GENERIC" ) ) {
        foundGeneric = true;
        break;
      }
    }
    Assert.assertTrue( "When running as a test case, there should minimally be the generic type in this list.",
        foundGeneric );
    dialectService = new DatabaseDialectService( false );
    dbTypes = dialectService.getDatabaseTypes();
    Assert.assertNotNull( "dbTypes should not come back null.", dbTypes );
    Assert.assertTrue(
        "There should minimally be found 15 types here based on the classes in org.pentaho.database.dialect",
        ( dbTypes.size() >= 15 ) );
  }

  @Test public void testGetDatabaseDialects() {
    DatabaseDialectService dialectService = new DatabaseDialectService();
    List<IDatabaseDialect> dbDialects = dialectService.getDatabaseDialects();
    boolean foundGeneric = false;
    for ( IDatabaseDialect dbDialect : dbDialects ) {
      if ( dbDialect.getDatabaseType().getShortName().equals( "GENERIC" ) ) {
        foundGeneric = true;
        break;
      }
    }
    Assert.assertTrue( "When running as a test case, there should minimally be the generic dialect in this list.",
        foundGeneric );
    dialectService = new DatabaseDialectService( false );
    dbDialects = dialectService.getDatabaseDialects();
    Assert.assertNotNull( "dbTypes should not come back null.", dbDialects );
    Assert.assertTrue(
        "There should minimally be found 15 dialects here based on the classes in org.pentaho.database.dialect",
        ( dbDialects.size() >= 15 ) );
  }

  @Test public void testDataIntegrationPresent() {
    DatabaseDialectService dialectService = new DatabaseDialectService( false );
    IDatabaseType kettleThinType = null;
    for ( IDatabaseType dbType : dialectService.getDatabaseTypes() ) {
      if ( dbType.getShortName().equals( "KettleThin" ) || dbType.getShortName().equals( "KETTLETHIN" ) ) {
        kettleThinType = dbType;
        break;
      }
    }
    Assert.assertNotNull( "Should have found the 'KettleThin' DatabaseType", kettleThinType );
    IDatabaseDialect dialect = dialectService.getDialect( kettleThinType );
    Assert.assertEquals( "Types should match", kettleThinType, dialect.getDatabaseType() );
    Assert.assertEquals( "pentaho", kettleThinType.getDefaultDatabaseName() );
    Map<String, String> options = kettleThinType.getDefaultOptions();
    Assert.assertEquals( 0, options.size() );
    System.out.println( kettleThinType );
  }
}
