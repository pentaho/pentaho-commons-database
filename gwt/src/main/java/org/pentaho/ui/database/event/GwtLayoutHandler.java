/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2028-08-13
 ******************************************************************************/

package org.pentaho.ui.database.event;

import com.google.gwt.user.client.Command;
import org.pentaho.ui.xul.containers.XulTree;
import org.pentaho.ui.xul.gwt.tags.GwtTree;

/**
 * @author Rowell Belen
 */
public class GwtLayoutHandler {

  public static void deferUpdateUI( XulTree xulTree, Command command ) {

    GwtTree gwtTree = null;
    try {

      gwtTree = (GwtTree) xulTree;

      if ( gwtTree != null ) {
        gwtTree.suppressLayout( true );
      }

      if ( command != null ) {
        command.execute();
      }
    } finally {
      if ( gwtTree != null ) {
        gwtTree.suppressLayout( false );
        gwtTree.updateUI();
      }
    }
  }

}
