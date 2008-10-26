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
import java.awt.Dimension;
import java.awt.Frame;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;

import com.mipper.music.midi.MidiHelper;
import com.mipper.util.Util;


/**
 * About box windows.  Contains the version information read from the jar
 * manifest and the details of the MIDI system available on the running machine.
 *
 * @author Cliff Evans
 * @version $Id$
 */
public class AboutFrame
  extends
    TeteDialog
{

  /**
   * Constructor
   *
   * @param owner Owning window.
   * @param title Title to display on window.
   */
  public AboutFrame ( final Frame owner, final String title )
  {
    super ( owner, title );
    initialise ( new Dimension ( 562, 350 ) );
  }


  @Override
  protected final JComponent createContentPane ()
  {
    final JPanel panel = new JPanel ();
    panel.setLayout ( new BorderLayout () );
    panel.add ( createLogo (), java.awt.BorderLayout.NORTH );
    panel.add ( createBottom (), java.awt.BorderLayout.SOUTH );
    final JTabbedPane tabs = new JTabbedPane ();
    tabs.addTab ( GuiUtil.readProperty ( "label.about" ), createAboutDetails () );
    tabs.addTab ( GuiUtil.readProperty ( "label.midi" ), createMidiDetails () );
    panel.add ( tabs, java.awt.BorderLayout.CENTER );
    return panel;
  }


  private JLabel createLogo ()
  {
    final JLabel label = new JLabel ( "" );
    final URL url = GuiUtil.getResourceUrl ( "/img/tete_logo3.png" );
    if ( null != url )
    {
      label.setIcon ( new ImageIcon ( url ) );
    }
    return label;
  }


  private JComponent createAboutDetails ()
  {
    final JTextPane txt = setupTextArea ();
    try
    {
      final String jar = Util.getFilename ( this.getClass ().getProtectionDomain().getCodeSource().getLocation().toString () );
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
    final JScrollPane scroll = new JScrollPane ( txt );
    txt.setCaretPosition ( 0 );
    return scroll;
  }


  private JComponent createMidiDetails ()
  {
    final JTextPane txt = setupTextArea ();
    final JScrollPane scroll = new JScrollPane ( txt );
    txt.setText ( MidiHelper.getMidiInfo ( true ) );
    txt.setCaretPosition ( 0 );
    return scroll;
  }


  private JTextPane setupTextArea ()
  {
    final JTextPane txt = new JTextPane ();
    txt.setFont ( new java.awt.Font ( "Arial", java.awt.Font.PLAIN, 12 ) );
    txt.setBackground ( new java.awt.Color ( 238, 238, 238 ) );
    txt.setForeground ( java.awt.Color.gray );
    return txt;
  }


  private static final long serialVersionUID = 1L;

}
