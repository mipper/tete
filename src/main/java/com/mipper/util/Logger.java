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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.slf4j.LoggerFactory;


/**
 * Utility methods for logging.  Uses the commons logging library.
 *
 * @version $Id$
 * @author Cliff Evans
 */
public class Logger
{

//  /**
//   * @param msg
//   */
//  public static void fatal ( Object msg )
//  {
//    _defaultLogger.fatal ( msg );
//  }
//
//
//  /**
//   * Log a message with fatal level.
//   *
//   * @param msg Object to log.
//   */
//  public static void fatal ( Throwable msg )
//  {
//    _defaultLogger.fatal ( getMessage ( msg, true ) );
//  }
//
//
//  /**
//   * @param msg
//   * @param obj
//   */
//  public static void fatal ( String msg, Throwable obj )
//  {
//    _defaultLogger.fatal ( msg + CRTAB + getMessage ( obj, true ) );
//  }
//
//
//  /**
//   * Log a message with fatal level using params as parameters to MessageFormat.
//   *
//   * @param msg Object to log.
//   * @param params Parameters passed to MessageFormat.format.
//   */
//  public static void fatal ( String msg, Object... params )
//  {
//    _defaultLogger.fatal ( MessageFormat.format ( msg, params ) );
//  }
//
//
//  /**
//   * Log an fatal level message against a specified org.slf4j.Logger.
//   *
//   * @param logger The logger name to log the message against.
//   * @param msg The exception to log.
//   */
//  public static void fatalEx ( String logger, Throwable msg )
//  {
//    LoggerFactory.getLogger ( logger ).fatal ( getMessage ( msg, true ) );
//  }
//
//
//  /**
//   * Log an fatal level message against a specified org.slf4j.Logger.
//   *
//   * @param logger The logger name to log the message against.
//   * @param msg The message to log
//   */
//  public static void fatalEx ( String logger, Object msg )
//  {
//    LoggerFactory.getLogger ( logger ).fatal ( msg );
//  }
//
//
//  /**
//   * Log an fatal level message against a specified org.slf4j.Logger using a parameterised
//   * message string.
//   *
//   * @param logger The logger name to log the message against.
//   * @param msg The message to log
//   * @param params Params passed into MessageFormat.format call.
//   */
//  public static void fatalEx ( String logger, String msg, Object... params )
//  {
//    LoggerFactory.getLogger ( logger ).fatal ( MessageFormat.format ( msg, params ) );
//  }
//
//
//  /**
//   * Log an fatal level message against a specified org.slf4j.Logger using a parameterised
//   * message string.
//   *
//   * @param logger The logger name to log the message against.
//   * @param e Throwable to report along with log entry.
//   * @param msg The message to log
//   * @param params Params passed into MessageFormat.format call.
//   */
//  public static void fatalEx ( String logger, Throwable e, String msg, Object... params )
//  {
//    LoggerFactory.getLogger ( logger ).fatal ( MessageFormat.format ( msg, params ), e );
//  }


  /**
   * @param msg
   */
  public static void error ( final String msg )
  {
    _defaultLogger.error ( msg );
  }


  /**
   * Log a message with error level.
   *
   * @param msg Object to log.
   */
  public static void error ( final Throwable msg )
  {
    _defaultLogger.error ( getMessage ( msg, true ) );
  }


  /**
   * @param obj
   * @param msg
   */
  public static void error ( final Throwable obj, final String msg )
  {
    _defaultLogger.error ( msg + CRTAB + getMessage ( obj, true ) );
  }


  /**
   * Log a message with error level using params as parameters to MessageFormat.
   *
   * @param msg Object to log.
   * @param params Parameters passed to MessageFormat.format.
   */
  public static void error ( final String msg, final Object... params )
  {
    _defaultLogger.error ( MessageFormat.format ( msg, params ) );
  }


  /**
   * Log a message with error level using params as parameters to MessageFormat.
   *
   * @param e Throwable causing the log message.
   * @param msg Object to log.
   * @param params Parameters passed to MessageFormat.format.
   */
  public static void error ( final Throwable e, final String msg, final Object... params )
  {
    _defaultLogger.error ( MessageFormat.format ( msg, params ), e );
  }


