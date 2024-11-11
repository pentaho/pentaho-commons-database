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


package org.pentaho.database.dialect;

import org.pentaho.database.DatabaseDialectException;
import org.pentaho.database.IValueMeta;
import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.DatabaseType;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.model.IDatabaseType;

public class GoogleBigQueryDatabaseDialect extends AbstractDatabaseDialect {

  private static final long serialVersionUID = -3736345602216241687L;

  private static final IDatabaseType DBTYPE = new DatabaseType( "Google BigQuery", "GOOGLEBIGQUERY", DatabaseAccessType.getList(
      DatabaseAccessType.NATIVE, DatabaseAccessType.JNDI ), 443,
      "https://cloud.google.com/bigquery/partners/simba-drivers/" );

  public GoogleBigQueryDatabaseDialect() {
  }

  public IDatabaseType getDatabaseType() {
    return DBTYPE;
  }

  public String getNativeDriver() {
    return "com.simba.googlebigquery.jdbc42.Driver";
  }

  public String getNativeJdbcPre() {
    return "jdbc:bigquery://";
  }

  @Override
  public String[] getUsedLibraries() {
    return new String[] {
      "google-api-client-1.22.0.jar", "google-api-services-bigquery-v2-rev355-1.22.0.jar",
      "google-http-client-1.22.0.jar", "google-http-client-jackson2-1.22.0.jar",
      "google-oauth-client-1.22.0.jar", "GoogleBigQueryJDBC42.jar", "jackson-core-2.14.2.jar"};
  }

  @Override public String getLimitClause( int nrRows ) {
    return " LIMIT " + nrRows;
  }

  @Override
  public String getSQLQueryFields( String tableName ) {
    return "SELECT * FROM " + tableName + " LIMIT 0"; //$NON-NLS-1$ //$NON-NLS-2$
  }

  @Override
  public String getSQLTableExists( String tablename ) {
    return getSQLQueryFields( tablename );
  }

  @Override
  public String getSQLColumnExists( String columnname, String tablename ) {
    return getSQLQueryColumnFields( columnname, tablename );
  }

  public String getSQLQueryColumnFields( String columnname, String tableName ) {
    return "SELECT " + columnname + " FROM " + tableName + " LIMIT 0"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  }

  @Override
  public String getURL( IDatabaseConnection connection ) throws DatabaseDialectException {
    return "jdbc:bigquery://" + connection.getHostname() + ":"
      + ( isEmpty( connection.getDatabasePort() ) ? "443" : connection.getDatabasePort() ) + ";"
      + ( isEmpty( connection.getDatabaseName() ) ? "" : "ProjectId=" + connection.getDatabaseName() ) + ";";
  }

  @Override public String getAddColumnStatement(
    String tablename, IValueMeta v, String tk, boolean use_autoinc,
    String pk, boolean semicolon ) {
    // BigQuery does not support DDL through JDBC.
    // https://cloud.google.com/bigquery/partners/simba-drivers/#do_the_drivers_provide_the_ability_to_manage_tables_create_table
    return null;
  }

  @Override public String getModifyColumnStatement(
    String tablename, IValueMeta v, String tk, boolean use_autoinc,
    String pk, boolean semicolon ) {
    // BigQuery does not support DDL through JDBC.
    // https://cloud.google.com/bigquery/partners/simba-drivers/#do_the_drivers_provide_the_ability_to_manage_tables_create_table
    return null;
  }

  @Override
  public String getFieldDefinition(
    IValueMeta v, String tk, String pk, boolean use_autoinc,
    boolean add_fieldname, boolean add_cr ) {
    String retval = "";

    String fieldname = v.getName();
    int precision = v.getPrecision();

    if ( add_fieldname ) {
      retval += fieldname + " ";
    }

    int type = v.getType();
    switch ( type ) {
      case IValueMeta.TYPE_DATE:
        retval += "DATE";
        break;

      case IValueMeta.TYPE_BOOLEAN:
        retval += "BOOL";
        break;

      case IValueMeta.TYPE_NUMBER:
      case IValueMeta.TYPE_INTEGER:
      case IValueMeta.TYPE_BIGNUMBER:
        if ( precision == 0 ) {
          retval += "INT64";
        } else {
          retval += "FLOAT64";
        }
        if ( fieldname.equalsIgnoreCase( tk )
          || fieldname.equalsIgnoreCase( pk ) ) {
          retval += " NOT NULL";
        }
        break;

      case IValueMeta.TYPE_STRING:
        retval += "STRING";
        break;

      case IValueMeta.TYPE_BINARY:
        retval += "BYTES";
        break;

      default:
        retval += " UNKNOWN";
        break;
    }

    if ( add_cr ) {
      retval += CR;
    }

    return retval;
  }

  @Override public boolean supportsAutoInc() {
    return false;
  }

  @Override public boolean supportsTimeStampToDateConversion() {
    return false;
  }

  @Override public boolean supportsBooleanDataType() {
    return true;
  }

  @Override public boolean supportsBitmapIndex() {
    return false;
  }

  @Override public boolean supportsViews() {
    return false;
  }

  @Override public boolean supportsSynonyms() {
    return false;
  }

  @Override public boolean supportsTransactions() {
    return false;
  }

  @Override public boolean isRequiringTransactionsOnQueries() {
    return false;
  }

  @Override public String getExtraOptionSeparator() {
    return ";";
  }

  @Override public String getExtraOptionIndicator() {
    return "";
  }

  @Override public String getStartQuote() {
    return "`";
  }

  @Override public String getEndQuote() {
    return "`";
  }

  @Override public String[] getReservedWords() {
    return new String[] { "ALL", "AND", "ANY", "ARRAY", "AS", "ASC", "ASSERT_ROWS_MODIFIED", "AT", "BETWEEN",
      "COLLATE", "CONTAINS", "CREATE", "CROSS", "CUBE", "CURRENT", "DEFAULT", "DEFINE", "DESC", "DISTINCT",
      "ELSE", "END", "ENUM", "ESCAPE", "EXCEPT", "EXCLUDE", "EXISTS", "EXTRACT", "FALSE", "FETCH", "FOLLOWING",
      "FOR", "FROM", "FULL", "GROUP", "GROUPING", "GROUPS", "HASH", "HAVING", "IF", "IGNORE", "IN", "INNER",
      "INTERSECT", "INTERVAL", "INTO", "IS", "JOIN", "LATERAL", "LEFT", "LIKE", "LIMIT", "LOOKUP", "MERGE",
      "NATURAL", "NEW", "NO", "NOT", "NULL", "NULLS", "OF", "ON", "OR", "ORDER", "OUTER", "OVER", "PARTITION",
      "PRECEDING", "PROTO", "RANGE", "RECURSIVE", "RESPECT", "RIGHT", "ROLLUP", "ROWS", "SELECT", "SET", "SOME",
      "STRUCT", "TABLESAMPLE", "THEN", "TO", "TREAT", "TRUE", "UNBOUNDED", "UNION", "UNNEST", "USING", "WHEN",
      "WHERE", "WINDOW", "WITH", "WITHIN", "BY", "CASE", "CAST" };
  }
}
