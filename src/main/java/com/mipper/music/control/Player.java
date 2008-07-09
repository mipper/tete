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

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Soundbank;
import com.mipper.music.midi.MidiException;
import com.mipper.music.midi.MidiHelper;


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
    _helper = new MidiHelper ();
    _instrument = _helper.getAvailableInstruments()[0];
    // TODO: remove me
    _helper.getAvailableSynthesizers ();
  }
  
  
  /**
   * Constructor.
   * 
   * @param instrument MIDI id of the instrument to use.
   */
  public Player ( Instrument instrument )
  {
    super ();
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
   * @return The tempo in beats per minute for the player.
   */
  public int getBpm ()
  {
    return _bpm;
  }

  
  /**
   * @param bpm The tempo sequences will be played at.
   */
  public void setBpm ( int bpm )
  {
    _bpm = bpm;
  }

  
  /**
   * @return The velocity of played notes.
   */
  public int getVelocity ()
  {
    return _velocity;
  }

  
  /**
   * @param velocity The velocity to play notes.
   */
  public void setVelocity ( int velocity )
  {
    _velocity = velocity;
  }
  

  /**
   * @return Instrument arrqay of all available instruments.
   * 
   * @throws MidiUnavailableException
   * @throws MidiException
   */
  public Instrument[] getAvailableInstruments ()
    throws
      MidiUnavailableException,
      MidiException
  {
    return _helper.getAvailableInstruments ();
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
    return _helper.getCurrentSoundbank ();
  }
  
  
  /**
   * @return Returns the instrument.
   */
  public Instrument getInstrument ()
  {
    return _instrument;
  }


  
  /**
   * @param instrument The instrument to set.
   */
  public void setInstrument ( Instrument instrument )
  {
    _instrument = instrument;
  }


  
  /**
   * @return Returns the noteLength.
   */
  public int getNoteLength ()
  {
    return _noteLength;
  }


  
  /**
   * @param noteLength The noteLength to set.
   */
  public void setNoteLength ( int noteLength )
  {
    _noteLength = noteLength;
  }


  
  /**
   * @return Returns the spread.
   */
  public int getArpeggioDelay ()
  {
    return _delay;
  }


  
  /**
   * @param spread The spread to set.
   */
  public void setArpeggioDelay ( int spread )
  {
    _delay = spread;
  }

  
  /**
   * @return Returns the cascade setting.
   */
  public boolean isCascade ()
  {
    return _cascade;
  }

  
  /**
   * @param cascade The cascade to set.
   */
  public void setCascade ( boolean cascade )
  {
    _cascade = cascade;
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
    _helper.playSequence ( _helper.buildChordSequence ( notes, 
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
    _helper.stop ();
  }

  
  /**
   * @param listener Listener to add to the meta event listeners list.
   */
  public void setMetaListener ( MetaEventListener listener )
  {
    _helper.getSequencer ().addMetaEventListener ( listener );
  }
  
  
  private static final int NOTE_VELOCITY = 64;
  private static final int BPM = 60;
  
  private MidiHelper _helper;
  private int _bpm = BPM;
  private int _velocity = NOTE_VELOCITY;
  private int _noteLength = 32;
  private int _delay = 8;
  private boolean _cascade = false;
  private Instrument _instrument = null;
  
}
