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
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jdesktop.layout.GroupLayout;

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
   * @param velocity Velocity to play midi notes.
   */
  public PreferencesDialog ( final Frame owner,
                             final String title,
                             final PatternPlayerModel model,
                             final String sbPath,
                             final int velocity )
  {
    super ( owner, title );
    _model = model;
    initialise ( new Dimension ( 400, 400 ) );
    setSoundbankPath ( sbPath );
    setVelocity ( velocity );
  }


  @Override
  protected void initialise ( final Dimension dim )
  {
    super.initialise ( dim );
    initSynths ();
    cboSynth.setSelectedIndex ( lookupSynth ( _model.getSynth () ) );
    spnVelocity.setValue ( _model.getVelocity () );
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
  public void setSoundbankPath ( final String path )
  {
    txtSoundbank.setText ( path );
  }


  /**
   * @return Velocity with which to play notes.
   */
  public int getVelocity ()
  {
    return ( ( SpinnerNumberModel ) spnVelocity.getModel () ).getNumber ().intValue ();
  }


  /**
   * @param velocity Velocity with which to play notes.
   */
  public void setVelocity ( final int velocity )
  {
    spnVelocity.getModel ().setValue ( velocity );
  }


  /* (non-Javadoc)
   * @see com.mipper.music.gui.TeteDialog#createContentPane()
   */
  @Override
  protected JComponent createContentPane ()
  {
    final JPanel panel = new JPanel ();
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
                                  public void itemStateChanged ( final ItemEvent evt )
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
      public void actionPerformed ( final ActionEvent evt )
      {
        final JFileChooser chooser = new JFileChooser ();
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

    spnVelocity = new JSpinner ( new SpinnerNumberModel ( 64, 0, 127, 1 ) );
    lblVelocity = new JLabel ( GuiUtil.readProperty ( "label.velocity" ) );

//    JTabbedPane tabs = new JTabbedPane ();
//    tabs.addTab ( GuiUtil.readProperty ( "label.about" ), createAboutDetails () );
//    tabs.addTab ( GuiUtil.readProperty ( "label.midi" ), createMidiDetails () );
//    return tabs;
    final JPanel panel = new JPanel ();
    final GroupLayout layout = new GroupLayout ( panel );
    layout.setAutocreateContainerGaps ( true );
    layout.setAutocreateGaps ( true );
    panel.setLayout ( layout );

    layout.setHorizontalGroup (
      layout.createSequentialGroup ()
            .add ( layout.createParallelGroup ( GroupLayout.LEADING )
                         .add ( lblSynth )
                         .add ( lblSoundbank )
                         .add ( lblVelocity ) )
            .add ( layout.createParallelGroup ( GroupLayout.LEADING )
                         .add ( cboSynth )
                         .add ( layout.createSequentialGroup ()
                                      .add ( txtSoundbank )
                                      .add ( btnSoundbank ) )
                         .add ( spnVelocity ) ) );
    layout.setVerticalGroup (
      layout.createSequentialGroup ()
            .add ( layout.createParallelGroup ( GroupLayout.BASELINE )
                         .add ( lblSynth )
                         .add ( cboSynth ) )
            .add ( layout.createParallelGroup ( GroupLayout.BASELINE )
                         .add ( lblSoundbank )
                         .add ( txtSoundbank )
                         .add ( btnSoundbank ) )
            .add ( layout.createParallelGroup ( GroupLayout.BASELINE )
                         .add ( lblVelocity )
                         .add ( spnVelocity ) ) );
    return panel;
  }


  private void cboSynthItemStateChanged (final ItemEvent evt)
  {
    if ( evt.getStateChange () == ItemEvent.SELECTED )
    {
      final Synthesizer synth = ( Synthesizer ) evt.getItem ();
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
          lbl.setText ( ( ( Synthesizer ) value ).getDeviceInfo ().getName () );
          return lbl;
        }
        private static final long serialVersionUID = 1L;
      }
    );
  }


  private void loadSoundbank ( final File file )
  {
    txtSoundbank.setText ( file.getAbsolutePath () );
  }


  private int lookupSynth ( final Synthesizer synth )
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
  private JLabel lblVelocity;
  private JSpinner spnVelocity;
  private final PatternPlayerModel _model;

}
