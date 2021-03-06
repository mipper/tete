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
package com.mipper.music.control;

import java.util.EventObject;


/**
 * Event fired when a sound is played.
 *
 * @author Cliff Evans
 * @version $Id$
 */
public class SoundEvent extends EventObject
{

  /**
   *
   */
  private static final long serialVersionUID = 1L;


  /**
   * Constructor.
   *
   * @param source Source of the event.
   * @param name Name of the sound to be played.
   */
  public SoundEvent ( Object source, String name )
  {
    super ( source );
    _name = name;
  }


  /**
   * Returns the name of the sound to be played.
   *
   * @return Name of the sound to be played.
   */
  public String getName ()
  {
    return _name;
  }


  private final String _name;

}
