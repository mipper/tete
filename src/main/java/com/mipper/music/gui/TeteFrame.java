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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Hashtable;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
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
import com.mipper.util.Logger;
import com.mipper.util.PreferenceManager;
import com.mipper.util.Util;
import com.sun.media.sound.SF2Soundbank;


/**
 * Main window for Tete.  Allows selection of sounds and configuration.
 *
 * @author  Cliff Evans
 * @version $$Revision: 1.5 $$
 */
public final class TeteFrame extends javax.swing.JFrame
{

  /**
   * @param args the command line arguments
   *
   * @throws Exception
   */
  public static void main ( String args[] )
    throws
      Exception
  {
    new TeteFrame ().setVisible ( true );
  }


  /**
   * Creates new form TeteFrame
   */
  public TeteFrame ()
  {
    initComponents ();
    try
    {
      initMidi ();
      loadPreferences ();
      loadSounds ();
    }
    catch ( final MidiException e )
    {
      handleException ( e );
//      exitApp ();
    }
    catch ( final MidiUnavailableException e )
    {
      handleException ( e );
//      exitApp ();
    }
    Util.centreWindow ( this );
    setApplicationIcon ();
  }


  private void btnAboutActionPerformed ( ActionEvent evt )
  {
    final AboutFrame f = new AboutFrame ( this, 
                                          GuiUtil.readProperty ( "title.about" ) );
    Util.centreWindow ( this, f );
    f.setVisible ( true );
  }


//  private void btnConfigActionPerformed ( ActionEvent evt )
//  {
//    final PreferencesDialog f = new PreferencesDialog ( this, 
//                                                        ResourceBundle.getBundle ( "tete" )
//                                                                      .getString ( "title.config" ) );
//    Util.centreWindow ( this, f );
//    f.setVisible ( true );
//  }


