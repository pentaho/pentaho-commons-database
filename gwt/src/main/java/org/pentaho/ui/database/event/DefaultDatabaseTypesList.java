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

import org.pentaho.database.model.DatabaseType;
import org.pentaho.database.model.IDatabaseType;

/**
 * @author wseyler
 *
 */
@XmlRootElement
public class DefaultDatabaseTypesList implements IDatabaseTypesList {
  
  private List<IDatabaseType> types;

  public DefaultDatabaseTypesList() {
    super();
  }

  /* (non-Javadoc)
   * @see org.pentaho.ui.database.event.IDatabaseTypesList#getTypes()
   */
  @Override
  @XmlElement(type=DatabaseType.class)
  public List<IDatabaseType> getDbTypes() {
    return types;
  }

  /* (non-Javadoc)
   * @see org.pentaho.ui.database.event.IDatabaseTypesList#setTypes(java.util.List)
   */
  @Override
  public void setDbTypes(List<IDatabaseType> types) {
    this.types = types;
  }

}
