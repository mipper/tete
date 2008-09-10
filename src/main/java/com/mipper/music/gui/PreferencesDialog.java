/**
 * This file contains proprietary information of Rule Financial. 
 * Copying or reproduction without prior written approval is prohibited. 
 *
 * <b>Copyright</b> (c)2008
 * <b>Company</b> Rule Financial Ltd
 */
package com.mipper.music.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.sound.midi.Synthesizer;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.mipper.music.generate.PatternPlayerModel;
import com.mipper.util.Logger;


/**
 * @author Cliff Evans
 * @version $Revision: $
 */
@SuppressWarnings("synthetic-access")
public class PreferencesDialog extends TeteDialog
{

  /**
   * @param owner
   * @param title
   * @param model
   * @param sbPath Path to the soundbank to load.
   */
  public PreferencesDialog ( Frame owner,
                             String title,
                             PatternPlayerModel model,
                             String sbPath )
  {
    super ( owner, title );
    _model = model;
    initialise ( new Dimension ( 400, 400 ) );
    setSoundbankPath ( sbPath );
  }


  @Override
  protected void initialise ( Dimension dim )
  {
    super.initialise ( dim );
    initSynths ();
    cboSynth.setSelectedIndex ( lookupSynth ( _model.getSynth () ) );
  }
  
  
  /**
   * @return Path to the soundbank to load.
   */
  public String getSoundbankPath ()
  {
    return txtSoundbank.getText ();
  }
  
  
  /**
   * @param path Path to the soundbank to load.
   */
  public void setSoundbankPath ( String path )
  {
    txtSoundbank.setText ( path );
  }
  
  
  /* (non-Javadoc)
   * @see com.mipper.music.gui.TeteDialog#createContentPane()
   */
  @Override
  protected JComponent createContentPane ()
  {
    JPanel panel = new JPanel ();
    panel.setLayout ( new BorderLayout () );
    panel.add ( createBottom (), java.awt.BorderLayout.SOUTH );
    panel.add ( createMain (), java.awt.BorderLayout.NORTH );
    return panel;
  }


  private JComponent createMain ()
  {
    cboSynth = new JComboBox ();
    cboSynth.setOpaque ( false );
    cboSynth.addItemListener ( new ItemListener ()
                                {
                                  public void itemStateChanged ( ItemEvent evt )
                                  {
                                    cboSynthItemStateChanged ( evt );
                                  }
                                } );
    
    lblSynth = new JLabel ( GuiUtil.readProperty ( "label.synth" ) );
    lblSynth.setLabelFor ( cboSynth );
    
    txtSoundbank = new JTextField ();
    btnSoundbank = new JButton ( "..." );
    btnSoundbank.addActionListener ( new ActionListener ()
    {
      public void actionPerformed ( ActionEvent evt )
      {
        JFileChooser chooser = new JFileChooser ();
        chooser.setMultiSelectionEnabled ( false );
        chooser.setSelectedFile ( new File ( getSoundbankPath () ) );
        chooser.setFileFilter ( new FileNameExtensionFilter ( "Midi Soundbank files (sf2, dls)",
                                                              "sf2", 
                                                              "dls" ) );
        
        if ( JFileChooser.APPROVE_OPTION == chooser.showOpenDialog ( btnSoundbank ) )
        {
          Logger.debug ( "Chose: {0}", chooser.getSelectedFile () );
          loadSoundbank ( chooser.getSelectedFile () );
        }
      }
    } );
    
    lblSoundbank = new JLabel ( GuiUtil.readProperty ( "label.soundbank" ) );
    lblSoundbank.setLabelFor ( txtSoundbank );
    
//    JTabbedPane tabs = new JTabbedPane ();
//    tabs.addTab ( GuiUtil.readProperty ( "label.about" ), createAboutDetails () );
//    tabs.addTab ( GuiUtil.readProperty ( "label.midi" ), createMidiDetails () );
//    return tabs;
    JPanel panel = new JPanel ();
    panel.setLayout ( new GridLayout ( 2, 3 ) );
    panel.add ( lblSynth );
    panel.add ( cboSynth );
    panel.add ( new JLabel () );
    panel.add ( lblSoundbank );
    panel.add ( txtSoundbank );
    panel.add ( btnSoundbank );
    return panel;
  }


  private void cboSynthItemStateChanged (ItemEvent evt)
  {
    if ( evt.getStateChange () == ItemEvent.SELECTED )
    {
      Synthesizer synth = ( Synthesizer ) evt.getItem ();
//      try
//      {
//        for ( Instrument i: sb.getInstruments () )
//        {
//          Logger.debug ( "cboSynthItemStateChanged Instrument: {0}", i );
//        }
        _model.setSynth ( synth );
//      }
//      catch ( MidiUnavailableException e )
//      {
//        Logger.error ( e );
//        _model.setInstrument ( synth.getAvailableInstruments ()[0] );
////        cboInstrument.setSelectedIndex ( 0 );
//      }
//      catch ( MidiException e )
//      {
//        Logger.error ( e );
//        _model.setInstrument ( synth.getAvailableInstruments ()[0] );
////        cboInstrument.setSelectedIndex ( 0 );
//      }
//      catch ( FileNotFoundException e )
//      {
//        Logger.error ( e );
//        _model.setInstrument ( synth.getAvailableInstruments ()[0] );
////        cboInstrument.setSelectedIndex ( 0 );
//      }
    }
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

  
  private void loadSoundbank ( File file )
  {
    txtSoundbank.setText ( file.getAbsolutePath () );
  }
  
  
  private int lookupSynth ( Synthesizer synth )
  {
    for ( int i = 0; i < cboSynth.getModel ().getSize (); i++ )
    {
      if ( synth.getClass () == ( ( Synthesizer ) cboSynth.getModel ().getElementAt ( i ) ).getClass () )
      {
        return i;
      }
    }
    return 0;
  }
  
  
  private static final long serialVersionUID = 1L;

  private JLabel lblSynth;
  private JComboBox cboSynth;
  private JLabel lblSoundbank;
  private JTextField txtSoundbank;
  private JButton btnSoundbank;
  private PatternPlayerModel _model;

}
