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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Hashtable;

import javax.sound.midi.Instrument;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;

import com.mipper.music.control.ContinuousPlayer;
import com.mipper.music.control.SoundEvent;
import com.mipper.music.control.SoundEventListener;
import com.mipper.music.generate.PatternPlayerModel;
import com.mipper.music.generate.SoundTypeRepository;
import com.mipper.music.midi.MidiException;
import com.mipper.music.model.IntervalPattern;
import com.mipper.music.model.Note;
import com.mipper.music.model.Octave;
import com.mipper.music.model.SoundCollectionAdaptor;
import com.mipper.util.PreferenceManager;
import com.mipper.util.Util;


/**
 * Main window for Tete.  Allows selection of sounds and configuration.
 *
 * @author  Cliff Evans
 * @version $$Revision: 1.5 $$
 */
@SuppressWarnings("synthetic-access")
public class TeteFrame extends JFrame
{

  /**
   * @param args the command line arguments
   */
  public static void main ( final String args[] )
  {
    new TeteFrame ().setVisible ( true );
  }


  /**
   * Creates new form TeteFrame
   */
  public TeteFrame ()
  {
    _mgr = PreferenceManager.instanceOf ();
    try
    {
      createUiComponents ();
      _model = new PatternPlayerModel ();
      initUiComponents ();
      loadPreferences ();
    }
    catch ( final MidiException e )
    {
      GuiUtil.handleException ( this, e );
    }
    Util.centreWindow ( this );
    setApplicationIcon ();
  }


  private void btnAboutActionPerformed ( final ActionEvent evt )
  {
    final TeteDialog f = new AboutFrame ( this,
                                          GuiUtil.readProperty ( "title.about" ) );
    Util.centreWindow ( this, f );
    f.setVisible ( true );
  }


  private void btnConfigActionPerformed ( final ActionEvent evt )
  {
    final PreferencesDialog f = new PreferencesDialog ( this,
                                                        GuiUtil.readProperty ( "title.config" ),
                                                        _model,
                                                        _mgr.getSoundbankPath () );
    Util.centreWindow ( this, f );
    f.setVisible ( true );
    if ( StringUtils.isEmpty ( f.getSoundbankPath () ) )
    {
      _mgr.setSoundbankPath ( "" );
    }
    else
    {
      final File sb = new File ( f.getSoundbankPath () );
      if ( sb.getAbsolutePath () != _mgr.getSoundbankPath () )
      {
        loadSoundbank ( sb );
        // TODO
        try
        {
          cboInstrument.setSelectedItem ( _model.lookupInstrument ( _mgr.getPatch () ) );
        }
        catch ( final MidiException e )
        {
          GuiUtil.handleException ( this, e );
        }
      }
    }
  }


  private void loadSoundbank ( final File sb )
  {
    try
    {
      if ( sb.exists () && _model.loadSoundbank ( sb ) )
      {
        _mgr.setSoundbankPath ( sb.getAbsolutePath () );
        setupInstrumentCombo ();
//        cboInstrument.setSelectedItem ( _model.getInstrument () );
      }
    }
    catch ( final MidiException e )
    {
      GuiUtil.handleException ( this, e );
    }
    catch ( final IOException e )
    {
      GuiUtil.handleException ( this, e );
    }
  }


  private void btnLoopActionPerformed ( final ActionEvent evt )
  {
    if ( btnLoop.isSelected () )
    {
      _model.setPatterns ( extractIntervals ( lstSounds.getSelectedValues () ) );
      _looper = new ContinuousPlayer ( _model );
      _looper.addSoundEventListener ( new SoundEventListener ()
                    {
                      public void soundEventOccurred ( final SoundEvent evt )
                      {
                        soundPlaying ( evt );
                      }
                    });
      try
      {
        _looper.start ();
      }
      // TODO: Why Exception?
      catch ( final Exception e )
      {
        GuiUtil.handleException ( this, e );
      }
      btnLoop.setText ( GuiUtil.readProperty ( "label.stop" ) );
      btnTest.setEnabled ( false );
    }
    else
    {
      _looper.stop ();
      _looper = null;
      btnLoop.setText ( GuiUtil.readProperty ( "label.play" ) );
      btnTest.setEnabled ( true );
      lstSounds.clearPlaying ();
    }
  }


  private IntervalPattern[] extractIntervals ( final Object[] objs )
  {
    final IntervalPattern[] intervals = new IntervalPattern[objs.length];
    for ( int i = 0; i < objs.length; i++ )
    {
      intervals[i] = ( IntervalPattern ) objs[i];
    }
    return intervals;
  }


