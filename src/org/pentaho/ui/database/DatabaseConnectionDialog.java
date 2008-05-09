package org.pentaho.ui.database;

import java.util.HashMap;
import java.util.Map;

import org.pentaho.ui.xul.XulComponent;
import org.pentaho.ui.xul.XulDomContainer;
import org.pentaho.ui.xul.XulException;
import org.pentaho.ui.xul.containers.XulDialog;
import org.pentaho.ui.xul.containers.XulWindow;
import org.pentaho.ui.xul.dom.Document;
import org.pentaho.ui.xul.impl.AbstractXulComponent;
import org.pentaho.ui.xul.impl.XulWindowContainer;
import org.pentaho.ui.xul.swt.SwtXulLoader;

public class DatabaseConnectionDialog {

  public static final String DIALOG_DEFINITION_FILE = "org/pentaho/ui/database/databasedialog.xul"; //$NON-NLS-1$

  private Map<String, String> extendedClasses = new HashMap<String, String>();

  public DatabaseConnectionDialog() {
  }

  public void registerClass(String key, String className) {
    extendedClasses.put(key, className);
  }

  public XulDomContainer getSwtInstance() throws XulException {

    XulDomContainer container = null;
    SwtXulLoader loader = new SwtXulLoader();

    Iterable<String> keyIterable = extendedClasses.keySet();
    for (Object key : keyIterable) {
      loader.register((String) key, extendedClasses.get(key));
    }

    container = loader.loadXul(DIALOG_DEFINITION_FILE, Messages.getBundle());
    return container;
  }

  /* 
   * Ugly as heck code, but better isolated to one place than proliferated throughout an app that needs this thing.
   */
  public static XulDialog getAsDialog(XulWindowContainer currDomContainer) throws XulException {
    XulDomContainer fragmentContainer = currDomContainer.loadFragment(DatabaseConnectionDialog.DIALOG_DEFINITION_FILE,
        Messages.getBundle());
    //TODO: Add convenience method to get at wrapped fragment root.
    Document document = currDomContainer.getDocumentRoot();

    //Re-parent contents of Window into a Dialog
    //TODO: Remove reparenting once SWT has Dialog Support
    XulWindow window = (XulWindow) fragmentContainer.getDocumentRoot().getRootElement();
    XulDialog dialog = (XulDialog) document.createElement("dialog");
    dialog.setId(window.getId());

    for (XulComponent comp : window.getChildNodes()) {
      dialog.addChild(comp);
      dialog.addComponent(comp);
    }
    dialog.setOnload(window.getOnload());
    dialog.setWidth(window.getWidth());
    dialog.setHeight(window.getHeight());

    ((AbstractXulComponent) dialog).layout();

    //Merge in Event Handlers
    currDomContainer.mergeContainer(fragmentContainer);

    //Add to document
    currDomContainer.getDocumentRoot().getRootElement().addChild(dialog);

    return dialog;
  }
}
