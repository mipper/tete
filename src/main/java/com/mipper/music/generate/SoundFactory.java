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

import java.util.LinkedList;
import java.util.Random;

import com.mipper.music.model.Interval;
import com.mipper.music.model.IntervalPattern;
import com.mipper.music.model.Note;
import com.mipper.music.model.NoteRange;
import com.mipper.music.model.Sound;


/**
 * Class which generates random Sounds based on a list of IntervalPatterns and
 * a note range.
 *
 * @author Cliff Evans
 * @version $Id$
 */
public class SoundFactory
{

  /**
   * Constructor.  Creates a factory which can generate all intervals.
   */
  public SoundFactory ()
  {
    super ();
  }


  /**
   * Constructor.
   *
   * @param repository Add all patterns from the repository to the factory.
   */
  public SoundFactory ( IntervalPatternRepository repository  )
  {
    super ();
    addPatterns ( repository );
  }


  /**
   * Adds an interval to the list of available intervals.
   *
   * @param pattern Interval to add to the list.
   */
  public void addPattern ( IntervalPattern pattern )
  {
    if ( _availablePatterns.contains ( pattern ) )
    {
      throw new DuplicateException ( pattern.getName () );
    }
    _availablePatterns.add ( pattern );
  }


  /**
   * Adds all possible intervals to the available list.
   *
   * @param repository Repository whose patterns to add to the factory.
   */
  public void addPatterns ( IntervalPatternRepository repository )
  {
    _availablePatterns.clear ();
    for ( final IntervalPattern ct: repository.getPatterns () )
    {
      _availablePatterns.add ( ct );
    }
  }


  /**
   * Remove all available chords.
   */
  public void clear ()
  {
    _availablePatterns.clear ();
  }


  /**
   * Generates a random pattern based on a note range and available patterns.
   *
   * @param range The Range of notes available for the pattern.
   *
   * @return A pattern from the available list within the specified note range.
   *
   * @throws EmptyException
   */
  public Sound generateSound ( NoteRange range )
    throws
      EmptyException
  {
    return generateSound ( range, getRandomPattern () );
  }


  /**
   * Generates a random diad of the specified interval based on a note range.
   *
   * @param range The Range of notes available for the diad.
   * @param pattern Interval to return.
   *
   * @return A diad from within the specified note range whose interval is of
   *         the specified type.
   */
  public Sound generateSound ( NoteRange range, IntervalPattern pattern )
  {
    return new Playable ( pattern, getRandomRoot ( range, pattern ) );
  }


  /**
   * @return An array containing all IntervalPatters the factory can create.
   */
  public IntervalPattern[] getPatterns ()
  {
    return _availablePatterns.toArray ( new IntervalPattern[] {} );
  }


  /**
   * Removes an interval from the list of available intervals.
   *
   * @param pattern Interval to remove from the list.
   *
   * @throws NotFoundException
   */
  public void removePattern ( IntervalPattern pattern )
    throws
      NotFoundException
  {
    if ( !_availablePatterns.contains ( pattern ) )
    {
      throw new NotFoundException ( pattern.toString () );
    }
    _availablePatterns.remove ( pattern );
  }


  private IntervalPattern getRandomPattern ()
    throws
      EmptyException
  {
    if ( _availablePatterns.size () == 0 )
    {
      throw new EmptyException ();
    }
    return _availablePatterns.get ( _random.nextInt ( _availablePatterns.size () ) );
  }


  private int getRandomRoot ( NoteRange range, IntervalPattern pattern )
  {
    final Interval i = pattern.getLargestInterval ();
    int offset = _random.nextInt ( range.getNoteCount () );
    if ( range.getRootNote () == null )
    {
      offset = i.ordinal () >= range.getNoteCount ()
               ? 0 // Cannot pass 0 into _random.nextInt ()!
               : offset - i.ordinal ();
    }
    else
    {
      offset = offset / Note.values ().length * Note.values ().length + range.getRootNote ().ordinal ();
    }
    return range.getLowestNote () + offset;
  }


  private final Random _random = new Random ();
  private final LinkedList<IntervalPattern> _availablePatterns = new LinkedList<IntervalPattern> ();

}