  private void btnTestActionPerformed ( final ActionEvent evt )
  {
    _model.setPatterns ( extractIntervals ( lstSounds.getSelectedValues () ) );
    final TeteTestFrame test = new TeteTestFrame ( this,
                                                   GuiUtil.readProperty ( "title.test" ),
                                                   _model );
    test.setVisible ( true );
  }


  private void cboInstrumentItemStateChanged ( final ItemEvent evt )
  {
    if ( evt.getStateChange () == ItemEvent.SELECTED )
    {
      _model.setInstrument ( ( Instrument ) evt.getItem () );
    }
  }


  private Boolean decodeDirection ()
  {
    final int idx = cboNoteOrder.getSelectedIndex ();
    return idx == 0 ? null : Boolean.valueOf ( idx == 1 );
  }


  private void exitApp ( final int code )
  {
    if ( _looper != null )
    {
      _looper.stop ();
    }
    if ( cboInstrument.getSelectedItem () != null )
    {
      savePreferences ();
    }
    System.exit ( code );
  }


  private int getOctaveRoot ( final Octave octave )
  {
    return Octave.INTERVAL_COUNT + octave.getOctaveNumber () * Octave.INTERVAL_COUNT;
  }


  private int getOctaveTop ( final Octave octave )
  {
    return getOctaveRoot ( octave ) + Octave.INTERVAL_COUNT - 1;
  }


  private SoundCollectionAdaptor getSelectedSoundType ()
  {
    return ( SoundCollectionAdaptor ) cboSoundType.getSelectedItem ();
  }


