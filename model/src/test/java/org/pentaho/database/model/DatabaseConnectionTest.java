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


package org.pentaho.database.model;

import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Pavel Sakun
 */
public class DatabaseConnectionTest {
  @Test
  public void testGetDatabaseName() {
    DatabaseConnection connection = mock( DatabaseConnection.class );
    DatabaseType type = mock( DatabaseType.class );
    when( type.getShortName() ).thenReturn( "KettleThin", "Oracle" );
    when( type.getDefaultDatabaseName() ).thenReturn( "default" );
    when( connection.getDatabaseType() ).thenReturn( type );
    when( connection.getDatabaseName() ).thenCallRealMethod();
    Assert.assertEquals( null, connection.getDatabaseName() );
    Assert.assertEquals( "default", connection.getDatabaseName() );
  }
}
