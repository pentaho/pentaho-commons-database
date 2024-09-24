/*!
* This program is free software; you can redistribute it and/or modify it under the
* terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
* Foundation.
*
* You should have received a copy of the GNU Lesser General Public License along with this
* program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
* or from the Free Software Foundation, Inc.,
* 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
* without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
* See the GNU Lesser General Public License for more details.
*
* Copyright (c) 2002-2023 Hitachi Vantara..  All rights reserved.
*/

package org.pentaho.ui.database.event;

import com.google.gwt.core.client.GWT;
import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.model.IDatabaseType;
import org.pentaho.database.util.DatabaseTypeHelper;
import org.pentaho.gwt.widgets.client.listbox.CustomListBox;
import org.pentaho.ui.xul.XulComponent;
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

import java.util.Set;
import java.util.TreeSet;

/**
 * Fragment handler deals with the logistics of replacing a portion of the dialog
 * from a XUL fragment when the combination of database connection type and database
 * access method calls for a replacement.
 *
 * @author gmoran
 * @since Mar 19, 2008
 */
public class GwtFragmentHandler extends AbstractXulEventHandler implements IFragmentHandler {

  private XulListbox connectionBox;

  private XulListbox accessBox;

  private DatabaseTypeHelper databaseTypeHelper;

  private IMessages messages;

  Set<String> supportedFragments = new TreeSet<String>();

  private boolean disableRefresh = false;

  public GwtFragmentHandler() {
    setName( "fragmentHandler" );
    supportedFragments.add( "generic_native.xul" );
    supportedFragments.add( "informix_jndi.xul" );
    supportedFragments.add( "informix_native.xul" );
    supportedFragments.add( "mssql_jndi.xul" );
    supportedFragments.add( "mssql_native.xul" );
    supportedFragments.add( "mssqlnative_jndi.xul" );
    supportedFragments.add( "mssqlnative_native.xul" );
    supportedFragments.add( "mysql_jndi.xul" );
    supportedFragments.add( "mysql_native.xul" );
    supportedFragments.add( "oracle_jndi.xul" );
    supportedFragments.add( "oracle_native.xul" );
    supportedFragments.add( "oracle_oci.xul" );
    supportedFragments.add( "kettlethin_native.xul" );
    supportedFragments.add( "sapr3_plugin.xul" );
    supportedFragments.add( "snowflakehv_native.xul" );
    supportedFragments.add( "redshift_native.xul" );
    supportedFragments.add( "azuresqldb_native.xul" );
  }

  public void setDisableRefresh( boolean disableRefresh ) {
    this.disableRefresh = disableRefresh;
  }

  public void setDatabaseTypeHelper( DatabaseTypeHelper databaseTypeHelper ) {
    this.databaseTypeHelper = databaseTypeHelper;
  }

  public void setMessages( IMessages messages ) {
    this.messages = messages;
  }

  private void loadDatabaseOptionsFragment( String fragmentUri, final DataHandler dataHandler,
                                            final IDatabaseType database, final IFragmentHandler.Callback parentCallback ) throws XulException {

    // clean out group before reloading
    XulComponent groupElement = document.getElementById( "database-options-box" ); //$NON-NLS-1$
    for ( XulComponent component : groupElement.getChildNodes() ) {
      groupElement.removeChild( component );

    }

    // Get new group box fragment ...
    // This will effectively set up the SWT parent child relationship...

    IXulLoaderCallback internalCallback = new IXulLoaderCallback() {
      public void overlayLoaded() {
        // TODO Auto-generated method stub
        afterOverlay( dataHandler, database );
        if ( parentCallback != null ) {
          parentCallback.callback();
        }
      }

      public void overlayRemoved() {
      }

      public void xulLoaded( GwtXulRunner runner ) {
      }
    };

    // this call will cache the individual overlays in a map within AsyncXulLoader
    AsyncXulLoader.loadOverlayFromUrl( GWT.getModuleBaseURL() + fragmentUri, GWT.getModuleBaseURL() + "databasedialog",
      (GwtXulDomContainer) getXulDomContainer(), internalCallback, true );
  }

  @Bindable
  public void refreshOptions() {
    refreshOptionsWithCallback( new IFragmentHandler.Callback() {
      @Override
      public void callback() {
        if ( accessBox != null ) {
          CustomListBox accessBoxWidget = (CustomListBox) accessBox.getManagedObject();
          accessBoxWidget.setFocus( true );
          accessBoxWidget.scrollSelectedItemIntoView();
        }
      }
    } );
  }

