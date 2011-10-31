package org.pentaho.ui.database.services;

import java.util.List;

import org.pentaho.database.model.DatabaseConnectionPoolParameter;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.ui.xul.XulServiceCallback;

public interface IXulAsyncDatabaseConnectionService {
  void checkParameters(IDatabaseConnection connection, XulServiceCallback<List<String>> callback);
  void testConnection(IDatabaseConnection connection, XulServiceCallback<String> callback);
  void getPoolingParameters(XulServiceCallback<DatabaseConnectionPoolParameter[]> callback);
  void createDatabaseConnection(String driver, String url, XulServiceCallback<IDatabaseConnection> callback );
 }
