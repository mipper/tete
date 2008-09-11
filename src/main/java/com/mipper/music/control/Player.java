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
package com.mipper.music.control;

import java.util.List;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;

import com.mipper.music.midi.MidiException;
import com.mipper.music.midi.MidiHelper;
import com.mipper.util.Logger;


// TODO: Move this to the midi package
/**
 * A wrapper class to make playing midi notes more simple.
 *
 * @author Cliff Evans
 * @version $Revision: 1.3 $
 */
public class Player
{

  /**
   * Constructor.
   *
   * @throws MidiUnavailableException
   * @throws MidiException
   */
  public Player ()
    throws
      MidiUnavailableException,
      MidiException
  {
    super ();
    _sequencer = MidiSystem.getSequencer ( false );
    _sequencer.open ();
//    _synth = MidiHelper.getAvailableSynthesizers ().get ( 0 );
    setSynth ( MidiHelper.getAvailableSynthesizers ().get ( 0 ) );
    _instrument = getAvailableInstruments ().get ( 0 );
  }


  /**
   * Constructor.
   * 
   * @param synth Synthesizer to be used for playback.
   * @param instrument MIDI id of the instrument to use.
   */
  public Player ( Synthesizer synth, Instrument instrument )
  {
    super ();
    setSynth ( synth );
    _instrument = instrument;
  }

  
  /**
   * Constructor.
   *
   * @param instrument MIDI id of the instrument to use.
   * @param noteLength Length of time to hold the notes.
   * @param spread Time between the first and second notes.
   */
  public Player ( Instrument instrument, int noteLength, int spread )
  {
    super ();
    _instrument = instrument;
    _noteLength = noteLength;
    _delay = spread;
  }


  /**
   * @param spread The spread to set.
   */
  public void setArpeggioDelay ( int spread )
  {
    _delay = spread;
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
  
  
  /**
   * Sets the synthesize the player will use to playback sequences.  The
   * instrument will be set to the first one available.
   *
   * @param synth Synthesizer to use for playback.
   */
  public void setSynth ( Synthesizer synth )
  {
    _synth = synth;
    try
    {
      List<Instrument> insts = getLoadedInstruments ();
      if ( null != insts && insts.size () > 0 )
      {
        _instrument = insts.get ( 0 );
      }
      else
      {
        _instrument = getAvailableInstruments ().get ( 0 );
      }
      Logger.debug ( "Player.setSynth selected instrument: " + _instrument );
      _sequencer.getTransmitter ().setReceiver ( synth.getReceiver () );
    }
    catch ( MidiUnavailableException e )
    {
      Logger.error ( e );
      _instrument = null;
    }
    catch ( MidiException e )
    {
      Logger.error ( e );
      _instrument = null;
    }
  }

  
  /**
   * @param bpm The tempo sequences will be played at.
   */
  public void setBpm ( int bpm )
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
   * @param instrument The instrument to set.
   */
  public void setInstrument ( Instrument instrument )
  {
    Logger.debugEx ( "midi", "Setting instrument: {0}, patch {1}", new Object[] {instrument, instrument.getPatch ()} );
    _instrument = instrument;
  }


  /**
   * @return Returns the instrument.
   */
  public Instrument getInstrument ()
  {
    return _instrument;
  }


  /**
   * @param noteLength The noteLength to set.
   */
  public void setNoteLength ( int noteLength )
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
   * @return Current Soundbank.
   */
  public Soundbank getSoundbank ()
  {
    return MidiHelper.getCurrentSoundbank ( _synth );
  }


  /**
   * @param velocity The velocity to play notes.
   */
  public void setVelocity ( int velocity )
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
  public void setCascade ( boolean cascade )
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
  public void setMetaListener ( MetaEventListener listener )
  {
    _sequencer.addMetaEventListener ( listener );
  }


  /**
   * @return Instrument array of all available instruments.
   */
  public List<Synthesizer> getAvailableSynths ()
  {
    return MidiHelper.getAvailableSynthesizers ();
  }


  /**
   * @return Instrument arrqay of all loaded instruments.
   *
   * @throws MidiUnavailableException
   * @throws MidiException
   */
  public List<Instrument> getLoadedInstruments ()
    throws
      MidiUnavailableException,
      MidiException
  {
    List<Instrument> insts = MidiHelper.getLoadedInstruments ( _synth );
    if ( insts == null || insts.size () == 0 )
    {
      insts = MidiHelper.getAvailableInstruments ( _synth );
    }
    return insts;
  }


  /**
   * @return Instrument arrqay of all available instruments.
   *
   * @throws MidiUnavailableException
   * @throws MidiException
   */
  public List<Instrument> getAvailableInstruments ()
    throws
      MidiUnavailableException,
      MidiException
  {
    return MidiHelper.getAvailableInstruments ( _synth );
  }


  /**
   * Sound the specified interval.
   *
   * @param notes Chord to sound.
   *
   * @throws InvalidMidiDataException
   */
  public void play ( int[] notes )
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
  private void playSequence ( Sequence sequence, int bpm )
    throws
      InvalidMidiDataException
  {
    _sequencer.setSequence ( sequence );
    _sequencer.setTempoInBPM ( bpm );
    _sequencer.start ();
  }


//  private List<Instrument> getAvailableInstruments ( Synthesizer synth )
//    throws
//      MidiUnavailableException,
//      MidiException
//  {
//    return MidiHelper.getAvailableInstruments ( synth );
////    if ( _instruments == null )
////    {
////      _instruments = MidiHelper.getAvailableInstruments ( synth );
////    }
////    return _instruments;
//  }


  private static final int NOTE_VELOCITY = 64;
  private static final int BPM = 60;

  private int _bpm = BPM;
  private int _velocity = NOTE_VELOCITY;
  private int _noteLength = 32;
  private int _delay = 8;
  private boolean _cascade = false;
  private Sequencer _sequencer;
  private Synthesizer _synth;
  private Instrument _instrument;
//  private List<Instrument> _instruments;


}
