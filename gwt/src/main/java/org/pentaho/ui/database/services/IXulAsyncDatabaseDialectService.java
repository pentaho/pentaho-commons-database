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

package org.pentaho.ui.database.services;

import java.util.List;

import org.pentaho.database.IDatabaseDialect;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.model.IDatabaseType;
import org.pentaho.ui.xul.XulServiceCallback;

public interface IXulAsyncDatabaseDialectService {

  void registerDatabaseDialect(IDatabaseDialect databaseDialect, XulServiceCallback<Void> callback);

  void registerDatabaseDialect(IDatabaseDialect databaseDialect, boolean validateClassExists, XulServiceCallback<Void> callback);
  
  void getDialect(IDatabaseType databaseType, XulServiceCallback<IDatabaseDialect> callback);
  
  void getDialect(IDatabaseConnection connection, XulServiceCallback<IDatabaseDialect> callback);
  
  void getDatabaseDialects(XulServiceCallback<List<IDatabaseDialect>> callback);
  
  void getDatabaseTypes(XulServiceCallback<List<IDatabaseType>> callback);
  
  void validateJdbcDriverClassExists(String classname, XulServiceCallback<Boolean> callback);

}
