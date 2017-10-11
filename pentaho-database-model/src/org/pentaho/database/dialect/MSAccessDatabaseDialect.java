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

package org.pentaho.database.dialect;

import org.pentaho.database.DatabaseDialectException;
import org.pentaho.database.IValueMeta;
import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.DatabaseType;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.model.IDatabaseType;

public class MSAccessDatabaseDialect extends AbstractDatabaseDialect {

  /**
   * 
   */
  private static final long serialVersionUID = 84703860781502052L;
  private static final IDatabaseType DBTYPE = new DatabaseType( "MS Access", "MSACCESS", DatabaseAccessType
      .getList( DatabaseAccessType.ODBC ), -1, null );

  public MSAccessDatabaseDialect() {

  }

  public IDatabaseType getDatabaseType() {
    return DBTYPE;
  }

  @Override
  public String getNativeDriver() {
    return null;
  }

  @Override
  public String getNativeJdbcPre() {
    return null;
  }

  @Override
  public boolean supportsSetCharacterStream() {
    return false;
  }

  /**
   * @see DatabaseInterface#getNotFoundTK(boolean)
   */
  @Override
  public int getNotFoundTK( boolean use_autoinc ) {
    if ( supportsAutoInc() && use_autoinc ) {
      return 1;
    }
    return super.getNotFoundTK( use_autoinc );
  }

  /**
   * Checks whether or not the command setFetchSize() is supported by the JDBC databaseType...
   * 
   * @return true is setFetchSize() is supported!
   */
  public boolean isFetchSizeSupported() {
    return false;
  }

  /**
   * @see org.pentaho.di.core.database.DatabaseInterface#getSchemaTableCombination(java.lang.String, java.lang.String)
   */
  public String getSchemaTableCombination( String schema_name, String table_part ) {
    return "\"" + schema_name + "\".\"" + table_part + "\"";
  }

  /**
   * Get the maximum length of a text field for this database connection. This includes optional CLOB, Memo and Text
   * fields. (the maximum!)
   * 
   * @return The maximum text field length for this database type. (mostly CLOB_LENGTH)
   */
  public int getMaxTextFieldLength() {
    return 65536;
  }

  /**
   * @return true if the database supports transactions.
   */
  public boolean supportsTransactions() {
    return false;
  }

  /**
   * @return true if the database supports bitmap indexes
   */
  public boolean supportsBitmapIndex() {
    return false;
  }

  /**
   * @return true if the database JDBC databaseType supports the setLong command
   */
  public boolean supportsSetLong() {
    return false;
  }

  /**
   * @return true if the database supports views
   */
  public boolean supportsViews() {
    return false;
  }

  /**
   * @return true if the database supports synonyms
   */
  public boolean supportsSynonyms() {
    return false;
  }

  /**
   * @param tableName
   *          The table to be truncated.
   * @return The SQL statement to truncate a table: remove all rows from it without a transaction
   */
  @Override
  public String getTruncateTableStatement( String tableName ) {
    return "DELETE FROM " + tableName;
  }

  /**
   * Generates the SQL statement to add a column to the specified table
   * 
   * @param tablename
   *          The table to add
   * @param v
   *          The column defined as a value
   * @param tk
   *          the name of the technical key field
   * @param use_autoinc
   *          whether or not this field uses auto increment
   * @param pk
   *          the name of the primary key field
   * @param semicolon
   *          whether or not to add a semi-colon behind the statement.
   * @return the SQL statement to add a column to the specified table
   */
  @Override
  public String getAddColumnStatement( String tablename, IValueMeta v, String tk, boolean use_autoinc, String pk,
      boolean semicolon ) {
    return "ALTER TABLE " + tablename + " ADD COLUMN " + getFieldDefinition( v, tk, pk, use_autoinc, true, false );
  }

