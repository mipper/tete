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
import com.mipper.music.midi.MidiException;
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
@SuppressWarnings("synthetic-access")
public class TeteTestFrame extends javax.swing.JDialog
{

  /**
   * Class implementing the Model and Controller.
   *
   * @author Cliff Evans
   * @version $Revision: 1.4 $
   */
  static class Controller
  {

    /**
     * Constructor.
     *
     * @param config Contains model information to allow generation of the test
     *               interval patterns.
     */
    public Controller ( final PatternPlayerModel config )
    {
      super ();
      _model = config;
    }


    /**
     * @return Array containing all interval patterns that can be generated for
     *         the test.
     */
    public IntervalPattern[] getAllPatterns ()
    {
      return _model.getPatterns ();
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
     * @throws MidiException
     */
    public void playCurrent ()
      throws
        MidiException
    {
      _model.play ( _current );
    }


    /**
     * Generate a random pattern and set it as the current before playing it.
     *
     * @throws MidiException
     * @throws EmptyException
     */
    public void playNext ()
      throws
        MidiException,
        EmptyException
    {

      _current = _model.generateSound ();
      playCurrent ();
    }


    /**
     * @param guess String identifying the pattern the user thinks is the
     *              current pattern.
     *
     * @return true if the guess matches the current pattern, false if not.
     */
    public boolean processGuess ( final String guess )
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
     * @throws MidiException
     */
    public void startTest ()
      throws
        EmptyException,
        MidiException
    {
      reset ();
      playNext ();
    }


    private void reset ()
    {
      _count = 0;
      _correctCount = 0;
    }


    private final PatternPlayerModel _model;
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
  public TeteTestFrame ( final JFrame parent, final String title, final PatternPlayerModel config )
  {
    super ( parent, title, true );
    initComponents ();
    _controller = new Controller ( config );
    createComponents ( pnlOptions );
    updateGui ();
    pack ();
    Util.centreWindow ( parent, this );
  }


  private void btnExitActionPerformed ( final ActionEvent evt )
  {
    dispose ();
  }


  private void btnRepeatActionPerformed ( final ActionEvent evt )
  {
    try
    {
      _controller.playCurrent ();
    }
    catch ( final MidiException e )
    {
      GuiUtil.handleException ( this, e );
    }
  }


  private void btnStartActionPerformed (final java.awt.event.ActionEvent evt)
  {
    if ( isTestRunning () )
    {
      try
      {
        _controller.startTest ();
        btnStart.setText ( GuiUtil.readProperty ( "label.stop" ) );
      }
      catch ( final MidiException e )
      {
        GuiUtil.handleException ( this, e );
      }
      catch ( final EmptyException e )
      {
        GuiUtil.handleException ( this, e );
      }
    }
    else
    {
      btnStart.setText ( GuiUtil.readProperty ( "label.start" ) );
      icoResult.setIcon ( null );
    }
    updateGui ();
  }


  private void createComponents ( final Container pnlOptions )
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
          public void actionPerformed ( final ActionEvent evt )
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
            catch ( final MidiException e )
            {
              GuiUtil.handleException ( null, e );
            }
            catch ( final EmptyException e )
            {
              GuiUtil.handleException ( null, e );
            }
          }
        } );
      pnlOptions.add ( btn );
    }
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
    btnStart.setText ( GuiUtil.readProperty ( "label.start" ) );
    btnStart.addActionListener ( new ActionListener ()
    {

      public void actionPerformed ( final ActionEvent evt )
      {
        btnStartActionPerformed ( evt );
      }
    } );
    pnlControl.add ( btnStart );
    btnRepeat.setText ( GuiUtil.readProperty ( "label.repeat" ) );
    btnRepeat.addActionListener ( new ActionListener ()
    {

      public void actionPerformed ( final ActionEvent evt )
      {
        btnRepeatActionPerformed ( evt );
      }
    } );
    pnlControl.add ( btnRepeat );
    btnExit.setText ( GuiUtil.readProperty ( "label.close" ) );
    btnExit.addActionListener ( new ActionListener ()
    {

      public void actionPerformed ( final ActionEvent evt )
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


  private void updateOptions ( final boolean value )
  {
    for ( int i = 0; i < pnlOptions.getComponentCount (); i++ )
    {
      pnlOptions.getComponent ( i ).setEnabled ( value );
    }
  }


  private static final int MAX_COL = 15;
  private static final long serialVersionUID = 1L
  ;
  private javax.swing.JButton btnExit;
  private javax.swing.JButton btnRepeat;
  private javax.swing.JToggleButton btnStart;
  private javax.swing.JLabel icoResult;
  private javax.swing.JProgressBar pgsProgress;
  private javax.swing.JPanel pnlControl;
  private javax.swing.JPanel pnlOptions;
  private javax.swing.JPanel pnlProgress;

  private final Controller _controller;
  private final ImageIcon _right = new ImageIcon ( GuiUtil.getResourceUrl ( "/img/right.gif" ) );
  private final ImageIcon _wrong = new ImageIcon ( GuiUtil.getResourceUrl ( "/img/wrong.gif" ) );

}
