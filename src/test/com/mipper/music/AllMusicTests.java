/*
 * This file contains proprietary information of Rule Financial. 
 * Copying or reproduction without prior written approval is prohibited. 
 *
 * <b>Copyright</b> (c) 2004
 * <b>Company</b> Rule Financial
 */
package test.com.mipper.music;

import junit.framework.Test;
import junit.framework.TestSuite;
import test.com.mipper.music.model.TestIntervalPattern;
import test.com.mipper.music.model.TestNoteRange;


/**
 * Runs all unit tests from the com.mipper.music package.
 * 
 * @author Cliff Evans
 * @version $Revision: 1.1 $
 */
public class AllMusicTests
{

  /**
   * @return Suite comprising all of the unit tests in the com.mipper.music package.
   */
  public static Test suite ()
  {
    TestSuite suite = new TestSuite ( "Test for test.com.mipper.music.model" );
    //$JUnit-BEGIN$
    suite.addTestSuite ( TestIntervalPattern.class );
    suite.addTestSuite ( TestNoteRange.class );
    //$JUnit-END$
    return suite;
  }
}
