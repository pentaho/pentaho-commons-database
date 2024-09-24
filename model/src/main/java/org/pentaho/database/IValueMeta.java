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

package org.pentaho.database;

public interface IValueMeta extends Cloneable {
  /** Value type indicating that the value has no type set */
  public static final int TYPE_NONE = 0;

  /** Value type indicating that the value contains a floating point double precision number. */
  public static final int TYPE_NUMBER = 1;

  /** Value type indicating that the value contains a text String. */
  public static final int TYPE_STRING = 2;

  /** Value type indicating that the value contains a Date. */
  public static final int TYPE_DATE = 3;

  /** Value type indicating that the value contains a boolean. */
  public static final int TYPE_BOOLEAN = 4;

  /** Value type indicating that the value contains a long integer. */
  public static final int TYPE_INTEGER = 5;

  /** Value type indicating that the value contains a floating point precision number with arbitrary precision. */
  public static final int TYPE_BIGNUMBER = 6;

  /** Value type indicating that the value contains an Object. */
  public static final int TYPE_SERIALIZABLE = 7;

  /** Value type indicating that the value contains binary data: BLOB, CLOB, ... */
  public static final int TYPE_BINARY = 8;

  public String getName();

  public void setName( String name );

  public int getLength();

  public void setLength( int length );

  public int getPrecision();

  public void setPrecision( int precision );

  public void setLength( int length, int precision );

  public int getType();

  public void setType( int type );

  /**
   * @return a copy of this value meta object
   */
  public IValueMeta clone();
}
