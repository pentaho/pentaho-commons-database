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
