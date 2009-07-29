package org.pentaho.ui.database.gwt;

import java.util.List;

import org.pentaho.database.model.DatabaseConnection;
import org.pentaho.database.model.DatabaseConnectionPoolParameter;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.model.IDatabaseType;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IGwtDatabaseConnectionServiceAsync {
  
  void checkParameters(IDatabaseConnection connection, AsyncCallback<List<String>> callback);

  void testConnection(IDatabaseConnection connection, AsyncCallback<String> callback);

  void getPoolingParameters(AsyncCallback<DatabaseConnectionPoolParameter[]> callback);
  
  void getDatabaseTypes(AsyncCallback<List<IDatabaseType>> callback);
  
  void getBogoDatabase(AsyncCallback<DatabaseConnection> arg1);
  /*

public interface IGwtDatabaseConnectionServiceAsync {
  void checkParameters(org.pentaho.database.model.IDatabaseConnection connection, com.google.gwt.user.client.rpc.AsyncCallback<java.util.List<java.lang.String>> arg2);
  void getBogoDatabase(com.google.gwt.user.client.rpc.AsyncCallback<org.pentaho.database.model.DatabaseConnection> arg1);
  void getPoolingParameters(com.google.gwt.user.client.rpc.AsyncCallback<org.pentaho.database.model.DatabaseConnectionPoolParameter[]> arg1);
  void testConnection(org.pentaho.database.model.IDatabaseConnection connection, com.google.gwt.user.client.rpc.AsyncCallback<java.lang.String> arg2);
}

   */
}
