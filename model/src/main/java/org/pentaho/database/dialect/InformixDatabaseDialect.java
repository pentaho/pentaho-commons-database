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

public class InformixDatabaseDialect extends AbstractDatabaseDialect {
  /**
   * 
   */
  private static final long serialVersionUID = -3869260264366995990L;
  private static final IDatabaseType DBTYPE = new DatabaseType( "Informix", "INFORMIX", DatabaseAccessType.getList(
      DatabaseAccessType.NATIVE, DatabaseAccessType.JNDI ), 9088,
      "http://publib.boulder.ibm.com/infocenter/idshelp/v10/index.jsp?topic=/com.ibm.jdbc_pg.doc/jdbc212.htm" );
  
  public InformixDatabaseDialect() {

  }

  @Override
  public IDatabaseType getDatabaseType() {
    return DBTYPE;
  }

  @Override
  public String getURL( IDatabaseConnection connection ) throws DatabaseDialectException {
    // jdbc:informix-sqli://192.168.149.128:9088/stores:INFORMIXSERVER=demo_on
    return "jdbc:informix-sqli://" + connection.getHostname() + ":" + connection.getDatabasePort() + "/"
      + connection.getDatabaseName() + ":INFORMIXSERVER=" + connection.getInformixServername() + ";DELIMIDENT=Y";
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
    return "ALTER TABLE " + tablename + " ADD " + getFieldDefinition( v, tk, pk, use_autoinc, true, false );
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
    return "ALTER TABLE " + tablename + " MODIFY " + getFieldDefinition( v, tk, pk, use_autoinc, true, false );
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
        retval += "DATETIME YEAR to FRACTION";
        break;
      case IValueMeta.TYPE_BOOLEAN:
        if ( supportsBooleanDataType() ) {
          retval += "BOOLEAN";
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
            retval += "SERIAL8";
          } else {
            retval += "INTEGER PRIMARY KEY";
          }
        } else {
          if ( ( length < 0 && precision < 0 ) || precision > 0 || length > 9 ) {
            retval += "FLOAT";
          } else { // Precision == 0 && length<=9
            retval += "INTEGER";
          }
        }
        break;
      case IValueMeta.TYPE_STRING:
        if ( length >= CLOB_LENGTH ) {
          retval += "CLOB";
        } else {
          if ( length < 256 ) {
            retval += "VARCHAR";
            if ( length > 0 ) {
              retval += "(" + length + ")";
            }
          } else {
            if ( length < 32768 ) {
              retval += "LVARCHAR";
            } else {
              retval += "TEXT";
            }
          }
        }
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

  @Override
  public String[] getUsedLibraries() {
    return new String[] { "ifxjdbc.jar" };
  }

  @Override
  public String getNativeDriver() {
    return "com.informix.jdbc.IfxDriver";
  }

  @Override
  public String getNativeJdbcPre() {
    return "jdbc:informix-sqli:";
  }

  // /////////////////
  @Override
  public int getNotFoundTK( boolean use_autoinc ) {
    if ( supportsAutoInc() && use_autoinc ) {
      return 1;
    }
    return super.getNotFoundTK( use_autoinc );
  }

  /**
   * Indicates the need to insert a placeholder (0) for auto increment fields.
   * 
   * @return true if we need a placeholder for auto increment fields in insert statements.
   */
  @Override
  public boolean needsPlaceHolder() {
    return true;
  }

  @Override
  public boolean needsToLockAllTables() {
    return false;
  }

  @Override
  public String getSQLQueryFields( String tableName ) {
    return "SELECT FIRST 1 * FROM " + tableName;
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
    return "SELECT FIRST 1 " + columnname + " FROM " + tableName;
  }

  @Override
  public String getSQLLockTables( String[] tableNames ) {
    String sql = "";
    for ( int i = 0; i < tableNames.length; i++ ) {
      sql += "LOCK TABLE " + tableNames[i] + " IN EXCLUSIVE MODE;" + CR;
    }
    return sql;
  }

  @Override
  public String getSQLUnlockTables( String[] tableNames ) {
    return null;
    // String sql=""; for (int i=0;i<tableNames.length;i++) { sql+="UNLOCK TABLE "+tableNames[i]+";"+ CR; } return
    // sql;
  }

}
