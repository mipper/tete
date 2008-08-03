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
package com.mipper.util;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang.StringUtils;


/**
 * Utility methods.
 *
 * @author Cliff Evans
 * @version $Revision: 1.3 $
 */
public class Util
{

  /**
   * Returns the specified date a number of days added.
   *
   * @param date End period date of a previous period
   * @param num Number of days to add.  May be -ve.
   *
   * @return Returns a date num days away from date.
   */
  public static Date addDays ( Date date, int num )
  {
    return calculateDate ( date, Calendar.DAY_OF_MONTH, num );
  }


  /**
   * Returns the specified date a number of months added.
   *
   * @param date End period date of a previous period
   * @param num Number of months to add.  May be -ve.
   *
   * @return Returns a date num months away from date.
   */
  public static Date addMonths ( Date date, int num )
  {
    return calculateDate ( date, Calendar.MONTH, num );
  }


  /**
   * Adds a number of years to a date.
   *
   * @param date Date to add a year to.
   * @param num Number of year to add.  May be -ve.
   *
   * @return A date 1 year after the specified date.
   */
  public static Date addYears ( Date date, int num )
  {
    return calculateDate ( date, Calendar.YEAR, num );
  }


  /**
   * Adds components to a date.
   *
   * @param date The date to use in the calculation.
   * @param part Constant from @link java.util.Calendar.
   * @param num Number to add - this can be negetive.
   *
   * @return Date after applying the specified changes.
   */
  public static Date calculateDate ( Date date, int part, int num )
  {
    if ( null == date )
    {
      return null;
    }
    final Calendar cal = new GregorianCalendar ();
    cal.setTime ( date );
    cal.set ( part, cal.get ( part ) + num );
    return cal.getTime ();
  }


  /**
   * Centers a window in relation to a parent window.
   *
   * @param parent Parent window in which to centre the other window.
   * @param w Window to centre in relation to the parent window.
   */
  public static void centreWindow ( Window parent, Window w )
  {
    w.setLocation ( parent.getX () + ( parent.getWidth () - w.getWidth () ) / 2,
                    parent.getY () + ( parent.getHeight () - w.getHeight () ) / 2 );
  }


  /**
   * Centres the speicified window in the screen.
   *
   * @param w Window to centre.
   */
  public static void centreWindow ( Window w )
  {
    final Dimension screenSize = Toolkit.getDefaultToolkit ().getScreenSize ();
    w.setLocation ( ( screenSize.width - w.getWidth () ) / 2,
                    ( screenSize.height - w.getHeight () ) / 2 );
  }


  /**
   * Concatenates two object arrays.
   *
   * @param first First array.
   * @param second Second array.
   *
   * @return A single array containing the first array followd by the second.
   */
  public static Object[] concat ( Object[] first, Object[] second )
  {
    final int len1 = first == null ? 0 : first.length;
    final int len2 = second == null ? 0 : second.length;
    final Object[] result = new Object[len1 + len2];
    if ( len1 > 0 )
    {
      System.arraycopy ( first, 0, result, 0, len1 );
    }
    if ( len2 > 0 )
    {
      System.arraycopy ( second, 0, result, len1, len2 );
    }
    return result;
  }


  /**
   * Concatinates two strings together to create a full path string.  The caller
   * need not worry about path seperators between the two parts of the path.
   *
   * Be careful how you use this function since it will return the OS absolute
   * path to the file.  i.e. if you don't give it an absolute path it will return
   * a value relative to the processes working directory.
   *
   * e.g.  Given dir/file.xml it could return C:/Jboss/bin/dir/file.xml
   *
   * @param root The root part of the desired path.
   * @param tail The tail part of the desired path.
   *
   * @return String The absolute path of the concatanated strings.
   */
  public static String concatPaths ( String root, String tail )
  {
    return new File ( root, tail ).getAbsolutePath ();
  }


  /**
   * Creates all directories which don't exist in the given path.
   *
   * @param dirs path to create.
   * 
   * @return true if directories created, false if not.
   */
  public static boolean createDirs ( String dirs )
  {
    if ( null != dirs )
    {
      return new File ( dirs ).mkdirs ();
    }
    return false;
  }


