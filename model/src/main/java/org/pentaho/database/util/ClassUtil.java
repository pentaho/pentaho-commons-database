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
