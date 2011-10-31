package org.pentaho.ui.database.gwt;

import org.pentaho.database.IDatabaseDialect;
import org.pentaho.database.service.IDatabaseDialectService;

import com.google.gwt.user.client.rpc.RemoteService;

public interface IGwtDatabaseDialectService extends IDatabaseDialectService, RemoteService {
  public IDatabaseDialect getBogoDatabase();
}
