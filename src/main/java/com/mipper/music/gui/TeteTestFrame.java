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
package com.mipper.music.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JToggleButton;
import javax.swing.WindowConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import com.mipper.music.generate.EmptyException;
import com.mipper.music.generate.PatternPlayerModel;
import com.mipper.music.model.IntervalPattern;
import com.mipper.music.model.Sound;
import com.mipper.util.Logger;
import com.mipper.util.Util;

/**
 * Window used when running a test cycle.
 *
 * @author  Cliff Evans
 * @version $$Revision: 1.4 $$
 */
public class TeteTestFrame extends javax.swing.JDialog
{

  /**
   * Class implementing the Model and Controller.
   *
   * @author Cliff Evans
   * @version $Revision: 1.4 $
   */
  class Controller
  {

    /**
     * Constructor.
     *
     * @param config Contains model information to allow generation of the test
     *               interval patterns.
     */
    public Controller ( PatternPlayerModel config )
    {
      super ();
      _config = config;
    }


    /**
     * @return Array containing all interval patterns that can be generated for
     *         the test.
     */
    public IntervalPattern[] getAllPatterns ()
    {
      return _config.getPatterns ();
    }


    /**
     * @return The total number of correct guesses.
     */
    public int getCorrectCount ()
    {
      return _correctCount;
    }


    /**
     * @return The total number of guesses made.
     */
    public int getCount ()
    {
      return _count;
    }


    /**
     * @return Integer value of the percentage of correct guesses to incorrect
     *         guesses.
     */
    public int getPercentageCorrect ()
    {
      return 0 == _count ? 0 : 100 * _correctCount / _count;
    }


    /**
     * Play the current Interval pattern.
     *
     * @throws InvalidMidiDataException
     */
    public void playCurrent ()
      throws
        InvalidMidiDataException
    {
      _config.play ( _current );
    }


    /**
     * Generate a random pattern and set it as the current before playing it.
     *
     * @throws InvalidMidiDataException
     * @throws EmptyException
     */
    public void playNext ()
      throws
        InvalidMidiDataException,
        EmptyException
    {
      _current = _config.generateSound ();
      playCurrent ();
    }


    /**
     * @param guess String identifying the pattern the user thinks is the
     *              current pattern.
     *
     * @return true if the guess matches the current pattern, false if not.
     */
    public boolean processGuess ( String guess )
    {
      boolean result;
      _count++;
      if ( guess.equals ( _current.getName () ) )
      {
        _correctCount++;
        result = true;
      }
      else
      {
        result = false;
      }
      Logger.debug ( "Correct: " + _correctCount + " from " + _count );
      return result;
    }


    /**
     * Start a test.  Resets the progress tracking variables.
     *
     * @throws EmptyException
     * @throws InvalidMidiDataException
     */
    public void startTest ()
      throws
        EmptyException,
        InvalidMidiDataException
    {
      reset ();
      playNext ();
    }


    private void reset ()
    {
      _count = 0;
      _correctCount = 0;
    }


    private final PatternPlayerModel _config;
    private Sound _current;
    private int _count = 0;
    private int _correctCount = 0;

  }


  /**
   * Constructor.
   *
   * @param parent
   * @param title
   * @param config
   */
  public TeteTestFrame ( JFrame parent, String title, PatternPlayerModel config )
  {
    super ( parent, title, true );
    initComponents ();
    _controller = new Controller ( config );
    createComponents ( pnlOptions );
    updateGui ();
    pack ();
    Util.centreInParent ( parent, this );
  }
  private void btnExitActionPerformed (ActionEvent evt)
  {
    dispose ();
  }


  private void btnRepeatActionPerformed (ActionEvent evt)
  {
    try
    {
      _controller.playCurrent ();
    }
    catch ( final Exception e )
    {
      handleException ( e );
    }
  }


  private void btnStartActionPerformed (java.awt.event.ActionEvent evt)
  {
    if ( isTestRunning () )
    {
      try
      {
        _controller.startTest ();
        btnStart.setText ( ResourceBundle.getBundle ( "tete" ).getString ( "label.stop" ) );
      }
      catch ( final Exception e )
      {
        handleException ( e );
      }
    }
    else
    {
      btnStart.setText ( ResourceBundle.getBundle ( "tete" ).getString ( "label.start" ) );
      icoResult.setIcon ( null );
    }
    updateGui ();
  }