  /**
   * Log an error level message against a specified org.slf4j.Logger.
   *
   * @param logger The logger name to log the message against.
   * @param msg The exception to log.
   */
  public static void errorEx ( final String logger, final Throwable msg )
  {
    LoggerFactory.getLogger ( logger ).error ( getMessage ( msg, true ) );
  }


  /**
   * Log an error level message against a specified org.slf4j.Logger.
   *
   * @param logger The logger name to log the message against.
   * @param msg The message to log
   */
  public static void errorEx ( final String logger, final String msg )
  {
    LoggerFactory.getLogger ( logger ).error ( msg );
  }


  /**
   * Log an error level message against a specified org.slf4j.Logger using a parameterised
   * message string.
   *
   * @param logger The logger name to log the message against.
   * @param msg The message to log
   * @param params Params passed into MessageFormat.format call.
   */
  public static void errorEx ( final String logger, final String msg, final Object... params )
  {
    LoggerFactory.getLogger ( logger ).error ( MessageFormat.format ( msg, params ) );
  }


  /**
   * Log an error level message against a specified org.slf4j.Logger using a parameterised
   * message string.
   *
   * @param logger The logger name to log the message against.
   * @param e Throwable to report along with log entry.
   * @param msg The message to log
   * @param params Params passed into MessageFormat.format call.
   */
  public static void errorEx ( final String logger, final Throwable e, final String msg, final Object... params )
  {
    LoggerFactory.getLogger ( logger ).error ( MessageFormat.format ( msg, params ), e );
  }


  /**
   * Log a message with warn level.
   *
   * @param msg Object to log.
   */
  public static void warn ( final String msg )
  {
    _defaultLogger.warn ( msg );
  }


  /**
   * Log a message with warn level.
   *
   * @param msg Object to log.
   */
  public static void warn ( final Throwable msg )
  {
    if ( _defaultLogger.isWarnEnabled () )
    {
      _defaultLogger.warn ( getMessage ( msg, true ) );
    }
  }


  /**
   * @param obj
   * @param msg
   */
  public static void warn ( final Throwable obj, final String msg )
  {
    if ( _defaultLogger.isWarnEnabled () )
    {
      _defaultLogger.warn ( msg + CRTAB + getMessage ( obj, true ) );
    }
  }


  /**
   * Log a parameterised message with warn level.
   *
   * @param msg Message to log.
   * @param params Parameters which will be processed via MessageFormat.format
   *                against the msg string.
   */
  public static void warn ( final String msg, final Object... params )
  {
    if ( _defaultLogger.isWarnEnabled () )
    {
      _defaultLogger.warn ( MessageFormat.format ( msg, params ) );
    }
  }


  /**
   * Log a message with warm level using params as parameters to MessageFormat.
   *
   * @param e Throwable causing the log message.
   * @param msg Object to log.
   * @param params Parameters passed to MessageFormat.format.
   */
  public static void warn ( final Throwable e, final String msg, final Object... params )
  {
    if ( _defaultLogger.isWarnEnabled () )
    {
      _defaultLogger.warn ( MessageFormat.format ( msg, params ), e );
    }
  }


  /**
   * Log a warn level message against a specified org.slf4j.Logger.
   *
   * @param logger The logger name to log the message against.
   * @param msg The message to log
   */
  public static void warnEx ( final String logger, final String msg )
  {
    LoggerFactory.getLogger ( logger ).warn ( msg );
  }


  /**
   * Log a warn level message against a specified org.slf4j.Logger.
   *
   * @param logger The logger name to log the message against.
   * @param msg The exception to log.
   */
  public static void warnEx ( final String logger, final Throwable msg )
  {
    if ( _defaultLogger.isWarnEnabled () )
    {
      LoggerFactory.getLogger ( logger ).warn ( getMessage ( msg, true ) );
    }
  }


  /**
   * Log a warn level message against a specified org.slf4j.Logger using a parameterised
   * message string.
   *
   * @param logger The logger name to log the message against.
   * @param msg The message to log
   * @param params Params passed into MessageFormat.format call.
   */
  public static void warnEx ( final String logger, final String msg, final Object... params )
  {
    if ( _defaultLogger.isWarnEnabled () )
    {
      LoggerFactory.getLogger ( logger ).warn ( MessageFormat.format ( msg, params ) );
    }
  }


