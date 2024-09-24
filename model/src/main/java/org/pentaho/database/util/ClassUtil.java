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
package org.pentaho.database.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by bryan on 5/6/16.
 */
public class ClassUtil {
  private static final Log logger = LogFactory.getLog( ClassUtil.class );

  public static boolean canLoadClass( String classname ) {
    // no need to test if the class exists if it is null
    if ( classname == null ) {
      return true;
    }
    return getClass( classname ) != null;
  }

  public static Class<?> getClass( String classname ) {
    if ( classname == null ) {
      return null;
    }
    try {
      return Class.forName( classname );
    } catch ( NoClassDefFoundError e ) {
      if ( logger.isDebugEnabled() ) {
        logger.debug( "classExists returning false", e );
      }
    } catch ( ClassNotFoundException e ) {
      if ( logger.isDebugEnabled() ) {
        logger.debug( "classExists returning false", e );
      }
    } catch ( Exception e ) {
      if ( logger.isDebugEnabled() ) {
        logger.debug( "classExists returning false", e );
      }
    }
    // if we've made it here, an exception has occurred.
    return null;
  }
}
