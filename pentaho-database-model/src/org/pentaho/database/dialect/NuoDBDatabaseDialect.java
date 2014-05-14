/*
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
 * Copyright 2008-2013 Pentaho Corporation.  All rights reserved.
 *
 */
package org.pentaho.database.dialect;

import org.pentaho.database.DatabaseDialectException;
import org.pentaho.database.IValueMeta;
import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.DatabaseType;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.model.IDatabaseType;

public class NuoDBDatabaseDialect extends AbstractDatabaseDialect {

  private static final int DEFAULT_PORT = 48004;

  private static final IDatabaseType DBTYPE = new DatabaseType("NuoDB", "NUODB", DatabaseAccessType.getList(
      DatabaseAccessType.NATIVE, DatabaseAccessType.ODBC, DatabaseAccessType.JNDI), DEFAULT_PORT,
      "http://doc.nuodb.com/display/doc/JDBC+Connection+Properties");

  public NuoDBDatabaseDialect() {
    super();
  }

  @Override
  public String getSQLSequenceExists(String sequenceName) {
    return "SELECT * FROM SYSTEM.SEQUENCES WHERE SEQUENCENAME = '" + sequenceName.toUpperCase() + "'";
  }

  @Override
  public String getExtraOptionsHelpText() {
    return "http://doc.nuodb.com/display/doc/JDBC+Connection+Properties";
  }

  @Override
  public int getDefaultDatabasePort() {
    return DEFAULT_PORT;
  }

  @Override
  public String getSQLColumnExists(String columnname, String tablename) {
    return "SELECT " + columnname + " FROM " + tablename + " LIMIT 0";
  }

  @Override
  public boolean supportsSetCharacterStream() {
    return false;
  }

  @Override
  public int getNotFoundTK(boolean use_autoinc) {
    if (supportsAutoInc() && use_autoinc) {
      return 1;
    }
    return super.getNotFoundTK(use_autoinc);
  }

  @Override
  public boolean supportsCatalogs() {
    return false;
  }

  @Override
  public String getSQLQueryFields(String tableName) {
    return "SELECT * FROM " + tableName + " LIMIT 0";
  }

  @Override
  public boolean supportsSequences() {
    return true;
  }

  @Override
  public String getLimitClause(int nrRows) {
    return " LIMIT " + nrRows;
  }

  @Override
  public String getURL(IDatabaseConnection connection) throws DatabaseDialectException {
    if (connection.getAccessType() == DatabaseAccessType.ODBC) {
      return "jdbc:odbc:" + connection.getDatabaseName();
    } else {
      if (isEmpty(connection.getDatabasePort())) {
        return "jdbc:com.nuodb://" + connection.getHostname() + "/" + connection.getDatabaseName();
      } else {
        return "jdbc:com.nuodb://" + connection.getHostname() + ":" + connection.getDatabasePort() + "/" + connection.getDatabaseName();
      }
    }
  }

  @Override
  public boolean supportsBitmapIndex() {
    return false;
  }

  @Override
  public String[] getReservedWords() {
    // as of 11/21/2013
    return new String[]{"ABS", "ACOS", "ASIN", "ATAN2", "ATAN", "AVG", "BITS",
        "BIT_LENGTH", "BREAK", "CASCADE", "CATCH", "CEILING", "CHARACTER_LENGTH",
        "COALESCE", "CONCAT", "CONTAINING", "CONVERT_TZ", "COS", "COT", "COUNT",
        "CURRENT_SCHEMA", "DAYOFWEEK", "DAYOFYEAR", "DEGREES", "END_FOR",
        "END_IF", "END_PROCEDURE", "END_TRIGGER", "END_TRY", "END_WHILE", "ENUM",
        "EXTRACT", "FLOOR", "GENERATED", "IF", "IFNULL", "KEY", "LIMIT", "LOCATE",
        "LOWER", "LTRIM", "MAX", "MIN", "MOD", "MSLEEP", "NEXT", "NEXT_VALUE",
        "NOW", "NTEXT", "NULLIF", "NVARCHAR", "OCTETS", "OCTET_LENGTH", "OFF",
        "OFFSET", "PI", "POSITION", "POWER", "RADIANS", "RAND", "RECORD_BATCHING",
        "REGEXP", "RESTART", "RESTRICT", "REVERSE", "ROUND", "RTRIM", "SHOW",
        "SIN", "SMALLDATETIME", "SQRT", "STARTING", "STRING_TYPE", "SUBSTRING_INDEX",
        "SUBSTR", "SUM", "TAN", "THROW", "TINYBLOB", "TINYINT", "TRIM", "TRY",
        "VAR", "VER", "WHILE"};
  }

