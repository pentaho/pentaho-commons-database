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

import java.sql.Driver;

/**
 * Created by bryan on 5/11/16.
 */
public interface IDriverLocator {
  /**
   * Attempt to determine if driver is available. If it's not available, return false.
   *
   * @return true if the driver is available
   */
  boolean isUsable();

  /**
   * Performs any necessary actions to initialise the class via the dialect
   *
   * @param classname
   * @return a boolean indicating whether the class was accessable and initializable
   */
  boolean initialize( String classname );

  /**
   * Returns a driver for the given url
   *
   * @param url the url
   * @return the driver
   */
  Driver getDriver( String url );
}
