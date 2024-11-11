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

package org.pentaho.database.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.database.IDatabaseDialect;
import org.pentaho.database.IDatabaseDialectProvider;
import org.pentaho.database.IDriverLocator;
import org.pentaho.database.model.IDatabaseType;
import org.pentaho.database.util.ClassUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by bryan on 5/6/16.
 */
public class ServiceLoaderDatabaseDialectProvider implements IDatabaseDialectProvider {
  private static final Log LOG = LogFactory.getLog( DatabaseDialectService.class );
  /*
   * Note - keeping two different list groups for simplicity. The number of valid / provided dialects
   * is low - so it shouldn't balloon memory. If this becomes a single data structure for all
   * the indexes and such would be simple to do.
   *
   *   validXXXXXX - These are dialects for which we found JDBC Drivers in the environment
   *   allXXXXX    - These are dialects which we could find using the ServiceLoader
   *
   *   Marc
   */
  private final List<IDatabaseDialect> usableDialects;
  private final Map<IDatabaseType, IDatabaseDialect> usableDialectTypeMap;
  private List<IDatabaseDialect> allDialects;
  private Map<IDatabaseType, IDatabaseDialect> allDialectsTypeMap;

  public ServiceLoaderDatabaseDialectProvider() {
    this( databaseDialectClass ->
            ServiceLoader.load(
                    databaseDialectClass, ServiceLoaderDatabaseDialectProvider.class.getClassLoader()
            ) );
  }

  public ServiceLoaderDatabaseDialectProvider(
    Function<Class<IDatabaseDialect>, Iterable<IDatabaseDialect>> loaderFunction ) {
    Stream<IDatabaseDialect> databaseDialectStream =
      StreamSupport.stream( loaderFunction.apply( IDatabaseDialect.class ).spliterator(), false );
    allDialects = Collections.unmodifiableList( databaseDialectStream.collect( Collectors.toList() ) );
    allDialectsTypeMap = Collections.unmodifiableMap(
      allDialects.stream().collect( Collectors.toMap( IDatabaseDialect::getDatabaseType, Function.identity() ) ) );

    usableDialects = Collections
      .unmodifiableList( allDialects.stream().filter( usableFilter( LOG ) ).collect( Collectors.toList() ) );
    usableDialectTypeMap = Collections.unmodifiableMap( usableDialects.stream()
      .collect( Collectors.toMap( IDatabaseDialect::getDatabaseType, Function.identity() ) ) );
  }

  @Override public Collection<IDatabaseDialect> getDialects( boolean usableOnly ) {
    return usableOnly ? usableDialects : allDialects;
  }

  @Override public IDatabaseDialect getDialect( boolean usableOnly, IDatabaseType databaseType ) {
    return usableOnly ? usableDialectTypeMap.get( databaseType ) : allDialectsTypeMap.get( databaseType );
  }

  Predicate<IDatabaseDialect> usableFilter( Log logger ) {
    if ( logger.isDebugEnabled() ) {
      return dialect -> {
        logger.debug( String.format( "Checking for presence of %s ( %s )", dialect.getDatabaseType().getName(),
          dialect.getNativeDriver() ) );
        boolean result = false;
        if ( dialect instanceof IDriverLocator ) {
          result = ( (IDriverLocator) dialect ).isUsable();
        } else if ( ClassUtil.canLoadClass( dialect.getNativeDriver() ) ) {
          result = true;
        }
        if ( !result ) {
          logger.debug( String.format( "%s not detected.", dialect.getDatabaseType().getName() ) );
        }
        return result;
      };
    }
    return dialect -> {
      if ( dialect instanceof IDriverLocator ) {
        return ( (IDriverLocator) dialect ).isUsable();
      } else if ( ClassUtil.canLoadClass( dialect.getNativeDriver() ) ) {
        return true;
      }
      return false;
    };
  }
}
