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

package org.pentaho.ui.database;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Shell;
import org.pentaho.database.service.IDatabaseConnectionService;
import org.pentaho.database.util.DatabaseTypeHelper;
import org.pentaho.ui.database.event.DataHandler;
import org.pentaho.ui.database.event.FragmentHandler;
import org.pentaho.ui.util.Launch;
import org.pentaho.ui.xul.XulDomContainer;
import org.pentaho.ui.xul.XulException;
import org.pentaho.ui.xul.XulRunner;
import org.pentaho.ui.xul.swt.SwtXulLoader;
import org.pentaho.ui.xul.swt.SwtXulRunner;

public class DatabaseConnectionDialog {

  public static final String DIALOG_DEFINITION_FILE = "org/pentaho/ui/database/resources/databasedialog.xul"; //$NON-NLS-1$

  private Map<String, String> extendedClasses = new HashMap<String, String>();
  private IDatabaseConnectionService connectionService;
  private DatabaseTypeHelper databaseTypeHelper;
  public DatabaseConnectionDialog(IDatabaseConnectionService connectionService, DatabaseTypeHelper databaseTypeHelper) {
    this.connectionService = connectionService;
    this.databaseTypeHelper = databaseTypeHelper;
  }

  public void registerClass(String key, String className) {
    extendedClasses.put(key, className);
  }

  public XulDomContainer getSwtInstance(Shell shell) throws XulException {

    Messages messages = new Messages();
    XulDomContainer container = null;
    SwtXulLoader loader = new SwtXulLoader();

    Iterable<String> keyIterable = extendedClasses.keySet();
    for (Object key : keyIterable) {
      loader.register((String) key, extendedClasses.get(key));
    }
    
    loader.setOuterContext(shell);
    
    container = loader.loadXul(DIALOG_DEFINITION_FILE, messages.getBundle());
    
    // ui services
    
    // ui controllers
    DataHandler dataHandler = new DataHandler();
    FragmentHandler fragmentHandler = new FragmentHandler();
    
    
    dataHandler.setDatabaseTypeHelper(databaseTypeHelper);
    dataHandler.setMessages(messages);
//    dataHandler.setAsyncDatabaseConnectionService(service);
    dataHandler.setLaunch(new Launch());
    dataHandler.setFragmentHandler(fragmentHandler);

    fragmentHandler.setDatabaseTypeHelper(databaseTypeHelper);
    
    container.addEventHandler(fragmentHandler);
    container.addEventHandler(dataHandler);
    
    final XulRunner runner = new SwtXulRunner();
    runner.addContainer(container);
    
    // call onload methods with this call
    runner.initialize();
    
    return container;
  }

}