  private void createUiComponents ()
  {
    pnlPlayback = new JPanel ();
    pnlCombos = new JPanel ();
    lblInstrument = new JLabel ();
    cboInstrument = new JComboBox ();
    lblBottomOctave = new JLabel ();
    cboBottomOctave = new JComboBox ();
    lblTopOctave = new JLabel ();
    cboTopOctave = new JComboBox ();
    lblRootNote = new JLabel ();
    cboRootNote = new JComboBox ();
    lblNoteOrder = new JLabel ();
    cboNoteOrder = new JComboBox ();
    pnlSliders = new JPanel ();
    lblNoteLength = new JLabel ();
    sldNoteLength = new JSlider ();
    lblArpeggioDelay = new JLabel ();
    sldArpeggioDelay = new JSlider ();
    pnlControl = new JPanel ();
    btnLoop = new JToggleButton ();
    btnTest = new JButton ();
    btnExit = new JButton ();
    btnConfig = new JButton ();
    btnAbout = new JButton ();
    pnlSounds = new JPanel ();
    pnlSoundType = new JPanel ();
    lblSoundType = new JLabel ();
    cboSoundType = new JComboBox ();
    scrSounds = new JScrollPane ();
    lstSounds = new JSoundList ();
    chkCascade = new JCheckBox ();

    // TODO: figure out how to stop a window becoming too small
    getContentPane ().setMinimumSize ( new Dimension ( 394, 554 ) );
    addWindowListener ( new WindowAdapter ()
                        {
                          @Override
                          public void windowClosing ( final WindowEvent e )
                          {
                            exitApp ( 0 );
                          }
                        } );
    setDefaultCloseOperation ( WindowConstants.DO_NOTHING_ON_CLOSE );
    setTitle ( GuiUtil.readProperty ( "title.application" ) );

    pnlPlayback.setLayout ( new GridBagLayout () );
    pnlPlayback.setBorder ( new TitledBorder ( GuiUtil.readProperty ( "title.playback" ) ) );

    pnlCombos.setLayout ( new GridLayout ( 0, 1 ) );

    lblInstrument.setLabelFor ( cboInstrument );
    lblInstrument.setText ( GuiUtil.readProperty ( "label.instrument" ) );
    pnlCombos.add ( lblInstrument );

    cboInstrument.setOpaque ( false );
    cboInstrument.addItemListener ( new ItemListener ()
                                {
                                  public void itemStateChanged ( final ItemEvent evt )
                                  {
                                    cboInstrumentItemStateChanged ( evt );
                                  }
                                } );
    pnlCombos.add ( cboInstrument );

    lblBottomOctave.setLabelFor ( cboBottomOctave );
    lblBottomOctave.setText ( GuiUtil.readProperty ( "label.bottom-octave" ) );
    pnlCombos.add ( lblBottomOctave );

    cboBottomOctave.setPreferredSize ( new java.awt.Dimension ( 29, 17 ) );
    pnlCombos.add ( cboBottomOctave );

    lblTopOctave.setLabelFor ( cboTopOctave );
    lblTopOctave.setText ( GuiUtil.readProperty ( "label.top-octave" ) );
    pnlCombos.add ( lblTopOctave );

    cboTopOctave.setPreferredSize ( new java.awt.Dimension ( 29, 17 ) );
    pnlCombos.add ( cboTopOctave );

    lblRootNote.setLabelFor ( cboRootNote );
    lblRootNote.setText ( GuiUtil.readProperty ( "label.root-note" ) );
    pnlCombos.add ( lblRootNote );

    final DefaultComboBoxModel m = new DefaultComboBoxModel ( Note.values () );
    m.insertElementAt ( GuiUtil.readProperty ( "label.random" ), 0 );
    cboRootNote.setModel ( m );
    cboRootNote.setPreferredSize ( new java.awt.Dimension ( 29, 17 ) );
    cboRootNote.addItemListener ( new ItemListener ()
                          {
                            public void itemStateChanged ( final ItemEvent evt )
                            {
                              if ( evt.getStateChange () == ItemEvent.SELECTED )
                              {
                                setRange ();
                              }
                            }
                          } );
    pnlCombos.add ( cboRootNote );

    lblNoteOrder.setLabelFor ( cboNoteOrder );
    lblNoteOrder.setText ( GuiUtil.readProperty ( "label.direction" ) );
    pnlCombos.add ( lblNoteOrder );

    cboNoteOrder.setModel ( new javax.swing.DefaultComboBoxModel ( new String[]
                             {GuiUtil.readProperty ( "label.random" ),
                              GuiUtil.readProperty ( "label.ascending" ),
                              GuiUtil.readProperty ( "label.descending" )} ) );
    cboNoteOrder.setSelectedIndex ( 1 );
    cboNoteOrder.addItemListener ( new ItemListener ()
                          {
                            public void itemStateChanged ( final ItemEvent evt )
                            {
                              if ( evt.getStateChange () == ItemEvent.SELECTED )
                              {
                                _model.setDirection ( decodeDirection () );
                              }
                            }
                          } );
    pnlCombos.add ( cboNoteOrder );

    GridBagConstraints gridBagConstraints;
    gridBagConstraints = new java.awt.GridBagConstraints ();
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    pnlPlayback.add ( pnlCombos, gridBagConstraints );

    pnlSliders.setLayout ( new java.awt.GridLayout ( 5, 1 ) );
    lblNoteLength.setLabelFor ( sldNoteLength );
    lblNoteLength.setText ( GuiUtil.readProperty ( "label.note-length" ) );
    pnlSliders.add ( lblNoteLength );

    sldNoteLength.setMajorTickSpacing ( 8 );
    sldNoteLength.setMaximum ( 64 );
    sldNoteLength.setMinorTickSpacing ( 2 );
    sldNoteLength.setPaintLabels ( true );
    sldNoteLength.setPaintTicks ( true );
    sldNoteLength.setSnapToTicks ( true );
    sldNoteLength.setValue ( 32 );
    sldNoteLength.addChangeListener ( new ChangeListener ()
                                  {
                                    public void stateChanged ( final ChangeEvent evt )
                                    {
                                      sldNoteLengthStateChanged ( evt );
                                    }
                                  } );
    pnlSliders.add ( sldNoteLength );

    lblArpeggioDelay.setLabelFor ( sldArpeggioDelay );
    lblArpeggioDelay.setText ( GuiUtil.readProperty ( "label.agpeggio-dely" ) );
    pnlSliders.add ( lblArpeggioDelay );

    sldArpeggioDelay.setMajorTickSpacing ( 8 );
    sldArpeggioDelay.setMaximum ( 64 );
    sldArpeggioDelay.setMinorTickSpacing ( 2 );
    sldArpeggioDelay.setPaintLabels ( true );
    sldArpeggioDelay.setPaintTicks ( true );
    sldNoteLength.setSnapToTicks ( true );
    sldArpeggioDelay.setValue ( 8 );
    sldArpeggioDelay.addChangeListener ( new ChangeListener ()
                                  {
                                    public void stateChanged ( final ChangeEvent evt )
                                    {
                                      sldArpeggioDelayStateChanged ( evt );
                                    }
                                  } );
    pnlSliders.add ( sldArpeggioDelay );

    chkCascade.setHorizontalTextPosition ( SwingConstants.LEADING  );
    chkCascade.setText ( GuiUtil.readProperty ( "label.cascade" ) );
    chkCascade.addItemListener ( new ItemListener ()
                              {
                                public void itemStateChanged ( final ItemEvent evt )
                                {
                                  _model.setCascade( chkCascade.isSelected () );
                                }
                              } );
    pnlSliders.add ( chkCascade );

    gridBagConstraints = new java.awt.GridBagConstraints ();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = GridBagConstraints.NORTH;
    pnlPlayback.add ( pnlSliders, gridBagConstraints );

    btnLoop.setText ( GuiUtil.readProperty ( "label.play" ) );
    btnLoop.addActionListener ( new ActionListener ()
                              {
                                public void actionPerformed ( final ActionEvent evt )
                                {
                                  btnLoopActionPerformed ( evt );
                                }
                              } );
    pnlControl.add ( btnLoop );

    btnTest.setText ( GuiUtil.readProperty ( "label.test" ) );
    btnTest.addActionListener ( new ActionListener ()
                              {
                                public void actionPerformed ( final ActionEvent evt )
                                {
                                  btnTestActionPerformed ( evt );
                                }
                              } );
    pnlControl.add ( btnTest );

    btnConfig.setText ( GuiUtil.readProperty ( "label.config" ) );
    btnConfig.addActionListener ( new ActionListener ()
                              {
                                public void actionPerformed ( final ActionEvent evt )
                                {
                                  btnConfigActionPerformed ( evt );
                                }
                              } );
    pnlControl.add ( btnConfig );

    btnExit.setText ( GuiUtil.readProperty ( "label.exit" ) );
    btnExit.addActionListener ( new ActionListener ()
                              {

                                public void actionPerformed ( final ActionEvent evt )
                                {
                                  exitApp ( 0 );
                                }
                              } );
    pnlControl.add ( btnExit );

    btnAbout.setText ( GuiUtil.readProperty ( "label.about" ) );
    btnAbout.addActionListener ( new ActionListener ()
                              {
                                public void actionPerformed ( final ActionEvent evt )
                                {
                                  btnAboutActionPerformed ( evt );
                                }
                              } );
    pnlControl.add ( btnAbout );

    getContentPane ().add ( pnlControl, BorderLayout.SOUTH );

    pnlSounds.setLayout ( new BorderLayout ( 0, 5 ) );
    pnlSounds.setBorder ( new TitledBorder ( GuiUtil.readProperty ( "label.sounds" ) ) );
    pnlSoundType.setLayout ( new GridLayout ( 2, 1 ) );
    lblSoundType.setLabelFor ( cboSoundType );
    lblSoundType.setText ( GuiUtil.readProperty ( "label.type" ) );
    pnlSoundType.add ( lblSoundType );

    cboSoundType.addItemListener ( new ItemListener ()
                  {

                    public void itemStateChanged ( final ItemEvent e )
                    {
                      if ( e.getStateChange () == ItemEvent.SELECTED )
                      {
                        setupIntervalPatterns ();
                      }
                      else
                      {
                        saveSounds ( ( SoundCollectionAdaptor ) e.getItem () );
                      }
                    }
                  } );
    pnlSoundType.add ( cboSoundType );

    pnlSounds.add ( pnlSoundType, BorderLayout.NORTH );

    lstSounds.addListSelectionListener ( new ListSelectionListener ()
                           {
                             public void valueChanged ( final ListSelectionEvent evt )
                             {
                               lstSoundsValueChanged ( evt );
                             }
                           } );
    scrSounds.setViewportView ( lstSounds );
    lstSounds.setCellRenderer ( new SoundListRenderer () );
    pnlSounds.add ( scrSounds, BorderLayout.CENTER );

    final JSplitPane splitPane = new JSplitPane ( JSplitPane.HORIZONTAL_SPLIT,
                                            pnlPlayback,
                                            pnlSounds );
    getContentPane ().add ( splitPane, BorderLayout.CENTER );

    pack ();
  }


