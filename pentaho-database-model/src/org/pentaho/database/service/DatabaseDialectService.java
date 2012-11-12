package org.pentaho.database.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.database.IDatabaseDialect;
import org.pentaho.database.dialect.DB2DatabaseDialect;
import org.pentaho.database.dialect.GenericDatabaseDialect;
import org.pentaho.database.dialect.H2DatabaseDialect;
import org.pentaho.database.dialect.HypersonicDatabaseDialect;
import org.pentaho.database.dialect.MSSQLServerDatabaseDialect;
import org.pentaho.database.dialect.MSSQLServerNativeDatabaseDialect;
import org.pentaho.database.dialect.MonetDatabaseDialect;
import org.pentaho.database.dialect.MySQLDatabaseDialect;
import org.pentaho.database.dialect.OracleDatabaseDialect;
import org.pentaho.database.dialect.PostgreSQLDatabaseDialect;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.model.IDatabaseType;

public class DatabaseDialectService implements IDatabaseDialectService{
  
  private static final Log logger = LogFactory.getLog(DatabaseDialectService.class);
  
  List<IDatabaseDialect> databaseDialects = new ArrayList<IDatabaseDialect>();
  List<IDatabaseType> databaseTypes = new ArrayList<IDatabaseType>();
  Map<IDatabaseType, IDatabaseDialect> typeToDialectMap = new HashMap<IDatabaseType, IDatabaseDialect>();
  GenericDatabaseDialect genericDialect = new GenericDatabaseDialect();

  
  public DatabaseDialectService() {
    this(true);
  }

  public DatabaseDialectService(boolean validateClasses) {
    // temporary until we have a better approach
    registerDatabaseDialect(new OracleDatabaseDialect(), validateClasses);
    registerDatabaseDialect(new MySQLDatabaseDialect(), validateClasses);
    registerDatabaseDialect(new HypersonicDatabaseDialect(), validateClasses);
    registerDatabaseDialect(new MSSQLServerDatabaseDialect(), validateClasses);
    registerDatabaseDialect(new MSSQLServerNativeDatabaseDialect(), validateClasses);
    registerDatabaseDialect(new DB2DatabaseDialect(), validateClasses);
    registerDatabaseDialect(new PostgreSQLDatabaseDialect(), validateClasses);
    registerDatabaseDialect(new H2DatabaseDialect(), validateClasses);
    registerDatabaseDialect(new MonetDatabaseDialect(), validateClasses);
    // the generic service is special, because it plays a role
    // in generation from a URL and Driver
    registerDatabaseDialect(genericDialect, validateClasses);
  }

  
  public void registerDatabaseDialect(IDatabaseDialect databaseDialect) {
    registerDatabaseDialect(databaseDialect, true);
  }
  
  /**
   * 
   * @param databaseDialect
   * @param validateClassExists
   */
  public void registerDatabaseDialect(IDatabaseDialect databaseDialect, boolean validateClassExists) {
    if (!validateClassExists || validateJdbcDriverClassExists(databaseDialect.getNativeDriver())) {
      databaseTypes.add(databaseDialect.getDatabaseType());
      typeToDialectMap.put(databaseDialect.getDatabaseType(), databaseDialect);
      databaseDialects.add(databaseDialect);
    }
  }
  
  
  /**
   * Attempt to load the JDBC Driver class. If it's not available, return false.
   * 
   * @param classname validate that this classname exists in the classpath
   * 
   * @return true if the class exists
   */
  public boolean validateJdbcDriverClassExists(String classname) {
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
  
  public List<IDatabaseType> getDatabaseTypes() {
    return databaseTypes;
  }
  
  public IDatabaseDialect getDialect(IDatabaseType databaseType) {
    return typeToDialectMap.get(databaseType);
  }
  
  public IDatabaseDialect getDialect(IDatabaseConnection connection) {
    return typeToDialectMap.get(connection.getDatabaseType());
  }
  
  public List<IDatabaseDialect> getDatabaseDialects() {
    return databaseDialects;
  }
}
