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

public class DatabaseDialectException extends Exception {
  private static final long serialVersionUID = -2260895195255402040L;
  /**
   * CR: operating systems specific Carriage Return
   */
  public static final String CR = "\r\n"; //$NON-NLS-1$

  /**
   * Constructs a new throwable with null as its detail message.
   */
  public DatabaseDialectException() {
    super();
  }

  /**
   * Constructs a new throwable with the specified detail message.
   * 
   * @param message
   *          - the detail message. The detail message is saved for later retrieval by the getMessage() method.
   */
  public DatabaseDialectException( String message ) {
    super( message );
  }

  /**
   * Constructs a new throwable with the specified cause and a detail message of (cause==null ? null : cause.toString())
   * (which typically contains the class and detail message of cause).
   * 
   * @param cause
   *          the cause (which is saved for later retrieval by the getCause() method). (A null value is permitted, and
   *          indicates that the cause is nonexistent or unknown.)
   */
  public DatabaseDialectException( Throwable cause ) {
    super( cause );
  }

  /**
   * Constructs a new throwable with the specified detail message and cause.
   * 
   * @param message
   *          the detail message (which is saved for later retrieval by the getMessage() method).
   * @param cause
   *          the cause (which is saved for later retrieval by the getCause() method). (A null value is permitted, and
   *          indicates that the cause is nonexistent or unknown.)
   */
  public DatabaseDialectException( String message, Throwable cause ) {
    super( message, cause );
  }

  /**
   * get the messages back to it's origin cause.
   */
  public String getMessage() {
    String retval = CR;
    retval += super.getMessage() + CR;

    Throwable cause = getCause();
    if ( cause != null ) {
      String message = cause.getMessage();
      if ( message != null ) {
        retval += message + CR;
      } else {
        // Add with stack trace elements of cause...
        StackTraceElement[] ste = cause.getStackTrace();
        for ( int i = ste.length - 1; i >= 0; i-- ) {
          retval +=
              " at " + ste[i].getClassName() + "." + ste[i].getMethodName() + " (" + ste[i].getFileName() + ":"
                  + ste[i].getLineNumber() + ")" + CR;
        }
      }
    }

    return retval;
  }

  public String getSuperMessage() {
    return super.getMessage();
  }

}
