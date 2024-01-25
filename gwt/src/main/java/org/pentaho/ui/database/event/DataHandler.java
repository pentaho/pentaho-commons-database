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
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Command;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import org.pentaho.database.dialect.SnowflakeDatabaseDialect;
import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.DatabaseConnection;
import org.pentaho.database.model.DatabaseConnectionPoolParameter;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.model.IDatabaseConnectionPoolParameter;
import org.pentaho.database.model.IDatabaseType;
import org.pentaho.database.model.PartitionDatabaseMeta;
import org.pentaho.database.util.DatabaseTypeHelper;
import org.pentaho.gwt.widgets.client.listbox.CustomListBox;
import org.pentaho.gwt.widgets.client.utils.NameUtils;
import org.pentaho.gwt.widgets.client.utils.string.StringUtils;
import org.pentaho.ui.database.event.ILaunch.Status;
import org.pentaho.ui.database.gwt.Base64ClientUtils;
import org.pentaho.ui.xul.XulComponent;
import org.pentaho.ui.xul.XulException;
import org.pentaho.ui.xul.components.XulCheckbox;
import org.pentaho.ui.xul.components.XulLabel;
import org.pentaho.ui.xul.components.XulListitem;
import org.pentaho.ui.xul.components.XulMenuList;
import org.pentaho.ui.xul.components.XulMessageBox;
import org.pentaho.ui.xul.components.XulTextbox;
import org.pentaho.ui.xul.containers.XulDeck;
import org.pentaho.ui.xul.containers.XulDialog;
import org.pentaho.ui.xul.containers.XulListbox;
import org.pentaho.ui.xul.containers.XulRoot;
import org.pentaho.ui.xul.containers.XulTree;
import org.pentaho.ui.xul.containers.XulTreeItem;
import org.pentaho.ui.xul.containers.XulTreeRow;
import org.pentaho.ui.xul.containers.XulWindow;
import org.pentaho.ui.xul.gwt.tags.GwtVbox;
import org.pentaho.ui.xul.impl.AbstractXulEventHandler;
import org.pentaho.ui.xul.stereotype.Bindable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.pentaho.di.core.database.AzureSqlDataBaseMeta.CLIENT_SECRET_KEY;
import static org.pentaho.di.core.database.AzureSqlDataBaseMeta.CLIENT_ID;
import static org.pentaho.di.core.database.AzureSqlDataBaseMeta.IS_ALWAYS_ENCRYPTION_ENABLED;
import static org.pentaho.di.core.database.RedshiftDatabaseMeta.IAM_ACCESS_KEY_ID;
import static org.pentaho.di.core.database.RedshiftDatabaseMeta.IAM_PROFILE_NAME;
import static org.pentaho.di.core.database.RedshiftDatabaseMeta.IAM_SECRET_ACCESS_KEY;
import static org.pentaho.di.core.database.RedshiftDatabaseMeta.IAM_SESSION_TOKEN;
import static org.pentaho.di.core.database.RedshiftDatabaseMeta.JDBC_AUTH_METHOD;

/**
 * Handles all manipulation of the DatabaseMeta, data retrieval from XUL DOM and rudimentary validation.
 * <p>
 * TODO: 2. Needs to be abstracted away from the DatabaseMeta object, so other tools in the platform can use the dialog
 * and their preferred database object. 3. Needs exception handling, string resourcing and logging
 *
 * @author gmoran
 * @since Mar 19, 2008
 */
public class DataHandler extends AbstractXulEventHandler {

  private static final String LINE_SEPARATOR = "\n"; // System.getProperty("line.separator"); //$NON-NLS-1$

  // See http://bugs.jquery.com/ticket/1450 for an explanation
  private static final int SC_NO_CONTENT_IE = 1223;

  // Kettle thin related
  private static final String EXTRA_OPTION_WEB_APPLICATION_NAME = "KettleThin.webappname";
  private static final String DEFAULT_WEB_APPLICATION_NAME = "pentaho";

  private static final String DEFAULT_DATASTORE_INITIAL_SIZE = "0";
  private static final String DEFAULT_DATASTORE_MAXIMUM_SIZE = "20";

  protected DatabaseDialogListener listener;

  protected IMessages messages;

  protected ILaunch launch;

  // protected IXulAsyncDatabaseConnectionService connectionService;
  protected DatabaseTypeHelper databaseTypeHelper;

  protected IFragmentHandler fragmentHandler;

  private DatabaseConnectionPoolParameter[] poolingParameters;

  protected IDatabaseConnection databaseConnection = null;

  private IDatabaseConnection cache;

  private XulDeck dialogDeck;

  private XulListbox deckOptionsBox;

  private XulListbox connectionBox;

  private XulListbox accessBox;

  private XulTextbox connectionNameBox;

  protected XulTextbox hostNameBox;

  protected XulTextbox databaseNameBox;

  protected XulTextbox portNumberBox;

  protected XulTextbox userNameBox;

  protected XulTextbox passwordBox;

  // Generic database specific
  protected XulTextbox customDriverClassBox;

  // Generic database specific
  protected XulTextbox customUrlBox;

  // Oracle specific
  protected XulTextbox dataTablespaceBox;

  // Oracle specific
  protected XulTextbox indexTablespaceBox;

  // MS SQL Server specific
  private XulTextbox serverInstanceBox;

  private XulTextbox warehouseBox;

  // Informix specific
  private XulTextbox serverNameBox;

  // SAP R/3 specific
  protected XulTextbox languageBox;

  // SAP R/3 specific
  protected XulTextbox systemNumberBox;

  // SAP R/3 specific
  protected XulTextbox clientBox;

  // MS SQL Server specific
  private XulCheckbox doubleDecimalSeparatorCheck;

  private XulCheckbox useIntegratedSecurityCheck;

  // MySQL specific
  private XulCheckbox resultStreamingCursorCheck;

  // Hitachi Vantara data services specific
  private XulTextbox webAppName;

  // ==== Options Panel ==== //

  protected XulTree optionsParameterTree;

  // ==== Clustering Panel ==== //

  private XulCheckbox clusteringCheck;

  protected XulTree clusterParameterTree;

  private XulLabel clusterParameterDescriptionLabel;

  // ==== Advanced Panel ==== //

  XulCheckbox quoteIdentifiersCheck;

  XulCheckbox lowerCaseIdentifiersCheck;

  XulCheckbox upperCaseIdentifiersCheck;

  XulTextbox sqlBox;

  // ==== Pooling Panel ==== //

  private XulLabel poolSizeLabel;

  private XulLabel maxPoolSizeLabel;

  private XulCheckbox poolingCheck;

  protected XulTextbox poolSizeBox;

  protected XulTextbox maxPoolSizeBox;

  private XulTextbox poolingDescription;

  private XulLabel poolingParameterDescriptionLabel;

  private XulLabel poolingDescriptionLabel;

  protected XulTree poolParameterTree;

  private XulMenuList jdbcAuthMethod;
  private XulTextbox iamAccessKeyId;
  private XulTextbox iamSecretKeyId;
  private XulTextbox iamSessionToken;
  private XulTextbox iamProfileName;

  private XulMenuList azureSqlDBJdbcAuthMethod;
  private XulCheckbox azureSqlDBAlwaysEncryptionEnabled;
  private XulTextbox azureSqlDBClientSecretId;
  private XulTextbox azureSqlDBClientSecretKey;

  protected IConnectionAutoBeanFactory connectionAutoBeanFactory;

  public DataHandler() {
    setName( "dataHandler" ); //$NON-NLS-1$
    connectionAutoBeanFactory = GWT.create( IConnectionAutoBeanFactory.class );
    cache = createDatabaseConnection();
  }

  public void setFragmentHandler( IFragmentHandler fragmentHandler ) {
    this.fragmentHandler = fragmentHandler;
  }

  public void setDatabaseTypeHelper( DatabaseTypeHelper databaseTypeHelper ) {
    this.databaseTypeHelper = databaseTypeHelper;
  }

  public void setDatabaseDialogListener( DatabaseDialogListener listener ) {
    this.listener = listener;
  }

  public void setMessages( IMessages messages ) {
    this.messages = messages;
  }

  public void setLaunch( ILaunch launch ) {
    this.launch = launch;
  }

  private IDatabaseConnection createDatabaseConnection() {
    IDatabaseConnection database = connectionAutoBeanFactory.iDatabaseConnection().as();
    database.setAttributes( new HashMap<String, String>() );
    database.setConnectionPoolingProperties( new HashMap<String, String>() );
    database.setExtraOptions( new HashMap<String, String>() );
    database.setExtraOptionsOrder( new HashMap<String, String>() );
    return database;
  }