  private void createComponents ( Container pnlOptions )
  {
    final IntervalPattern[] patterns = _controller.getAllPatterns ();
    final int cols = patterns.length / MAX_COL + ( patterns.length % MAX_COL == 0 ? 0 : 1 );
    final int rows = patterns.length / cols + ( patterns.length % cols == 0 ? 0 : 1 );
    pnlOptions.setLayout( new GridLayout ( rows, cols, 5, 5 ) );
    for ( final IntervalPattern ip: patterns )
    {
      final JButton btn = new JButton ( ip.getName () );
      btn.setMaximumSize ( new Dimension ( 32000, 32000 ) );
      btn.addActionListener ( new ActionListener ()
        {
          public void actionPerformed ( ActionEvent evt )
          {
            try
            {
              if ( _controller.processGuess ( evt.getActionCommand () ) )
              {
                icoResult.setIcon ( _right );
                _controller.playNext ();
              }
              else
              {
                icoResult.setIcon ( _wrong );
              }
              pgsProgress.setValue ( _controller.getPercentageCorrect () );
            }
            catch ( final Exception e )
            {
              handleException ( e );
            }
          }
        } );
      pnlOptions.add ( btn );
    }
  }


  private void handleException ( Exception e )
  {
    Logger.error ( e );
  }


  /** This method is called from within the constructor to
   * initialize the form.
   */
  private void initComponents ()
  {
    pnlControl = new JPanel ();
    btnStart = new JToggleButton ();
    btnRepeat = new JButton ();
    btnExit = new JButton ();
    pnlOptions = new JPanel ();
    pnlProgress = new JPanel ();
    pgsProgress = new JProgressBar ();
    icoResult = new JLabel ();
    setDefaultCloseOperation ( WindowConstants.DISPOSE_ON_CLOSE );
    btnStart.setText ( ResourceBundle.getBundle ( "tete" )
                                     .getString ( "label.start" ) );
    btnStart.addActionListener ( new ActionListener ()
    {

      public void actionPerformed ( ActionEvent evt )
      {
        btnStartActionPerformed ( evt );
      }
    } );
    pnlControl.add ( btnStart );
    btnRepeat.setText ( ResourceBundle.getBundle ( "tete" )
                                      .getString ( "label.repeat" ) );
    btnRepeat.addActionListener ( new ActionListener ()
    {

      public void actionPerformed ( ActionEvent evt )
      {
        btnRepeatActionPerformed ( evt );
      }
    } );
    pnlControl.add ( btnRepeat );
    btnExit.setText ( ResourceBundle.getBundle ( "tete" )
                                    .getString ( "label.close" ) );
    btnExit.addActionListener ( new ActionListener ()
    {

      public void actionPerformed ( ActionEvent evt )
      {
        btnExitActionPerformed ( evt );
      }
    } );
    pnlControl.add ( btnExit );
    getContentPane ().add ( pnlControl, BorderLayout.SOUTH );
    pnlOptions.setBorder ( new CompoundBorder ( new EtchedBorder (), new EmptyBorder ( new Insets ( 1, 5, 5, 5 ) ) ) );
    getContentPane ().add ( pnlOptions, BorderLayout.CENTER );
    pnlProgress.setLayout ( new BorderLayout () );
    pgsProgress.setOrientation ( 1 );
    pgsProgress.setStringPainted ( true );
    pnlProgress.add ( pgsProgress, BorderLayout.CENTER );
    icoResult.setMinimumSize ( new Dimension ( 32, 32 ) );
    icoResult.setPreferredSize ( new Dimension ( 32, 32 ) );
    pnlProgress.add ( icoResult, BorderLayout.NORTH );
    getContentPane ().add ( pnlProgress, BorderLayout.EAST );
    pack ();
  }

  private boolean isTestRunning ()
  {
    return btnStart.isSelected ();
  }

  private void updateGui ()
  {
    if ( isTestRunning () )
    {
      updateOptions ( true );
      btnRepeat.setEnabled ( true );
    }
    else
    {
      updateOptions ( false );
      btnRepeat.setEnabled ( false );
    }
    pgsProgress.setValue ( _controller.getPercentageCorrect () );
  }

  private void updateOptions ( boolean value )
  {
    for ( int i = 0; i < pnlOptions.getComponentCount (); i++ )
    {
      pnlOptions.getComponent ( i ).setEnabled ( value );
    }
  }


  private static final int MAX_COL = 15;
  private static final long serialVersionUID = 3761693372708237873L;
  private javax.swing.JButton btnExit;
  private javax.swing.JButton btnRepeat;
  private javax.swing.JToggleButton btnStart;
  private javax.swing.JLabel icoResult;
  private javax.swing.JProgressBar pgsProgress;
  private javax.swing.JPanel pnlControl;

  private javax.swing.JPanel pnlOptions;

  private javax.swing.JPanel pnlProgress;
  private final Controller _controller;
  private final ImageIcon _right = new ImageIcon ( getClass ().getResource ( "/img/right.gif" ) );


  private final ImageIcon _wrong = new ImageIcon ( getClass ().getResource ( "/img/wrong.gif" ) );

}
