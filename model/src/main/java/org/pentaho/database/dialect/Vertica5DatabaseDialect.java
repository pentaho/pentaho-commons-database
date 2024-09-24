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

package org.pentaho.database.dialect;

import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.DatabaseType;
import org.pentaho.database.model.IDatabaseType;

/**
 * Vertica Analytic Database version 5 and later (changed driver class name)
 * 
 * @author DEinspanjer
 * @since 2009-03-16
 * @author Matt
 * @since May-2008
 * @author Jens
 * @since Aug-2012
 */

public class Vertica5DatabaseDialect extends VerticaDatabaseDialect {

  /**
   * 
   */
  private static final long serialVersionUID = -7791425035679133135L;
  private static final IDatabaseType DBTYPE = new DatabaseType( "Vertica 5+", "VERTICA5", DatabaseAccessType.getList(
      DatabaseAccessType.NATIVE, DatabaseAccessType.ODBC, DatabaseAccessType.JNDI ), 5433, null );

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
   * @return false as the database does not support timestamp to date conversion.
   */
  @Override
  public boolean supportsTimeStampToDateConversion() {
    return false;
  }
}