  /**
   * Creates a text file with the given filename containing the string passed
   * in.  If there is a path component to the filename then all directories will
   * be created.
   *
   * @param filename Path to the file.
   * @param contents Contents of the file.
   *
   * @throws IOException
   */
  public static void createFile ( String filename, String contents )
    throws
      IOException
  {
    final File file = new File ( filename );
    createDirs ( file.getParent() );
    final PrintWriter fout = new PrintWriter ( new FileWriter ( file ) );
    fout.println ( contents );
    fout.close ();
  }


  /**
   * Improved File.delete method that allows recursive directory deletion.
   *
   * @param filePath Path of file or directory.
   * @param recursive True if all sub-directories should be deleted.
   *
   * @return True if the deletion was successful.
   */
  public static boolean delete ( String filePath, boolean recursive )
  {
  	final File file = new File ( filePath );
  	if ( !file.exists () )
    {
  		return true;
    }

  	if ( !recursive || !file.isDirectory () )
    {
  		return file.delete ();
    }

  	final String[] list = file.list ();
  	for ( int i = 0; i < list.length; i++ )
    {
  		if ( !delete ( filePath + File.separator + list[i], true ) )
      {
  			return false;
      }
  	}

  	return file.delete ();
  }


  /**
   * Deletes the specified file.
   *
   * @param filename Path to the file to delete.
   *
   * @return true if file deleted, false if not.
   */
  public static boolean deleteFile ( String filename )
  {
    return new File ( filename ).delete ();
  }


  /**
   * Converts the contents of a byte array into a string for display.
   *
   * @param cells byte array to convert.
   *
   * @return String representation of the array.
   */
  public static String display1DArray ( byte[] cells )
  {
    String result = "";
    for ( final byte element : cells )
    {
      result += ( char ) element;
    }
    return result;
  }


  /**
   * Converts the contents of a byte array into a string for display.
   *
   * @param cells byte array to convert.
   *
   * @return String representation of the array.
   */
  public static String display1DArray ( int[] cells )
  {
    final StringBuffer buf = new StringBuffer ();
    for ( final int element : cells )
    {
      buf.append ( element );
    }
    return buf.toString ();
  }


  /**
   * Displays an array of objects as a list of string values.
   *
   * @param cells Values to display.
   *
   * @return String showing the contents of the array.
   */
  public static String display1DArray ( Object[] cells )
  {
    return display1DArray ( null, "\n", cells );
  }


  /**
   * Displays an array of objects as a list of string values.
   *
   * @param header Header to give the list.
   * @param cells Values to display.
   *
   * @return String showing the contents of the array.
   */
  public static String display1DArray ( String header, Object[] cells )
  {
    return display1DArray ( header, "\n", cells );
  }


  /**
   * Displays an array of objects as a list of string values.
   *
   * @param header Header to give the list.
   * @param delim String to append after each array value.
   * @param cells Values to display.
   *
   * @return String showing the contents of the array.
   */
  public static String display1DArray ( String header, String delim, Object[] cells )
  {
    final StringBuffer buf = new StringBuffer ();
    appendHeader ( buf, header );
    if ( null != cells )
    {
      for ( final Object element : cells )
      {
          buf.append ( element ).append ( delim );
      }
    }
    return buf.toString ();
  }


//  /**
//   * Returns all files in a directory which match the specified pattern.
//   *
//   * @param dir File for the directory to use a root.
//   * @param pattern String regular expression used to match files.
//   *
//   * @return Array of file object for all matching files.
//   */
//  public static File[] listFiles ( File dir, String pattern )
//  {
//    GlobFilenameFilter filter = new GlobFilenameFilter ( pattern,
//                                                         GlobCompiler.CASE_INSENSITIVE_MASK );
//    return dir.listFiles ( ( FilenameFilter ) filter );
//  }


//  /**
//   * Returns all files in a directory which match the specified pattern.
//   *
//   * @param dir directory to use a root.
//   * @param pattern String regular expression used to match files.
//   *
//   * @return Array of file object for all matching files.
//   */
//  public static File[] listFiles ( String dir, String pattern )
//  {
//    return listFiles ( new File ( dir ), pattern );
//  }


  /**
   * Displays a 2 dimensional array of objects as a grid of string values.  Rows
   * will be delimited with tabs.
   *
   * @param cells Values to display.
   *
   * @return String showing the contents of the array.
   */
  public static String display2DArray ( Object[][] cells )
  {
    return display2DArray ( null, "\t", cells );
  }


