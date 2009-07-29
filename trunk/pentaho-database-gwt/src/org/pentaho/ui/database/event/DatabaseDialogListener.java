package org.pentaho.ui.database.event;

import org.pentaho.database.model.IDatabaseConnection;

public interface DatabaseDialogListener {
  public void onDialogReady();
  public void onDialogAccept(IDatabaseConnection connection);
  public void onDialogCancel();
}
