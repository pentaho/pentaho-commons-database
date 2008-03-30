package org.pentaho.ui.database.swt.event;

import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.pentaho.di.core.database.DatabaseInterface;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.ui.xul.XulContainer;
import org.pentaho.ui.xul.XulDomContainer;
import org.pentaho.ui.xul.XulEventHandler;
import org.pentaho.ui.xul.XulException;
import org.pentaho.ui.xul.components.XulTextbox;
import org.pentaho.ui.xul.containers.XulListbox;
import org.pentaho.ui.xul.dom.Element;
import org.pentaho.ui.xul.swt.SwtXulLoader;

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
    
    
    Element groupElement = document.getElementById("database-options-box");
    Element parentElement = groupElement.getParent();


    Document doc;
    XulDomContainer fragmentContainer = null;
    try {
      
      // Get new group box fragment ...
      // This will effectively set up the SWT parent child relationship...
      
      fragmentContainer = this.xulDomContainer.loadFragment(fragmentUri);
      Element newGroup = fragmentContainer.getDocumentRoot().getFirstChild();
      parentElement.replaceChild(groupElement, newGroup);
      
    } catch (XulException e) {
      System.out.println("Error loading Database Fragment: "+e.getMessage());
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

    switch(access){
      case DatabaseMeta.TYPE_ACCESS_JNDI:
        loadDatabaseOptionsFragment(packagePath.concat("common_jndi.xul"));
        break;
      case DatabaseMeta.TYPE_ACCESS_NATIVE:
        fragment = packagePath.concat(database.getDatabaseTypeDesc()).concat("_native.xul");
        in = getClass().getClassLoader().getResourceAsStream(fragment);
        if (in == null){
          fragment = packagePath.concat("common_native.xul");
        }
        loadDatabaseOptionsFragment(fragment);
        break;
      case DatabaseMeta.TYPE_ACCESS_OCI:
        fragment = packagePath.concat(database.getDatabaseTypeDesc()).concat("_oci.xul");
        in = getClass().getClassLoader().getResourceAsStream(fragment);
        if (in == null){
          fragment = packagePath.concat("common_native.xul");
        }
        loadDatabaseOptionsFragment(fragment);
        break;
      case DatabaseMeta.TYPE_ACCESS_ODBC:
        loadDatabaseOptionsFragment(packagePath.concat("common_odbc.xul"));
        break;
      case DatabaseMeta.TYPE_ACCESS_PLUGIN:
        fragment = packagePath.concat(database.getDatabaseTypeDesc()).concat("_plugin.xul");
        in = getClass().getClassLoader().getResourceAsStream(fragment);
        if (in == null){
          fragment = packagePath.concat("common_native.xul");
        }
        loadDatabaseOptionsFragment(fragment);
        break;
    }
    
    XulTextbox portBox = (XulTextbox)document.getElementById("port-number-text");
    if (portBox != null){
      int port = database.getDefaultDatabasePort();
      if (port > 0){
        portBox.setValue(Integer.toString(port));
      }
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