  /**
   * Displays a 2 dimensional array of objects as a grid of string values.  Rows
   * will be delimited with tabs.
   *
   * @param header Header to give the list.
   * @param cells Values to display.
   *
   * @return String showing the contents of the array.
   */
  public static String display2DArray ( String header, Object[][] cells )
  {
    return display2DArray ( header, "\t", cells );
  }


  /**
   * Displays a 2 dimensional array of objects as a grid of string values.
   *
   * @param header Header to give the list.
   * @param delim String to put between values
   * @param cells Values to display.
   *
   * @return String showing the contents of the array.
   */
  public static String display2DArray ( String header,
                                        String delim,
                                        Object[][] cells )
  {
    final StringBuffer buf = new StringBuffer ();
    appendHeader ( buf, header );
    if ( null != cells )
    {
      for ( final Object[] element : cells )
      {
        for ( int j = 0; j < element.length; j++ )
        {
          buf.append ( element[j] ).append ( delim );
        }
        buf.append ( "\n" );
      }
    }
    buf.deleteCharAt ( buf.length () - 1 );
    return buf.toString ();
  }


  /**
   * Converts a Collection into a string.
   *
   * @param col Collection to traverse to create the string.
   *
   * @return String representation of the collection.
   */
  public static String displayCollection ( Collection<?> col )
  {
    return displayCollection ( col, DEF_FORMAT );
  }


  /**
   * Converts a Collection into a string.
   *
   * @param col Collection to traverse to create the string.
   * @param format String array containing the following:
   *  <li>List header string.  e.g. "{"</li>
   *  <li>Seperator string.  e.g. ", "</li>
   *  <li>List footer string.  e.g. "}"</li>
   *
   * @return String representation of the collection.
   */
  public static String displayCollection ( Collection<?> col, String[] format )
  {
    final Iterator<?> it = col.iterator ();
    final StringBuffer buf = new StringBuffer ();
    buf.append ( format[0] );
    while ( it.hasNext() )
    {
      buf.append ( it.next ().toString () );
      if ( it.hasNext () )
      {
        buf.append ( format[1] );
      }
    }
    buf.append ( format[2] );
    return buf.toString ();
  }


  /**
   * @param e Enumeration to display.
   *
   * @return String representation of the enumeration.
   */
  public static String displayEnumeration ( Enumeration<?> e )
  {
    final StringBuffer buf = new StringBuffer ();
    while ( e.hasMoreElements () )
    {
      buf.append ( e.nextElement () ).append ( "\n" );
    }
    return buf.toString ();
  }


  /**
   * Converts the contents of a Map into a string for display.
   *
   * @param map Map to display.
   *
   * @return String representation of the map.
   */
  public static String displayMap ( Map<?, ?> map )
  {
    return displayCollection ( map.entrySet () );
  }


  /**
   * @param date Date to get first day of year for.
   *
   * @return 1st day of the year of the specified date.
   */
  public static Date firstDayOfYear ( Date date )
  {
    final Calendar cal = new GregorianCalendar ();
    cal.setTime ( date );
    cal.set ( Calendar.DAY_OF_MONTH, 1 );
    cal.set ( Calendar.MONTH, Calendar.JANUARY );
    return cal.getTime ();
  }


  /**
   * Returns the class loader used to load the specified object.
   *
   * @param obj Object to find the class loader for.
   *
   * @return The classloader that loaded the object.
   */
  public static ClassLoader getClassLoader ( Object obj )
  {
    return obj.getClass ().getClassLoader ();
  }


  /**
   * Returns the extension of the given file.
   *
   * @param file File whose extension we wish to obtain.
   *
   * @return The string after the last occurrence of . or the empty string if
   * there is no extension.
   */
  public static String getExtension ( File file )
  {
    return getExtension ( file.getName () );
  }


  /**
   * Returns the extension of the given filename.
   *
   * @param filename Name of file whose extension we wish to obtain.
   *
   * @return The string after the last occurrence of . or the empty string if
   *         there is no extension.
   */
  public static String getExtension ( String filename )
  {
    return StringUtils.substringAfterLast ( filename, "." );
  }


