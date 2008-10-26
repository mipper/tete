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
package com.mipper.music.generate;


/**
 *
 * @author Cliff Evans
 * @version $Id$
 */
public class NotFoundException extends Exception
{

  /**
   * Constructor.
   *
   */
  public NotFoundException ()
  {
    super ();
  }


  /**
   * Constructor.
   *
   * @param message
   */
  public NotFoundException ( String message )
  {
    super ( message );
  }


  /**
   * Constructor.
   *
   * @param message
   * @param cause
   */
  public NotFoundException ( String message, Throwable cause )
  {
    super ( message, cause );
  }


  /**
   * Constructor.
   *
   * @param cause
   */
  public NotFoundException ( Throwable cause )
  {
    super ( cause );
  }


  private static final long serialVersionUID = 3257007640071911473L;

}
