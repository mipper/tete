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

import java.util.Arrays;


/**
 * Class representing a collection of intervals.
 *
 * @author Cliff Evans
 * @version $Revision: 1.3 $
 */
public class IntervalPattern
{

  /**
   * Constructor.  Creates an empty IntervalPattern object.
   *
   * @param name Name given to the pattern.
   */
  public IntervalPattern ( String name )
  {
    super ();
    _name = name;
  }


  /**
   * Constructor.  Creates an IntervalPattern object which contains exactly the
   * intervals of the Interval array passed in.
   *
   * @param interval Array of intervals to set in the pattern.
   * @param name Name given to the pattern.
   */
  public IntervalPattern ( String name, Interval[] interval )
  {
    super ();
    _name = name;
    addIntervals ( interval );
  }


  /**
   * @param interval Interval to set.  If the interval is already present in the
   *                 pattern then this method does nothing.
   */
  public void addInterval ( Interval interval )
  {
    if ( !_intervals[interval.ordinal ()] )
    {
      _intervals[interval.ordinal ()] = true;
      _count++;
    }
  }


  /**
   * Adds the intervals contained in the Interval array parameter to those
   * already contained in this pattern.
   *
   * @param intervals Array of intervals to add.
   */
  public void addIntervals ( Interval[] intervals )
  {
    for ( final Interval element : intervals )
    {
      addInterval ( element );
    }
  }


  /**
   * Remove all intervals from this pattern.
   */
  public void clear ()
  {
    Arrays.fill ( _intervals, false );
    _count = 0;
  }


  /**
   * Tests whether the specified pattern is contained in this pattern.  This
   * method will return true if all of the intervals in pattern are also present
   * in this IntervalPattern object.
   *
   * @param pattern The pattern to check.
   *
   * @return true if this pattern contains the specified pattern, false otherwise.
   */
  public boolean contains ( IntervalPattern pattern )
  {
    for ( int i = 0; i < pattern._intervals.length; i++ )
    {
      if ( pattern._intervals[i] )
      {
        if ( !_intervals[i] )
        {
          return false;
        }
      }
    }
    return true;
  }


  /**
   * @return An ordered array of the intervals making up the interal pattern.
   */
  public Interval[] getIntervals ()
  {
    final Interval[] intervals = new Interval[_count];
    int count = 0;
    for ( int i = 0; i < _intervals.length; i++ )
    {
      if ( _intervals[i] )
      {
        intervals[count++] = Interval.getInterval ( i );
      }
    }
    return intervals;
  }


  /**
   * @return The largest interval contained in the Chord Type.
   */
  public Interval getLargestInterval ()
  {
    return getIntervals ()[getIntervals ().length - 1];
  }


  /**
   * @return The name of the pattern.
   */
  public String getName ()
  {
    return _name;
  }


  /**
   * @param interval Interval to remove.  If the intervals does not exist in the
   *                 pattern then this method does nothing.
   */
  public void removeInterval ( Interval interval )
  {
    if ( _intervals[interval.ordinal ()] )
    {
      _intervals[interval.ordinal ()] = false;
      _count--;
    }
  }


  /**
   * Sets the interval pattern for this object to match the array passed in.
   *
   * @param intervals Interval array to set.
   */
  public void setIntervals ( Interval[] intervals )
  {
    clear ();
    addIntervals ( intervals );
  }


  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString ()
  {
    return _name;
//    StringBuffer buf = new StringBuffer ();
//    buf.append ( _name ).append ( ": " );
//    for ( int i = 0; i < _intervals.length; i++ )
//    {
//      if ( _intervals[i] )
//      {
//        buf.append ( i ).append ( " " );
//      }
//    }
//    return buf.toString ();
  }


  private final boolean[] _intervals = new boolean[Interval.count ()];
  private int _count = 0;
  private final String _name;

}
