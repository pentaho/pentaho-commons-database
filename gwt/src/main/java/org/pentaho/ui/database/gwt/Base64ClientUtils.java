/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2029-07-20
 ******************************************************************************/



package org.pentaho.ui.database.gwt;

public class Base64ClientUtils {

  private Base64ClientUtils() { }

  public static native String encode( String text ) /*-{
    return window.btoa( text );
  }-*/;

  public static native String decode( String encodedText ) /*-{
    return window.atob( encodedText );
  }-*/;
}
