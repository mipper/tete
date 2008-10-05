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
package com.mipper.music.midi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Patch;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;

import com.mipper.music.model.SoundFileException;
import com.mipper.util.Logger;


/**
 * A wrapper class to make playing midi notes more simple.
 *
 * @author Cliff Evans
 * @version $Revision: 1.3 $
 */
public class Player
{

  /**
   * Constructor.  Uses the default synthesizer and default soundbank.
   *
   * @throws MidiUnavailableException
   */
  public Player ()
    throws
      MidiUnavailableException
  {
    this ( MidiHelper.getAvailableSynthesizers ().get ( 0 ), null );
  }


  /**
   * Constructor.
   *
   * @param synth Synthesizer to be used for playback.
   * @param sb Soundbank to load into the synthesizer, null if no soundbank.
   *
   * @throws MidiUnavailableException
   */
  public Player ( final Synthesizer synth, final Soundbank sb )
    throws
      MidiUnavailableException
  {
    super ();
    _sequencer = MidiSystem.getSequencer ( false );
    _sequencer.open ();
    try
    {
      setSynth ( synth, sb );
    }
    catch ( final SoundFileException e )
    {
      Logger.error ( "Error loading soundbank.", e );
    }
  }


  /**
   * @param delay The spread to set.
   */
  public void setArpeggioDelay ( final int delay )
  {
    _delay = delay;
  }


  /**
   * @return Returns the spread.
   */
  public int getArpeggioDelay ()
  {
    return _delay;
  }


  /**
   * @return Synthesizer being used for playback.
   */
  public Synthesizer getSynth ()
  {
    return _synth;
  }


  // TODO: Put this back?
//  public void setSynth ( final Synthesizer synth )
//  {
//    setSynth ( synth, null );
//  }


  /**
   * Sets the synthesizer the player will use to playback sequences.  The
   * instrument will be set to the first one available.
   *
   * @param synth Synthesizer to use for playback.
   * @param sb Soundbank to load into the synthesizer.
   *
   * @return true if the synth was set, false if it was already set.
   *
   * @throws MidiUnavailableException
   * @throws SoundFileException Couldn't load the soundbank.  Synth is okay though.
   */
  public boolean setSynth ( final Synthesizer synth, final Soundbank sb )
    throws
      MidiUnavailableException,
      SoundFileException
  {
    Logger.debugEx ( "midi.insts", "Player.setSynth: {0}, {1}", synth, sb );
    if ( null == _synth || !synth.getClass ().equals ( _synth.getClass () ) )
    {
      _synth = synth;
      connectToSequencer ();
      _synth.open ();
      Logger.debugEx ( "midi.insts", "Player.setSynth: NEW" );
      setSoundbank ( sb );
      return true;
    }
    setSoundbank ( sb );
    return false;
  }


  /**
   * @return Current Soundbank.
   */
  public Soundbank getSoundbank ()
  {
    Logger.debugEx ( "midi.insts", "getSoundbank: {0}", _soundbank );
    return _soundbank;
  }


  /**
   * @param sb Soundbank to set.
   *
   * @throws SoundFileException
   * @throws MidiUnavailableException
   */
  public void setSoundbank ( final Soundbank sb )
    throws
      SoundFileException,
      MidiUnavailableException
  {
    getSynth ().open ();
    Logger.debugEx ( "midi.insts", "setSoundbank: {0}, Default: {1}", sb, getSynth ().getDefaultSoundbank () );
    final Soundbank temp = ( null == sb ? getSynth ().getDefaultSoundbank () : sb );
    if ( getSynth ().isSoundbankSupported ( temp ) )
    {
      unloadAllInstruments ();
      _soundbank = temp;
      getSynth ().loadAllInstruments ( _soundbank );
      setInstrument ( _soundbank.getInstruments ()[0] );
    }
    else
    {
      _instrument = getSynth ().getAvailableInstruments ()[0];
      Logger.warn ( "Soundbank {0} not supported by synthesizer {1}", sb, getSynth () );
      throw new SoundFileException ( "Soundbank not supported by synthesizer." );
    }
  }


  /**
   * @param instrument The instrument to set.
   */
  public void setInstrument ( final Instrument instrument )
  {
    Logger.debugEx ( "midi.insts", "Setting instrument: {0}, patch {1}", new Object[] {instrument, instrument.getPatch ()} );
    if ( getSynth ().loadInstrument ( instrument ) )
    {
      _instrument = instrument;
    }
  }


