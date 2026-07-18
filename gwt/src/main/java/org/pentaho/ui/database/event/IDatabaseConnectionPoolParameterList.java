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



package org.pentaho.ui.database.event;

import java.util.List;

import org.pentaho.database.model.IDatabaseConnectionPoolParameter;

/**
 * @author wseyler
 *
 */
public interface IDatabaseConnectionPoolParameterList {
  List<IDatabaseConnectionPoolParameter> getDatabaseConnectionPoolParameters();
  void setDatabaseConnectionPoolParameters(List<IDatabaseConnectionPoolParameter> databaseConnectionPoolParameters);
}
