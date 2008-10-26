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
 * Class representing an allowable note range.  Stores the top and bottom notes
 * of a range along with any specified root note.
 *
 * @author Cliff Evans
 * @version $Id$
 */
public class NoteRange
{

  /**
   * Constructor.
   */
  public NoteRange ()
  {
    super ();
    setNoteRange ( NOTE_BOTTOM, NOTE_TOP, null );
  }
  /**
   * Constructor.
   *
   * @param bottom MIDI value of lowest available note.
   * @param top MIDI value of the highest available note.
   * @param root The note to use as the root or null if the root should be random.
   */
  public NoteRange ( int bottom, int top, Note root )
  {
    super ();
    setNoteRange ( bottom, top, root );
  }
  /**
   * @return Returns the highestNote.
   */
  public int getHighestNote ()
  {
    return _highestNote;
  }


  /**
   * @return Returns the lowestNote.
   */
  public int getLowestNote ()
  {
    return _lowestNote;
  }


  /**
   * @return Number of notes in the range.
   */
  public int getNoteCount ()
  {
    return _highestNote - _lowestNote;
  }


  /**
   * @return The root note to use, or null if the root should be random.
   */
  public Note getRootNote ()
  {
    return _root;
  }


  /**
   * Sets the note range for the object.
   *
   * @param lowestNote Lowest allowable note in the range.
   * @param highestNote Highest allowable note in the range.
   * @param root The note to use as the root or null if the root should be random.
   *
   * @throws NoteRangeException If the lowest note is higher than the highestNote.
   */
  public void setNoteRange ( int lowestNote, int highestNote, Note root )
    throws
      NoteRangeException
  {
    if ( lowestNote > highestNote )
    {
      throw new NoteRangeException ( lowestNote, highestNote );
    }
    _lowestNote = lowestNote;
    _highestNote = highestNote;
    _root = root;
  }


  /** Default MIDI value for the lowest available note */
  public static final int NOTE_BOTTOM = 0;


  /** Default MIDI value for the highest available note */
  public static final int NOTE_TOP = 127;


  /** MIDI value for middle C */
  public static final int MIDDLE_C = 60;


  private int _lowestNote;
  private int _highestNote;
  private Note _root;

}
