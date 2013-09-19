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
* Copyright (c) 2002-2013 Pentaho Corporation..  All rights reserved.
*/

package org.pentaho.database.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DatabaseType implements Serializable, IDatabaseType {
  
  /**
     * @author wseyler
     *
     */
  private static final long serialVersionUID = 1955013893420806385L;

  private String name;
  private String shortName;
  private int defaultPort;
  private List<DatabaseAccessType> supportedAccessTypes;
  private String extraOptionsHelpUrl;
  
  public DatabaseType() {
    super();
  }
  
  public DatabaseType(String name, String shortName, List<DatabaseAccessType> supportedAccessTypes, int defaultPort, String extraOptionsHelpUrl) {
    this();
    this.name = name;
    this.shortName = shortName;
    this.defaultPort = defaultPort;
    this.supportedAccessTypes = supportedAccessTypes;
    this.extraOptionsHelpUrl = extraOptionsHelpUrl;
  }
  
  /* (non-Javadoc)
   * @see org.pentaho.database.model.IDatabaseType#getName()
   */
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  /* (non-Javadoc)
   * @see org.pentaho.database.model.IDatabaseType#getShortName()
   */
  public String getShortName() {
    return shortName;
  }
  
  public void setShortName(String shortName) {
    this.shortName = shortName;
  }
  
  /* (non-Javadoc)
   * @see org.pentaho.database.model.IDatabaseType#getSupportedAccessTypes()
   */
  public List<DatabaseAccessType> getSupportedAccessTypes() {
    return supportedAccessTypes;
  }
  
  public void setSupportedAccessTypes(List<DatabaseAccessType> supportedAccessTypes) {
    this.supportedAccessTypes = supportedAccessTypes;
  }
  
  /* (non-Javadoc)
   * @see org.pentaho.database.model.IDatabaseType#getDefaultDatabasePort()
   */
  public int getDefaultDatabasePort() {
    return defaultPort;
  }
  
  public void setDefaultDatabasePort(int defaultPort) {
    this.defaultPort = defaultPort;
  }
  
  /* (non-Javadoc)
   * @see org.pentaho.database.model.IDatabaseType#getExtraOptionsHelpUrl()
   */
  public String getExtraOptionsHelpUrl() {
    return extraOptionsHelpUrl;
  }
  
  public void setExtraOptionsHelpUrl(String extraOptionsHelpUrl) {
    this.extraOptionsHelpUrl = extraOptionsHelpUrl;
  }

  public boolean equals(Object obj) {
    DatabaseType dbtype = (DatabaseType)obj;
    return (getShortName().equals(dbtype.getShortName()));
  }
  
  public int hashCode() {
    return getShortName().hashCode();
  }

  @Override
  public String toString() {
    return "DatabaseType [name=" + name + ", shortName=" + shortName + ", defaultPort=" + defaultPort + ", supportedAccessTypes=" + supportedAccessTypes + ", extraOptionsHelpUrl=" + extraOptionsHelpUrl + "]";
  }
  
}
