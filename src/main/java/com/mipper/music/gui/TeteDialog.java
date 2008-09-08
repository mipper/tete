/**
 * This file contains proprietary information of Rule Financial. 
 * Copying or reproduction without prior written approval is prohibited. 
 *
 * <b>Copyright</b> (c)2008
 * <b>Company</b> Rule Financial Ltd
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
 * @version $Revision: $
 */
public abstract class TeteDialog extends JDialog
{

  /**
   * @param owner
   * @param title
   */
  public TeteDialog ( Frame owner, String title )
  {
    super ( owner, title, true );
  }


  /**
   * @param dim Size to create window at.
   */
  protected void initialise ( Dimension dim )
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
  
        public void actionPerformed ( java.awt.event.ActionEvent e )
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