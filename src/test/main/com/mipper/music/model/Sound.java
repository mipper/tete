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
package com.mipper.music.model;


/**
 * Interface which supplies actual MIDI note values to play a sound.
 * 
 * @author Cliff Evans
 * @version $Revision: 1.1 $
 */
public interface Sound
{
  
  /**
   * @return Ascending array of MIDI note values that make up the Sound.
   */
  int[] getNoteValues ();
  
  
  /**
   * @return true if notes overlap when played, false if not.
   */
  boolean getOverlap ();
  
  
  /**
   * @param overlap true if notes overlap when played, false if not.
   */
  void setOverlap ( boolean overlap );
  
  
  /**
   * @return Name of the pattern.
   */
  String getName ();
  
  
}