  /**
   * This method handles the resource-like loading of the XUL
   * fragment definitions based on connection type and access
   * method. If there is a common definition, and no connection
   * specific override definition, then the common definition is used.
   * Connection specific definition resources follow the naming
   * pattern [connection type code]_[access method].xul.
   */
  public void refreshOptionsWithCallback( final IFragmentHandler.Callback callback ) {

    if ( this.disableRefresh ) {
      return;
    }

    connectionBox = (XulListbox) document.getElementById( "connection-type-list" ); //$NON-NLS-1$
    accessBox = (XulListbox) document.getElementById( "access-type-list" ); //$NON-NLS-1$

    String connectionKey = getSelectedString( connectionBox );
    if ( connectionKey == null ) {
      return;
    }
    //    DatabaseInterface database = DataHandler.connectionMap.get(connectionKey);
    IDatabaseType database = databaseTypeHelper.getDatabaseTypeByName( connectionKey );

    String accessKey = getSelectedString( accessBox );
    DatabaseAccessType access = DatabaseAccessType.getAccessTypeByName( accessKey );

    if ( access == null ) {
      return;
    }

    String fragment = null;

    DataHandler dataHandler = null;
    try {
      dataHandler = (DataHandler) xulDomContainer.getEventHandler( "dataHandler" ); //$NON-NLS-1$
      dataHandler.pushCache();
    } catch ( XulException e ) {
      // TODO not a critical function, but should log a problem...
    }

    switch ( access ) {
      case JNDI:
        fragment = getFragment( database, "_jndi.xul", "common_jndi.xul" ); //$NON-NLS-1$ //$NON-NLS-2$
        break;
      case NATIVE:
        fragment = getFragment( database, "_native.xul", "common_native.xul" ); //$NON-NLS-1$ //$NON-NLS-2$
        break;
      case OCI:
        fragment = getFragment( database, "_oci.xul", "common_native.xul" ); //$NON-NLS-1$ //$NON-NLS-2$
        break;
      case PLUGIN:
        fragment = getFragment( database, "_plugin.xul", "common_native.xul" ); //$NON-NLS-1$ //$NON-NLS-2$
        break;
    }

    try {
      loadDatabaseOptionsFragment( fragment.toLowerCase(), dataHandler, database, callback );
    } catch ( XulException e ) {
      // TODO should be reporting as an error dialog; need error dialog in XUL framework
      showMessage( messages.getString( "FragmentHandler.USER.CANT_LOAD_OPTIONS", database.getName() ) //$NON-NLS-1$
      );
    }
  }

  private void afterOverlay( DataHandler dataHandler, IDatabaseType database ) {
    XulTextbox portBox = (XulTextbox) document.getElementById( "port-number-text" ); //$NON-NLS-1$
    Object data = dataHandler.getData();
    String portValue = null;
    IDatabaseConnection databaseConnection;
    // Extract the stored value for port number in the model
    if ( data instanceof IDatabaseConnection ) {
      databaseConnection = (IDatabaseConnection) data;
      portValue = databaseConnection.getDatabasePort();
    }
    if ( portBox != null ) {
      // If the model has the port number use it other wise use the default one for the selected database
      int port = ( portValue != null && portValue.length() > 0 ) ? Integer.parseInt( portValue ) : database
        .getDefaultDatabasePort();
      if ( port > 0 ) {
        portBox.setValue( Integer.toString( port ) );
      }
    }

    if ( dataHandler != null ) {
      dataHandler.popCache();
    }

    GwtGroupBox box1 = (GwtGroupBox) document.getElementById( "database-options-box" );

    XulHbox box = (XulHbox) document.getElementById( "connection-access-list-box" );

    box1.layout();

    ( (GwtHbox) box ).layout();

  }

  private String getSelectedString( XulListbox box ) {
    String key;
    Object keyObj = box.getSelectedItem();
    if ( keyObj instanceof XulListitem ) {
      key = ( (XulListitem) keyObj ).getLabel();
    } else {
      key = (String) keyObj;
    }
    return key;
  }

  private String getFragment( IDatabaseType database, String extension, String defaultFragment ) {
    String fragment = database.getShortName().concat( extension ).toLowerCase();
    if ( !supportedFragments.contains( fragment ) ) {
      fragment = defaultFragment;
    }
    return fragment;
  }

  private void showMessage( String message ) {
    try {
      XulMessageBox box = (XulMessageBox) document.createElement( "messagebox" ); //$NON-NLS-1$
      box.setMessage( message );
      box.open();
    } catch ( XulException e ) {
      System.out.println( "Error creating messagebox " + e.getMessage() );
    }
  }

  @Override
  public boolean isRefreshDisabled() {
    return this.disableRefresh;
  }

  @Bindable
  public void setAuthMethodVisibility() throws XulException {
    DataHandler dataHandler = (DataHandler) xulDomContainer.getEventHandler( "dataHandler" );
    dataHandler.setAuthFieldsVisible();
  }
}
