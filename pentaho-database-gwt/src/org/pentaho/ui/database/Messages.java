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

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.pentaho.ui.database.event.IMessages;

public class Messages implements IMessages {
  
  private static final String BUNDLE_NAME = "org.pentaho.ui.database.resources.databasedialog"; //$NON-NLS-1$

  private ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

  public Messages() {
  }

  public ResourceBundle getBundle() {
    if(RESOURCE_BUNDLE == null){
      RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
    }
      
    return RESOURCE_BUNDLE;
  }
  
  public String getString(String key) {
    try {
      return RESOURCE_BUNDLE.getString(key);
    } catch (MissingResourceException e) {
      return '!' + key + '!';
    }
  }

  public String getString(String key, String... params) {
    try {
      return MessageFormat.format(getString(key), (Object[])params);
    } catch (Exception e) {
      return '!' + key + '!';
    }
  }
}
