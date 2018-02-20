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

import java.util.Iterator;
import java.util.Map;

import org.pentaho.database.DatabaseDialectException;
import org.pentaho.database.IValueMeta;
import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.DatabaseType;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.model.IDatabaseType;

public class TeradataDatabaseDialect extends AbstractDatabaseDialect {
  /**
   * 
   */
  private static final long serialVersionUID = -3869260264366995990L;
  private static final IDatabaseType DBTYPE = new DatabaseType( "Teradata", "TERADATA", DatabaseAccessType.getList(
      DatabaseAccessType.NATIVE, DatabaseAccessType.ODBC, DatabaseAccessType.JNDI ), 1025,
      "http://www.info.ncr.com/eTeradata-BrowseBy-Results.cfm?pl=&PID=&title=%25&release=&kword=CJDBC&sbrn=7&nm=Teradata+Tools+and+Utilities+-+Java+Database+Connectivity+(JDBC)" );

  public TeradataDatabaseDialect() {

  }

  @Override
  public IDatabaseType getDatabaseType() {
    return DBTYPE;
  }

  @Override
  public String getURL( IDatabaseConnection connection ) throws DatabaseDialectException {
    if (connection.getAccessType() == DatabaseAccessType.NATIVE) {
      String url = "jdbc:teradata://" + connection.getHostname();
      String sep = "/"; // Default separator is / if it's the only parameter, otherwise it's a comma
      if(connection.getDatabaseName() != null && connection.getDatabaseName().length()>0){
        url += "/DATABASE="+connection.getDatabaseName();
        sep = ",";
      } 
      if(connection.getDatabasePort() != null && connection.getDatabasePort().length()>0){
        url += sep + "DBS_PORT=" + connection.getDatabasePort();
      }
      return url;
      
    } else {
      return "jdbc:odbc:" + connection.getDatabaseName();
    }
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
    return "ALTER TABLE "+tablename+" ADD "+getFieldDefinition(v, tk, pk, use_autoinc, true, false);
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
    return "ALTER TABLE "+tablename+" MODIFY "+getFieldDefinition(v, tk, pk, use_autoinc, true, false);
  }

  @Override
  public String getFieldDefinition( IValueMeta v, String tk, String pk, boolean use_autoinc, boolean add_fieldname,
      boolean add_cr ) {
    String retval="";
    
    String fieldname = v.getName();
    int    length    = v.getLength();
    int    precision = v.getPrecision();
    
    if (add_fieldname) retval+=fieldname+" ";
    
    int type         = v.getType();
    switch(type)
    {
    case IValueMeta.TYPE_DATE   : retval+="TIMESTAMP"; break;
    case IValueMeta.TYPE_BOOLEAN: retval+="CHAR(1)"; break;
    case IValueMeta.TYPE_NUMBER : 
    case IValueMeta.TYPE_INTEGER: 
        case IValueMeta.TYPE_BIGNUMBER: 
      if (fieldname.equalsIgnoreCase(tk) || // Technical key
          fieldname.equalsIgnoreCase(pk)    // Primary key
          ) 
      {
        retval+="INTEGER"; // TERADATA has no Auto-increment functionality nor Sequences!
      } 
      else
      {
        if (length>0)
        {
          if (precision>0 || length>9)
          {
            retval+="DECIMAL("+length+", "+precision+")";
          }
          else
          {
            if (length>5)
            {
                            retval+="INTEGER";
                        }
                        else
                        {
                            if (length<3)
                            {
                                retval+="BYTEINT";
                            }
                            else
                            {
                                retval+="SMALLINT";
                            }
            }
          }
          
        }
        else
        {
          retval+="DOUBLE PRECISION";
        }
      }
      break;
    case IValueMeta.TYPE_STRING:
      if (length>64000)
      {
        retval+="CLOB";
      }
      else
      {
        retval+="VARCHAR"; 
        if (length>0)
        {
          retval+="("+length+")";
        }
        else
        {
          retval+="(64000)"; // Maybe use some default DB String length?
        }
      }
      break;
    default:
      retval+=" UNKNOWN";
      break;
    }
    
    if (add_cr) retval += CR;
    
    return retval;
  }

  @Override
  public String[] getUsedLibraries() {
    return new String[] { "terajdbc4.jar", "tdgssjava.jar" };
  }

  
  @Override
  public String getNativeDriver() {
    return "com.teradata.jdbc.TeraDriver";
  }

