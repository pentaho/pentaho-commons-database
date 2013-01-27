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

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

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
