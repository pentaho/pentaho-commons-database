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
* Copyright (c) 2002 - 2024 Hitachi Vantara..  All rights reserved.
*/

package org.pentaho.database.dialect;

import java.util.HashMap;
import java.util.Map;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.DatabaseConnection;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;


@RunWith( MockitoJUnitRunner.class )
public class PDIDialectTest {

  @Mock
  DatabaseConnection connection;

  private PDIDialect dialect;

  public PDIDialectTest() {
    this.dialect = new PDIDialect();
  }

  @Before
  public void setUp() {
    when( connection.getDatabasePort() ).thenReturn( "8080" );
    when( connection.getHostname() ).thenReturn( "localhost" );
  }

  @Test
  public void testGetURL() throws Exception {
    when( connection.getDatabaseName() ).thenReturn( "pentaho" );
    assertThat( dialect.getURL( connection ), equalTo( "jdbc:pdi://localhost:8080/pentaho/kettle" ) );
    Map attributes = new HashMap();
    attributes.put( "KettleThin.webappname", "pentaho" );
    assertThat( dialect.getURL( connection ), equalTo( "jdbc:pdi://localhost:8080/pentaho/kettle" ) );
  }

  @Test
  public void testGetDatabaseName() throws Exception {
    assertThat( dialect.getDatabaseType().getDefaultDatabaseName(), equalTo( "pentaho" ) );
  }

  @Test
  public void testGetNativeJdbcPre() {
    Assert.assertEquals( dialect.getNativeJdbcPre(), "jdbc:pdi://" );
  }

  @Test
  public void testSupportsSynonyms() {
    Assert.assertFalse( dialect.supportsSynonyms() );
  }

  @Test
  public void testSupportsViews() {
    Assert.assertFalse( dialect.supportsViews() );
  }

  @Test
  public void testSupportsBooleanDataType() {
    Assert.assertTrue( dialect.supportsBooleanDataType() );
  }

  @Test
  public void testGetExtraOptionSeparator() {
    Assert.assertEquals( dialect.getExtraOptionSeparator(), "&" );
  }

  @Test
  public void testGetExtraOptionIndicator() {
    Assert.assertEquals( dialect.getExtraOptionIndicator(), "?" );
  }

  @Test
  public void testSupportsOptionsInURL() {
    Assert.assertTrue( dialect.supportsOptionsInURL() );
  }

  @Test
  public void testGetDefaultPort() throws Exception {
    assertEquals( 8080, dialect.getDatabaseType().getDefaultDatabasePort() );
  }
}
