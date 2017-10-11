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
