/*
 * Tete Ear Trainer Copyright (C) 2005-8 Cliff Evans
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.mipper.music.gui;

import java.awt.Window;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import com.mipper.util.Logger;


/**
 * Utility methods for Gui code.
 *
 * @author Cliff Evans
 * @version $Id$
 */
public class GuiUtil
{

  /**
   * Utility method for reading properties from the tete property bundle.
   *
   * @param property Key of the property to read.
   *
   * @return The value of the property.
   */
  public static String readProperty ( final String property )
  {
    return ResourceBundle.getBundle ( "tete" )
                         .getString ( property );
  }


  /**
   * Logs the exception and shows a message to the user.
   *
   * @param parent Parent window.  Dialoge will be shown centred on this.
   * @param e Exception causing the error.
   */
  public static void handleException ( final Window parent, final Exception e )
  {
    Logger.error ( e );
    JOptionPane.showMessageDialog ( parent,
                                    e.getMessage (),
                                    readProperty ( "title.error" ),
                                    JOptionPane.ERROR_MESSAGE );
  }


  /**
   * @param path Path to the resource to get.
   *
   * @return URL pointing to the specified resource.
   */
  public static URL getResourceUrl ( final String path )
  {
    return GuiUtil.class.getResource ( path );
  }

}
