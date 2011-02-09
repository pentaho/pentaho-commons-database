package org.pentaho.database.dialect;

import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.DatabaseConnection;
import org.pentaho.database.model.DatabaseType;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.model.IDatabaseType;

public class MSSQLServerNativeDatabaseDialect extends MSSQLServerDatabaseDialect {

  // TODO: Implement Integrated Security configuration
  public static final String ATTRIBUTE_USE_INTEGRATED_SECURITY = "MSSQLUseIntegratedSecurity"; //$NON-NLS-1$

  
  private static final IDatabaseType DBTYPE = 
    new DatabaseType(
        "MS SQL Server (Native)",
        "MSSQLNative",
        DatabaseAccessType.getList(
            DatabaseAccessType.NATIVE, 
            DatabaseAccessType.ODBC, 
            DatabaseAccessType.JNDI
        ), 
        1433, 
        "http://msdn.microsoft.com/en-us/library/ms378428.aspx"
    );
  
  public IDatabaseType getDatabaseType() {
    return DBTYPE;
  }

  @Override
  public String getNativeDriver() {
    return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
  }
  
  @Override
  public String getNativeJdbcPre() {
    return "jdbc:sqlserver://";
  }

  @Override
  public String getURL(IDatabaseConnection connection)
    {
    if (connection.getAccessType()==DatabaseAccessType.ODBC) {
      return "jdbc:odbc:"+connection.getDatabaseName();
    }
    else
    {
      /*
      String useIntegratedSecurity = null;
      Object value = connection.getAttributes().get(ATTRIBUTE_USE_INTEGRATED_SECURITY);
      if(value != null && value instanceof String) {
        useIntegratedSecurity = (String) value;
        // Check if the String can be parsed into a boolean
        try {
            Boolean.parseBoolean(useIntegratedSecurity);
        } catch (IllegalArgumentException e) {
          useIntegratedSecurity = "false";//$NON-NLS-1$
        }
      }
      */
      //  +";integratedSecurity="+useIntegratedSecurity;
      return getNativeJdbcPre() + connection.getHostname()+":"+connection.getDatabasePort()+";databaseName="+connection.getDatabaseName();
    }
  }
  
  @Override
  public IDatabaseConnection createNativeConnection(String jdbcUrl) {
    if (!jdbcUrl.startsWith(getNativeJdbcPre())) {
      throw new RuntimeException("JDBC URL " + jdbcUrl + " does not start with " + getNativeJdbcPre());
    }
    DatabaseConnection dbconn = new DatabaseConnection();
    dbconn.setDatabaseType(getDatabaseType());
    dbconn.setAccessType(DatabaseAccessType.NATIVE);
    String str = jdbcUrl.substring(getNativeJdbcPre().length());
    String hostname = null;
    String port = null;
    String databaseNameAndParams = null;
    
    // hostname:port;databaseName=dbname;integratedSecurity=security

    // hostname:port
    // hostname/dbname
    // dbname

    if (str.indexOf(":") >= 0) {
      hostname = str.substring(0, str.indexOf(":"));
      str = str.substring(str.indexOf(":") + 1);
      if (str.indexOf(getExtraOptionIndicator()) >= 0) {
        port = str.substring(0, str.indexOf(";")); 
        databaseNameAndParams = str.substring(str.indexOf(getExtraOptionIndicator())+1);
      } else {
        port = str;
      }
    } else {
      if (str.indexOf(getExtraOptionIndicator()) >= 0) {
        hostname = str.substring(0, str.indexOf(getExtraOptionIndicator()));
        databaseNameAndParams = str.substring(str.indexOf(getExtraOptionIndicator())+1);
      } else {
        hostname = str;
      }
    }
    if (hostname != null) {
      dbconn.setHostname(hostname);
    }
    if (port != null) {
      dbconn.setDatabasePort(port);
    }
    
    // parse parameters out of URL
    if (databaseNameAndParams != null) {
      setDatabaseNameAndParams(dbconn, databaseNameAndParams);
    }
    return dbconn;
  }
  
  @Override
  protected void setDatabaseNameAndParams(DatabaseConnection dbconn, String databaseNameAndParams) {
    String paramData[] = databaseNameAndParams.split(getExtraOptionSeparator());
    for (String param : paramData) {
      String nameAndValue[] = param.split(getExtraOptionValueSeparator());
      if (nameAndValue[0] != null && nameAndValue[0].trim().length() > 0) {
        if (nameAndValue.length == 1) {
          if (nameAndValue[0].equals("databaseName")) {
            dbconn.setDatabaseName(""); 
          } else {
            dbconn.addExtraOption(dbconn.getDatabaseType().getShortName(), nameAndValue[0], "");
          }
        } else {
          if (nameAndValue[0].equals("databaseName")) {
            dbconn.setDatabaseName(nameAndValue[1]); 
          } else {
            dbconn.addExtraOption(dbconn.getDatabaseType().getShortName(), nameAndValue[0], nameAndValue[1]);
          }
        }
      }
    }
    
  }

  
  @Override
  public String[] getUsedLibraries()
  {
      return new String[] { "sqljdbc.jar" };
  }
  
  
}
