This software is Copyright (c) 2005-2008 Cliff Evans.
All rights reserved.

This is the second version of the Tete ear trainer.  I haven't had time to put
together a lot of documentation, but you can find a tutorial on the website
http://tete.sourceforge.net/tutorial.shtml.  The web site is also the place to
look for more up to date information than found here.  The site will be updated
regardless of wether there's a release or not.  I hope over time to rectify this
lack of documentation by adding some online help but pressures of work and
trying to having a life etc. may mean this takes some time to achieve.

Tete allows you to select intervals, chords, or scales and play examples of the
selected sounds in a random order.  As each is played it is highlighted in the
list.  Once you think you can tell what is playing you can test yourself.  A
series of sounds will be played, again at random, and you attempt to identify
what has been played.

You can control how each sound is played back by changing the midi instrument,
the octave range allowable, root note, and whether the sounds are played
ascending or descending.  You can also adjust the length of the notes and the
extent to arpeggio the playback.

The program is written in Java and need the Java Runtime Environment from Sun's
website.  It should run on any PC running Microsoft Windows or Linux and should
also run on Macs.

Installation requirements
=========================

Tete requires that Java version 1.5 or higher is available.  The Java Runtime
Environment (JRE) is available for download from Sun:
http://java.sun.com/j2se/1.5.0/download.jsp.

The latest release uses the OpenJDK synthesizer implementation so no further
downloads are required. If you want to use Sun's original synthesizer
implementation, you will also require the MIDI part of the JRE to be available. 
If you have this it will be found in the audio directory of your installed JRE:
{JRE Directory}\lib\audio. If you don't have it, it can be freely downloaded
from Sun's website http://java.sun.com/products/java-media/sound/soundbanks.html

Installation
============

Installation is simply a matter of unziping the distribution file into a
directory of your choice. On Windows you might use C:\Program Files\Tete
assuming your installation of Windows is on your C: drive.

Running Tete
============

If you have associated jar files with the java runtime you will be able to
double click on the tete.jar file in the directory you unzipped the Tete files
into.

Alternatively you can right click on the jar file and select Open with... and
select the java.exe from the JRE 1.5 bin directory.

If none of the above works, you can use the command line: java -jar tete.jar 
