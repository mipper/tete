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

import java.io.IOException;
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
 * @version $Id$
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
  public static Sequence buildSequence ( final int[] notes,
                                         final int noteLength,
                                         final int arpeggioDelay,
                                         final boolean cascade,
                                         final int velocity,
                                         final Instrument instrument )
    throws
      InvalidMidiDataException
  {
    final Sequence sequence = new Sequence ( Sequence.PPQ, 16 );
    final Track track = sequence.createTrack ();
    setInstrument ( track, instrument );
    for ( int i = 0; i < notes.length; i++ )
    {
//      Logger.debugEx ( "midi.notes", "Instrument: {3} Adding note: {0} for {1}, velocity: {2}", notes[i], noteLength, velocity, instrument );
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
//        Logger.debug ( String.format ( "MIDI device Name: %s, Class: %s", info.getName (), device.getClass ().getName () ) );
        if ( device instanceof Synthesizer )
        {
          final Synthesizer synth = ( Synthesizer ) device;
          synths.add ( synth );
//          Logger.debug ( String.format ( "Synthisizer Name: %s, Latency: %d, Voices: %d", info.getName(), Long.valueOf ( synth.getLatency () ), Long.valueOf ( synth.getMaxPolyphony () ) ) );
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
  public static List<Instrument> getLoadedInstruments ( final Synthesizer synth )
    throws
      MidiUnavailableException
  {
    synth.open ();
    Logger.debugEx ( "midi.insts", "Getting loaded instruments from synthesizer: " + synth.getDeviceInfo ().getName () );
    final List<Instrument> instruments = new ArrayList<Instrument> ( Arrays.asList ( synth.getLoadedInstruments () ) );
    if ( instruments.isEmpty () )
    {
      Logger.info ( "No loaded instruments for synthesizer {0}", synth.getDeviceInfo ().getName () );
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
   */
  public static List<Instrument> getAvailableInstruments ( final Synthesizer synth )
    throws
      MidiUnavailableException
  {
    synth.open ();
    final List<Instrument> instruments = Arrays.asList ( synth.getAvailableInstruments () );
    if ( instruments == null || instruments.isEmpty () )
    {
      Logger.warn ( "Can''t find any available instruments for synthesizer {0}", synth.getDeviceInfo ().getName () );
//      throw new SoundException ( "Can't find any available instruments.  Check that one of the soundbank files is in your JRE's lib/audio folder." );
    }
    return instruments;
  }


  /**
   * @param track Track to insert instrument change to.
   * @param instrument Instrument to change to.
   *
   * @throws InvalidMidiDataException
   */
  public static void setInstrument ( final Track track,
                                     final Instrument instrument )
    throws
      InvalidMidiDataException
  {
    setPatch ( track, instrument.getPatch () );
  }


  /**
   * Adds a midi event to the track to change the patch used for playback.
   *
   * @param track Track to add the event to.
   * @param patch Patch to use for playback.
   *
   * @return Track object events have been added to.
   *
   * @throws InvalidMidiDataException
   */
  public static Track setPatch ( final Track track, final Patch patch )
    throws
      InvalidMidiDataException
  {
    changeBank ( track, patch.getBank () );
    return changeProgram ( track, patch.getProgram () );
  }


  /**
   * @param track Track to change program for.
   * @param program Program number to change to.
   *
   * @return Track object events have been added to.
   *
   * @throws InvalidMidiDataException
   */
  public static Track changeProgram ( final Track track, final int program )
    throws
      InvalidMidiDataException
  {
    final ShortMessage message = new ShortMessage ();
    message.setMessage ( ShortMessage.PROGRAM_CHANGE,
                         0,
                         program,
                         0 );
    track.add ( new MidiEvent ( message, 0 ) );
    return track;
  }


  /**
   * @param track Track to change program for.
   * @param bank Bank number to change to.
   *
   * @return Track object events have been added to.
   *
   * @throws InvalidMidiDataException
   */
  public static Track changeBank ( final Track track, final int bank )
    throws
      InvalidMidiDataException
  {
    Logger.debugEx ( "midi", "changeBank: 0x{0}:b{1}:{2}", Integer.toHexString ( bank ), Integer.toBinaryString ( bank ), bank );
    ShortMessage message = new ShortMessage ();
    message.setMessage ( ShortMessage.CONTROL_CHANGE,
                         0,
                         0,
                         ( bank >> 7 ) & 0x7f );
    track.add ( new MidiEvent ( message, 0 ) );
    message = new ShortMessage ();
    message.setMessage ( ShortMessage.CONTROL_CHANGE,
                         0,
                         0x20,
                         bank & 0x7f );
    track.add ( new MidiEvent ( message, 0 ) );
    return track;
  }


  /**
   * @param incInst true to include instruments availabe in the synthesizer.
   *
   * @return String detailing the MidiDevices available.
   */
  public static String getMidiInfo ( final boolean incInst )
  {
    final StringBuffer buf = new StringBuffer ();
    try
    {
      for ( final MidiDevice.Info info: MidiSystem.getMidiDeviceInfo () )
      {
        dumpDeviceInfo ( info, buf, incInst );
      }
    }
    catch ( final MidiUnavailableException e )
    {
      buf.append ( "Unable to get MIDI information.\n" ).append ( e );
    }
    return buf.toString ();
  }


  /**
   * @param seq Sequence to convert to a string.
   *
   * @return String representation of the sequence.
   */
  public static String sequence2String ( final Sequence seq )
  {
    final StringBuffer buf = new StringBuffer ();
    for ( final Track t : seq.getTracks () )
    {
      buf.append ( "Track " ).append ( t ).append ( "\n\t" );
      for ( int i = 0; i < t.size (); i++ )
      {
        dumpEvent ( buf, t.get ( i ) );
        buf.append ( " " );
      }
      buf.append ( "\n" );
    }
    return buf.toString ();
  }


  /**
   * @param seq Sequencer whose status will be recorded.
   * @param synth Synthesizer whose status will be recorded.
   */
  public static void dumpMidiStatus ( final Sequencer seq,
                                      final Synthesizer synth )
  {
    Logger.debugEx ( "midi", "Seq:\t{0}\n\t{1}\nSynth:\t{2}\n\t{3}",
                     seq.getDeviceInfo ().getName (),
                     seq.isOpen (),
                     synth.getDeviceInfo ().getName (),
                     synth.isOpen () );
  }


  /**
   * @param sequence Sequence to dump as a string.
   */
  public static void dumpSequence ( final Sequence sequence )
  {
    try
    {
      MidiSystem.write ( sequence, 1, new java.io.File ( "/tmp/sequence.midi" ) );
      Logger.debugEx ( "midi", "Sequence:\n{0}", sequence2String ( sequence ) );
    }
    catch ( final IOException e )
    {
      Logger.error ( "Error writing MIDI file.", e );
    }
  }


  private static MidiEvent createNoteEvent ( final int type,
                                             final int note,
                                             final int velocity,
                                             final int time )
    throws
      InvalidMidiDataException
  {
    final ShortMessage message = new ShortMessage ();
    message.setMessage ( type, 0, note, velocity );
    return new MidiEvent ( message, time );
  }


  private static void dumpEvent ( final StringBuffer buf, final MidiEvent e )
  {
    buf.append ( "[" ).append ( e.getTick () ).append ( ":" );
    for ( int i = 0; i < e.getMessage ().getLength (); i++ )
    {
      buf.append ( " " ).append ( ( e.getMessage ().getMessage ()[i] & 0xFF ) );
    }
    buf.append ( "]" );
  }


  private static void dumpDeviceInfo ( final Info info,
                                       final StringBuffer buf,
                                       final boolean incInst )
    throws
      MidiUnavailableException
  {
    buf.append ( info.getName () )
       .append ( "\n\t" ).append ( info.getDescription () )
       .append ( "\n\t" ).append ( info.getVendor () )
       .append ( "\n\t" ).append ( info.getVersion () );
    final MidiDevice dev = MidiSystem.getMidiDevice ( info );
    if ( dev instanceof Synthesizer )
    {
      dumpSynthInfo ( ( Synthesizer ) dev, buf, incInst );
    }
    if ( dev instanceof Sequencer )
    {
      dumpSequencerInfo ( ( Sequencer ) dev, buf );
    }
    buf.append ( "\n" );
  }


  private static void dumpSynthInfo ( final Synthesizer synth,
                                      final StringBuffer buf,
                                      final boolean incInst )
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
  private static void dumpSequencerInfo ( final Sequencer synth, final StringBuffer buf )
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


  private static void dumpSoundbank ( final Soundbank sb, final StringBuffer buf )
  {
    if ( null != sb )
    {
      buf.append ( "\nSoundBank:" )
         .append ( "\n\t" ).append ( sb.getName () )
         .append ( "\n\t" ).append ( sb.getDescription () )
         .append ( "\n\t" ).append ( sb.getVendor () )
         .append ( "\n\t" ).append ( sb.getVersion () );
      for ( final Instrument i: sb.getInstruments () )
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
