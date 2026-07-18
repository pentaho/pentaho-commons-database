/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 - 2026 by Pentaho Canada Inc. : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2030-06-15
 ******************************************************************************/



package org.pentaho.ui.database.event;

public interface IFragmentHandler {
  public interface Callback {
    public void callback();
  }

  public boolean isRefreshDisabled();

  public void setDisableRefresh(boolean disableRefresh);

  public void refreshOptionsWithCallback(Callback callback);
}
