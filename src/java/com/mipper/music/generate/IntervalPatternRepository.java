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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import com.mipper.music.model.IntervalPattern;
import com.mipper.music.model.SoundCollectionAdaptor;


/**
 * 
 * @author Cliff Evans
 * @version $Revision: 1.1 $
 */
public class IntervalPatternRepository
  implements
    SoundCollectionAdaptor
{

  /**
   * Constructor.
   * 
   * @param name Name of the pattern collection.
   */
  public IntervalPatternRepository ( String name )
  {
    super ();
    _name = name;
  }
  
  
  /**
   * @return Name of this collection of patterns.
   */
  public String getName ()
  {
    return _name;
  }
  
  
  /**
   * @param chord Chord to add to the repository.
   */
  public void registerPattern ( IntervalPattern chord )
  {
    _allPatterns.add ( chord );
  }
  
  
  /**
   * @param pattern Chord to remove from the repository.
   */
  public void unregisterPattern ( IntervalPattern pattern )
  {
    _allPatterns.remove ( pattern );
  }
  

  /**
   * @param name Name of the chord to retrieve.
   * 
   * @return ChordType with the given name.
   * 
   * @throws NotFoundException
   */
  public IntervalPattern getPattern ( String name )
    throws
      NotFoundException
  {
    IntervalPattern p = findPattern ( name );
    if ( p != null )
    {
      return p;
    }
    throw new NotFoundException ( "Cannot find Chord type: " + name );
  }
  
  
  /**
   * @return The number of ChordTypes in the repository.
   */
  public int count ()
  {
    return _allPatterns.size ();
  }
  
  
  /**
   * @return Set of chord type names in the repository.
   */
  public Collection<String> getPatternNames ()
  {
    ArrayList<String> res = new ArrayList<String> ( count () );
    for ( int i = 0; i < count (); i++ )
    {
      res.add ( _allPatterns.get ( i ).getName () );
    }
    return res;
  }
  
  
  /**
   * @return Collection of all chord types in the repository.
   */
  public Collection<IntervalPattern> getPatterns ()
  {
    return new LinkedList<IntervalPattern> ( _allPatterns );
  }
  
  
  private IntervalPattern findPattern ( String name )
  {
    for ( int i = 0; i < count (); i++ )
    {
      if ( _allPatterns.get ( i ).getName ().equals ( name ) )
      {
        return _allPatterns.get ( i );
      }
    }
    return null;
  }
  
  
  /**
   * @see java.lang.Object#toString()
   */
  public String toString ()
  {
    return getName ();
  }


  private String _name;
  private LinkedList<IntervalPattern> _allPatterns = new LinkedList<IntervalPattern> ();
  
}