  /**
   * @return The hostname of the local host.
   */
  public static String getHostname ()
  {
    try
    {
      return java.net.InetAddress.getLocalHost ().getHostName ();
    }
    catch ( final java.net.UnknownHostException e )
    {
      e.printStackTrace ();
      return "";
    }
  }


  /**
   * Calculates the next day.
   *
   * @param  date the data to be incremented
   *
   * @return Calendar the next day
   */
  public static java.util.Date getNextDay ( java.util.Date date )
  {
    if ( date == null )
    {
      return null;
    }
    return findAnyDay ( date, 1 ).getTime ();
  }


  /**
   * Calculates the next weekday.
   *
   * @param  date the data to be incremented
   *
   * @return Calendar the next weekday
   */
  public static java.util.Date getNextWeekDay ( java.util.Date date )
  {
    if ( date == null )
    {
      return null;
    }
    return findWeekDay ( date, 1 ).getTime ();
  }


  /**
   * Calculates the previous day.
   *
   * @param  date the data to be decrimented
   *
   * @return Calendar the next day
   */
  public static java.util.Date getPrevDay ( java.util.Date date )
  {
    if ( date == null )
    {
      return null;
    }
    return findAnyDay ( date, -1 ).getTime ();
  }


  /**
   * Calculates the previous weekday.
   *
   * @param  date the data to be decrimented
   *
   * @return Calendar the next weekday
   */
  public static java.util.Date getPrevWeekDay ( java.util.Date date )
  {
    if ( date == null )
    {
      return null;
    }
    return findWeekDay ( date, -1 ).getTime ();
  }


  /**
   * Return a File representing the relative path between two files.
   *
   * @param root Starting point.
   * @param target End point.
   *
   * @return File object representing the relative path.
   */
  public static File getRelativePath ( File root, File target )
  {
    return getRelativePath ( root, target, ".." );
  }


  /**
   * Return a File representing the relative path between two files.
   *
   * @param root Starting point.
   * @param target End point.
   * @param upDir String used to represent traversal up a directory.
   *
   * @return File object representing the relative path.
   */
  public static File getRelativePath ( File root, File target, String upDir )
  {
    final StringBuffer buf = new StringBuffer ( 100 );

    if ( root.isFile () )
    {
      root = root.getParentFile ();
    }

    while ( root != null )
    {
      if ( root.equals ( target ) || isParentOf ( root, target ) )
      {
        break;
      }

      buf.append ( upDir )
         .append ( File.separator );
      root = root.getParentFile ();
    }

    if ( root == null )
    {
      return null;
    }

    boolean bAddedPath = false;
    final int nInsertPos = buf.length ();

    while ( !target.equals ( root ) )
    {
      if ( bAddedPath )
      {
        buf.insert ( nInsertPos, File.separator );
      }

      buf.insert ( nInsertPos, target.getName () );
      bAddedPath = true;
      target = target.getParentFile ();
    }

    return new File ( buf.toString () );
  }


  /**
   * Return a File representing the relative path between two files.
   *
   * @param root Starting point.
   * @param target End point.
   *
   * @return File object representing the relative path.
   */
  public static String getRelativePath ( String root, String target )
  {
    return getRelativePath ( new File ( root ),
                             new File ( target ),
                             ".." ).getPath ();
  }


  /**
   * Gets an InputStream for the specified resoruce.
   *
   * @param loader Class loader to use to find the resource.
   * @param resourceName path of the resoruce.
   *
   * @return InputStream opened for the specified resoruce.
   */
  public static InputStream getResource ( ClassLoader loader,
                                          String resourceName )
  {
    final InputStream str = loader.getResourceAsStream ( resourceName );
    if ( null == str )
    {
      throw new MissingResourceException ( "Cannot locate resource.",
                                           loader.toString (),
                                           resourceName );
    }
    return str;
  }


  /**
   * Gets an InputStream for the specified resoruce.
   *
   * @param obj Object whose class loader will be used to locate the resource.
   * @param resourceName path of the resoruce.
   *
   * @return InputStream opened for the specified resoruce.
   */
  public static InputStream getResource ( Object obj, String resourceName )
  {
    return getResource ( getClassLoader ( obj ), resourceName );
  }


