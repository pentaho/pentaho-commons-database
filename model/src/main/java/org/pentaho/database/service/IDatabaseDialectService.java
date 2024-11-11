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


package org.pentaho.database.service;

import java.util.List;

import org.pentaho.database.IDatabaseDialect;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.model.IDatabaseType;

public interface IDatabaseDialectService {

  public void registerDatabaseDialect( IDatabaseDialect databaseDialect );

  public void registerDatabaseDialect( IDatabaseDialect databaseDialect, boolean validateClassExists );

  public IDatabaseDialect getDialect( IDatabaseType databaseType );

  public IDatabaseDialect getDialect( IDatabaseConnection connection );

  public List<IDatabaseDialect> getDatabaseDialects();

  public List<IDatabaseType> getDatabaseTypes();

  public boolean validateJdbcDriverClassExists( String classname );

}
