package org.pentaho.ui.database.swt.event;

import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.eclipse.swt.widgets.Composite;
import org.pentaho.di.core.database.DatabaseInterface;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.ui.xul.XulContainer;
import org.pentaho.ui.xul.XulDomContainer;
import org.pentaho.ui.xul.XulEventHandler;
import org.pentaho.ui.xul.components.XulTextbox;
import org.pentaho.ui.xul.containers.XulListbox;
import org.pentaho.ui.xul.dom.Element;
import org.pentaho.ui.xul.swt.SwtXulLoader;

public class FragmentHandler extends XulEventHandler {
  
  private XulListbox connectionBox;
  private XulListbox accessBox;

  private String packagePath = "org/pentaho/ui/database/";
  
  public FragmentHandler() {
  }
  
  private void loadDatabaseOptionsFragment(String fragmentUri){
    
    // TODO This could be a generic method for removing and replacing children...
    
    InputStream in = getClass().getClassLoader().getResourceAsStream(fragmentUri);
    if (in == null) {
      // TODO log error
      return;
    }

    Element groupElement = document.getElementById("database-options-box");
    Composite group = (Composite)groupElement.getXulElement().getManagedObject();
    
    org.pentaho.ui.xul.dom.Element parentElement = document.getElementById("database-options-box").getParent();

    group.dispose();
    parentElement.removeChild(groupElement);

    Document doc;
    XulDomContainer fragmentContainer = null;
    try {
      SAXReader rdr = new SAXReader();
      doc = rdr.read(in);
      fragmentContainer = new SwtXulLoader().loadXulFragment(doc,(XulContainer)parentElement);
      
      parentElement.addChild(fragmentContainer.getDocumentRoot());
      parentElement.getXulElement().layout();

      Composite parentComposite = (Composite) parentElement.getXulElement().getManagedObject();
      parentComposite.layout(true);
    
    } catch (Exception e) {
      // TODO catch exception
    }
    
    if (fragmentContainer == null){
      return;
    }
    
  }
  
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


}
