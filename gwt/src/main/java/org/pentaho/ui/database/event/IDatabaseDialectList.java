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


package org.pentaho.ui.database.event;

import java.util.List;

import org.pentaho.database.IDatabaseDialect;

/**
 * @author wseyler
 *
 */
public interface IDatabaseDialectList {
  public List<IDatabaseDialect> getDialects();
  public void setDialects(List<IDatabaseDialect> dialects);
}
