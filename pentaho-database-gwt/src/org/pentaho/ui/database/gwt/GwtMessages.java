/*
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
 * Copyright 2008-2013 Pentaho Corporation.  All rights reserved.
 *
 */
package org.pentaho.ui.database.gwt;

import org.pentaho.gwt.widgets.client.utils.i18n.ResourceBundle;
import org.pentaho.ui.database.event.IMessages;

public class GwtMessages implements IMessages {
  
  ResourceBundle messageBundle;
  
  public GwtMessages() {
    
  }
  public GwtMessages(ResourceBundle messageBundle){
    this.messageBundle = messageBundle;
  }

  public String getString(String key) {
    if (this.messageBundle == null) {
      return key;
    }
    String val = this.messageBundle.getString(key);
    return val.replaceAll("\\:", ":");
  }

  public String getString(String key, String... parameters) {
    if (this.messageBundle == null) {
      return key;
    }
    String val = this.messageBundle.getString(key, key, parameters);
    return val.replaceAll("\\:", ":");
  }

  public ResourceBundle getMessageBundle() {
    return this.messageBundle;
  }
  public void setMessageBundle(ResourceBundle messageBundle) {
    this.messageBundle = messageBundle;
  }
}
