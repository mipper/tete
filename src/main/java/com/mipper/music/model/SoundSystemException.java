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
 *
 * @author Cliff Evans
 * @version $Id$
 */
public class SoundSystemException extends SoundException
{

  /**
   * Constructor.
   */
  public SoundSystemException ()
  {
    super ();
  }


  /**
   * Constructor.
   *
   * @param message
   */
  public SoundSystemException ( final String message )
  {
    super ( message );
  }


  /**
   * Constructor.
   *
   * @param message
   * @param cause
   */
  public SoundSystemException ( final String message, final Throwable cause )
  {
    super ( message, cause );
  }


  /**
   * Constructor.
   *
   * @param cause
   */
  public SoundSystemException ( final Throwable cause )
  {
    super ( cause );
  }


  private static final long serialVersionUID = 1L;

}
