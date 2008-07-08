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
package com.mipper.music.generate;

import java.util.Collection;
import java.util.HashMap;
import com.mipper.music.model.Interval;
import com.mipper.music.model.IntervalPattern;


/**
 * A container for holding groups of interval patterns such as Intervals, Chords
 * and Scales.
 * 
 * @author Cliff Evans
 * @version $Revision: 1.8 $
 */
public class SoundTypeRepository
{

  /**
   * Constructor.
   */
  public SoundTypeRepository ()
  {
    super ();
    _repository = new HashMap<String, IntervalPatternRepository> ();
    temp ();
  }
  
  
  /**
   * Adds a named group of IntervalPatterns to the repository.
   * 
   * @param soundType Sound type to add to the repository.
   */
  public void addSoundType ( IntervalPatternRepository soundType )
  {
    if ( _repository.containsKey ( soundType.getName () ) )
    {
      throw new DuplicateException  ( "Sound type already in repository: " + soundType.getName () );
    }
  }
  
  
  /**
   * @param name Name of the sound type to retrieve.
   * 
   * @return The IntervalPatternRepository for the specified type.
   * 
   * @throws NotFoundException
   */
  public IntervalPatternRepository getPatterns ( String name )
    throws
      NotFoundException
  {
    if ( _repository.containsKey ( name ) )
    {
      return _repository.get ( name );
    }
    throw new NotFoundException ( "Sound type does not exits: " + name );
  }
  
  
  /**
   * @return A Collection containing all the IntervalPattern groups in the
   *         repository.
   */
  public Collection<IntervalPatternRepository> getAllPatternGroups ()
  {
    return _repository.values ();
  }
  
  
  // TODO: These should be configured externally or read from a file.
  private void temp ()
  {
    IntervalPatternRepository chords = new IntervalPatternRepository ( "Chords" );
    chords.registerPattern ( new IntervalPattern ( "Major", new Interval[] {Interval.PERFECT_1ST, Interval.MAJOR_3RD, Interval.PERFECT_5TH} ) );
    chords.registerPattern ( new IntervalPattern ( "Minor", new Interval[] {Interval.PERFECT_1ST, Interval.MINOR_3RD, Interval.PERFECT_5TH} ) );
    chords.registerPattern ( new IntervalPattern ( "Suspended 2nd", new Interval[] {Interval.PERFECT_1ST, Interval.MAJOR_2ND, Interval.PERFECT_5TH} ) );
    chords.registerPattern ( new IntervalPattern ( "Suspended 4th", new Interval[] {Interval.PERFECT_1ST, Interval.PERFECT_4TH, Interval.PERFECT_5TH} ) );
    chords.registerPattern ( new IntervalPattern ( "Augmented", new Interval[] {Interval.PERFECT_1ST, Interval.MAJOR_3RD, Interval.MINOR_6TH} ) );
    chords.registerPattern ( new IntervalPattern ( "Diminished", new Interval[] {Interval.PERFECT_1ST, Interval.MINOR_3RD, Interval.DIMINISHED_5TH} ) );
    chords.registerPattern ( new IntervalPattern ( "Major b5", new Interval[] {Interval.PERFECT_1ST, Interval.MAJOR_3RD, Interval.DIMINISHED_5TH} ) );
    chords.registerPattern ( new IntervalPattern ( "Major 7th", new Interval[] {Interval.PERFECT_1ST, Interval.MAJOR_3RD, Interval.PERFECT_5TH, Interval.MAJOR_7TH} ) );
    chords.registerPattern ( new IntervalPattern ( "Minor 7th", new Interval[] {Interval.PERFECT_1ST, Interval.MINOR_3RD, Interval.PERFECT_5TH, Interval.MINOR_7TH} ) );
    chords.registerPattern ( new IntervalPattern ( "Dominant 7th", new Interval[] {Interval.PERFECT_1ST, Interval.MAJOR_3RD, Interval.PERFECT_5TH, Interval.MINOR_7TH} ) );
    chords.registerPattern ( new IntervalPattern ( "7 Suspended 2nd", new Interval[] {Interval.PERFECT_1ST, Interval.MAJOR_2ND, Interval.PERFECT_5TH, Interval.MINOR_7TH} ) );
    chords.registerPattern ( new IntervalPattern ( "7 Suspended 4th", new Interval[] {Interval.PERFECT_1ST, Interval.PERFECT_4TH, Interval.PERFECT_5TH, Interval.MINOR_7TH} ) );
    chords.registerPattern ( new IntervalPattern ( "7 Augmented", new Interval[] {Interval.PERFECT_1ST, Interval.MAJOR_3RD, Interval.MINOR_6TH, Interval.MINOR_7TH} ) );
    chords.registerPattern ( new IntervalPattern ( "Minor 7th b5", new Interval[] {Interval.PERFECT_1ST, Interval.MINOR_3RD, Interval.DIMINISHED_5TH, Interval.MINOR_7TH} ) );
    chords.registerPattern ( new IntervalPattern ( "Minor Major 7th", new Interval[] {Interval.PERFECT_1ST, Interval.MINOR_3RD, Interval.PERFECT_5TH, Interval.MAJOR_7TH} ) );
    chords.registerPattern ( new IntervalPattern ( "Major 7 Augmented", new Interval[] {Interval.PERFECT_1ST, Interval.MAJOR_3RD, Interval.MINOR_6TH, Interval.MAJOR_7TH} ) );
    chords.registerPattern ( new IntervalPattern ( "Major 9th", new Interval[] {Interval.PERFECT_1ST, Interval.MAJOR_3RD, Interval.PERFECT_5TH, Interval.MAJOR_7TH, Interval.MAJOR_9TH} ) );
    chords.registerPattern ( new IntervalPattern ( "Dominant 9th", new Interval[] {Interval.PERFECT_1ST, Interval.MAJOR_3RD, Interval.PERFECT_5TH, Interval.MINOR_7TH, Interval.MAJOR_9TH} ) );
    chords.registerPattern ( new IntervalPattern ( "Minor 9th", new Interval[] {Interval.PERFECT_1ST, Interval.MINOR_3RD, Interval.PERFECT_5TH, Interval.MINOR_7TH, Interval.MAJOR_9TH} ) );
    chords.registerPattern ( new IntervalPattern ( "9 Suspended 4th", new Interval[] {Interval.PERFECT_1ST, Interval.PERFECT_4TH, Interval.PERFECT_5TH, Interval.MINOR_7TH, Interval.MAJOR_9TH} ) );
    chords.registerPattern ( new IntervalPattern ( "Minor Major 9th", new Interval[] {Interval.PERFECT_1ST, Interval.MINOR_3RD, Interval.PERFECT_5TH, Interval.MAJOR_7TH, Interval.MAJOR_9TH} ) );
    chords.registerPattern ( new IntervalPattern ( "Dominant 11th", new Interval[] {Interval.PERFECT_1ST, Interval.MAJOR_3RD, Interval.PERFECT_5TH, Interval.MINOR_7TH, Interval.MAJOR_9TH, Interval.PERFECT_11TH} ) );
    chords.registerPattern ( new IntervalPattern ( "Minor 11th", new Interval[] {Interval.PERFECT_1ST, Interval.MINOR_3RD, Interval.PERFECT_5TH, Interval.MINOR_7TH, Interval.MAJOR_9TH, Interval.PERFECT_11TH} ) );
    chords.registerPattern ( new IntervalPattern ( "Major 13th", new Interval[] {Interval.PERFECT_1ST, Interval.MAJOR_3RD, Interval.PERFECT_5TH, Interval.MAJOR_7TH, Interval.MAJOR_9TH, Interval.MAJOR_13TH} ) );
    chords.registerPattern ( new IntervalPattern ( "Dominant 13th", new Interval[] {Interval.PERFECT_1ST, Interval.MAJOR_3RD, Interval.PERFECT_5TH, Interval.MINOR_7TH, Interval.MAJOR_9TH, Interval.MAJOR_13TH} ) );
    chords.registerPattern ( new IntervalPattern ( "Minor 13th", new Interval[] {Interval.PERFECT_1ST, Interval.MINOR_3RD, Interval.PERFECT_5TH, Interval.MINOR_7TH, Interval.MAJOR_9TH, Interval.MAJOR_13TH} ) );
    chords.registerPattern ( new IntervalPattern ( "13 suspended 4th", new Interval[] {Interval.PERFECT_1ST, Interval.PERFECT_4TH, Interval.PERFECT_5TH, Interval.MINOR_7TH, Interval.MAJOR_9TH, Interval.MAJOR_13TH} ) );
    chords.registerPattern ( new IntervalPattern ( "Diminished 7th", new Interval[] {Interval.PERFECT_1ST, Interval.MINOR_3RD, Interval.DIMINISHED_5TH, Interval.MAJOR_6TH} ) );
    chords.registerPattern ( new IntervalPattern ( "Major 6th", new Interval[] {Interval.PERFECT_1ST, Interval.MAJOR_3RD, Interval.PERFECT_5TH, Interval.MAJOR_6TH} ) );
    chords.registerPattern ( new IntervalPattern ( "Minor 6th", new Interval[] {Interval.PERFECT_1ST, Interval.MINOR_3RD, Interval.PERFECT_5TH, Interval.MAJOR_6TH} ) );
    chords.registerPattern ( new IntervalPattern ( "Major Add 9th", new Interval[] {Interval.PERFECT_1ST, Interval.MAJOR_3RD, Interval.PERFECT_5TH, Interval.MAJOR_9TH} ) );
    chords.registerPattern ( new IntervalPattern ( "Minor Add 9th", new Interval[] {Interval.PERFECT_1ST, Interval.MINOR_3RD, Interval.PERFECT_5TH, Interval.MAJOR_9TH} ) );
    chords.registerPattern ( new IntervalPattern ( "Major 6th/9th", new Interval[] {Interval.PERFECT_1ST, Interval.MAJOR_3RD, Interval.PERFECT_5TH, Interval.MAJOR_6TH, Interval.MAJOR_9TH} ) );
    chords.registerPattern ( new IntervalPattern ( "Minor 6th/9th", new Interval[] {Interval.PERFECT_1ST, Interval.MINOR_3RD, Interval.PERFECT_5TH, Interval.MAJOR_6TH, Interval.MAJOR_9TH} ) );
    chords.registerPattern ( new IntervalPattern ( "7th Add b9th", new Interval[] {Interval.PERFECT_1ST, Interval.MAJOR_3RD, Interval.PERFECT_5TH, Interval.MINOR_7TH, Interval.MINOR_9TH} ) );
    chords.registerPattern ( new IntervalPattern ( "7th Add Augmented 9th", new Interval[] {Interval.PERFECT_1ST, Interval.MAJOR_3RD, Interval.PERFECT_5TH, Interval.MINOR_7TH, Interval.MINOR_10TH} ) );
    chords.registerPattern ( new IntervalPattern ( "7th 5th/b9th", new Interval[] {Interval.PERFECT_1ST, Interval.MAJOR_3RD, Interval.MINOR_6TH, Interval.MINOR_7TH, Interval.MINOR_9TH} ) );
    chords.registerPattern ( new IntervalPattern ( "7th 5th/Augmented 9th", new Interval[] {Interval.PERFECT_1ST, Interval.MAJOR_3RD, Interval.MINOR_6TH, Interval.MINOR_7TH, Interval.MINOR_10TH} ) );
    chords.registerPattern ( new IntervalPattern ( "9th Add Augmented 11th", new Interval[] {Interval.PERFECT_1ST, Interval.MAJOR_3RD, Interval.PERFECT_5TH, Interval.MINOR_7TH, Interval.MAJOR_9TH, Interval.AUGMENTED_11TH} ) );
    chords.registerPattern ( new IntervalPattern ( "13th Add Augmented 11th", new Interval[] {Interval.PERFECT_1ST, Interval.MAJOR_3RD, Interval.PERFECT_5TH, Interval.MINOR_7TH, Interval.MAJOR_9TH, Interval.AUGMENTED_11TH, Interval.MAJOR_13TH} ) );
    _repository.put( chords.getName (), chords );
    
    IntervalPatternRepository scales = new IntervalPatternRepository ( "Scales" );
    scales.registerPattern ( new IntervalPattern ( "Major (Ionian)", 
                                                   new Interval[] {Interval.PERFECT_1ST, 
                                                                   Interval.MAJOR_2ND, 
                                                                   Interval.MAJOR_3RD, 
                                                                   Interval.PERFECT_4TH, 
                                                                   Interval.PERFECT_5TH, 
                                                                   Interval.MAJOR_6TH,
                                                                   Interval.MAJOR_7TH,
                                                                   Interval.PERFECT_8TH} ) );
    scales.registerPattern ( new IntervalPattern ( "(Dorian)", 
                                                   new Interval[] {Interval.PERFECT_1ST, 
                                                                   Interval.MAJOR_2ND, 
                                                                   Interval.MINOR_3RD, 
                                                                   Interval.PERFECT_4TH, 
                                                                   Interval.PERFECT_5TH, 
                                                                   Interval.MAJOR_6TH,
                                                                   Interval.MINOR_7TH,
                                                                   Interval.PERFECT_8TH} ) );
    scales.registerPattern ( new IntervalPattern ( "(Phrygian)", 
                                                   new Interval[] {Interval.PERFECT_1ST, 
                                                                   Interval.MINOR_2ND, 
                                                                   Interval.MINOR_3RD, 
                                                                   Interval.PERFECT_4TH, 
                                                                   Interval.PERFECT_5TH, 
                                                                   Interval.MINOR_6TH,
                                                                   Interval.MINOR_7TH,
                                                                   Interval.PERFECT_8TH} ) );
    scales.registerPattern ( new IntervalPattern ( "(Lydian)", 
                                                   new Interval[] {Interval.PERFECT_1ST, 
                                                                   Interval.MAJOR_2ND, 
                                                                   Interval.MAJOR_3RD, 
                                                                   Interval.DIMINISHED_5TH, 
                                                                   Interval.PERFECT_5TH, 
                                                                   Interval.MAJOR_6TH,
                                                                   Interval.MAJOR_7TH,
                                                                   Interval.PERFECT_8TH} ) );
    scales.registerPattern ( new IntervalPattern ( "(Mixolydian)", 
                                                   new Interval[] {Interval.PERFECT_1ST, 
                                                                   Interval.MAJOR_2ND, 
                                                                   Interval.MAJOR_3RD, 
                                                                   Interval.PERFECT_4TH, 
                                                                   Interval.PERFECT_5TH, 
                                                                   Interval.MAJOR_6TH,
                                                                   Interval.MINOR_7TH,
                                                                   Interval.PERFECT_8TH} ) );
    scales.registerPattern ( new IntervalPattern ( "Natural Minor (Aeolian)", 
                                                   new Interval[] {Interval.PERFECT_1ST, 
                                                                   Interval.MAJOR_2ND, 
                                                                   Interval.MINOR_3RD, 
                                                                   Interval.PERFECT_4TH, 
                                                                   Interval.PERFECT_5TH, 
                                                                   Interval.MINOR_6TH,
                                                                   Interval.MINOR_7TH,
                                                                   Interval.PERFECT_8TH} ) );
    scales.registerPattern ( new IntervalPattern ( "(Locrian)", 
                                                   new Interval[] {Interval.PERFECT_1ST, 
                                                                   Interval.MINOR_2ND, 
                                                                   Interval.MINOR_3RD, 
                                                                   Interval.PERFECT_4TH, 
                                                                   Interval.DIMINISHED_5TH, 
                                                                   Interval.MINOR_6TH,
                                                                   Interval.MINOR_7TH,
                                                                   Interval.PERFECT_8TH} ) );
    scales.registerPattern ( new IntervalPattern ( "Harmonic Minor", 
                                                   new Interval[] {Interval.PERFECT_1ST, 
                                                                   Interval.MAJOR_2ND, 
                                                                   Interval.MINOR_3RD, 
                                                                   Interval.PERFECT_4TH, 
                                                                   Interval.PERFECT_5TH, 
                                                                   Interval.MINOR_6TH,
                                                                   Interval.MAJOR_7TH,
                                                                   Interval.PERFECT_8TH} ) );
    scales.registerPattern ( new IntervalPattern ( "Melodic Minor", 
                                                   new Interval[] {Interval.PERFECT_1ST, 
                                                                   Interval.MAJOR_2ND, 
                                                                   Interval.MINOR_3RD, 
                                                                   Interval.PERFECT_4TH, 
                                                                   Interval.PERFECT_5TH, 
                                                                   Interval.MAJOR_6TH,
                                                                   Interval.MAJOR_7TH,
                                                                   Interval.PERFECT_8TH} ) );
    scales.registerPattern ( new IntervalPattern ( "Major Pentatonic", 
                                                   new Interval[] {Interval.PERFECT_1ST, 
                                                                   Interval.MAJOR_2ND, 
                                                                   Interval.MAJOR_3RD, 
                                                                   Interval.PERFECT_5TH, 
                                                                   Interval.MAJOR_6TH,
                                                                   Interval.PERFECT_8TH} ) );
    scales.registerPattern ( new IntervalPattern ( "Minor Pentatonic", 
                                                   new Interval[] {Interval.PERFECT_1ST, 
                                                                   Interval.MINOR_3RD, 
                                                                   Interval.PERFECT_4TH, 
                                                                   Interval.PERFECT_5TH, 
                                                                   Interval.MINOR_7TH,
                                                                   Interval.PERFECT_8TH} ) );
    scales.registerPattern ( new IntervalPattern ( "Blues", 
                                                   new Interval[] {Interval.PERFECT_1ST, 
                                                                   Interval.MINOR_3RD, 
                                                                   Interval.PERFECT_4TH, 
                                                                   Interval.DIMINISHED_5TH, 
                                                                   Interval.PERFECT_5TH, 
                                                                   Interval.MINOR_7TH,
                                                                   Interval.PERFECT_8TH} ) );
    scales.registerPattern ( new IntervalPattern ( "Augmented", 
                                                   new Interval[] {Interval.PERFECT_1ST, 
                                                                   Interval.MINOR_3RD, 
                                                                   Interval.MAJOR_3RD, 
                                                                   Interval.PERFECT_5TH, 
                                                                   Interval.MINOR_6TH, 
                                                                   Interval.MAJOR_7TH,
                                                                   Interval.PERFECT_8TH} ) );
    scales.registerPattern ( new IntervalPattern ( "Hungarian Major", 
                                                   new Interval[] {Interval.PERFECT_1ST, 
                                                                   Interval.MINOR_3RD, 
                                                                   Interval.DIMINISHED_5TH, 
                                                                   Interval.PERFECT_5TH, 
                                                                   Interval.MAJOR_6TH,
                                                                   Interval.MINOR_7TH,
                                                                   Interval.PERFECT_8TH} ) );
    scales.registerPattern ( new IntervalPattern ( "Hungarian Minor", 
                                                   new Interval[] {Interval.PERFECT_1ST, 
                                                                   Interval.MAJOR_2ND, 
                                                                   Interval.MINOR_3RD, 
                                                                   Interval.DIMINISHED_5TH, 
                                                                   Interval.PERFECT_5TH, 
                                                                   Interval.MINOR_6TH, 
                                                                   Interval.MAJOR_7TH,
                                                                   Interval.PERFECT_8TH} ) );
    scales.registerPattern ( new IntervalPattern ( "Enigmatic", 
                                                   new Interval[] {Interval.PERFECT_1ST, 
                                                                   Interval.MINOR_2ND, 
                                                                   Interval.MAJOR_3RD, 
                                                                   Interval.DIMINISHED_5TH, 
                                                                   Interval.MINOR_6TH, 
                                                                   Interval.MINOR_7TH,
                                                                   Interval.MAJOR_7TH,
                                                                   Interval.PERFECT_8TH} ) );

    IntervalPatternRepository intervals = new IntervalPatternRepository ( "Intervals" );
    for ( Interval i: Interval.values () )
    {
      if ( i.ordinal () > Interval.PERFECT_8TH.ordinal () )
      {
        break;
      }
      intervals.registerPattern ( new IntervalPattern ( i.getLongName (), 
                                                        new Interval[] {Interval.PERFECT_1ST, i} ) );
    }
    _repository.put ( intervals.getName (), intervals );
    
    _repository.put ( scales.getName (), scales );
  }
  
  
  private HashMap<String, IntervalPatternRepository> _repository;
  
}
