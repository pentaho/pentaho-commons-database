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

package org.pentaho.database.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Assert;

import org.mockito.Mockito;
import static org.mockito.Mockito.any;

import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.model.IDatabaseType;
import org.pentaho.di.core.database.BaseDatabaseMeta;
import org.pentaho.di.core.database.DatabaseInterface;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.plugins.ClassLoadingPluginInterface;
import org.pentaho.di.core.plugins.DatabasePluginType;
import org.pentaho.di.core.plugins.Plugin;
import org.pentaho.di.core.plugins.PluginRegistry;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DatabaseUtilTest {

  private static final String DBTYPE = "DBTYPE";

  @SuppressWarnings( { "unchecked", "serial" } )
  private static void registerDBPlugin( String dbName ) throws Exception {
    Properties props = new Properties();

    DatabaseInterface dbIface = Mockito.mock( BaseDatabaseMeta.class, Mockito.withSettings().extraInterfaces( Cloneable.class ) );
    Class<?> dbIfaceClass = dbIface.getClass();
    Mockito.when( dbIface.getDatabaseName() ).thenReturn( dbName );
    Mockito.when( dbIface.clone() ).thenReturn( dbIface );
    Mockito.when( dbIface.getAttributes() ).thenReturn( props );
    Mockito.when( dbIface.getConnectionPoolingProperties() ).thenCallRealMethod();
    Mockito.when( dbIface.getExtraOptions() ).thenCallRealMethod();
    Mockito.doCallRealMethod().when( dbIface ).setAttributes( any( Properties.class ) );
    dbIface.setAttributes( new Properties() );

    final Plugin dbPlugin = Mockito.mock( Plugin.class, Mockito.withSettings().extraInterfaces( ClassLoadingPluginInterface.class ) );
    Mockito.doReturn( dbIfaceClass ).when( dbPlugin ).getMainType();
    Mockito.when( dbPlugin.getIds() ).thenReturn( new String[] { dbName } );
    Mockito.when( dbPlugin.getName() ).thenReturn( dbName );
    Mockito.when( dbPlugin.matches( dbName ) ).thenReturn( true );
    Mockito.when( dbPlugin.getClassMap() ).thenReturn(
        new HashMap<Class<?>, String>() { { put( dbPlugin.getClass(), "org.pentaho.di.core.database.InfobrightDatabaseMeta" ); } } );
    Mockito.doReturn( dbIface ).when( ( (ClassLoadingPluginInterface) dbPlugin ) ).loadClass( any( Class.class ) );

    PluginRegistry.getInstance().registerPlugin(
        DatabasePluginType.class,
        dbPlugin );
  }

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    PluginRegistry.init();
    PluginRegistry.getInstance().registerPluginType( DatabasePluginType.class );

    registerDBPlugin( "Oracle" );
    registerDBPlugin( "MYSQL" );
    registerDBPlugin( DBTYPE );
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @SuppressWarnings( "serial" )
  @Test
  public void testConvertToDatabaseMeta() {
    IDatabaseConnection conn = Mockito.mock( IDatabaseConnection.class );

    Map<String, String> attributes = new HashMap<String, String>() {
      {
        put( "ATTR_NAME_1", "ATTR_VALUE_1" );
        put( BaseDatabaseMeta.ATTRIBUTE_PREFIX_EXTRA_OPTION + DBTYPE + ".extra_opt_override", "extra_opt_override_attr" );
        put( BaseDatabaseMeta.ATTRIBUTE_PREFIX_EXTRA_OPTION + DBTYPE + ".extra_opt_no_override", "extra_opt_no_override_attr" );
        put( BaseDatabaseMeta.ATTRIBUTE_POOLING_PARAMETER_PREFIX + "pooling_opt_override", "pooling_opt_override_attr" );
        put( BaseDatabaseMeta.ATTRIBUTE_POOLING_PARAMETER_PREFIX + "pooling_opt_no_override", "pooling_opt_no_override_attr" );
      }
    };

    Map<String, String> extra = new HashMap<String, String>() {
      {
        put( DBTYPE + ".extra_opt_override", "extra_opt_val_override_extra" );
      }
    };

    Map<String, String> pool = new HashMap<String, String>() {
      {
        put( "pooling_opt_override", "pooling_opt_override_pool" );
      }
    };

    Mockito.when( conn.getAttributes() ).thenReturn( attributes );
    Mockito.when( conn.getExtraOptions() ).thenReturn( extra );
    Mockito.when( conn.getConnectionPoolingProperties() ).thenReturn( pool );
    Mockito.when( conn.getAccessType() ).thenReturn( DatabaseAccessType.NATIVE );

    IDatabaseType dbType = Mockito.mock( IDatabaseType.class );
    Mockito.when( dbType.getShortName() ).thenReturn( DBTYPE );
    Mockito.when( conn.getDatabaseType() ).thenReturn( dbType );

    DatabaseMeta meta = DatabaseUtil.convertToDatabaseMeta( conn );

    Assert.assertNotNull( meta );

    Properties attrs = meta.getAttributes();

    // Check generic attributes
    Assert.assertEquals( "ATTR_VALUE_1", attrs.getProperty( "ATTR_NAME_1" ) );

    // Check extra attributes as part of generic with prefixes
    Assert.assertEquals( "extra_opt_val_override_extra",
        attrs.getProperty( BaseDatabaseMeta.ATTRIBUTE_PREFIX_EXTRA_OPTION + DBTYPE + ".extra_opt_override" ) );
    Assert.assertEquals( "extra_opt_no_override_attr",
        attrs.getProperty( BaseDatabaseMeta.ATTRIBUTE_PREFIX_EXTRA_OPTION + DBTYPE + ".extra_opt_no_override" ) );

    // Check pooling attributes as part of generic with prefixes
    Assert.assertEquals( "pooling_opt_override_pool",
        attrs.getProperty( BaseDatabaseMeta.ATTRIBUTE_POOLING_PARAMETER_PREFIX + "pooling_opt_override" ) );
    Assert.assertEquals( "pooling_opt_no_override_attr",
        attrs.getProperty( BaseDatabaseMeta.ATTRIBUTE_POOLING_PARAMETER_PREFIX + "pooling_opt_no_override" ) );

    // Check pooling attributes as a separate set w/o prefixes
    Properties poolProps = meta.getConnectionPoolingProperties();
    Assert.assertEquals( "pooling_opt_override_pool", poolProps.getProperty( "pooling_opt_override" ) );
    Assert.assertEquals( "pooling_opt_no_override_attr", poolProps.getProperty( "pooling_opt_no_override" ) );

    // Check extra attributes as a separate set w/o prefixes
    Map<String, String> extraProps = meta.getExtraOptions();
    Assert.assertEquals( "extra_opt_val_override_extra", extraProps.get( DBTYPE + ".extra_opt_override" ) );
    Assert.assertEquals( "extra_opt_no_override_attr", extraProps.get( DBTYPE + ".extra_opt_no_override" ) );
  }

}
