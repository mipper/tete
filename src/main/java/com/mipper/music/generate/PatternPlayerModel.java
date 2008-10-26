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
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Patch;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;

import org.apache.commons.lang.StringUtils;

import com.mipper.music.midi.MidiHelper;
import com.mipper.music.midi.Player;
import com.mipper.music.model.IntervalPattern;
import com.mipper.music.model.Note;
import com.mipper.music.model.NoteRange;
import com.mipper.music.model.Sound;
import com.mipper.music.model.SoundFileException;
import com.mipper.music.model.SoundSystemException;
import com.mipper.util.Logger;


/**
 *
 * @author Cliff Evans
 * @version $Id$
 */
public class PatternPlayerModel
{

  /**
   * Constructor.
   *
   * @param synth Name of the synth to use.
   * @param sb Path to the soundbank to load.
   *
   * @throws SoundSystemException
   */
  public PatternPlayerModel ( final String synth, final String sb )
    throws
      SoundSystemException
  {
    super ();
    init ( synth, sb );
  }


  /**
   * Constructor.
   *
   * @param synth Name of the synth to use.
   *
   * @throws SoundSystemException
   */
  public PatternPlayerModel ( final String synth )
    throws
      SoundSystemException
  {
    super ();
    init ( synth, null );
  }


  private void init ( final String synth, final String sb )
    throws
      SoundSystemException
  {
    _factory = new SoundFactory ();
    _range = new NoteRange ();
    _direction = Boolean.valueOf ( true );
    try
    {
      if ( StringUtils.isEmpty ( sb ) )
      {
        _player = new Player ( lookupSynth ( synth ), null );
      }
      else
      {
        _player = new Player ( lookupSynth ( synth ), loadSoundbankSafe ( sb ) );
      }
    }
    catch ( final MidiUnavailableException e )
    {
      throw new SoundSystemException ( e );
    }
  }


  /**
   * @param name Name of the synth to load.
   *
   * @return The specified synth if it can be found, the first available synth
   *         if it can't.
   */
  public static Synthesizer lookupSynth ( final String name )
  {
    final List<Synthesizer> synths = MidiHelper.getAvailableSynthesizers ();
    for ( int i = 0; i < synths.size (); i++ )
    {
      if ( name.equals ( synths.get ( i ).getDeviceInfo ().getName () ) )
      {
        return synths.get ( i );
      }
    }
    return synths.get ( 0 );
  }


//  /**
//   * @param path Path for the soundbank file.
//   *
//   * @return Soundbank created from the specified path, or null if it couldn't
//   *         be loaded.
//   *
//   * @throws SoundException
//   * @throws IOException
//   * @throws InvalidMidiDataException
//   */
//  public static Soundbank loadSoundbank ( final String path )
//    throws
//      InvalidMidiDataException,
//      IOException,
//      SoundException
//  {
//    return Player.loadSoundbank ( path );
//  }


  /**
   * @param path Path to the soundbank to load.
   *
   * @return Soundbank loaded from the file.
   *
   * @throws SoundFileException Couldn't open the Soundbank file.
   */
  public static Soundbank loadSoundbank ( final String path )
    throws
      SoundFileException
  {
    if ( !StringUtils.isEmpty ( path ) )
    {
      final File sb = new File ( path );
      if ( sb.exists () )
      {
        try
        {
          return MidiSystem.getSoundbank ( sb );
        }
        catch ( final InvalidMidiDataException e )
        {
          throw new SoundFileException ( "Invalid Soundbank file: " + path, e );
        }
        catch ( final IOException e )
        {
          throw new SoundFileException ( "Unable to open file: " + path, e );
        }
      }
    }
    throw new SoundFileException ( "File does not exist: " + path );
  }


  /**
   * Loads a Soundbank if possible or returns null if not.
   *
   * @param path Path to the soundbank to load.
   *
   * @return The Soundbank or null if it could not be loaded.
   */
  public static Soundbank loadSoundbankSafe ( final String path )
  {
    try
    {
      return loadSoundbank ( path );
    }
    catch ( final SoundFileException e )
    {
      Logger.error ( e );
    }
    return null;
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
   * @return Instrument array of all available instruments.
   */
  public static List<Synthesizer> getAvailableSynths ()
  {
    return MidiHelper.getAvailableSynthesizers ();
  }


  /**
   * @return Array of Instruments available for playback.
   */
  public List<Instrument> getInstruments ()
  {
    return _player.getAvailableInstruments ();
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
   * @return Velocity of played note.
   */
  public int getVelocity ()
  {
    return _player.getVelocity ();
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


//  /**
//   * @return Current Soundbank.
//   */
//  public Soundbank getSoundbank ()
//  {
//    return _player.getSoundbank ();
//  }


  /**
   * Looks up the instrument in the current soundbank that has the specified
   * patch.
   *
   * @param patch Patch to lookup
   *
   * @return The instrument that matches the patch.
   */
  public Instrument lookupInstrument ( final Patch patch )
  {
    Logger.debugEx ( "midi.insts", "PPM.lookupInstrument: {0}", patch );
    return _player.getSoundbank ().getInstrument ( patch );
  }


  /**
   * @param snd int array of notes to sound.
   *
   * @throws SoundSystemException
   */
  public void play ( final Sound snd )
    throws
      SoundSystemException
  {
    Logger.debugEx ( "midi.notes", "Playing: {0} with {1}, {2}", snd, _player.getSynth (), _player.getInstrument () );
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
      throw new SoundSystemException ( e );
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
   * @param velocity Velocity with which to play notes.
   */
  public void setVelocity ( final int velocity )
  {
    _player.setVelocity ( velocity );
  }


  /**
   * @param synth Synthesizer to use for playback.
   * @param sb Soundbank to load into synth.
   *
   * @return true if the synth was set, false if it was already set or there was
   *         an error.
   *
   * @throws SoundFileException
   * @throws SoundSystemException
   */
  public boolean setSynth ( final Synthesizer synth, final Soundbank sb )
    throws
      SoundFileException,
      SoundSystemException
  {
    try
    {
      return _player.setSynth ( synth, sb );
    }
    catch ( final MidiUnavailableException e )
    {
      throw new SoundSystemException ( e );
    }
  }


  /**
   * Set the synthesizer being used to a named instance, else use the first one
   * we find.
   *
   * @param name Name of the synthesizer to return.
   * @param sb Path to the soundbank file to load.
   *
   * @return true if the synth was set, false if it was already set or there was
   *         an error.
   *
   * @throws SoundSystemException
   * @throws SoundFileException
   */
  public boolean setSynth ( final String name, final String sb )
    throws
      SoundSystemException,
      SoundFileException
  {
    return setSynth ( lookupSynth ( name ), loadSoundbank ( sb ) );
  }


  /**
   * @return Synth used for playback.
   */
  public Synthesizer getSynth ()
  {
    return _player.getSynth ();
  }


  /**
   * @param sb Soundbank to load.
   *
   * @throws SoundFileException
   * @throws SoundSystemException
   */
  public void setSoundbank ( final Soundbank sb )
    throws
      SoundFileException,
      SoundSystemException
  {
    try
    {
      _player.setSoundbank ( sb );
    }
    catch ( final MidiUnavailableException e )
    {
      throw new SoundSystemException ( e );
    }
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


  private SoundFactory _factory;
  private NoteRange _range;
  private Boolean _direction;
  private Player _player;
  private final Random _random = new Random ();

}