  private void savePreferences ()
  {
    _mgr.setSynthName ( _model.getSynth ().getDeviceInfo ().getName () );
    _mgr.setInstrument ( ( Instrument ) cboInstrument.getSelectedItem () );
    _mgr.setRootNote ( cboRootNote.getSelectedIndex () );
    _mgr.setBottomOctave ( cboBottomOctave.getSelectedIndex () );
    _mgr.setTopOctave ( cboTopOctave.getSelectedIndex () );
    _mgr.setRootNote ( cboRootNote.getSelectedIndex () );
    _mgr.setNoteOrder ( cboNoteOrder.getSelectedIndex () );
    _mgr.setNoteLength ( sldNoteLength.getValue () );
    _mgr.setArpeggioDelay ( sldArpeggioDelay.getValue () );
    _mgr.setCascade ( chkCascade.isSelected () );
    _mgr.setPatternType ( cboSoundType.getSelectedIndex () );
    saveSounds ( getSelectedSoundType () );
    try
    {
      for ( final String key : _selectedSounds.keySet () )
      {
        _mgr.setSelectedPatterns ( key, _selectedSounds.get ( key ) );
      }
      _mgr.setSizePos ( this.getBounds () );
    }
    catch ( final IOException e )
    {
      GuiUtil.handleException ( this, e );
    }
  }


