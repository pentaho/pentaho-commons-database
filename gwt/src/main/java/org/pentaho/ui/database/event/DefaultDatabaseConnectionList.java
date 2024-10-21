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


package org.pentaho.ui.database.event;

import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

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
