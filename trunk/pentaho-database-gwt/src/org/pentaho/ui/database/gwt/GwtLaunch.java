package org.pentaho.ui.database.gwt;

import org.pentaho.ui.database.event.ILaunch;
import org.pentaho.ui.database.event.IMessages;

import com.google.gwt.user.client.Window;

public class GwtLaunch implements ILaunch {

  public Status openUrl(String url, IMessages messages) {
    Window.open(url, "_blank", "");  //$NON-NLS-1$ //$NON-NLS-2$
    return Status.Success;
  }
  
}