  @Bindable
  public void loadConnectionData() {

    // HACK: need to check if onload event was already fired.
    // It is called from XulDatabaseDialog from dcDialog.getSwtInstance(shell); AND dialog.show();
    // Multiple calls lead to multiple numbers of database types.
    // Therefore we check if the connectionBox was already filled.
    if ( connectionBox != null ) {
      return;
    }

    getControls();

    // Add sorted types to the listbox now.

    for ( String key : databaseTypeHelper.getDatabaseTypeNames() ) {
      connectionBox.addItem( key );
    }

    // HACK: Need to force height of list control, as it does not behave
    // well when using relative layouting

    connectionBox.setRows( connectionBox.getRows() );

    Object key = getSelectedString( connectionBox );

    // TODO Implement a connection type preference,
    // and use that type as the default for
    // new databases.

    if ( key == null ) {
      key = databaseTypeHelper.getDatabaseTypeNames().get( 0 ); // connectionMap.firstKey();
      connectionBox.setSelectedItem( key );
    } else {
      connectionBox.setSelectedItem( key );
    }

    // HACK: Need to force selection of first panel

    if ( dialogDeck != null ) {
      setDeckChildIndex();
    }

    setDefaultPoolParameters();

    if ( databaseConnection != null ) {
      setInfo( databaseConnection );
    }
  }

  @Bindable
  public void setAzureSqlDBAuthRelatedFieldsVisible() {
    if ( azureSqlDBJdbcAuthMethod != null ) {
      passwordBox.setDisabled(
          "Azure Active Directory - Universal With MFA".equals( azureSqlDBJdbcAuthMethod.getValue() )
        || "Azure Active Directory - Integrated".equals( azureSqlDBJdbcAuthMethod.getValue() ) );
      userNameBox.setDisabled( "Azure Active Directory - Integrated".equals( azureSqlDBJdbcAuthMethod.getValue() ) );
    }
  }

  @Bindable
  public void enableAzureSqlDBEncryption() {
    if ( azureSqlDBAlwaysEncryptionEnabled != null ) {
      boolean isAlwaysEncryptionEnabled = azureSqlDBAlwaysEncryptionEnabled.isChecked();
      if ( !isAlwaysEncryptionEnabled ) {
        azureSqlDBClientSecretId.setDisabled( true );
        azureSqlDBClientSecretKey.setDisabled( true );
      } else {
        azureSqlDBClientSecretId.setDisabled( false );
        azureSqlDBClientSecretKey.setDisabled( false );
      }
    }

  }


  // On Database type change
  @Bindable
  public void loadAccessData() {

    try {

      jsni_showLoadingIndicator(); // prevent successive calls

      getControls();

      pushCache();

      String key = getSelectedString( connectionBox );

      // Nothing selected yet...
      if ( key == null ) {
        key = databaseTypeHelper.getDatabaseTypeNames().get( 0 );
        connectionBox.setSelectedItem( key );
        return;
      }

      // DatabaseInterface database = connectionMap.get(key);
      IDatabaseType database = databaseTypeHelper.getDatabaseTypeByName( key );

      List<DatabaseAccessType> acc = database.getSupportedAccessTypes();
      Object accessKey = getSelectedString( accessBox );

      boolean refreshInitiallyDisabled = fragmentHandler.isRefreshDisabled();

      fragmentHandler.setDisableRefresh( true );

      // Remove items from the access box
      accessBox.removeItems();

      // Add those access types applicable to this connection type
      for ( DatabaseAccessType value : acc ) {
        accessBox.addItem( value.getName() );
      }

      // In case the refresh was disabled externally
      if ( !refreshInitiallyDisabled ) {
        fragmentHandler.setDisableRefresh( false );
      }

      // HACK: Need to force height of list control, as it does not behave
      // well when using relative layouting
      accessBox.setRows( accessBox.getRows() );

      // May not exist for this connection type.
      accessBox.setSelectedItem( accessKey );

      // Refreshes the options since the above selection may not fire the selected item event
      fragmentHandler.refreshOptionsWithCallback(new IFragmentHandler.Callback() {
        @Override
        public void callback() {
          if ( connectionBox != null ) {
            CustomListBox connectionBoxWidget = (CustomListBox) connectionBox.getManagedObject();
            connectionBoxWidget.setFocus( true );
            connectionBoxWidget.scrollSelectedItemIntoView();
          }
        }
      } );

      if ( databaseConnection != null ) {
        Map<String, String> options = databaseConnection.getExtraOptions();
        Map<String, String> extraOptionsOrder = databaseConnection.getExtraOptionsOrder();
        if ( options == null || options.size() == 0 ) {
          options = database.getDefaultOptions();
        }
        setOptionsData( options, extraOptionsOrder );
      }
      setClusterData( databaseConnection != null ? databaseConnection.getPartitioningInformation() : null );

      popCache();
    } finally {
      jsni_hideLoadingIndicator();
    }
  }

  private native void jsni_showLoadingIndicator()/*-{
                                                 if($wnd.top && $wnd.top.showLoadingIndicator){
                                                 $wnd.top.showLoadingIndicator();
                                                 }
                                                 }-*/;

  private native void jsni_hideLoadingIndicator()/*-{
                                                 if($wnd.top && $wnd.top.hideLoadingIndicator){
                                                 $wnd.top.hideLoadingIndicator();
                                                 }
                                                 }-*/;

  private String getSelectedString( XulListbox box ) {
    String key = null;
    Object keyObj = box.getSelectedItem();
    if ( keyObj instanceof XulListitem ) {
      key = ( (XulListitem) keyObj ).getLabel();
    } else {
      key = (String) keyObj;
    }
    return key;
  }

  @Bindable
  public void editOptions( int index ) {
    if ( index + 1 == optionsParameterTree.getRows() ) {
      // editing last row add a new one below

      Object[][] values = optionsParameterTree.getValues();
      String[] row = (String[]) values[values.length - 1];
      if ( row != null && !( isBlank( row[0] ) && isBlank( row[1] ) ) ) {
        // actually have something in current last row
        XulTreeRow newRow = optionsParameterTree.getRootChildren().addNewRow();

        newRow.addCellText( 0, "" ); //$NON-NLS-1$
        newRow.addCellText( 1, "" ); //$NON-NLS-1$
      }
    }
  }

  @Bindable
  public void getOptionHelp() {

    String message = null;
    IDatabaseConnection database = createDatabaseConnection();

    getInfo( database );
    String url = database.getDatabaseType().getExtraOptionsHelpUrl();

    if ( isBlank( url ) ) {
      message = messages.getString( "DataHandler.USER_NO_HELP_AVAILABLE" ); //$NON-NLS-1$

      showMessage( messages.getString( "DataHandler.ERROR_MESSAGE_TITLE" ), message, false ); //$NON-NLS-1$
      return;
    }

    if ( launch != null ) {
      Status status = launch.openUrl( url, messages );

      if ( status.equals( Status.Failed ) ) {
        message = messages.getString( "DataHandler.USER_UNABLE_TO_LAUNCH_BROWSER", url ); //$NON-NLS-1$
        showMessage( messages.getString( "DataHandler.ERROR_MESSAGE_TITLE" ), message, false ); //$NON-NLS-1$
      }
    } else {
      showMessage( messages.getString( "DataHandler.ERROR_MESSAGE_TITLE" ), //$NON-NLS-1$
          messages.getString( "DataHandler.LAUNCH_NOT_SUPPORTED" ), //$NON-NLS-1$
          false );
    }
  }

  @Bindable
  public void addEmptyRowsToOptions() {
    final Object[][] values = optionsParameterTree.getValues();
    GwtLayoutHandler.deferUpdateUI( this.optionsParameterTree, new Command() {
      @Override
      public void execute() {
        int rowsToAdd = 15 - values.length;
        while ( rowsToAdd-- > 0 ) {
          XulTreeRow row = DataHandler.this.optionsParameterTree.getRootChildren().addNewRow();
          row.addCellText( 0, "" ); //$NON-NLS-1$
          row.addCellText( 1, "" ); //$NON-NLS-1$
        }
      }
    } );
  }

  @Bindable
  public void setDeckChildIndex() {

    getControls();

    // if pooling selected, check the parameter validity before allowing
    // a deck panel switch...
    int originalSelection = dialogDeck.getSelectedIndex();

    boolean passed = true;
    if ( originalSelection == 3 ) {
      passed = checkPoolingParameters();
    }

    addEmptyRowsToOptions();

    if ( passed ) {
      int selected = deckOptionsBox.getSelectedIndex();
      if ( selected < 0 && deckOptionsBox.getRowCount() > 0 ) {
        selected = 0;
        deckOptionsBox.setSelectedIndex( 0 );
      }
      dialogDeck.setSelectedIndex( selected );
    } else {
      dialogDeck.setSelectedIndex( originalSelection );
      deckOptionsBox.setSelectedIndex( originalSelection );
    }

  }

  @Bindable
  public void onPoolingCheck() {
    if ( poolingCheck != null ) {
      final boolean dis = !poolingCheck.isChecked();
      if ( poolSizeBox != null ) {
        poolSizeBox.setDisabled( dis );
      }
      if ( maxPoolSizeBox != null ) {
        maxPoolSizeBox.setDisabled( dis );
      }
      if ( poolSizeLabel != null ) {
        poolSizeLabel.setDisabled( dis );
      }
      if ( maxPoolSizeLabel != null ) {
        maxPoolSizeLabel.setDisabled( dis );
      }
      if ( poolParameterTree != null ) {
        GwtLayoutHandler.deferUpdateUI( poolParameterTree, new Command() {
          @Override
          public void execute() {
            poolParameterTree.setDisabled( dis );
            traverseDomSetReadOnly( poolParameterTree, dis );
          }
        } );
      }
      if ( poolingParameterDescriptionLabel != null ) {
        poolingParameterDescriptionLabel.setDisabled( dis );
      }
      if ( poolingDescriptionLabel != null ) {
        poolingDescriptionLabel.setDisabled( dis );
      }
      if ( poolingDescription != null ) {
        poolingDescription.setDisabled( dis );
      }

    }
  }

