package org.pentaho.ui.database.event;

//import java.io.InputStream;

import java.util.Set;
import java.util.TreeSet;

import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.model.IDatabaseType;
import org.pentaho.database.util.DatabaseTypeHelper;
import org.pentaho.ui.database.event.IMessages;
import org.pentaho.ui.xul.XulComponent;
import org.pentaho.ui.xul.XulDomContainer;
import org.pentaho.ui.xul.XulException;
import org.pentaho.ui.xul.components.XulListitem;
import org.pentaho.ui.xul.components.XulMessageBox;
import org.pentaho.ui.xul.components.XulTextbox;
import org.pentaho.ui.xul.containers.XulHbox;
import org.pentaho.ui.xul.containers.XulListbox;
import org.pentaho.ui.xul.gwt.GwtXulDomContainer;
import org.pentaho.ui.xul.gwt.GwtXulRunner;
import org.pentaho.ui.xul.gwt.tags.GwtGroupBox;
import org.pentaho.ui.xul.gwt.tags.GwtHbox;
import org.pentaho.ui.xul.gwt.util.AsyncXulLoader;
import org.pentaho.ui.xul.gwt.util.IXulLoaderCallback;
import org.pentaho.ui.xul.impl.AbstractXulEventHandler;
import org.pentaho.ui.xul.stereotype.Bindable;

/**
 * Fragment handler deals with the logistics of replacing a portion of the dialog
 * from a XUL fragment when the combination of database connection type and database
 * access method calls for a replacement.
 *
 * @author gmoran
 * @created Mar 19, 2008
 */
public class GwtFragmentHandler extends AbstractXulEventHandler implements IFragmentHandler {

  private XulListbox connectionBox;
  private XulListbox accessBox;
  private DatabaseTypeHelper databaseTypeHelper;
  private IMessages messages;
  Set<String> supportedFragments = new TreeSet<String>();

  private boolean disableRefresh = false;

  public GwtFragmentHandler() {
    setName("fragmentHandler");
    supportedFragments.add("generic_native.xul");
    supportedFragments.add("informix_jndi.xul");
    supportedFragments.add("informix_native.xul");
    supportedFragments.add("mssql_jndi.xul");
    supportedFragments.add("mssql_native.xul");
    supportedFragments.add("mysql_jndi.xul");
    supportedFragments.add("mysql_native.xul");
    supportedFragments.add("oracle_jndi.xul");
    supportedFragments.add("oracle_native.xul");
    supportedFragments.add("oracle_oci.xul");
    supportedFragments.add("oracle_odbc.xul");
    supportedFragments.add("sapr3_plugin.xul");
  }

  public void setDisableRefresh(boolean disableRefresh) {
    this.disableRefresh = disableRefresh;
  }

  public void setDatabaseTypeHelper(DatabaseTypeHelper databaseTypeHelper) {
    this.databaseTypeHelper = databaseTypeHelper;
  }

  public void setMessages(IMessages messages) {
    this.messages = messages;
  }

  private void loadDatabaseOptionsFragment(String fragmentUri, final DataHandler dataHandler, final IDatabaseType database, final IFragmentHandler.Callback parentCallback) throws XulException{

    // clean out group before reloading
    XulComponent groupElement = document.getElementById("database-options-box"); //$NON-NLS-1$
    for (XulComponent component : groupElement.getChildNodes()) {
      groupElement.removeChild(component);

    }

    XulComponent parentElement = groupElement.getParent();
    XulDomContainer fragmentContainer = null;

//    try {

      // Get new group box fragment ...
      // This will effectively set up the SWT parent child relationship...

      IXulLoaderCallback internalCallback = new IXulLoaderCallback() {
        public void overlayLoaded() {
          // TODO Auto-generated method stub
          afterOverlay(dataHandler, database);
          if (parentCallback != null) {
            parentCallback.callback();
          }
        }
        public void overlayRemoved() {}
        public void xulLoaded(GwtXulRunner runner) {}
      };

      // this call will cache the individual overlays in a map within AsyncXulLoader
      AsyncXulLoader.loadOverlayFromUrl(fragmentUri, "databasedialog", (GwtXulDomContainer)getXulDomContainer(), internalCallback, true);

//      fragmentContainer = this.xulDomContainer.loadFragment(fragmentUri, (Object)null); //messages.getBundle());
//      XulComponent newGroup = fragmentContainer.getDocumentRoot().getFirstChild();
//      parentElement.replaceChild(groupElement, newGroup);

//    } catch (XulException e) {
//      e.printStackTrace();
//      throw e;
//    }

//    if (fragmentContainer == null){
//      return;
//    }

  }

