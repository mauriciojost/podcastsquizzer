/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

/**
 *
 * @author Mauricio
 */
public class Help {
    public static String getIntructionsHelp(){
        return 
     "*GENERAL *KEYS" + "\n" +
     "/* . RETURN (returns to the previous screen)"+ "\n" +
     "/# . CHANGE MODE (switches the #mode )"+ "\n" +
     "/LEFT . REWIND (audio rewind action)"+ "\n" +
     "/MAIN . PLAY/STOP ( audio )"+ "\n" +
     "/RIGHT . FORWARD (audio forwarding action)"+ "\n" +
     "/UP .    UP SCREEN"+ "\n" +
     "/DOWN .  DOWN SCREEN"+ "\n" +
     " "+ "\n" +
     "*TUPLES *MODE (useful to have a vocabulary's quiz)"+ "\n" +
     "/4 . PREVIOUS TUPLE (shows the previous tuple of the quiz)"+ "\n" +
     "/5 . REVEAL (shows the following part of the tuple, depending on the 'tuple mode')"+ "\n" +
     "/6 . NEXT TUPLE"+ "\n" +
     "/8 . CHANGE TUPLE MODE (changes the 'tuple mode', so the order in which the parts of the tuple are shown will change)"+ "\n" +
     " "+ "\n" +
     "*MARKS *MODE (useful to manage marks/comments of pieces of the current podcast)"+ "\n" +
     "/4 . ADD MARK (adds a new empty mark in the current time, or replaces the existing one)"+ "\n" +
     "/5 . COMMENT MARK (adds a comment to the current mark)"+ "\n" +
     "/6 . SAVE ALL MARKS (save all the marks into a file)"+ "\n" +
     "/7 . MOVE BACK (shows the previous mark)"+ "\n" +
     "/8 . ADD THIS MARK HERE (changes the time of the current mark)"+ "\n" +
     
     " "+ "\n" +
     "*ANIMATED *MODE (useful to reproduce the listening and see the marks together)"+ "\n" +
     
     "/7 . MOVE BACK (shows the previous mark)"+ "\n" +
     "/8 . CHANGE TUPLE MODE (changes the 'tuple mode', so the order in which the parts of the tuple are shown will change)"+ "\n" +
     "/9 . MOVE FORWARD (shows the following mark)"+ "\n"
     ;
      
        
    }
}
