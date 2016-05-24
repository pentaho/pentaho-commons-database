/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2015 by Pentaho : http://www.pentaho.com
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