  @Bindable
  public void onClusterCheck() {
    if ( clusteringCheck != null ) {
      boolean dis = !clusteringCheck.isChecked();
      if ( clusterParameterTree != null ) {
        clusterParameterTree.setDisabled( dis );
      }
      if ( clusterParameterDescriptionLabel != null ) {
        clusterParameterDescriptionLabel.setDisabled( dis );
      }
    }
  }

  public Object getData() {
    return databaseConnection;
  }

  public void setData( Object data ) {

    // if a null value is passed in, replace it with an
    // empty database connection
    if ( data == null ) {
      data = createDatabaseConnection();
    }

    databaseConnection = (IDatabaseConnection) data;
    setInfo( databaseConnection );
  }

  public void pushCache() {
    getConnectionSpecificInfo( cache );
  }

  public void popCache() {
    setConnectionSpecificInfo( cache );
  }

  @Bindable
  public void onCancel() {
    close();
    if ( listener != null ) {
      listener.onDialogCancel();
    }
  }

  @Bindable
  private void close() {
    //Clear the UI components
    databaseConnection = createDatabaseConnection();
    setConnectionSpecificInfo( databaseConnection );

    XulComponent window = document.getElementById( "general-datasource-window" ); //$NON-NLS-1$

    if ( window == null ) { // window must be root
      window = document.getRootElement();
    }
    if ( window instanceof XulDialog ) {
      ( (XulDialog) window ).hide();
    } else if ( window instanceof XulWindow ) {
      ( (XulWindow) window ).close();
    }
  }

  private boolean windowClosed() {
    boolean closedWindow = true;
    XulComponent window = document.getElementById( "general-datasource-window" ); //$NON-NLS-1$

    if ( window == null ) { // window must be root
      window = document.getRootElement();
    }
    if ( window instanceof XulWindow ) {
      closedWindow = ( (XulWindow) window ).isClosed();
    }
    return closedWindow;
  }

  @Bindable
  @SuppressWarnings( "unused" ) // Bound via XUL
  public void onOK() {
    final IDatabaseConnection database = createDatabaseConnection();
    getInfo( database );

    String illegals = NameUtils.getReservedChars();
    if ( !NameUtils.isValidFileName( database.getName() ) ) {
      showMessage( messages.getString( "DatabaseDialog.ErrorConnectionName.title" ), messages.getString(
          "DatabaseDialog.ErrorConnectionName.description", illegals ), false );
      return;
    }

    if ( isBlank( database.getDatabaseName() ) && databaseNameBox != null ) {
      if ( !"KettleThin".equals( database.getDatabaseType().getShortName() ) ) {
        showMessage(
            messages.getString( "DatabaseDialog.ErrorMissingDatabaseName.title" ), //$NON-NLS-1$
            messages.getString( "DatabaseDialog.ErrorMissingDatabaseName.description" ), false ); //$NON-NLS-1$
        return;
      }
    }

    if ( !checkPoolingParameters() ) {
      return;
    }

    RequestBuilder
        checkParamsBuilder =
        new RequestBuilder( RequestBuilder.POST, getBaseURL() + "checkParams" ); //$NON-NLS-1$
    checkParamsBuilder.setHeader( "Content-Type", "application/json" ); //$NON-NLS-1$//$NON-NLS-2$
    try {
      if ( !StringUtils.isEmpty( database.getPassword() ) ) {
        database.setPassword( "ENC:" + Base64ClientUtils.encode( database.getPassword() ) );
      }
      AutoBean<IDatabaseConnection> bean = AutoBeanUtils.getAutoBean( database );
      String checkParamsJson = AutoBeanCodex.encode( bean ).getPayload();
      checkParamsBuilder.sendRequest( checkParamsJson, new RequestCallback() {

        @Override
        public void onError( Request request, Throwable exception ) {
          showMessage(
              messages.getString( "DataHandler.ERROR_MESSAGE_TITLE" ), exception.getMessage(),
              exception.getMessage().length() > 300 ); //$NON-NLS-1$
        }

        @Override
        public void onResponseReceived( Request request, Response response ) {
          if ( response.getStatusCode() == Response.SC_NO_CONTENT || response.getStatusCode() == SC_NO_CONTENT_IE ) {
            if ( databaseConnection == null ) {
              databaseConnection = connectionAutoBeanFactory.iDatabaseConnection().as();
            }

            // Clear extra options before reapplying all values from web
            databaseConnection.setExtraOptions( new HashMap<String, String>() );
            databaseConnection.setExtraOptionsOrder( new HashMap<String, String>() );
            // Populate database connection with new values
            getInfo( databaseConnection );

            databaseConnection.setChanged( true );
            if ( listener != null ) {
              listener.onDialogAccept( databaseConnection );
            }
            close();
          } else {
            gatherErrors( response );
          }
        }
      } );
    } catch ( RequestException e ) {
      showMessage(
          messages.getString( "DataHandler.ERROR_MESSAGE_TITLE" ), e.getMessage(),
          e.getMessage().length() > 300 ); //$NON-NLS-1$
    }
  }

  @Bindable
  public void testDatabaseConnection() {
    final IDatabaseConnection database = createDatabaseConnection();
    getInfo( database );
    if ( databaseConnection != null ) {
      // apply ID from exist database for find password on the server size
      database.setId( databaseConnection.getId() );
    }

    RequestBuilder
        checkParamsBuilder =
        new RequestBuilder( RequestBuilder.POST, getBaseURL() + "checkParams" ); //$NON-NLS-1$
    checkParamsBuilder.setHeader( "Content-Type", "application/json" ); //$NON-NLS-1$ //$NON-NLS-2$
    try {
      if ( !StringUtils.isEmpty( database.getPassword() ) ) {
        database.setPassword( "ENC:" + Base64ClientUtils.encode( database.getPassword() ) );
      }
      AutoBean<IDatabaseConnection> bean = AutoBeanUtils.getAutoBean( database );
      String checkParamsJson = AutoBeanCodex.encode( bean ).getPayload();
      checkParamsBuilder.sendRequest( checkParamsJson, new RequestCallback() {

        @Override
        public void onError( Request request, Throwable exception ) {
          showMessage(
              messages.getString( "DataHandler.ERROR_MESSAGE_TITLE" ), exception.getMessage(),
              exception.getMessage().length() > 300 ); //$NON-NLS-1$
        }

        @Override
        public void onResponseReceived( Request request, Response response ) {
          int statusCode = response.getStatusCode();

          if ( statusCode == Response.SC_NO_CONTENT || statusCode == SC_NO_CONTENT_IE ) {
            RequestBuilder testBuilder = new RequestBuilder( RequestBuilder.PUT, getBaseURL() + "test" ); //$NON-NLS-1$
            testBuilder.setHeader( "Content-Type", "application/json" ); //$NON-NLS-1$ //$NON-NLS-2$
            try {
              AutoBean<IDatabaseConnection> autoBean = AutoBeanUtils.getAutoBean( database );
              String testConnectionJson = AutoBeanCodex.encode( autoBean ).getPayload();
              testBuilder.sendRequest( testConnectionJson, new RequestCallback() {

                @Override
                public void onError( Request request1, Throwable exception ) {
                  showMessage(
                      messages.getString( "DataHandler.ERROR_MESSAGE_TITLE" ), exception.getMessage(),
                      exception.getMessage().length() > 300 ); //$NON-NLS-1$
                }

                @Override
                public void onResponseReceived( Request request1, Response response1 ) {
                  showMessage(
                      messages.getString( "DataHandler.TEST_MESSAGE_TITLE" ), response1.getText(),
                      response1.getText().length() > 300 ); //$NON-NLS-1$
                }

              } );
            } catch ( RequestException e ) {
              showMessage(
                  messages.getString( "DataHandler.ERROR_MESSAGE_TITLE" ), e.getMessage(),
                  e.getMessage().length() > 300 ); //$NON-NLS-1$
            }
          } else {
            gatherErrors( response );
          }
        }
      } );
    } catch ( RequestException e ) {
      showMessage(
          messages.getString( "DataHandler.ERROR_MESSAGE_TITLE" ), e.getMessage(),
          e.getMessage().length() > 300 ); //$NON-NLS-1$
    }
  }

