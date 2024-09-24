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

package org.pentaho.database.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum DatabaseAccessType {

  NATIVE( "Native (JDBC)" ), ODBC( "ODBC (deprecated)" ), OCI( "OCI" ), PLUGIN( "Plugin specific access method" ), JNDI( "JNDI" ), CUSTOM(
      "Custom" );

  private String name;

  private static Map<String, DatabaseAccessType> typeByName = null;

  private DatabaseAccessType( String name ) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String getValue() {
    return this.toString();
  }

  public static DatabaseAccessType getAccessTypeByName( String name ) {
    if ( typeByName == null ) {
      typeByName = new HashMap<String, DatabaseAccessType>();
      for ( DatabaseAccessType type : values() ) {
        typeByName.put( type.getName(), type );
      }
    }
    return typeByName.get( name );
  }

  public static List<DatabaseAccessType> getList( DatabaseAccessType... accessTypes ) {
    ArrayList<DatabaseAccessType> list = new ArrayList<DatabaseAccessType>();
    for ( DatabaseAccessType accessType : accessTypes ) {
      list.add( accessType );
    }
    return list;
  }
}
