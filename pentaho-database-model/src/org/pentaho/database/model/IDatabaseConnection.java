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
import java.util.List;
import java.util.Map;

public interface IDatabaseConnection extends Serializable {

  void setId( String id );

  String getId();

  void setAccessType( DatabaseAccessType accessType );

  DatabaseAccessType getAccessType();

  void setDatabaseType( IDatabaseType driver );

  IDatabaseType getDatabaseType();

  void setExtraOptions( Map<String, String> extraOptions );

  Map<String, String> getExtraOptions();

  void setExtraOptionsOrder( Map<String, String> extraOptionsOrder );

  Map<String, String> getExtraOptionsOrder();

  void setName( String name );

  String getName();

  void setHostname( String hostname );

  String getHostname();

  void setDatabaseName( String databaseName );

  String getDatabaseName();

  void setDatabasePort( String databasePort );

  String getDatabasePort();

  void setUsername( String username );

  String getUsername();

  void setPassword( String password );

  String getPassword();

  void setStreamingResults( boolean streamingResults );

  boolean isStreamingResults();

  void setDataTablespace( String dataTablespace );

  String getDataTablespace();

  void setIndexTablespace( String indexTablespace );

  String getIndexTablespace();

  void setSQLServerInstance( String sqlServerInstance );

  String getSQLServerInstance();

  void setUsingDoubleDecimalAsSchemaTableSeparator( boolean usingDoubleDecimalAsSchemaTableSeparator );

  boolean isUsingDoubleDecimalAsSchemaTableSeparator();

  void setInformixServername( String informixServername );

  String getInformixServername();

  void addExtraOption( String databaseTypeCode, String option, String value );

  void setAttributes( Map<String, String> attributes );

  Map<String, String> getAttributes();

  void setChanged( boolean changed );

  boolean getChanged();

  void setQuoteAllFields( boolean quoteAllFields );

  boolean isQuoteAllFields();

  // advanced option (convert to enum with upper, lower, none?)
  void setForcingIdentifiersToLowerCase( boolean forcingIdentifiersToLowerCase );

  boolean isForcingIdentifiersToLowerCase();

  void setForcingIdentifiersToUpperCase( boolean forcingIdentifiersToUpperCase );

  boolean isForcingIdentifiersToUpperCase();

  void setConnectSql( String sql );

  String getConnectSql();

  void setUsingConnectionPool( boolean usingConnectionPool );

  boolean isUsingConnectionPool();

  void setInitialPoolSize( int initialPoolSize );

  int getInitialPoolSize();

  void setMaximumPoolSize( int maxPoolSize );

  int getMaximumPoolSize();

  void setPartitioned( boolean partitioned );

  boolean isPartitioned();

  Map<String, String> getConnectionPoolingProperties();

  void setConnectionPoolingProperties( Map<String, String> connectionPoolingProperties );

  void setPartitioningInformation( List<PartitionDatabaseMeta> partitioningInformation );

  List<PartitionDatabaseMeta> getPartitioningInformation();

}
