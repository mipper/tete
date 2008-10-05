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
package com.mipper.music.model;

/**
 * @author Cliff Evans
 * @version $Revision: $
 *
 */
public class SoundException extends Exception
{

  /**
   *
   */
  public SoundException ()
  {
    super ();
  }


  /**
   * @param message
   */
  public SoundException ( final String message )
  {
    super ( message );
  }


  /**
   * @param cause
   */
  public SoundException ( final Throwable cause )
  {
    super ( cause );
  }


  /**
   * @param message
   * @param cause
   */
  public SoundException ( final String message, final Throwable cause )
  {
    super ( message, cause );
  }


  private static final long serialVersionUID = 1L;

}