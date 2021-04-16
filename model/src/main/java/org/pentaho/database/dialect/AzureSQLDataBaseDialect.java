package org.pentaho.database.dialect;

import org.pentaho.database.DatabaseDialectException;
import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.DatabaseType;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.model.IDatabaseType;

public class AzureSQLDataBaseDialect extends MSSQLServerDatabaseDialect {

  private static final long serialVersionUID = -4148186495934841696L;
  static final String JDBC_AUTH_METHOD = "jdbcAuthMethod";
  static final String IS_ALWAYS_ENCRYPTION_ENABLED = "azureAlwaysEncryptionEnabled";
  static final String CLIENT_ID = "azureClientSecretId";
  static final String CLIENT_SECRET_KEY = "azureClientSecretKey";

  public static final String SQL_AUTHENTICATION = "SQL Server Authentication";
  public static final String ACTIVE_DIRECTORY_PASSWORD = "Azure Active Directory - Password";
  public static final String ACTIVE_DIRECTORY_MFA = "Azure Active Directory - Universal With MFA";
  public static final String ACTIVE_DIRECTORY_INTEGRATED = "Azure Active Directory - Integrated";

  private static final IDatabaseType DBTYPE = new DatabaseType( "Azure SQL DB", "AZURESQLDB",
            DatabaseAccessType.getList(
                    DatabaseAccessType.NATIVE,
                    DatabaseAccessType.JNDI ), 1433,
            null );

  public AzureSQLDataBaseDialect() {
    //Default Constructor for initialization
  }

  @Override
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
  public String getURL( IDatabaseConnection connection ) throws DatabaseDialectException {
    String url = "jdbc:sqlserver://" + connection.getHostname() + ":" + connection.getDatabasePort() + ";database=" + connection.getDatabaseName() + ";encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
    Object value1 = connection.getAttributes().get( IS_ALWAYS_ENCRYPTION_ENABLED );
    Object value2 = connection.getAttributes().get( JDBC_AUTH_METHOD );
    String isAlwaysEncryptionEnabled = "";
    String jdbcAuthMethod = "";
    if ( value1 instanceof String ) {
      isAlwaysEncryptionEnabled = (String) value1;
    }

    if ( value2 instanceof String ) {
      jdbcAuthMethod = (String) value2;
    }

    if ( isAlwaysEncryptionEnabled.equals( "true" ) ) {
      Object value3 = connection.getAttributes().get( CLIENT_ID );
      Object value4 = connection.getAttributes().get( CLIENT_SECRET_KEY );
      String clientId = "";
      String clientKey = "";
      if ( value3 instanceof String ) {
        clientId = (String) value3;
      }

      if ( value4 instanceof String ) {
        clientKey = (String) value4;
      }
      url += "columnEncryptionSetting=Enabled;keyVaultProviderClientId=" + clientId + ";keyVaultProviderClientKey=" + clientKey + ";";
    }

    if ( ACTIVE_DIRECTORY_PASSWORD.equals( jdbcAuthMethod ) ) {
      return url + "authentication=ActiveDirectoryPassword;";
    } else if ( ACTIVE_DIRECTORY_MFA.equals( jdbcAuthMethod ) ) {
      return url + "authentication=ActiveDirectoryInteractive;";
    } else if ( ACTIVE_DIRECTORY_INTEGRATED.equals( jdbcAuthMethod ) ) {
      return url + "Authentication=ActiveDirectoryIntegrated;";
    } else {
      return url;
    }
  }


  @Override
  public String[] getUsedLibraries() {
    return new String[] { "mssql-jdbc-9.2.1.jre8.jar" };
  }
}