  /**
   * Gets a reader for the specified text resoruce.
   *
   * @param loader Class loader to use to find the resource.
   * @param resourceName path of the resoruce.
   *
   * @return Reader opened for the specified resoruce.
   */
  public static Reader getResourceReader ( ClassLoader loader,
                                           String resourceName )
  {
    return new InputStreamReader ( getResource ( loader, resourceName ) );
  }


  /**
   * Gets a reader for the specified text resoruce.
   *
   * @param obj Object whose class loader will be used to locate the resource.
   * @param resourceName path of the resoruce.
   *
   * @return Reader opened for the specified resoruce.
   */
  public static Reader getResourceReader ( Object obj, String resourceName )
  {
    final InputStream str = getResource ( obj, resourceName );
    return new InputStreamReader ( str );
  }

  /**
   * Determines if p is a parent of c.
   *
   * @param p The candidate parent class.
   * @param c The child file to test.
   *
   * @return True if p is a parent of c, false if not.
   */
  public static boolean isParentOf ( File p, File c )
  {
    File file = c.getParentFile ();

    while ( file != null )
    {
      if ( file.equals ( p ) )
      {
        return true;
      }

      file = file.getParentFile ();
    }

     return false;
  }


  /**
   * Checks if the date is a weekend
   *
   * @param cal the data to be checked
   *
   * @return boolean returns true if this is weekend or false if it is a weekday
   */
  public static boolean isWeekend ( Calendar cal )
  {
    return cal.get ( Calendar.DAY_OF_WEEK ) == Calendar.SATURDAY ||
           cal.get ( Calendar.DAY_OF_WEEK ) == Calendar.SUNDAY;
  }


  /**
   * Calculates the number of months between two dates.  It is only concerned
   * with the number of calendar months not the number of days.
   *
   * @param start Start date.
   * @param end End date.
   *
   * @return The number of months difference.  If start date is after end date
   *         then the months will be -ve.
   */
  public static int monthsBetween ( Date start, Date end )
  {
    return ( int ) ( ( end.getTime () - start.getTime () ) 
                     / ( 1000L * 60L * 60L * 24L * 30L ) );
//    return Math.round ( ( ( end.getTime () - start.getTime () ) 
//        / ( 1000L * 60L * 60L * 24L * 30L ) ) );
  }


  /**
   * Extracts the main entries from the specified JarFile and returns them in a
   * StringBuffer.
   *
   * @param jar JarFile containing the manifest from which to extract entries.
   *
   * @return StringBuffer containing the main manifest entries.
   *
   * @throws IOException
   */
  public static StringBuffer readMainManifest ( JarFile jar )
    throws
      IOException
  {
    return readMainManifest ( jar.getManifest () );
  }


  /**
   * Extracts the main entries from the specified manifest and returns them in a
   * StringBuffer.
   *
   * @param mf Manifest from which to extract the entries.
   *
   * @return StringBuffer containing the main manifest entries.
   */
  public static StringBuffer readMainManifest ( Manifest mf )
  {
    final Attributes attrs = mf.getMainAttributes ();
    final StringBuffer res = new StringBuffer ( 512 );
    extractAttributes ( res, attrs );
    return res;
  }


  /**
   * Extracts the main entries from the jar file whose path is supplied and
   * returns them in a StringBuffer.
   *
   * @param jar Path to the jar file containing the Manifest from which to
   *            extract the entries.
   *
   * @return StringBuffer containing the main manifest entries.
   *
   * @throws IOException
   */
  public static StringBuffer readMainManifest ( String jar )
    throws
      IOException
  {
    return readMainManifest ( new JarFile ( jar ) );
  }


  /**
   * Extracts all entries from the jar file whose path is supplied and returns
   * them in a StringBuffer.
   *
   * @param jar Path to the jar file containing the Manifest from which to
   *            extract the entries.
   *
   * @return StringBuffer containing the manifest entries.
   *
   * @throws IOException
   */
  public static StringBuffer readManifest ( String jar )
    throws
      IOException
  {
    final Manifest mf = new JarFile ( jar ).getManifest ();
    final StringBuffer res = readMainManifest ( mf );
    final Map<String, Attributes> map = mf.getEntries ();
    for ( final Map.Entry<String, Attributes> entry: map.entrySet () )
    {
      res.append ( "Name: " ).append ( entry.getKey () ).append ( "\n" );
      final Attributes attrs = entry.getValue ();
      extractAttributes ( res, attrs );
    }
    return res;
  }


