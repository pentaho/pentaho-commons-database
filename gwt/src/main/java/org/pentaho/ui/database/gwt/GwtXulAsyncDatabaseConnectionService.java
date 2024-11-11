/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2028-08-13
 ******************************************************************************/


package org.pentaho.ui.database.gwt;

import java.util.List;

import org.pentaho.database.model.DatabaseConnectionPoolParameter;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.gwt.widgets.login.client.AuthenticatedGwtServiceUtil;
import org.pentaho.gwt.widgets.login.client.IAuthenticatedGwtCommand;
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
    } else if (moduleUrl.indexOf("api") > -1) {//$NON-NLS-1$
      //we are running the client in the context of a BI Server plugin, so 
      //point the request to the GWT rpc proxy servlet
      String baseUrl = moduleUrl.substring(0, moduleUrl.indexOf("api"));//$NON-NLS-1$
      //NOTE: the dispatch URL ("connectionService") must match the bean id for 
      //this service object in your plugin.xml.  "gwtrpc" is the servlet 
      //that handles plugin gwt rpc requests in the BI Server.
      return baseUrl + "gwtrpc/databaseConnectionService";//$NON-NLS-1$
    }
    //we are running this client in hosted mode, so point to the servlet 
    //defined in war/WEB-INF/web.xml
    return moduleUrl + "databaseConnectionService";
  }

  
  public void checkParameters(final IDatabaseConnection connection, final XulServiceCallback<List<String>> callback) { 
    AuthenticatedGwtServiceUtil.invokeCommand(new IAuthenticatedGwtCommand() {
      public void execute(AsyncCallback callback) {
        SERVICE.checkParameters(connection, callback);
      }
    }, new AsyncCallback<List<String>>() {

      public void onFailure(Throwable arg0) {
        callback.error("error checking parameters: ", arg0);//$NON-NLS-1$
      }
      public void onSuccess(List<String> arg0) {
        callback.success(arg0);
      }

    });
  }

  public void getPoolingParameters(final XulServiceCallback<DatabaseConnectionPoolParameter[]> callback) {
    AuthenticatedGwtServiceUtil.invokeCommand(new IAuthenticatedGwtCommand() {
      public void execute(AsyncCallback callback) {
        SERVICE.getPoolingParameters(callback);
      }
    }, new AsyncCallback<DatabaseConnectionPoolParameter[]>() {

      public void onFailure(Throwable arg0) {
        callback.error("error getting pooling parameters: ", arg0);//$NON-NLS-1$
      }
      public void onSuccess(DatabaseConnectionPoolParameter[] arg0) {
        callback.success(arg0);
      }
    });
  }

  public void testConnection(final IDatabaseConnection connection, final XulServiceCallback<String> callback) {
    AuthenticatedGwtServiceUtil.invokeCommand(new IAuthenticatedGwtCommand() {
      public void execute(AsyncCallback callback) {
        SERVICE.testConnection(connection, callback);
      }
    }, new AsyncCallback<String>() {
      
      public void onFailure(Throwable arg0) {
        callback.error("error testing connection: ", arg0);//$NON-NLS-1$
      }
      public void onSuccess(String arg0) {
        callback.success(arg0);
      }
    });
  }


  @Override
  public void createDatabaseConnection(final String driver, final String url, final XulServiceCallback<IDatabaseConnection> callback) {
    AuthenticatedGwtServiceUtil.invokeCommand(new IAuthenticatedGwtCommand() {
      public void execute(AsyncCallback callback) {
        SERVICE.createDatabaseConnection(driver, url, callback);
      }
    }, new AsyncCallback<IDatabaseConnection>() {
      
      public void onFailure(Throwable arg0) {
        callback.error("error testing connection: ", arg0);//$NON-NLS-1$
      }
      public void onSuccess(IDatabaseConnection arg0) {
        callback.success(arg0);
      }
    });
  }

}
