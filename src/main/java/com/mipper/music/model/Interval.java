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
 * Enumerated type representing an interval between two notes.
 *
 * @author Cliff Evans
 * @version $Revision: 1.4 $
 */
public enum Interval
{

  /** */
  PERFECT_1ST ( "P1", "d2", "Perfect 1st", "Diminished 1st" ),
  /** */
  MINOR_2ND ( "m2", "A1", "Minor 2nd", "Augmented 1st" ),
  /** */
  MAJOR_2ND ( "M2", "d3", "Major 2nd", "Diminished 3rd" ),
  /** */
  MINOR_3RD ( "m3", "A2", "Minor 3rd", "Augmented 2nd" ),
  /** */
  MAJOR_3RD ( "M3", "d4", "Major 3rd", "Diminished 4th" ),
  /** */
  PERFECT_4TH ( "P4", "A3", "Perfect 4th", "Augmented 3rd" ),
  /** */
  DIMINISHED_5TH ( "d5", "A4", "Diminished 5th", "Augmented 4th" ),
  /** */
  PERFECT_5TH ( "P5", "d6", "Perfect 5th", "Diminished 6th" ),
  /** */
  MINOR_6TH ( "m6", "A5", "Minor 6th", "Augmented 5th" ),
  /** */
  MAJOR_6TH ( "M6", "d7", "Major 6th", "Diminished 7th" ),
  /** */
  MINOR_7TH ( "m7", "A6", "Minor 7th", "Augmented 6th" ),
  /** */
  MAJOR_7TH ( "M7", "d8", "Major 7th", "Diminished 8th" ),
  /** */
  PERFECT_8TH ( "P8", "A7", "Octave", "Augmented 7th" ),
  /** */
  MINOR_9TH ( "m9", null, "Minor 9th", null),
  /** */
  MAJOR_9TH ( "M9", null, "Major 9th", null),
  /** */
  MINOR_10TH ( "A9", "m10", "Augmented 9th", "Minor 10th" ),
  /** */
  MAJOR_10 ( "P10", null, "Perfect 10th", null ),
  /** */
  PERFECT_11TH ( "P11", null, "Perfect 11th", null ),
  /** */
  AUGMENTED_11TH ( "A11", null, "Augmented 11th", null ),
  /** */
  PERFECT_12 ( "P5", null, "Perfect 5th", null ),
  /** */
  MINOR_13TH ( "m13", null, "Minor 13th", null ),
  /** */
  MAJOR_13TH ( "M13", null, "Major 13th", null ),
  /** */
  MINOR_14 ( "m7", null, "Minor 7th", null ),
  /** */
  MAJOR_14 ( "M7", null, "Major 7th", null ),
  /** */
  PERFECT_16TH ( "P8", null, "Double Octave", null );



  /**
   * @return The total number of possible Intervals.
   */
  public static int count ()
  {
    return Interval.values ().length;
  }


  /**
   * @param value Ordinal value of Interval to retrieve.
   *
   * @return The interval with the specified ordinal value.
   */
  public static Interval getInterval ( final int value )
  {
    return Interval.values ()[value];
  }


  private Interval ( final String shortName,
                     final String altShortName,
                     final String longName,
                     final String altLongName )
  {
    _shortName = shortName;
    _alternativeShortName = altShortName;
    _longName = longName;
    _alternativeLongName = altLongName;
  }


  /**
   * @return Returns the alternativeLongName.
   */
  public String getAlternativeLongName ()
  {
    return _alternativeLongName;
  }


  /**
   * @return Returns the alternativeShortName.
   */
  public String getAlternativeShortName ()
  {
    return _alternativeShortName;
  }


  /**
   * @return Returns the longName.
   */
  public String getLongName ()
  {
    return _longName;
  }


  /**
   * @return Returns the shortName.
   */
  public String getShortName ()
  {
    return _shortName;
  }


  private String _shortName;
  private String _alternativeShortName;
  private String _longName;
  private String _alternativeLongName;

}
