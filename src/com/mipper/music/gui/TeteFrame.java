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
import java.io.IOException;
import java.net.URL;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import javax.sound.midi.Instrument;
import javax.sound.midi.MidiUnavailableException;
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


/**
 * Main window for Tete.  Allows selection of sounds and configuration.
 * 
 * @author  Cliff Evans
 * @version $$Revision: 1.5 $$
 */
public class TeteFrame extends javax.swing.JFrame
{
  
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
    catch ( MidiException e )
    {
      handleException ( e );
      exitApp ();
    }
    catch ( MidiUnavailableException e )
    {
      handleException ( e );
      exitApp ();
    }
    Util.centreWindow ( this );
    setApplicationIcon ();
  }


  /**
   * Sets the application's icon.
   */
  private void setApplicationIcon ()
  {
    URL i = getClass ().getResource ( "/img/headphones.gif" );
    if ( i != null )
    {
      this.setIconImage ( new ImageIcon ( i ).getImage () );
    }
  }
  
  
  private void initMidi ()
    throws
      MidiUnavailableException,
      MidiException
  {
    _model = new PatternPlayerModel ();
    initInstruments ();
    initBottomOctave ();
    initTopOctave ();
    setRange ();
    setupSoundTypes ();
    _model.setInstrument ( ( Instrument ) cboInstrument.getSelectedItem () );
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
            Octave octave = ( Octave ) evt.getItem ();
            if ( octave.getOctaveNumber () > ( ( Octave ) cboTopOctave.getSelectedItem () ).getOctaveNumber () )
            {
              cboTopOctave.setSelectedIndex ( cboBottomOctave.getSelectedIndex () );
            }
            setRange ();
          }
        }
      });
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
            Octave octave = ( Octave ) evt.getItem ();
            if ( octave.getOctaveNumber () < ( ( Octave ) cboBottomOctave.getSelectedItem () ).getOctaveNumber () )
            {
              cboBottomOctave.setSelectedIndex ( cboTopOctave.getSelectedIndex () );
            }
            setRange ();
          }
        }
      });
  }
  
  
  private void setRange ()
  {
    Object root = cboRootNote.getSelectedItem ();
    _model.setNoteRange ( getOctaveRoot ( ( Octave ) cboBottomOctave.getSelectedItem () ),
                          getOctaveTop ( ( Octave ) cboTopOctave.getSelectedItem () ),
                          root instanceof Note ? ( Note ) root : null );
  }
  
  
  private int getOctaveRoot ( Octave octave )
  {
    return Octave.INTERVAL_COUNT + octave.getOctaveNumber () * Octave.INTERVAL_COUNT;
  }
  
  
  private int getOctaveTop ( Octave octave )
  {
    return getOctaveRoot ( octave ) + Octave.INTERVAL_COUNT - 1;
  }
  
  
  private void initInstruments ()
    throws
      MidiUnavailableException,
      MidiException
  {
    cboInstrument.setModel ( new DefaultComboBoxModel ( _model.getAvailableInstruments () ) );
    cboInstrument.setRenderer ( new DefaultListCellRenderer ()
      {
        private static final long serialVersionUID = 3258695407566928183L;
        public Component getListCellRendererComponent ( JList list,
                                                        Object value,
                                                        int index,
                                                        boolean isSelected,
                                                        boolean cellHasFocus )
        {
          JLabel lbl = ( JLabel ) super.getListCellRendererComponent ( list,
                                                                       value,
                                                                       index,
                                                                       isSelected,
                                                                       cellHasFocus );
          lbl.setText ( ( ( Instrument ) value ).getName () );
          return lbl;
        }
      }
    );
  }
  
  
  private void setupSoundTypes ()
  {
    DefaultComboBoxModel model = new DefaultComboBoxModel ();
    SoundTypeRepository rep = new SoundTypeRepository ();
    for ( SoundCollectionAdaptor ipr: rep.getAllPatternGroups () )
    {
      model.addElement ( ipr );
    }
    cboSoundType.setModel ( model );
  }
  
  
  private void loadSounds ()
  {
    DefaultListModel lm = new DefaultListModel ();
    for ( IntervalPattern ct: ( ( SoundCollectionAdaptor ) cboSoundType.getSelectedItem () ).getPatterns () )
    {
      lm.addElement ( ct );
    }
    lstSounds.setModel ( lm );
    lstSounds.setSelectionInterval ( 0, lm.getSize () - 1 );
    int[] selections = _selectedSounds.get ( getSelectedSoundType ().getName () );
    if ( selections != null )
    {
      lstSounds.setSelectedIndices ( selections );
    }
  }


  /**
   * @return The SoundCollectionAdaptor which is currently selected as the sound
   *         type.
   */
  private SoundCollectionAdaptor getSelectedSoundType ()
  {
    return ( SoundCollectionAdaptor ) cboSoundType.getSelectedItem ();
  }
  

  private void saveSounds ( SoundCollectionAdaptor key )
  {
    _selectedSounds.put ( key.getName (), lstSounds.getSelectedIndices () );
  }
  
  
  /**
   * @param evt
   */
  private void soundPlaying ( SoundEvent evt )
  {
    lstSounds.setPlaying ( evt.getName () );
  }


  private void loadPreferences ()
  {
    PreferenceManager mgr = PreferenceManager.instanceOf ();
    try
    {
      cboInstrument.setSelectedItem ( _model.lookupInstrument ( mgr.getPatch () ) );
    }
    catch ( MidiUnavailableException e )
    {
      handleException ( e );
    }
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
      Rectangle r = mgr.getSizePos ();
      if ( null != r )
      {
        setBounds ( r );
      }
    }
    catch ( IOException e )
    {
      handleException ( e );
    }
    catch ( ClassNotFoundException e )
    {
      handleException ( e );
    }
  }
  
  
  private void savePreferences ()
  {
    PreferenceManager mgr = PreferenceManager.instanceOf ();
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
      for ( String key : _selectedSounds.keySet () )
      {
        mgr.setSelectedPatterns ( key, _selectedSounds.get ( key ) );
      }
      mgr.setSizePos ( this.getBounds () );
    }
    catch ( IOException e )
    {
      handleException ( e );
    }
  }
  
  
  private void resetPerferences ()
  {
    try
    {
      PreferenceManager.instanceOf().reset();
    }
    catch ( BackingStoreException e )
    {
      handleException ( e );
    }
  }

  
  private void handleException ( Exception e )
  {
    Logger.error ( e );
    JOptionPane.showMessageDialog ( this,
                                    e.getMessage (), 
                                    ResourceBundle.getBundle ( "tete" )
                                                  .getString ( "title.error" ), 
                                    JOptionPane.ERROR_MESSAGE );
  }
  
  
  // TODO: Read default from model
  private void initComponents ()
  {
    pnlPlayback = new JPanel ();
    jPanel3 = new JPanel ();
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
    jPanel2 = new JPanel ();
    lblNoteLength = new JLabel ();
    sldNoteLength = new JSlider ();
    lblArpeggioDelay = new JLabel ();
    sldArpeggioDelay = new JSlider ();
    pnlControl = new JPanel ();
    btnLoop = new JToggleButton ();
    btnTest = new JButton ();
    btnExit = new JButton ();
    btnAbout = new JButton ();
    pnlSounds = new JPanel ();
    pnlSoundType = new JPanel ();
    lblSoundType = new JLabel ();
    cboSoundType = new JComboBox ();
    scrSounds = new JScrollPane ();
    lstSounds = new JSoundList ();
    chkCascade = new JCheckBox ();
    ResourceBundle bundle = ResourceBundle.getBundle ( "tete" );
    
    // TODO: figure out how to stop a window becoming too small
    getContentPane ().setMinimumSize ( new Dimension ( 394, 554 ) );
    addWindowListener ( new TeteWindowAdapter () );
    setDefaultCloseOperation ( WindowConstants.DO_NOTHING_ON_CLOSE );
    setTitle ( bundle.getString ( "title.application" ) );
    
    pnlPlayback.setLayout ( new GridBagLayout () );
    pnlPlayback.setBorder ( new TitledBorder ( bundle.getString ( "title.playback" ) ) );
    
    jPanel3.setLayout ( new GridLayout ( 10, 1 ) );
    lblInstrument.setLabelFor ( cboInstrument );
    lblInstrument.setText ( bundle.getString ( "label.instrument" ) );
    jPanel3.add ( lblInstrument );
    
    cboInstrument.setOpaque ( false );
    cboInstrument.addItemListener ( new ItemListener ()
    {
      public void itemStateChanged ( ItemEvent evt )
      {
        cboInstrumentItemStateChanged ( evt );
      }
    } );
    jPanel3.add ( cboInstrument );
    
    lblBottomOctave.setLabelFor ( cboBottomOctave );
    lblBottomOctave.setText ( bundle.getString ( "label.bottom-octave" ) );
    jPanel3.add ( lblBottomOctave );
    
    cboBottomOctave.setPreferredSize ( new java.awt.Dimension ( 29, 17 ) );
    jPanel3.add ( cboBottomOctave );
    
    lblTopOctave.setLabelFor ( cboTopOctave );
    lblTopOctave.setText ( bundle.getString ( "label.top-octave" ) );
    jPanel3.add ( lblTopOctave );
    
    cboTopOctave.setPreferredSize ( new java.awt.Dimension ( 29, 17 ) );
    jPanel3.add ( cboTopOctave );
    
    lblRootNote.setLabelFor ( cboRootNote );
    lblRootNote.setText ( bundle.getString ( "label.root-note" ) );
    jPanel3.add ( lblRootNote );
    
    DefaultComboBoxModel m = new DefaultComboBoxModel ( Note.values () );
    m.insertElementAt ( bundle.getString ( "label.random" ), 0 );
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
    jPanel3.add ( cboRootNote );
    
    lblNoteOrder.setLabelFor ( cboNoteOrder );
    lblNoteOrder.setText ( bundle.getString ( "label.direction" ) );
    jPanel3.add ( lblNoteOrder );
    
    cboNoteOrder.setModel ( new javax.swing.DefaultComboBoxModel ( new String[]
       {bundle.getString ( "label.random" ),
        bundle.getString ( "label.ascending" ),
        bundle.getString ( "label.descending" )} ) );
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
    jPanel3.add ( cboNoteOrder );
    
    GridBagConstraints gridBagConstraints;
    gridBagConstraints = new java.awt.GridBagConstraints ();
//    gridBagConstraints.gridwidth = GridBagConstraints.RELATIVE;
//    gridBagConstraints.gridheight = GridBagConstraints.RELATIVE;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
//    gridBagConstraints.anchor = GridBagConstraints.NORTH;
    pnlPlayback.add ( jPanel3, gridBagConstraints );
    
    jPanel2.setLayout ( new java.awt.GridLayout ( 5, 1 ) );
    lblNoteLength.setLabelFor ( sldNoteLength );
    lblNoteLength.setText ( bundle.getString ( "label.note-length" ) );
    jPanel2.add ( lblNoteLength );
    
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
    jPanel2.add ( sldNoteLength );
    
    lblArpeggioDelay.setLabelFor ( sldArpeggioDelay );
    lblArpeggioDelay.setText ( bundle.getString ( "label.agpeggio-dely" ) );
    jPanel2.add ( lblArpeggioDelay );
    
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
    jPanel2.add ( sldArpeggioDelay );
    
    chkCascade.setHorizontalTextPosition ( SwingConstants.LEADING  ); 
    chkCascade.setText ( bundle.getString ( "label.cascade" ) );
    chkCascade.addItemListener ( new ItemListener ()
      {
        public void itemStateChanged ( ItemEvent evt )
        {
          _model.setCascade( chkCascade.isSelected () );
        }
      } );
    jPanel2.add ( chkCascade );
    
    gridBagConstraints = new java.awt.GridBagConstraints ();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = GridBagConstraints.NORTH;
    pnlPlayback.add ( jPanel2, gridBagConstraints );
    
    getContentPane ().add ( pnlPlayback, BorderLayout.WEST );
    
    btnLoop.setText ( bundle.getString ( "label.play" ) );
    btnLoop.addActionListener ( new ActionListener ()
    {
      public void actionPerformed ( ActionEvent evt )
      {
        btnLoopActionPerformed ( evt );
      }
    } );
    pnlControl.add ( btnLoop );
    
    btnTest.setText ( bundle.getString ( "label.test" ) );
    btnTest.addActionListener ( new ActionListener ()
    {
      public void actionPerformed ( ActionEvent evt )
      {
        btnTestActionPerformed ( evt );
      }
    } );
    pnlControl.add ( btnTest );
    
    btnExit.setText ( bundle.getString ( "label.exit" ) );
    btnExit.addActionListener ( new ActionListener ()
    {

      public void actionPerformed ( ActionEvent evt )
      {
        exitApp ();
      }
    } );
    pnlControl.add ( btnExit );
    
    btnAbout.setText ( bundle.getString ( "label.about" ) );
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
    pnlSounds.setBorder ( new TitledBorder ( bundle.getString ( "label.sounds" ) ) );
    pnlSoundType.setLayout ( new GridLayout ( 2, 1 ) );
    lblSoundType.setLabelFor ( cboSoundType );
    lblSoundType.setText ( bundle.getString ( "label.type" ) );
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
    
    getContentPane ().add ( pnlSounds, BorderLayout.CENTER );
    
    pack ();
  }

  
  private void btnTestActionPerformed (ActionEvent evt)
  {
    try
    {
      _model.setPatterns ( lstSounds.getSelectedValues () );
      TeteTestFrame test = new TeteTestFrame ( this,
                                               ResourceBundle.getBundle ( "tete" )
                                                             .getString ( "title.test" ),
                                               _model );
      test.setVisible ( true );
    }
    catch ( Exception e )
    {
      throw new RuntimeException ( e );
    }
  }

  
  private void lstSoundsValueChanged (ListSelectionEvent evt)
  {
    btnLoop.setEnabled ( lstSounds.getSelectedValues ().length != 0 );
    btnTest.setEnabled ( btnLoop.isEnabled () );
  }

  
  private class TeteWindowAdapter extends WindowAdapter
  {
    
    /**
     * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
     */
    public void windowClosing ( WindowEvent e )
    {
      exitApp ();
    }
    
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
  
  
  private void btnAboutActionPerformed ( ActionEvent evt )
  {
    AboutFrame f = new AboutFrame ( this, ResourceBundle.getBundle ( "tete" )
                                                        .getString ( "title.about" ) );
    Util.centreWindow ( f );
    f.setVisible ( true );
  }
  
  
  private void cboInstrumentItemStateChanged (ItemEvent evt)
  {
    if ( evt.getStateChange () == ItemEvent.SELECTED )
    {
      _model.setInstrument ( ( Instrument ) evt.getItem () );
    }
  }

  
  private void sldArpeggioDelayStateChanged (ChangeEvent evt)
  {
    _model.setArpeggioDelay ( sldArpeggioDelay.getValue () );
  }

  
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
      catch ( Exception e )
      {
        handleException ( e );
      }
      btnLoop.setText ( ResourceBundle.getBundle ( "tete" )
                                      .getString ( "label.stop" ) );
      btnTest.setEnabled ( false );
    }
    else
    {
      _looper.stop ();
      _looper = null;
      btnLoop.setText ( ResourceBundle.getBundle ( "tete" )
                                      .getString ( "label.play" ) );
      btnTest.setEnabled ( true );
      lstSounds.clearPlaying ();
    }
  }
  
  
  private void sldNoteLengthStateChanged (ChangeEvent evt)
  {
    _model.setNoteLength ( sldNoteLength.getValue () );
  }
    

  private Boolean decodeDirection ()
  {
    int idx = cboNoteOrder.getSelectedIndex ();
    return idx == 0 ? null : new Boolean ( idx == 1 );
  }
  
  
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
  
  
  private JButton btnExit;
  private JButton btnAbout;
  private JToggleButton btnLoop;
  private JButton btnTest;
  private JComboBox cboBottomOctave;
  private JComboBox cboInstrument;
  private JComboBox cboNoteOrder;
  private JComboBox cboRootNote;
  private JComboBox cboSoundType;
  private JComboBox cboTopOctave;
  private JPanel jPanel2;
  private JPanel jPanel3;
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
  
  private static final long serialVersionUID = 3905236849197593913L;
  private PatternPlayerModel _model;
  private ContinuousPlayer _looper;
  private Hashtable<String, int[]> _selectedSounds = new Hashtable<String, int[]> ();
  
}
