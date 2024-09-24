/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2028-08-13
 ******************************************************************************/
package org.pentaho.database;

import org.pentaho.database.model.IDatabaseType;

import java.util.Collection;

/**
 * Created by bryan on 5/6/16.
 */
public interface IDatabaseDialectProvider {
  Collection<IDatabaseDialect> getDialects( boolean usableOnly );
  IDatabaseDialect getDialect( boolean usableOnly, IDatabaseType databaseType );
}
