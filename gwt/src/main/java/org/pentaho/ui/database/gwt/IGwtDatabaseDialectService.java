/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 - 2026 by Pentaho Canada Inc. : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2030-06-15
 ******************************************************************************/



package org.pentaho.ui.database.gwt;

import org.pentaho.database.IDatabaseDialect;
import org.pentaho.database.service.IDatabaseDialectService;

import com.google.gwt.user.client.rpc.RemoteService;

public interface IGwtDatabaseDialectService extends IDatabaseDialectService, RemoteService {
  public IDatabaseDialect getBogoDatabase();
}
