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

import org.apache.commons.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.pentaho.database.IDatabaseDialect;
import org.pentaho.database.dialect.AbstractDatabaseDialect;
import org.pentaho.database.model.IDatabaseType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by bryan on 5/9/16.
 */
@RunWith( MockitoJUnitRunner.class )
public class ServiceLoaderDatabaseDialectProviderTest {
  List<IDatabaseDialect> dialects;
  @Mock AbstractDatabaseDialect availableDialect;
  @Mock IDatabaseType availableDatabaseType;
  @Mock AbstractDatabaseDialect unavailableDialect;
  @Mock IDatabaseType unavailableDatabaseType;
  private ServiceLoaderDatabaseDialectProvider dialectProvider;

  @Before
  public void setup() {
    when( availableDialect.isUsable() ).thenReturn( true );
    when( availableDialect.getDatabaseType() ).thenReturn( availableDatabaseType );
    when( unavailableDialect.isUsable() ).thenReturn( false );
    when( unavailableDialect.getDatabaseType() ).thenReturn( unavailableDatabaseType );
    dialects = new ArrayList<>( Arrays.asList( availableDialect, unavailableDialect ) );
    Function<Class<IDatabaseDialect>, Iterable<IDatabaseDialect>> loaderFunction = mock( Function.class );
    when( loaderFunction.apply( IDatabaseDialect.class ) ).thenReturn( dialects );
    dialectProvider = new ServiceLoaderDatabaseDialectProvider( loaderFunction );
  }

  @Test
  public void testGetDialectsUsable() {
    Collection<IDatabaseDialect> dialects = dialectProvider.getDialects( true );
    assertEquals( 1, dialects.size() );
    assertEquals( availableDialect, dialects.iterator().next() );
  }

  @Test
  public void testGetDialectsAll() {
    Collection<IDatabaseDialect> dialects = dialectProvider.getDialects( false );
    assertEquals( 2, dialects.size() );
    Iterator<IDatabaseDialect> iterator = dialects.iterator();
    assertEquals( availableDialect, iterator.next() );
    assertEquals( unavailableDialect, iterator.next() );
  }

  @Test
  public void testGetDialectUsable() {
    assertEquals( availableDialect, dialectProvider.getDialect( true, availableDatabaseType ) );
    assertNull( dialectProvider.getDialect( true, unavailableDatabaseType ) );
  }

  @Test
  public void testGetDialectAll() {
    assertEquals( availableDialect, dialectProvider.getDialect( false, availableDatabaseType ) );
    assertEquals( unavailableDialect, dialectProvider.getDialect( false, unavailableDatabaseType ) );
    assertNull( dialectProvider.getDialect( false, mock( IDatabaseType.class ) ) );
  }

  @Test
  public void testUsableFilterNoLog() {
    Log log = mock( Log.class );
    when( log.isDebugEnabled() ).thenReturn( false );
    AbstractDatabaseDialect dialect = mock( AbstractDatabaseDialect.class );
    IDatabaseDialect iDatabaseDialect = mock( IDatabaseDialect.class );
    when( iDatabaseDialect.getNativeDriver() ).thenReturn( Object.class.getCanonicalName() ).thenReturn( "fake.class" );
    when( dialect.isUsable() ).thenReturn( true ).thenReturn( false );
    Predicate<IDatabaseDialect> filter = dialectProvider.usableFilter( log );
    assertTrue( filter.test( dialect ) );
    assertFalse( filter.test( dialect ) );
    assertTrue( filter.test( iDatabaseDialect ) );
    assertFalse( filter.test( iDatabaseDialect ) );
    verify( log, never() ).debug( anyString() );
  }

  @Test
  public void testUsableFilterLog() {
    Log log = mock( Log.class );
    when( log.isDebugEnabled() ).thenReturn( true );
    AbstractDatabaseDialect dialect = mock( AbstractDatabaseDialect.class );
    IDatabaseType type = mock( IDatabaseType.class );
    IDatabaseDialect iDatabaseDialect = mock( IDatabaseDialect.class );
    when( iDatabaseDialect.getNativeDriver() ).thenReturn( Object.class.getCanonicalName() );
    when( iDatabaseDialect.getDatabaseType() ).thenReturn( type );
    when( dialect.getDatabaseType() ).thenReturn( type );
    when( dialect.isUsable() ).thenReturn( true ).thenReturn( false );
    Predicate<IDatabaseDialect> filter = dialectProvider.usableFilter( log );
    assertTrue( filter.test( dialect ) );
    verify( log, times( 1 ) ).debug( anyString() );
    assertFalse( filter.test( dialect ) );
    verify( log, times( 3 ) ).debug( anyString() );
    assertTrue( filter.test( iDatabaseDialect ) );
    verify( log, times( 4 ) ).debug( anyString() );
    iDatabaseDialect = mock( IDatabaseDialect.class );
    when( iDatabaseDialect.getNativeDriver() ).thenReturn( "fake.class" );
    when( iDatabaseDialect.getDatabaseType() ).thenReturn( type );
    assertFalse( filter.test( iDatabaseDialect ) );
    verify( log, times( 6 ) ).debug( anyString() );
  }
}
