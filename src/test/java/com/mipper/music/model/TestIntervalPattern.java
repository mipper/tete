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

import junit.framework.TestCase;


/**
 *
 * @author Cliff Evans
 * @version $Revision: 1.1 $
 */
public class TestIntervalPattern extends TestCase
{

  /**
   * Class under test for void IntervalPattern()
   */
  public void testIntervalPattern ()
  {
    final IntervalPattern ip = new IntervalPattern ( "test" );
    assertEquals ( 0, ip.getIntervals ().length );
  }


  /**
   * Class under test for void IntervalPattern(Interval[])
   */
  public void testIntervalPatternIntervalArray ()
  {
    final IntervalPattern ip = new IntervalPattern ( "major", MAJOR );
    testMajor ( ip.getIntervals () );
    ip.addInterval ( Interval.MINOR_3RD );
    ip.removeInterval ( Interval.MAJOR_3RD );
    testMinor ( ip.getIntervals () );
    ip.addInterval ( Interval.MINOR_3RD );
    testMinor ( ip.getIntervals () );
    ip.setIntervals ( new Interval[] {Interval.MAJOR_3RD, Interval.PERFECT_5TH, Interval.PERFECT_1ST} );
    testMajor ( ip.getIntervals () );
  }


  /**
   *
   */
  public void testAddIntervals ()
  {
    final IntervalPattern majScale = new IntervalPattern ( "major", MAJOR_SCALE );
    final IntervalPattern maj = new IntervalPattern ( "major", MAJOR );
    final IntervalPattern min = new IntervalPattern ( "minor", MINOR );
    assertTrue ( majScale.contains ( maj ) );
    assertTrue ( !majScale.contains ( min ) );
    majScale.addIntervals( new Interval[] {Interval.MINOR_3RD, Interval.MINOR_7TH} );
    assertTrue ( majScale.contains ( min ) );
    min.addInterval ( Interval.MINOR_7TH );
    assertTrue ( majScale.contains ( min ) );
  }


  /**
   *
   */
  public void testClear ()
  {
    final IntervalPattern maj = new IntervalPattern ( "major", MAJOR_SCALE );
    maj.clear ();
    assertEquals ( 0, maj.getIntervals().length );
  }


  /**
   * @param ret
   */
  private void testMajor ( final Interval[] ret )
  {
    assertEquals ( 3, ret.length );
    assertEquals ( Interval.PERFECT_1ST, ret[0] );
    assertEquals ( Interval.MAJOR_3RD, ret[1] );
    assertEquals ( Interval.PERFECT_5TH, ret[2] );
  }


  /**
   * @param ret
   */
  private void testMinor ( final Interval[] ret )
  {
    assertEquals ( 3, ret.length );
    assertEquals ( Interval.PERFECT_1ST, ret[0] );
    assertEquals ( Interval.MINOR_3RD, ret[1] );
    assertEquals ( Interval.PERFECT_5TH, ret[2] );
  }


  private static final Interval[] MAJOR = new Interval[]
 {
     Interval.PERFECT_1ST,
     Interval.MAJOR_3RD,
     Interval.PERFECT_5TH
 };

  private static final Interval[] MINOR = new Interval[]
  {
     Interval.PERFECT_1ST,
     Interval.MINOR_3RD,
     Interval.PERFECT_5TH
  };

  private static final Interval[] MAJOR_SCALE = new Interval[]
  {
     Interval.PERFECT_1ST,
     Interval.MAJOR_2ND,
     Interval.MAJOR_3RD,
     Interval.PERFECT_4TH,
     Interval.PERFECT_5TH,
     Interval.MAJOR_6TH,
     Interval.MAJOR_7TH
  };

}
