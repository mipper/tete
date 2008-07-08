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
package test.com.mipper.music.model;

import junit.framework.TestCase;
import com.mipper.util.Logger;
import com.mipper.music.model.NoteRange;
import com.mipper.music.model.NoteRangeException;


/**
 * 
 * @author Cliff Evans
 * @version $Revision: 1.2 $
 */
public class TestNoteRange extends TestCase
{
  
  /**
   * 
   */
  public void testNormal ()
  {
    NoteRange range = new NoteRange ();
    assertEquals ( NoteRange.NOTE_BOTTOM, range.getLowestNote () );
    assertEquals ( NoteRange.NOTE_TOP, range.getHighestNote () );
    range.setNoteRange ( 9, 99, null );
    assertEquals ( 9, range.getLowestNote () );
    assertEquals ( 99, range.getHighestNote () );
    range = new NoteRange ( 9, 99, null );
    assertEquals ( 9, range.getLowestNote () );
    assertEquals ( 99, range.getHighestNote () );
    range = new NoteRange ( 9, 9, null );
  }
  
  
  /**
   * 
   */
  public void testErrors ()
  {
    NoteRange range;
    try
    {
      range = new NoteRange ( 19, 18, null );
      fail ( "Expected constructor to throw an exception." );
    }
    catch ( NoteRangeException e )
    {
      Logger.info ( "Got expected exception." );
    }

    range = new NoteRange ();
    try
    {
      range.setNoteRange ( 99, 9, null );
      fail ( "Expected method to throw an exception." );
    }
    catch ( NoteRangeException e )
    {
      Logger.info ( "Got expected exception." );
    }
  }
}
