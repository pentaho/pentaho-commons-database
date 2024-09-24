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

///////////////////////////////////////////////////////// 
// Bare Bones Browser Launch                           // 
// Version 1.5 (December 10, 2005)                     // 
// By Dem Pilafian                                     // 
// Supports: Mac OS X, GNU/Linux, Unix, Windows XP     // 
// Example Usage:                                      // 
// String url = "http://www.centerkey.com/";           // 
// BareBonesBrowserLaunch.openURL(url);                // 
// Public Domain Software -- Free to Use as You Like   // 
/////////////////////////////////////////////////////////
package org.pentaho.ui.util;

import java.lang.reflect.Method;

import org.pentaho.ui.database.event.ILaunch;
import org.pentaho.ui.database.event.IMessages;

public class Launch implements ILaunch {

  public Status openUrl(String url, IMessages messages) {

    Status r = Status.Success;
    String osName = System.getProperty("os.name"); //$NON-NLS-1$

    try {
      if (osName.startsWith("Mac OS")) { //$NON-NLS-1$
        Class <?> fileMgr = Class.forName("com.apple.eio.FileManager"); //$NON-NLS-1$
        Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[] { String.class }); //$NON-NLS-1$
        openURL.invoke(null, new Object[] { url });
      } else if (osName.startsWith("Windows")){ //$NON-NLS-1$
        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url); //$NON-NLS-1$
      } else { //assume Unix or Linux
        String[] browsers = { "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
        String browser = null;
        for (int count = 0; count < browsers.length && browser == null; count++){
          if (Runtime.getRuntime().exec(new String[] { "which", browsers[count] }).waitFor() == 0){ //$NON-NLS-1$
            browser = browsers[count];
          }
        }
        if (browser == null){
          throw new Exception(messages.getString("Launch.ERROR_0001_BROWSER_NOT_FOUND")); //$NON-NLS-1$
        }else{
          Runtime.getRuntime().exec(new String[] { browser, url });
        }
      }
    } catch (Exception e) {
      r = Status.Failed;
    }
    return r;
  }

}
