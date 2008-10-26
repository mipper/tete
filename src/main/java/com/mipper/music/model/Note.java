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
 * Enum representing a named musical note.
 *
 * @author Cliff Evans
 * @version $Id$
 */
public enum Note
{

  /** */
  C ( "C", "C" ),
  /** */
  C_SHARP ( "C#", "Db" ),
  /** */
  D ( "D", "D" ),
  /** */
  D_SHARP ( "D#", "Eb" ),
  /** */
  E ( "E", "E" ),
  /** */
  F ( "F", "F" ),
  /** */
  F_SHARP ( "F#", "Gb" ),
  /** */
  G ( "G", "G" ),
  /** */
  G_SHARP ( "G#", "Ab" ),
  /** */
  A ( "A", "A" ),
  /** */
  A_SHARP ( "A#", "Bb" ),
  /** */
  B ( "B", "B" );


  private Note ( String sharpName, String flatName )
  {
    _sharpName = sharpName;
    _flatName = flatName;
  }


  /**
   * @return Note name in sharp keys.
   */
  public String getFlatName ()
  {
    return _flatName;
  }


  /**
   * @return Note name in flat keys.
   */
  public String getSharpName ()
  {
    return _sharpName;
  }


  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString ()
  {
    return _sharpName;
  }


  private String _sharpName;
  private String _flatName;

}