  @Override
  public String getAddColumnStatement(String tablename, IValueMeta v, String tk, boolean use_autoinc, String pk, boolean semicolon) {
    return "ALTER TABLE " + tablename + " ADD COLUMN " + getFieldDefinition(v, tk, pk, use_autoinc, true, false);
  }

  @Override
  public boolean useSchemaNameForTableList() {
    return true;
  }

  @Override
  public String getModifyColumnStatement(String tablename, IValueMeta v, String tk, boolean use_autoinc, String pk, boolean semicolon) {
    return "ALTER TABLE " + tablename + " MODIFY COLUMN " + getFieldDefinition(v, tk, pk, use_autoinc, true, false);
  }

  @Override
  public String getFieldDefinition(IValueMeta v, String tk, String pk, boolean use_autoinc, boolean add_fieldname, boolean add_cr) {
    String retval = "";

    int length = v.getLength();
    int precision = v.getPrecision();

    String fieldname = v.getName();
    if (add_fieldname) {
      retval += fieldname + " ";
    }

    int type = v.getType();
    switch (type) {
      case IValueMeta.TYPE_INTEGER:
      case IValueMeta.TYPE_NUMBER:
      case IValueMeta.TYPE_BIGNUMBER:
        if (fieldname.equalsIgnoreCase(tk)) {
          retval += "BIGINT";
          if (use_autoinc) {
            retval += " GENERATED ALWAYS AS IDENTITY";
          }
          if (fieldname.equalsIgnoreCase(pk)) {
            retval += " PRIMARY KEY";
          }
        } else {
          if (precision == 0) {
            if (length > 18) {
              retval += "DECIMAL(" + length + ",0)";
            } else {
              if (length > 9) {
                retval += "BIGINT";
              } else {
                retval += "INTEGER";
              }
              if (fieldname.equalsIgnoreCase(pk)) {
                retval += " PRIMARY KEY";
              }
            }
          } else {
            if (length > 15) {
              retval += "DECIMAL(" + length;
              if (precision > 0) {
                retval += ", " + precision;
              }
              retval += ")";
            } else {
              retval += "DOUBLE";
            }
          }
        }
        break;
      case IValueMeta.TYPE_STRING:
        retval += "STRING";
        break;
      case IValueMeta.TYPE_DATE:
        retval += "DATE";
        break;
      case IValueMeta.TYPE_BOOLEAN:
        retval += "BOOLEAN";
        break;
      case IValueMeta.TYPE_BINARY:
        retval += "BLOB";
        break;
      default:
        retval += "UNKNOWN";
        break;
    }

    if (add_cr) {
      retval += CR;
    }

    return retval;
  }

  @Override
  public String[] getUsedLibraries() {
    return new String[]{"nuodb-jdbc.jar"};
  }

  @Override
  public String getNativeDriver() {
    return "com.nuodb.jdbc.Driver";
  }

  @Override
  public IDatabaseType getDatabaseType() {
    return DBTYPE;
  }

  @Override
  public String getNativeJdbcPre() {
    return "jdbc:com.nuodb://";
  }

  @Override
  public String getExtraOptionSeparator() {
    return "&";
  }

  @Override
  public String getExtraOptionIndicator() {
    return "?";
  }
}
