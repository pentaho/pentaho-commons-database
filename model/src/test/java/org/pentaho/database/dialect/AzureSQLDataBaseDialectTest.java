package org.pentaho.database.dialect;

import junit.framework.TestCase;
import org.junit.Test;
import org.pentaho.database.DatabaseDialectException;
import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.DatabaseConnection;
import org.pentaho.database.model.IDatabaseType;

import java.util.HashMap;
import java.util.Map;

import static org.pentaho.database.dialect.AzureSQLDataBaseDialect.ACTIVE_DIRECTORY_INTEGRATED;
import static org.pentaho.database.dialect.AzureSQLDataBaseDialect.ACTIVE_DIRECTORY_MFA;
import static org.pentaho.database.dialect.AzureSQLDataBaseDialect.ACTIVE_DIRECTORY_PASSWORD;
import static org.pentaho.database.dialect.AzureSQLDataBaseDialect.JDBC_AUTH_METHOD;
import static org.pentaho.database.dialect.AzureSQLDataBaseDialect.IS_ALWAYS_ENCRYPTION_ENABLED;
import static org.pentaho.database.dialect.AzureSQLDataBaseDialect.CLIENT_ID;
import static org.pentaho.database.dialect.AzureSQLDataBaseDialect.CLIENT_SECRET_KEY;

public class AzureSQLDataBaseDialectTest extends TestCase {
    private AzureSQLDataBaseDialect dialect;

    public AzureSQLDataBaseDialectTest() {
        this.dialect = new AzureSQLDataBaseDialect();
    }

    @Test
    public void testGetNativeDriver() {
        assertEquals( dialect.getNativeDriver(), "com.microsoft.sqlserver.jdbc.SQLServerDriver" );
    }

    @Test
    public void testGetDatabaseType() {
        IDatabaseType dbType = dialect.getDatabaseType();
        assertEquals( "Azure SQL DB", dbType.getName() );
        assertEquals( "[NATIVE, JNDI]", dbType.getSupportedAccessTypes().toString() );
    }


    @Test
    public void testGetNativeJdbcPre() {
        assertEquals( dialect.getNativeJdbcPre(), "jdbc:sqlserver://" );
    }

    @Test
    public void testGetUsedLibraries() {
        assertEquals( "mssql-jdbc-9.2.1.jre8.jar", dialect.getUsedLibraries()[0] );
    }

    @Test
    public void testAlwaysEncryption() throws DatabaseDialectException {
        DatabaseConnection conn = new DatabaseConnection();
        conn.setAccessType( DatabaseAccessType.NATIVE );
        conn.setHostname( "hitachi.database.windows.net" );
        conn.setDatabaseName( "azuredb" );
        conn.setDatabasePort( "1433" );
        Map<String, String> attributes = new HashMap<>();
        attributes.put( IS_ALWAYS_ENCRYPTION_ENABLED, "true" );
        attributes.put( CLIENT_ID, "azure" );
        attributes.put( CLIENT_SECRET_KEY, "xxx" );
        conn.setAttributes( attributes );
        assertEquals( "jdbc:sqlserver://hitachi.database.windows.net:1433;database=azuredb;encrypt=true;trustServerCertificate=false;" +
                        "hostNameInCertificate=*.database.windows.net;loginTimeout=30;columnEncryptionSetting=Enabled;keyVaultProviderClientId=azure;keyVaultProviderClientKey=xxx;",
                dialect.getURL( conn ) );
    }

    @Test
    public void testSqlAuth() throws DatabaseDialectException {
        DatabaseConnection conn = new DatabaseConnection();
        conn.setAccessType( DatabaseAccessType.NATIVE );
        conn.setHostname( "hitachi.database.windows.net" );
        conn.setDatabaseName( "azuredb" );
        conn.setDatabasePort( "1433" );
        Map<String, String> attributes = new HashMap<>();
        attributes.put( IS_ALWAYS_ENCRYPTION_ENABLED, "false" );
        conn.setAttributes( attributes );
        assertEquals( "jdbc:sqlserver://hitachi.database.windows.net:1433;database=azuredb;encrypt=true;trustServerCertificate=false;" +
                        "hostNameInCertificate=*.database.windows.net;loginTimeout=30;", dialect.getURL( conn ) );

    }