  /**
   * Log a warn level message against a specified org.slf4j.Logger using a parameterised
   * message string.
   *
   * @param logger The logger name to log the message against.
   * @param e Throwable to report along with log entry.
   * @param msg The message to log
   * @param params Params passed into MessageFormat.format call.
   */
  public static void warnEx ( final String logger, final Throwable e, final String msg, final Object... params )
  {
    if ( _defaultLogger.isWarnEnabled () )
    {
      LoggerFactory.getLogger ( logger ).warn ( MessageFormat.format ( msg, params ), e );
    }
  }


  /**
   * @param msg
   */
  public static void info ( final String msg )
  {
    _defaultLogger.info ( msg );
  }


  /**
   * Log a message with info level.
   *
   * @param msg Object to log.
   */
  public static void info ( final Throwable msg )
  {
    if ( _defaultLogger.isInfoEnabled () )
    {
      _defaultLogger.info ( getMessage ( msg, true ) );
    }
  }


  /**
   * @param obj
   * @param msg
   */
  public static void info ( final Throwable obj, final String msg )
  {
    if ( _defaultLogger.isInfoEnabled () )
    {
      _defaultLogger.info ( msg + CRTAB + getMessage ( obj, true ) );
    }
  }


  /**
   * Log a parameterised message with info level.
   *
   * @param msg Message to log.
   * @param params Parameters which will be processed via MessageFormat.format
   *                against the msg string.
   */
  public static void info ( final String msg, final Object... params )
  {
    if ( _defaultLogger.isInfoEnabled () )
    {
      _defaultLogger.info ( MessageFormat.format ( msg, params ) );
    }
  }


  /**
   * Log an info level message against a specified Logger using a parameterised
   * message string.
   *
   * @param e Throwable to report along with log entry.
   * @param msg The message to log
   * @param params Params passed into MessageFormat.format call.
   */
  public static void info ( final Throwable e, final String msg, final Object... params )
  {
    if ( _defaultLogger.isInfoEnabled () )
    {
      _defaultLogger.info ( MessageFormat.format ( msg, params ), e );
    }
  }


  /**
   * Log an info level message against a specified org.slf4j.Logger.
   *
   * @param logger The logger name to log the message against.
   * @param msg The message to log
   */
  public static void infoEx ( final String logger, final String msg )
  {
    LoggerFactory.getLogger ( logger ).info ( msg );
  }


  /**
   * Log an info level message against a specified org.slf4j.Logger.
   *
   * @param logger The logger name to log the message against.
   * @param msg The exception to log.
   */
  public static void infoEx ( final String logger, final Throwable msg )
  {
    final org.slf4j.Logger l = LoggerFactory.getLogger ( logger );
    if ( l.isInfoEnabled () )
    {
      l.info ( getMessage ( msg, true ) );
    }
  }


  /**
   * Log an info level message against a specified org.slf4j.Logger using a parameterised
   * message string.
   *
   * @param logger The logger name to log the message against.
   * @param msg The message to log
   * @param params Params passed into MessageFormat.format call.
   */
  public static void infoEx ( final String logger, final String msg, final Object... params )
  {
    final org.slf4j.Logger l = LoggerFactory.getLogger ( logger );
    if ( l.isInfoEnabled () )
    {
      l.info ( MessageFormat.format ( msg, params ) );
    }
  }


  /**
   * Log an info level message against a specified org.slf4j.Logger using a parameterised
   * message string.
   *
   * @param logger The logger name to log the message against.
   * @param e Throwable to report along with log entry.
   * @param msg The message to log
   * @param params Params passed into MessageFormat.format call.
   */
  public static void infoEx ( final String logger, final Throwable e, final String msg, final Object... params )
  {
    final org.slf4j.Logger l = LoggerFactory.getLogger ( logger );
    if ( l.isInfoEnabled () )
    {
      l.info ( MessageFormat.format ( msg, params ), e );
    }
  }


  /**
   * @param msg
   */
  public static void debug ( final String msg )
  {
    _defaultLogger.debug ( msg );
  }


  /**
   * Log a message with debug level.
   *
   * @param msg Object to log.
   */
  public static void debug ( final Throwable msg )
  {
    if ( _defaultLogger.isDebugEnabled () )
    {
      _defaultLogger.debug ( getMessage ( msg, true ) );
    }
  }


  /**
   * @param obj
   * @param msg
   */
  public static void debug ( final Throwable obj, final String msg )
  {
    if ( _defaultLogger.isDebugEnabled () )
    {
      _defaultLogger.debug ( msg + CRTAB + getMessage ( obj, true ) );
    }
  }