  /**
   * Looks up an instrument from the synthesizer based on the specified patch.
   *
   * @param patch Patch to lookup.
   */
  public void setInstrument ( final Patch patch )
  {
    Logger.debugEx ( "midi.insts", "Player.setInstrument: {0}", patch );
    Instrument inst = null;
    final Soundbank sb = getSoundbank ();
    if ( sb != null )
    {
      inst = sb.getInstrument ( patch );
    }
    if ( null != inst )
    {
      setInstrument ( inst );
    }
  }


  /**
   * @return Returns the instrument.
   */
  public Instrument getInstrument ()
  {
    return _instrument;
  }


  /**
   * @param bpm The tempo sequences will be played at.
   */
  public void setBpm ( final int bpm )
  {
    _bpm = bpm;
  }


  /**
   * @return The tempo in beats per minute for the player.
   */
  public int getBpm ()
  {
    return _bpm;
  }


  /**
   * @param noteLength The noteLength to set.
   */
  public void setNoteLength ( final int noteLength )
  {
    _noteLength = noteLength;
  }


  /**
   * @return Returns the noteLength.
   */
  public int getNoteLength ()
  {
    return _noteLength;
  }


  /**
   * Unloads all loaded instruments from the current synthesizer.
   *
   * @throws MidiUnavailableException
   */
  public void unloadAllInstruments ()
    throws
      MidiUnavailableException
  {
    Logger.debugEx ( "midi", "unloadAllInstruments opening synth: {0}", getSynth ().isOpen () );
    getSynth ().open ();
    for ( final Instrument i : getSynth ().getLoadedInstruments () )
    {
      getSynth ().unloadInstrument ( i );
    }
  }


  /**
   * @param velocity The velocity to play notes.
   */
  public void setVelocity ( final int velocity )
  {
    _velocity = velocity;
  }


  /**
   * @return The velocity of played notes.
   */
  public int getVelocity ()
  {
    return _velocity;
  }


  /**
   * @param cascade The cascade to set.
   */
  public void setCascade ( final boolean cascade )
  {
    _cascade = cascade;
  }


  /**
   * @return Returns the cascade setting.
   */
  public boolean isCascade ()
  {
    return _cascade;
  }


  /**
   * @param listener Listener to add to the meta event listeners list.
   */
  public void setMetaListener ( final MetaEventListener listener )
  {
    _sequencer.addMetaEventListener ( listener );
  }


  /**
   * @return Instrument array of all loaded instruments.
   *
   * @throws MidiUnavailableException
   */
  public List<Instrument> getLoadedInstruments ()
    throws
      MidiUnavailableException
  {
    return MidiHelper.getLoadedInstruments ( getSynth () );
  }


  /**
   * @return Instrument array of all available instruments, this included all
   *         loaded instruments.
   */
  public List<Instrument> getAvailableInstruments ()
  {
    if ( null != getSoundbank () )
    {
      return new ArrayList<Instrument> ( Arrays.asList ( getSoundbank ().getInstruments () ) );
    }
    return new ArrayList<Instrument> ( 0 );
  }


  /**
   * Sound the specified interval.
   *
   * @param notes Chord to sound.
   *
   * @throws InvalidMidiDataException
   */
  public void play ( final int[] notes )
    throws
      InvalidMidiDataException
  {
    playSequence ( MidiHelper.buildSequence ( notes,
                                              _noteLength,
                                              _delay,
                                              _cascade,
                                              _velocity,
                                              _instrument ),
                   _bpm );
  }


  /**
   * Stops the currently playing sequence.
   */
  public void stop ()
  {
    Logger.debug ( "Stopping Player." );
    _sequencer.stop ();
  }


  /**
   * Plays the specified Sequence.
   *
   * @param sequence The Sequence to play.
   * @param bpm The tempo to play the sequence at in beats per minute.
   *
   * @throws InvalidMidiDataException
   */
  private void playSequence ( final Sequence sequence, final int bpm )
    throws
      InvalidMidiDataException
  {
    MidiHelper.dumpSequence ( sequence );
    _sequencer.setSequence ( sequence );
    _sequencer.setTempoInBPM ( bpm );
    _sequencer.start ();
  }


  private void connectToSequencer ()
    throws
      MidiUnavailableException
  {
    Logger.debugEx ( "midi", "Player.connectToSequencer: {0} -> {1}", _sequencer, getSynth () );
    _sequencer.getTransmitter ().setReceiver ( getSynth ().getReceiver () );
  }


  private int _bpm = 60;
  private int _velocity = 64;
  private int _noteLength = 32;
  private int _delay = 8;
  private boolean _cascade = false;
  private final Sequencer _sequencer;
  private Synthesizer _synth;
  private Soundbank _soundbank;
  private Instrument _instrument;

}