  private void gatherErrors( Response response ) {
    if ( response.getStatusCode() == Response.SC_OK && !response.getText().equalsIgnoreCase( "null" ) ) { //$NON-NLS-1$
      String message = ""; //$NON-NLS-1$
      final JSONValue jsonValue = JSONParser.parseStrict( response.getText() );
      final String keyItems = "items"; //$NON-NLS-1$
      final String starter = "* "; //$NON-NLS-1$
      if ( jsonValue.isObject() != null && jsonValue.isObject().containsKey( keyItems ) ) {
        final JSONValue items = jsonValue.isObject().get( keyItems );
        if ( items.isArray() != null ) {
          for ( int i = 0; i < items.isArray().size(); i++ ) {
            message =
                message.concat( starter ).concat( items.isArray().get( i ).isString().stringValue() ).concat(
                    LINE_SEPARATOR );
          }
        } else if ( items.isString() != null ) {
          message = message.concat( starter ).concat( items.isString().stringValue() ).concat( LINE_SEPARATOR );
        } else {
          message = message.concat( starter ).concat( items.toString() ).concat( LINE_SEPARATOR );
        }
      } else {
        message = message.concat( starter ).concat( jsonValue.toString() ).concat( LINE_SEPARATOR );
      }
      showMessage( messages.getString( "DataHandler.CHECK_PARAMS_TITLE" ), message, false ); //$NON-NLS-1$
    } else {
      showMessage(
          messages.getString( "DataHandler.ERROR_MESSAGE_TITLE" ), response.getStatusText(),
          response.getStatusText().length() > 300 ); //$NON-NLS-1$
    }
  }

  protected void getInfo( IDatabaseConnection dbConnection ) {

    getControls();

    // TODO: WG: why is this necessary?
    // if (this.databaseMeta != null && this.databaseMeta != meta) {
    // meta.initializeVariablesFrom(this.databaseMeta);
    // }

    // Let's not remove any (default) options or attributes
    // We just need to display the correct ones for the database type below...
    //
    // In fact, let's just clear the database port...
    //
    // TODO: what about the port number?

    // Name:
    dbConnection.setName( connectionNameBox.getValue() );

    // Connection type:
    Object connection = getSelectedString( connectionBox );
    if ( connection != null ) {
      dbConnection.setDatabaseType( databaseTypeHelper.getDatabaseTypeByName( (String) connection ) );
    }

    // Access type:
    Object access = getSelectedString( accessBox );
    if ( access != null ) {
      dbConnection.setAccessType( DatabaseAccessType.getAccessTypeByName( (String) access ) );
    }

    getConnectionSpecificInfo( dbConnection );

    // Port number:
    if ( portNumberBox != null ) {
      dbConnection.setDatabasePort( portNumberBox.getValue() );
    }

    // Option parameters:

    if ( optionsParameterTree != null ) {
      Object[][] values = optionsParameterTree.getValues();
      for ( int i = 0; i < values.length; i++ ) {

        String parameter = (String) values[i][0];
        String value = (String) values[i][1];

        if ( value == null ) {
          value = ""; //$NON-NLS-1$
        }

        // int dbType = meta.getDatabaseType();

        // Only if parameter are supplied, we will add to the map...
        if ( !isBlank( parameter ) ) {
          String databaseTypeCode = dbConnection.getDatabaseType().getShortName();
          dbConnection.addExtraOption( databaseTypeCode, parameter, value );
          dbConnection.getExtraOptionsOrder().put( String.valueOf( i ), databaseTypeCode + "." + parameter );
        }
      }
    }

    // Advanced panel settings:

    if ( quoteIdentifiersCheck != null ) {
      dbConnection.setQuoteAllFields( quoteIdentifiersCheck.isChecked() );
    }

    if ( lowerCaseIdentifiersCheck != null ) {
      dbConnection.setForcingIdentifiersToLowerCase( lowerCaseIdentifiersCheck.isChecked() );
    }

    if ( upperCaseIdentifiersCheck != null ) {
      dbConnection.setForcingIdentifiersToUpperCase( upperCaseIdentifiersCheck.isChecked() );
    }

    if ( sqlBox != null ) {
      dbConnection.setConnectSql( sqlBox.getValue() );
    }

    // Cluster panel settings
    if ( clusteringCheck != null ) {
      dbConnection.setPartitioned( clusteringCheck.isChecked() );
    }

    if ( ( clusterParameterTree != null ) && ( dbConnection.isPartitioned() ) ) {

      Object[][] values = clusterParameterTree.getValues();
      List<PartitionDatabaseMeta> pdms = new ArrayList<PartitionDatabaseMeta>();
      for ( int i = 0; i < values.length; i++ ) {

        String partitionId = (String) values[i][0];

        if ( isBlank( partitionId ) ) {
          continue;
        }

        String hostname = (String) values[i][1];
        String port = (String) values[i][2];
        String dbName = (String) values[i][3];
        String username = (String) values[i][4];
        String password = (String) values[i][5];
        PartitionDatabaseMeta pdm = new PartitionDatabaseMeta( partitionId, hostname, port, dbName );
        pdm.setUsername( username );
        pdm.setPassword( password );
        pdms.add( pdm );
      }
      dbConnection.setPartitioningInformation( pdms );
    }

    if ( poolingCheck != null ) {
      dbConnection.setUsingConnectionPool( poolingCheck.isChecked() );
    }

    if ( dbConnection.isUsingConnectionPool() ) {
      if ( poolSizeBox != null ) {
        try {
          int initialPoolSize = Integer.parseInt( poolSizeBox.getValue() );
          dbConnection.setInitialPoolSize( initialPoolSize );
        } catch ( NumberFormatException e ) {
          // TODO log exception and move on ...
        }
      }

      if ( maxPoolSizeBox != null ) {
        try {
          int maxPoolSize = Integer.parseInt( maxPoolSizeBox.getValue() );
          dbConnection.setMaximumPoolSize( maxPoolSize );
        } catch ( NumberFormatException e ) {
          // TODO log exception and move on ...
        }
      }

      if ( poolParameterTree != null ) {
        Object[][] values = poolParameterTree.getValues();
        Map<String, String> properties = new HashMap<String, String>();
        for ( int i = 0; i < values.length; i++ ) {

          boolean isChecked = false;
          if ( values[i][0] instanceof Boolean ) {
            isChecked = ( (Boolean) values[i][0] ).booleanValue();
          } else {
            isChecked = Boolean.valueOf( (String) values[i][0] );
          }

          if ( !isChecked ) {
            continue;
          }

          String parameter = (String) values[i][1];
          String value = (String) values[i][2];
          if ( !isBlank( parameter ) && !isBlank( value ) ) {
            properties.put( parameter, value );
          }

        }
        dbConnection.setConnectionPoolingProperties( properties );
      }
    }

  }

  private void setInfo( final IDatabaseConnection databaseConnection ) {

    if ( databaseConnection == null ) {
      return;
    }

    // Instantiate attributes
    if ( databaseConnection.getAttributes() == null ) {
      databaseConnection.setAttributes( new HashMap<String, String>() );
    }

    if ( databaseConnection.getExtraOptions().containsKey( EXTRA_OPTION_WEB_APPLICATION_NAME ) ) {
      databaseConnection.setDatabaseName( databaseConnection.getExtraOptions()
          .get( EXTRA_OPTION_WEB_APPLICATION_NAME ) );
      databaseConnection.getExtraOptions().remove( EXTRA_OPTION_WEB_APPLICATION_NAME );
      databaseConnection.setChanged( true );
    }

    getControls();

    // TODO: Delete method: copyConnectionSpecificInfo(meta, cache);

    // Name:
    connectionNameBox.setValue( databaseConnection.getName() );

    // disable refresh for now
    fragmentHandler.setDisableRefresh( true );

    // Connection type:
    if ( databaseConnection.getDatabaseType() != null ) {
      connectionBox.setSelectedItem( databaseConnection.getDatabaseType().getName() );
    } else {
      connectionBox.setSelectedIndex( 0 );
    }

    // Access type:
    if ( databaseConnection.getAccessType() != null ) {
      accessBox.setSelectedItem( databaseConnection.getAccessType().getName() );
    } else {
      accessBox.setSelectedIndex( 0 );
    }

    fragmentHandler.setDisableRefresh( false );

    // this is broken out so we can set the cache information only when caching
    // connection values
    fragmentHandler.refreshOptionsWithCallback( new IFragmentHandler.Callback() {
      public void callback() {
        setConnectionSpecificInfo( databaseConnection );
      }
    } );

    // Port number:
    if ( portNumberBox != null ) {
      portNumberBox.setValue( databaseConnection.getDatabasePort() );
    }

    // Options Parameters:

    setOptionsData( databaseConnection.getExtraOptions(), databaseConnection.getExtraOptionsOrder() );

    // Advanced panel settings:

    if ( quoteIdentifiersCheck != null ) {
      quoteIdentifiersCheck.setChecked( databaseConnection.isQuoteAllFields() );
    }

    if ( lowerCaseIdentifiersCheck != null ) {
      lowerCaseIdentifiersCheck.setChecked( databaseConnection.isForcingIdentifiersToLowerCase() );
    }

    if ( upperCaseIdentifiersCheck != null ) {
      upperCaseIdentifiersCheck.setChecked( databaseConnection.isForcingIdentifiersToUpperCase() );
    }

    if ( sqlBox != null ) {
      sqlBox.setValue(
          databaseConnection.getConnectSql() == null ? "" : databaseConnection.getConnectSql() ); //$NON-NLS-1$
    }

    // Clustering panel settings

    if ( clusteringCheck != null ) {
      clusteringCheck.setChecked( databaseConnection.isPartitioned() );
    }

    setClusterData( databaseConnection.getPartitioningInformation() );

    // Pooling panel settings

    if ( poolingCheck != null ) {
      // BISERVER-11276 connection pooling should default to checked
      if ( databaseConnection.getId() != null ) {
        poolingCheck.setChecked( databaseConnection.isUsingConnectionPool() );
      } else {
        // new connection, default values
        poolingCheck.setChecked( true );
        poolSizeBox.setValue( DEFAULT_DATASTORE_INITIAL_SIZE );
        maxPoolSizeBox.setValue( DEFAULT_DATASTORE_MAXIMUM_SIZE );
      }
    }

    applyPoolProperties();

    if ( databaseConnection.isUsingConnectionPool() ) {
      if ( poolSizeBox != null ) {
        poolSizeBox.setValue( Integer.toString( databaseConnection.getInitialPoolSize() ) );
      }
      if ( maxPoolSizeBox != null ) {
        maxPoolSizeBox.setValue( Integer.toString( databaseConnection.getMaximumPoolSize() ) );
      }
    }

    dialogDeck.setSelectedIndex( 0 );
    deckOptionsBox.setSelectedIndex( 0 );

    setDeckChildIndex();
    onPoolingCheck();
    onClusterCheck();
    enableAzureSqlDBEncryption();
    setAzureSqlDBAuthRelatedFieldsVisible();

  }

