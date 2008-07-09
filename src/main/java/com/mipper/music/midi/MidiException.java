/*
 * This file contains proprietary information of Rule Financial. 
 * Copying or reproduction without prior written approval is prohibited. 
 *
 * <b>Copyright</b> (c) 2004
 * <b>Company</b> Rule Financial
 */
package com.mipper.music.midi;


/**
 * 
 * @author Cliff Evans
 * @version $Revision: 1.1 $
 */
public class MidiException extends Exception
{

  /**
   * Constructor.
   */
  public MidiException ()
  {
    super ();
  }


  /**
   * Constructor.
   * 
   * @param message
   */
  public MidiException ( String message )
  {
    super ( message );
  }


  /**
   * Constructor.
   * 
   * @param message
   * @param cause
   */
  public MidiException ( String message, Throwable cause )
  {
    super ( message, cause );
  }


  /**
   * Constructor.
   * 
   * @param cause
   */
  public MidiException ( Throwable cause )
  {
    super ( cause );
  }
  
}