  private void initUiComponents ()
    throws
      MidiException
  {
    setupInstrumentCombo ();
    setupBottomOctaveCombo ();
    setupTopOctaveCombo ();
    setupSoundTypesCombo ();
    setupIntervalPatterns ();
    setRange ();
  }


  private void loadPreferences ()
    throws
      MidiException
  {
    _model.setSynth ( _mgr.getSynthName () );
    if ( !StringUtils.isEmpty ( _mgr.getSoundbankPath () ) )
    {
      loadSoundbank ( new File ( _mgr.getSoundbankPath () ) );
    }
    cboInstrument.setSelectedItem ( _model.lookupInstrument ( _mgr.getPatch () ) );
    cboBottomOctave.setSelectedIndex ( _mgr.getBottomOctave () );
    cboTopOctave.setSelectedIndex ( _mgr.getTopOctave () );
    cboRootNote.setSelectedIndex ( _mgr.getRootNote () );
    cboNoteOrder.setSelectedIndex ( _mgr.getNoteOrder () );
    sldArpeggioDelay.setValue ( _mgr.getArpeggioDelay () );
    chkCascade.setSelected ( _mgr.getCascade () );
    sldNoteLength.setValue ( _mgr.getNoteLength () );
    SoundCollectionAdaptor key;
    for ( int i = 0; i < cboSoundType.getItemCount (); i++ )
    {
      key = ( SoundCollectionAdaptor ) cboSoundType.getItemAt ( i );
      _selectedSounds.put ( key.getName (), _mgr.getSelectedPatterns ( key.getName () ) );
    }
    key = getSelectedSoundType ();
    lstSounds.setSelectedIndices ( _selectedSounds.get ( key.getName () ) );
    cboSoundType.setSelectedIndex ( _mgr.getPatternType () );
    final Rectangle r = _mgr.getSizePos ();
    if ( null != r )
    {
      setBounds ( r );
    }
  }


  private void setupInstrumentCombo ()
    throws
      MidiException
  {
    cboInstrument.setModel ( new DefaultComboBoxModel ( _model.getLoadedInstruments ()
                                                              .toArray () ) );
    cboInstrument.setRenderer ( new DefaultListCellRenderer ()
      {
        @Override
        public Component getListCellRendererComponent ( final JList list,
                                                        final Object value,
                                                        final int index,
                                                        final boolean isSelected,
                                                        final boolean cellHasFocus )
        {
          final JLabel lbl = ( JLabel ) super.getListCellRendererComponent ( list,
                                                                             value,
                                                                             index,
                                                                             isSelected,
                                                                             cellHasFocus );
          if ( null != value )
          {
            lbl.setText ( ( ( Instrument ) value ).getName () );
          }
          return lbl;
        }

        private static final long serialVersionUID = 1L;
      }
    );
  }


  private void setupBottomOctaveCombo ()
  {
    cboBottomOctave.setModel ( new DefaultComboBoxModel ( Octave.values () ) );
    cboBottomOctave.addItemListener ( new ItemListener ()
      {
        public void itemStateChanged ( final ItemEvent evt )
        {
          if ( evt.getStateChange () == ItemEvent.SELECTED )
          {
            final Octave octave = ( Octave ) evt.getItem ();
            if ( octave.getOctaveNumber () > ( ( Octave ) cboTopOctave.getSelectedItem () ).getOctaveNumber () )
            {
              cboTopOctave.setSelectedIndex ( cboBottomOctave.getSelectedIndex () );
            }
            setRange ();
          }
        }
      });
  }


