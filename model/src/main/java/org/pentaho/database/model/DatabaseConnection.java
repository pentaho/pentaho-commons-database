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
 * Copyright (c) 2002-2023 Hitachi Vantara..  All rights reserved.
 */

package org.pentaho.database.model;

import com.google.gwt.core.shared.GwtIncompatible;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.pentaho.database.util.Const;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DatabaseConnection implements Serializable, IDatabaseConnection {

  private static final long serialVersionUID = -3816140282186728714L;

  public static final String EMPTY_OPTIONS_STRING = "><EMPTY><"; //$NON-NLS-1$

  // part of the generic database connection, move somewhere else
  public static final String ATTRIBUTE_CUSTOM_URL = "CUSTOM_URL"; //$NON-NLS-1$

  public static final String ATTRIBUTE_CUSTOM_DRIVER_CLASS = "CUSTOM_DRIVER_CLASS"; //$NON-NLS-1$

  public static final String ATTRIBUTE_PREFIX_EXTRA_OPTION = "EXTRA_OPTION_"; //$NON-NLS-1$

  String id;

  String name;

  String databaseName;

  String databasePort;

  String hostname;

  String username;

  String password;

  String dataTablespace;

  String indexTablespace;

  boolean streamingResults;

  boolean quoteAllFields;

  // should this be here?
  boolean changed;

  // dialect specific fields?
  boolean usingDoubleDecimalAsSchemaTableSeparator;

  // Informix server name
  String informixServername;

  boolean forcingIdentifiersToLowerCase;

  boolean forcingIdentifiersToUpperCase;

  String connectSql;

  boolean usingConnectionPool;

  String accessTypeValue = null;

  DatabaseAccessType accessType = null;

  // @XmlElement(type=DatabaseType.class)
  IDatabaseType databaseType = null;

  Map<String, String> extraOptions = new TreeMap<>();

  Map<String, String> extraOptionsOrder = new TreeMap<>();

  Map<String, String> attributes = new TreeMap<>();

  Map<String, String> connectionPoolingProperties = new TreeMap<>();

  List<PartitionDatabaseMeta> partitioningInformation;

  int initialPoolSize;

  int maxPoolSize;

  boolean partitioned;

  public DatabaseConnection() {
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#setAccessType(org.pentaho.database.model.DatabaseAccessType)
   */
  public void setAccessType( DatabaseAccessType accessType ) {
    this.accessType = accessType;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#getAccessType()
   */
  public DatabaseAccessType getAccessType() {
    return accessType;
  }

  /**
   * This method is used to set the access type value. This is only used during marshalling that does not support enums
   * (like Apache Axis)
   *
   * @param value
   */
  public void setAccessTypeValue( String value ) {
    accessTypeValue = value;
  }

  /**
   * This method is used to set the access type value. This is only used during marshalling that does not support enums
   * (like Apache Axis)
   */
  public String getAccessTypeValue() {
    return accessType == null ? accessTypeValue : accessType.toString();
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#setDatabaseDriver(org.pentaho.database.model.DatabaseType)
   */
  @XmlElement( type = DatabaseType.class )
  public void setDatabaseType( IDatabaseType driver ) {
    this.databaseType = driver;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#getDatabaseType()
   */
  public IDatabaseType getDatabaseType() {
    return databaseType;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#setExtraOptions()
   */
  @Override
  public void setExtraOptions( Map<String, String> extraOptions ) {
    this.extraOptions = extraOptions;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#getExtraOptions()
   */
  public Map<String, String> getExtraOptions() {
    return extraOptions;
  }

  @Override
  public void setExtraOptionsOrder( Map<String, String> extraOptionsOrder ) {
    this.extraOptionsOrder = extraOptionsOrder;
  }

  @Override
  public Map<String, String> getExtraOptionsOrder() {
    return this.extraOptionsOrder;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#setName(java.lang.String)
   */
  public void setName( String name ) {
    this.name = name;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#getName()
   */
  public String getName() {
    return name;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#setHostname(java.lang.String)
   */
  public void setHostname( String hostname ) {
    this.hostname = hostname;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#getHostname()
   */
  public String getHostname() {
    return hostname;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#setDatabaseName(java.lang.String)
   */
  public void setDatabaseName( String databaseName ) {
    this.databaseName = databaseName;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#getDatabaseName()
   */
  public String getDatabaseName() {
    if ( getDatabaseType() != null
      && ( databaseName == null || databaseName.trim().length() == 0 ) && !"KettleThin"
      .equals( getDatabaseType().getShortName() ) ) {
      return getDatabaseType().getDefaultDatabaseName();
    } else {
      return databaseName;
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#setDatabasePort(java.lang.String)
   */
  public void setDatabasePort( String databasePort ) {
    this.databasePort = databasePort;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#getDatabasePort()
   */
  public String getDatabasePort() {
    return databasePort;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#setUsername(java.lang.String)
   */
  public void setUsername( String username ) {
    this.username = username;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#getUsername()
   */
  public String getUsername() {
    return username;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#setPassword(java.lang.String)
   */
  public void setPassword( String password ) {
    this.password = password;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#getPassword()
   */
  public String getPassword() {
    return password;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#setStreamingResults(boolean)
   */
  public void setStreamingResults( boolean streamingResults ) {
    this.streamingResults = streamingResults;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#isStreamingResults()
   */
  public boolean isStreamingResults() {
    return streamingResults;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#setDataTablespace(java.lang.String)
   */
  public void setDataTablespace( String dataTablespace ) {
    this.dataTablespace = dataTablespace;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#getDataTablespace()
   */
  public String getDataTablespace() {
    return dataTablespace;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#setIndexTablespace(java.lang.String)
   */
  public void setIndexTablespace( String indexTablespace ) {
    this.indexTablespace = indexTablespace;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#getIndexTablespace()
   */
  public String getIndexTablespace() {
    return indexTablespace;
  }

  // can we move these out into some other list like advanced features?

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#setSQLServerInstance(java.lang.String)
   */
  public void setSQLServerInstance( String sqlServerInstance ) {
    addExtraOption( "MSSQL", "instance", sqlServerInstance ); //$NON-NLS-1$ //$NON-NLS-2$
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#getSQLServerInstance()
   */
  public String getSQLServerInstance() {
    return getExtraOptions().get( "MSSQL.instance" ); //$NON-NLS-1$
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#setUsingDoubleDecimalAsSchemaTableSeparator(boolean)
   */
  public void setUsingDoubleDecimalAsSchemaTableSeparator( boolean usingDoubleDecimalAsSchemaTableSeparator ) {
    this.usingDoubleDecimalAsSchemaTableSeparator = usingDoubleDecimalAsSchemaTableSeparator;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#isUsingDoubleDecimalAsSchemaTableSeparator()
   */
  public boolean isUsingDoubleDecimalAsSchemaTableSeparator() {
    return usingDoubleDecimalAsSchemaTableSeparator;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#setInformixServername(java.lang.String)
   */
  public void setInformixServername( String informixServername ) {
    this.informixServername = informixServername;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#getInformixServername()
   */
  public String getInformixServername() {
    return informixServername;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#addExtraOption(java.lang.String, java.lang.String,
   * java.lang.String)
   */
  public void addExtraOption( String databaseTypeCode, String option, String value ) {
    extraOptions.put( databaseTypeCode + "." + option, value ); //$NON-NLS-1$
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#getAttributes()
   */
  public Map<String, String> getAttributes() {
    return attributes;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#setAttributes()
   */
  @Override
  public void setAttributes( Map<String, String> attributes ) {
    this.attributes = attributes;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#setChanged(boolean)
   */
  public void setChanged( boolean changed ) {
    this.changed = changed;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#getChanged()
   */
  public boolean getChanged() {
    return changed;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#setQuoteAllFields(boolean)
   */
  public void setQuoteAllFields( boolean quoteAllFields ) {
    this.quoteAllFields = quoteAllFields;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#isQuoteAllFields()
   */
  public boolean isQuoteAllFields() {
    return quoteAllFields;
  }

  // advanced option (convert to enum with upper, lower, none?)
  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#setForcingIdentifiersToLowerCase(boolean)
   */
  public void setForcingIdentifiersToLowerCase( boolean forcingIdentifiersToLowerCase ) {
    this.forcingIdentifiersToLowerCase = forcingIdentifiersToLowerCase;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#isForcingIdentifiersToLowerCase()
   */
  public boolean isForcingIdentifiersToLowerCase() {
    return forcingIdentifiersToLowerCase;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#setForcingIdentifiersToUpperCase(boolean)
   */
  public void setForcingIdentifiersToUpperCase( boolean forcingIdentifiersToUpperCase ) {
    this.forcingIdentifiersToUpperCase = forcingIdentifiersToUpperCase;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#isForcingIdentifiersToUpperCase()
   */
  public boolean isForcingIdentifiersToUpperCase() {
    return forcingIdentifiersToUpperCase;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#setConnectSql(java.lang.String)
   */
  public void setConnectSql( String connectSql ) {
    this.connectSql = connectSql;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#getConnectSql()
   */
  public String getConnectSql() {
    return connectSql;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#setUsingConnectionPool(boolean)
   */
  public void setUsingConnectionPool( boolean usingConnectionPool ) {
    this.usingConnectionPool = usingConnectionPool;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#isUsingConnectionPool()
   */
  public boolean isUsingConnectionPool() {
    return usingConnectionPool;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#setInitialPoolSize(int)
   */
  public void setInitialPoolSize( int initialPoolSize ) {
    this.initialPoolSize = initialPoolSize;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#getInitialPoolSize()
   */
  public int getInitialPoolSize() {
    return initialPoolSize;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#setMaximumPoolSize(int)
   */
  public void setMaximumPoolSize( int maxPoolSize ) {
    this.maxPoolSize = maxPoolSize;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#getMaximumPoolSize()
   */
  public int getMaximumPoolSize() {
    return maxPoolSize;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#setPartitioned(boolean)
   */
  public void setPartitioned( boolean partitioned ) {
    this.partitioned = partitioned;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#isPartitioned()
   */
  public boolean isPartitioned() {
    return partitioned;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#getConnectionPoolingProperties()
   */
  public Map<String, String> getConnectionPoolingProperties() {
    return connectionPoolingProperties;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#setConnectionPoolingProperties(java.util.Map)
   */
  public void setConnectionPoolingProperties( Map<String, String> connectionPoolingProperties ) {
    this.connectionPoolingProperties = connectionPoolingProperties;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#setPartitioningInformation(java.util.List)
   */
  public void setPartitioningInformation( List<PartitionDatabaseMeta> partitioningInformation ) {
    this.partitioningInformation = partitioningInformation;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.pentaho.database.model.IDatabaseConnection#getPartitioningInformation()
   */
  public List<PartitionDatabaseMeta> getPartitioningInformation() {
    return this.partitioningInformation;
  }

  @GwtIncompatible( "Not required for GWT" )
  @Override
  public String calculateHash() {

    String version = System.getProperty( "java.version" );
    if ( version.startsWith( "1.8" ) ) {  // Can't use Sha3-256 with java 8
      return DigestUtils.sha256Hex( encodeProperties() );
    }

    return DigestUtils.sha3_256Hex( encodeProperties() );
  }

  private String encodeProperties() {
    StringBuilder sb = new StringBuilder();
    sb.append( encodeAttribute( "name=", name ) );
    sb.append( encodeAttribute( "databaseName=", databaseName ) );
    sb.append( encodeAttribute( "databasePort=", databasePort ) );
    sb.append( encodeAttribute( "hostname=", hostname ) );
    sb.append( encodeAttribute( "username=", username ) );
    sb.append( encodeAttribute( "password", password ) );
    sb.append( encodeAttribute( "dataTablespace=", dataTablespace ) );
    sb.append( encodeAttribute( "indexTablespace=", indexTablespace ) );
    sb.append( encodeAttribute( "streamingResults=", String.valueOf( streamingResults ) ) );
    sb.append( encodeAttribute( "quoteAllFields=", String.valueOf( quoteAllFields ) ) );
    sb.append( encodeAttribute( "changed=", String.valueOf( changed ) ) );
    sb.append( encodeAttribute( "usingDoubleDecimalAsSchemaTableSeparator=", String.valueOf( usingDoubleDecimalAsSchemaTableSeparator ) ) );
    sb.append( encodeAttribute( "informixServername=", informixServername ) );
    sb.append( encodeAttribute( "forcingIdentifiersToLowerCase=", String.valueOf( forcingIdentifiersToLowerCase ) ) );
    sb.append( encodeAttribute( "forcingIdentifiersToUpperCase=", String.valueOf( forcingIdentifiersToUpperCase ) ) );
    sb.append( encodeAttribute( "connectSql=", connectSql ) );
    sb.append( encodeAttribute( "usingConnectionPool=", String.valueOf( usingConnectionPool ) ) );
    sb.append( encodeAttribute( "accessTypeValue=", accessTypeValue ) );
    sb.append( encodeAttribute( "accessType=", String.valueOf( accessType ) ) );
    sb.append( encodeAttribute( "databaseType=", String.valueOf( databaseType ) ) );
    sb.append( encodeAttribute( "partitioningInformation=", String.valueOf( partitioningInformation ) ) );
    sb.append( encodeAttribute( "initialPoolSize=", String.valueOf( initialPoolSize ) ) );
    sb.append( encodeAttribute( "maxPoolSize=", String.valueOf( maxPoolSize ) ) );
    sb.append( encodeAttribute( "partitioned=", String.valueOf( partitioned ) ) );
    sb.append( encodeAttribute( "extraOptions=", extraOptions ) );
    sb.append( encodeAttribute( "extraOptionsOrder", extraOptionsOrder ) );
    sb.append( encodeAttribute( "attributes=", attributes ) );
    sb.append( encodeAttribute( "connectionPoolingProperties=", connectionPoolingProperties ) );

    return sb.toString();
  }

  private String encodeAttribute( String attName, String val ) {
    return attName + "=" + ( val == null || val.isEmpty() ? "" : val ) + ",";
  }

  private String encodeAttribute( String attName, Map<String, String> attributeMap ) {
    StringBuilder sb = new StringBuilder();
    sb.append( attName ).append( "=[" );
    for (Map.Entry<String, String> entry : attributeMap.entrySet()) {
      if ( entry.getValue() != null && !entry.getValue().isEmpty() ) {
        sb.append( encodeAttribute( entry.getKey(), entry.getValue() ) );
      }
    }
    sb.append( "]" );

    return sb.toString();
  }

  @Override
  public void setId( String id ) {
    this.id = id;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String toString() {
    return "DatabaseConnection [id=" + id + ", name=" + name + ", databaseName=" + databaseName + ", databasePort="
      + databasePort + ", hostname=" + hostname + ", username=" + username + ", password=*****"
      + ", dataTablespace=" + dataTablespace + ", indexTablespace=" + indexTablespace + ", streamingResults="
      + streamingResults + ", quoteAllFields=" + quoteAllFields + ", changed=" + changed
      + ", usingDoubleDecimalAsSchemaTableSeparator=" + usingDoubleDecimalAsSchemaTableSeparator
      + ", informixServername=" + informixServername + ", forcingIdentifiersToLowerCase="
      + forcingIdentifiersToLowerCase + ", forcingIdentifiersToUpperCase=" + forcingIdentifiersToUpperCase
      + ", connectSql=" + connectSql + ", usingConnectionPool=" + usingConnectionPool + ", accessTypeValue="
      + accessTypeValue + ", accessType=" + accessType + ", databaseType=" + databaseType + ", extraOptions="
      + extraOptions + ", attributes=" + attributes + ", connectionPoolingProperties=" + connectionPoolingProperties
      + ", partitioningInformation=" + partitioningInformation + ", initialPoolSize=" + initialPoolSize
      + ", maxPoolSize=" + maxPoolSize + ", partitioned=" + partitioned + "]";
  }

}
