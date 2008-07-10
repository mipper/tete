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
 * Enumeration of the MIDI octaves.
 *
 * @author Cliff Evans
 * @version $Revision: 1.1 $
 */
public enum Octave
{

  /** */
  MINUS_ONE ( -1, "-1", "Minus One" ),
  /** */
  ZERO ( 0, "0", "Zero" ),
  /** */
  ONE ( 1, "1", "One" ),
  /** */
  TWO ( 2, "2", "Two" ),
  /** */
  THREE ( 3, "3", "Three" ),
  /** */
  FOUR ( 4, "4 (Middle C)", "Four (Middle C)" ),
  /** */
  FIVE ( 5, "5", "Five" ),
  /** */
  SIX ( 6, "6", "Six" ),
  /** */
  SEVEN ( 7, "7", "Seven" ),
  /** */
  EIGHT ( 8, "8", "Eight" ),
  /** */
  NINE ( 9, "9", "Nine" );


  private Octave ( int value, String shortName, String longName )
  {
    _value = value;
    _shortName = shortName;
    _longName = longName;
  }


  /**
   * @return The long name for the octave.
   */
  public String getLongName ()
  {
    return _longName;
  }


  /**
   * @return Octave number.  Middle C is in octave 4.
   */
  public int getOctaveNumber ()
  {
    return _value;
  }


  /**
   * @return The shortened name for the octave.
   */
  public String getShortName ()
  {
    return _shortName;
  }


  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString ()
  {
    return _longName;
  }


  /**
   * Number of Intervals making up an octave.
   */
  public static int INTERVAL_COUNT = 12;


  private int _value;
  private String _shortName;
  private String _longName;

}
