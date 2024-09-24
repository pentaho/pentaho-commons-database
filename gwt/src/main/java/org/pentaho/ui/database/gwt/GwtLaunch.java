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

package org.pentaho.ui.database.gwt;

import org.pentaho.ui.database.event.ILaunch;
import org.pentaho.ui.database.event.IMessages;

import com.google.gwt.user.client.Window;

public class GwtLaunch implements ILaunch {

  public Status openUrl(String url, IMessages messages) {
    Window.open(url, "_blank", "");  //$NON-NLS-1$ //$NON-NLS-2$
    return Status.Success;
  }
  
}
