/*
 * Copyright 2007 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt. The Original Code is the Pentaho 
 * BI Platform.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
 *
 * @created Jan 7, 2013 
 * @author wseyler
 */


package org.pentaho.ui.database.event;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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
