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
import java.awt.Frame;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import com.mipper.util.Util;


/**
 * 
 * @author Cliff Evans
 * @version $Revision: 1.1 $
 */
public class AboutFrame extends JDialog
{

  /**
   * This is the default constructor
   * 
   * @param owner
   * @param title
   */
  public AboutFrame ( Frame owner, String title )
  {
    super ( owner, title );
    initialize ();
  }


  /**
   * This method initializes _bottom
   * 
   * @return javax.swing.JPanel
   */
  private JPanel get_bottom ()
  {
    if ( _bottom == null )
    {
      _bottom = new JPanel ();
      _bottom.add ( get_close (), null );
    }
    return _bottom;
  }


  /**
   * This method initializes _close
   * 
   * @return javax.swing.JButton
   */
  private JButton get_close ()
  {
    if ( _close == null )
    {
      _close = new JButton ();
      _close.setText ( ResourceBundle.getBundle ( "tete" )
                                     .getString ( "label.close" ) );
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


  /**
   * This method initializes _details
   * 
   * @return javax.swing.JTextPane
   */
  private JTextPane get_details ()
  {
    if ( _details == null )
    {
      _details = new JTextPane ();
      try
      {
        _details.setText ( Util.readMainManifest ( "tete.jar" ).toString () );
        _details.setFont ( new java.awt.Font ( "Arial", java.awt.Font.PLAIN, 12 ) );
        _details.setBackground ( new java.awt.Color ( 238, 238, 238 ) );
        _details.setForeground ( java.awt.Color.gray );
      }
      catch ( final IOException e )
      {
        _details.setText ( e.getMessage () );
      }
    }
    return _details;
  }


  /**
   * This method initializes jContentPane
   * 
   * @return javax.swing.JPanel
   */
  private JPanel getJContentPane ()
  {
    if ( jContentPane == null )
    {
      _logo = new JLabel ();
      _logo.setText ( "" );
      _logo.setIcon ( new ImageIcon ( getClass ().getResource ( "/img/tete_logo3.png" ) ) );
      jContentPane = new JPanel ();
      jContentPane.setLayout ( new BorderLayout () );
      jContentPane.add ( _logo, java.awt.BorderLayout.NORTH );
      jContentPane.add ( get_details (), java.awt.BorderLayout.CENTER );
      jContentPane.add ( get_bottom (), java.awt.BorderLayout.SOUTH );
    }
    return jContentPane;
  }


  /**
   * This method initializes this
   */
  private void initialize ()
  {
    setSize ( new java.awt.Dimension ( 562, 300 ) );
    setResizable ( false );
    setModal ( true );
    setDefaultCloseOperation ( javax.swing.WindowConstants.DISPOSE_ON_CLOSE );
    setContentPane ( getJContentPane () );
  }

  
  private static final long serialVersionUID = 1L;

  private JPanel jContentPane = null;
  private JLabel _logo = null;
  private JTextPane _details = null;
  private JPanel _bottom = null;
  private JButton _close = null;

}
