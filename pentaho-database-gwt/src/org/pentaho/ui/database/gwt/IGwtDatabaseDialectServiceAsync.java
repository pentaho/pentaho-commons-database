package org.pentaho.ui.database.gwt;

import java.util.List;

import org.pentaho.database.IDatabaseDialect;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.model.IDatabaseType;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IGwtDatabaseDialectServiceAsync {

  void registerDatabaseDialect(IDatabaseDialect databaseDialect, AsyncCallback<Void> callback);

  void registerDatabaseDialect(IDatabaseDialect databaseDialect, boolean validateClassExists, AsyncCallback<Void> callback);
  
  void getDialect(IDatabaseType databaseType, AsyncCallback<IDatabaseDialect> callback);
  
  void getDialect(IDatabaseConnection connection, AsyncCallback<IDatabaseDialect> callback);
  
  void getDatabaseDialects(AsyncCallback<List<IDatabaseDialect>> callback);
  
  void getDatabaseTypes(AsyncCallback<List<IDatabaseType>> callback);

  void validateJdbcDriverClassExists(String classname, AsyncCallback<Boolean> callback);
  
  void getBogoDatabase(AsyncCallback<IDatabaseDialect> databaseDialect);

}
