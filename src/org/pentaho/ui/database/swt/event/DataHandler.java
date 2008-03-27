package org.pentaho.ui.database.swt.event;

import java.util.SortedMap;
import java.util.TreeMap;

import org.pentaho.di.core.database.DatabaseInterface;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.database.GenericDatabaseMeta;
import org.pentaho.di.core.database.SAPR3DatabaseMeta;
import org.pentaho.ui.xul.XulEventHandler;
import org.pentaho.ui.xul.components.XulCheckbox;
import org.pentaho.ui.xul.components.XulLabel;
import org.pentaho.ui.xul.components.XulMessageBox;
import org.pentaho.ui.xul.components.XulTextbox;
import org.pentaho.ui.xul.containers.XulDeck;
import org.pentaho.ui.xul.containers.XulListbox;

/**
 * Handles all manipulation of the DatabaseMeta, data retrieval from XUL DOM and rudimentary validation.
 * 
 *  TODO:
 *  2. Needs to be abstracted away from the DatabaseMeta object, so other tools 
 *  in the platform can use the dialog and their preferred database object.
 *  3. Needs exception handling, string resourcing and logging
 *   
 * @author gmoran
 * @created Mar 19, 2008
 *
 */
public class DataHandler extends XulEventHandler {

  public static final SortedMap<String, DatabaseInterface> connectionMap = new TreeMap<String, DatabaseInterface>();

  // The connectionMap allows us to keep track of the connection
  // type we are working with and the correlating database interface

  static {
    String[] dbTypeDescriptions = DatabaseMeta.getDBTypeDescLongList();
    DatabaseInterface[] dbInterfaces = DatabaseMeta.getDatabaseInterfaces();

    // Sort the connection types, and associate them with an instance of each interface...

    for (int i = 0; i < dbTypeDescriptions.length; i++) {
      connectionMap.put(dbTypeDescriptions[i], dbInterfaces[i]);
    }
  }

  private DatabaseMeta databaseMeta = null;

  private XulDeck dialogDeck;

  private XulListbox deckOptionsBox;

  private XulListbox connectionBox;

  private XulListbox accessBox;

  private XulTextbox connectionNameBox;

  private XulTextbox hostNameBox;

  private XulTextbox databaseNameBox;

  private XulTextbox portNumberBox;

  private XulTextbox userNameBox;

  private XulTextbox passwordBox;

  // Generic database specific
  private XulTextbox customDriverClassBox;

  // Generic database specific
  private XulTextbox customUrlBox;

  // Oracle specific
  private XulTextbox dataTablespaceBox;

  // Oracle specific
  private XulTextbox indexTablespaceBox;

  // MS SQL Server specific
  private XulTextbox serverInstanceBox;

  // Informix specific
  private XulTextbox serverNameBox;

  // SAP R/3 specific
  private XulTextbox languageBox;

  // SAP R/3 specific
  private XulTextbox systemNumberBox;

  // SAP R/3 specific
  private XulTextbox clientBox;

  // MS SQL Server specific
  private XulCheckbox doubleDecimalSeparatorCheck;

  // MySQL specific
  private XulCheckbox resultStreamingCursorCheck;
  
  private XulCheckbox poolingCheck;
  
  private XulCheckbox clusteringCheck;
  
  private XulTextbox poolSizeBox;
  
  private XulTextbox maxPoolSizeBox;

  private XulLabel poolSizeLabel;
  
  private XulLabel maxPoolSizeLabel;

  public DataHandler() {
  }

  public void loadConnectionData() {

    getControls();

    // Add sorted types to the listbox now.

    for (String key : connectionMap.keySet()) {
      connectionBox.addItem(key);
    }

    // HACK: Need to force height of list control, as it does not behave 
    // well when using relative layouting

    connectionBox.setRows(connectionBox.getRows());

    Object key = connectionBox.getSelectedItem();

    // Nothing selected yet...select first item.

    // TODO Implement a connection type preference,
    // and use that type as the default for 
    // new databases.

    if (key == null) {
      key = connectionMap.firstKey();
      connectionBox.setSelectedItem(key);
    }
    
    // HACK: Need to force selection of first panel

    if (dialogDeck != null){
      dialogDeck.setSelectedIndex(dialogDeck.getSelectedIndex());
    }

  }

