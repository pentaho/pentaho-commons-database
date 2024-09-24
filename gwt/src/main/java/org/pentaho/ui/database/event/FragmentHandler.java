/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2028-08-13
 ******************************************************************************/

package org.pentaho.ui.database.event;

import java.io.InputStream;

import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.IDatabaseType;
import org.pentaho.database.util.DatabaseTypeHelper;
import org.pentaho.ui.database.Messages;
import org.pentaho.ui.xul.XulComponent;
import org.pentaho.ui.xul.XulDomContainer;
import org.pentaho.ui.xul.XulException;
import org.pentaho.ui.xul.components.XulMessageBox;
import org.pentaho.ui.xul.components.XulTextbox;
import org.pentaho.ui.xul.containers.XulListbox;
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
public class FragmentHandler extends AbstractXulEventHandler implements IFragmentHandler {

  private Messages messages = new Messages();

  private XulListbox connectionBox;

  private XulListbox accessBox;

  private DatabaseTypeHelper databaseTypeHelper;

  private String packagePath = "org/pentaho/ui/database/resources/"; //$NON-NLS-1$

  public FragmentHandler() {
  }

  public String getName() {
    return "fragmentHandler";
  }

  public void setDatabaseTypeHelper(DatabaseTypeHelper databaseTypeHelper) {
    this.databaseTypeHelper = databaseTypeHelper;
  }

  private void loadDatabaseOptionsFragment(String fragmentUri) throws XulException {

    XulComponent groupElement = document.getElementById("database-options-box"); //$NON-NLS-1$
    XulComponent parentElement = groupElement.getParent();

    XulDomContainer fragmentContainer = null;

    try {

      // Get new group box fragment ...
      // This will effectively set up the SWT parent child relationship...

      fragmentContainer = this.xulDomContainer.loadFragment(fragmentUri, messages.getBundle());
      XulComponent newGroup = fragmentContainer.getDocumentRoot().getElementById("database-options-box");
      parentElement.replaceChild(groupElement, newGroup);

    } catch (XulException e) {
      e.printStackTrace();
      throw e;
    }

    if (fragmentContainer == null) {
      return;
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
  @Bindable
  public void refreshOptions() {
    refreshOptionsWithCallback(null);
  }

  public void refreshOptionsWithCallback(Callback callback) {

    // Short circuit to disable refreshing
    if (disableRefresh) {
      return;
    }

    connectionBox = (XulListbox) document.getElementById("connection-type-list"); //$NON-NLS-1$
    accessBox = (XulListbox) document.getElementById("access-type-list"); //$NON-NLS-1$

    String connectionKey = (String) connectionBox.getSelectedItem();
    //    DatabaseInterface database = DataHandler.connectionMap.get(connectionKey);
    IDatabaseType database = databaseTypeHelper.getDatabaseTypeByName(connectionKey);

    String accessKey = (String) accessBox.getSelectedItem();
    if (accessKey == null) {
      accessBox.setSelectedIndex(0);
      accessKey = (String) accessBox.getSelectedItem();
    }
    DatabaseAccessType access = DatabaseAccessType.getAccessTypeByName(accessKey);

    if (access == null) {
      return;
    }

    String fragment = null;

    DataHandler dataHandler = null;
    try {
      dataHandler = (DataHandler) xulDomContainer.getEventHandler("dataHandler"); //$NON-NLS-1$
      dataHandler.pushCache();
    } catch (XulException e) {
      // TODO not a critical function, but should log a problem...
    }

    switch (access) {
      case JNDI:
        fragment = getFragment(database, "_jndi.xul", "common_jndi.xul"); //$NON-NLS-1$ //$NON-NLS-2$
        break;
      case NATIVE:
        fragment = getFragment(database, "_native.xul", "common_native.xul"); //$NON-NLS-1$ //$NON-NLS-2$
        break;
      case OCI:
        fragment = getFragment(database, "_oci.xul", "common_native.xul"); //$NON-NLS-1$ //$NON-NLS-2$
        break;
      case PLUGIN:
        fragment = getFragment(database, "_plugin.xul", "common_native.xul"); //$NON-NLS-1$ //$NON-NLS-2$
        break;
    }

    try {
      loadDatabaseOptionsFragment(fragment.toLowerCase());
    } catch (XulException e) {
      // TODO should be reporting as an error dialog; need error dialog in XUL framework
      showMessage(messages.getString("FragmentHandler.USER.CANT_LOAD_OPTIONS", database.getName()) //$NON-NLS-1$
      );
    }

    XulTextbox portBox = (XulTextbox) document.getElementById("port-number-text"); //$NON-NLS-1$
    if (portBox != null) {
      int port = database.getDefaultDatabasePort();
      if (port > 0) {
        portBox.setValue(Integer.toString(port));
      }
    }

    if (dataHandler != null) {
      dataHandler.popCache();
    }

    if (callback != null) {
      callback.callback();
    }

  }

  private String getFragment(IDatabaseType database, String extension, String defaultFragment) {
    String fragment = packagePath.concat(database.getShortName()).concat(extension);
    InputStream in = getClass().getClassLoader().getResourceAsStream(fragment.toLowerCase());
    if (in == null) {
      fragment = packagePath.concat(defaultFragment);
    }
    return fragment;
  }

  public Object getData() {
    return null;
  }

  public void setData(Object arg0) {
  }

  private void showMessage(String message) {
    try {
      XulMessageBox box = (XulMessageBox) document.createElement("messagebox"); //$NON-NLS-1$
      box.setMessage(message);
      box.open();
    } catch (XulException e) {
      System.out.println("Error creating messagebox " + e.getMessage());
    }
  }

  boolean disableRefresh = false;

  public void setDisableRefresh(boolean disableRefresh) {
    this.disableRefresh = disableRefresh;
  }

  @Override
  public boolean isRefreshDisabled() {
    return this.disableRefresh;
  }
}