  /**
   * @see #runOSCommand(String, String)
   *
   * @param command
   */
  public static void runOSCommand ( String command )
  {
    runOSCommand ( command, command );
  }


  /**
   * Method runOSCommand. Runs a command in the owning process's shell context
   *
   * @param command the command to be executed
   * @param displayCommand alternate version of command that will be displayed,
   *                       can be used to suppress the display of sensitive info
   *                       such as passwords
   *
   * @return StringBuffer containing all output generated by the command.
   */
  public static StringBuffer runOSCommand ( String command, String displayCommand )
  {
    final StringBuffer sbLogInfo = new StringBuffer ( 256 );
    try
    {
      final Runtime rt = Runtime.getRuntime ();
      final Process proc = rt.exec ( command );
      sbLogInfo.append ( "\n<IN> " ).append ( displayCommand ).append ( "\n" );

      //Declaring Stream for errors
      final InputStreamReader isr = new InputStreamReader ( proc.getErrorStream () );
      final BufferedReader br = new BufferedReader ( isr );
      try
      {
        String line = null;
        while ( ( line = br.readLine () ) != null )
        {
          sbLogInfo.append ( "<ERR> " ).append ( line ).append ( "\n" );
        }
      }
      finally
      {
        br.close ();
      }

      //Declaring Stream to capture output from the command execution
      final InputStreamReader isr1 = new InputStreamReader ( proc.getInputStream () );
      final BufferedReader br1 = new BufferedReader ( isr1 );
      try
      {
        String line1 = null;
        while ( ( line1 = br1.readLine () ) != null )
        {
          sbLogInfo.append ( "<OUT> " ).append ( line1 ).append ( "\n" );
        }
      }
      finally
      {
        br1.close ();
      }

      try
      {
        final int exitVal = proc.waitFor ();
        //If Exit Value for this command is 0 it means that the command was
        // executed successfully.
        sbLogInfo.append ( "ExitValue for command: " + exitVal );
      }
      catch ( final InterruptedException ei )
      {
        Logger.error ( "runOsCommand: Exception in proc.waitFor()", ei );
      }
    }//end of try block
    catch ( final IOException eo )
    {
      Logger.error ( "runOsCommand: Exception in main code - stacktrace follows", eo );
    }
    return sbLogInfo;
  }


  /**
  * Method snapShot.
  *
  * Method to support messages including timing the duration of operations
  *
  * @param baseMsecs - the original milliseconds from which the elapsed time should be calculated
  * @param message - the text part of the logged timing message
  * @return long - returns the current milliseconds to allow a new base to be set in the calling method
  */
  public static long snapShot ( long baseMsecs, String message )
  {
   return snapShot ( baseMsecs, message, 0 );
  }


 /**
* Method to support messages including timing the duration of operations
*
* @param start the original milliseconds from which the elapsed time
*                  should be calculated.
* @param msg the text part of the logged timing message.
* @param rowsAffected if supplied > 0, will also output a 'rows per second'
*                     message element
* @return the current milliseconds to allow a new base to be set in the
*         calling method
*/
public static long snapShot ( long start, String msg, int rowsAffected )
{
 final long msecsNow = System.currentTimeMillis ();
 final long thisSnapshot = msecsNow - start;
 double dRowsPerSec = 0;
final double dThisSnapshot = thisSnapshot;
 dRowsPerSec =  thisSnapshot == 0 ? 0 : rowsAffected / dThisSnapshot * 1000;
 final NumberFormat nf = NumberFormat.getNumberInstance ();
 if ( nf instanceof DecimalFormat )
 {
   ( ( DecimalFormat ) nf ).applyPattern ("#,##0.0" );
 }
 Logger.debug ( msg + ": " + nf.format ( dThisSnapshot / 1000 ) + " secs" +
                ( dRowsPerSec > 0 ? ", rows per sec " + nf.format ( dRowsPerSec ) : "" ) );
 return msecsNow;
}


  /**
   * Returns the sum of the integers held in an int array.
   *
   * @param counts int[] to sum.
   *
   * @return Sum of all values held in the array.
   */
  public static int sumIntArray ( int[] counts )
  {
    int result = 0;
    for ( final int element : counts )
    {
      result += element;
    }
    return result;
  }


