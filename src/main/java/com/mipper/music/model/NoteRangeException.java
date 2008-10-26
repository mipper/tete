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
package com.mipper.music.model;


/**
 *
 * @author Cliff Evans
 * @version $Id$
 */
public class NoteRangeException extends RuntimeException
{

  /**
   * Constructor.
   *
   * @param low
   * @param high
   *
   */
  public NoteRangeException ( int low, int high )
  {
    this ( null, low, high );
  }


  /**
   * Constructor.
   *
   * @param message
   * @param low
   * @param high
   */
  public NoteRangeException ( String message, int low, int high )
  {
    super ( message );
    _low = low;
    _high = high;
  }


  /**
   * Constructor.
   *
   * @param message
   * @param cause
   */
  public NoteRangeException ( String message, Throwable cause )
  {
    super ( message, cause );
  }


  /**
   * Constructor.
   *
   * @param cause
   */
  public NoteRangeException ( Throwable cause )
  {
    super ( cause );
  }


  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString ()
  {
    return ( getMessage () == null ? "" : getMessage () ) + " [" + _low + ", " + _high + "]";
  }


  private static final long serialVersionUID = 3257005453798945075L;
  private int _low;
  private int _high;

}
