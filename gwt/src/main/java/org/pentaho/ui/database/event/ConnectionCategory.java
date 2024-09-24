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

package org.pentaho.ui.database.event;

import java.util.HashMap;

import org.pentaho.database.model.IDatabaseConnection;

import com.google.web.bindery.autobean.shared.AutoBean;

/**
 * @author wseyler
 *
 */
public class ConnectionCategory {
  public static void addExtraOption(AutoBean<IDatabaseConnection> instance, String databaseTypeCode, String option,
      String value) {
    IDatabaseConnection dbConnection = instance.as();

    if (dbConnection.getExtraOptions() == null) {
      dbConnection.setExtraOptions(new HashMap<String, String>());
    }

    dbConnection.getExtraOptions().put(databaseTypeCode + "." + option, value); //$NON-NLS-1$
  }
}
