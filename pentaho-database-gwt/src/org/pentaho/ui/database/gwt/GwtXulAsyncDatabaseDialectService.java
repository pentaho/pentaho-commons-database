package org.pentaho.ui.database.gwt;

import java.util.List;

import org.pentaho.database.IDatabaseDialect;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.model.IDatabaseType;
import org.pentaho.gwt.widgets.login.client.AuthenticatedGwtServiceUtil;
import org.pentaho.gwt.widgets.login.client.IAuthenticatedGwtCommand;
import org.pentaho.ui.database.services.IXulAsyncDatabaseDialectService;
import org.pentaho.ui.xul.XulServiceCallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;


public class GwtXulAsyncDatabaseDialectService implements IXulAsyncDatabaseDialectService {
  
  static IGwtDatabaseDialectServiceAsync SERVICE;

  static {
    SERVICE = (IGwtDatabaseDialectServiceAsync) GWT.create(IGwtDatabaseDialectService.class);
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
      return  baseUrl + "gwtrpc/databaseDialectService";
    } else if (moduleUrl.indexOf("api") > -1) {//$NON-NLS-1$
      //we are running the client in the context of a BI Server plugin, so 
      //point the request to the GWT rpc proxy servlet
      String baseUrl = moduleUrl.substring(0, moduleUrl.indexOf("api"));//$NON-NLS-1$
      //NOTE: the dispatch URL ("connectionService") must match the bean id for 
      //this service object in your plugin.xml.  "gwtrpc" is the servlet 
      //that handles plugin gwt rpc requests in the BI Server.
      return baseUrl + "gwtrpc/databaseDialectService";//$NON-NLS-1$
    }
    //we are running this client in hosted mode, so point to the servlet 
    //defined in war/WEB-INF/web.xml
    return moduleUrl + "databaseDialectService";
  }

  @Override
  public void registerDatabaseDialect(final IDatabaseDialect databaseDialect, final XulServiceCallback<Void> callback) {
    AuthenticatedGwtServiceUtil.invokeCommand(new IAuthenticatedGwtCommand() {
      public void execute(AsyncCallback callback) {
        SERVICE.registerDatabaseDialect(databaseDialect, callback);
      }
    }, new AsyncCallback<Void>() {
      
      public void onFailure(Throwable arg0) {
        callback.error("error testing connection: ", arg0);//$NON-NLS-1$
      }
      public void onSuccess(Void arg0) {
        callback.success(arg0);
      }
    });
  }

  @Override
  public void registerDatabaseDialect(final IDatabaseDialect databaseDialect, final boolean validateClassExists,
      final XulServiceCallback<Void> callback) {
    AuthenticatedGwtServiceUtil.invokeCommand(new IAuthenticatedGwtCommand() {
      public void execute(AsyncCallback callback) {
        SERVICE.registerDatabaseDialect(databaseDialect, validateClassExists, callback);
      }
    }, new AsyncCallback<Void>() {
      
      public void onFailure(Throwable arg0) {
        callback.error("error testing connection: ", arg0);//$NON-NLS-1$
      }
      public void onSuccess(Void arg0) {
        callback.success(arg0);
      }
    });
  }

  @Override
  public void getDialect(final IDatabaseType databaseType, final XulServiceCallback<IDatabaseDialect> callback) {
    AuthenticatedGwtServiceUtil.invokeCommand(new IAuthenticatedGwtCommand() {
      public void execute(AsyncCallback callback) {
        SERVICE.getDialect(databaseType, callback);
      }
    }, new AsyncCallback<IDatabaseDialect>() {
      
      public void onFailure(Throwable arg0) {
        callback.error("error testing connection: ", arg0);//$NON-NLS-1$
      }
      public void onSuccess(IDatabaseDialect arg0) {
        callback.success(arg0);
      }
    });
  }

  @Override
  public void getDialect(final IDatabaseConnection connection, final XulServiceCallback<IDatabaseDialect> callback) {
    AuthenticatedGwtServiceUtil.invokeCommand(new IAuthenticatedGwtCommand() {
      public void execute(AsyncCallback callback) {
        SERVICE.getDialect(connection, callback);
      }
    }, new AsyncCallback<IDatabaseDialect>() {
      
      public void onFailure(Throwable arg0) {
        callback.error("error testing connection: ", arg0);//$NON-NLS-1$
      }
      public void onSuccess(IDatabaseDialect arg0) {
        callback.success(arg0);
      }
    });
  }

  @Override
  public void getDatabaseDialects(final XulServiceCallback<List<IDatabaseDialect>> callback) {
    AuthenticatedGwtServiceUtil.invokeCommand(new IAuthenticatedGwtCommand() {
      public void execute(AsyncCallback callback) {
        SERVICE.getDatabaseDialects(callback);
      }
    }, new AsyncCallback<List<IDatabaseDialect>>() {
      
      public void onFailure(Throwable arg0) {
        callback.error("error testing connection: ", arg0);//$NON-NLS-1$
      }
      public void onSuccess(List<IDatabaseDialect> arg0) {
        callback.success(arg0);
      }
    });
  }

  @Override
  public void getDatabaseTypes(final XulServiceCallback<List<IDatabaseType>> callback) {
    AuthenticatedGwtServiceUtil.invokeCommand(new IAuthenticatedGwtCommand() {
      public void execute(AsyncCallback callback) {
        SERVICE.getDatabaseTypes(callback);
      }
    }, new AsyncCallback<List<IDatabaseType>>() {
      
      public void onFailure(Throwable arg0) {
        callback.error("error testing connection: ", arg0);//$NON-NLS-1$
      }
      public void onSuccess(List<IDatabaseType> arg0) {
        callback.success(arg0);
      }
    });
  }

  @Override
  public void validateJdbcDriverClassExists(final String classname, final XulServiceCallback<Boolean> callback) {
    AuthenticatedGwtServiceUtil.invokeCommand(new IAuthenticatedGwtCommand() {
      public void execute(AsyncCallback callback) {
        SERVICE.validateJdbcDriverClassExists(classname, callback);
      }
    }, new AsyncCallback<Boolean>() {
      
      public void onFailure(Throwable arg0) {
        callback.error("error testing connection: ", arg0);//$NON-NLS-1$
      }
      public void onSuccess(Boolean arg0) {
        callback.success(arg0);
      }
    });
  }

}
