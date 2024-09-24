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

package org.pentaho.database.util;

import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.di.core.database.BaseDatabaseMeta;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.database.PartitionDatabaseMeta;

import java.util.Properties;

public class DatabaseUtil {

  public static DatabaseMeta convertToDatabaseMeta( IDatabaseConnection conn ) {
    DatabaseMeta meta = new DatabaseMeta();

    meta.setDatabaseType( conn.getDatabaseType().getShortName() );

    Properties props = meta.getDatabaseInterface().getAttributes();

    for ( String key : conn.getAttributes().keySet() ) {
      String val = conn.getAttributes().get( key );
      if ( val != null ) {
        props.put( key, val );
      }
    }

    for ( String key : conn.getExtraOptions().keySet() ) {
      if ( conn.getExtraOptions().get( key ) != null ) {
        props.put( BaseDatabaseMeta.ATTRIBUTE_PREFIX_EXTRA_OPTION + key, conn.getExtraOptions().get( key ) );
      }
    }

    for ( String key : conn.getConnectionPoolingProperties().keySet() ) {
      if ( conn.getConnectionPoolingProperties().get( key ) != null ) {
        props.put( BaseDatabaseMeta.ATTRIBUTE_POOLING_PARAMETER_PREFIX + key, conn.getConnectionPoolingProperties()
            .get( key ) );
      }
    }

    meta.setAttributes( props );

    meta.setName( conn.getName() );
    meta.setAccessType( conn.getAccessType().ordinal() );
    meta.setHostname( conn.getHostname() );
    meta.setDBName( conn.getDatabaseName() );
    if ( conn.getDatabasePort() != null ) {
      meta.setDBPort( conn.getDatabasePort() );
    } else {
      meta.setDBPort( "" + meta.getDefaultDatabasePort() );
    }
    meta.setUsername( conn.getUsername() );
    meta.setPassword( conn.getPassword() );

    meta.setServername( conn.getInformixServername() );
    meta.setDataTablespace( conn.getDataTablespace() );
    meta.setIndexTablespace( conn.getIndexTablespace() );

    // add
    if ( conn.getConnectSql() != null ) {
      meta.setConnectSQL( conn.getConnectSql() );
    }

    meta.setInitialPoolSize( conn.getInitialPoolSize() );
    meta.setMaximumPoolSize( conn.getMaximumPoolSize() );

    meta.setForcingIdentifiersToLowerCase( conn.isForcingIdentifiersToLowerCase() );
    meta.setForcingIdentifiersToUpperCase( conn.isForcingIdentifiersToUpperCase() );
    meta.setPartitioned( conn.isPartitioned() );
    meta.setQuoteAllFields( conn.isQuoteAllFields() );
    meta.setStreamingResults( conn.isStreamingResults() );
    meta.setUsingConnectionPool( conn.isUsingConnectionPool() );
    meta.setUsingDoubleDecimalAsSchemaTableSeparator( conn.isUsingDoubleDecimalAsSchemaTableSeparator() );

    if ( conn.getPartitioningInformation() != null ) {
      PartitionDatabaseMeta[] pdmetas = new PartitionDatabaseMeta[conn.getPartitioningInformation().size()];

      // TODO
      int c = 0;
      for ( org.pentaho.database.model.PartitionDatabaseMeta pdmeta : conn.getPartitioningInformation() ) {
        pdmetas[c++] = convertToPartitionDatabaseMeta( pdmeta );
      }
      meta.setPartitioningInformation( pdmetas );
    }

    if ( conn.getChanged() ) {
      meta.setChanged();
    }

    return meta;
  }

  public static PartitionDatabaseMeta convertToPartitionDatabaseMeta(
      org.pentaho.database.model.PartitionDatabaseMeta pdm ) {
    PartitionDatabaseMeta pdmeta = new PartitionDatabaseMeta();
    pdmeta.setDatabaseName( pdm.getDatabaseName() );
    pdmeta.setHostname( pdm.getHostname() );
    pdmeta.setPartitionId( pdm.getPartitionId() );
    pdmeta.setPassword( pdm.getPassword() );
    pdmeta.setPort( pdm.getPort() );
    pdmeta.setUsername( pdm.getUsername() );
    return pdmeta;
  }
}
