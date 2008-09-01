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
import java.awt.Window;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;

import com.mipper.music.midi.MidiHelper;
import com.mipper.util.Logger;
import com.mipper.util.Util;


/**
 * About box windows.  Contains the version information read from the jar
 * manifest and the details of the MIDI system available on the running machine.
 * 
 * @author Cliff Evans
 * @version $Revision: 1.1 $
 */
public class AboutFrame extends JDialog
{

  /**
   * Constructor
   * 
   * @param owner Owning window.
   * @param title Title to display on window.
   */
  public AboutFrame ( Window owner, String title )
  {
    super ( owner, title );
    initialize ();
  }


  private void initialize ()
  {
    setSize ( new java.awt.Dimension ( 562, 350 ) );
    setResizable ( false );
    setModal ( true );
    setDefaultCloseOperation ( javax.swing.WindowConstants.DISPOSE_ON_CLOSE );
    setContentPane ( createContentPane () );
  }

  
  private JComponent createContentPane ()
  {
    if ( _contentPane == null )
    {
      _contentPane = new JPanel ();
      _contentPane.setLayout ( new BorderLayout () );
      _contentPane.add ( createLogo (), java.awt.BorderLayout.NORTH );
      _contentPane.add ( createBottom (), java.awt.BorderLayout.SOUTH );
      JTabbedPane tabs = new JTabbedPane ();
      tabs.addTab ( GuiUtil.readProperty ( "label.about" ), createAboutDetails () );
      tabs.addTab ( GuiUtil.readProperty ( "label.midi" ), createMidiDetails () );
      _contentPane.add ( tabs, java.awt.BorderLayout.CENTER );
    }
    return _contentPane;
  }


  private JLabel createLogo ()
  {
    _logo = new JLabel ();
    _logo.setText ( "" );
    _logo.setIcon ( new ImageIcon ( GuiUtil.getResource ( "/img/tete_logo3.png" ) ) );
    return _logo;
  }


  private JPanel createBottom ()
  {
    if ( _bottom == null )
    {
      _bottom = new JPanel ();
      _bottom.add ( createClose (), null );
    }
    return _bottom;
  }


  private JButton createClose ()
  {
    if ( _close == null )
    {
      _close = new JButton ();
      _close.setText ( GuiUtil.readProperty ( "label.close" ) );
      _close.addActionListener ( new java.awt.event.ActionListener ()
      {

        public void actionPerformed ( java.awt.event.ActionEvent e )
        {
          dispose ();
        }
      } );
    }
    return _close;
  }


  private JComponent createAboutDetails ()
  {
    if ( _details == null )
    {
      JTextPane txt = setupTextArea ();
      try
      {
        String jar = Util.getFilename ( this.getClass ().getProtectionDomain().getCodeSource().getLocation().toString () );
        if ( jar.toLowerCase ().endsWith ( ".jar" ) )
        {
          txt.setText ( Util.readMainManifest ( jar ).toString () );
        }
        else
        {
          txt.setText ( "Can't find version information." );
        }
      }
      catch ( final IOException e )
      {
        txt.setText ( e.getMessage () );
      }
      _details = new JScrollPane ( txt );
      txt.setCaretPosition ( 0 );
    }
    return _details;
  }


  private JComponent createMidiDetails ()
  {
    if ( _midi == null )
    {
      JTextPane txt = setupTextArea ();
      _midi = new JScrollPane ( txt );
      txt.setText ( MidiHelper.getMidiInfo ( false ) );
      txt.setCaretPosition ( 0 );
    }
    return _midi;
  }


  private JTextPane setupTextArea ()
  {
    JTextPane txt = new JTextPane ();
    txt.setFont ( new java.awt.Font ( "Arial", java.awt.Font.PLAIN, 12 ) );
    txt.setBackground ( new java.awt.Color ( 238, 238, 238 ) );
    txt.setForeground ( java.awt.Color.gray );
    return txt;
  }


  private static final long serialVersionUID = 1L;

  private JComponent _contentPane = null;
  private JLabel _logo = null;
  private JComponent _details = null;
  private JComponent _midi = null;
  private JPanel _bottom = null;
  private JButton _close = null;

}
