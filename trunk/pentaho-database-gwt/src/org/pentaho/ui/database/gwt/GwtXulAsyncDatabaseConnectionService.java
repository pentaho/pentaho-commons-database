package org.pentaho.ui.database.gwt;

import java.util.List;

import org.pentaho.database.model.DatabaseConnectionPoolParameter;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.model.IDatabaseType;
import org.pentaho.ui.database.services.IXulAsyncDatabaseConnectionService;
import org.pentaho.ui.xul.XulServiceCallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class GwtXulAsyncDatabaseConnectionService implements IXulAsyncDatabaseConnectionService {

  static IGwtDatabaseConnectionServiceAsync SERVICE;

  static {
    SERVICE = (IGwtDatabaseConnectionServiceAsync) GWT.create(IGwtDatabaseConnectionService.class);
    ServiceDefTarget endpoint = (ServiceDefTarget) SERVICE;
    endpoint.setServiceEntryPoint(getBaseUrl());
  }
  
  /** 
   * Returns the context-aware URL to the rpc service
   */
  private static String getBaseUrl() {
    String moduleUrl = GWT.getModuleBaseURL();
    
    //
    //Set the base url appropriately based on the context in which we are running this client
    //
    if(moduleUrl.indexOf("content") > -1) {
      //we are running the client in the context of a BI Server plugin, so 
      //point the request to the GWT rpc proxy servlet
      String baseUrl = moduleUrl.substring(0, moduleUrl.indexOf("content"));
      //NOTE: the dispatch URL ("connectionService") must match the bean id for 
      //this service object in your plugin.xml.  "gwtrpc" is the servlet 
      //that handles plugin gwt rpc requests in the BI Server.
      return  baseUrl + "gwtrpc/databaseConnectionService";
    }
    //we are running this client in hosted mode, so point to the servlet 
    //defined in war/WEB-INF/web.xml
    return moduleUrl + "databaseConnectionService";
  }

  
  public void checkParameters(final IDatabaseConnection connection, final XulServiceCallback<List<String>> callback) {
    SERVICE.checkParameters(connection, new AsyncCallback<List<String>>() {
      public void onFailure(Throwable arg0) {
        callback.error("error checking parameters: ", arg0);//$NON-NLS-1$
      }
      public void onSuccess(List<String> arg0) {
        callback.success(arg0);
      }
    });
  }

  public void getPoolingParameters(final XulServiceCallback<DatabaseConnectionPoolParameter[]> callback) {
    SERVICE.getPoolingParameters(new AsyncCallback<DatabaseConnectionPoolParameter[]>() {
      public void onFailure(Throwable arg0) {
        callback.error("error getting pooling parameters: ", arg0);//$NON-NLS-1$
      }
      public void onSuccess(DatabaseConnectionPoolParameter[] arg0) {
        callback.success(arg0);
      }
    });
  }

  public void testConnection(final IDatabaseConnection connection, final XulServiceCallback<String> callback) {
    SERVICE.testConnection(connection, new AsyncCallback<String>() {
      public void onFailure(Throwable arg0) {
        callback.error("error testing connection: ", arg0);//$NON-NLS-1$
      }
      public void onSuccess(String arg0) {
        callback.success(arg0);
      }
    });
  }

  public void getDatabaseTypes(final XulServiceCallback<List<IDatabaseType>> callback) {
    SERVICE.getDatabaseTypes(new AsyncCallback<List<IDatabaseType>>() {
      public void onFailure(Throwable arg0) {
        callback.error("error testing connection: ", arg0);//$NON-NLS-1$
      }
      public void onSuccess(List<IDatabaseType> arg0) {
        callback.success(arg0);
      }
    });
  }
}
