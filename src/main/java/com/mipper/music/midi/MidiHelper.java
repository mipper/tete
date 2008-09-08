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
import javax.sound.midi.Sequencer;
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
   * Builds a sequence of notes.
   *
   * @param notes An array containing the values of each note in the sequence.
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
  public static Sequence buildSequence ( int[] notes,
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
    for ( int i = 0; i < notes.length; i++ )
    {
      Logger.debugEx ( "midi", "Adding note: {0} for {1}, velocity: {2}", notes[i], noteLength, velocity );
      track.add ( createNoteEvent ( ShortMessage.NOTE_ON,
                                    notes[i],
                                    velocity,
                                    i * arpeggioDelay ) );
      track.add ( createNoteEvent ( ShortMessage.NOTE_OFF,
                                    notes[i],
                                    velocity,
                                    ( cascade ? notes.length - 1 : i ) * arpeggioDelay + noteLength ) );
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
   */
  public static List<Instrument> getLoadedInstruments ( Synthesizer synth )
    throws
      MidiUnavailableException
  {
    if ( !synth.isOpen () )
    {
      synth.open ();
    }
    Logger.debug ( "Getting loaded instruments from synthesizer: " + synth.getDeviceInfo ().getName () );
    List<Instrument> instruments = Arrays.asList ( synth.getLoadedInstruments () );
//    synth.close ();
    if ( instruments == null || instruments.isEmpty () )
    {
      Logger.info ( "No loaded instruments for synthesizer {0}", synth.getDeviceInfo ().getName () );
//      throw new MidiException ( "The MIDI soundbank cannot be found.  Check that one of the soundbank files is in your JRE's lib/audio folder." );
    }
    return instruments;
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
      Logger.debug ( "Getting loaded instruments from synthesizer: " + synth.getDeviceInfo ().getName () );
    }
    List<Instrument> instruments = Arrays.asList ( synth.getAvailableInstruments () );
//    synth.close ();
    if ( instruments == null || instruments.isEmpty () )
    {
      Logger.warn ( "Can''t find any available instruments for synthesizer {0}", synth.getDeviceInfo ().getName () );
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
   * @param incInst true to include instruments availabe in the synthesizer.
   * 
   * @return String detailing the MidiDevices available.
   */
  public static String getMidiInfo ( boolean incInst )
  {
    StringBuffer buf = new StringBuffer ();
    try
    {
      for ( MidiDevice.Info info: MidiSystem.getMidiDeviceInfo () )
      {
        dumpDeviceInfo ( info, buf, incInst );
      }
    }
    catch ( MidiUnavailableException e )
    {
      buf.append ( "Cannot get MIDI information.\n" ).append ( e );
    }
    return buf.toString ();
  }
  
  
  private static void setInstrument ( Track track, Instrument instrument )
    throws
      InvalidMidiDataException
  {
    setPatch ( track, instrument.getPatch () );
  }


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


  private static void dumpDeviceInfo ( Info info,
                                       StringBuffer buf,
                                       boolean incInst )
    throws
      MidiUnavailableException
  {
    buf.append ( info.getName () )
       .append ( "\n\t" ).append ( info.getDescription () )
       .append ( "\n\t" ).append ( info.getVendor () )
       .append ( "\n\t" ).append ( info.getVersion () );
    MidiDevice dev = MidiSystem.getMidiDevice ( info );
    if ( dev instanceof Synthesizer )
    {
      dumpSynthInfo ( ( Synthesizer ) dev, buf, incInst );
    }
    if ( dev instanceof Sequencer )
    {
      dumpSeqInfo ( ( Sequencer ) dev, buf );
    }
    buf.append ( "\n" );
  }


  private static void dumpSynthInfo ( Synthesizer synth, 
                                      StringBuffer buf, 
                                      boolean incInst )
    throws
      MidiUnavailableException
  {
    boolean open = synth.isOpen ();
    if ( !open )
    {
      synth.open ();
    }
    try
    {
      buf.append ( "\n\tMax Polyphony: " ).append ( synth.getMaxPolyphony () )
         .append ( "\n\tMax Transmitters: " ).append ( synth.getMaxTransmitters () )
         .append ( "\n\tMax Receivers: " ).append ( synth.getMaxReceivers () );
      if ( incInst )
      {
        dumpSoundbank ( synth.getDefaultSoundbank (), buf );
      }
    }
    finally
    {
      if ( !open )
      {
        synth.close ();
      }
    }
  }
  
  
  /**
   * @param synth
   * @param buf
   * @throws MidiUnavailableException
   */
  private static void dumpSeqInfo ( Sequencer synth, StringBuffer buf )
    throws
      MidiUnavailableException
  {
    boolean open = synth.isOpen ();
    if ( !open )
    {
      synth.open ();
    }
    try
    {
      buf.append ( "\n\tMax Transmitters: " ).append ( synth.getMaxTransmitters () )
         .append ( "\n\tMax Receivers: " ).append ( synth.getMaxReceivers () );
    }
    finally
    {
      if ( !open )
      {
        synth.close ();
      }
    }
  }
  
  
  private static void dumpSoundbank ( Soundbank sb, StringBuffer buf )
  {
    if ( null != sb )
    {
      buf.append ( "\nSoundBank:" )
         .append ( "\n\t" ).append ( sb.getName () )
         .append ( "\n\t" ).append ( sb.getDescription () )
         .append ( "\n\t" ).append ( sb.getVendor () )
         .append ( "\n\t" ).append ( sb.getVersion () );
      for ( Instrument i: sb.getInstruments () )
      {
        buf.append ( "\n\t" ).append ( i );
      }
    }
  }

  
//  private static void dumpInstruments ( Synthesizer synth, StringBuffer buf )
//  {
//    buf.append ( "\nAvailable Instruments:" );
//    for ( Instrument i: synth.getAvailableInstruments () )
//    {
//      buf.append ( "\n\t" ).append ( i ).append ( " : " ).append ( i.getPatch () );
//    }
//    buf.append ( "\nLoaded Instruments:" );
//    for ( Instrument i: synth.getLoadedInstruments () )
//    {
//      buf.append ( "\n\t" ).append ( i );
//    }
//  }
  
  
  /**
   * Constructor.
   */
  private MidiHelper ()
  {
    // hide default constructor
  }

}
