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

import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author wseyler
 */
@XmlRootElement
public class DatabaseType implements Serializable, IDatabaseType {

  private static final long serialVersionUID = 1955013893420806385L;

  private String name;
  private String shortName;
  private int defaultPort;
  private List<DatabaseAccessType> supportedAccessTypes;
  private String extraOptionsHelpUrl;
  private String defaultDatabaseName;
  private Map<String, String> defaultOptions;

  public DatabaseType() {
    super();
  }

  public DatabaseType( String name, String shortName, List<DatabaseAccessType> supportedAccessTypes, int defaultPort,
      String extraOptionsHelpUrl ) {
    this();
    this.name = name;
    this.shortName = shortName;
    this.defaultPort = defaultPort;
    this.supportedAccessTypes = supportedAccessTypes;
    this.extraOptionsHelpUrl = extraOptionsHelpUrl;
  }

  public DatabaseType( String name, String shortName,
                       List<DatabaseAccessType> supportedAccessTypes,
                       int defaultPort,
                       String extraOptionsHelpUrl,
                       String defaultDatabaseName,
                       Map<String, String> defaultOptions ) {
    this( name, shortName, supportedAccessTypes, defaultPort, extraOptionsHelpUrl );
    this.defaultDatabaseName = defaultDatabaseName;
    this.defaultOptions = defaultOptions;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.model.IDatabaseType#getName()
   */
  public String getName() {
    return name;
  }

  public void setName( String name ) {
    this.name = name;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.model.IDatabaseType#getShortName()
   */
  public String getShortName() {
    return shortName;
  }

  public void setShortName( String shortName ) {
    this.shortName = shortName;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.model.IDatabaseType#getSupportedAccessTypes()
   */
  public List<DatabaseAccessType> getSupportedAccessTypes() {
    return supportedAccessTypes;
  }

  public void setSupportedAccessTypes( List<DatabaseAccessType> supportedAccessTypes ) {
    this.supportedAccessTypes = supportedAccessTypes;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.model.IDatabaseType#getDefaultDatabasePort()
   */
  public int getDefaultDatabasePort() {
    return defaultPort;
  }

  public void setDefaultDatabasePort( int defaultPort ) {
    this.defaultPort = defaultPort;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.model.IDatabaseType#getExtraOptionsHelpUrl()
   */
  public String getExtraOptionsHelpUrl() {
    return extraOptionsHelpUrl;
  }

  public void setExtraOptionsHelpUrl( String extraOptionsHelpUrl ) {
    this.extraOptionsHelpUrl = extraOptionsHelpUrl;
  }

  public String getDefaultDatabaseName() {
    return defaultDatabaseName;
  }

  public void setDefaultDatabaseName( String defaultDBName ) {
    defaultDatabaseName = defaultDBName;
  }

  public Map<String, String> getDefaultOptions() {
    return defaultOptions;
  }

  public void setDefaultOptions( Map<String, String> defOptions ) {
    defaultOptions = defOptions;
  }

  public boolean equals( Object obj ) {
    if ( !( obj instanceof DatabaseType ) ) {
      return false;
    }
    DatabaseType dbtype = (DatabaseType) obj;
    return ( getShortName().equals( dbtype.getShortName() ) );
  }

  public int hashCode() {
    return getShortName().hashCode();
  }

  @Override
  public String toString() {
    return "DatabaseType [name=" + name + ", shortName=" + shortName + ", defaultPort=" + defaultPort
        + ", supportedAccessTypes=" + supportedAccessTypes + ", extraOptionsHelpUrl=" + extraOptionsHelpUrl
        + ", defaultDatabaseName=" + defaultDatabaseName
        + ", defaultOptions=" + defaultOptions + "]";
  }
}
