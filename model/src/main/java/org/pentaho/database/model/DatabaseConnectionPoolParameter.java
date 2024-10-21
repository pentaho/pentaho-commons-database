/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2029-07-20
 ******************************************************************************/


package org.pentaho.database.model;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlRootElement;

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
