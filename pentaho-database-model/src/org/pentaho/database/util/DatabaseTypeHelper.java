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

package org.pentaho.database.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.pentaho.database.model.IDatabaseType;

public class DatabaseTypeHelper {

  List<IDatabaseType> databaseTypes;
  List<String> databaseTypeNames = null;
  Map<String, IDatabaseType> databaseTypeByName = null;
  Map<String, IDatabaseType> databaseTypeByShortName = null;

  public DatabaseTypeHelper( List<IDatabaseType> databaseTypes ) {
    this.databaseTypes = databaseTypes;
  }

  private void init() {
    List<String> dbTypeNames = new ArrayList<String>();
    Map<String, IDatabaseType> dbTypeByName = new TreeMap<String, IDatabaseType>();
    Map<String, IDatabaseType> dbTypeByShortName = new TreeMap<String, IDatabaseType>();
    for ( IDatabaseType dbtype : databaseTypes ) {
      dbTypeNames.add( dbtype.getName() );
      dbTypeByName.put( dbtype.getName(), dbtype );
      dbTypeByShortName.put( dbtype.getShortName().toUpperCase(), dbtype );
    }
    databaseTypeNames = dbTypeNames; // );
    databaseTypeByName = dbTypeByName; // );
    databaseTypeByShortName = dbTypeByShortName; // );

  }

  public List<String> getDatabaseTypeNames() {
    if ( databaseTypeNames == null ) {
      init();
    }
    return databaseTypeNames;
  }

  public IDatabaseType getDatabaseTypeByName( String name ) {
    if ( databaseTypeByName == null ) {
      init();
    }
    return databaseTypeByName.get( name );
  }

  public IDatabaseType getDatabaseTypeByShortName( String name ) {
    if ( databaseTypeByShortName == null ) {
      init();
    }
    IDatabaseType _dbType = databaseTypeByShortName.get( name );
    if ( _dbType == null && name != null ) {
      _dbType = databaseTypeByShortName.get( name.toUpperCase() );
    }
    return _dbType;
  }

}