  @Override
  public String getNativeJdbcPre() {
    return "jdbc:teradata://";
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
   * Set to "false" based on the following references:
   * http://www.info.teradata.com/HTMLPubs/DB_TTU_14_00/index.html#page/SQL_Reference/B035_1184_111A/Create_Table-Details.012.045.html
   * http://ysgitdiary.blogspot.com/2010/06/how-to-add-autoincrement-in-teradata.html
   * 
   * @return true if we need a placeholder for auto increment fields in insert statements.
   */
  @Override
  public boolean needsPlaceHolder() {
    return false;
  }

  @Override
  /**
   * needsToLockAllTables is not described in the interface - setting to false
   */
  public boolean needsToLockAllTables() {
    return false;
  }

  @Override
  /*
   * (non-Javadoc)
   * Source for this syntax:
   * http://forums.teradata.com/forum/database/limit-rows
   * 
   */
  public String getSQLQueryFields( String tableName ) {
    return "SELECT * FROM " + tableName + " sample 1";
  }

  @Override
  public String getSQLTableExists( String tablename ) {
    return "show table " + tablename;
  }

  @Override
  public String getSQLColumnExists(String columnname, String tablename)
  {
      return  "SELECT * FROM DBC.columns WHERE tablename =" + tablename + " AND columnname =" + columnname;
  }

  /*
   * (non-Javadoc)
   * Source for this syntax:
   * http://forums.teradata.com/forum/database/limit-rows
   * 
   */
  public String getSQLQueryColumnFields( String columnname, String tableName ) {
    return "SELECT " + columnname + " FROM " + tableName + " sample 1";
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#getLimitClause(int)
   */
  public String getLimitClause( int nrRows ) {
    return " sample " + Integer.toString( nrRows );
  }

  
  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#supportsBitmapIndex()
   */
  public boolean supportsBitmapIndex() {
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * Source:
   * http://teradataforum.com/l030103a.htm
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#getReservedWords()
   */
  public String[] getReservedWords() {
    return new String[] {"ABORT", "ABORTSESSION", "ABS", "ABSOLUTE", "ACCESS_LOCK", "ACCOUNT", 
      "ACOS", "ACOSH", "ACTION", "ADD", "ADD_MONTHS", "ADMIN", "AFTER", "AGGREGATE", "ALIAS", "ALL", 
      "ALLOCATE", "ALTER", "AMP", "AND", "ANSIDATE", "ANY", "ARE", "ARRAY", "AS", "ASC", "ASIN", "ASINH", 
      "ASSERTION", "AT", "ATAN", "ATAN2", "ATANH", "ATOMIC", "AUTHORIZATION", "AVE", "AVERAGE", "AVG", 
      "BEFORE", "BEGIN", "BETWEEN", "BINARY", "BIT", "BLOB", "BOOLEAN", "BOTH", "BREADTH", "BT", "BUT", 
      "BY", "BYTE", "BYTEINT", "BYTES", "CALL", "CASCADE", "CASCADED", "CASE", "CASE_N", "CASESPECIFIC", 
      "CAST", "CATALOG", "CD", "CHAR", "CHAR_LENGTH", "CHAR2HEXINT", "CHARACTER", "CHARACTER_LENGTH", 
      "CHARACTERS", "CHARS", "CHECK", "CHECKPOINT", "CLASS", "CLOB", "CLOSE", "CLUSTER", "CM", "COALESCE", 
      "COLLATE", "COLLATION", "COLLECT", "COLUMN", "COMMENT", "COMMIT", "COMPLETION", "COMPRESS", "CONNECT", 
      "CONNECTION", "CONSTRAINT", "CONSTRAINTS", "CONSTRUCTOR", "CONTINUE", "CONVERT_TABLE_HEADER", "CORR", 
      "CORRESPONDING", "COS", "COSH", "COUNT", "COVAR_POP", "COVAR_SAMP", "CREATE", "CROSS", "CS", "CSUM", 
      "CT", "CUBE", "CURRENT", "CURRENT_DATE", "CURRENT_PATH", "CURRENT_ROLE", "CURRENT_TIME", "CURRENT_TIMESTAMP", 
      "CURRENT_USER", "CURSOR", "CV", "CYCLE", "DATA", "DATABASE", "DATABLOCKSIZE", "DATE", "DATEFORM", "DAY", 
      "DEALLOCATE", "DEC", "DECIMAL", "DECLARE", "DEFAULT", "DEFERRABLE", "DEFERRED", "DEGREES", "DEL", "DELETE", 
      "DEPTH", "DEREF", "DESC", "DESCRIBE", "DESCRIPTOR", "DESTROY", "DESTRUCTOR", "DETERMINISTIC", "DIAGNOSTIC", 
      "DIAGNOSTICS", "DICTIONARY", "DISABLED", "DISCONNECT", "DISTINCT", "DO", "DOMAIN", "DOUBLE", "DROP", "DUAL", 
      "DUMP", "DYNAMIC", "EACH", "ECHO", "ELSE", "ELSEIF", "ENABLED", "END", "END-EXEC", "EQ", "EQUALS", "ERROR", 
      "ERRORFILES", "ERRORTABLES", "ESCAPE", "ET", "EVERY", "EXCEPT", "EXCEPTION", "EXEC", "EXECUTE", "EXISTS", 
      "EXIT", "EXP", "EXPLAIN", "EXTERNAL", "EXTRACT", "FALLBACK", "FALSE", "FASTEXPORT", "FETCH", "FIRST", "FLOAT", 
      "FOR", "FOREIGN", "FORMAT", "FOUND", "FREE", "FREESPACE", "FROM", "FULL", "FUNCTION", "GE", "GENERAL", "GENERATED", 
      "GET", "GIVE", "GLOBAL", "GO", "GOTO", "GRANT", "GRAPHIC", "GROUP", "GROUPING", "GT", "HANDLER", "HASH", "HASHAMP", 
      "HASHBAKAMP", "HASHBUCKET", "HASHROW", "HAVING", "HELP", "HOST", "HOUR", "IDENTITY", "IF", "IGNORE", "IMMEDIATE", 
      "IN", "INCONSISTENT", "INDEX", "INDICATOR", "INITIALIZE", "INITIALLY", "INITIATE", "INNER", "INOUT", "INPUT", 
      "INS", "INSERT", "INSTEAD", "INT", "INTEGER", "INTEGERDATE", "INTERSECT", "INTERVAL", "INTO", "IS", "ISOLATION", 
      "ITERATE", "JOIN", "JOURNAL", "KEY", "KURTOSIS", "LANGUAGE", "LARGE", "LAST", "LATERAL", "LE", "LEADING", "LEAVE", 
      "LEFT", "LESS", "LEVEL", "LIKE", "LIMIT", "LN", "LOADING", "LOCAL", "LOCALTIME", "LOCALTIMESTAMP", "LOCATOR", "LOCK", 
      "LOCKING", "LOG", "LOGGING", "LOGON", "LONG", "LOOP", "LOWER", "LT", "MACRO", "MAP", "MATCH", "MAVG", "MAX", "MAXIMUM", 
      "MCHARACTERS", "MDIFF", "MERGE", "MIN", "MINDEX", "MINIMUM", "MINUS", "MINUTE", "MLINREG", "MLOAD", "MOD", "MODE", 
      "MODIFIES", "MODIFY", "MODULE", "MONITOR", "MONRESOURCE", "MONSESSION", "MONTH", "MSUBSTR", "MSUM", "MULTISET", "NAMED", 
      "NAMES", "NATIONAL", "NATURAL", "NCHAR", "NCLOB", "NE", "NEW", "NEW_TABLE", "NEXT", "NO", "NONE", "NOT", "NOWAIT", "NULL", 
      "NULLIF", "NULLIFZERO", "NUMERIC", "OBJECT", "OBJECTS", "OCTET_LENGTH", "OF", "OFF", "OLD", "OLD_TABLE", "ON", "ONLY", 
      "OPEN", "OPERATION", "OPTION", "OR", "ORDER", "ORDINALITY", "OUT", "OUTER", "OUTPUT", "OVER", "OVERLAPS", "OVERRIDE", "PAD", 
      "PARAMETER", "PARAMETERS", "PARTIAL", "PASSWORD", "PATH", "PERCENT", "PERCENT_RANK", "PERM", "PERMANENT", "POSITION", 
      "POSTFIX", "PRECISION", "PREFIX", "PREORDER", "PREPARE", "PRESERVE", "PRIMARY", "PRIOR", "PRIVATE", "PRIVILEGES", "PROCEDURE", 
      "PROFILE", "PROPORTIONAL", "PROTECTION", "PUBLIC", "QUALIFIED", "QUALIFY", "QUANTILE", "RADIANS", "RANDOM", "RANGE_N", "RANK", 
      "READ", "READS", "REAL", "RECURSIVE", "REF", "REFERENCES", "REFERENCING", "REGR_AVGX", "REGR_AVGY", "REGR_COUNT", "REGR_INTERCEPT", 
      "REGR_R2", "REGR_SLOPE", "REGR_SXX", "REGR_SXY", "REGR_SYY", "RELATIVE", "RELEASE", "RENAME", "REPEAT", "REPLACE", "REPLICATION", 
      "REPOVERRIDE", "REQUEST", "RESTART", "RESTORE", "RESTRICT", "RESULT", "RESUME", "RET", "RETRIEVE", "RETURN", "RETURNS", 
      "REVALIDATE", "REVOKE", "RIGHT", "RIGHTS", "ROLE", "ROLLBACK", "ROLLFORWARD", "ROLLUP", "ROUTINE", "ROW", "ROW_NUMBER", 
      "ROWID", "ROWS", "SAMPLE", "SAMPLEID", "SAVEPOINT", "SCHEMA", "SCOPE", "SCROLL", "SEARCH", "SECOND", "SECTION", "SEL", 
      "SELECT", "SEQUENCE", "SESSION", "SESSION_USER", "SET", "SETRESRATE", "SETS", "SETSESSRATE", "SHOW", "SIN", "SINH", "SIZE", 
      "SKEW", "SMALLINT", "SOME", "SOUNDEX", "SPACE", "SPECIFIC", "SPECIFICTYPE", "SPOOL", "SQL", "SQLEXCEPTION", "SQLSTATE", "SQLTEXT", 
      "SQLWARNING", "SQRT", "SS", "START", "STARTUP", "STATE", "STATEMENT", "STATIC", "STATISTICS", "STDDEV_POP", "STDDEV_SAMP", 
      "STEPINFO", "STRING_CS", "STRUCTURE", "SUBSCRIBER", "SUBSTR", "SUBSTRING", "SUM", "SUMMARY", "SUSPEND", "SYSTEM_USER", "TABLE", 
      "TAN", "TANH", "TBL_CS", "TEMPORARY", "TERMINATE", "THAN", "THEN", "THRESHOLD", "TIME", "TIMESTAMP", "TIMEZONE_HOUR", "TIMEZONE_MINUTE", 
      "TITLE", "TO", "TRACE", "TRAILING", "TRANSACTION", "TRANSLATE", "TRANSLATE_CHK", "TRANSLATION", "TREAT", "TRIGGER", "TRIM", 
      "TRUE", "TYPE", "UC", "UNDEFINED", "UNDER", "UNDO", "UNION", "UNIQUE", "UNKNOWN", "UNNEST", "UNTIL", "UPD", "UPDATE", "UPPER", 
      "UPPERCASE", "USAGE", "USER", "USING", "VALUE", "VALUES", "VAR_POP", "VAR_SAMP", "VARBYTE", "VARCHAR", "VARGRAPHIC", "VARIABLE", 
      "VARYING", "VIEW", "VOLATILE", "WAIT", "WHEN", "WHENEVER", "WHERE", "WHILE", "WIDTH_BUCKET", "WITH", "WITHOUT", "WORK", "WRITE", 
      "YEAR", "ZEROIFNULL", "ZONE"};
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.database.dialect.IDatabaseDialect#getSynonymTypes()
   */
  public String[] getSynonymTypes() {
    return new String[] {  };
  }
  
  /**
   * @param tableName The table to be truncated.
   * @return The SQL statement to truncate a table: remove all rows from it without a transaction
   */
  @Override
  public String getTruncateTableStatement(String tableName)
  {
      return "DELETE FROM "+tableName;
  }

  /**
   * @return The extra option separator in database URL for this platform (usually this is semicolon ; )
   */
  public String getExtraOptionSeparator() {
    return ",";
  }

  /**
   * @return The extra option value separator in database URL for this platform (usually this is the equal sign = )
   */
  public String getExtraOptionValueSeparator() {
    return "=";
  }

  /**
   * @return This indicator separates the normal URL from the options
   */
  public String getExtraOptionIndicator() {
    return "/";
  }

  public String getURLWithExtraOptions( IDatabaseConnection connection ) throws DatabaseDialectException {
    StringBuffer url = new StringBuffer( getURL( connection ) );
    if ( supportsOptionsInURL() ) {
      // OK, now add all the options...
      String optionIndicator = getExtraOptionIndicator();
      String optionSeparator = getExtraOptionSeparator();
      String valueSeparator = getExtraOptionValueSeparator();

      Map<String, String> map = connection.getExtraOptions();
      if ( map.size() > 0 ) {
        Map.Entry<String, String> mapEntry = null;
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        boolean first = true;
        while ( it.hasNext() ) {
          mapEntry = it.next();
          String typedParameter = mapEntry.getKey();
          int dotIndex = typedParameter.indexOf( '.' );
          if ( dotIndex >= 0 ) {
            String typeCode = typedParameter.substring( 0, dotIndex );
            String parameter = typedParameter.substring( dotIndex + 1 );
            String value = mapEntry.getValue();

            // Only add to the URL if it's the same database type code...
            //
            if ( connection.getDatabaseType().getShortName().equals( typeCode ) ) {
              // Teradata supports '/' as the first separator ... after the first /xxxx=yyy then you
              // separate the 
              if ( first && url.indexOf( optionIndicator ) == -1 ) { // if first, and it cannot find '/'
                url.append( optionIndicator ); // use the '/' first
              } else {
                url.append( optionSeparator ); // use the ',' thereafter.
              }

              url.append( parameter );
              if ( !isEmpty( value ) ) {
                url.append( valueSeparator ).append( value );
              }
              first = false;
            }
          }
        }
      }
    }
    return url.toString();
  }

}

