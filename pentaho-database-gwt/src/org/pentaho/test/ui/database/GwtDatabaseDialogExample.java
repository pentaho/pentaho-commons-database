package org.pentaho.test.ui.database;

import java.util.List;

import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.DatabaseConnection;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.model.IDatabaseType;
import org.pentaho.database.util.DatabaseTypeHelper;
import org.pentaho.ui.database.event.DatabaseDialogListener;
import org.pentaho.ui.database.gwt.GwtDatabaseDialog;
import org.pentaho.ui.database.gwt.GwtXulAsyncDatabaseConnectionService;
import org.pentaho.ui.database.gwt.GwtXulAsyncDatabaseDialectService;
import org.pentaho.ui.xul.XulServiceCallback;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class GwtDatabaseDialogExample implements EntryPoint, DatabaseDialogListener {

  GwtXulAsyncDatabaseConnectionService connService = new GwtXulAsyncDatabaseConnectionService();
  GwtXulAsyncDatabaseDialectService dialectService = new GwtXulAsyncDatabaseDialectService();
  GwtDatabaseDialog dialog;
  DatabaseTypeHelper databaseTypeHelper;
  Button button = new Button("Loading...");
  Label label = new Label("no data");
  
  IDatabaseConnection connection;
  
  public void onModuleLoad() {
    button.setEnabled(false);
    RootPanel.get().add(button);
    RootPanel.get().add(label);

    button.addClickHandler(new ClickHandler() {

      public void onClick(ClickEvent event) {
        // TODO Auto-generated method stub
        if (connection == null) {
          connection = new DatabaseConnection();
          connection.setDatabaseType(databaseTypeHelper.getDatabaseTypeByName("Hypersonic"));
          connection.setAccessType(DatabaseAccessType.NATIVE);
          connection.setHostname("localhost");
          connection.setName("My Connection");
        }
        dialog.setDatabaseConnection(connection);
        dialog.show();
      }
      
    });
    
    XulServiceCallback<List<IDatabaseType>> callback = new XulServiceCallback<List<IDatabaseType>>() {
      public void error(String message, Throwable error) {
      }
      public void success(List<IDatabaseType> retVal) {
        databaseTypeHelper = new DatabaseTypeHelper(retVal);
        dialog = new GwtDatabaseDialog(connService, databaseTypeHelper, "testoverlay.xul", GwtDatabaseDialogExample.this); //$NON-NLS-1$
      }
    };
    dialectService.getDatabaseTypes(callback);
  }

  public void onDialogAccept(IDatabaseConnection connection) {
    this.connection = connection;
    label.setText(connection.getName() + ": " + connection.getDatabaseName());      
  }

  public void onDialogCancel() {
    this.connection = null;
    label.setText("Cancel Pressed");
  }

  public void onDialogReady() {
    button.setText("Test...");
    button.setEnabled(true);
  }
}
