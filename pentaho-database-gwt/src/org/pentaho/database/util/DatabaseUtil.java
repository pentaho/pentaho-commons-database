package org.pentaho.database.util;

import java.util.Properties;

import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.di.core.database.BaseDatabaseMeta;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.database.PartitionDatabaseMeta;

public class DatabaseUtil {

  public static DatabaseMeta convertToDatabaseMeta(IDatabaseConnection conn) {
    DatabaseMeta meta = new DatabaseMeta();

    meta.setDatabaseType(conn.getDatabaseType().getShortName());

    Properties props = meta.getDatabaseInterface().getAttributes();

    for (String key : conn.getExtraOptions().keySet()) {
      if (conn.getExtraOptions().get(key) != null) {
        props.put(BaseDatabaseMeta.ATTRIBUTE_PREFIX_EXTRA_OPTION + key, conn.getExtraOptions().get(key));
      }
    }

    for (String key : conn.getConnectionPoolingProperties().keySet()) {
      if (conn.getConnectionPoolingProperties().get(key) != null) {
        props.put(BaseDatabaseMeta.ATTRIBUTE_POOLING_PARAMETER_PREFIX + key,
            conn.getConnectionPoolingProperties().get(key));
      }
    }

    for (String key : conn.getAttributes().keySet()) {
      String val = conn.getAttributes().get(key);
      if (val != null) {
        props.put(key, conn.getAttributes().get(key));
      }
    }

    meta.setAttributes(props);

    meta.setName(conn.getName());
    meta.setAccessType(conn.getAccessType().ordinal());
    meta.setHostname(conn.getHostname());
    meta.setDBName(conn.getDatabaseName());
    if (conn.getDatabasePort() != null) {
      meta.setDBPort(conn.getDatabasePort());
    } else {
      meta.setDBPort("" + meta.getDefaultDatabasePort());
    }
    meta.setUsername(conn.getUsername());
    meta.setPassword(conn.getPassword());

    meta.setServername(conn.getInformixServername());
    meta.setDataTablespace(conn.getDataTablespace());
    meta.setIndexTablespace(conn.getIndexTablespace());

    // add
    if (conn.getConnectSql() != null) {
      meta.setConnectSQL(conn.getConnectSql());
    }

    meta.setInitialPoolSize(conn.getInitialPoolSize());
    meta.setMaximumPoolSize(conn.getMaximumPoolSize());

    if (conn.getSQLServerInstance() != null) {
      meta.setSQLServerInstance(conn.getSQLServerInstance());
    }
    meta.setForcingIdentifiersToLowerCase(conn.isForcingIdentifiersToLowerCase());
    meta.setForcingIdentifiersToUpperCase(conn.isForcingIdentifiersToUpperCase());
    meta.setPartitioned(conn.isPartitioned());
    meta.setQuoteAllFields(conn.isQuoteAllFields());
    meta.setStreamingResults(conn.isStreamingResults());
    meta.setUsingConnectionPool(conn.isUsingConnectionPool());
    meta.setUsingDoubleDecimalAsSchemaTableSeparator(conn.isUsingDoubleDecimalAsSchemaTableSeparator());

    if (conn.getPartitioningInformation() != null) {
      PartitionDatabaseMeta pdmetas[] = new PartitionDatabaseMeta[conn.getPartitioningInformation().size()];

      //TODO
      int c = 0;
      for (org.pentaho.database.model.PartitionDatabaseMeta pdmeta : conn.getPartitioningInformation()) {
        pdmetas[c++] = convertToPartitionDatabaseMeta(pdmeta);
      }
      meta.setPartitioningInformation(pdmetas);
    }

    if (conn.getChanged()) {
      meta.setChanged();
    }

    return meta;
  }

  public static PartitionDatabaseMeta convertToPartitionDatabaseMeta(
      org.pentaho.database.model.PartitionDatabaseMeta pdm) {
    PartitionDatabaseMeta pdmeta = new PartitionDatabaseMeta();
    pdmeta.setDatabaseName(pdm.getDatabaseName());
    pdmeta.setHostname(pdm.getHostname());
    pdmeta.setPartitionId(pdm.getPartitionId());
    pdmeta.setPassword(pdm.getPassword());
    pdmeta.setPort(pdm.getPort());
    pdmeta.setUsername(pdm.getUsername());
    return pdmeta;
  }
}
