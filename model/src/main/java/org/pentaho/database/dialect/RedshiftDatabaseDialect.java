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

import org.pentaho.database.model.DatabaseAccessType;
import org.pentaho.database.model.DatabaseType;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.model.IDatabaseType;

import java.util.Arrays;
import java.util.Map;

public class RedshiftDatabaseDialect extends PostgreSQLDatabaseDialect {

  private static final long serialVersionUID = 7855404769773045690L;

  private static final IDatabaseType DBTYPE = new DatabaseType( "Redshift", "REDSHIFT", DatabaseAccessType.getList(
      DatabaseAccessType.NATIVE, DatabaseAccessType.ODBC, DatabaseAccessType.JNDI ), 5439,
      "http://http://docs.aws.amazon.com/redshift/latest/mgmt/configure-jdbc-connection.html" );

  static final String JDBC_AUTH_METHOD = "jdbcAuthMethod";
  static final String PROFILE_CREDENTIALS = "Profile";
  static final String IAM_CREDENTIALS = "IAM Credentials";
  static final String IAM_ACCESS_KEY_ID = "iamAccessKeyId";
  static final String IAM_SECRET_ACCESS_KEY = "iamSecretAccessKey";
  static final String IAM_SESSION_TOKEN = "iamSessionToken";
  static final String IAM_PROFILE_NAME = "iamProfileName";

  @Override
  public IDatabaseType getDatabaseType() {
    return DBTYPE;
  }

  @Override
  public String getNativeDriver() {
    return "com.amazon.redshift.jdbc.Driver";
  }

  @Override
  public String getNativeJdbcPre() {
    return "jdbc:redshift://";
  }

  @Override
  public String[] getUsedLibraries() {
    return new String[] { "RedshiftJDBC4_1.0.10.1010.jar" };
  }

  @Override public String getURL( IDatabaseConnection connection ) {
    if ( connection.getAccessType() == DatabaseAccessType.ODBC ) {
      return "jdbc:odbc:" + connection.getDatabaseName();
    } else {
      if ( Arrays.asList( PROFILE_CREDENTIALS, IAM_CREDENTIALS )
        .contains( connection.getAttributes().get( JDBC_AUTH_METHOD ) ) ) {
        return "jdbc:redshift:iam://" + endOfUrl( connection );
      } else {
        return "jdbc:redshift://" + endOfUrl( connection );
      }
    }
  }

  @Override protected void putOptionalOptions( IDatabaseConnection connection, Map<String, String> map ) {
    if ( IAM_CREDENTIALS.equals( connection.getAttributes().get( JDBC_AUTH_METHOD ) ) ) {
      map.put( "REDSHIFT.AccessKeyID", connection.getAttributes().get( IAM_ACCESS_KEY_ID ) );
      map.put( "REDSHIFT.SecretAccessKey", connection.getAttributes().get( IAM_SECRET_ACCESS_KEY ) );
      map.put( "REDSHIFT.SessionToken", connection.getAttributes().get( IAM_SESSION_TOKEN ) );
    } else if ( PROFILE_CREDENTIALS.equals( connection.getAttributes().get( JDBC_AUTH_METHOD ) ) ) {
      map.put( "REDSHIFT.Profile", connection.getAttributes().get( IAM_PROFILE_NAME ) );
    }
  }

  private String endOfUrl( IDatabaseConnection connection ) {
    return connection.getHostname() + ":" + connection.getDatabasePort() + "/" + connection.getDatabaseName();
  }

}
