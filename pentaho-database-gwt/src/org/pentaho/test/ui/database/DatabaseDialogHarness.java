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
* Copyright (c) 2002-2017 Hitachi Vantara..  All rights reserved.
*/

package org.pentaho.test.ui.database;

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.DatabaseConnection;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.model.PartitionDatabaseMeta;
import org.pentaho.database.service.DatabaseConnectionService;
import org.pentaho.database.service.DatabaseDialectService;
import org.pentaho.database.service.IDatabaseConnectionService;
import org.pentaho.database.service.IDatabaseDialectService;
import org.pentaho.database.util.DatabaseTypeHelper;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.ui.database.DatabaseConnectionDialog;
import org.pentaho.ui.xul.XulDomContainer;
import org.pentaho.ui.xul.XulException;
import org.pentaho.ui.xul.containers.XulDialog;
import org.pentaho.ui.xul.containers.XulRoot;
import org.pentaho.ui.xul.containers.XulWindow;

public class DatabaseDialogHarness {

  IDatabaseConnection database = null;

  IDatabaseConnectionService service = new DatabaseConnectionService();
  IDatabaseDialectService dialectService = new DatabaseDialectService();
  DatabaseTypeHelper databaseTypeHelper = new DatabaseTypeHelper(dialectService.getDatabaseTypes());
  
  public static void main(String[] args) throws Exception {
//    dialog.setDatabaseConnection(conn);
    KettleEnvironment.init(false);
    DatabaseDialogHarness harness = new DatabaseDialogHarness();

    harness.database = new DatabaseConnection();
    harness.database.setDatabaseType(harness.databaseTypeHelper.getDatabaseTypeByName("Hypersonic"));
    harness.database.setAccessType(DatabaseAccessType.NATIVE);
    harness.database.setHostname("localhost");
    harness.database.setName("My Connection");
        
    harness.showDialog();
  }
  
  private void showDialog(){
    XulDomContainer container = null;
    try {
      DatabaseConnectionDialog dcDialog = new DatabaseConnectionDialog(service, databaseTypeHelper);
      container = dcDialog.getSwtInstance(new Shell(SWT.NONE));
      if (database != null){
        container.getEventHandler("dataHandler").setData(database); //$NON-NLS-1$
      }
    } catch (XulException e) {
       e.printStackTrace();
    }

    XulRoot root = (XulRoot) container.getDocumentRoot().getRootElement();
    if (root instanceof XulDialog){
      ((XulDialog)root).show();
    }
    if (root instanceof XulWindow){
      ((XulWindow)root).open();
    }

    try {
      database = (IDatabaseConnection) container.getEventHandler("dataHandler").getData(); //$NON-NLS-1$
    } catch (Exception e) {
      e.printStackTrace();
    }

    String message = DatabaseDialogHarness.setMessage(database);
    Shell shell = new Shell(SWT.DIALOG_TRIM);
    shell.setLayout(new RowLayout());
    Label label = new Label(shell, SWT.NONE);
    label.setText(message);
    Button button = new Button(shell, SWT.NONE);
    button.setText("Edit Database ..."); //$NON-NLS-1$

    button.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        try {
          showDialog();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    shell.pack();
    shell.open();

    while (!shell.isDisposed()) {
      if (!shell.getDisplay().readAndDispatch()) {
        shell.getDisplay().sleep();
      }
    }
  }

  private static String setMessage(IDatabaseConnection database) {
    String message = ""; //$NON-NLS-1$
    if (database != null) {
      String carriageReturn = System.getProperty("line.separator"); //$NON-NLS-1$
      try {
        message = "Name: ".concat(database.getName()).concat(carriageReturn) //$NON-NLS-1$
        .concat("Database Name: ").concat(database.getDatabaseName()).concat(carriageReturn) //$NON-NLS-1$
        .concat("Host Name: ").concat(database.getHostname()).concat(carriageReturn) //$NON-NLS-1$
        .concat("Port Number: ").concat(database.getDatabasePort()).concat(carriageReturn) //$NON-NLS-1$
                .concat("User Name: ").concat(database.getUsername()).concat(carriageReturn) //$NON-NLS-1$
                .concat("Password: ").concat(database.getPassword()).concat(carriageReturn) //$NON-NLS-1$
                // .concat("Driver Class: ").concat(database.getDatabaseType().getDriverClass()).concat(carriageReturn) //$NON-NLS-1$
                //.concat("URL: ").concat(new DatabaseConnectionService().getJdbcUrl(database)).concat(carriageReturn); //$NON-NLS-1$
        ;
        
        Iterator<String> keys = database.getExtraOptions().keySet().iterator();
        message = message.concat(carriageReturn).concat("Option Parameters:").concat(carriageReturn); //$NON-NLS-1$
        while (keys.hasNext()){
            String parameter = keys.next();
            String value = database.getExtraOptions().get(parameter);
            message = message.concat(carriageReturn).concat(parameter).concat(": ").concat(value).concat(carriageReturn); //$NON-NLS-1$
        }
        
        message = message.concat(carriageReturn).concat("SQL: ") //$NON-NLS-1$
        .concat(database.getConnectSql()!=null ? database.getConnectSql() : "").concat(carriageReturn) //$NON-NLS-1$
        .concat("Quote Identifiers: ").concat(Boolean.toString(database.isQuoteAllFields())).concat(carriageReturn) //$NON-NLS-1$
        .concat("Upper Case Identifiers: ").concat(Boolean.toString(database.isForcingIdentifiersToUpperCase())).concat(carriageReturn) //$NON-NLS-1$
        .concat("Lower Case Identifiers: ").concat(Boolean.toString(database.isForcingIdentifiersToLowerCase())).concat(carriageReturn); //$NON-NLS-1$
        
        message = message.concat(carriageReturn).concat("Is Partitioned: ") //$NON-NLS-1$
        .concat(Boolean.toString(database.isPartitioned())).concat(carriageReturn);
        
        if (database.isPartitioned()){
          List<PartitionDatabaseMeta> partitions = database.getPartitioningInformation();
          if (partitions != null){
            for (int i = 0; i < partitions.size(); i++) {
              PartitionDatabaseMeta pdm = partitions.get(i);
              message = message.concat(carriageReturn).concat(Integer.toString(i)).concat(". ID: ") //$NON-NLS-1$
              .concat(pdm.getPartitionId()).concat(", Host: ") //$NON-NLS-1$
              .concat(pdm.getHostname()).concat(", Db: ") //$NON-NLS-1$
              .concat(pdm.getDatabaseName()).concat(", Port: ") //$NON-NLS-1$
              .concat(pdm.getPort()).concat(", User: ") //$NON-NLS-1$
              .concat(pdm.getUsername()).concat(", Pass: ") //$NON-NLS-1$
              .concat(pdm.getPassword()).concat(carriageReturn);
            }
          }
        }
        Iterator <String> poolKeys  = database.getConnectionPoolingProperties().keySet().iterator();
        message = message.concat(carriageReturn).concat("Pooling Parameters:").concat(carriageReturn); //$NON-NLS-1$
        while (poolKeys.hasNext()){
            String parameter = (String)poolKeys.next();
            String value = database.getConnectionPoolingProperties().get(parameter);
            message = message.concat(carriageReturn).concat(parameter).concat(": ").concat(value).concat(carriageReturn); //$NON-NLS-1$
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return message;

  }

}
