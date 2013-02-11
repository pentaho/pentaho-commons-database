package org.pentaho.ui.database;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.pentaho.ui.database.event.IMessages;

public class Messages implements IMessages {
  
  private static final String BUNDLE_NAME = "org.pentaho.ui.database.resources.databasedialog"; //$NON-NLS-1$

  private ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

  public Messages() {
  }

  public ResourceBundle getBundle() {
    if(RESOURCE_BUNDLE == null){
      RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
    }
      
    return RESOURCE_BUNDLE;
  }
  
  public String getString(String key) {
    try {
      return RESOURCE_BUNDLE.getString(key);
    } catch (MissingResourceException e) {
      return '!' + key + '!';
    }
  }

  public String getString(String key, String... params) {
    try {
      return MessageFormat.format(getString(key), (Object[])params);
    } catch (Exception e) {
      return '!' + key + '!';
    }
  }
}
