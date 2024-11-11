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


package org.pentaho.ui.database;

import java.util.List;

import org.pentaho.database.IDatabaseDialect;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.model.IDatabaseType;
import org.pentaho.database.service.IDatabaseDialectService;
import org.pentaho.ui.database.services.IXulAsyncDatabaseDialectService;
import org.pentaho.ui.xul.XulServiceCallback;

public class XulAsyncDatabaseDialectService implements IXulAsyncDatabaseDialectService{

  IDatabaseDialectService service;
  
  public XulAsyncDatabaseDialectService() {
  }
  
  public XulAsyncDatabaseDialectService(IDatabaseDialectService service) {
    this.service = service;
  }
  
  public void setDatabaseDialectService(IDatabaseDialectService service) {
    this.service = service;
  }

  
  @Override
  public void registerDatabaseDialect(IDatabaseDialect databaseDialect, XulServiceCallback<Void> callback) {
    service.registerDatabaseDialect(databaseDialect);
  }

  @Override
  public void registerDatabaseDialect(IDatabaseDialect databaseDialect, boolean validateClassExists,
      XulServiceCallback<Void> callback) {
    service.registerDatabaseDialect(databaseDialect, validateClassExists);
    
  }

  @Override
  public void getDialect(IDatabaseType databaseType, XulServiceCallback<IDatabaseDialect> callback) {
    callback.success(service.getDialect(databaseType));
  }

  @Override
  public void getDialect(IDatabaseConnection connection, XulServiceCallback<IDatabaseDialect> callback) {
    callback.success(service.getDialect(connection));
  }

  @Override
  public void getDatabaseDialects(XulServiceCallback<List<IDatabaseDialect>> callback) {
    callback.success(service.getDatabaseDialects());
  }

  @Override
  public void getDatabaseTypes(XulServiceCallback<List<IDatabaseType>> callback) {
    callback.success(service.getDatabaseTypes());
  }

  @Override
  public void validateJdbcDriverClassExists(String classname, XulServiceCallback<Boolean> callback) {
    callback.success(service.validateJdbcDriverClassExists(classname));
  }

}
