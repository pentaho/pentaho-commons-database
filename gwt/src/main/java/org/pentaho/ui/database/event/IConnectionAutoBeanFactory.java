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
