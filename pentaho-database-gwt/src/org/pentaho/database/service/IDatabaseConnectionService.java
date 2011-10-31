package org.pentaho.database.service;

import java.util.List;

import org.pentaho.database.model.DatabaseConnectionPoolParameter;
import org.pentaho.database.model.IDatabaseConnection;

public interface IDatabaseConnectionService {

  IDatabaseConnection createDatabaseConnection(String driver, String url);
  
  List<String> checkParameters(final IDatabaseConnection connection);

  String testConnection(final IDatabaseConnection connection);

  DatabaseConnectionPoolParameter[] getPoolingParameters();
  
}