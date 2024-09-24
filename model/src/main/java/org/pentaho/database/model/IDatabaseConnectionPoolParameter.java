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

package org.pentaho.database.model;

public interface IDatabaseConnectionPoolParameter {

  /**
   * @return the defaultValue
   */
  public String getDefaultValue();

  /**
   * @param defaultValue
   *          the defaultValue to set
   */
  public void setDefaultValue( String defaultValue );

  /**
   * @return the description
   */
  public String getDescription();

  /**
   * @param description
   *          the description to set
   */
  public void setDescription( String description );

  /**
   * @return the parameter
   */
  public String getParameter();

  /**
   * @param parameter
   *          the parameter to set
   */
  public void setParameter( String parameter );

}
