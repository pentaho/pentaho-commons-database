package org.pentaho.ui.database.gwt;

public class Base64ClientUtils {

  public static native String encode( String text ) /*-{
    return window.btoa( text );
  }-*/;

  public static native String decode( String encodedText ) /*-{
    return window.atob( encodedText );
  }-*/;
}