  public void loadAccessData() {

    getControls();

    Object key = connectionBox.getSelectedItem();

    // Nothing selected yet...
    if (key == null) {
      key = connectionMap.firstKey();
      connectionBox.setSelectedItem(key);
      return;
    }

    DatabaseInterface database = connectionMap.get(key);

    int acc[] = database.getAccessTypeList();
    Object accessKey = accessBox.getSelectedItem();
    accessBox.removeItems();

    // Add those access types applicable to this conneciton type

    for (int value : acc) {
      accessBox.addItem(DatabaseMeta.getAccessTypeDescLong(value));
    }

    // HACK: Need to force height of list control, as it does not behave 
    // well when using relative layouting

    accessBox.setRows(accessBox.getRows());

    // May not exist for this connection type.

    accessBox.setSelectedItem(accessKey);

    // Last resort, set first as default
    if (accessBox.getSelectedItem() == null) {
      accessBox.setSelectedItem(DatabaseMeta.getAccessTypeDescLong(acc[0]));
    }
  }
  
  public void setDeckChildIndex(){
    
    getControls();
    int selected = deckOptionsBox.getSelectedIndex();
    dialogDeck.setSelectedIndex(selected);
    
  }

  public void onPoolingCheck(){
    if (poolingCheck != null){
      boolean dis = !poolingCheck.isChecked();
      if (poolSizeBox != null){
        poolSizeBox.setDisabled(dis);
      }
      if (maxPoolSizeBox != null){
        maxPoolSizeBox.setDisabled(dis);
      }
      if (poolSizeLabel != null){
        poolSizeLabel.setDisabled(dis);
      }
      if (maxPoolSizeLabel != null){
        maxPoolSizeLabel.setDisabled(dis);
      }
    }
  }
  
  public void onClusterCheck(){
    if (clusteringCheck != null){
      
    }
  }

  @Override
  public Object getData() {
    databaseMeta = new DatabaseMeta();
    this.getInfo(databaseMeta);
    return databaseMeta;
  }

  @Override
  public void setData(Object data) {
    if (data instanceof DatabaseMeta){
      databaseMeta = (DatabaseMeta)data;
    }
    setInfo(databaseMeta);
  }
  
  public void onCancel() {
    this.xulDomContainer.close();
  }

  public void onOK() {

    DatabaseMeta database = new DatabaseMeta();
    this.getInfo(database);

    String[] remarks = database.checkParameters();
    String message = ""; //$NON-NLS-1$

    if (remarks.length != 0) {
      for (int i = 0; i < remarks.length; i++) {
        message = message.concat("* ").concat(remarks[i]).concat(System.getProperty("line.separator")); //$NON-NLS-1$ //$NON-NLS-2$
      }
      XulMessageBox messageBox = xulDomContainer.createMessageBox(message);
      messageBox.open();
    } else {
      this.xulDomContainer.close();
      databaseMeta = database;
    }
  }

  public void testDatabaseConnection() {

    DatabaseMeta database = new DatabaseMeta();

    getInfo(database);
    String[] remarks = database.checkParameters();
    String message = ""; //$NON-NLS-1$

    if (remarks.length != 0) {
      for (int i = 0; i < remarks.length; i++) {
        message = message.concat("* ").concat(remarks[i]).concat(System.getProperty("line.separator")); //$NON-NLS-1$  //$NON-NLS-2$
      }
    } else {
      message = database.testConnection();
    }
      XulMessageBox messageBox = xulDomContainer.createMessageBox(message);
      messageBox.open();
  }

