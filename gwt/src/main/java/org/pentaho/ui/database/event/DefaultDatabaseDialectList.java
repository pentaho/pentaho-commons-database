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

package org.pentaho.ui.database.event;

import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.pentaho.database.IDatabaseDialect;

/**
 * @author wseyler
 *
 */
@XmlRootElement
public class DefaultDatabaseDialectList implements IDatabaseDialectList {
 
  private List<IDatabaseDialect> dialects;
  
  public DefaultDatabaseDialectList() {
    super();
  }
  
  /* (non-Javadoc)
   * @see org.pentaho.ui.database.event.IDatabaseDialectList#getDialects()
   */
  @Override
  @XmlAnyElement
  public List<IDatabaseDialect> getDialects() {
    return dialects;
  }

  /* (non-Javadoc)
   * @see org.pentaho.ui.database.event.IDatabaseDialectList#setDialects(java.util.List)
   */
  @Override
  public void setDialects(List<IDatabaseDialect> dialects) {
    this.dialects = dialects;
  }

}