  private void setupTopOctaveCombo ()
  {
    cboTopOctave.setModel ( new DefaultComboBoxModel ( Octave.values () ) );
    cboTopOctave.addItemListener ( new ItemListener ()
      {
        public void itemStateChanged ( final ItemEvent evt )
        {
          if ( evt.getStateChange () == ItemEvent.SELECTED )
          {
            final Octave octave = ( Octave ) evt.getItem ();
            if ( octave.getOctaveNumber () < ( ( Octave ) cboBottomOctave.getSelectedItem () ).getOctaveNumber () )
            {
              cboBottomOctave.setSelectedIndex ( cboTopOctave.getSelectedIndex () );
            }
            setRange ();
          }
        }
      } );
  }


  private void setupIntervalPatterns ()
  {
    final DefaultListModel lm = new DefaultListModel ();
    for ( final IntervalPattern ct: ( ( SoundCollectionAdaptor ) cboSoundType.getSelectedItem () ).getPatterns () )
    {
      lm.addElement ( ct );
    }
    lstSounds.setModel ( lm );
    lstSounds.setSelectionInterval ( 0, lm.getSize () - 1 );
    final int[] selections = _selectedSounds.get ( getSelectedSoundType ().getName () );
    if ( selections != null )
    {
      lstSounds.setSelectedIndices ( selections );
    }
  }


  private void setupSoundTypesCombo ()
  {
    final DefaultComboBoxModel model = new DefaultComboBoxModel ();
    final SoundTypeRepository rep = new SoundTypeRepository ();
    for ( final SoundCollectionAdaptor ipr: rep.getAllPatternGroups () )
    {
      model.addElement ( ipr );
    }
    cboSoundType.setModel ( model );
  }


  private void lstSoundsValueChanged ( final ListSelectionEvent evt )
  {
    btnLoop.setEnabled ( lstSounds.getSelectedValues ().length != 0 );
    btnTest.setEnabled ( btnLoop.isEnabled () );
  }


  private void saveSounds ( final SoundCollectionAdaptor key )
  {
    _selectedSounds.put ( key.getName (), lstSounds.getSelectedIndices () );
  }


  /**
   * Sets the application's icon.
   */
  private void setApplicationIcon ()
  {
    final URL i = GuiUtil.getResourceUrl ( "/img/headphones.gif" );
    if ( i != null )
    {
      this.setIconImage ( new ImageIcon ( i ).getImage () );
    }
  }


  private void setRange ()
  {
    final Object root = cboRootNote.getSelectedItem ();
    _model.setNoteRange ( getOctaveRoot ( ( Octave ) cboBottomOctave.getSelectedItem () ),
                          getOctaveTop ( ( Octave ) cboTopOctave.getSelectedItem () ),
                          root instanceof Note ? ( Note ) root : null );
  }


  private void sldArpeggioDelayStateChanged ( final ChangeEvent evt )
  {
    _model.setArpeggioDelay ( sldArpeggioDelay.getValue () );
  }


  private void sldNoteLengthStateChanged ( final ChangeEvent evt )
  {
    _model.setNoteLength ( sldNoteLength.getValue () );
  }


  /**
   * @param evt
   */
  private void soundPlaying ( final SoundEvent evt )
  {
    lstSounds.setPlaying ( evt.getName () );
  }


  private static final long serialVersionUID = 1L;

  private JButton btnExit;
  private JButton btnAbout;
  private JToggleButton btnLoop;
  private JButton btnConfig;
  private JButton btnTest;
  private JComboBox cboBottomOctave;
  private JComboBox cboInstrument;
  private JComboBox cboNoteOrder;
  private JComboBox cboRootNote;
  private JComboBox cboSoundType;
  private JComboBox cboTopOctave;
  private JPanel pnlSliders;
  private JPanel pnlCombos;
  private JLabel lblArpeggioDelay;
  private JLabel lblBottomOctave;
  private JLabel lblInstrument;
  private JLabel lblNoteLength;
  private JLabel lblNoteOrder;
  private JLabel lblRootNote;
  private JLabel lblSoundType;
  private JLabel lblTopOctave;
  private JPanel pnlControl;
  private JPanel pnlPlayback;
  private JPanel pnlSoundType;
  private JPanel pnlSounds;
  private JScrollPane scrSounds;
  private JSlider sldArpeggioDelay;
  private JSlider sldNoteLength;
  private JSoundList lstSounds;
  private JCheckBox chkCascade;

  private PatternPlayerModel _model;
  private ContinuousPlayer _looper;
  private final Hashtable<String, int[]> _selectedSounds = new Hashtable<String, int[]> ();
  private final PreferenceManager _mgr;

}