  private void btnLoopActionPerformed (ActionEvent evt)
  {
    if ( btnLoop.isSelected () )
    {
      _model.setPatterns ( lstSounds.getSelectedValues () );
      _looper = new ContinuousPlayer ( _model );
      _looper.addSoundEventListener ( new SoundEventListener ()
        {
          public void soundEventOccurred ( SoundEvent evt )
          {
            soundPlaying ( evt );
          }
        });
      try
      {
        _looper.start ();
      }
      catch ( final Exception e )
      {
        handleException ( e );
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


  private void btnTestActionPerformed (ActionEvent evt)
  {
    try
    {
      _model.setPatterns ( lstSounds.getSelectedValues () );
      final TeteTestFrame test = new TeteTestFrame ( this,
                                                     GuiUtil.readProperty ( "title.test" ),
                                                     _model );
      test.setVisible ( true );
    }
    catch ( final Exception e )
    {
      throw new RuntimeException ( e );
    }
  }


  private void cboSynthItemStateChanged (ItemEvent evt)
  {
    if ( evt.getStateChange () == ItemEvent.SELECTED )
    {
      Synthesizer synth = ( Synthesizer ) evt.getItem ();
      try
      {
//        Soundbank sb = new SF2Soundbank ( Util.getResource ( this, "Strat_Marshall.SF2" ) );
        Soundbank sb = new SF2Soundbank ( Util.getResource ( this, "Guitar Vince.sf2" ) );
        Logger.debug ( "cboSynthItemStateChanged Select synth: " + synth );
        for ( Instrument i: sb.getInstruments () )
        {
          Logger.debug ( "cboSynthItemStateChanged Instrument: {0}", i );
        }
        if ( synth.isSoundbankSupported ( sb ) )
        {
          synth.open ();
//          if ( synth.getDefaultSoundbank () != null )
//          {
//            synth.unloadAllInstruments ( synth.getDefaultSoundbank () );
//          }
          Logger.debug ( "Unloaded Insts: loaded {0}, avail {1}", synth.getLoadedInstruments ().length, synth.getAvailableInstruments ().length );
          Logger.debug ( "Load All insts: " + synth.loadAllInstruments ( sb ) );
          Logger.debug ( "Loaded Insts: {0}, available: {1}", synth.getLoadedInstruments ().length, synth.getAvailableInstruments ().length );
          initInstruments ();
//        cboInstrument.setSelectedItem ( _model.getInstrument () );
        }
        _model.setSynth ( synth );
      }
      catch ( MidiUnavailableException e )
      {
        Logger.error ( e );
        cboInstrument.setSelectedIndex ( 0 );
      }
      catch ( MidiException e )
      {
        Logger.error ( e );
        cboInstrument.setSelectedIndex ( 0 );
      }
      catch ( FileNotFoundException e )
      {
        Logger.error ( e );
        cboInstrument.setSelectedIndex ( 0 );
      }
      catch ( IOException e )
      {
        Logger.error ( e );
        cboInstrument.setSelectedIndex ( 0 );
      }
    }
  }


  private void cboInstrumentItemStateChanged (ItemEvent evt)
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


  private void exitApp ()
  {
    if ( _looper != null )
    {
      _looper.stop ();
    }
    if ( cboInstrument.getSelectedItem () != null )
    {
      savePreferences ();
    }
    System.exit ( 0 );
  }


  private int getOctaveRoot ( Octave octave )
  {
    return Octave.INTERVAL_COUNT + octave.getOctaveNumber () * Octave.INTERVAL_COUNT;
  }


  private int getOctaveTop ( Octave octave )
  {
    return getOctaveRoot ( octave ) + Octave.INTERVAL_COUNT - 1;
  }


  /**
   * @return The SoundCollectionAdaptor which is currently selected as the sound
   *         type.
   */
  private SoundCollectionAdaptor getSelectedSoundType ()
  {
    return ( SoundCollectionAdaptor ) cboSoundType.getSelectedItem ();
  }


  private void handleException ( Exception e )
  {
    Logger.error ( e );
    JOptionPane.showMessageDialog ( this,
                                    e.getMessage (),
                                    GuiUtil.readProperty ( "title.error" ),
                                    JOptionPane.ERROR_MESSAGE );
  }


  private void initBottomOctave ()
  {
    cboBottomOctave.setModel ( new DefaultComboBoxModel ( Octave.values () ) );
    cboBottomOctave.addItemListener ( new ItemListener ()
      {
        public void itemStateChanged ( ItemEvent evt )
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


  // TODO: Read default from model
  private void initComponents ()
  {
    pnlPlayback = new JPanel ();
    pnlCombos = new JPanel ();
    lblSynth = new JLabel ();
    cboSynth = new JComboBox ();
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
    btnAbout = new JButton ();
//    btnConfig = new JButton ();
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
                          public void windowClosing ( WindowEvent e )
                          {
                            exitApp ();
                          }
                        } );
    setDefaultCloseOperation ( WindowConstants.DO_NOTHING_ON_CLOSE );
    setTitle ( GuiUtil.readProperty ( "title.application" ) );

    pnlPlayback.setLayout ( new GridBagLayout () );
    pnlPlayback.setBorder ( new TitledBorder ( GuiUtil.readProperty ( "title.playback" ) ) );

    pnlCombos.setLayout ( new GridLayout ( 0, 1 ) );
    
    lblSynth.setLabelFor ( cboSynth );
    lblSynth.setText ( GuiUtil.readProperty ( "label.synth" ) );
    pnlCombos.add ( lblSynth );

    cboSynth.setOpaque ( false );
    cboSynth.addItemListener ( new ItemListener ()
                                {
                                  public void itemStateChanged ( ItemEvent evt )
                                  {
                                    cboSynthItemStateChanged ( evt );
                                  }
                                } );
    pnlCombos.add ( cboSynth );

    lblInstrument.setLabelFor ( cboInstrument );
    lblInstrument.setText ( GuiUtil.readProperty ( "label.instrument" ) );
    pnlCombos.add ( lblInstrument );

    cboInstrument.setOpaque ( false );
    cboInstrument.addItemListener ( new ItemListener ()
                                {
                                  public void itemStateChanged ( ItemEvent evt )
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
      public void itemStateChanged ( ItemEvent evt )
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
      public void itemStateChanged ( ItemEvent evt )
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
//    gridBagConstraints.gridwidth = GridBagConstraints.RELATIVE;
//    gridBagConstraints.gridheight = GridBagConstraints.RELATIVE;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
//    gridBagConstraints.anchor = GridBagConstraints.NORTH;
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
      public void stateChanged ( ChangeEvent evt )
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
        public void stateChanged ( ChangeEvent evt )
        {
          sldArpeggioDelayStateChanged ( evt );
        }
      } );
    pnlSliders.add ( sldArpeggioDelay );

    chkCascade.setHorizontalTextPosition ( SwingConstants.LEADING  );
    chkCascade.setText ( GuiUtil.readProperty ( "label.cascade" ) );
    chkCascade.addItemListener ( new ItemListener ()
      {
        public void itemStateChanged ( ItemEvent evt )
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

//    getContentPane ().add ( pnlPlayback, BorderLayout.WEST );

    btnLoop.setText ( GuiUtil.readProperty ( "label.play" ) );
    btnLoop.addActionListener ( new ActionListener ()
    {
      public void actionPerformed ( ActionEvent evt )
      {
        btnLoopActionPerformed ( evt );
      }
    } );
    pnlControl.add ( btnLoop );

    btnTest.setText ( GuiUtil.readProperty ( "label.test" ) );
    btnTest.addActionListener ( new ActionListener ()
    {
      public void actionPerformed ( ActionEvent evt )
      {
        btnTestActionPerformed ( evt );
      }
    } );
    pnlControl.add ( btnTest );

//    btnConfig.setText ( bundle.getString ( "label.config" ) );
//    btnConfig.addActionListener ( new ActionListener ()
//    {
//      public void actionPerformed ( ActionEvent evt )
//      {
//        btnConfigActionPerformed ( evt );
//      }
//    } );
//    pnlControl.add ( btnConfig );

    btnExit.setText ( GuiUtil.readProperty ( "label.exit" ) );
    btnExit.addActionListener ( new ActionListener ()
    {

      public void actionPerformed ( ActionEvent evt )
      {
        exitApp ();
      }
    } );
    pnlControl.add ( btnExit );

    btnAbout.setText ( GuiUtil.readProperty ( "label.about" ) );
    btnAbout.addActionListener ( new ActionListener ()
    {
      public void actionPerformed ( ActionEvent evt )
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

      public void itemStateChanged ( ItemEvent e )
      {
        if ( e.getStateChange () == ItemEvent.SELECTED )
        {
          loadSounds ();
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
       public void valueChanged ( ListSelectionEvent evt )
       {
         lstSoundsValueChanged ( evt );
       }
     } );
    scrSounds.setViewportView ( lstSounds );
    lstSounds.setCellRenderer ( new SoundListRenderer () );
    pnlSounds.add ( scrSounds, BorderLayout.CENTER );

    JSplitPane splitPane = new JSplitPane ( JSplitPane.HORIZONTAL_SPLIT,
                                            pnlPlayback,
                                            pnlSounds );
    getContentPane ().add ( splitPane, BorderLayout.CENTER );
//    getContentPane ().add ( pnlSounds, BorderLayout.CENTER );

    pack ();
  }


  private void initSynths ()
  {
    cboSynth.setModel ( new DefaultComboBoxModel ( _model.getAvailableSynths ()
                                                         .toArray () ) );
    cboSynth.setRenderer ( new DefaultListCellRenderer ()
      {
        @Override
        public Component getListCellRendererComponent ( JList list,
                                                        Object value,
                                                        int index,
                                                        boolean isSelected,
                                                        boolean cellHasFocus )
        {
          final JLabel lbl = ( JLabel ) super.getListCellRendererComponent ( list,
                                                                             value,
                                                                             index,
                                                                             isSelected,
                                                                             cellHasFocus );
          lbl.setText ( ( ( Synthesizer ) value ).getDeviceInfo ().getName () );
          return lbl;
        }
        private static final long serialVersionUID = 1L;
      }
    );
  }


  private void initInstruments ()
    throws
      MidiUnavailableException,
      MidiException
  {
    Logger.debug ( "Synth: " + _model.getSynth () );
    Logger.debug ( "Instruments:", Util.displayCollection ( _model.getLoadedInstruments () ) );
    cboInstrument.setModel ( new DefaultComboBoxModel ( _model.getLoadedInstruments ().toArray () ) );
    cboInstrument.setRenderer ( new DefaultListCellRenderer ()
      {
        @Override
        public Component getListCellRendererComponent ( JList list,
                                                        Object value,
                                                        int index,
                                                        boolean isSelected,
                                                        boolean cellHasFocus )
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
  
  
  private void initMidi ()
    throws
      MidiUnavailableException,
      MidiException
  {
    _model = new PatternPlayerModel ();
    initSynths ();
    initInstruments ();
    initBottomOctave ();
    initTopOctave ();
    setRange ();
    setupSoundTypes ();
    if ( cboInstrument.getSelectedItem () != null )
    {
      _model.setInstrument ( ( Instrument ) cboInstrument.getSelectedItem () );
    }
  }


  private void initTopOctave ()
  {
    cboTopOctave.setModel ( new DefaultComboBoxModel ( Octave.values () ) );
    cboTopOctave.addItemListener ( new ItemListener ()
      {
        public void itemStateChanged ( ItemEvent evt )
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
      });
  }


  private void loadPreferences ()
  {
    final PreferenceManager mgr = PreferenceManager.instanceOf ();
    cboSynth.setSelectedIndex ( lookupSynth ( mgr.getSynthName () ) );
    cboInstrument.setSelectedItem ( _model.lookupInstrument ( mgr.getPatch () ) );
    cboBottomOctave.setSelectedIndex ( mgr.getBottomOctave () );
    cboTopOctave.setSelectedIndex ( mgr.getTopOctave () );
    cboRootNote.setSelectedIndex ( mgr.getRootNote () );
    cboNoteOrder.setSelectedIndex ( mgr.getNoteOrder () );
    sldArpeggioDelay.setValue ( mgr.getArpeggioDelay () );
    chkCascade.setSelected ( mgr.getCascade () );
    sldNoteLength.setValue ( mgr.getNoteLength () );
    SoundCollectionAdaptor key;
    try
    {
      for ( int i = 0; i < cboSoundType.getItemCount (); i++ )
      {
        key = ( SoundCollectionAdaptor ) cboSoundType.getItemAt ( i );
        _selectedSounds.put ( key.getName (), mgr.getSelectedPatterns ( key.getName () ) );
      }
      key = getSelectedSoundType ();
      lstSounds.setSelectedIndices ( _selectedSounds.get ( key.getName () ) );
      cboSoundType.setSelectedIndex ( mgr.getPatternType () );
      final Rectangle r = mgr.getSizePos ();
      if ( null != r )
      {
        setBounds ( r );
      }
    }
    catch ( final IOException e )
    {
      handleException ( e );
    }
    catch ( final ClassNotFoundException e )
    {
      handleException ( e );
    }
  }


  private int lookupSynth ( String name )
  {
    for ( int i = 0; i < cboSynth.getModel ().getSize (); i++ )
    {
      if ( name.equals ( ( ( Synthesizer ) cboSynth.getModel ().getElementAt ( i ) ).getDeviceInfo ().getName () ) )
      {
        return i;
      }
    }
    return 0;
  }
  
  
  private void loadSounds ()
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


  private void lstSoundsValueChanged ( ListSelectionEvent evt )
  {
    btnLoop.setEnabled ( lstSounds.getSelectedValues ().length != 0 );
    btnTest.setEnabled ( btnLoop.isEnabled () );
  }


  private void savePreferences ()
  {
    final PreferenceManager mgr = PreferenceManager.instanceOf ();
    mgr.setSynthName ( ( ( Synthesizer ) cboSynth.getSelectedItem () ).getDeviceInfo ().getName () );
    mgr.setInstrument ( ( Instrument ) cboInstrument.getSelectedItem () );
    mgr.setRootNote ( cboRootNote.getSelectedIndex () );
    mgr.setBottomOctave ( cboBottomOctave.getSelectedIndex () );
    mgr.setTopOctave ( cboTopOctave.getSelectedIndex () );
    mgr.setRootNote ( cboRootNote.getSelectedIndex () );
    mgr.setNoteOrder ( cboNoteOrder.getSelectedIndex () );
    mgr.setNoteLength ( sldNoteLength.getValue () );
    mgr.setArpeggioDelay ( sldArpeggioDelay.getValue () );
    mgr.setCascade ( chkCascade.isSelected () );
    mgr.setPatternType ( cboSoundType.getSelectedIndex () );
    saveSounds ( getSelectedSoundType () );
    try
    {
      for ( final String key : _selectedSounds.keySet () )
      {
        mgr.setSelectedPatterns ( key, _selectedSounds.get ( key ) );
      }
      mgr.setSizePos ( this.getBounds () );
    }
    catch ( final IOException e )
    {
      handleException ( e );
    }
  }


  private void saveSounds ( SoundCollectionAdaptor key )
  {
    _selectedSounds.put ( key.getName (), lstSounds.getSelectedIndices () );
  }


  /**
   * Sets the application's icon.
   */
  private void setApplicationIcon ()
  {
    final URL i = GuiUtil.getResource ( "/img/headphones.gif" );
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


  private void setupSoundTypes ()
  {
    final DefaultComboBoxModel model = new DefaultComboBoxModel ();
    final SoundTypeRepository rep = new SoundTypeRepository ();
    for ( final SoundCollectionAdaptor ipr: rep.getAllPatternGroups () )
    {
      model.addElement ( ipr );
    }
    cboSoundType.setModel ( model );
  }


  private void sldArpeggioDelayStateChanged ( ChangeEvent evt )
  {
    _model.setArpeggioDelay ( sldArpeggioDelay.getValue () );
  }


  private void sldNoteLengthStateChanged ( ChangeEvent evt )
  {
    _model.setNoteLength ( sldNoteLength.getValue () );
  }


  /**
   * @param evt
   */
  void soundPlaying ( SoundEvent evt )
  {
    lstSounds.setPlaying ( evt.getName () );
  }


  private static final long serialVersionUID = 1L;
  private JButton btnExit;
  private JButton btnAbout;
  private JToggleButton btnLoop;
  private JButton btnTest;
  private JComboBox cboBottomOctave;
  private JComboBox cboSynth;
  private JComboBox cboInstrument;
  private JComboBox cboNoteOrder;
  private JComboBox cboRootNote;
  private JComboBox cboSoundType;
  private JComboBox cboTopOctave;
  private JPanel pnlSliders;
  private JPanel pnlCombos;
  private JLabel lblArpeggioDelay;
  private JLabel lblBottomOctave;
  private JLabel lblSynth;
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

}
