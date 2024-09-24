/*!
* This program is free software; you can redistribute it and/or modify it under the
* terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
* Foundation.
*
* You should have received a copy of the GNU Lesser General Public License along with this
* program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
* or from the Free Software Foundation, Inc.,
* 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
* without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
* See the GNU Lesser General Public License for more details.
*
* Copyright (c) 2002-2017 Hitachi Vantara..  All rights reserved.
*/

package org.pentaho.ui.database.gwt;

import java.util.List;

import com.google.gwt.core.client.Scheduler;
import org.pentaho.database.IDatabaseDialect;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.model.IDatabaseType;
import org.pentaho.gwt.widgets.client.utils.NameUtils;
import org.pentaho.ui.database.event.IConnectionAutoBeanFactory;
import org.pentaho.ui.database.event.IDatabaseDialectList;
import org.pentaho.ui.database.event.IDatabaseTypesList;
import org.pentaho.ui.database.services.IXulAsyncDatabaseDialectService;
import org.pentaho.ui.xul.XulServiceCallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;


public class GwtXulAsyncDatabaseDialectService implements IXulAsyncDatabaseDialectService {
  protected IConnectionAutoBeanFactory connectionAutoBeanFactory;
  
  public GwtXulAsyncDatabaseDialectService() {
    super();
    connectionAutoBeanFactory = GWT.create(IConnectionAutoBeanFactory.class);
  }
   /** 
   * Returns the context-aware URL to the REST service
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
      return  baseUrl + "plugin/data-access/api/dialect/";
    } else if (moduleUrl.indexOf("api") > -1) {//$NON-NLS-1$
      //we are running the client in the context of a BI Server plugin, so 
      //point the request to the GWT rpc proxy servlet
      String baseUrl = moduleUrl.substring(0, moduleUrl.indexOf("api"));//$NON-NLS-1$
      //NOTE: the dispatch URL ("connectionService") must match the bean id for 
      //this service object in your plugin.xml.  "gwtrpc" is the servlet 
      //that handles plugin gwt rpc requests in the BI Server.
      return baseUrl + "plugin/data-access/api/dialect/";//$NON-NLS-1$
    }
    //we are running this client in hosted mode, so point to the servlet 
    //defined in war/WEB-INF/web.xml
    return moduleUrl + "plugin/data-access/api/dialect/";
  }

  @Override
  public void registerDatabaseDialect(final IDatabaseDialect databaseDialect, final XulServiceCallback<Void> callback) {
    RequestBuilder registerDialectRequestBuilder = new RequestBuilder( RequestBuilder.POST, getBaseUrl() + "registerDatabaseDialect" );
    registerDialectRequestBuilder.setHeader("Content-Type", "application/json");
    AutoBean<IDatabaseDialect> bean = AutoBeanUtils.getAutoBean(databaseDialect);
    String databaseDialectJson = AutoBeanCodex.encode(bean).getPayload();
  
    try {
      registerDialectRequestBuilder.sendRequest(databaseDialectJson, new RequestCallback() {
 
        @Override
        public void onResponseReceived(Request request, Response response) {
          callback.success(null);
        }
 
        @Override
        public void onError(Request request, Throwable exception) {
          callback.error("error testing connection: ", exception);//$NON-NLS-1$
        }
        
      });
    } catch (RequestException e) {
      callback.error("error testing connection: ", e);//$NON-NLS-1$
    }
  }

  @Override
  public void registerDatabaseDialect(final IDatabaseDialect databaseDialect, final boolean validateClassExists,
      final XulServiceCallback<Void> callback) {
    RequestBuilder registerDialectRequestBuilder = new RequestBuilder( RequestBuilder.POST, getBaseUrl() +
      "registerDatabaseDialectWithValidation/" + Boolean.toString(validateClassExists) );
    registerDialectRequestBuilder.setHeader("Content-Type", "application/json");
    AutoBean<IDatabaseDialect> bean = AutoBeanUtils.getAutoBean(databaseDialect);
    String databaseDialectJson = AutoBeanCodex.encode(bean).getPayload();
  
    try {
      registerDialectRequestBuilder.sendRequest(databaseDialectJson, new RequestCallback() {
 
        @Override
        public void onResponseReceived(Request request, Response response) {
          callback.success(null);
        }
 
        @Override
        public void onError(Request request, Throwable exception) {
          callback.error("error testing connection: ", exception);//$NON-NLS-1$
        }
        
      });
    } catch (RequestException e) {
      callback.error("error testing connection: ", e);//$NON-NLS-1$
    }
  }

  @Override
  public void getDialect(final IDatabaseType databaseType, final XulServiceCallback<IDatabaseDialect> callback) {
    RequestBuilder registerDialectRequestBuilder = new RequestBuilder( RequestBuilder.POST, getBaseUrl() +
      "getDialectByType" );
    registerDialectRequestBuilder.setHeader("Content-Type", "application/json");
    AutoBean<IDatabaseType> bean = AutoBeanUtils.getAutoBean(databaseType);
    String databaseDialectJson = AutoBeanCodex.encode(bean).getPayload();
  
    try {
      registerDialectRequestBuilder.sendRequest(databaseDialectJson, new RequestCallback() {
 
        @Override
        public void onResponseReceived(Request request, Response response) {
          AutoBean<IDatabaseDialect> databaseDialectBean = AutoBeanCodex.decode(connectionAutoBeanFactory, IDatabaseDialect.class, response.getText());
          callback.success(databaseDialectBean.as());
        }
 
        @Override
        public void onError(Request request, Throwable exception) {
          callback.error("error testing connection: ", exception);//$NON-NLS-1$
        }
        
      });
    } catch (RequestException e) {
      callback.error("error testing connection: ", e);//$NON-NLS-1$
    }
  }

  @Override
  public void getDialect(final IDatabaseConnection connection, final XulServiceCallback<IDatabaseDialect> callback) {
    RequestBuilder registerDialectRequestBuilder = new RequestBuilder( RequestBuilder.POST, getBaseUrl() +
      "getDialectByConnection" );
    registerDialectRequestBuilder.setHeader("Content-Type", "application/json");
    AutoBean<IDatabaseConnection> bean = AutoBeanUtils.getAutoBean(connection);
    String connectionJson = AutoBeanCodex.encode(bean).getPayload();
  
    try {
      registerDialectRequestBuilder.sendRequest(connectionJson, new RequestCallback() {
 
        @Override
        public void onResponseReceived(Request request, Response response) {
          AutoBean<IDatabaseDialect> databaseDialectBean = AutoBeanCodex.decode(connectionAutoBeanFactory, IDatabaseDialect.class, response.getText());
          callback.success(databaseDialectBean.as());
        }
 
        @Override
        public void onError(Request request, Throwable exception) {
          callback.error("error testing connection: ", exception);//$NON-NLS-1$
        }
        
      });
    } catch (RequestException e) {
      callback.error("error testing connection: ", e);//$NON-NLS-1$
    }
  }

  @Override
  public void getDatabaseDialects(final XulServiceCallback<List<IDatabaseDialect>> callback) {
    RequestBuilder registerDialectRequestBuilder = new RequestBuilder( RequestBuilder.GET, getBaseUrl() +
      "getDatabaseDialects" );
    registerDialectRequestBuilder.setHeader("Content-Type", "application/json");
  
    try {
      registerDialectRequestBuilder.sendRequest(null, new RequestCallback() {
 
        @Override
        public void onResponseReceived(Request request, Response response) {
          AutoBean<IDatabaseDialectList> databaseDialectBean = AutoBeanCodex.decode(connectionAutoBeanFactory, IDatabaseDialectList.class, response.getText());
          callback.success(databaseDialectBean.as().getDialects());
        }
 
        @Override
        public void onError(Request request, Throwable exception) {
          callback.error("error testing connection: ", exception);//$NON-NLS-1$
        }
        
      });
    } catch (RequestException e) {
      callback.error("error testing connection: ", e);//$NON-NLS-1$
    }
  }

  @Override
  public void getDatabaseTypes(final XulServiceCallback<List<IDatabaseType>> callback) {
    RequestBuilder registerDialectRequestBuilder = new RequestBuilder( RequestBuilder.GET, getBaseUrl() +
      "getDatabaseTypes" );
    registerDialectRequestBuilder.setHeader("Content-Type", "application/json");
  
    try {
      registerDialectRequestBuilder.sendRequest(null, new RequestCallback() {
 
        @Override
        public void onResponseReceived(final Request request, final Response response) {
          Scheduler.get().scheduleDeferred( new Scheduler.ScheduledCommand() {
            @Override public void execute() {
              AutoBean<IDatabaseTypesList> databaseTypesBean = AutoBeanCodex.decode(connectionAutoBeanFactory, IDatabaseTypesList.class, response.getText());
              callback.success(databaseTypesBean.as().getDbTypes());
            }
          } );
        }
 
        @Override
        public void onError(Request request, Throwable exception) {
          callback.error("error testing connection: ", exception);//$NON-NLS-1$
        }
        
      });
    } catch (RequestException e) {
      callback.error("error testing connection: ", e);//$NON-NLS-1$
    }
  }

  @Override
  public void validateJdbcDriverClassExists(final String classname, final XulServiceCallback<Boolean> callback) {
    RequestBuilder registerDialectRequestBuilder = new RequestBuilder( RequestBuilder.POST, getBaseUrl() +
      "validateJdbcDriverClassExists" );
    registerDialectRequestBuilder.setHeader("Content-Type", "application/json");
  
    try {
      registerDialectRequestBuilder.sendRequest(classname, new RequestCallback() {
 
        @Override
        public void onResponseReceived(Request request, Response response) {
          callback.success(parseBoolean(response.getText()));
        }
 
        @Override
        public void onError(Request request, Throwable exception) {
          callback.error("error testing connection: ", exception);//$NON-NLS-1$
        }
        
      });
    } catch (RequestException e) {
      callback.error("error testing connection: ", e);//$NON-NLS-1$
    }
  }

  private static final native Boolean parseBoolean(String jsonBoolean) /*-{
    return JSON.parse(jsonBoolean);
  }-*/;
  
}
