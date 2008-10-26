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

import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;


/**
 * @author Cliff Evans
 * @version $Id$
 */
public abstract class TeteDialog extends JDialog
{

  /**
   * @param owner
   * @param title
   */
  public TeteDialog ( final Frame owner, final String title )
  {
    super ( owner, title, true );
  }


  /**
   * @param dim Size to create window at.
   */
  protected void initialise ( final Dimension dim )
  {
    setSize ( dim );
    setDefaultCloseOperation ( javax.swing.WindowConstants.DISPOSE_ON_CLOSE );
    setContentPane ( createContentPane () );
  }


  /**
   * @return JComponent to act as the main window.
   */
  protected abstract JComponent createContentPane ();


  /**
   * @return JPanel containing a close button.
   */
  protected JPanel createBottom ()
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

        public void actionPerformed ( final java.awt.event.ActionEvent e )
        {
          dispose ();
        }
      } );
    }
    return _close;
  }


  private JPanel _bottom = null;
  private JButton _close = null;

}