  /**
   * @return the list of parameters that were enabled, but had invalid return values (null or empty)
   */
  private boolean checkPoolingParameters() {

    List<String> returnList = new ArrayList<String>();
    if ( poolParameterTree != null ) {
      Object[][] values = poolParameterTree.getValues();
      for ( int i = 0; i < values.length; i++ ) {

        boolean isChecked = false;
        if ( values[i][0] instanceof Boolean ) {
          isChecked = ( (Boolean) values[i][0] ).booleanValue();
        } else {
          isChecked = Boolean.valueOf( (String) values[i][0] );
        }

        if ( !isChecked ) {
          continue;
        }

        String parameter = (String) values[i][1];
        String value = (String) values[i][2];
        if ( isBlank( value ) ) {
          returnList.add( parameter );
        }

      }
      if ( returnList.size() > 0 ) {
        String parameters = LINE_SEPARATOR;
        for ( String parameter : returnList ) {
          parameters = parameters.concat( parameter ).concat( LINE_SEPARATOR );
        }

        String message = messages.getString( "DataHandler.USER_INVALID_PARAMETERS" ).concat( parameters ); //$NON-NLS-1$
        showMessage( messages.getString( "DataHandler.ERROR_MESSAGE_TITLE" ), message, false ); //$NON-NLS-1$
      }
    }
    return returnList.size() <= 0;
  }

  private void applyPoolProperties() {
    GwtLayoutHandler.deferUpdateUI( poolParameterTree, new Command() {
      @Override
      public void execute() {

        // set all of the pooling properties to the defaults
        restoreDefaults();

        // set any connection specific pooling properties now
        Map<String, String> poolProperties = databaseConnection.getConnectionPoolingProperties();
        if ( poolProperties != null && poolProperties.size() > 0 ) {
          setPoolProperties( poolProperties );
        }
      }
    } );
  }

  private void setPoolProperties( Map<String, String> properties ) {
    if ( poolParameterTree != null ) {
      Object[][] values = poolParameterTree.getValues();
      for ( int i = 0; i < values.length; i++ ) {

        String parameter = (String) values[i][1];
        boolean isChecked = properties.containsKey( parameter );

        if ( !isChecked ) {
          continue;
        }
        // XulTreeRow row = poolParameterTree.getRootChildren().addNewRow();
        //        row.addCellText(0, "true"); //$NON-NLS-1$
        // row.addCellText(1, parameter);
        // row.addCellText(2, properties.get(parameter));
        XulTreeRow row = poolParameterTree.getRootChildren().getItem( i ).getRow();
        row.addCellText( 0, "true" ); // checks the checkbox //$NON-NLS-1$
        String value = properties.get( parameter );
        row.addCellText( 2, value );

      }
    }

  }

  @Bindable
  public void restoreDefaults() {
    if ( poolingParameters != null && poolParameterTree != null ) {
      for ( int i = 0; i < poolParameterTree.getRootChildren().getItemCount(); i++ ) {
        XulTreeItem item = poolParameterTree.getRootChildren().getItem( i );
        String parameterName = item.getRow().getCell( 1 ).getLabel();
        String defaultValue =
            DatabaseConnectionPoolParameter.findParameter( parameterName, poolingParameters ).getDefaultValue();
        if ( isBlank( defaultValue ) ) {
          item.getRow().addCellText( 2, "" );
        }
        item.getRow().addCellText( 2, defaultValue );
        item.getRow().addCellText( 0, "false" );
        if ( "validationQuery".equals( parameterName ) ) {
          item.getRow().setParentTreeItem( item );
          item.getRow().getCell( 0 ).setTreeRowParent( findRow( "testOnBorrow" ) );
        }
      }
    }

  }

  private XulTreeRow findRow( String _parameterName ) {
    for ( int i = 0; i < poolParameterTree.getRootChildren().getItemCount(); i++ ) {
      XulTreeItem item = poolParameterTree.getRootChildren().getItem( i );
      String parameterName = item.getRow().getCell( 1 ).getLabel();
      if ( _parameterName.equals( parameterName ) ) {
        item.getRow().setParentTreeItem( item );
        XulTree vtree = item.getTree();
        return item.getRow();
      }
    }
    return null;
  }

  private void setDefaultPoolParameters() {
    RequestBuilder
        poolingParamsBuilder =
        new RequestBuilder( RequestBuilder.GET, getBaseURL() + "poolingParameters" ); //$NON-NLS-1$
    try {
      poolingParamsBuilder.sendRequest( null, new RequestCallback() {

        @Override
        public void onError( Request request, Throwable exception ) {
          showMessage(
              messages.getString( "DataHandler.ERROR_MESSAGE_TITLE" ), exception.getMessage(),
              exception.getMessage().length() > 300 ); //$NON-NLS-1$
        }

        @Override
        public void onResponseReceived( Request request, Response response ) {
          Boolean success = response.getStatusCode() == Response.SC_OK;
          if ( success ) {
            AutoBean<IDatabaseConnectionPoolParameterList> bean =
                AutoBeanCodex.decode( connectionAutoBeanFactory, IDatabaseConnectionPoolParameterList.class, response
                    .getText() );
            final IDatabaseConnectionPoolParameterList paramListWrapper = bean.as();
            poolingParameters =
                new DatabaseConnectionPoolParameter[paramListWrapper.getDatabaseConnectionPoolParameters().size()];

            GwtLayoutHandler.deferUpdateUI( poolParameterTree, new Command() {
              @Override
              public void execute() {
                if ( poolParameterTree != null ) {
                  int i = 0;
                  for ( IDatabaseConnectionPoolParameter parameter : paramListWrapper
                      .getDatabaseConnectionPoolParameters() ) {
                    XulTreeRow row = poolParameterTree.getRootChildren().addNewRow();
                    row.addCellText( 0, "false" ); //$NON-NLS-1$
                    row.addCellText( 1, parameter.getParameter() );
                    row.addCellText( 2, parameter.getDefaultValue() );
                    poolingParameters[i] =
                        new DatabaseConnectionPoolParameter( parameter.getParameter(), parameter.getDefaultValue(),
                            parameter.getDescription() );
                    i++;
                  }

                  // HACK: reDim the pooling table
                  poolParameterTree.setRows( poolParameterTree.getRows() );
                }
              }
            } );
          }
        }
      } );
    } catch ( RequestException e ) {
      showMessage(
          messages.getString( "DataHandler.ERROR_MESSAGE_TITLE" ), e.getMessage(),
          e.getMessage().length() > 300 ); //$NON-NLS-1$
    }
  }

  private void clearOptions() {
    Object[][] values = optionsParameterTree.getValues();
    for ( int i = values.length - 1; i >= 0; i-- ) {
      optionsParameterTree.getRootChildren().removeItem( i );
    }
  }

  private void setOptionsData( final Map<String, String> extraOptions, final Map<String, String> extraOptionsOrder ) {

    if ( optionsParameterTree == null ) {
      return;
    }

    GwtLayoutHandler.deferUpdateUI( this.optionsParameterTree, new Command() {
      @Override
      public void execute() {

        clearOptions();
        if ( extraOptions != null ) {
          Iterator<String> keys = extraOptions.keySet().iterator();
          if ( extraOptionsOrder != null && !extraOptionsOrder.isEmpty() ) {
            keys = new TreeMap<String, String>( extraOptionsOrder ).values().iterator();
          }
          String connection = getSelectedString( connectionBox );
          IDatabaseType currentType = null;

          if ( connection != null ) {
            currentType = databaseTypeHelper.getDatabaseTypeByName( connection );
          }

          while ( keys.hasNext() ) {

            String parameter = keys.next();
            if ( isBlank( parameter ) ) {
              continue;
            }

            String value = extraOptions.get( parameter );
            if ( isBlank( value ) ) {
              continue;
            }

            // If the parameter starts with a database type code we show it in the options, otherwise we don't.
            // For example MySQL.defaultFetchSize
            //

            int dotIndex = parameter.indexOf( '.' );
            if ( dotIndex >= 0 ) {
              String parameterOption = parameter.substring( dotIndex + 1 );
              String databaseTypeString = parameter.substring( 0, dotIndex );
              IDatabaseType databaseType = databaseTypeHelper.getDatabaseTypeByShortName( databaseTypeString );
              if ( currentType == databaseType ) {
                XulTreeRow row = optionsParameterTree.getRootChildren().addNewRow();
                row.addCellText( 0, parameterOption );
                row.addCellText( 1, value );
              }
            }
          }

        }
        // Add 5 blank rows if none are already there, otherwise, just add one.
        int numToAdd = 15;
        if ( extraOptions != null && extraOptions.keySet().size() > 0 ) {
          numToAdd = 1;
        }
        while ( numToAdd-- > 0 ) {
          XulTreeRow row = optionsParameterTree.getRootChildren().addNewRow();
          // easy way of putting new cells in the row
          row.addCellText( 0, "" ); //$NON-NLS-1$
          row.addCellText( 1, "" ); //$NON-NLS-1$
        }
      }
    } );
  }