  /**
   * Log a parameterised message with debug level.
   *
   * @param msg Message to log.
   * @param params Parameters which will be processed via MessageFormat.format
   *                against the msg string.
   */
  public static void debug ( final String msg, final Object... params )
  {
    if ( _defaultLogger.isDebugEnabled () )
    {
      _defaultLogger.debug ( MessageFormat.format ( msg, params ) );
    }
  }


  /**
   * Log a message with error level using params as parameters to MessageFormat.
   *
   * @param e Throwable causing the log message.
   * @param msg Object to log.
   * @param params Parameters passed to MessageFormat.format.
   */
  public static void debug ( final Throwable e, final String msg, final Object... params )
  {
    if ( _defaultLogger.isDebugEnabled () )
    {
      _defaultLogger.debug ( MessageFormat.format ( msg, params ), e );
    }
  }


  /**
   * Log a debug level message against a specified org.slf4j.Logger.
   *
   * @param logger The logger name to log the message against.
   * @param msg The message to log
   */
  public static void debugEx ( final String logger, final String msg )
  {
    final org.slf4j.Logger l = LoggerFactory.getLogger ( logger );
    if ( l.isDebugEnabled () )
    {
      l.debug ( msg );
    }
  }


  /**
   * Log a debug level message against a specified org.slf4j.Logger using a parameterised
   * message string.
   *
   * @param logger The logger name to log the message against.
   * @param e Exception to output.
   */
  public static void debugEx ( final String logger, final Throwable e )
  {
    final org.slf4j.Logger l = LoggerFactory.getLogger ( logger );
    if ( l.isDebugEnabled () )
    {
      l.debug ( getMessage ( e, true ) );
    }
  }


  /**
   * Log a debug level message against a specified org.slf4j.Logger using a parameterised
   * message string.
   *
   * @param logger The logger name to log the message against.
   * @param msg The message to log
   * @param params Params passed into MessageFormat.format call.
   */
  public static void debugEx ( final String logger, final String msg, final Object... params )
  {
    final org.slf4j.Logger l = LoggerFactory.getLogger ( logger );
    if ( l.isDebugEnabled () )
    {
      l.debug ( MessageFormat.format ( msg, params ) );
    }
  }


