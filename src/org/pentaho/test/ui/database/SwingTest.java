package org.pentaho.test.ui.database;

import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.ui.xul.XulDomContainer;
import org.pentaho.ui.xul.XulException;
import org.pentaho.ui.xul.containers.XulWindow;
import org.pentaho.ui.xul.swing.SwingXulLoader;
import org.pentaho.ui.xul.swt.SwtXulLoader;

public class SwingTest {

	DatabaseMeta database = null;

	public static void main(String[] args) {

		SwingTest harness = new SwingTest();

		try {
			InputStream in = DatabaseDialogHarness.class.getClassLoader()
					.getResourceAsStream("org/pentaho/ui/database/databasedialog.xul");
			if (in == null) {
				System.out.println("Invalid Input");
				return;
			}

			SAXReader rdr = new SAXReader();
			final Document doc = rdr.read(in);

			harness.showDialog(doc);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void showDialog(final Document doc) {

		XulDomContainer container = null;
		try {
			container = new SwingXulLoader().loadXul(doc);
			if (database != null) {
				container.getEventHandler("dataHandler").setData(database);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		XulWindow dialog = (XulWindow) container.getDocumentRoot().getRootElement();
		container.initialize();
		dialog.open();
		try{
			Object data = container.getEventHandler("dataHandler").getData();
			int i=0;
			i++;
		} catch(XulException e){
			System.out.println("Error getting data");
		}
	}

}
