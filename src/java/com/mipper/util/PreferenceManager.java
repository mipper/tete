/*
 * This file contains proprietary information of Rule Financial. 
 * Copying or reproduction without prior written approval is prohibited. 
 *
 * <b>Copyright</b> (c) 2004
 * <b>Company</b> Rule Financial
 */
package com.mipper.util;

import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.sound.midi.Instrument;
import javax.sound.midi.Patch;


/**
 * 
 * @author Cliff Evans
 * @version $Revision: 1.2 $
 */
public class PreferenceManager
{

  /**
   * @return A reference to the PreferenceManager.
   */
  public static PreferenceManager instanceOf ()
  {
    if ( _mgr == null )
    {
      _mgr = new PreferenceManager ();
    }
    return _mgr;
  }
  
  
  /**
   * @param evt
   */
  public void updateValue ( ItemEvent evt )
  {
//    Logger.debug ( "Event: {0}={1}", new Object[] {evt.getSource ().getClass ().getName (), evt.getItem ().getClass ()} );
    _root.put ( evt.getSource ().getClass ().getName (),
                evt.getItem ().toString () );
  }

  
  /**
   * Store the currently selected instrument.
   * 
   * @param inst Instrument to store.
   */
  public void setInstrument ( Instrument inst )
  {
    setPatch ( inst.getPatch () );
  }
  
  
  /**
   * Store the patch representing the currently selected instrument.
   * 
   * @param patch Patch to store.
   */
  public void setPatch ( Patch patch )
  {
    _root.putInt ( INSTRUMENT_BANK, patch.getBank () );
    _root.putInt ( INSTRUMENT_PROGRAM, patch.getProgram () );
  }
  
  
  /**
   * Retrieve the previously stored patch.
   * 
   * @return Patch representing the instrument.
   */
  public Patch getPatch ()
  {
    return new Patch ( _root.getInt ( INSTRUMENT_BANK, 0 ),
                       _root.getInt ( INSTRUMENT_PROGRAM, 0 ) );
  }
  
  
  /**
   * Returns the lowest octave to use for playback.
   * 
   * @return The index in the list of octaves of the bottom octave.
   */
  public int getBottomOctave ()
  {
    return _root.getInt ( BOTTOM_OCTAVE, DEFAULT_BOTTOM_OCTAVE );
  }
  
  
  /**
   * Store the index of the bottom octave used in playback.
   * 
   * @param idx Index of the bottom octave in the list of octaves.
   */
  public void setBottomOctave ( int idx )
  {
    _root.putInt ( BOTTOM_OCTAVE, idx );
  }
  
  
  /**
   * Returns the highest octave to use for playback.
   * 
   * @return The index in the list of octaves of the top octave.
   */
  public int getTopOctave ()
  {
    return _root.getInt ( TOP_OCTAVE, DEFAULT_TOP_OCTAVE );
  }
  
  
  /**
   * Store the index of the top octave used in playback.
   * 
   * @param idx Index of the top octave in the list of octaves.
   */
  public void setTopOctave ( int idx )
  {
    _root.putInt ( TOP_OCTAVE, idx );
  }
  
  
  /**
   * Get the stored length of notes during playback.
   * 
   * @return Stored length of notes.
   */
  public int getNoteLength ()
  {
    return _root.getInt ( NOTE_LENGTH, DEFAULT_NOTE_LENGTH );
  }
  
  
  /**
   * Store the length to use for a note during playback.
   * 
   * @param len The length to store.
   */
  public void setNoteLength ( int len )
  {
    _root.putInt ( NOTE_LENGTH, len );
  }
  
  
  /**
   * Retrieve the previously stored arpeggio delay.
   * 
   * @return Stored arpeggio delay.
   */
  public int getArpeggioDelay ()
  {
    return _root.getInt ( ARPEGGIO_DELAY, DEFAULT_ARPEGGIO_DELAY );
  }
  
  
  /**
   * Store the delay to use for arpeggiating notes during playback.
   * 
   * @param delay The delay to store.
   */
  public void setArpeggioDelay ( int delay )
  {
    _root.putInt ( ARPEGGIO_DELAY, delay );
  }
  
  
  /**
   * @return The value of the cascade flag.
   */
  public boolean getCascade ()
  {
    return _root.getBoolean ( CASCADE, DEFAULT_CASCADE );
  }
  
  
  /**
   * @param value The value of the cascade flag.
   */
  public void setCascade ( boolean value )
  {
    _root.putBoolean ( CASCADE, value );
  }
  
  
  /**
   * Get the type of pattern to play.
   * 
   * @return Index of the pattern type in the list of types.
   */
  public int getPatternType ()
  {
    return _root.getInt ( PATTERN_TYPE, DEFAULT_PATTERN_TYPE );
  }
  
  
  /**
   * Store the currently playing pattern type.
   * 
   * @param idx Index of the pattern type in the list of types.
   */
  public void setPatternType ( int idx )
  {
    _root.putInt ( PATTERN_TYPE, idx );
  }
  
  
  /**
   * Store the indexes of the selected patterns in a pattern list.
   * 
   * @param type Name of the pattern type.
   * @param patterns Array of indexes of the selected patterns within the type.
   * 
   * @throws IOException
   */
  public void setSelectedPatterns ( String type, int[] patterns )
    throws
      IOException
  {
    _root.putByteArray ( String.format ( PATTERN_LIST, type ).toLowerCase (),
                         object2Bytes ( patterns ) );
  }
  
  
  /**
   * Retrieves the indexes of the selected patterns in a pattern list.
   * 
   * @param type Name of the pattern type.
   * 
   * @return Array of indexes of the selected patterns within the type.
   * 
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public int[] getSelectedPatterns ( String type )
    throws
      IOException,
      ClassNotFoundException
  {
    byte[] b = _root.getByteArray ( String.format ( PATTERN_LIST, type ).toLowerCase (), null );
    if ( b == null )
    {
      return new int[] {};
    }
    return ( int[] ) bytes2Object ( b );
  }
  
  
  /**
   * Returns the index of the root note used during playback.
   * 
   * @param idx Index of the root note in the list of notes.
   */
  public void setRootNote ( int idx )
  {
    _root.putInt ( NOTE_NAME, idx );
  }
  
  
  /**
   * @return The index of the note in the list of notes to use as the root.
   */
  public int getRootNote ()
  {
    return _root.getInt ( NOTE_NAME, DEFAULT_NOTE );
  }
  
  
  /**
   * Stores the index of the order to playback notes.
   * 
   * @param order Index of the order to use during playback.
   */
  public void setNoteOrder ( int order )
  {
    _root.putInt ( NOTE_ORDER, order );
  }
  
  
  /**
   * @return Index into the list of possible playback orders to use.
   */
  public int getNoteOrder ()
  {
    return _root.getInt ( NOTE_ORDER, DEFAULT_NOTE_ORDER );
  }
  
  
  /**
   * Store the current size and position of the main window.
   * 
   * @param r Rectangle representing the current position and size of the main
   *          window.
   * 
   * @throws IOException
   */
  public void setSizePos ( Rectangle r )
    throws
      IOException
  {
    _root.putByteArray( POS_SIZE, object2Bytes ( r ) );
  }
  
  
  /**
   * @return The stored size and position rectangle for the main window.
   * 
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public Rectangle getSizePos ()
    throws 
      IOException, 
      ClassNotFoundException
  {
    return ( Rectangle ) bytes2Object ( _root.getByteArray ( POS_SIZE, null ) );
  }
  
  
  /**
   * Reset all preferences back to their defaults.
   * 
   * @throws BackingStoreException
   */
  public void reset ()
    throws
      BackingStoreException
  {
    _root.removeNode ();
  }
  
  
  /**
   * Constructor.
   */
  protected PreferenceManager ()
  {
    super ();
    _root = Preferences.userRoot ().node ( PREF_ROOT );
  }
  
  
  /**
   * @return Gets a reference to the root of the tree managed by this manager.
   */
  public Preferences getRoot ()
  {
    return _root;
  }
  
  
  static private byte[] object2Bytes ( Object o )
    throws
      IOException
  {
    ByteArrayOutputStream baos = new ByteArrayOutputStream ();
    ObjectOutputStream oos = new ObjectOutputStream ( baos );
    oos.writeObject ( o );
    return baos.toByteArray ();
  }


