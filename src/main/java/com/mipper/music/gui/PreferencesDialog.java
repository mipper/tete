/*
 * Tete Ear Trainer Copyright (C) 2005-8 Cliff Evans
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
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.lang.StringUtils;
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
   * @param lastDir Directory to start looking from fields.
   * @param sbPath Path to the soundbank to load.
   */
  public PreferencesDialog ( final Frame owner,
                             final String title,
                             final PatternPlayerModel model,
                             final String lastDir,
                             final String sbPath )
  {
    super ( owner, title );
    initialise ( new Dimension ( 550, 200 ) );
    setSynth ( model.getSynth () );
    setSoundbankPath ( sbPath );
    setVelocity ( model.getVelocity () );
    _lastDir = new File ( lastDir );
  }


  @Override
  protected void initialise ( final Dimension dim )
  {
    super.initialise ( dim );
    initSynths ();
  }


  /**
   * @return The selected synthesizer.
   */
  public Synthesizer getSynth ()
  {
    return _synth;
  }


  /**
   * @param synth Sets the selected synthesizer.
   */
  public void setSynth ( final Synthesizer synth )
  {
    _synth = synth;
    cboSynth.setSelectedIndex ( lookupSynth ( synth ) );
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
    return sldVelocity.getValue ();
  }


  /**
   * @param velocity Velocity with which to play notes.
   */
  public void setVelocity ( final int velocity )
  {
    sldVelocity.setValue ( velocity );
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
    btnClear = new JButton ( "x" );
    btnClear.addActionListener ( new ActionListener ()
                        {
                          public void actionPerformed ( final ActionEvent evt )
                          {
                            txtSoundbank.setText ( "" );
                          }
                        } );
    btnSoundbank = new JButton ( "..." );
    btnSoundbank.addActionListener ( new ActionListener ()
          {
            public void actionPerformed ( final ActionEvent evt )
            {
              final JFileChooser chooser = new JFileChooser ();
              chooser.setMultiSelectionEnabled ( false );
              if ( StringUtils.isEmpty ( getSoundbankPath () ) )
              {
                chooser.setCurrentDirectory ( getLastDirectory () );
              }
              else
              {
                chooser.setSelectedFile ( new File ( getSoundbankPath () ) );
              }
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

    sldVelocity = new JSlider ( 0, 127 );
    sldVelocity.setMajorTickSpacing ( 32 );
    sldVelocity.setMinorTickSpacing ( 8 );
    sldVelocity.setPaintLabels ( true );
    sldVelocity.setPaintTicks ( true );
    sldVelocity.setSnapToTicks ( true );
    lblVelocity = new JLabel ( GuiUtil.readProperty ( "label.velocity" ) );

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
                                      .add ( btnClear )
                                      .add ( btnSoundbank ) )
                         .add ( sldVelocity ) ) );
    layout.setVerticalGroup (
      layout.createSequentialGroup ()
            .add ( layout.createParallelGroup ( GroupLayout.BASELINE )
                         .add ( lblSynth )
                         .add ( cboSynth ) )
            .add ( layout.createParallelGroup ( GroupLayout.BASELINE )
                         .add ( lblSoundbank )
                         .add ( txtSoundbank )
                         .add ( btnClear )
                         .add ( btnSoundbank ) )
            .add ( layout.createParallelGroup ( GroupLayout.BASELINE )
                         .add ( lblVelocity )
                         .add ( sldVelocity ) ) );
    return panel;
  }


  private File getLastDirectory ()
  {
    return _lastDir;
  }


  private void cboSynthItemStateChanged ( final ItemEvent evt )
  {
    if ( evt.getStateChange () == ItemEvent.SELECTED )
    {
      _synth = ( Synthesizer ) evt.getItem ();
//      _model.setSynth ( ( Synthesizer ) evt.getItem () );
    }
  }


  private void initSynths ()
  {
    cboSynth.setModel ( new DefaultComboBoxModel ( PatternPlayerModel.getAvailableSynths ()
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
  private JButton btnClear;
  private JLabel lblVelocity;
  private JSlider sldVelocity;
  private final File _lastDir;
  private Synthesizer _synth;

}
