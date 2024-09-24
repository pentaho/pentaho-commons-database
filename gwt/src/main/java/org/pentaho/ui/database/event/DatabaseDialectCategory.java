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

package org.pentaho.ui.database.event;


import org.pentaho.database.DatabaseDialectException;
import org.pentaho.database.IDatabaseDialect;
import org.pentaho.database.IValueMeta;
import org.pentaho.database.model.IDatabaseConnection;

import com.google.web.bindery.autobean.shared.AutoBean;

/**
 * @author wseyler
 *
 */
public class DatabaseDialectCategory {
  public static boolean needsPlaceHolder(AutoBean<IDatabaseDialect> instance) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.needsPlaceHolder();
  }
  
  public static boolean needsToLockAllTables(AutoBean<IDatabaseDialect> instance) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.needsToLockAllTables();
  }

  public static boolean quoteReservedWords(AutoBean<IDatabaseDialect> instance) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.quoteReservedWords();
  }

  public static boolean supportsAutoInc(AutoBean<IDatabaseDialect> instance) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.supportsAutoInc();
  }

  public static boolean supportsBatchUpdates(AutoBean<IDatabaseDialect> instance) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.supportsBatchUpdates();
  }
  
  public static boolean supportsBitmapIndex(AutoBean<IDatabaseDialect> instance) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.supportsBitmapIndex();
  }

  public static boolean supportsBooleanDataType(AutoBean<IDatabaseDialect> instance) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.supportsBooleanDataType();
  }

  public static boolean supportsCatalogs(AutoBean<IDatabaseDialect> instance) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.supportsCatalogs();
  }

  public static boolean supportsEmptyTransactions(AutoBean<IDatabaseDialect> instance) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.supportsEmptyTransactions();
  }

  public static boolean supportsFloatRoundingOnUpdate(AutoBean<IDatabaseDialect> instance) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.supportsFloatRoundingOnUpdate();
  }

  public static boolean supportsGetBlob(AutoBean<IDatabaseDialect> instance) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.supportsGetBlob();
  }

  public static boolean supportsOptionsInURL(AutoBean<IDatabaseDialect> instance) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.supportsOptionsInURL();
  }

  public static boolean supportsRepository(AutoBean<IDatabaseDialect> instance) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.supportsRepository();
  }

  public static boolean supportsSchemas(AutoBean<IDatabaseDialect> instance) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.supportsSchemas();
  }
  
  public static boolean supportsSequences(AutoBean<IDatabaseDialect> instance) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.supportsSequences();
  }

  public static boolean supportsSetCharacterStream(AutoBean<IDatabaseDialect> instance) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.supportsSetCharacterStream();
  }

  public static boolean supportsSetLong(AutoBean<IDatabaseDialect> instance) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.supportsSetLong();
  }

  public static boolean supportsSetMaxRows(AutoBean<IDatabaseDialect> instance) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.supportsSetMaxRows();
  }

  public static boolean supportsSynonyms(AutoBean<IDatabaseDialect> instance) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.supportsSynonyms();
  }

  public static boolean supportsTimeStampToDateConversion(AutoBean<IDatabaseDialect> instance) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.supportsTimeStampToDateConversion();
  }

  public static boolean supportsTransactions(AutoBean<IDatabaseDialect> instance) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.supportsTransactions();
  }

  public static boolean supportsViews(AutoBean<IDatabaseDialect> instance) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.supportsViews();
  }
  
  public static boolean useSchemaNameForTableList(AutoBean<IDatabaseDialect> instance) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.useSchemaNameForTableList();
  }

  public static int getNotFoundTK(AutoBean<IDatabaseDialect> instance, boolean use_autoinc) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.getNotFoundTK(use_autoinc);
  }

  public static String getAddColumnStatement(AutoBean<IDatabaseDialect> instance, String tablename, IValueMeta v, String tk, boolean use_autoinc, String pk, boolean semicolon) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.getAddColumnStatement(tablename, v, tk, use_autoinc, pk, semicolon);
  }

  public static String getDriverClass(AutoBean<IDatabaseDialect> instance, IDatabaseConnection connection) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.getDriverClass(connection);
  }

  public static String getDropColumnStatement(AutoBean<IDatabaseDialect> instance, String tablename, IValueMeta v, String tk, boolean use_autoinc, String pk, boolean semicolon) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.getDropColumnStatement(tablename, v, tk, use_autoinc, pk, semicolon);
  }

  public static String getFieldDefinition(AutoBean<IDatabaseDialect> instance, IValueMeta v, String tk, String pk, boolean use_autoinc, boolean add_fieldname, boolean add_cr) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.getFieldDefinition(v, tk, pk, use_autoinc, add_fieldname, add_cr);
  }

  public static String getLimitClause(AutoBean<IDatabaseDialect> instance, int nrRows) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.getLimitClause(nrRows);
  }

  public static String getModifyColumnStatement(AutoBean<IDatabaseDialect> instance, String tablename, IValueMeta v, String tk, boolean use_autoinc, String pk, boolean semicolon) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.getModifyColumnStatement(tablename, v, tk, use_autoinc, pk, semicolon);
  }

  public static String getSQLColumnExists(AutoBean<IDatabaseDialect> instance, String columnname, String tablename) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.getSQLColumnExists(columnname, tablename);
  }

  public static String getSQLColumnExists(AutoBean<IDatabaseDialect> instance, String sequenceName) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.getSQLCurrentSequenceValue(sequenceName);
  }

  public static String getSQLCurrentSequenceValue(AutoBean<IDatabaseDialect> instance, String sequenceName) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.getSQLCurrentSequenceValue(sequenceName);
  }

  public static String getSQLListOfProcedures(AutoBean<IDatabaseDialect> instance, IDatabaseConnection connection) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.getSQLListOfProcedures(connection);
  }

  public static String getSQLLockTables(AutoBean<IDatabaseDialect> instance, String[] tableNames) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.getSQLLockTables(tableNames);
  }

  public static String getSQLNextSequenceValue(AutoBean<IDatabaseDialect> instance, String sequenceName) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.getSQLNextSequenceValue(sequenceName);
  }

  public static String getSQLQueryFields(AutoBean<IDatabaseDialect> instance, String tableName) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.getSQLQueryFields(tableName);
  }

  public static String getSQLSequenceExists(AutoBean<IDatabaseDialect> instance, String sequenceName) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.getSQLSequenceExists(sequenceName);
  }

  public static String getSQLTableExists(AutoBean<IDatabaseDialect> instance, String tableName) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.getSQLTableExists(tableName);
  }

  public static String getSQLUnlockTables(AutoBean<IDatabaseDialect> instance, String[] tableNames) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.getSQLUnlockTables(tableNames);
  }

  public static String getSchemaTableCombination(AutoBean<IDatabaseDialect> instance, String schema_name, java.lang.String table_part) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.getSchemaTableCombination(schema_name, table_part);
  }

  public static String getTruncateTableStatement(AutoBean<IDatabaseDialect> instance, String tableName) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.getTruncateTableStatement(tableName);
  }

  public static String getURL(AutoBean<IDatabaseDialect> instance, IDatabaseConnection connection) throws DatabaseDialectException {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.getURL(connection);
  }

  public static String getURLWithExtraOptions(AutoBean<IDatabaseDialect> instance, IDatabaseConnection connection) throws DatabaseDialectException {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.getURLWithExtraOptions(connection);
  }

  public static IDatabaseConnection createNativeConnection(AutoBean<IDatabaseDialect> instance, String jdbcUrl) {
    IDatabaseDialect databaseDialect = instance.as();
    return databaseDialect.createNativeConnection(jdbcUrl);
  }
}
