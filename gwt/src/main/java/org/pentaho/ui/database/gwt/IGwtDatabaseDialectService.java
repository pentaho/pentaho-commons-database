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

import org.pentaho.database.IDatabaseDialect;
import org.pentaho.database.service.IDatabaseDialectService;

import com.google.gwt.user.client.rpc.RemoteService;

public interface IGwtDatabaseDialectService extends IDatabaseDialectService, RemoteService {
  public IDatabaseDialect getBogoDatabase();
}
