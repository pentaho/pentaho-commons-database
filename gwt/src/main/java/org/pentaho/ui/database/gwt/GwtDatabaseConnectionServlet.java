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

  public IDatabaseConnection createDatabaseConnection(String driver, String url) {
    return service.createDatabaseConnection(driver, url);
  }
}
