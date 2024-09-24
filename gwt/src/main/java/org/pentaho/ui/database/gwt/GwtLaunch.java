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

import org.pentaho.ui.database.event.ILaunch;
import org.pentaho.ui.database.event.IMessages;

import com.google.gwt.user.client.Window;

public class GwtLaunch implements ILaunch {

  public Status openUrl(String url, IMessages messages) {
    Window.open(url, "_blank", "");  //$NON-NLS-1$ //$NON-NLS-2$
    return Status.Success;
  }
  
}
