package org.pentaho.ui.database.gwt;

import java.util.List;

import org.pentaho.database.model.DatabaseConnection;
import org.pentaho.database.model.DatabaseConnectionPoolParameter;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.model.IDatabaseType;
import org.pentaho.database.service.DatabaseConnectionService;
import org.pentaho.database.service.IDatabaseConnectionService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class GwtDatabaseConnectionServlet extends RemoteServiceServlet implements IGwtDatabaseConnectionService {

  IDatabaseConnectionService service = new DatabaseConnectionService();
  
  public List<String> checkParameters(IDatabaseConnection connection) {
    return service.checkParameters(connection);
  }

  public DatabaseConnectionPoolParameter[] getPoolingParameters() {
    return service.getPoolingParameters();
  }

  public String testConnection(IDatabaseConnection connection) {
    return service.testConnection(connection);
  }
  
  public DatabaseConnection getBogoDatabase() {
    return null;
  }

  public List<IDatabaseType> getDatabaseTypes() {
    return service.getDatabaseTypes();
  }
}