  /**
   * @param url URL from which to retrieve data.
   * @param dest Stream to write the data to.
   * 
   * @throws IOException 
   */
  public static void getHttpFile ( final URL url, final OutputStream dest )
    throws 
      IOException
  {
    final BufferedInputStream bufIn = new BufferedInputStream ( url.openStream () );
    try
    {
      final byte[] buf = new byte[4096];
      for ( ;; )
      {
        final int len = bufIn.read ( buf );
        if ( len == -1 )
        {
          return;
        }
        dest.write ( buf, 0, len );
      }
    }
    finally
    {
      bufIn.close ();
    }
  }

  
  /**
   * Saves the uncompressed data from the ZipInputStream to a file.
   *
   * @param zin Zipped input stream.
   * @param s Path to save the uncompressed data to.
   *
   * @throws IOException
   */
  public static void unzip ( ZipInputStream zin, String s )
    throws
      IOException
  {
    final int BUF_SIZE = 1024;
    Logger.debug ( "Unzipping " + s );
    final BufferedOutputStream out = new BufferedOutputStream ( new FileOutputStream ( s ),
                                                          BUF_SIZE );
    final byte[] buf = new byte[BUF_SIZE];
    int len = 0;
    while ( ( len = zin.read ( buf ) ) != -1 )
    {
      out.write ( buf, 0, len );
    }
    out.close ();
  }


  /**
   * Extract all files from the specified zip file.
   *
   * @param outDir Directory into which to unzip files.
   * @param filename Full path of the zip file to extract.
   *
   * @throws IOException
   */
  public static void unzipFile ( String outDir, String filename )
    throws
      IOException
  {
    unzipFile ( outDir, filename, new ArrayList<String> ( 0 ) );
  }


  /**
   * Extracts the files specified in the list from a zip file.
   *
   * @param outDir Directory into which to unzip files.
   * @param filename Full path to the zip file.
   * @param files List of filenames to extract from the zip.
   *
   * @throws IOException
   */
  public static void unzipFile ( String outDir, String filename, List<String> files )
    throws
      IOException
  {
    Logger.debug ( "unzipFile: " + filename );
    final ZipInputStream zin = new ZipInputStream ( new BufferedInputStream ( new FileInputStream ( filename ) ) );
    ZipEntry e;
    while ( ( e = zin.getNextEntry () ) != null )
    {
      if ( !files.isEmpty () )
      {
        if ( !files.contains ( e.getName () ) )
        {
          continue;
        }
      }
      unzip ( zin, concatPaths ( outDir, e.getName () ) );
    }
    zin.close ();
  }


  /**
     * Append the specified string to the buffer and underline it.
     *
     * @param buf Buffer to add string to.
     * @param header String to add to buffer.
     */
    private static void appendHeader ( StringBuffer buf, String header )
    {
      if ( null != header && !header.trim ().equals ( "" ) )
      {
        buf.append ( header ).append ( "\n" );
        buf.append ( StringUtils.repeat ( "=", header.length () ) ).append ( "\n" );
      }
    }


  /**
   * @param res
   * @param attrs
   */
  private static void extractAttributes ( StringBuffer res, Attributes attrs )
  {
    for ( final Object object : attrs.keySet () )
    {
      final Attributes.Name attrName = ( Attributes.Name ) object;
      res.append ( attrName )
         .append ( ": " )
         .append ( attrs.getValue ( attrName ) )
         .append ( "\n" );
    }
  }


  /**
   * Calculates the next day by adding the value of the increment parameter
   * to the date argument.
   *
   * @param date Date to find the next week day for.
   * @param increment 1 to go forward in time, -1 to go backwards.
   *
   * @return Calendar representing the next week day.
   */
  private static Calendar findAnyDay ( java.util.Date date, int increment )
  {
    final GregorianCalendar cal = new GregorianCalendar ();
    cal.setTime ( date );
    cal.add ( Calendar.DATE, increment );

    return cal;
  }


