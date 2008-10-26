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

import java.awt.Color;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.ListModel;


/**
 * Swing list which allows an item in the list to be displayed in a different
 * colour than unselected and selected items.
 *
 * @author Cliff Evans
 * @version $Id$
 */
public class JSoundList extends JList
{

  /**
   * Constructor.
   *
   */
  public JSoundList ()
  {
    super ();
  }
  /**
   * Constructor.
   *
   * @param dataModel
   */
  public JSoundList ( ListModel dataModel )
  {
    super ( dataModel );
  }


  /**
   * Constructor.
   *
   * @param listData
   */
  public JSoundList ( Object[] listData )
  {
    super ( listData );
  }


  /**
   * Constructor.
   *
   * @param listData
   */
  public JSoundList ( Vector<?> listData )
  {
    super ( listData );
  }


  /**
   * Clears the name of the sound being played.
   */
  public void clearPlaying ()
  {
    setPlaying ( "" );
  }


  /**
   * Gets the name of the sound being played.
   *
   * @return Name of the sound being played.
   */
  public String getPlaying ()
  {
    return _playing;
  }


  /**
   * @return Returns the playingBkgnd.
   */
  public Color getPlayingBackground ()
  {
    return _playingBkgnd;
  }


  /**
   * @return Returns the playingFrgnd.
   */
  public Color getPlayingForeground ()
  {
    return _playingFrgnd;
  }


  /**
   * Sets the name of the sound being played.
   *
   * @param playing Name of the sound being played.
   */
  public void setPlaying ( String playing )
  {
    _playing = playing;
    repaint ();
  }



  /**
   * @param playingBkgnd The playingBkgnd to set.
   */
  public void setPlayingBackground ( Color playingBkgnd )
  {
    _playingBkgnd = playingBkgnd;
  }



  /**
   * @param playingFrgnd The playingFrgnd to set.
   */
  public void setPlayingForeground ( Color playingFrgnd )
  {
    _playingFrgnd = playingFrgnd;
  }



  /**
   *
   */
  private static final long serialVersionUID = 1L;


  private String _playing = "";
  private Color _playingBkgnd = Color.RED;
  private Color _playingFrgnd = Color.WHITE;

}
