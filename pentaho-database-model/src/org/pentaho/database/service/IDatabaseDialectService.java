package org.pentaho.database.service;

import java.util.List;

import org.pentaho.database.IDatabaseDialect;
import org.pentaho.database.model.IDatabaseConnection;
import org.pentaho.database.model.IDatabaseType;

public interface IDatabaseDialectService {

  public void registerDatabaseDialect(IDatabaseDialect databaseDialect);

  public void registerDatabaseDialect(IDatabaseDialect databaseDialect, boolean validateClassExists);
  
  public IDatabaseDialect getDialect(IDatabaseType databaseType);
  
  public IDatabaseDialect getDialect(IDatabaseConnection connection);
  
  public List<IDatabaseDialect> getDatabaseDialects();
  
  public List<IDatabaseType> getDatabaseTypes();
  
  public boolean validateJdbcDriverClassExists(String classname);

}