    @Test
    public void testAadPasswordAuth() throws DatabaseDialectException {
        DatabaseConnection conn = new DatabaseConnection();
        conn.setAccessType( DatabaseAccessType.NATIVE );
        conn.setHostname( "hitachi.database.windows.net" );
        conn.setDatabaseName( "azuredb" );
        conn.setDatabasePort( "1433" );
        Map<String, String> attributes = new HashMap<>();
        attributes.put( IS_ALWAYS_ENCRYPTION_ENABLED, "false" );
        attributes.put( JDBC_AUTH_METHOD, ACTIVE_DIRECTORY_PASSWORD );
        conn.setAttributes( attributes );
        assertEquals( "jdbc:sqlserver://hitachi.database.windows.net:1433;database=azuredb;encrypt=true;trustServerCertificate=false;" +
                "hostNameInCertificate=*.database.windows.net;loginTimeout=30;authentication=ActiveDirectoryPassword;", dialect.getURL( conn ) );

    }

    @Test
    public void testMfaAuth() throws DatabaseDialectException {
        DatabaseConnection conn = new DatabaseConnection();
        conn.setAccessType( DatabaseAccessType.NATIVE );
        conn.setHostname( "hitachi.database.windows.net" );
        conn.setDatabaseName( "azuredb" );
        conn.setDatabasePort( "1433" );
        Map<String, String> attributes = new HashMap<>();
        attributes.put( IS_ALWAYS_ENCRYPTION_ENABLED, "false" );
        attributes.put( JDBC_AUTH_METHOD, ACTIVE_DIRECTORY_MFA );
        conn.setAttributes( attributes );
        assertEquals("jdbc:sqlserver://hitachi.database.windows.net:1433;database=azuredb;encrypt=true;trustServerCertificate=false;" +
                "hostNameInCertificate=*.database.windows.net;loginTimeout=30;authentication=ActiveDirectoryInteractive;", dialect.getURL( conn ) );


    }

    @Test
    public void testAadIntegrated() throws DatabaseDialectException {
        DatabaseConnection conn = new DatabaseConnection();
        conn.setAccessType( DatabaseAccessType.NATIVE );
        conn.setHostname( "hitachi.database.windows.net" );
        conn.setDatabaseName( "azuredb" );
        conn.setDatabasePort( "1433" );
        Map<String, String> attributes = new HashMap<>();
        attributes.put( IS_ALWAYS_ENCRYPTION_ENABLED, "false" );
        attributes.put( JDBC_AUTH_METHOD, ACTIVE_DIRECTORY_INTEGRATED );
        conn.setAttributes( attributes );
        assertEquals( "jdbc:sqlserver://hitachi.database.windows.net:1433;database=azuredb;encrypt=true;trustServerCertificate=false;" +
                "hostNameInCertificate=*.database.windows.net;loginTimeout=30;Authentication=ActiveDirectoryIntegrated;", dialect.getURL( conn ) );
    }

    @Test
    public void testAlwaysEncryptionWithAadAuth() throws DatabaseDialectException {
        DatabaseConnection conn = new DatabaseConnection();
        conn.setAccessType( DatabaseAccessType.NATIVE );
        conn.setHostname( "hitachi.database.windows.net" );
        conn.setDatabaseName( "azuredb" );
        conn.setDatabasePort( "1433" );
        Map<String, String> attributes = new HashMap<>();
        attributes.put( JDBC_AUTH_METHOD, ACTIVE_DIRECTORY_PASSWORD );
        attributes.put( IS_ALWAYS_ENCRYPTION_ENABLED, "true" );
        attributes.put( CLIENT_ID, "azure" );
        attributes.put( CLIENT_SECRET_KEY, "xxx" );
        conn.setAttributes( attributes );
        assertEquals("jdbc:sqlserver://hitachi.database.windows.net:1433;database=azuredb;encrypt=true;trustServerCertificate=false;" +
                        "hostNameInCertificate=*.database.windows.net;loginTimeout=30;columnEncryptionSetting=Enabled;keyVaultProviderClientId=azure;keyVaultProviderClientKey=xxx;" +
                        "authentication=ActiveDirectoryPassword;", dialect.getURL( conn ) );

    }

}