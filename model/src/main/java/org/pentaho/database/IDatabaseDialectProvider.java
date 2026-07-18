/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 - 2026 by Pentaho Canada Inc. : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2030-06-15
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