  /**
   * Log a debug level message against a specified org.slf4j.Logger using a parameterised
   * message string.
   *
   * @param logger The logger name to log the message against.
   * @param e Throwable to report along with log entry.
   * @param msg The message to log
   * @param params Params passed into MessageFormat.format call.
   */
  public static void debugEx ( final String logger, final Throwable e, final String msg, final Object... params )
  {
    final org.slf4j.Logger l = LoggerFactory.getLogger ( logger );
    if ( l.isDebugEnabled () )
    {
      l.debug ( MessageFormat.format ( msg, params ), e );
    }
  }


//  /**
//   * Log a message with trace level.
//   *
//   * @param msg String to log.
//   */
//  public static void trace ( String msg )
//  {
//    if ( isTraceEnabled ( _defaultLogger ) && _defaultLogger.isDebugEnabled () )
//    {
//      _defaultLogger.trace ( msg );
//    }
//  }
//
//
//  /**
//   * Log a message with trace level.
//   *
//   * @param msg Object to log.
//   */
//  public static void trace ( Throwable msg )
//  {
//    if ( isTraceEnabled ( _defaultLogger ) && _defaultLogger.isDebugEnabled () )
//    {
//      _defaultLogger.trace ( getMessage ( msg, true ) );
//    }
//  }
//
//
//  /**
//   * @param obj
//   * @param msg
//   */
//  public static void trace ( Throwable obj, String msg )
//  {
//    if ( isTraceEnabled ( _defaultLogger ) && _defaultLogger.isDebugEnabled () )
//    {
//      _defaultLogger.trace ( msg + CRTAB + getMessage ( obj, true ) );
//    }
//  }
//
//
//  /**
//   * Log a parameterised message with trace level.
//   *
//   * @param msg Message to log.
//   * @param params Parameters which will be processed via MessageFormat.format
//   *                against the msg string.
//   */
//  public static void trace ( String msg, Object... params )
//  {
//    if ( isTraceEnabled ( _defaultLogger ) && _defaultLogger.isDebugEnabled () )
//    {
//      _defaultLogger.trace ( MessageFormat.format ( msg, params ) );
//    }
//  }
//
//
//  /**
//   * Log a trace level message against a specified org.slf4j.Logger using a parameterised
//   * message string.
//   *
//   * @param e Throwable to report along with log entry.
//   * @param msg The message to log
//   * @param params Params passed into MessageFormat.format call.
//   */
//  public static void trace ( Throwable e, String msg, Object... params )
//  {
//    if ( isTraceEnabled ( _defaultLogger ) && _defaultLogger.isDebugEnabled () )
//    {
//      _defaultLogger.trace ( MessageFormat.format ( msg, params ), e );
//    }
//  }
//
//
//  /**
//   * Log a trace level message against a specified org.slf4j.Logger.
//   *
//   * @param logger The logger name to log the message against.
//   * @param msg The exception to log.
//   */
//  public static void traceEx ( String logger, Throwable msg )
//  {
//    org.slf4j.Logger l = LoggerFactory.getLogger ( logger );
//    if ( isTraceEnabled ( l ) && l.isDebugEnabled () )
//    {
//      l.trace ( getMessage ( msg, true ) );
//    }
//  }
//
//
//  /**
//   * Log a trace level message against a specified org.slf4j.Logger.
//   *
//   * @param logger The logger name to log the message against.
//   * @param msg The message to log
//   */
//  public static void traceEx ( String logger, String msg )
//  {
//    org.slf4j.Logger l = LoggerFactory.getLogger ( logger );
//    if ( isTraceEnabled ( l ) && l.isDebugEnabled () )
//    {
//      l.trace ( msg );
//    }
//  }
//
//
//  /**
//   * Log a trace level message against a specified org.slf4j.Logger using a parameterised
//   * message string.
//   *
//   * @param logger The logger name to log the message against.
//   * @param msg The message to log
//   * @param params Params passed into MessageFormat.format call.
//   */
//  public static void traceEx ( String logger, String msg, Object... params )
//  {
//    org.slf4j.Logger l = LoggerFactory.getLogger ( logger );
//    if ( isTraceEnabled ( l ) && l.isDebugEnabled () )
//    {
//      l.trace ( MessageFormat.format ( msg, params ) );
//    }
//  }
//
//
//  /**
//   * Log a trace level message against a specified org.slf4j.Logger using a parameterised
//   * message string.
//   *
//   * @param logger The logger name to log the message against.
//   * @param e Throwable to report along with log entry.
//   * @param msg The message to log
//   * @param params Params passed into MessageFormat.format call.
//   */
//  public static void traceEx ( String logger, Throwable e, String msg, Object... params )
//  {
//    org.slf4j.Logger l = LoggerFactory.getLogger ( logger );
//    if ( isTraceEnabled ( l ) && l.isDebugEnabled () )
//    {
//      l.trace ( MessageFormat.format ( msg, params ), e );
//    }
//  }


  /**
   * Output a message to the "output" org.slf4j.Logger.  Useful for temporary logging
   * during development.  The level for this org.slf4j.Logger can have its output level
   * set independently of the others.
   *
   * @param msg String to log.
   */
  public static void dump ( final String msg )
  {
    final org.slf4j.Logger l = LoggerFactory.getLogger ( OUTPUT_LOGGER_NAME );
    l.debug ( msg );
  }


  /**
   * Output a message to the "output" org.slf4j.Logger.  Useful for temporary logging
   * during development.  The level for this org.slf4j.Logger can have its output level
   * set independently of the others.
   *
   * @param msg Object to log.
   */
  public static void dump ( final Throwable msg )
  {
    final org.slf4j.Logger l = LoggerFactory.getLogger ( OUTPUT_LOGGER_NAME );
    if ( l.isDebugEnabled () )
    {
      l.debug ( getMessage ( msg, true ) );
    }
  }


  /**
   * @param obj
   * @param msg
   */
  public static void dump ( final Throwable obj, final String msg )
  {
    final org.slf4j.Logger l = LoggerFactory.getLogger ( OUTPUT_LOGGER_NAME );
    if ( l.isDebugEnabled () )
    {
      l.debug ( msg + CRTAB + getMessage ( obj, true ) );
    }
  }


