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
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.database.IDatabaseDialect;
import org.pentaho.database.dialect.GenericDatabaseDialect;
import org.pentaho.database.model.DatabaseConnectionPoolParameter;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.util.DatabaseUtil;
import org.pentaho.di.core.database.DatabaseMeta;

public class DatabaseConnectionService implements IDatabaseConnectionService {
  
  private static final Log logger = LogFactory.getLog(DatabaseConnectionService.class);
  
  public static int MAX_RETURN_VALUE_LENGTH = 350;
  GenericDatabaseDialect genericDialect = new GenericDatabaseDialect();
  IDatabaseDialectService dialectService = null;

  public DatabaseConnectionService(IDatabaseDialectService dialectService) {
    this.dialectService = dialectService;
  }
  
  public DatabaseConnectionService() {
    this.dialectService = new DatabaseDialectService(true);
  }
  
  /* (non-Javadoc)
   * @see org.pentaho.database.service.IDatabaseConnectionService#checkParameters(org.pentaho.database.model.IDatabaseConnection)
   */
  public List<String> checkParameters(IDatabaseConnection connection) {
    DatabaseMeta meta = DatabaseUtil.convertToDatabaseMeta(connection);
    String params[] = meta.checkParameters();
    List<String> paramList = new ArrayList<String>();
    for (String param : params) {
      paramList.add(param);
    }
    return paramList;
  }
  
  /* (non-Javadoc)
   * @see org.pentaho.database.service.IDatabaseConnectionService#testConnection(org.pentaho.database.model.IDatabaseConnection)
   */
  public String testConnection(IDatabaseConnection connection) {
    DatabaseMeta meta = DatabaseUtil.convertToDatabaseMeta(connection);
    String returnValue = meta.testConnection();
    if (logger.isDebugEnabled()) {
      logger.debug("Return Value from test connection:\n" + returnValue);
    }
    if(returnValue != null && returnValue.length() > MAX_RETURN_VALUE_LENGTH) {
      returnValue = returnValue.substring(0, MAX_RETURN_VALUE_LENGTH-1);
    }
    return returnValue;
  }
  
  private static final DatabaseConnectionPoolParameter[] poolingParameters = new DatabaseConnectionPoolParameter[]
    {
       new DatabaseConnectionPoolParameter("defaultAutoCommit", "true", "The default auto-commit state of connections created by this pool."), 
       new DatabaseConnectionPoolParameter("defaultReadOnly", null, "The default read-only state of connections created by this pool.\nIf not set then the setReadOnly method will not be called.\n (Some drivers don't support read only mode, ex: Informix)"), 
       new DatabaseConnectionPoolParameter("defaultTransactionIsolation", null, "the default TransactionIsolation state of connections created by this pool. One of the following: (see javadoc)\n\n  * NONE\n  * READ_COMMITTED\n  * READ_UNCOMMITTED\n  * REPEATABLE_READ  * SERIALIZABLE\n"), 
       new DatabaseConnectionPoolParameter("defaultCatalog", null, "The default catalog of connections created by this pool."),
       
       new DatabaseConnectionPoolParameter("initialSize", "0", "The initial number of connections that are created when the pool is started."), 
       new DatabaseConnectionPoolParameter("maxActive", "8", "The maximum number of active connections that can be allocated from this pool at the same time, or non-positive for no limit."), 
       new DatabaseConnectionPoolParameter("maxIdle", "8", "The maximum number of connections that can remain idle in the pool, without extra ones being released, or negative for no limit."), 
       new DatabaseConnectionPoolParameter("minIdle", "0", "The minimum number of connections that can remain idle in the pool, without extra ones being created, or zero to create none."), 
       new DatabaseConnectionPoolParameter("maxWait", "-1", "The maximum number of milliseconds that the pool will wait (when there are no available connections) for a connection to be returned before throwing an exception, or -1 to wait indefinitely."),
       
       new DatabaseConnectionPoolParameter("validationQuery", null, "The SQL query that will be used to validate connections from this pool before returning them to the caller.\nIf specified, this query MUST be an SQL SELECT statement that returns at least one row."), 
       new DatabaseConnectionPoolParameter("testOnBorrow", "true", "The indication of whether objects will be validated before being borrowed from the pool.\nIf the object fails to validate, it will be dropped from the pool, and we will attempt to borrow another.\nNOTE - for a true value to have any effect, the validationQuery parameter must be set to a non-null string."), 
       new DatabaseConnectionPoolParameter("testOnReturn", "false", "The indication of whether objects will be validated before being returned to the pool.\nNOTE - for a true value to have any effect, the validationQuery parameter must be set to a non-null string."), 
       new DatabaseConnectionPoolParameter("testWhileIdle", "false", "The indication of whether objects will be validated by the idle object evictor (if any). If an object fails to validate, it will be dropped from the pool.\nNOTE - for a true value to have any effect, the validationQuery parameter must be set to a non-null string."), 
       new DatabaseConnectionPoolParameter("timeBetweenEvictionRunsMillis", null, "The number of milliseconds to sleep between runs of the idle object evictor thread. When non-positive, no idle object evictor thread will be run."),
       
       new DatabaseConnectionPoolParameter("poolPreparedStatements", "false", "Enable prepared statement pooling for this pool."), 
       new DatabaseConnectionPoolParameter("maxOpenPreparedStatements", "-1", "The maximum number of open statements that can be allocated from the statement pool at the same time, or zero for no limit."), 
       new DatabaseConnectionPoolParameter("accessToUnderlyingConnectionAllowed", "false", "Controls if the PoolGuard allows access to the underlying connection."), 
       new DatabaseConnectionPoolParameter("removeAbandoned", "false", "Flag to remove abandoned connections if they exceed the removeAbandonedTimout.\nIf set to true a connection is considered abandoned and eligible for removal if it has been idle longer than the removeAbandonedTimeout. Setting this to true can recover db connections from poorly written applications which fail to close a connection."), 
       new DatabaseConnectionPoolParameter("removeAbandonedTimeout", "300", "Timeout in seconds before an abandoned connection can be removed."), 
       new DatabaseConnectionPoolParameter("logAbandoned", "false", "Flag to log stack traces for application code which abandoned a Statement or Connection.\nLogging of abandoned Statements and Connections adds overhead for every Connection open or new Statement because a stack trace has to be generated."), 
    };
  
  /* (non-Javadoc)
   * @see org.pentaho.database.service.IDatabaseConnectionService#getPoolingParameters()
   */
  public DatabaseConnectionPoolParameter[] getPoolingParameters() {
   return poolingParameters; 
  }
  


  
  public IDatabaseConnection createDatabaseConnection(String driver, String url) {
    for (IDatabaseDialect dialect : dialectService.getDatabaseDialects()) {
      if (dialect.getNativeDriver() != null && 
          dialect.getNativeDriver().equals(driver)) {
        if (dialect.getNativeJdbcPre() != null && url.startsWith(dialect.getNativeJdbcPre())) {
          return dialect.createNativeConnection(url);
        }
      }
    }
    
    // if no native driver was found, create a custom dialect object.
    
    IDatabaseConnection conn = genericDialect.createNativeConnection(url);
    conn.getAttributes().put(GenericDatabaseDialect.ATTRIBUTE_CUSTOM_DRIVER_CLASS, driver);
    
    return conn;
  }


}
