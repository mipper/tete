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

import com.mipper.music.model.Interval;
import com.mipper.music.model.IntervalPattern;
import com.mipper.music.model.Note;
import com.mipper.music.model.Sound;


/**
 * A playable object is a combination of an IntervalPattern and a root note.  It
 * is an object which can actually be sounded.
 *
 * @author Cliff Evans
 * @version $Revision: 1.3 $
 */
public class Playable
  implements
    Sound
{

  /**
   * Constructor.
   *
   * @param pattern IntervalPattern describing the playable object.
   * @param root MIDI value of the root to use to play the pattern.
   */
  public Playable ( IntervalPattern pattern, int root )
  {
    this ( pattern, root, true );
  }


  /**
   * Constructor.
   *
   * @param pattern IntervalPattern describing the playable object.
   * @param root MIDI value of the root to use to play the pattern.
   * @param overlap Indicates whether notes overlap when being played.
   */
  public Playable ( IntervalPattern pattern, int root, boolean overlap )
  {
    super ();
    _intervalPattern = pattern;
    _root = root;
    _overlap = overlap;
  }


  /**
   * @see com.mipper.music.model.Sound#getName()
   */
  public String getName ()
  {
    return _intervalPattern.getName ();
  }


  /**
   * @see com.mipper.music.model.Sound#getNoteValues()
   */
  public int[] getNoteValues ()
  {
    final Interval[] intervals = _intervalPattern.getIntervals ();
    // If we only have one interval its the unison interval
    final int[] notes = new int[intervals.length + ( intervals.length == 1 ? 1 : 0 )];
    for ( int i = 0; i < intervals.length; i++ )
    {
      notes[i] = _root + intervals[i].ordinal ();
    }
    if ( intervals.length == 1 )
    {
      notes[1] = _root;
    }
    return notes;
  }


  /**
   * @see com.mipper.music.model.Sound#getOverlap()
   */
  public boolean getOverlap ()
  {
    return _overlap;
  }



  /**
   * @see com.mipper.music.model.Sound#setOverlap(boolean)
   */
  public void setOverlap ( boolean overlap )
  {
    _overlap = overlap;
  }


  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString ()
  {
    final StringBuffer buf = new StringBuffer ();
    buf.append ( Note.values ()[_root % 12] ).append ( " " ).append (  _intervalPattern.getName () );
    for ( final Interval ip : _intervalPattern.getIntervals () )
    {
      buf.append( " " ).append ( ip.getShortName() );
    }
    return buf.toString ();
  }


  private final IntervalPattern _intervalPattern;
  private final int _root;
  private boolean _overlap;

}