  private void setClusterData( final List<PartitionDatabaseMeta> clusterInformation ) {

    if ( clusterParameterTree == null ) {
      // there's nothing to do
      return;
    }

    GwtLayoutHandler.deferUpdateUI( this.clusterParameterTree, new Command() {
      @Override
      public void execute() {

        if ( ( clusterInformation != null ) && ( clusterParameterTree != null ) ) {

          for ( int i = 0; i < clusterInformation.size(); i++ ) {

            PartitionDatabaseMeta meta = clusterInformation.get( i );
            XulTreeRow row = clusterParameterTree.getRootChildren().addNewRow();
            row.addCellText( 0, meta.getPartitionId() == null ? "" : meta.getPartitionId() ); //$NON-NLS-1$
            row.addCellText( 1, meta.getHostname() == null ? "" : meta.getHostname() ); //$NON-NLS-1$
            row.addCellText( 2, meta.getPort() == null ? "" : meta.getPort() ); //$NON-NLS-1$
            row.addCellText( 3, meta.getDatabaseName() == null ? "" : meta.getDatabaseName() ); //$NON-NLS-1$
            row.addCellText( 4, meta.getUsername() == null ? "" : meta.getUsername() ); //$NON-NLS-1$
            row.addCellText( 5, meta.getPassword() == null ? "" : meta.getPassword() ); //$NON-NLS-1$
          }
        }
        // Add 5 blank rows if none are already there, otherwise, just add one.
        int numToAdd = 5;
        if ( clusterInformation != null && clusterInformation.size() > 0 ) {
          numToAdd = 1;
        }
        while ( numToAdd-- > 0 ) {
          XulTreeRow row = clusterParameterTree.getRootChildren().addNewRow();
          // easy way of putting new cells in the row
          row.addCellText( 0, "" ); //$NON-NLS-1$
          row.addCellText( 1, "" ); //$NON-NLS-1$
          row.addCellText( 2, "" ); //$NON-NLS-1$
          row.addCellText( 3, "" ); //$NON-NLS-1$
          row.addCellText( 4, "" ); //$NON-NLS-1$
          row.addCellText( 5, "" ); //$NON-NLS-1$
        }
      }
    } );
  }

  @Bindable
  public void poolingRowChange( int idx ) {
    if ( poolingParameters != null ) {
      if ( idx != -1 ) {

        if ( idx >= poolingParameters.length ) {
          idx = poolingParameters.length - 1;
        }
        if ( idx < 0 ) {
          idx = 0;
        }
        poolingDescription.setValue( poolingParameters[idx].getDescription() );

        XulTreeRow row = poolParameterTree.getRootChildren().getItem( idx ).getRow();
        if ( row.getSelectedColumnIndex() == 2 ) {
          row.addCellText( 0, "true" ); //$NON-NLS-1$
        }
      }
    }
  }

  private void copyConnectionSpecificInfo( IDatabaseConnection from, IDatabaseConnection to ) {
    // Hostname:
    if ( from.getHostname() != null ) {
      to.setHostname( from.getHostname() );
    }

    // Database name:
    if ( from.getDatabaseName() != null ) {
      to.setDatabaseName( from.getDatabaseName() );
    }

    // Username:
    if ( from.getUsername() != null ) {
      to.setUsername( from.getUsername() );
    }

    // Password:
    if ( from.getPassword() != null ) {
      to.setPassword( from.getPassword() );
    }

    // Streaming result cursor:
    to.setStreamingResults( from.isStreamingResults() );

    // Data tablespace:
    if ( from.getDataTablespace() != null ) {
      to.setDataTablespace( from.getDataTablespace() );
    }

    // Index tablespace
    if ( from.getIndexTablespace() != null ) {
      to.setIndexTablespace( from.getIndexTablespace() );
    }

    // Extra options
    to.setExtraOptions( from.getExtraOptions() );

    to.setExtraOptionsOrder( from.getExtraOptionsOrder() );

    // SQL Server double decimal separator
    to.setUsingDoubleDecimalAsSchemaTableSeparator( from.isUsingDoubleDecimalAsSchemaTableSeparator() );

    // SAP Attributes...
    if ( from.getAttributes().get( "SAPLanguage" ) != null ) { //$NON-NLS-1$
      to.getAttributes().put( "SAPLanguage", from.getAttributes().get( "SAPLanguage" ) ); //$NON-NLS-1$ //$NON-NLS-2$
    }
    if ( from.getAttributes().get( "SAPSystemNumber" ) != null ) { //$NON-NLS-1$
      to.getAttributes()
          .put( "SAPSystemNumber", from.getAttributes().get( "SAPSystemNumber" ) ); //$NON-NLS-1$//$NON-NLS-2$
    }
    if ( from.getAttributes().get( "SAPClient" ) != null ) { //$NON-NLS-1$
      to.getAttributes().put( "SAPClient", from.getAttributes().get( "SAPClient" ) ); //$NON-NLS-1$ //$NON-NLS-2$
    }

    // Generic settings...
    if ( from.getAttributes().get( DatabaseConnection.ATTRIBUTE_CUSTOM_URL ) != null ) {
      to.getAttributes().put( DatabaseConnection.ATTRIBUTE_CUSTOM_URL,
          from.getAttributes().get( DatabaseConnection.ATTRIBUTE_CUSTOM_URL ) );
    }

    if ( from.getAttributes().get( DatabaseConnection.ATTRIBUTE_CUSTOM_DRIVER_CLASS ) != null ) {
      to.getAttributes().put( DatabaseConnection.ATTRIBUTE_CUSTOM_DRIVER_CLASS,
          from.getAttributes().get( DatabaseConnection.ATTRIBUTE_CUSTOM_DRIVER_CLASS ) );
    }

    if ( from.getInformixServername() != null ) {
      to.setInformixServername( from.getInformixServername() );
    }

    final String warehouse = from.getAttributes().get( SnowflakeDatabaseDialect.WAREHOUSE );
    if ( warehouse != null ) {
      to.getAttributes().put( SnowflakeDatabaseDialect.WAREHOUSE, warehouse );
    }
  }

