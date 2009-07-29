package org.pentaho.ui.database.gwt;

import org.pentaho.gwt.widgets.client.utils.i18n.ResourceBundle;
import org.pentaho.ui.database.event.IMessages;

public class GwtMessages implements IMessages {
  
  ResourceBundle messageBundle;
  
  public GwtMessages() {
    
  }
  public GwtMessages(ResourceBundle messageBundle){
    this.messageBundle = messageBundle;
  }

  public String getString(String key) {
    if (this.messageBundle == null) {
      return key;
    }
    String val = this.messageBundle.getString(key);
    return val.replaceAll("\\:", ":");
  }

  public String getString(String key, String... parameters) {
    if (this.messageBundle == null) {
      return key;
    }
    String val = this.messageBundle.getString(key, key, parameters);
    return val.replaceAll("\\:", ":");
  }

  public ResourceBundle getMessageBundle() {
    return this.messageBundle;
  }
  public void setMessageBundle(ResourceBundle messageBundle) {
    this.messageBundle = messageBundle;
  }
}
