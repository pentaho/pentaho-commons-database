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
import org.pentaho.database.model.DatabaseConnection;
import org.pentaho.database.model.DatabaseType;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.model.IDatabaseType;

public class MonetDatabaseDialect extends AbstractDatabaseDialect {

  /**
   * 
   */
  private static final long serialVersionUID = -1560490581511085L;
  private static final IDatabaseType DBTYPE = new DatabaseType(
      "MonetDB", //$NON-NLS-1$
      "MONETDB", //$NON-NLS-1$
      DatabaseAccessType.getList( DatabaseAccessType.NATIVE, DatabaseAccessType.JNDI ), 50000,
      "" //$NON-NLS-1$
  );

  public MonetDatabaseDialect() {

  }

  public IDatabaseType getDatabaseType() {
    return DBTYPE;
  }

  @Override
  public String getNativeDriver() {
    return "nl.cwi.monetdb.jdbc.MonetDriver"; //$NON-NLS-1$
  }

  @Override
  public String getNativeJdbcPre() {
    return "jdbc:monetdb:"; //$NON-NLS-1$
  }

  @Override
  public String getURL( IDatabaseConnection databaseConnection ) throws DatabaseDialectException {
    if ( toInt( databaseConnection.getDatabasePort(), -1 ) <= 0 || isEmpty( databaseConnection.getHostname() ) ) {
      // When no port is specified, or port is 0 support local/memory
      // HSQLDB databases.
      return getNativeJdbcPre() + databaseConnection.getDatabaseName();
    } else {
      return getNativeJdbcPre()
        + "//" + databaseConnection.getHostname() + ":" + databaseConnection.getDatabasePort() + "/"
        + databaseConnection.getDatabaseName(); //$NON-NLS-1$
    }
  }

  /**
   * @return true if the database supports bitmap indexes
   */
  @Override
  public boolean supportsBitmapIndex() {
    return false;
  }