  private void getConnectionSpecificInfo( IDatabaseConnection meta ) {
    // Hostname:
    if ( hostNameBox != null ) {
      meta.setHostname( hostNameBox.getValue() );
    }

    // Database name:
    if ( databaseNameBox != null ) {
      meta.setDatabaseName( databaseNameBox.getValue() );
    }

    // Username:
    if ( userNameBox != null ) {
      meta.setUsername( userNameBox.getValue() );
    }

    // Password:
    if ( passwordBox != null ) {
      meta.setPassword( passwordBox.getValue() );
    }

    // Streaming result cursor:
    if ( resultStreamingCursorCheck != null ) {
      meta.setStreamingResults( resultStreamingCursorCheck.isChecked() );
    }

    // Data tablespace:
    if ( dataTablespaceBox != null ) {
      meta.setDataTablespace( dataTablespaceBox.getValue() );
    }

    // Index tablespace
    if ( indexTablespaceBox != null ) {
      meta.setIndexTablespace( indexTablespaceBox.getValue() );
    }

    // Extra options
    if ( serverInstanceBox != null ) {
      String value = serverInstanceBox.getValue();
      if ( !isBlank( value ) ) {
        meta.setSQLServerInstance( serverInstanceBox.getValue() );
      }
    }

    // SQL Server double decimal separator
    if ( doubleDecimalSeparatorCheck != null ) {
      meta.setUsingDoubleDecimalAsSchemaTableSeparator( doubleDecimalSeparatorCheck.isChecked() );
    }

    if ( useIntegratedSecurityCheck != null ) {
      meta.getAttributes()
          .put( "MSSQLUseIntegratedSecurity", "" + useIntegratedSecurityCheck.isChecked() ); //$NON-NLS-1$//$NON-NLS-2$
    }

    // SAP Attributes...
    if ( languageBox != null ) {
      meta.getAttributes().put( "SAPLanguage", languageBox.getValue() ); //$NON-NLS-1$
    }
    if ( systemNumberBox != null ) {
      meta.getAttributes().put( "SAPSystemNumber", systemNumberBox.getValue() ); //$NON-NLS-1$
    }
    if ( clientBox != null ) {
      meta.getAttributes().put( "SAPClient", clientBox.getValue() ); //$NON-NLS-1$
    }

    // Generic settings...
    if ( customUrlBox != null ) {
      meta.getAttributes().put( DatabaseConnection.ATTRIBUTE_CUSTOM_URL,
          customUrlBox.getValue() != null ? customUrlBox.getValue() : "" ); //$NON-NLS-1$
    }
    if ( customDriverClassBox != null ) {
      meta.getAttributes().put( DatabaseConnection.ATTRIBUTE_CUSTOM_DRIVER_CLASS, customDriverClassBox.getValue() );
    }

    // Server Name: (Informix)
    if ( serverNameBox != null ) {
      meta.setInformixServername( serverNameBox.getValue() );
    }

    // Warehouse
    if ( warehouseBox != null ) {
      meta.getAttributes().put( SnowflakeDatabaseDialect.WAREHOUSE, warehouseBox.getValue()  );
    }

    if ( webAppName != null ) {
      meta.setDatabaseName( webAppName.getValue() );
    }

    //Azure SQL DB
    if ( azureSqlDBJdbcAuthMethod != null ) {
      meta.getAttributes().put( JDBC_AUTH_METHOD, azureSqlDBJdbcAuthMethod.getValue() );
    }

    if ( azureSqlDBClientSecretId != null ) {
      meta.getAttributes().put( CLIENT_ID, azureSqlDBClientSecretId.getValue() );
    }
    if ( azureSqlDBAlwaysEncryptionEnabled != null ) {
      if ( azureSqlDBAlwaysEncryptionEnabled.isChecked() ) {
        meta.getAttributes().put( IS_ALWAYS_ENCRYPTION_ENABLED, "true" );
      } else {
        meta.getAttributes().put( IS_ALWAYS_ENCRYPTION_ENABLED, "false" );
      }
    }

    if ( azureSqlDBClientSecretKey != null ) {
      meta.getAttributes().put( CLIENT_SECRET_KEY, azureSqlDBClientSecretKey.getValue() );
    }

    if ( azureSqlDBClientSecretKey != null ) {
      meta.getAttributes().put( CLIENT_SECRET_KEY, azureSqlDBClientSecretKey.getValue() );
    }

    if ( jdbcAuthMethod != null ) {
      meta.getAttributes().put( JDBC_AUTH_METHOD, jdbcAuthMethod.getValue() );
    }
    if ( iamAccessKeyId != null ) {
      meta.getAttributes().put( IAM_ACCESS_KEY_ID, iamAccessKeyId.getValue() );
    }
    if ( iamSecretKeyId != null ) {
      meta.getAttributes().put( IAM_SECRET_ACCESS_KEY, iamSecretKeyId.getValue() );
    }
    if ( iamSessionToken != null ) {
      meta.getAttributes().put( IAM_SESSION_TOKEN, iamSessionToken.getValue() );
    }
    if ( iamProfileName != null ) {
      meta.getAttributes().put( IAM_PROFILE_NAME, iamProfileName.getValue() );
    }
  }

  private void setConnectionSpecificInfo( IDatabaseConnection meta ) {

    getControls();

    if ( hostNameBox != null ) {
      hostNameBox.setValue( meta.getHostname() );
    }

    // Database name:
    if ( databaseNameBox != null ) {
      databaseNameBox.setValue( meta.getDatabaseName() );
    }

    // Username:
    if ( userNameBox != null ) {
      userNameBox.setValue( meta.getUsername() );
    }

    // Password:
    if ( passwordBox != null ) {
      passwordBox.setValue( meta.getPassword() );
    }

    // Streaming result cursor:
    if ( resultStreamingCursorCheck != null ) {
      resultStreamingCursorCheck.setChecked( meta.isStreamingResults() );
    }

    // Data tablespace:
    if ( dataTablespaceBox != null ) {
      dataTablespaceBox.setValue( meta.getDataTablespace() );
    }

    // Index tablespace
    if ( indexTablespaceBox != null ) {
      indexTablespaceBox.setValue( meta.getIndexTablespace() );
    }

    if ( serverInstanceBox != null ) {
      String instance = meta.getSQLServerInstance();
      if ( !isBlank( instance ) ) {
        serverInstanceBox.setValue( instance );
      }
    }

    // SQL Server double decimal separator
    if ( doubleDecimalSeparatorCheck != null ) {
      doubleDecimalSeparatorCheck.setChecked( meta.isUsingDoubleDecimalAsSchemaTableSeparator() );
    }

    if ( useIntegratedSecurityCheck != null ) {
      useIntegratedSecurityCheck.setChecked(
          "true".equals( meta.getAttributes().get( "MSSQLUseIntegratedSecurity" ) ) ); //$NON-NLS-1$//$NON-NLS-2$
    }

    // SAP Attributes...
    if ( languageBox != null ) {
      languageBox.setValue( meta.getAttributes().get( "SAPLanguage" ) ); //$NON-NLS-1$
    }
    if ( systemNumberBox != null ) {
      systemNumberBox.setValue( meta.getAttributes().get( "SAPSystemNumber" ) ); //$NON-NLS-1$
    }
    if ( clientBox != null ) {
      clientBox.setValue( meta.getAttributes().get( "SAPClient" ) ); //$NON-NLS-1$
    }

    // Generic settings...
    if ( customUrlBox != null ) {
      customUrlBox.setValue( meta.getAttributes().get( DatabaseConnection.ATTRIBUTE_CUSTOM_URL ) );
    }
    if ( customDriverClassBox != null ) {
      customDriverClassBox.setValue( meta.getAttributes().get( DatabaseConnection.ATTRIBUTE_CUSTOM_DRIVER_CLASS ) );
    }

    // Server Name: (Informix)
    if ( serverNameBox != null ) {
      serverNameBox.setValue( meta.getInformixServername() );
    }

    if ( warehouseBox != null ) {
      warehouseBox.setValue( meta.getAttributes().get( SnowflakeDatabaseDialect.WAREHOUSE )  );
    }

    if ( webAppName != null ) {
      if ( databaseConnection == null || StringUtils.isEmpty( databaseConnection.getName() ) ) {
        webAppName.setValue( DEFAULT_WEB_APPLICATION_NAME );
      } else {
        webAppName.setValue( meta.getDatabaseName() );
      }
    }

    //Azure sql db
    if ( azureSqlDBJdbcAuthMethod != null ) {
      azureSqlDBJdbcAuthMethod.setValue( meta.getAttributes().get( JDBC_AUTH_METHOD ) );
    }

    if ( azureSqlDBAlwaysEncryptionEnabled != null && meta.getAttributes().get( IS_ALWAYS_ENCRYPTION_ENABLED ) != null ) {
      azureSqlDBAlwaysEncryptionEnabled.setChecked( meta.getAttributes().get( IS_ALWAYS_ENCRYPTION_ENABLED ).equals( "true" ) );
    }

    if ( azureSqlDBClientSecretId != null ) {
      azureSqlDBClientSecretId.setValue( meta.getAttributes().get( CLIENT_ID ) );
    }

    if ( azureSqlDBClientSecretKey != null ) {
      azureSqlDBClientSecretKey.setValue( meta.getAttributes().get( CLIENT_SECRET_KEY ) );
    }

    if ( jdbcAuthMethod != null ) {
      jdbcAuthMethod.setValue( meta.getAttributes().get( JDBC_AUTH_METHOD ) );
      setAuthFieldsVisible();
    }
    if ( iamAccessKeyId != null ) {
      iamAccessKeyId.setValue( meta.getAttributes().get( IAM_ACCESS_KEY_ID ) );
    }
    if ( iamSecretKeyId != null ) {
      iamSecretKeyId.setValue( meta.getAttributes().get( IAM_SECRET_ACCESS_KEY ) );
    }
    if ( iamSessionToken != null ) {
      iamSessionToken.setValue( meta.getAttributes().get( IAM_SESSION_TOKEN ) );
    }
    if ( iamProfileName != null ) {
      iamProfileName.setValue( meta.getAttributes().get( IAM_PROFILE_NAME ) );
    }
  }

  @SuppressWarnings ( "unused" )
  public void setAuthFieldsVisible() {
    jdbcAuthMethod = (XulMenuList) document.getElementById( "redshift-auth-method-list" );
    GwtVbox standardControls = (GwtVbox) document.getElementById( "auth-standard-controls" );
    GwtVbox iamControls = (GwtVbox) document.getElementById( "auth-iam-controls" );
    GwtVbox profileControls = (GwtVbox) document.getElementById( "auth-profile-controls" );
    int jdbcAuthMethodValue = jdbcAuthMethod.getSelectedIndex();
    switch ( jdbcAuthMethodValue ) {
      case 1:
        //iam
        standardControls.setVisible( false );
        iamControls.setVisible( true );
        profileControls.setVisible( false );
        break;
      case 2:
        //profile
        standardControls.setVisible( false );
        iamControls.setVisible( false );
        profileControls.setVisible( true );
        break;
      default:
        //standard
        standardControls.setVisible( true );
        iamControls.setVisible( false );
        profileControls.setVisible( false );
        break;
    }
  }