  private void getInfo(DatabaseMeta meta) {

    getControls();

    if (this.databaseMeta != null && this.databaseMeta != meta) {
      meta.initializeVariablesFrom(this.databaseMeta);
    }
    // Before we put all attributes back in, clear the old list to make sure...
    // Warning: the port is an attribute too now.
    // 
    meta.getAttributes().clear();

    // Name:
    meta.setName(connectionNameBox.getValue());

    // Connection type:
    Object connection = connectionBox.getSelectedItem();
    if (connection != null) {
      meta.setDatabaseType((String) connection);
    }

    // Access type:
    Object access = accessBox.getSelectedItem();
    if (access != null) {
      meta.setAccessType(DatabaseMeta.getAccessType((String) access));
    }

    // Hostname:
    if (hostNameBox != null) {
      meta.setHostname(hostNameBox.getValue());
    }

    // Database name:
    if (databaseNameBox != null) {
      meta.setDBName(databaseNameBox.getValue());
    }

    // Port number:
    if (portNumberBox != null) {
      meta.setDBPort(portNumberBox.getValue());
    }

    // Username:
    if (userNameBox != null) {
      meta.setUsername(userNameBox.getValue());
    }

    // Password:
    if (passwordBox != null) {
      meta.setPassword(passwordBox.getValue());
    }

    // Streaming result cursor:
    if (resultStreamingCursorCheck != null) {
      meta.setStreamingResults(resultStreamingCursorCheck.isChecked());
    }

    // Data tablespace:
    if (dataTablespaceBox != null) {
      meta.setDataTablespace(dataTablespaceBox.getValue());
    }

    // Index tablespace
    if (indexTablespaceBox != null) {
      meta.setIndexTablespace(indexTablespaceBox.getValue());
    }

    // The SQL Server instance name overrides the option.
    // Empty doesn't clear the option, we have mercy.

    if (serverInstanceBox != null) {
      if (serverInstanceBox.getValue().trim().length() > 0) {
        meta.setSQLServerInstance(serverInstanceBox.getValue());
      }
    }

    // SQL Server double decimal separator
    if (doubleDecimalSeparatorCheck != null) {
      meta.setUsingDoubleDecimalAsSchemaTableSeparator(doubleDecimalSeparatorCheck.isChecked());
    }

    // SAP Attributes...
    if (languageBox != null) {
      meta.getAttributes().put(SAPR3DatabaseMeta.ATTRIBUTE_SAP_LANGUAGE, languageBox.getValue());
    }
    if (systemNumberBox != null) {
      meta.getAttributes().put(SAPR3DatabaseMeta.ATTRIBUTE_SAP_SYSTEM_NUMBER, systemNumberBox.getValue());
    }
    if (clientBox != null) {
      meta.getAttributes().put(SAPR3DatabaseMeta.ATTRIBUTE_SAP_CLIENT, clientBox.getValue());
    }

    // Generic settings...
    if (customUrlBox != null) {
      meta.getAttributes().put(GenericDatabaseMeta.ATRRIBUTE_CUSTOM_URL, customUrlBox.getValue());
    }
    if (customDriverClassBox != null) {
      meta.getAttributes().put(GenericDatabaseMeta.ATRRIBUTE_CUSTOM_DRIVER_CLASS, customDriverClassBox.getValue());
    }

    // Server Name:  (Informix)
    if (serverNameBox != null) {
      meta.setServername(serverNameBox.getValue());
    }

  }

  private void setInfo(DatabaseMeta meta) {

    if (meta == null) {
      return;
    }

    // Name:
    connectionNameBox.setValue(meta.getName());

    // Connection type:
    connectionBox.setSelectedItem(meta.getDatabaseInterface().getDatabaseTypeDescLong());

    // Access type:
    accessBox.setSelectedItem(DatabaseMeta.getAccessTypeDescLong(meta.getAccessType()));

    getControls();

    if (hostNameBox != null) {
      hostNameBox.setValue(meta.getHostname());
    }

    // Database name:
    if (databaseNameBox != null) {
      databaseNameBox.setValue(meta.getDatabaseName());
    }

    // Port number:
    if (portNumberBox != null) {
      portNumberBox.setValue(meta.getDatabasePortNumberString());
    }

    // Username:
    if (userNameBox != null) {
      userNameBox.setValue(meta.getUsername());
    }

    // Password:
    if (passwordBox != null) {
      passwordBox.setValue(meta.getPassword());
    }

    // Streaming result cursor:
    if (resultStreamingCursorCheck != null) {
      resultStreamingCursorCheck.setChecked(meta.isStreamingResults());
    }

    // Data tablespace:
    if (dataTablespaceBox != null) {
      dataTablespaceBox.setValue(meta.getDataTablespace());
    }

    // Index tablespace
    if (indexTablespaceBox != null) {
      indexTablespaceBox.setValue(meta.getIndexTablespace());
    }

    if (serverInstanceBox != null) {
      serverInstanceBox.setValue(meta.getSQLServerInstance());
    }

    // SQL Server double decimal separator
    if (doubleDecimalSeparatorCheck != null) {
      doubleDecimalSeparatorCheck.setChecked(meta.isUsingDoubleDecimalAsSchemaTableSeparator());
    }

    // SAP Attributes...
    if (languageBox != null) {
      languageBox.setValue(meta.getAttributes().getProperty(SAPR3DatabaseMeta.ATTRIBUTE_SAP_LANGUAGE));
    }
    if (systemNumberBox != null) {
      systemNumberBox.setValue(meta.getAttributes().getProperty(SAPR3DatabaseMeta.ATTRIBUTE_SAP_SYSTEM_NUMBER));
    }
    if (clientBox != null) {
      clientBox.setValue(meta.getAttributes().getProperty(SAPR3DatabaseMeta.ATTRIBUTE_SAP_CLIENT));
    }

    // Generic settings...
    if (customUrlBox != null) {
      customUrlBox.setValue(meta.getAttributes().getProperty(GenericDatabaseMeta.ATRRIBUTE_CUSTOM_URL));
    }
    if (customDriverClassBox != null) {
      customDriverClassBox.setValue(meta.getAttributes().getProperty(GenericDatabaseMeta.ATRRIBUTE_CUSTOM_DRIVER_CLASS));
    }

    // Server Name:  (Informix)
    if (serverNameBox != null) {
      serverNameBox.setValue(meta.getServername());
    }

  }

