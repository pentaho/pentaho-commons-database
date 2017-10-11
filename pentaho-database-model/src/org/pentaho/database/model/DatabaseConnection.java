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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

  Map<String, String> extraOptions = new HashMap<String, String>();

  Map<String, String> extraOptionsOrder = new HashMap<String, String>(  );

  Map<String, String> attributes = new HashMap<String, String>();

  Map<String, String> connectionPoolingProperties = new HashMap<String, String>();

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
