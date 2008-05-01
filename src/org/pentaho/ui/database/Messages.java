package org.pentaho.ui.database;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
  private static final String BUNDLE_NAME = "org.pentaho.ui.database.messages.messages"; //$NON-NLS-1$

  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

  private Messages() {
  }

  public static ResourceBundle getBundle(){
    return RESOURCE_BUNDLE;
  }
  
  public static String getString(String key) {
    try {
      return RESOURCE_BUNDLE.getString(key);
    } catch (MissingResourceException e) {
      return '!' + key + '!';
    }
  }
  
  public static String getString(String key, String param1) {
    try {
      Object[] args = { param1 };
      return MessageFormat.format(getString(key), args);
    } catch (Exception e) {
      return '!' + key + '!';
    }
  }

  public static String getString(String key, String param1, String param2) {
    try {
      Object[] args = { param1, param2};
      return MessageFormat.format(getString(key), args);
    } catch (Exception e) {
      return '!' + key + '!';
    }
  }

  public static String getString(String key, String param1, String param2, String param3) {
    try {
      Object[] args = { param1, param2, param3};
      return MessageFormat.format(getString(key), args);
    } catch (Exception e) {
      return '!' + key + '!';
    }
  }

  public static String getString(String key, String param1, String param2, String param3, String param4) {
    try {
      Object[] args = { param1, param2, param3, param4 };
      return MessageFormat.format(getString(key), args);
    } catch (Exception e) {
      return '!' + key + '!';
    }
  }
  
}
