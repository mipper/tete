/**
 * This file contains proprietary information of Rule Financial. 
 * Copying or reproduction without prior written approval is prohibited. 
 *
 * <b>Copyright</b> (c)2008
 * <b>Company</b> Rule Financial Ltd
 */
package com.mipper.music.gui;

import java.net.URL;
import java.util.ResourceBundle;


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
  public static String readProperty ( String property )
  {
    return ResourceBundle.getBundle ( "tete" )
                         .getString ( property );
  }


  /**
   * @param path Path to the resource to get.
   * 
   * @return URL pointing to the specified resource.
   */
  public static URL getResourceUrl ( String path )
  {
    return GuiUtil.class.getResource ( path );
  }
  
}
