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

import org.pentaho.database.IDatabaseDialect;
import org.pentaho.database.IDatabaseDialectProvider;
import org.pentaho.database.IDriverLocator;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.model.IDatabaseType;
import org.pentaho.database.util.ClassUtil;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DatabaseDialectService implements IDatabaseDialectService {
  private static final List<IDatabaseDialectProvider> providers = Collections.unmodifiableList(
          StreamSupport
                  .stream( ServiceLoader.load( IDatabaseDialectProvider.class,
                          DatabaseDialectService.class.getClassLoader() ).spliterator(), false )
                  .collect( Collectors.toList() ) );

  private final boolean isOnlyReturnAvailable;

  public DatabaseDialectService() {
    this( true );
  }

  public DatabaseDialectService( boolean validateClasses ) {
    this.isOnlyReturnAvailable = validateClasses;
  }

  public void registerDatabaseDialect( IDatabaseDialect databaseDialect ) {
    // Do nothing here - no need to call this anymore
  }

  /**
   * @param databaseDialect
   * @param validateClassExists
   */
  public void registerDatabaseDialect( IDatabaseDialect databaseDialect, boolean validateClassExists ) {
    // Do nothing here - no need to call this anymore
  }

  public boolean validateJdbcDriverClassExists( String classname ) {
    return ClassUtil.canLoadClass( classname ) || providers.stream()
      .flatMap( provider -> provider.getDialects( isOnlyReturnAvailable ).stream() )
      .filter( dialect -> classname.equals( dialect.getNativeDriver() ) )
      .filter( dialect -> dialect instanceof IDriverLocator && ( (IDriverLocator) dialect ).isUsable() )
      .findFirst().isPresent();
  }


  public List<IDatabaseType> getDatabaseTypes() {
    return providers.stream().flatMap( provider -> provider.getDialects( isOnlyReturnAvailable ).stream() )
      .map( IDatabaseDialect::getDatabaseType ).collect( Collectors.toList() );
  }

  public IDatabaseDialect getDialect( IDatabaseType databaseType ) {
    return providers.stream().map( provider -> provider.getDialect( isOnlyReturnAvailable, databaseType ) )
      .filter( Objects::nonNull ).findFirst().orElse( null );
  }

  public IDatabaseDialect getDialect( IDatabaseConnection connection ) {
    return getDialect( connection.getDatabaseType() );
  }

  public List<IDatabaseDialect> getDatabaseDialects() {
    return providers.stream().flatMap( provider -> provider.getDialects( isOnlyReturnAvailable ).stream() )
      .collect( Collectors.toList() );
  }
}
