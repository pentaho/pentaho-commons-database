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


package org.pentaho.ui.database.gwt;

import org.pentaho.database.model.DatabaseConnection;
import org.pentaho.database.service.IDatabaseConnectionService;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * this interface makes the database connection service compatible with gwt's
 * remote service
 * 
 * @author Will Gorman (wgorman@pentaho.com)
 *
 */
public interface IGwtDatabaseConnectionService extends IDatabaseConnectionService, RemoteService {
  public DatabaseConnection getBogoDatabase();
}