  @Override
  public String getTruncateTableStatement( String tableName ) {
    return "DELETE FROM " + tableName; //$NON-NLS-1$
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
   * 
   */
  @Override
  public String getAddColumnStatement( String tablename, IValueMeta v, String tk, boolean use_autoinc, String pk,
      boolean semicolon ) {
    return "ALTER TABLE " + tablename + " ADD " + getFieldDefinition( v, tk, pk, use_autoinc, true, false ); //$NON-NLS-1$ //$NON-NLS-2$
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
    return "ALTER TABLE " + tablename + " MODIFY " + getFieldDefinition( v, tk, pk, use_autoinc, true, false ); //$NON-NLS-1$ //$NON-NLS-2$
  }

  @Override
  public String getFieldDefinition( IValueMeta v, String tk, String pk, boolean use_autoinc, boolean add_fieldname,
      boolean add_cr ) {
    StringBuffer retval = new StringBuffer( 128 );

    String fieldname = v.getName();
    int length = v.getLength();
    int precision = v.getPrecision();

    if ( add_fieldname ) {
      retval.append( fieldname ).append( ' ' );
    }

    int type = v.getType();
    switch ( type ) {
      case IValueMeta.TYPE_DATE:
        retval.append( "TIMESTAMP" );
        break;
      case IValueMeta.TYPE_BOOLEAN:
        if ( supportsBooleanDataType() ) {
          retval.append( "BOOLEAN" );
        } else {
          retval.append( "CHAR(1)" );
        }
        break;
      case IValueMeta.TYPE_NUMBER:
      case IValueMeta.TYPE_INTEGER:
      case IValueMeta.TYPE_BIGNUMBER:
        if ( fieldname.equalsIgnoreCase( tk ) || // Technical key
            fieldname.equalsIgnoreCase( pk ) // Primary key
        ) {
          retval.append( "BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 0, INCREMENT BY 1) PRIMARY KEY" );
        } else {
          if ( length > 0 ) {
            if ( precision > 0 || length > 18 ) {
              retval.append( "NUMERIC(" ).append( length ).append( ", " ).append( precision ).append( ')' );
            } else {
              if ( length > 9 ) {
                retval.append( "BIGINT" );
              } else {
                if ( length < 5 ) {
                  retval.append( "SMALLINT" );
                } else {
                  retval.append( "INTEGER" );
                }
              }
            }

          } else {
            retval.append( "DOUBLE PRECISION" );
          }
        }
        break;
      case IValueMeta.TYPE_STRING:
        if ( length >= CLOB_LENGTH ) {
          retval.append( "TEXT" );
        } else {
          retval.append( "VARCHAR" );
          if ( length > 0 ) {
            retval.append( '(' ).append( length );
          } else {
            retval.append( '(' ); // Maybe use some default DB String length?
          }
          retval.append( ')' );
        }
        break;
      default:
        retval.append( " UNKNOWN" );
        break;
    }

    if ( add_cr ) {
      retval.append( CR );
    }

    return retval.toString();
  }

  @Override
  public String[] getUsedLibraries() {
    return new String[] { "monetdb-1.7-jdbc.jar" };
  }

  @Override
  public String[] getReservedWords() {
    return new String[] { "ADD", "ALL", "ALLOCATE", "ALTER", "AND", "ANY", "ARE", "ARRAY", "AS", "ASENSITIVE",
      "ASYMMETRIC", "AT", "ATOMIC", "AUTHORIZATION", "BEGIN", "BETWEEN", "BIGINT", "BINARY", "BLOB", "BOOLEAN", "BOTH",
      "BY", "CALL", "CALLED", "CASCADED", "CASE", "CAST", "CHAR", "CHARACTER", "CHECK", "CLOB", "CLOSE", "COLLATE",
      "COLUMN", "COMMIT", "CONDIITON", "CONNECT", "CONSTRAINT", "CONTINUE", "CORRESPONDING", "CREATE", "CROSS", "CUBE",
      "CURRENT", "CURRENT_DATE", "CURRENT_DEFAULT_TRANSFORM_GROUP", "CURRENT_PATH", "CURRENT_ROLE", "CURRENT_TIME",
      "CURRENT_TIMESTAMP", "CURRENT_TRANSFORM_GROUP_FOR_TYPE", "CURRENT_USER", "CURSOR", "CYCLE", "DATE", "DAY",
      "DEALLOCATE", "DEC", "DECIMAL", "DECLARE", "DEFAULT", "DELETE", "DEREF", "DESCRIBE", "DETERMINISTIC",
      "DISCONNECT", "DISTINCT", "DO", "DOUBLE", "DROP", "DYNAMIC", "EACH", "ELEMENT", "ELSE", "ELSEIF", "END",
      "ESCAPE", "EXCEPT", "EXEC", "EXECUTE", "EXISTS", "EXIT", "EXTERNAL", "FALSE", "FETCH", "FILTER", "FLOAT", "FOR",
      "FOREIGN", "FREE", "FROM", "FULL", "FUNCTION", "GET", "GLOBAL", "GRANT", "GROUP", "GROUPING", "HANDLER",
      "HAVING", "HEADER", "HOLD", "HOUR", "IDENTITY", "IF", "IMMEDIATE", "IN", "INDICATOR", "INNER", "INOUT", "INPUT",
      "INSENSITIVE", "INSERT", "INT", "INTEGER", "INTERSECT", "INTERVAL", "INTO", "IS", "ITERATE", "JOIN", "LANGUAGE",
      "LARGE", "LATERAL", "LEADING", "LEAVE", "LEFT", "LIKE", "LOCAL", "LOCALTIME", "LOCALTIMESTAMP", "LOOP", "MATCH",
      "MEMBER", "METHOD", "MINUTE", "MODIFIES", "MODULE", "MONTH", "MULTISET", "NATIONAL", "NAUTRAL", "NCHAR", "NCLOB",
      "NEW", "NEXT", "NO", "NONE", "NOT", "NULL", "NUMERIC", "OF", "OLD", "ON", "ONLY", "OPEN", "OR", "ORDER", "OUT",
      "OUTER", "OUTPUT", "OVER", "OVERLAPS", "PARAMETER", "PARTITION", "PRECISION", "PREPARE", "PRIMARY", "PROCEDURE",
      "RANGE", "READS", "REAL", "RECURSIVE", "REF", "REFERENCES", "REFERENCING", "RELEASE", "REPEAT", "RESIGNAL",
      "RESULT", "RETURN", "RETURNS", "REVOKE", "RIGHT", "ROLLBACK", "ROLLUP", "ROW", "ROWS", "SAVEPOINT", "SCOPE",
      "SCROLL", "SECOND", "SEARCH", "SELECT", "SENSITIVE", "SESSION_USER", "SET", "SIGNAL", "SIMILAR", "SMALLINT",
      "SOME", "SPECIFIC", "SPECIFICTYPE", "SQL", "SQLEXCEPTION", "SQLSTATE", "SQLWARNING", "START", "STATIC",
      "SUBMULTISET", "SYMMETRIC", "SYSTEM", "SYSTEM_USER", "TABLE", "TABLESAMPLE", "THEN", "TIME", "TIMESTAMP",
      "TIMEZONE_HOUR", "TIMEZONE_MINUTE", "TO", "TRAILING", "TRANSLATION", "TREAT", "TRIGGER", "TRUE", "UNDO", "UNION",
      "UNIQUE", "UNKNOWN", "UNNEST", "UNTIL", "UPDATE", "USER", "USING", "VALUE", "VALUES", "VARCHAR", "VARYING",
      "WHEN", "WHENEVER", "WHERE", "WHILE", "WINDOW", "WITH", "WITHIN", "WITHOUT", "YEAR", "ALWAYS", "ACTION", "ADMIN",
      "AFTER", "ALIAS", "ASC", "AUTOCOMMIT", "AVG", "BACKUP", "BEFORE", "CACHED", "CASCADE", "CASEWHEN", "CHECKPOINT",
      "CLASS", "COALESCE", "COLLATION", "COMPACT", "COMPRESSED", "CONCAT", "CONVERT", "COUNT", "DATABASE", "DEFRAG",
      "DESC", "EVERY", "EXPLAIN", "EXTRACT", "GENERATED", "IFNULL", "IGNORECASE", "IMMEDIATELY", "INCREMENT", "INDEX",
      "KEY", "LIMIT", "LOGSIZE", "MAX", "MAXROWS", "MEMORY", "MERGE", "MIN", "MINUS", "NOW", "NOWAIT", "NULLIF", "NVL",
      "OFFSET", "PASSWORD", "SCHEMA", "PLAN", "PRESERVE", "POSITION", "PROPERTY", "PUBLIC", "QUEUE", "READONLY",
      "REFERENTIAL_INTEGRITY", "RENAME", "RESTART", "RESTRICT", "ROLE", "SCRIPT", "SCRIPTFORMAT", "SEQUENCE",
      "SHUTDOWN", "SOURCE", "STDDEV_POP", "STDDEV_SAMP", "SUBSTRING", "SUM", "SYSDATE", "TEMP", "TEMPORARY", "TEXT",
      "TODAY", "TOP", "TRIM", "VAR_POP", "VAR_SAMP", "VIEW", "WORK", "WRITE_DELAY", };
  }

  @Override
  public IDatabaseConnection createNativeConnection( String jdbcUrl ) {
    if ( !jdbcUrl.startsWith( getNativeJdbcPre() ) ) {
      throw new RuntimeException( "JDBC URL " + jdbcUrl + " does not start with " + getNativeJdbcPre() );
    }
    DatabaseConnection dbconn = new DatabaseConnection();
    dbconn.setDatabaseType( getDatabaseType() );
    dbconn.setAccessType( DatabaseAccessType.NATIVE );
    if ( jdbcUrl.startsWith( getNativeJdbcPre() + "monetdb://" ) ) {
      String str = jdbcUrl.substring( getNativeJdbcPre().length() + "monetdb://".length() );
      String hostname = null;
      String port = null;
      String databaseName = null;

      // hostname:port/dbname
      // hostname:port
      // hostname/dbname
      // dbname

      // TODO: Support Parameters

      if ( str.indexOf( ":" ) >= 0 ) {
        hostname = str.substring( 0, str.indexOf( ":" ) );
        str = str.substring( str.indexOf( ":" ) + 1 );
        if ( str.indexOf( "/" ) >= 0 ) {
          port = str.substring( 0, str.indexOf( "/" ) );
          databaseName = str.substring( str.indexOf( "/" ) + 1 );
        } else {
          port = str;
        }
      } else {
        if ( str.indexOf( "/" ) >= 0 ) {
          hostname = str.substring( 0, str.indexOf( "/" ) );
          databaseName = str.substring( str.indexOf( "/" ) + 1 );
        } else {
          databaseName = str;
        }
      }
      if ( hostname != null ) {
        dbconn.setHostname( hostname );
      }
      if ( port != null ) {
        dbconn.setDatabasePort( port );
      }
      if ( databaseName != null ) {
        setDatabaseNameAndParams( dbconn, databaseName );
      }
    } else {

      // databasename

      dbconn.setDatabaseName( jdbcUrl.substring( getNativeJdbcPre().length() ) );
    }
    return dbconn;
  }
}