  /**
   * Log a parameterised message with trace level.
   *
   * @param msg Message to log.
   * @param params Parameters which will be processed via MessageFormat.format
   *                against the msg string.
   */
  public static void dump ( final String msg, final Object... params )
  {
    final org.slf4j.Logger l = LoggerFactory.getLogger ( OUTPUT_LOGGER_NAME );
    if ( l.isDebugEnabled () )
    {
      l.debug ( MessageFormat.format ( msg, params ) );
    }
  }


  /**
   * @param logger
   * @param msg
   */
  public static void dumpEx ( final String logger, final Throwable msg )
  {
    final org.slf4j.Logger l = LoggerFactory.getLogger ( OUTPUT_LOGGER_NAME + "." + logger );
    if ( l.isDebugEnabled () )
    {
      l.debug ( getMessage ( msg, true ) );
    }
  }


  /**
   * @param logger
   * @param msg
   */
  public static void dumpEx ( final String logger, final String msg )
  {
    final org.slf4j.Logger l = LoggerFactory.getLogger ( OUTPUT_LOGGER_NAME + "." + logger );
    if ( l.isDebugEnabled () )
    {
      l.debug ( msg );
    }
  }


  /**
   * Log a trace level message against a specified org.slf4j.Logger using a parameterised
   * message string.
   *
   * @param logger The logger name to log the message against.
   * @param msg The message to log
   * @param params Params passed into MessageFormat.format call.
   */
  public static void dumpEx ( final String logger, final String msg, final Object... params )
  {
    final org.slf4j.Logger l = LoggerFactory.getLogger ( OUTPUT_LOGGER_NAME + "." + logger );
    if ( l.isDebugEnabled () )
    {
      l.debug ( MessageFormat.format ( msg, params ) );
    }
  }


  /**
   * Log a trace level message against a specified org.slf4j.Logger using a parameterised
   * message string.
   *
   * @param logger The logger name to log the message against.
   * @param e Throwable to report along with log entry.
   * @param msg The message to log
   * @param params Params passed into MessageFormat.format call.
   */
  public static void dumpEx ( final String logger, final Throwable e, final String msg, final Object... params )
  {
    final org.slf4j.Logger l = LoggerFactory.getLogger ( OUTPUT_LOGGER_NAME + "." + logger );
    if ( l.isDebugEnabled () )
    {
      l.debug ( MessageFormat.format ( msg, params ), e );
    }
  }


  /**
   * @return The default org.slf4j.Logger.
   */
  public static org.slf4j.Logger getDefaultLogger ()
  {
    return _defaultLogger;
  }


  /**
   * @param name
   *
   * @return Log instance with the given name.
   */
  public static org.slf4j.Logger setDefaultLoggerName ( final String name )
  {
    return LoggerFactory.getLogger ( name );
  }


  /**
   * Returns a string of the stack trace for an exception.
   *
   * @param e Exception to get stackTrace for.
   *
   * @return The stack trace.
   */
  public static String getStackTrace ( final Throwable e )
  {
    final StringWriter s = new StringWriter ( 1024 );
    final PrintWriter w = new PrintWriter ( s );
    e.printStackTrace ( w );

    return s.toString ();
  }


//  private static boolean isTraceEnabled ( org.slf4j.Logger logger )
//  {
//    return _localTrace && logger.isTraceEnabled ();
//  }


//  private static String getMessage ( Throwable msg )
//  {
//    return getMessage ( msg, false );
//  }


  private static String getMessage ( final Throwable msg, final boolean stackTrace )
  {
    String result = getStackTrace ( msg );
    if ( stackTrace )
    {
      result = StringUtils.replace ( result,
                                     SystemUtils.LINE_SEPARATOR,
                                     SystemUtils.LINE_SEPARATOR + "\t" );
    }
    return result;
  }


  private Logger ()
  {
    super ();
  }


  private static final String DEFAULT_LOGGER_NAME = "log.std";
  private static final String OUTPUT_LOGGER_NAME = "log.output";
  private static final String CRTAB = SystemUtils.LINE_SEPARATOR + "\t";
//  private static LoggerFactory _factory = LoggerFactory.getFactory ();
  private static org.slf4j.Logger _defaultLogger = setDefaultLoggerName ( DEFAULT_LOGGER_NAME );
  // To set this pass -Dlog.trace=true to the VM when running the app
//  private static boolean _localTrace = Boolean.getBoolean ( "log.trace" );

}