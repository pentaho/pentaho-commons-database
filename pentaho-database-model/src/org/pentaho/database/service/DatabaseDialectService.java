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

/* 
import org.pentaho.database.dialect.GenericDatabaseDialect;
import org.pentaho.database.dialect.DB2DatabaseDialect;
import org.pentaho.database.dialect.H2DatabaseDialect;
import org.pentaho.database.dialect.Hive2DatabaseDialect;
import org.pentaho.database.dialect.HiveDatabaseDialect;
import org.pentaho.database.dialect.HypersonicDatabaseDialect;
import org.pentaho.database.dialect.ImpalaDatabaseDialect;
import org.pentaho.database.dialect.InformixDatabaseDialect;
import org.pentaho.database.dialect.MSSQLServerDatabaseDialect;
import org.pentaho.database.dialect.MSSQLServerNativeDatabaseDialect;
import org.pentaho.database.dialect.MonetDatabaseDialect;
import org.pentaho.database.dialect.MySQLDatabaseDialect;
import org.pentaho.database.dialect.OracleDatabaseDialect;
import org.pentaho.database.dialect.PostgreSQLDatabaseDialect;
import org.pentaho.database.dialect.TeradataDatabaseDialect;
import org.pentaho.database.dialect.Vertica5DatabaseDialect;
import org.pentaho.database.dialect.VerticaDatabaseDialect;
*/
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.model.IDatabaseType;

public class DatabaseDialectService implements IDatabaseDialectService{
  
  private static ServiceLoader<IDatabaseDialect> dialectLoader = ServiceLoader.load(IDatabaseDialect.class);
  
  private static final Log logger = LogFactory.getLog(DatabaseDialectService.class);
  
  private static final List<IDatabaseDialect> databaseDialects = new ArrayList<IDatabaseDialect>();
  private static final List<IDatabaseType> databaseTypes = new ArrayList<IDatabaseType>();
  private static final Map<IDatabaseType, IDatabaseDialect> typeToDialectMap = new HashMap<IDatabaseType, IDatabaseDialect>();
  
  static {
    DatabaseDialectService.validateAndCatalogServices();
    if (logger.isDebugEnabled()) {
      logger.debug( "databaseDialects list ..." );
      for ( IDatabaseDialect dialect : databaseDialects ) {
        logger.debug(String.format( " ... %s ( %s )", dialect.getDatabaseType().getName(), dialect.getDatabaseType().getShortName() ) );
      }
    }
  }
  private static void validateAndCatalogServices() {
    for (IDatabaseDialect dialect : DatabaseDialectService.dialectLoader) {
      if (logger.isDebugEnabled()) {
        logger.debug( String.format("Checking for presence of %s ( %s )", dialect.getDatabaseType().getName(), dialect.getNativeDriver() )  );
      }
      if ( validateJdbcDriverClass(dialect.getNativeDriver())) {
        DatabaseDialectService.databaseTypes.add(dialect.getDatabaseType());
        DatabaseDialectService.typeToDialectMap.put(dialect.getDatabaseType(), dialect);
        DatabaseDialectService.databaseDialects.add(dialect);
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
   }
  
  public void registerDatabaseDialect(IDatabaseDialect databaseDialect) {

  }
  
  /**
   * 
   * @param databaseDialect
   * @param validateClassExists
   */
  public void registerDatabaseDialect(IDatabaseDialect databaseDialect, boolean validateClassExists) {
    
  }
  
  public boolean validateJdbcDriverClassExists(String classname) {
    return DatabaseDialectService.validateJdbcDriverClass(classname);
  }
  
  
  public List<IDatabaseType> getDatabaseTypes() {
    return Collections.unmodifiableList( databaseTypes );
  }
  
  public IDatabaseDialect getDialect(IDatabaseType databaseType) {
    return typeToDialectMap.get(databaseType);
  }
  
  public IDatabaseDialect getDialect(IDatabaseConnection connection) {
    return typeToDialectMap.get(connection.getDatabaseType());
  }
  
  public List<IDatabaseDialect> getDatabaseDialects() {
    return Collections.unmodifiableList( databaseDialects );
  }
}