  protected void getControls() {

    // Not all of these controls are created at the same time.. that's OK, for now, just check
    // each one for null before using.

    dialogDeck = (XulDeck) document.getElementById( "dialog-panel-deck" ); //$NON-NLS-1$
    deckOptionsBox = (XulListbox) document.getElementById( "deck-options-list" ); //$NON-NLS-1$
    connectionBox = (XulListbox) document.getElementById( "connection-type-list" ); //$NON-NLS-1$
    accessBox = (XulListbox) document.getElementById( "access-type-list" ); //$NON-NLS-1$
    connectionNameBox = (XulTextbox) document.getElementById( "connection-name-text" ); //$NON-NLS-1$
    hostNameBox = (XulTextbox) document.getElementById( "server-host-name-text" ); //$NON-NLS-1$
    databaseNameBox = (XulTextbox) document.getElementById( "database-name-text" ); //$NON-NLS-1$
    portNumberBox = (XulTextbox) document.getElementById( "port-number-text" ); //$NON-NLS-1$
    userNameBox = (XulTextbox) document.getElementById( "username-text" ); //$NON-NLS-1$
    passwordBox = (XulTextbox) document.getElementById( "password-text" ); //$NON-NLS-1$
    dataTablespaceBox = (XulTextbox) document.getElementById( "data-tablespace-text" ); //$NON-NLS-1$
    indexTablespaceBox = (XulTextbox) document.getElementById( "index-tablespace-text" ); //$NON-NLS-1$
    serverInstanceBox = (XulTextbox) document.getElementById( "instance-text" ); //$NON-NLS-1$
    serverNameBox = (XulTextbox) document.getElementById( "server-name-text" ); //$NON-NLS-1$
    customUrlBox = (XulTextbox) document.getElementById( "custom-url-text" ); //$NON-NLS-1$
    customDriverClassBox = (XulTextbox) document.getElementById( "custom-driver-class-text" ); //$NON-NLS-1$
    languageBox = (XulTextbox) document.getElementById( "language-text" ); //$NON-NLS-1$
    systemNumberBox = (XulTextbox) document.getElementById( "system-number-text" ); //$NON-NLS-1$
    clientBox = (XulTextbox) document.getElementById( "client-text" ); //$NON-NLS-1$
    doubleDecimalSeparatorCheck = (XulCheckbox) document.getElementById( "decimal-separator-check" ); //$NON-NLS-1$
    useIntegratedSecurityCheck = (XulCheckbox) document.getElementById( "use-integrated-security-check" ); //$NON-NLS-1$
    resultStreamingCursorCheck = (XulCheckbox) document.getElementById( "result-streaming-check" ); //$NON-NLS-1$
    poolingCheck = (XulCheckbox) document.getElementById( "use-pool-check" ); //$NON-NLS-1$
    clusteringCheck = (XulCheckbox) document.getElementById( "use-cluster-check" ); //$NON-NLS-1$
    clusterParameterDescriptionLabel =
        (XulLabel) document.getElementById( "cluster-parameter-description-label" ); //$NON-NLS-1$
    poolSizeLabel = (XulLabel) document.getElementById( "pool-size-label" ); //$NON-NLS-1$
    poolSizeBox = (XulTextbox) document.getElementById( "pool-size-text" ); //$NON-NLS-1$
    maxPoolSizeLabel = (XulLabel) document.getElementById( "max-pool-size-label" ); //$NON-NLS-1$
    maxPoolSizeBox = (XulTextbox) document.getElementById( "max-pool-size-text" ); //$NON-NLS-1$
    poolParameterTree = (XulTree) document.getElementById( "pool-parameter-tree" ); //$NON-NLS-1$
    clusterParameterTree = (XulTree) document.getElementById( "cluster-parameter-tree" ); //$NON-NLS-1$
    optionsParameterTree = (XulTree) document.getElementById( "puc_options-parameter-tree" ); //$NON-NLS-1$
    poolingDescription = (XulTextbox) document.getElementById( "pooling-description" ); //$NON-NLS-1$ 
    poolingParameterDescriptionLabel =
        (XulLabel) document.getElementById( "pool-parameter-description-label" ); //$NON-NLS-1$
    poolingDescriptionLabel = (XulLabel) document.getElementById( "pooling-description-label" ); //$NON-NLS-1$ 
    quoteIdentifiersCheck = (XulCheckbox) document.getElementById( "quote-identifiers-check" ); //$NON-NLS-1$;
    lowerCaseIdentifiersCheck = (XulCheckbox) document.getElementById( "force-lower-case-check" ); //$NON-NLS-1$;
    upperCaseIdentifiersCheck = (XulCheckbox) document.getElementById( "force-upper-case-check" ); //$NON-NLS-1$;
    sqlBox = (XulTextbox) document.getElementById( "sql-text" ); //$NON-NLS-1$;
    webAppName = (XulTextbox) document.getElementById( "web-application-name-text" );
    warehouseBox = (XulTextbox) document.getElementById( "warehouse-text" );
    jdbcAuthMethod = (XulMenuList) document.getElementById( "redshift-auth-method-list" );
    iamAccessKeyId = (XulTextbox) document.getElementById( "iam-access-key-id" );
    iamSecretKeyId = (XulTextbox) document.getElementById( "iam-secret-access-key" );
    iamSessionToken = (XulTextbox) document.getElementById( "iam-session-token" );
    iamProfileName = (XulTextbox) document.getElementById( "iam-profile-name" );
    azureSqlDBJdbcAuthMethod = (XulMenuList) document.getElementById( "azure-sql-db-auth-method-list" );
    azureSqlDBAlwaysEncryptionEnabled = (XulCheckbox) document.getElementById( "azure-sql-db-enable-always-encryption-on" );
    azureSqlDBClientSecretId = (XulTextbox) document.getElementById( "azure-sql-db-client-id" );
    azureSqlDBClientSecretKey = (XulTextbox) document.getElementById( "azure-sql-db-client-secret-key" );
  }

  private void showMessage( String title, String message, boolean scroll ) {
    try {
      XulMessageBox box = (XulMessageBox) document.createElement( "messagebox" ); //$NON-NLS-1$
      box.setTitle( title );
      box.setMessage( message );
      box.setModalParent(
          ( (XulRoot) document.getElementById( "general-datasource-window" ) ).getRootObject() ); //$NON-NLS-1$
      if ( scroll ) {
        box.setScrollable( true );
        box.setWidth( 500 );
        box.setHeight( 400 );
      }
      box.open();
    } catch ( XulException e ) {
      System.out.println( "Error creating messagebox " + e.getMessage() ); //$NON-NLS-1$
      e.printStackTrace();
    }
  }

  public void handleUseSecurityCheckbox() {
    if ( useIntegratedSecurityCheck != null ) {
      if ( useIntegratedSecurityCheck.isChecked() ) {
        userNameBox.setDisabled( true );
        passwordBox.setDisabled( true );
      } else {
        userNameBox.setDisabled( false );
        passwordBox.setDisabled( false );
      }
    }
  }

  @Bindable
  public void showContextHelp() {
    final String baseDocUrl = messages.getString( "DataHandler.BaseDocUrl" );
    jsni_showContextHelp( baseDocUrl );
  }

  private native void jsni_showContextHelp( final String baseDocUrl )/*-{
   $wnd.open(baseDocUrl + "mk-95pdia001/pentaho-configuration/tasks-to-be-performed-by-a-pentaho-administrator/define-data-connections",
       "webHelp","width=1200,height=600,location=no,status=no,toolbar=no");
  }-*/;

  public static String getBaseURL() {
    String moduleUrl = GWT.getModuleBaseURL();
    //
    // Set the base url appropriately based on the context in which we are running this client
    //
    if ( moduleUrl.indexOf( "content" ) > -1 ) { //$NON-NLS-1$
      // we are running the client in the context of a BI Server plugin, so
      // point the request to the GWT rpc proxy servlet
      String baseUrl = moduleUrl.substring( 0, moduleUrl.indexOf( "content" ) ); //$NON-NLS-1$
      return baseUrl + "plugin/data-access/api/connection/"; //$NON-NLS-1$
    }

    return moduleUrl + "plugin/data-access/api/connection/"; //$NON-NLS-1$
  }

  /**
   * Disables the refresh on the {@link IFragmentHandler}
   *
   * @param disableRefresh boolean - disables the ability to refresh the options
   */
  public void setFragmentHandlerDisableRefresh( boolean disableRefresh ) {
    if ( this.fragmentHandler != null ) {
      this.fragmentHandler.setDisableRefresh( disableRefresh );
    }
  }

  private void traverseDomSetReadOnly( XulComponent component, boolean readonly ) {
    component.setDisabled( readonly );
    List<XulComponent> children = component.getChildNodes();
    if ( children != null && children.size() > 0 ) {
      for ( XulComponent child : children ) {
        child.setDisabled( readonly );
        traverseDomSetReadOnly( child, readonly );
      }
    }
  }

  // instead org.apache.commons.lang.StringUtils.isBlank( str )
  private static final boolean isBlank( String str ) {
    return ( str == null || str.trim().length() == 0 );
  }
}
