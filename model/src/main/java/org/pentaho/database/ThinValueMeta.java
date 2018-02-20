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

package org.pentaho.database;

public class ThinValueMeta implements IValueMeta {

  private String name;
  private int type;
  private int length;
  private int precision;

  public ThinValueMeta() {
    this( null, IValueMeta.TYPE_NONE, -1, -1 );
  }

  public ThinValueMeta( String name ) {
    this( name, IValueMeta.TYPE_NONE, -1, -1 );
  }

  public ThinValueMeta( String name, int type ) {
    this( name, type, -1, -1 );
  }

  public ThinValueMeta( String name, int type, int length, int precision ) {
    this.name = name;
    this.type = type;
    this.length = length;
    this.precision = precision;
  }

  /**
   * @return the length
   */
  public int getLength() {
    return length;
  }

  /**
   * @param length
   *          the length to set
   */
  public void setLength( int length ) {
    this.length = length;
  }

  /**
   * @param length
   *          the length to set
   */
  public void setLength( int length, int precision ) {
    this.length = length;
    this.precision = precision;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name
   *          the name to set
   */
  public void setName( String name ) {
    this.name = name;
  }

  /**
   * @return the precision
   */
  public int getPrecision() {
    // For backward compatibility we need to tweak a bit...
    //
    if ( isInteger() || isBinary() ) {
      return 0;
    }
    if ( isString() || isBoolean() ) {
      return -1;
    }

    return precision;
  }

  /**
   * @param precision
   *          the precision to set
   */
  public void setPrecision( int precision ) {
    this.precision = precision;
  }

  /**
   * @return the type
   */
  public int getType() {
    return type;
  }

  /**
   * @param type
   *          the type to set
   */
  public void setType( int type ) {
    this.type = type;
  }

  /**
   * Checks whether or not the value is a String.
   * 
   * @return true if the value is a String.
   */
  public boolean isString() {
    return type == TYPE_STRING;
  }

  /**
   * Checks whether or not this value is a boolean
   * 
   * @return true if this value has type boolean.
   */
  public boolean isBoolean() {
    return type == TYPE_BOOLEAN;
  }

  /**
   * Checks whether or not this value is of type Binary
   * 
   * @return true if this value has type Binary
   */
  public boolean isBinary() {
    return type == TYPE_BINARY;
  }

  /**
   * Checks whether or not this value is an Integer
   * 
   * @return true if this value is an integer
   */
  public boolean isInteger() {
    return type == TYPE_INTEGER;
  }

  public IValueMeta clone() {
    try {
      return (IValueMeta) super.clone();
    } catch ( Exception e ) {
      return null;
    }
  }
}
