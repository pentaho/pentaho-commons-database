package org.pentaho.ui.database.services;

import java.util.List;

import org.pentaho.database.IDatabaseDialect;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.model.IDatabaseType;
import org.pentaho.ui.xul.XulServiceCallback;

public interface IXulAsyncDatabaseDialectService {

  void registerDatabaseDialect(IDatabaseDialect databaseDialect, XulServiceCallback<Void> callback);

  void registerDatabaseDialect(IDatabaseDialect databaseDialect, boolean validateClassExists, XulServiceCallback<Void> callback);
  
  void getDialect(IDatabaseType databaseType, XulServiceCallback<IDatabaseDialect> callback);
  
  void getDialect(IDatabaseConnection connection, XulServiceCallback<IDatabaseDialect> callback);
  
  void getDatabaseDialects(XulServiceCallback<List<IDatabaseDialect>> callback);
  
  void getDatabaseTypes(XulServiceCallback<List<IDatabaseType>> callback);
  
  void validateJdbcDriverClassExists(String classname, XulServiceCallback<Boolean> callback);

}
