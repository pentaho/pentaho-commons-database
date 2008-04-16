package org.pentaho.ui.database.event;

import java.io.InputStream;

import org.dom4j.Document;
import org.pentaho.di.core.database.DatabaseInterface;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.ui.xul.XulComponent;
import org.pentaho.ui.xul.XulDomContainer;
import org.pentaho.ui.xul.XulException;
import org.pentaho.ui.xul.components.XulTextbox;
import org.pentaho.ui.xul.containers.XulListbox;
import org.pentaho.ui.xul.impl.XulEventHandler;

/**
 * Fragment handler deals with the logistics of replacing a portion of the dialog 
 * from a XUL fragment when the combination of database connection type and database 
 * access method calls for a replacement.
 *  
 * @author gmoran
 * @created Mar 19, 2008
 */
public class FragmentHandler extends XulEventHandler {
  
  private XulListbox connectionBox;
  private XulListbox accessBox;

  private String packagePath = "org/pentaho/ui/database/";
  
  public FragmentHandler() {
  }
  
  private void loadDatabaseOptionsFragment(String fragmentUri){
    
    
    XulComponent groupElement = document.getElementById("database-options-box");
    XulComponent parentElement = groupElement.getParent();


    Document doc;
    XulDomContainer fragmentContainer = null;
    try {
      
      // Get new group box fragment ...
      // This will effectively set up the SWT parent child relationship...
      
      fragmentContainer = this.xulDomContainer.loadFragment(fragmentUri);
      XulComponent newGroup = fragmentContainer.getDocumentRoot().getFirstChild();
      parentElement.replaceChild(groupElement, newGroup);
      
    } catch (XulException e) {
      //System.out.println("Error loading Database Fragment: "+e.getMessage());
      e.printStackTrace(System.out);
    }
    
    if (fragmentContainer == null){
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
  public void refreshOptions(){

    connectionBox = (XulListbox)document.getElementById("connection-type-list");
    accessBox = (XulListbox)document.getElementById("access-type-list");
    
    Object connectionKey = connectionBox.getSelectedItem();
    DatabaseInterface database = DataHandler.connectionMap.get(connectionKey);
    
    Object accessKey = accessBox.getSelectedItem();
    int access = DatabaseMeta.getAccessType((String)accessKey);
    
    String fragment = null;
    InputStream in = null;

    DataHandler dataHandler=null;
    try {
      dataHandler = (DataHandler)xulDomContainer.getEventHandler("dataHandler");
      dataHandler.pushCache();
    } catch (XulException e) {
      // TODO not a critical function, but should log a problem...
    }

    switch(access){
      case DatabaseMeta.TYPE_ACCESS_JNDI:
        fragment = packagePath.concat(database.getDatabaseTypeDesc()).concat("_jndi.xul");
        in = getClass().getClassLoader().getResourceAsStream(fragment.toLowerCase());
        if (in == null){
          fragment = packagePath.concat("common_jndi.xul");
        }
        loadDatabaseOptionsFragment(fragment.toLowerCase());
        break;
      case DatabaseMeta.TYPE_ACCESS_NATIVE:
        fragment = packagePath.concat(database.getDatabaseTypeDesc()).concat("_native.xul");
        in = getClass().getClassLoader().getResourceAsStream(fragment.toLowerCase());
        if (in == null){
          fragment = packagePath.concat("common_native.xul");
        }
        loadDatabaseOptionsFragment(fragment.toLowerCase());
        break;
      case DatabaseMeta.TYPE_ACCESS_OCI:
        fragment = packagePath.concat(database.getDatabaseTypeDesc()).concat("_oci.xul");
        in = getClass().getClassLoader().getResourceAsStream(fragment.toLowerCase());
        if (in == null){
          fragment = packagePath.concat("common_native.xul");
        }
        loadDatabaseOptionsFragment(fragment.toLowerCase());
        break;
      case DatabaseMeta.TYPE_ACCESS_ODBC:
        fragment = packagePath.concat(database.getDatabaseTypeDesc()).concat("_odbc.xul");
        in = getClass().getClassLoader().getResourceAsStream(fragment.toLowerCase());
        if (in == null){
          fragment = packagePath.concat("common_odbc.xul");
        }
        loadDatabaseOptionsFragment(fragment.toLowerCase());
        break;
      case DatabaseMeta.TYPE_ACCESS_PLUGIN:
        fragment = packagePath.concat(database.getDatabaseTypeDesc()).concat("_plugin.xul");
        in = getClass().getClassLoader().getResourceAsStream(fragment.toLowerCase());
        if (in == null){
          fragment = packagePath.concat("common_native.xul");
        }
        loadDatabaseOptionsFragment(fragment.toLowerCase());
        break;
    }
    
    XulTextbox portBox = (XulTextbox)document.getElementById("port-number-text");
    if (portBox != null){
      int port = database.getDefaultDatabasePort();
      if (port > 0){
        portBox.setValue(Integer.toString(port));
      }
    }
    
   if (dataHandler != null){
     dataHandler.popCache();
   }
    
  }
  
  @Override
  public Object getData() {
    return null;
  }

  @Override
  public void setData(Object arg0) {
  }


}
