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

package org.pentaho.ui.database.gwt;

import com.google.gwt.core.client.Scheduler;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.util.DatabaseTypeHelper;
import org.pentaho.gwt.widgets.client.utils.i18n.ResourceBundle;
import org.pentaho.ui.database.event.DataHandler;
import org.pentaho.ui.database.event.DatabaseDialogListener;
import org.pentaho.ui.database.event.GwtFragmentHandler;
import org.pentaho.ui.xul.XulException;
import org.pentaho.ui.xul.containers.XulDialog;
import org.pentaho.ui.xul.gwt.GwtXulDomContainer;
import org.pentaho.ui.xul.gwt.GwtXulRunner;
import org.pentaho.ui.xul.gwt.util.AsyncXulLoader;
import org.pentaho.ui.xul.gwt.util.IXulLoaderCallback;

import com.google.gwt.core.client.GWT;

public class GwtDatabaseDialog {

  protected DatabaseTypeHelper databaseTypeHelper;

  protected DataHandler dataHandler = new DataHandler();

  protected GwtFragmentHandler fragmentHandler = new GwtFragmentHandler();

  protected GwtMessages messages = new GwtMessages();

  protected String overlay = null; // no overlay by default

  protected String overlayResource = "databasedialog"; //$NON-NLS-1$

  protected DatabaseDialogListener listener;

  protected XulDialog dialog;

  public GwtDatabaseDialog(DatabaseTypeHelper databaseTypeHelper, DatabaseDialogListener listener) {
    this.databaseTypeHelper = databaseTypeHelper;
    this.listener = listener;

    AsyncXulLoader
        .loadXulFromUrl(
            GWT.getModuleBaseURL() + "databasedialog.xul", GWT.getModuleBaseURL() + "databasedialog", new InternalCallback()); //$NON-NLS-1$//$NON-NLS-2$
  }

  public GwtDatabaseDialog(DatabaseTypeHelper databaseTypeHelper, String overlay, DatabaseDialogListener listener) {
    this.databaseTypeHelper = databaseTypeHelper;
    this.overlay = overlay;
    this.listener = listener;

    AsyncXulLoader
        .loadXulFromUrl(
            GWT.getModuleBaseURL() + "databasedialog.xul", GWT.getModuleBaseURL() + "databasedialog", new InternalCallback()); //$NON-NLS-1$//$NON-NLS-2$
  }

  public GwtDatabaseDialog(DatabaseTypeHelper databaseTypeHelper, String overlay, String overlayResource,
      DatabaseDialogListener listener) {
    this.databaseTypeHelper = databaseTypeHelper;
    this.overlay = overlay;
    this.overlayResource = overlayResource;
    this.listener = listener;

    AsyncXulLoader
        .loadXulFromUrl(
            GWT.getModuleBaseURL() + "databasedialog.xul", GWT.getModuleBaseURL() + "databasedialog", new InternalCallback()); //$NON-NLS-1$//$NON-NLS-2$
  }

  public void show() {
    dialog.show();
  }

  public void setDatabaseConnection(final IDatabaseConnection conn) {
    Scheduler.get().scheduleDeferred( new Scheduler.ScheduledCommand() {
      @Override public void execute() {
        dataHandler.setData(conn);
      }
    } );
  }

  public boolean isDialogReady() {
    return dialog != null;
  }

  public IDatabaseConnection getDatabaseConnection() {
    return (IDatabaseConnection) dataHandler.getData();
  }

  private class InternalCallback implements IXulLoaderCallback {
    public void overlayLoaded() {
    }

    public void overlayRemoved() {
    }

    public void xulLoaded(final GwtXulRunner runner) {
      try {
        // register our event handlers
        final GwtXulDomContainer container = (GwtXulDomContainer) runner.getXulDomContainers().get(0);
        messages.setMessageBundle((ResourceBundle) container.getResourceBundles().get(0));
        dataHandler.setMessages(messages);
        if (listener != null) {
          dataHandler.setDatabaseDialogListener(listener);
        }
        dataHandler.setLaunch(new GwtLaunch());
        dataHandler.setDatabaseTypeHelper(databaseTypeHelper);
        dataHandler.setFragmentHandler(fragmentHandler);

        container.addEventHandler(dataHandler);

        fragmentHandler.setMessages(messages);
        fragmentHandler.setDatabaseTypeHelper(databaseTypeHelper);
        container.addEventHandler(fragmentHandler);

        Scheduler.get().scheduleDeferred( new Scheduler.ScheduledCommand() {
          @Override public void execute() {
            fragmentHandler.setDisableRefresh(true);
            try {
              runner.initialize();
            } catch ( XulException e ) {
              e.printStackTrace();
            }
            fragmentHandler.setDisableRefresh(false);
          }
        } );

        if (overlay != null) {
          final IXulLoaderCallback callback2 = new IXulLoaderCallback() {
            public void overlayLoaded() {
              dialog = (XulDialog) container.getDocumentRoot().getElementById("general-datasource-window"); //$NON-NLS-1$
              if (listener != null) {
                listener.onDialogReady();
              }
            }

            public void overlayRemoved() {
            }

            public void xulLoaded(GwtXulRunner runner) {
            }
          };

          Scheduler.get().scheduleDeferred( new Scheduler.ScheduledCommand() {
            @Override public void execute() {
              AsyncXulLoader.loadOverlayFromUrl(overlay, overlayResource, container, callback2);
            }
          } );
        } else {
          dialog = (XulDialog) container.getDocumentRoot().getElementById("general-datasource-window"); //$NON-NLS-1$
          if (listener != null) {
            listener.onDialogReady();
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
