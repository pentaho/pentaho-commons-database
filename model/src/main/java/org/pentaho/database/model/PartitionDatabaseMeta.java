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

package org.pentaho.database.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class to contain the information needed to parition (cluster): id, hostname, port, database
 * 
 * @author Matt
 * 
 */

@XmlRootElement
public class PartitionDatabaseMeta implements Serializable {

  private static final long serialVersionUID = -4252906914407231458L;

  String partitionId;

  String hostname;
  String port;
  String databaseName;
  String username;
  String password;

  public PartitionDatabaseMeta() {
  }

  /**
   * @param partitionId
   * @param hostname
   * @param port
   * @param database
   */
  public PartitionDatabaseMeta( String partitionId, String hostname, String port, String database ) {
    super();

    this.partitionId = partitionId;
    this.hostname = hostname;
    this.port = port;
    this.databaseName = database;
  }

  /**
   * @return the partitionId
   */
  public String getPartitionId() {
    return partitionId;
  }

  /**
   * @param partitionId
   *          the partitionId to set
   */
  public void setPartitionId( String partitionId ) {
    this.partitionId = partitionId;
  }

  /**
   * @return the database
   */
  public String getDatabaseName() {
    return databaseName;
  }

  /**
   * @param database
   *          the database to set
   */
  public void setDatabaseName( String database ) {
    this.databaseName = database;
  }

  /**
   * @return the hostname
   */
  public String getHostname() {
    return hostname;
  }

  /**
   * @param hostname
   *          the hostname to set
   */
  public void setHostname( String hostname ) {
    this.hostname = hostname;
  }

  /**
   * @return the port
   */
  public String getPort() {
    return port;
  }

  /**
   * @param port
   *          the port to set
   */
  public void setPort( String port ) {
    this.port = port;
  }

  /**
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  /**
   * @param password
   *          the password to set
   */
  public void setPassword( String password ) {
    this.password = password;
  }

  /**
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * @param username
   *          the username to set
   */
  public void setUsername( String username ) {
    this.username = username;
  }

}
