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
