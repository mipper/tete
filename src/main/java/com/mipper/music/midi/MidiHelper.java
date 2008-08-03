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
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Patch;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;
import javax.sound.midi.MidiDevice.Info;

import com.mipper.util.Logger;


/**
 * Class implementing methods to ease playing of sounds via midi.
 *
 * @author Cliff Evans
 * @version $Revision: 1.4 $
 */
public class MidiHelper
{

  /**
   * Builds a sequence containing an interval.
   *
   * @param chord An array containing the values of each note in the chord.
   * @param noteLength The duration, in MIDI ticks, of each note.
   * @param arpeggioDelay The time, in MIDI ticks, between each note of the
   *                      chord.
   * @param cascade Whether to stack the notes i.e. sustain all notes until the
   *                last note has stopped sounding.
   * @param velocity Velocity at which each note is played.
   * @param instrument The midi ID of the instrument to use.
   *
   * @return Sequence to set the instrument and play the specified diad.
   *
   * @throws InvalidMidiDataException
   */
  public static Sequence buildChordSequence ( int[] chord,
                                              int noteLength,
                                              int arpeggioDelay,
                                              boolean cascade,
                                              int velocity,
                                              Instrument instrument )
    throws
      InvalidMidiDataException
  {
    final Sequence sequence = new Sequence ( Sequence.PPQ, 16 );
    final Track track = sequence.createTrack ();
    setInstrument ( track, instrument );
    for ( int i = 0; i < chord.length; i++ )
    {
      track.add ( createNoteEvent ( ShortMessage.NOTE_ON,
                                    chord[i],
                                    velocity,
                                    i * arpeggioDelay ) );
      track.add ( createNoteEvent ( ShortMessage.NOTE_OFF,
                                    chord[i],
                                    velocity,
                                    ( cascade ? chord.length - 1 : i ) * arpeggioDelay + noteLength ) );
    }
    return sequence;
  }


  /**
   * @return List of available synthesizers. 
   */
  public static List<Synthesizer> getAvailableSynthesizers ()
  {
    final ArrayList<Synthesizer> synths = new ArrayList<Synthesizer> ();
    final Info[] infos = MidiSystem.getMidiDeviceInfo ();
    for ( final Info info : infos )
    {
      try
      {
        final MidiDevice device = MidiSystem.getMidiDevice ( info );
        Logger.debug ( String.format ( "MIDI device Name: %s, Class: %s", info.getName (), device.getClass ().getName () ) );
        if ( device instanceof Synthesizer )
        {
          final Synthesizer synth = ( Synthesizer ) device;
          synths.add ( synth );
          Logger.debug ( String.format ( "Synthisizer Name: %s, Latency: %d, Voices: %d", info.getName(), Long.valueOf ( synth.getLatency () ), Long.valueOf ( synth.getMaxPolyphony () ) ) );
        }
      }
      catch ( final MidiUnavailableException e )
      {
        Logger.debug ( "Error getting Midi device: " + info );
      }
    }
    return synths;
  }


  /**
   * @param synth Synthisizer from which the soundbank will come.
   * 
   * @return Soundbank used by the synthesiser.
   */
  public static Soundbank getCurrentSoundbank ( Synthesizer synth )
  {
    return synth.getDefaultSoundbank ();
  }


  /**
   * Returns a list of available instruments in the MIDI system. If the JRE
   * doesn't have a soundbank installed, it looks for soundbank-min.gm in
   * the classpath.
   *
   * @param synth Synthesizer to be used for playback.
   * 
   * @return Instrument array of all available instruments.
   *
   * @throws MidiUnavailableException
   * @throws MidiException
   */
  public static List<Instrument> getAvailableInstruments ( Synthesizer synth )
    throws
      MidiUnavailableException,
      MidiException
  {
    if ( !synth.isOpen () )
    {
      synth.open ();
      Logger.debug ( "Getting instruments from synthesizer: " + synth.getDeviceInfo ().getName () );
    }
    List<Instrument> instruments = Arrays.asList ( synth.getAvailableInstruments () );
    synth.close ();
    if ( instruments == null || instruments.isEmpty () )
    {
      Logger.warn ( "getAvailableInstruments(): Can't find soundbank for synthesizer {0}", synth.getDeviceInfo ().getName () );
      throw new MidiException ( "The MIDI soundbank cannot be found.  Check that one of the soundbank files is in your JRE's lib/audio folder." );
    }
    return instruments;
  }


  /**
   * Adds a midi event to the track to change the patch used for playback.
   *
   * @param track Track to add the event to.
   * @param patch Patch to use for playback.
   *
   * @throws InvalidMidiDataException
   */
  public static void setPatch ( Track track, Patch patch )
    throws
      InvalidMidiDataException
  {
    final ShortMessage message = new ShortMessage ();
    message.setMessage ( ShortMessage.PROGRAM_CHANGE,
                         0,
                         patch.getProgram (),
                         patch.getBank () );
    track.add ( new MidiEvent ( message, 0 ) );
  }

  
  /**
   * Adds a midi event to the track to change the patch used for playback.
   *
   * @param track Track to add the event to.
   * @param instrument Instrument to use for playback.
   *
   * @throws InvalidMidiDataException
   */
  private static void setInstrument ( Track track, Instrument instrument )
    throws
      InvalidMidiDataException
  {
    setPatch ( track, instrument.getPatch () );
  }


  /**
   * @param type
   * @param note
   * @param velocity
   * @param time
   *
   * @return MidiEvent representing the note event.
   *
   * @throws InvalidMidiDataException
   */
  private static MidiEvent createNoteEvent ( int type,
                                             int note,
                                             int velocity,
                                             int time )
    throws
      InvalidMidiDataException
  {
    final ShortMessage message = new ShortMessage ();
    message.setMessage ( type, 0, note, velocity );
    return new MidiEvent ( message, time );
  }


  /**
   * Constructor.
   */
  private MidiHelper ()
  {
    // hide default constructor
  }

}
