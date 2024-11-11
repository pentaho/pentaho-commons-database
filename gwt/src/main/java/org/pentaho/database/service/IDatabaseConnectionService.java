/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2029-07-20
 ******************************************************************************/


package org.pentaho.database.service;

import java.util.List;

import org.pentaho.database.model.DatabaseConnectionPoolParameter;
import org.pentaho.database.model.IDatabaseConnection;

public interface IDatabaseConnectionService {

  IDatabaseConnection createDatabaseConnection(String driver, String url);
  
  List<String> checkParameters(final IDatabaseConnection connection);

  String testConnection(final IDatabaseConnection connection);

  DatabaseConnectionPoolParameter[] getPoolingParameters();
  
}
