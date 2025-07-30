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

import jakarta.xml.bind.annotation.XmlAnyElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import org.pentaho.database.IDatabaseDialect;

/**
 * @author wseyler
 *
 */
@XmlRootElement
public class DefaultDatabaseDialectList implements IDatabaseDialectList {
 
  private List<IDatabaseDialect> dialects;
  
  public DefaultDatabaseDialectList() {
    super();
  }
  
  /* (non-Javadoc)
   * @see org.pentaho.ui.database.event.IDatabaseDialectList#getDialects()
   */
  @Override
  @XmlAnyElement
  public List<IDatabaseDialect> getDialects() {
    return dialects;
  }

  /* (non-Javadoc)
   * @see org.pentaho.ui.database.event.IDatabaseDialectList#setDialects(java.util.List)
   */
  @Override
  public void setDialects(List<IDatabaseDialect> dialects) {
    this.dialects = dialects;
  }

}
