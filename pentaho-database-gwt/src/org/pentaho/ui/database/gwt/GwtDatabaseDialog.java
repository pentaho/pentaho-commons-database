package org.pentaho.ui.database.gwt;

import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.util.DatabaseTypeHelper;
import org.pentaho.gwt.widgets.client.utils.i18n.ResourceBundle;
import org.pentaho.ui.database.event.DataHandler;
import org.pentaho.ui.database.event.DatabaseDialogListener;
import org.pentaho.ui.database.event.GwtFragmentHandler;
import org.pentaho.ui.database.services.IXulAsyncDatabaseConnectionService;
import org.pentaho.ui.xul.containers.XulDialog;
import org.pentaho.ui.xul.gwt.GwtXulDomContainer;
import org.pentaho.ui.xul.gwt.GwtXulRunner;
import org.pentaho.ui.xul.gwt.util.AsyncXulLoader;
import org.pentaho.ui.xul.gwt.util.IXulLoaderCallback;

public class GwtDatabaseDialog {
  
  protected IXulAsyncDatabaseConnectionService connService;
  protected DatabaseTypeHelper databaseTypeHelper;
  protected DataHandler dataHandler = new DataHandler();
  protected GwtFragmentHandler fragmentHandler = new GwtFragmentHandler();
  protected GwtMessages messages = new GwtMessages();
  protected String overlay = null; // no overlay by default
  protected String overlayResource = "databasedialog"; //$NON-NLS-1$
  protected DatabaseDialogListener listener;
  protected XulDialog dialog;
  
  public GwtDatabaseDialog(IXulAsyncDatabaseConnectionService connService, DatabaseTypeHelper databaseTypeHelper, DatabaseDialogListener listener) {
    this.connService = connService;
    this.databaseTypeHelper = databaseTypeHelper;
    this.listener = listener;
    AsyncXulLoader.loadXulFromUrl("databasedialog.xul", "databasedialog", new InternalCallback()); //$NON-NLS-1$//$NON-NLS-2$
  }
  
  public GwtDatabaseDialog(IXulAsyncDatabaseConnectionService connService, DatabaseTypeHelper databaseTypeHelper, String overlay, DatabaseDialogListener listener) {
    this.connService = connService;
    this.databaseTypeHelper = databaseTypeHelper;
    this.overlay = overlay;
    this.listener = listener;
    AsyncXulLoader.loadXulFromUrl("databasedialog.xul", "databasedialog", new InternalCallback()); //$NON-NLS-1$//$NON-NLS-2$
  }
  
  public GwtDatabaseDialog(IXulAsyncDatabaseConnectionService connService, DatabaseTypeHelper databaseTypeHelper, String overlay, String overlayResource, DatabaseDialogListener listener) {
    this.connService = connService;
    this.databaseTypeHelper = databaseTypeHelper;
    this.overlay = overlay;
    this.overlayResource = overlayResource;
    this.listener = listener;
    AsyncXulLoader.loadXulFromUrl("databasedialog.xul", "databasedialog", new InternalCallback()); //$NON-NLS-1$//$NON-NLS-2$
  }
  
  public void show() {
    dialog.show();
    /*
    XulServiceCallback<List<IDatabaseType>> callback = new XulServiceCallback<List<IDatabaseType>>() {
      public void error(String message, Throwable error) {
      }
      public void success(List<IDatabaseType> retVal) {
        databaseTypeHelper = new DatabaseTypeHelper(retVal);
        dataHandler.setDatabaseTypeHelper(databaseTypeHelper);
        fragmentHandler.setDatabaseTypeHelper(databaseTypeHelper);
        AsyncXulLoader.loadXulFromUrl("databasedialog.xul", "databasedialog", new InternalCallback()); //$NON-NLS-1$//$NON-NLS-2$        
      }
    };
    connService.getDatabaseTypes(callback);
    */
  }
  
  public void setDatabaseConnection(IDatabaseConnection conn) {
    dataHandler.setData(conn);
  }
  
  public boolean isDialogReady() { 
    return dialog != null;
  }
  
  public IDatabaseConnection getDatabaseConnection() {
    return (IDatabaseConnection)dataHandler.getData();
  }
  
  private class InternalCallback implements IXulLoaderCallback {
    public void overlayLoaded() {}
    public void overlayRemoved() {}
    public void xulLoaded(GwtXulRunner runner) {
      try {
        // register our event handlers
        final GwtXulDomContainer container = (GwtXulDomContainer) runner.getXulDomContainers().get(0);
        messages.setMessageBundle((ResourceBundle) container.getResourceBundles().get(0));
        dataHandler.setMessages(messages);
        if (listener != null) {
          dataHandler.setDatabaseDialogListener(listener);
        }
        dataHandler.setLaunch(new GwtLaunch());
        dataHandler.setAsyncDatabaseConnectionService(connService);
        dataHandler.setDatabaseTypeHelper(databaseTypeHelper);
        dataHandler.setFragmentHandler(fragmentHandler);

        container.addEventHandler(dataHandler);
        
        fragmentHandler.setMessages(messages);
        fragmentHandler.setDatabaseTypeHelper(databaseTypeHelper);
        container.addEventHandler(fragmentHandler);

        runner.initialize();
        
        if (overlay != null) {
          IXulLoaderCallback callback2 = new IXulLoaderCallback() {
            public void overlayLoaded() {
              dialog = (XulDialog) container.getDocumentRoot().getElementById("general-datasource-window"); //$NON-NLS-1$
              if (listener != null) {
                listener.onDialogReady();
              }
            }
            public void overlayRemoved() {}
            public void xulLoaded(GwtXulRunner runner) {}
          };
          AsyncXulLoader.loadOverlayFromUrl(overlay, overlayResource, container, callback2);
        } else {
          dialog = (XulDialog) container.getDocumentRoot().getElementById("general-datasource-window"); //$NON-NLS-1$
          if (listener != null) {
            listener.onDialogReady();
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
