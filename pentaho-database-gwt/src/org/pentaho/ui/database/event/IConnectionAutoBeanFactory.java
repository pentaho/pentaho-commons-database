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
 * @created Nov 26, 2012 
 * @author wseyler
 */


package org.pentaho.ui.database.event;

import org.pentaho.database.IDatabaseDialect;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.model.IDatabaseType;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.autobean.shared.AutoBeanFactory.Category;

/**
 * @author wseyler
 *
 */
@Category({ConnectionCategory.class, DatabaseDialectCategory.class})
public interface IConnectionAutoBeanFactory extends AutoBeanFactory {
  AutoBean<IDatabaseConnection> iDatabaseConnection();
  AutoBean<IDatabaseConnectionList> iDatabaseConnectionList();
  AutoBean<IDatabaseDialect> iDatabaseDialect();
  AutoBean<IDatabaseType> iDatabaseType();
  AutoBean<IDatabaseDialectList> iDatabaseDialectList();
  AutoBean<IDatabaseTypesList> iDatabaseTypesList();
  AutoBean<IDatabaseConnectionPoolParameterList> iDatabaseConnectionPoolParameterList();
}
