/*******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2012 by Pentaho : http://www.pentaho.com
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

package org.pentaho.database.dialect;

import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.DatabaseType;
import org.pentaho.database.model.IDatabaseType;

/**
 * Vertica Analytic Database version 5 and later (changed driver class name) 
 * 
 * @author DEinspanjer
 * @since  2009-03-16
 * @author Matt
 * @since  May-2008
 * @author Jens
 * @since  Aug-2012
 */

public class Vertica5DatabaseDialect extends VerticaDatabaseDialect {
  
  private static final IDatabaseType DBTYPE = 
      new DatabaseType(
          "Vertica 5+",
          "VERTICA5",
          DatabaseAccessType.getList(
              DatabaseAccessType.NATIVE, 
              DatabaseAccessType.ODBC, 
              DatabaseAccessType.JNDI
          ), 
          50000, 
          null
      );    
    
  public Vertica5DatabaseDialect() {
  }

  public IDatabaseType getDatabaseType() {
    return DBTYPE;
  }
  
  @Override
  public String getNativeDriver() {
    return "com.vertica.jdbc.Driver";
  }

  /**
   * @return false as the database does not support timestamp to date
   *         conversion.
   */
  @Override
  public boolean supportsTimeStampToDateConversion() {
    return false;
  }
}