  /**
   * Generates the SQL statement to drop a column from the specified table
   * 
   * @param tablename
   *          The table to add
   * @param v
   *          The column defined as a value
   * @param tk
   *          the name of the technical key field
   * @param use_autoinc
   *          whether or not this field uses auto increment
   * @param pk
   *          the name of the primary key field
   * @param semicolon
   *          whether or not to add a semi-colon behind the statement.
   * @return the SQL statement to drop a column from the specified table
   */
  @Override
  public String getDropColumnStatement( String tablename, IValueMeta v, String tk, boolean use_autoinc, String pk,
      boolean semicolon ) {
    return "ALTER TABLE " + tablename + " DROP COLUMN " + v.getName() + CR;
  }

  /**
   * Generates the SQL statement to modify a column in the specified table
   * 
   * @param tablename
   *          The table to add
   * @param v
   *          The column defined as a value
   * @param tk
   *          the name of the technical key field
   * @param use_autoinc
   *          whether or not this field uses auto increment
   * @param pk
   *          the name of the primary key field
   * @param semicolon
   *          whether or not to add a semi-colon behind the statement.
   * @return the SQL statement to modify a column in the specified table
   */
  @Override
  public String getModifyColumnStatement( String tablename, IValueMeta v, String tk, boolean use_autoinc, String pk,
      boolean semicolon ) {
    return "ALTER TABLE " + tablename + " ALTER COLUMN " + getFieldDefinition( v, tk, pk, use_autoinc, true, false );
  }

