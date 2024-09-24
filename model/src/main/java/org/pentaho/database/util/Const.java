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

import java.util.List;

/**
 * User: Dzmitry Stsiapanau Date: 8/28/2015 Time: 11:07
 */
public class Const {

  public static final boolean isEmpty( String string ) {
    return string == null || string.length() == 0;
  }

  public static final boolean isEmpty( StringBuffer string ) {
    return string == null || string.length() == 0;
  }

  public static final boolean isEmpty( String[] strings ) {
    return strings == null || strings.length == 0;
  }

  public static final boolean isEmpty( Object[] array ) {
    return array == null || array.length == 0;
  }

  public static final boolean isEmpty( List<?> list ) {
    return list == null || list.size() == 0;
  }

}
