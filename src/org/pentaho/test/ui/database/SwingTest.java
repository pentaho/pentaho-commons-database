package org.pentaho.test.ui.database;

import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.ui.xul.XulDomContainer;
import org.pentaho.ui.xul.containers.XulWindow;
import org.pentaho.ui.xul.swing.SwingXulLoader;

public class SwingTest {

       DatabaseMeta database = null;

       public static void main(String[] args) {

         SwingTest harness = new SwingTest();
         
         try {
           InputStream in = DatabaseDialogHarness.class.getClassLoader()
                 .getResourceAsStream("org/pentaho/ui/database/databasedialog.xul"); //$NON-NLS-1$
           if (in == null) {
             System.out.println("Invalid Input"); //$NON-NLS-1$
             return;
           }
           
           SAXReader rdr = new SAXReader();
           final Document doc = rdr.read(in);
           
           harness.showDialog(doc);
           
         } catch (Exception e) {
           e.printStackTrace();
         }

       }
       
       private void showDialog(final Document doc){

         XulDomContainer container = null;
         try {
           container = new SwingXulLoader().loadXul(doc);
           if (database != null){
             container.getEventHandler("dataHandler").setData(database); //$NON-NLS-1$
           }
         } catch (Exception e) {
           e.printStackTrace();
         } 
         XulWindow dialog = (XulWindow) container.getDocumentRoot().getRootElement();
         container.initialize();
         dialog.open();

       
       }


     }
