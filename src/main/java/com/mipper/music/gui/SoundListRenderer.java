/*
 * Tete Ear Trainer Copyright (C) 2005 Cliff Evans
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

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;


/**
 * ListCellRenderer which renders a specified cell in a different colour than
 * the selected or unselected cells.
 *
 * @author Cliff Evans
 * @version $Revision: 1.1 $
 */
public class SoundListRenderer extends JLabel
  implements
    ListCellRenderer
{

  /**
   * Constructor.
   */
  public SoundListRenderer ()
  {
    super ();
  }


  /**
   * Constructor.
   *
   * @param image
   */
  public SoundListRenderer ( Icon image )
  {
    super ( image );
  }


  /**
   * Constructor.
   *
   * @param image
   * @param horizontalAlignment
   */
  public SoundListRenderer ( Icon image, int horizontalAlignment )
  {
    super ( image, horizontalAlignment );
  }


  /**
   * Constructor.
   *
   * @param text
   */
  public SoundListRenderer ( String text )
  {
    super ( text );
  }


  /**
   * Constructor.
   *
   * @param text
   * @param icon
   * @param horizontalAlignment
   */
  public SoundListRenderer ( String text, Icon icon, int horizontalAlignment )
  {
    super ( text, icon, horizontalAlignment );
  }


  /**
   * Constructor.
   *
   * @param text
   * @param horizontalAlignment
   */
  public SoundListRenderer ( String text, int horizontalAlignment )
  {
    super ( text, horizontalAlignment );
  }


  /**
   * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
   */
  public Component getListCellRendererComponent ( JList list,
                                                  Object value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus )
  {
    final String s = value.toString ();
    setText ( s );
    if ( isSelected )
    {
      if ( list instanceof JSoundList && ( ( JSoundList ) list ).getPlaying ().equals ( s ) )
      {
        setBackground ( ( ( JSoundList ) list ).getPlayingBackground () );
        setForeground ( ( ( JSoundList ) list ).getPlayingForeground () );
      }
      else
      {
        setBackground ( list.getSelectionBackground () );
        setForeground ( list.getSelectionForeground () );
      }
    }
    else
    {
      setBackground ( list.getBackground () );
      setForeground ( list.getForeground () );
    }
    setEnabled ( list.isEnabled () );
    setFont ( list.getFont () );
    setOpaque ( true );
    return this;
  }


  /**
   *
   */
  private static final long serialVersionUID = 1L;

}