  @Bindable
  public void refreshOptions() {
    if (!disableRefresh) {
      refreshOptionsWithCallback(null);
    }
  }

  /**
   * This method handles the resource-like loading of the XUL
   * fragment definitions based on connection type and access
   * method. If there is a common definition, and no connection
   * specific override definition, then the common definition is used.
   * Connection specific definition resources follow the naming
   * pattern [connection type code]_[access method].xul.
   */
  public void refreshOptionsWithCallback(final IFragmentHandler.Callback callback) {

    connectionBox = (XulListbox)document.getElementById("connection-type-list"); //$NON-NLS-1$
    accessBox = (XulListbox)document.getElementById("access-type-list"); //$NON-NLS-1$

    String connectionKey = getSelectedString(connectionBox);
    if(connectionKey == null){
      return;
    }
//    DatabaseInterface database = DataHandler.connectionMap.get(connectionKey);
    IDatabaseType database = databaseTypeHelper.getDatabaseTypeByName(connectionKey);

    String accessKey = getSelectedString(accessBox);
    DatabaseAccessType access = DatabaseAccessType.getAccessTypeByName(accessKey);

    if (access == null) {
      return;
    }

    String fragment = null;

    DataHandler dataHandler = null;
    try {
      dataHandler = (DataHandler)xulDomContainer.getEventHandler("dataHandler"); //$NON-NLS-1$
      dataHandler.pushCache();
    } catch (XulException e) {
      // TODO not a critical function, but should log a problem...
    }

    switch(access){
      case JNDI:
        fragment = getFragment(database, "_jndi.xul", "common_jndi.xul"); //$NON-NLS-1$ //$NON-NLS-2$
        break;
      case NATIVE:
        fragment = getFragment(database, "_native.xul", "common_native.xul"); //$NON-NLS-1$ //$NON-NLS-2$
        break;
      case OCI:
        fragment = getFragment(database, "_oci.xul", "common_native.xul"); //$NON-NLS-1$ //$NON-NLS-2$
        break;
      case ODBC:
        fragment = getFragment(database, "_odbc.xul", "common_odbc.xul"); //$NON-NLS-1$ //$NON-NLS-2$
        break;
      case PLUGIN:
        fragment = getFragment(database, "_plugin.xul", "common_native.xul"); //$NON-NLS-1$ //$NON-NLS-2$
        break;
    }

    try {
      loadDatabaseOptionsFragment(fragment.toLowerCase(), dataHandler, database, callback);
    } catch (XulException e) {
      // TODO should be reporting as an error dialog; need error dialog in XUL framework
      showMessage(
        messages.getString("FragmentHandler.USER.CANT_LOAD_OPTIONS", database.getName()) //$NON-NLS-1$
      );
    }
  }

  private void afterOverlay(DataHandler dataHandler, IDatabaseType database) {
    XulTextbox portBox = (XulTextbox)document.getElementById("port-number-text"); //$NON-NLS-1$
    Object data = dataHandler.getData();
    String portValue = null;
    IDatabaseConnection databaseConnection = null;
    // Extract the stored value for port number in the model
    if (data instanceof IDatabaseConnection) {
      databaseConnection = (IDatabaseConnection) data;
      portValue = databaseConnection.getDatabasePort();
    }
    if (portBox != null){
      // If the model has the port number use it other wise use the default one for the selected database
      int port = (portValue != null && portValue.length() > 0) ? Integer.parseInt(portValue) : database.getDefaultDatabasePort();
      if (port > 0){
        portBox.setValue(Integer.toString(port));
      }
    }

   if (dataHandler != null){
     dataHandler.popCache();
   }

   GwtGroupBox box1 = (GwtGroupBox)document.getElementById("database-options-box");

   XulHbox box = (XulHbox)document.getElementById("connection-access-list-box");

   box1.layout();

   ((GwtHbox)box).layout();

  }

  private String getSelectedString(XulListbox box) {
    String key = null;
    Object keyObj = box.getSelectedItem();
    if (keyObj instanceof XulListitem) {
      key = (String)((XulListitem)keyObj).getLabel();
    } else {
      key = (String)keyObj;
    }
    return key;
  }


  private String getFragment(IDatabaseType database, String extension, String defaultFragment ){
    String fragment = database.getShortName().concat(extension).toLowerCase();
    if (!supportedFragments.contains(fragment)) {
      fragment = defaultFragment;
    }
    return fragment;
  }

  private void showMessage(String message){
    try{
      XulMessageBox box = (XulMessageBox) document.createElement("messagebox"); //$NON-NLS-1$
      box.setMessage(message);
      box.open();
    } catch(XulException e){
      System.out.println("Error creating messagebox "+e.getMessage());
    }
  }
}
