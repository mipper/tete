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

import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.swing.event.EventListenerList;

import com.mipper.music.generate.EmptyException;
import com.mipper.music.generate.PatternPlayerModel;
import com.mipper.music.midi.MidiException;
import com.mipper.music.model.Sound;
import com.mipper.util.Logger;


/**
 * Plays a sequence of sounds derived from the model until told to stop.
 *
 * @author Cliff Evans
 * @version $Revision: 1.3 $
 */
public class ContinuousPlayer
{

  /**
   * Constructor.
   *
   * @param model PatternPlayerModel controlling what will be played.
   */
  public ContinuousPlayer ( PatternPlayerModel model )
  {
    super ();
    _model = model;
  }


  /**
   * Add an event listener for sounds.
   *
   * @param listener SoundEventListener to add.
   */
  public void addSoundEventListener ( SoundEventListener listener )
  {
    listenerList.add ( SoundEventListener.class, listener );
  }


  /**
   * Remove a SoundEventListener.
   *
   * @param listener SoundEventListener to remove.
   */
  public void removeSoundEventListener ( SoundEventListener listener )
  {
    listenerList.remove ( SoundEventListener.class, listener );
  }


  /**
   * Start playing random sounds derived from the model.
   *
   * @throws MidiException
   * @throws EmptyException
   */
  public void start ()
    throws
      MidiException,
      EmptyException
  {
    _looping = true;
    _model.getPlayer ().setMetaListener ( new MetaEventListener ()
      {
        @SuppressWarnings("synthetic-access")
        public void meta ( MetaMessage event )
        {
//          Logger.debug ( "ContinuousPlayer.start: " + event );
          if ( event.getType () == END_OF_TRACK_MESSAGE )
          {
            try
            {
              play ();  // play another random pattern
            }
            // TODO: Why Exception?
            catch ( final Exception e )
            {
              Logger.error ( e );
              stop ();
            }
          }
        }
      } );
    play ();
  }


  /**
   * Stop playing.
   */
  public void stop ()
  {
    _looping = false;
    _model.getPlayer ().stop ();
  }


  private void play ()
    throws
      MidiException,
      EmptyException
  {
    if ( _looping )
    {
      final Sound snd = _model.generateSound ();
      fireSoundEvent ( new SoundEvent ( this, snd.getName () ) );
      _model.play ( snd );
    }
  }


  private void fireSoundEvent ( SoundEvent evt )
  {
    final SoundEventListener[] listeners = listenerList.getListeners ( SoundEventListener.class );
    for ( final SoundEventListener element : listeners )
    {
      element.soundEventOccurred ( evt );
    }
  }


  public static final int END_OF_TRACK_MESSAGE = 47;
  
  private final PatternPlayerModel _model;
  private boolean _looping;
  private final EventListenerList listenerList = new EventListenerList ();

}