  static private Object bytes2Object ( byte raw[] )
    throws
      IOException,
      ClassNotFoundException
  {
    if ( raw == null )
    {
      return null;
    }
    ByteArrayInputStream bais = new ByteArrayInputStream ( raw );
    ObjectInputStream ois = new ObjectInputStream ( bais );
    Object o = ois.readObject ();
    return o;
  }
  
  
  private Preferences _root;
  private static PreferenceManager _mgr;
  private static final String PREF_ROOT = "/com/mipper/music/tete";
  private static final String INSTRUMENT_BANK = "playback/instrument/bank";
  private static final String INSTRUMENT_PROGRAM = "playback/instrument/program";
  private static final String BOTTOM_OCTAVE = "playback/bottom_octave";
  private static final String TOP_OCTAVE = "playback/top_octave";
  private static final String NOTE_ORDER = "playback/note_order";
  private static final String NOTE_NAME = "playback/note_name";
  private static final String NOTE_LENGTH = "playback/length";
  private static final String ARPEGGIO_DELAY = "playback/arpeggio_delay";
  private static final String CASCADE = "playback/cascade";
  private static final String PATTERN_TYPE = "sounds/type";
  private static final String PATTERN_LIST = "sounds/type/%s";
  private static final String POS_SIZE = "window/pos_size";
  private static final int DEFAULT_BOTTOM_OCTAVE = 3;
  private static final int DEFAULT_TOP_OCTAVE = 6;
  private static final int DEFAULT_NOTE_LENGTH = 32;
  private static final int DEFAULT_NOTE = 1;
  private static final int DEFAULT_ARPEGGIO_DELAY = 8;
  private static final boolean DEFAULT_CASCADE = false;
  private static final int DEFAULT_PATTERN_TYPE = 1;
  private static final int DEFAULT_NOTE_ORDER = 1;
    
}
