/*
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
 * Copyright 2007-2013 Pentaho Corporation.  All rights reserved.
 *
 */
package org.pentaho.ui.database.event;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.pentaho.database.model.DatabaseConnection;
import org.pentaho.database.model.IDatabaseConnection;

/**
 * @author wseyler
 *
 */
@XmlRootElement
public class DefaultDatabaseConnectionList implements IDatabaseConnectionList {
  private List<IDatabaseConnection> databaseConnections;
  
/* (non-Javadoc)
   * @see org.pentaho.ui.database.event.IDatabaseConnectionPoolParameterList#getDatabaseConnectionPoolParameters()
   */
  @Override
  public List<IDatabaseConnection> getDatabaseConnections() {
    return databaseConnections;
  }

  /* (non-Javadoc)
   * @see org.pentaho.ui.database.event.IDatabaseConnectionPoolParameterList#getDatabaseConnectionPoolParameters(java.util.List)
   */
  @Override
  @XmlElement(type=DatabaseConnection.class)
  public void setDatabaseConnections(List<IDatabaseConnection> databaseConnections) {
    this.databaseConnections = databaseConnections;
  }

}
