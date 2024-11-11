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


package org.pentaho.database.model;

import java.util.List;
import java.util.Map;

public interface IDatabaseType {

  String getName();

  String getShortName();

  List<DatabaseAccessType> getSupportedAccessTypes();

  int getDefaultDatabasePort();

  String getDefaultDatabaseName();

  Map<String, String> getDefaultOptions();

  void setDefaultOptions( Map<String, String> options );

  String getExtraOptionsHelpUrl();

}
