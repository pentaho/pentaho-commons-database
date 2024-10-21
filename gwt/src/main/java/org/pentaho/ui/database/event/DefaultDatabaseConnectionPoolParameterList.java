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

import org.pentaho.database.model.DatabaseConnectionPoolParameter;
import org.pentaho.database.model.IDatabaseConnectionPoolParameter;

/**
 * @author wseyler
 *
 */
@XmlRootElement
public class DefaultDatabaseConnectionPoolParameterList implements IDatabaseConnectionPoolParameterList {
  private List<IDatabaseConnectionPoolParameter> databaseConnectionPoolParameters;
  
/* (non-Javadoc)
   * @see org.pentaho.ui.database.event.IDatabaseConnectionPoolParameterList#getDatabaseConnectionPoolParameters()
   */
  @Override
  public List<IDatabaseConnectionPoolParameter> getDatabaseConnectionPoolParameters() {
    return databaseConnectionPoolParameters;
  }

  /* (non-Javadoc)
   * @see org.pentaho.ui.database.event.IDatabaseConnectionPoolParameterList#getDatabaseConnectionPoolParameters(java.util.List)
   */
  @Override
  @XmlElement(type=DatabaseConnectionPoolParameter.class)
  public void setDatabaseConnectionPoolParameters(List<IDatabaseConnectionPoolParameter> databaseConnectionPoolParameters) {
    this.databaseConnectionPoolParameters = databaseConnectionPoolParameters;
  }

}
