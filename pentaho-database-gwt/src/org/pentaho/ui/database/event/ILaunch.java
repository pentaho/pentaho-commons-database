package org.pentaho.ui.database.event;

public interface ILaunch {
  
  public enum Status {
    Success, Failed
  };
  
  Status openUrl(String url, IMessages messages);
}