  @Override
  public String getFieldDefinition( IValueMeta v, String tk, String pk, boolean use_autoinc, boolean add_fieldname,
      boolean add_cr ) {
    String retval = "";

    String fieldname = v.getName();
    int length = v.getLength();
    int precision = v.getPrecision();

    if ( add_fieldname ) {
      retval += fieldname + " ";
    }

    int type = v.getType();
    switch ( type ) {
      case IValueMeta.TYPE_DATE:
        retval += "DATETIME";
        break;
      // Move back to Y/N for bug - [# 1538] Repository on MS ACCESS: error creating repository
      case IValueMeta.TYPE_BOOLEAN:
        if ( supportsBooleanDataType() ) {
          retval += "BIT";
        } else {
          retval += "CHAR(1)";
        }
        break;
      case IValueMeta.TYPE_NUMBER:
      case IValueMeta.TYPE_INTEGER:
      case IValueMeta.TYPE_BIGNUMBER:
        if ( fieldname.equalsIgnoreCase( tk ) || // Technical key
            fieldname.equalsIgnoreCase( pk ) // Primary key
        ) {
          if ( use_autoinc ) {
            retval += "COUNTER PRIMARY KEY";
          } else {
            retval += "LONG PRIMARY KEY";
          }
        } else {
          if ( precision == 0 ) {
            if ( length > 9 ) {
              retval += "DOUBLE";
            } else {
              if ( length > 5 ) {
                retval += "LONG";
              } else {
                retval += "INTEGER";
              }
            }
          } else {
            retval += "DOUBLE";
          }
        }
        break;
      case IValueMeta.TYPE_STRING:
        if ( length > 0 ) {
          if ( length < 256 ) {
            retval += "TEXT(" + length + ")";
          } else {
            retval += "MEMO";
          }
        } else {
          retval += "TEXT";
        }
        break;
      case IValueMeta.TYPE_BINARY:
        retval += " LONGBINARY";
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

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.di.core.database.DatabaseInterface#getReservedWords()
   */
  @Override
  public String[] getReservedWords() {
    return new String[] {
    /*
     * http://support.microsoft.com/kb/q109312 Note that if you set a reference to a type library, an object library, or
     * an ActiveX control, that library's reserved words are also reserved words in your database. For example, if you
     * add an ActiveX control to a form, a reference is set and the names of the objects, methods, and properties of
     * that control become reserved words in your database. For existing objects with names that contain reserved words,
     * you can avoid errors by surrounding the object name with brackets [ ], see getStartQuote(),getEndQuote().
     */
    "ADD", "ALL", "ALPHANUMERIC", "ALTER", "AND", "ANY", "APPLICATION", "AS", "ASC", "ASSISTANT", "AUTOINCREMENT",
      "AVG", "BETWEEN", "BINARY", "BIT", "BOOLEAN", "BY", "BYTE", "CHAR", "CHARACTER", "COLUMN", "COMPACTDATABASE",
      "CONSTRAINT", "CONTAINER", "COUNT", "COUNTER", "CREATE", "CREATEDATABASE", "CREATEFIELD", "CREATEGROUP",
      "CREATEINDEX", "CREATEOBJECT", "CREATEPROPERTY", "CREATERELATION", "CREATETABLEDEF", "CREATEUSER",
      "CREATEWORKSPACE", "CURRENCY", "CURRENTUSER", "DATABASE", "DATE", "DATETIME", "DELETE", "DESC", "DESCRIPTION",
      "DISALLOW", "DISTINCT", "DISTINCTROW", "DOCUMENT", "DOUBLE", "DROP", "ECHO", "ELSE", "END", "EQV", "ERROR",
      "EXISTS", "EXIT", "FALSE", "FIELD", "FIELDS", "FILLCACHE", "FLOAT", "FLOAT4", "FLOAT8", "FOREIGN", "FORM",
      "FORMS", "FROM", "FULL", "FUNCTION", "GENERAL", "GETOBJECT", "GETOPTION", "GOTOPAGE", "GROUP", "GUID", "HAVING",
      "IDLE", "IEEEDOUBLE", "IEEESINGLE", "IF", "IGNORE", "IMP", "IN", "INDEX", "INDEX", "INDEXES", "INNER", "INSERT",
      "INSERTTEXT", "INT", "INTEGER", "INTEGER1", "INTEGER2", "INTEGER4", "INTO", "IS", "JOIN", "KEY", "LASTMODIFIED",
      "LEFT", "LEVEL", "LIKE", "LOGICAL", "LOGICAL1", "LONG", "LONGBINARY", "LONGTEXT", "MACRO", "MATCH", "MAX", "MIN",
      "MOD", "MEMO", "MODULE", "MONEY", "MOVE", "NAME", "NEWPASSWORD", "NO", "NOT", "NULL", "NUMBER", "NUMERIC",
      "OBJECT", "OLEOBJECT", "OFF", "ON", "OPENRECORDSET", "OPTION", "OR", "ORDER", "OUTER", "OWNERACCESS",
      "PARAMETER", "PARAMETERS", "PARTIAL", "PERCENT", "PIVOT", "PRIMARY", "PROCEDURE", "PROPERTY", "QUERIES", "QUERY",
      "QUIT", "REAL", "RECALC", "RECORDSET", "REFERENCES", "REFRESH", "REFRESHLINK", "REGISTERDATABASE", "RELATION",
      "REPAINT", "REPAIRDATABASE", "REPORT", "REPORTS", "REQUERY", "RIGHT", "SCREEN", "SECTION", "SELECT", "SET",
      "SETFOCUS", "SETOPTION", "SHORT", "SINGLE", "SMALLINT", "SOME", "SQL", "STDEV", "STDEVP", "STRING", "SUM",
      "TABLE", "TABLEDEF", "TABLEDEFS", "TABLEID", "TEXT", "TIME", "TIMESTAMP", "TOP", "TRANSFORM", "TRUE", "TYPE",
      "UNION", "UNIQUE", "UPDATE", "USER", "VALUE", "VALUES", "VAR", "VARP", "VARBINARY", "VARCHAR", "WHERE", "WITH",
      "WORKSPACE", "XOR", "YEAR", "YES", "YESNO" };
  }

  /**
   * @return The start quote sequence, mostly just double quote, but sometimes [, ...
   */
  @Override
  public String getStartQuote() {
    return "[";
  }

  /**
   * @return The end quote sequence, mostly just double quote, but sometimes ], ...
   */
  @Override
  public String getEndQuote() {
    return "]";
  }

  @Override
  public String[] getUsedLibraries() {
    return new String[] {};
  }

  @Override
  public boolean supportsGetBlob() {
    return false;
  }

  @Override
  public String getURL( IDatabaseConnection connection ) throws DatabaseDialectException {
    return "jdbc:odbc:" + connection.getDatabaseName();
  }
}
