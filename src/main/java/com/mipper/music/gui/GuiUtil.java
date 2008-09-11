/**
 * This file contains proprietary information of Rule Financial.
 * Copying or reproduction without prior written approval is prohibited.
 *
 * <b>Copyright</b> (c)2008
 * <b>Company</b> Rule Financial Ltd
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
 * @version $Revision: $
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
