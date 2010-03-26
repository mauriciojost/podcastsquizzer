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
     "_*. RETURN (returns to the previous screen)"+ "\n" +
     "_#. CHANGE MODE (switches the #mode )"+ "\n" +
     "_1. REWIND (audio rewind action)"+ "\n" +
     "_2. PLAY/STOP ( /audio )"+ "\n" +
     "_3. FORWARD (audio forwarding action)"+ "\n" +
     "_LEFT.  UP PAGE (move the current screen's view)"+ "\n" +
     "_RIGHT. DOWN PAGE"+ "\n" +
     "_UP.    UP LINE"+ "\n" +
     "_DOWN.  DOWN LINE"+ "\n" +
     " "+ "\n" +
     "*TUPLES *MODE (useful to have a vocabulary's quiz)"+ "\n" +
     "_4. PREVIOUS TUPLE (shows the previous tuple of the quiz)"+ "\n" +
     "_5. REVEAL (shows the following part of the tuple, depending on the 'tuple mode')"+ "\n" +
     "_6. NEXT TUPLE"+ "\n" +
     "_8. CHANGE TUPLE MODE (changes the 'tuple mode', so the order in which the parts of the tuple are shown will change)"+ "\n" +
     " "+ "\n" +
     "*MARKS *MODE (useful to manage marks/comments of pieces of the current podcast)"+ "\n" +
     "_4. ADD MARK (adds a new empty mark in the current time, or replaces the existing one)"+ "\n" +
     "_5. COMMENT MARK (adds a comment to the current mark)"+ "\n" +
     "_6. SAVE ALL MARKS (save all the marks into a file)"+ "\n" +
     "_7. MOVE BACK (shows the previous mark)"+ "\n" +
     "_8. ADD THIS MARK HERE (changes the time of the current mark)"+ "\n" +
     "_9. MOVE FORWARD (shows the following mark)"+ "\n" +
     " "+ "\n" +
     "*ANIMATED *MODE (useful to reproduce the listening and see the marks together)"+ "\n" +
     "_8. CHANGE TUPLE MODE (changes the 'tuple mode', so the order in which the parts of the tuple are shown will change)"+ "\n"
     ;
      
        
    }
}
