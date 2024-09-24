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

package org.pentaho.database.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DatabaseConnectionPoolParameter implements IDatabaseConnectionPoolParameter, Serializable {

  private static final long serialVersionUID = -1418014026922746690L;

  private String parameter;
  private String defaultValue;
  private String description;

  public DatabaseConnectionPoolParameter() {
  }

  /**
   * @param parameter
   * @param defaultValue
   * @param description
   */
  public DatabaseConnectionPoolParameter( String parameter, String defaultValue, String description ) {
    this();
    this.parameter = parameter;
    this.defaultValue = defaultValue;
    this.description = description;
  }

  /**
   * @return the defaultValue
   */
  public String getDefaultValue() {
    return defaultValue;
  }

  /**
   * @param defaultValue
   *          the defaultValue to set
   */
  public void setDefaultValue( String defaultValue ) {
    this.defaultValue = defaultValue;
  }

  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param description
   *          the description to set
   */
  public void setDescription( String description ) {
    this.description = description;
  }

  /**
   * @return the parameter
   */
  public String getParameter() {
    return parameter;
  }

  /**
   * @param parameter
   *          the parameter to set
   */
  public void setParameter( String parameter ) {
    this.parameter = parameter;
  }

  public static final String[] getParameterNames( DatabaseConnectionPoolParameter[] poolParameters ) {
    String[] names = new String[poolParameters.length];
    for ( int i = 0; i < names.length; i++ ) {
      names[i] = poolParameters[i].getParameter();
    }
    return names;
  }

  public static final DatabaseConnectionPoolParameter findParameter( String parameterName,
      DatabaseConnectionPoolParameter[] poolParameters ) {
    for ( int i = 0; i < poolParameters.length; i++ ) {
      if ( poolParameters[i].getParameter().equalsIgnoreCase( parameterName ) ) {
        return poolParameters[i];
      }
    }
    return null;
  }
}
