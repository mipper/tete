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

import java.util.Collection;



/**
 * Allow us to add different things to the Sound Type combo and create the
 * entries for the list.
 * 
 * @author Cliff Evans
 * @version $$Revision: 1.1 $$
 */
public interface SoundCollectionAdaptor
{
  
  /**
   * @return Name for this collection of Interval Patterns.
   */
  String getName ();
  
  
  /**
   * @return Collection of all interval patterns in this collection.
   */
  Collection<IntervalPattern> getPatterns ();
  
}
