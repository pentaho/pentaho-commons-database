package org.pentaho.ui.database.gwt;

import java.util.List;

import org.pentaho.database.model.DatabaseConnection;
import org.pentaho.database.model.DatabaseConnectionPoolParameter;
import org.pentaho.database.model.IDatabaseConnection;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IGwtDatabaseConnectionServiceAsync {
  
  void checkParameters(IDatabaseConnection connection, AsyncCallback<List<String>> callback);

  void testConnection(IDatabaseConnection connection, AsyncCallback<String> callback);

  void getPoolingParameters(AsyncCallback<DatabaseConnectionPoolParameter[]> callback);

  void createDatabaseConnection(String driver, String url, AsyncCallback<DatabaseConnection> callback );
  
  void getBogoDatabase(AsyncCallback<DatabaseConnection> arg1);

}
