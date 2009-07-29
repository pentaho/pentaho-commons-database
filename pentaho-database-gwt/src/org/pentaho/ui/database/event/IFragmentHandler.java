package org.pentaho.ui.database.event;

public interface IFragmentHandler {
  public interface Callback {
    public void callback();
  }
  public void setDisableRefresh(boolean disableRefresh);
  public void refreshOptionsWithCallback(Callback callback); 
}
