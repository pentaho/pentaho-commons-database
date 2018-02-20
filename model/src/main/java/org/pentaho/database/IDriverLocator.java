/*!
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 * or from the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright (c) 2002-2017 Hitachi Vantara..  All rights reserved.
 */

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
