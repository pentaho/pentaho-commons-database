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

import org.pentaho.database.model.IDatabaseConnection;

public interface DatabaseDialogListener {
  public void onDialogReady();
  public void onDialogAccept(IDatabaseConnection connection);
  public void onDialogCancel();
}