  /**
   * Calculates the next weekday by adding the value of the increment parameter
   * to the date argument until a weekday is calculated.
   *
   * @param date Date to find the next week day for.
   * @param increment 1 to go forward in time, -1 to go backwards.
   *
   * @return Calendar representing the next week day.
   */
  private static Calendar findWeekDay ( java.util.Date date, int increment )
  {
    final GregorianCalendar cal = new GregorianCalendar ();
    cal.setTime ( date );
    do
    {
      cal.add ( Calendar.DATE, increment );
    }
    while ( isWeekend ( cal ) );

    return cal;
  }


  private Util ()
  {
    super ();
  }


  private static final String[] DEF_FORMAT = {"{", ", ", "}"};


///**
//* Makes up for the lack of file copying utilities in Java
//*
//* @param from
//* @param to
//*
//* @return
//*/
//public static boolean copy ( File from, File to )
//{
// BufferedInputStream fin = null;
// BufferedOutputStream fout = null;
// try
// {
//   int bufSize = 8 * 1024;
//   fin = new BufferedInputStream ( new FileInputStream ( from ), bufSize );
//   fout = new BufferedOutputStream ( new FileOutputStream ( to ), bufSize );
//   copyPipe ( fin, fout, bufSize );
// }
// catch ( IOException ioex )
// {
//   return false;
// }
// catch ( SecurityException sx )
// {
//   return false;
// }
// finally
// {
//   if ( fin != null )
//   {
//     try
//     {
//       fin.close ();
//     }
//     catch ( IOException cioex )
//     {
//       Logger.warn ( cioex );
//     }
//   }
//   if ( fout != null )
//   {
//     try
//     {
//       fout.close ();
//     }
//     catch ( IOException cioex )
//     {
//       Logger.warn ( cioex );
//     }
//   }
// }
// return true;
//}
//
//
///**
//* Save URL contents to a file
//*
//* @param from
//* @param to
//*
//* @return
//*/
//public static boolean copy(URL from, File to)
//{
// BufferedInputStream urlin = null;
// BufferedOutputStream fout = null;
// try
// {
//   int bufSize = 8 * 1024;
//   urlin =
//     new BufferedInputStream(
//       from.openConnection().getInputStream(),
//       bufSize);
//   fout = new BufferedOutputStream(new FileOutputStream(to), bufSize);
//   copyPipe(urlin, fout, bufSize);
// }
// catch (IOException ioex)
// {
//   return false;
// }
// catch (SecurityException sx)
// {
//   return false;
// }
// finally
// {
//   if (urlin != null)
//   {
//     try
//     {
//       urlin.close();
//     }
//     catch (IOException cioex)
//     {
//       Logger.warn ( cioex );
//     }
//   }
//   if (fout != null)
//   {
//     try
//     {
//       fout.close();
//     }
//     catch (IOException cioex)
//     {
//       Logger.warn ( cioex );
//     }
//   }
// }
// return true;
//}


///**
//* @param target
//* @param substs
//*/
//public static void substitute ( Hashtable target, Hashtable substs )
//{
// for ( Enumeration ti = target.keys (); ti.hasMoreElements (); )
// {
//   Object key = ti.nextElement ();
//   Object value = target.get ( key );
//   Object replacement = substs.get ( value );
//   if ( replacement != null )
//   {
//     target.put ( key, replacement );
//   }
// }
//}


///**
//* Get mapping for key, inserting and returning a default object if necessary.
//*
//* @param map
//* @param key
//* @param valueClass
//*
//* @return
//*/
//public static Object getOrInsert ( Map map, Object key, Class valueClass )
//{
// Object value = map.get ( key );
// if ( value == null )
//  {
//   try
//   {
//     value = valueClass.newInstance ();
//   }
//   catch (InstantiationException e)
//   {
//     throw new RuntimeException ( e );
//   }
//   catch (IllegalAccessException e)
//   {
//     throw new RuntimeException ( e );
//   }
//   map.put ( key, value );
// }
// return value;
//}
//
//
//  private static void copyPipe ( InputStream in,
//                                 OutputStream out,
//                                 int bufSizeHint )
//    throws
//      IOException
//  {
//    int read = -1;
//    byte[] buf = new byte[bufSizeHint];
//    while ( ( read = in.read ( buf, 0, bufSizeHint ) ) >= 0 )
//    {
//      out.write ( buf, 0, read );
//    }
//    out.flush ();
//  }

}