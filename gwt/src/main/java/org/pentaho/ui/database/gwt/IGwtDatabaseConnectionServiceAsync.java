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
