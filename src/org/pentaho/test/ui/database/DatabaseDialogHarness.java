package org.pentaho.test.ui.database;

import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.ui.xul.XulDomContainer;
import org.pentaho.ui.xul.containers.XulWindow;
import org.pentaho.ui.xul.swt.SwtXulLoader;

public class DatabaseDialogHarness {

  /**
   * @param args
   */
  public static void main(String[] args) {
    try {
      InputStream in = DatabaseDialogHarness.class.getClassLoader().getResourceAsStream("org/pentaho/ui/database/databasedialog.xul");
      if (in == null) {
        System.out.println("Invalid Input");
        return;
      }
      SAXReader rdr = new SAXReader();
      Document doc = rdr.read(in);

      XulDomContainer container = new SwtXulLoader().loadXul(doc);
      XulWindow dialog = (XulWindow)container.getDocumentRoot().getXulElement();
      dialog.open();
      DatabaseMeta database = (DatabaseMeta)container.getEventHandler("dataHandler").getData();
 
      if (database != null){
        String message = "Name: ".concat(database.getName()).concat(System.getProperty("line.separator"))
                         .concat("Database Name: ").concat(database.getDatabaseName()).concat(System.getProperty("line.separator"))
                         .concat("Host Name: ").concat(database.getHostname()).concat(System.getProperty("line.separator"))
                         .concat("Port Number: ").concat(database.getDatabasePortNumberString()).concat(System.getProperty("line.separator"))
                         .concat("User Name: ").concat(database.getUsername()).concat(System.getProperty("line.separator"))
                         .concat("Password: ").concat(database.getPassword()).concat(System.getProperty("line.separator"))
                         .concat("Driver Class: ").concat(database.getDriverClass()).concat(System.getProperty("line.separator"))
                         .concat("URL: ").concat(database.getURL());
 
        Shell shell = new Shell(SWT.DIALOG_TRIM);
        shell.setLayout(new RowLayout());
        Label label = new Label(shell, SWT.None);
        label.setText(message);
        shell.pack();
        shell.open(); 

        while(!shell.isDisposed()) {
          if(!shell.getDisplay().readAndDispatch()) {
            shell.getDisplay().sleep();
          }
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
