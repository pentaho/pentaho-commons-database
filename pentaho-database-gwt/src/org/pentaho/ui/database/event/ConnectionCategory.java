/*
* Copyright 2002 - 2017 Hitachi Vantara.  All rights reserved.
* 
* This software was developed by Hitachi Vantara and is provided under the terms
* of the Mozilla Public License, Version 1.1, or any later version. You may not use
* this file except in compliance with the license. If you need a copy of the license,
* please go to http://www.mozilla.org/MPL/MPL-1.1.txt. TThe Initial Developer is Pentaho Corporation.
*
* Software distributed under the Mozilla Public License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to
* the license for the specific language governing your rights and limitations.
*/

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