  private void getControls() {

    // Not all of these controls are created at the same time.. that's OK, for now, just check
    // each one for null before using.

    dialogDeck = (XulDeck) document.getElementById("dialog-panel-deck"); //$NON-NLS-1$
    deckOptionsBox = (XulListbox)document.getElementById("deck-options-list"); //$NON-NLS-1$
    connectionBox = (XulListbox) document.getElementById("connection-type-list"); //$NON-NLS-1$
    accessBox = (XulListbox) document.getElementById("access-type-list"); //$NON-NLS-1$
    connectionNameBox = (XulTextbox) document.getElementById("connection-name-text"); //$NON-NLS-1$
    hostNameBox = (XulTextbox) document.getElementById("server-host-name-text"); //$NON-NLS-1$
    databaseNameBox = (XulTextbox) document.getElementById("database-name-text"); //$NON-NLS-1$
    portNumberBox = (XulTextbox) document.getElementById("port-number-text"); //$NON-NLS-1$
    userNameBox = (XulTextbox) document.getElementById("username-text"); //$NON-NLS-1$
    passwordBox = (XulTextbox) document.getElementById("password-text"); //$NON-NLS-1$
    dataTablespaceBox = (XulTextbox) document.getElementById("data-tablespace-text"); //$NON-NLS-1$
    indexTablespaceBox = (XulTextbox) document.getElementById("index-tablespace-text"); //$NON-NLS-1$
    serverInstanceBox = (XulTextbox) document.getElementById("instance-text"); //$NON-NLS-1$
    serverNameBox = (XulTextbox) document.getElementById("server-name-text"); //$NON-NLS-1$
    customUrlBox = (XulTextbox) document.getElementById("custom-url-text"); //$NON-NLS-1$
    customDriverClassBox = (XulTextbox) document.getElementById("custom-driver-class-text"); //$NON-NLS-1$
    languageBox = (XulTextbox) document.getElementById("language-text"); //$NON-NLS-1$
    systemNumberBox = (XulTextbox) document.getElementById("system-number-text"); //$NON-NLS-1$
    clientBox = (XulTextbox) document.getElementById("client-text"); //$NON-NLS-1$
    doubleDecimalSeparatorCheck = (XulCheckbox) document.getElementById("decimal-separator-check"); //$NON-NLS-1$
    resultStreamingCursorCheck = (XulCheckbox) document.getElementById("result-streaming-check"); //$NON-NLS-1$
    poolingCheck = (XulCheckbox) document.getElementById("use-pool-check"); //$NON-NLS-1$
    clusteringCheck = (XulCheckbox) document.getElementById("use-cluster-check"); //$NON-NLS-1$
    poolSizeLabel = (XulLabel) document.getElementById("pool-size-label"); //$NON-NLS-1$
    poolSizeBox = (XulTextbox) document.getElementById("pool-size-text"); //$NON-NLS-1$
    maxPoolSizeLabel = (XulLabel) document.getElementById("max-pool-size-label"); //$NON-NLS-1$
    maxPoolSizeBox = (XulTextbox) document.getElementById("max-pool-size-text"); //$NON-NLS-1$
    
  }

}
