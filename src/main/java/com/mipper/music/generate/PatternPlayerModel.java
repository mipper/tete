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


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Patch;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;

import com.mipper.music.midi.MidiException;
import com.mipper.music.midi.Player;
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
   * Constructor.
   *
   * @throws MidiException
   */
  public PatternPlayerModel ()
    throws
      MidiException
  {
    super ();
    _factory = new SoundFactory ();
    _range = new NoteRange ();
    _direction = Boolean.valueOf ( true );
    try
    {
      _player = new Player ();
    }
    catch ( final MidiUnavailableException e )
    {
      throw new MidiException ( e );
    }
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
   */
  public List<Synthesizer> getAvailableSynths ()
  {
    return _player.getAvailableSynths ();
  }


  /**
   * @return Array of Instruments available for playback.
   *
   * @throws MidiException
   */
  public List<Instrument> getLoadedInstruments ()
    throws
      MidiException
  {
    try
    {
      return _player.getLoadedInstruments ();
    }
    catch ( final MidiUnavailableException e )
    {
      throw new MidiException ( e );
    }
  }


  /**
   * @return The Instrument currently selected for playback.
   */
  public Instrument getInstrument ()
  {
    return _player.getInstrument ();
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
   */
  public Soundbank getSoundbank ()
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
   * @throws MidiException
   * @throws MidiUnavailableException
   */
  public Instrument lookupInstrument ( final Patch patch )
    throws
      MidiException
  {
    Instrument inst = getSoundbank ().getInstrument ( patch );
    if ( null == inst )
    {
      try
      {
        inst = _player.getLoadedInstruments ().get ( 0 );
      }
      catch ( final MidiUnavailableException e )
      {
        throw new MidiException ( e );
      }
    }
    _player.setInstrument ( inst );
    return _player.getInstrument ();
  }


  /**
   * @param snd int array of notes to sound.
   *
   * @throws MidiException
   */
  public void play ( final Sound snd )
    throws
      MidiException
  {
    Logger.debugEx ( "midi.sounds", "Playing: {0} with {1}, {2}", snd, _player.getSynth (), _player.getInstrument () );
    final int[] notes = snd.getNoteValues ();
    if ( !calcDirection () )
    {
      reverse ( notes );
    }
    try
    {
      _player.play ( notes );
    }
    catch ( final InvalidMidiDataException e )
    {
      throw new MidiException ( e );
    }
  }


  /**
   * @param value deley between each note of the pattern.  If this is 0 the
   *              pattern will be played as a chord.
   */
  public void setArpeggioDelay ( final int value )
  {
    _player.setArpeggioDelay ( value );
  }


  /**
   * @param value true to cascade the notes, i.e. sustain all notes until the
   *              final note has finished sounding.
   */
  public void setCascade ( final boolean value )
  {
    _player.setCascade ( value );
  }


  /**
   * @param direction Direction to play the notes.  True is ascending, False is
   *                  decending, and null is random.
   */
  public void setDirection ( final Boolean direction )
  {
    _direction = direction;
  }


  /**
   * @param synth Synthesizer to use for playback.
   */
  public void setSynth ( final Synthesizer synth )
  {
    _player.setSynth ( synth );
  }


  /**
   * Set the synthesizer being used to a named instance, else use the first one
   * we find.
   *
   * @param name Name of the synthesizer to return.
   */
  public void setSynth ( final String name )
  {
    setSynth ( lookupSynth ( name ) );
  }


  /**
   * @return Synth used for playback.
   */
  public Synthesizer getSynth ()
  {
    return _player.getSynth ();
  }


  /**
   * @param path File object for the soundbank file.
   *
   * @return true if all instruments loaded, false in some (or all) failed.
   *
   * @throws IOException
   * @throws MidiException
   */
  public boolean loadSoundbank ( final File path )
    throws
      IOException,
      MidiException
  {
    return _player.loadSoundbank ( path );
  }


  /**
   * @param instrument Instrument to use the play the pattern.
   */
  public void setInstrument ( final Instrument instrument )
  {
    _player.setInstrument ( instrument );
  }


  /**
   * @param value length to hold each note.
   */
  public void setNoteLength ( final int value )
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
  public void setNoteRange ( final int lowest, final int highest, final Note root )
  {
    _range.setNoteRange ( lowest, highest, root );
  }


  /**
   * @param patterns Sets the list of available patterns for generation.
   */
  public void setPatterns ( final IntervalPattern[] patterns )
  {
    _factory.clear ();
    for ( final IntervalPattern ct: patterns )
    {
      _factory.addPattern ( ct );
    }

  }


  private Synthesizer lookupSynth ( final String name )
  {
    final List<Synthesizer> synths = getAvailableSynths ();
    for ( int i = 0; i < synths.size (); i++ )
    {
      if ( name.equals ( synths.get ( i ).getDeviceInfo ().getName () ) )
      {
        return synths.get ( i );
      }
    }
    return synths.get ( 0 );
  }


  private boolean calcDirection ()
  {
    if ( null == _direction )
    {
      return _random.nextInt ( 2 ) == 1;
    }
    return _direction.booleanValue ();
  }


  private static void reverse ( final int[] notes )
  {
    for ( int left = 0, right = notes.length - 1; left < right; left++, right-- )
    {
      final int temp = notes[left];
      notes[left] = notes[right];
      notes[right] = temp;
    }
  }


  private final SoundFactory _factory;
  private final NoteRange _range;
  private Boolean _direction;
  private final Player _player;
  private final Random _random = new Random ();

}
