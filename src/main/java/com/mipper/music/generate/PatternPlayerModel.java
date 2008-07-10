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


import java.util.Random;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Patch;
import javax.sound.midi.Soundbank;

import com.mipper.music.control.Player;
import com.mipper.music.midi.MidiException;
import com.mipper.music.model.IntervalPattern;
import com.mipper.music.model.Note;
import com.mipper.music.model.NoteRange;
import com.mipper.music.model.Sound;
import com.mipper.util.Logger;


/**
 *
 * @author Cliff Evans
 * @version $$Revision: 1.3 $$
 */
public class PatternPlayerModel
{

  /**
   * @param notes
   */
  private static void reverse ( int[] notes )
  {
    for ( int left = 0, right = notes.length - 1; left < right; left++, right-- )
    {
      final int temp = notes[left];
      notes[left] = notes[right];
      notes[right] = temp;
    }
  }


  /**
   * Constructor.
   *
   * @throws MidiUnavailableException
   * @throws MidiException
   */
  public PatternPlayerModel ()
    throws
      MidiUnavailableException,
      MidiException
  {
    super ();
    _factory = new SoundFactory ();
    _player = new Player ();
    _range = new NoteRange ();
    _direction = Boolean.valueOf ( true );
  }


  /**
   * @return A randomly generated Playable object.
   *
   * @throws EmptyException
   */
  public Sound generateSound ()
    throws
      EmptyException
  {
    return _factory.generateSound ( _range );
  }


  /**
   * @return Array of Instruments available for playback.
   *
   * @throws MidiUnavailableException
   * @throws MidiException
   */
  public Object[] getAvailableInstruments ()
    throws
      MidiUnavailableException,
      MidiException
  {
    return _player.getAvailableInstruments ().toArray ();
  }


  /**
   * @return The direction to play the notes.
   */
  public Boolean getDirection ()
  {
    return _direction;
  }


  /**
   * @return The factory.
   */
  public SoundFactory getFactory ()
  {
    return _factory;
  }


  /**
   * @return Array of all IntervalPatterns that can be created.
   */
  public IntervalPattern[] getPatterns ()
  {
    return _factory.getPatterns ();
  }


  /**
   * @return The Player.
   */
  public Player getPlayer ()
  {
    return _player;
  }


  /**
   * @return The NoteRange.
   */
  public NoteRange getRange ()
  {
    return _range;
  }


  /**
   * @return Current Soundbank.
   *
   * @throws MidiUnavailableException
   */
  public Soundbank getSoundbank ()
    throws
      MidiUnavailableException
  {
    return _player.getSoundbank ();
  }


  /**
   * Looks up the instrument in the current soundbank that has the specified
   * patch.
   *
   * @param patch Patch to lookup
   *
   * @return The instrument that matches the patch.
   *
   * @throws MidiUnavailableException
   */
  public Instrument lookupInstrument ( Patch patch )
    throws
      MidiUnavailableException
  {
    return getSoundbank ().getInstrument ( patch );
  }


  /**
   * @param snd int array of notes to sound.
   *
   * @throws InvalidMidiDataException
   */
  public void play ( Sound snd )
    throws
      InvalidMidiDataException
  {
    Logger.debug ( "Playing: " + snd );
    final int[] notes = snd.getNoteValues ();
    if ( !calcDirection () )
    {
      reverse ( notes );
    }
    _player.play ( notes );
  }


  /**
   * @param value deley between each note of the pattern.  If this is 0 the
   *              pattern will be played as a chord.
   */
  public void setArpeggioDelay ( int value )
  {
    _player.setArpeggioDelay ( value );
  }


  /**
   * @param value true to cascade the notes, i.e. sustain all notes until the
   *              final note has finished sounding.
   */
  public void setCascade ( boolean value )
  {
    _player.setCascade ( value );
  }


  /**
   * @param direction Direction to play the notes.  True is ascending, False is
   *                  decending, and null is random.
   */
  public void setDirection ( Boolean direction )
  {
    _direction = direction;
  }


  /**
   * @param instrument Instrument to use the play the pattern.
   */
  public void setInstrument ( Instrument instrument )
  {
    _player.setInstrument ( instrument );
  }


  /**
   * @param value length to hold each note.
   */
  public void setNoteLength ( int value )
  {
    _player.setNoteLength ( value );
  }


  /**
   * Sets the range of notes available for play back.
   *
   * @param lowest Midi value of the lowest note to use.
   * @param highest Midi value of the highest note to use.
   * @param root The Note to use as the root or null if the root is random.
   */
  public void setNoteRange ( int lowest, int highest, Note root )
  {
    _range.setNoteRange ( lowest, highest, root );
  }


  /**
   * @param patterns Sets the list of available patterns for generation.
   */
  public void setPatterns ( Object[] patterns )
  {
    _factory.clear ();
    for ( final Object ct: patterns )
    {
      _factory.addPattern ( ( IntervalPattern ) ct );
    }

  }


  private boolean calcDirection ()
  {
    if ( null == _direction )
    {
      return _random.nextInt ( 2 ) == 1;
    }
    return _direction.booleanValue ();
  }


  private final SoundFactory _factory;
  private final NoteRange _range;
  private Boolean _direction;
  private final Player _player;
  private final Random _random = new Random ();

}
