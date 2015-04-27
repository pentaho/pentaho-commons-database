/*
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
 * Copyright 2008-2013 Pentaho Corporation.  All rights reserved.
 *
 */
package org.pentaho.database.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.database.IDatabaseDialect;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.model.IDatabaseType;

public class DatabaseDialectService implements IDatabaseDialectService{
  
  private static ServiceLoader<IDatabaseDialect> dialectLoader = ServiceLoader.load(IDatabaseDialect.class);
  
  private static final Log logger = LogFactory.getLog(DatabaseDialectService.class);
  
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
  private static final List<IDatabaseDialect> validDatabaseDialects = new ArrayList<IDatabaseDialect>();
  private static final List<IDatabaseType> validDatabaseTypes = new ArrayList<IDatabaseType>();
  private static final Map<IDatabaseType, IDatabaseDialect> validTypeToDialectMap = new HashMap<IDatabaseType, IDatabaseDialect>();

  private static final List<IDatabaseDialect> allDatabaseDialects = new ArrayList<IDatabaseDialect>();
  private static final List<IDatabaseType> allDatabaseTypes = new ArrayList<IDatabaseType>();
  private static final Map<IDatabaseType, IDatabaseDialect> allTypeToDialectMap = new HashMap<IDatabaseType, IDatabaseDialect>();

  private boolean isOnlyReturnAvailable = true; 
  
  static {
    DatabaseDialectService.validateAndCatalogServices();
    if (logger.isDebugEnabled()) {
      logger.debug( "Valid databaseDialects list ..." );
      for ( IDatabaseDialect dialect : validDatabaseDialects ) {
        logger.debug(String.format( " ... %s ( %s )", dialect.getDatabaseType().getName(), dialect.getDatabaseType().getShortName() ) );
      }
      logger.debug( "All databaseDialects list ..." );
      for ( IDatabaseDialect dialect : allDatabaseDialects ) {
        logger.debug(String.format( " ... %s ( %s )", dialect.getDatabaseType().getName(), dialect.getDatabaseType().getShortName() ) );
      }
    }
  }
  private static void validateAndCatalogServices() {
    for (IDatabaseDialect dialect : DatabaseDialectService.dialectLoader) {
      if (logger.isDebugEnabled()) {
        logger.debug( String.format("Checking for presence of %s ( %s )", dialect.getDatabaseType().getName(), dialect.getNativeDriver() )  );
      }
      // Catalog them all
      DatabaseDialectService.allDatabaseTypes.add(dialect.getDatabaseType());
      DatabaseDialectService.allTypeToDialectMap.put(dialect.getDatabaseType(), dialect);
      DatabaseDialectService.allDatabaseDialects.add(dialect);
      if ( validateJdbcDriverClass(dialect.getNativeDriver())) {
        DatabaseDialectService.validDatabaseTypes.add(dialect.getDatabaseType());
        DatabaseDialectService.validTypeToDialectMap.put(dialect.getDatabaseType(), dialect);
        DatabaseDialectService.validDatabaseDialects.add(dialect);
      } else {
        if (logger.isDebugEnabled()) {
          logger.debug( String.format("%s not detected.", dialect.getDatabaseType().getName()) );
        }
        
      }
    }
  }
  /**
   * Attempt to load the JDBC Driver class. If it's not available, return false.
   * 
   * @param classname validate that this classname exists in the classpath
   * 
   * @return true if the class exists
   */
  private static boolean validateJdbcDriverClass(String classname) {
    // no need to test if the class exists if it is null
    if (classname == null) {
      return true;
    }
    
    try {
      Class.forName(classname);
      return true;
    } catch(NoClassDefFoundError e) { 
      if (logger.isDebugEnabled()) {
        logger.debug("classExists returning false", e);
      }
    } catch(ClassNotFoundException e) {
      if (logger.isDebugEnabled()) {
        logger.debug("classExists returning false", e);
      }
    } catch(Exception e) { 
      if (logger.isDebugEnabled()) {
        logger.debug("classExists returning false", e);
      }
    }
    // if we've made it here, an exception has occurred.
    return false;
  }
  
  public DatabaseDialectService() {
    this(true);
  }

  public DatabaseDialectService(boolean validateClasses) {
    this.isOnlyReturnAvailable = validateClasses;
   }
  
  public void registerDatabaseDialect(IDatabaseDialect databaseDialect) {
    // Do nothing here - no need to call this anymore
  }
  
  /**
   * 
   * @param databaseDialect
   * @param validateClassExists
   */
  public void registerDatabaseDialect(IDatabaseDialect databaseDialect, boolean validateClassExists) {
    // Do nothing here - no need to call this anymore
  }
  
  public boolean validateJdbcDriverClassExists(String classname) {
    return DatabaseDialectService.validateJdbcDriverClass(classname);
  }
  
  
  public List<IDatabaseType> getDatabaseTypes() {
    return Collections.unmodifiableList( ( this.isOnlyReturnAvailable ? DatabaseDialectService.validDatabaseTypes:DatabaseDialectService.allDatabaseTypes )  );
  }
  
  public IDatabaseDialect getDialect(IDatabaseType databaseType) {
    if (this.isOnlyReturnAvailable) {
      return validTypeToDialectMap.get(databaseType);
    } else {
      return allTypeToDialectMap.get(databaseType);
    }
    
  }
  
  public IDatabaseDialect getDialect(IDatabaseConnection connection) {
    if (this.isOnlyReturnAvailable) {
      return validTypeToDialectMap.get(connection.getDatabaseType());
    } else {
      return allTypeToDialectMap.get(connection.getDatabaseType());
    }
  }
  
  public List<IDatabaseDialect> getDatabaseDialects() {
    return Collections.unmodifiableList( ( this.isOnlyReturnAvailable ? DatabaseDialectService.validDatabaseDialects:DatabaseDialectService.allDatabaseDialects ) );
  }
}
