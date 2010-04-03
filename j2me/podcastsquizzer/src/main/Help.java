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
    private static final int ROWS_OF_KEYS=4;
    private static int stage=0;
    
    
    private static String generalKeys = "*GENERAL *KEYS" + "\n" +
         "/* . RETURN (returns to the previous screen)"+ "\n" +
         "/# . CHANGE MODE (switches the #mode )"+ "\n" +
         "/LEFT . REWIND (audio rewind action)"+ "\n" +
         "/MAIN . PLAY/STOP ( audio )"+ "\n" +
         "/RIGHT . FORWARD (audio forwarding action)"+ "\n" +
         "/UP .    UP SCREEN"+ "\n" +
         "/DOWN .  DOWN SCREEN"+ "\n" +
         " "+ "\n";
    private static String tuplesKeys = "*TUPLES *MODE (useful to have a vocabulary's quiz)"+ "\n" +
         "/4 . PREVIOUS TUPLE (shows the previous tuple of the quiz)"+ "\n" +
         "/5 . REVEAL (shows the following part of the tuple, depending on the 'tuple mode')"+ "\n" +
         "/6 . NEXT TUPLE"+ "\n" +
         "/8 . CHANGE TUPLE MODE (changes the 'tuple mode', so the order in which the parts of the tuple are shown will change)"+ "\n" +
         " "+ "\n";
    private static String marksKeys = "*MARKS *MODE (useful to manage marks/comments of pieces of the current podcast)"+ "\n" +
         "/4 . ADD MARK (adds a new empty mark in the current time, or replaces the existing one)"+ "\n" +
         "/5 . COMMENT MARK (adds a comment to the current mark)"+ "\n" +
         "/6 . SAVE ALL MARKS (save all the marks into a file)"+ "\n" +
         "/7 . MOVE BACK (shows the previous mark)"+ "\n" +
         "/8 . ADD THIS MARK HERE (changes the time of the current mark)"+ "\n" +
         " "+ "\n" ;

    private static String animatedKeys = "*ANIMATED *MODE (useful to reproduce the listening and see the marks together)"+ "\n" +
         "/7 . MOVE BACK (shows the previous mark)"+ "\n" +
         "/8 . CHANGE TUPLE MODE (changes the 'tuple mode', so the order in which the parts of the tuple are shown will change)"+ "\n" +
         "/9 . MOVE FORWARD (shows the following mark)"+ "\n";

    public static String getIntructionsHelp(){
        return generalKeys + tuplesKeys + marksKeys + animatedKeys;
    }
    
    public static String getKeysMeaningNext(int mode){
        String ret = "";
        stage = (stage + 1) % ROWS_OF_KEYS;
        
        switch (mode){
            case 0:
                switch (stage){
                    case 0:
                        ret = "1.PREV 2.PREF 3.PREV"; break;
                    case 1:
                        ret = "4.PREV 5.PREF 6.PREV"; break;
                    case 2:
                        ret = "7.PREV 8.PREF 9.PREV"; break;
                    case 3:
                        ret = "(*).PREV 0.PREF (#).PREV"; break;
                    default:
                        ret = "<help>"; break;
                }
                
                break;
            case 1:
                switch (stage){
                    case 0:
                        ret = "1.PREV 2.PREF 3.PREV"; break;
                    case 1:
                        ret = "4.PREV 5.PREF 6.PREV"; break;
                    case 2:
                        ret = "7.PREV 8.PREF 9.PREV"; break;
                    case 3:
                        ret = "(*).PREV 0.PREF (#).PREV"; break;
                    default:
                        ret = "<help>"; break;
                }
                break;
            default:
                switch (stage){
                    case 0:
                        ret = "1.PREV 2.PREF 3.PREV"; break;
                    case 1:
                        ret = "4.PREV 5.PREF 6.PREV"; break;
                    case 2:
                        ret = "7.PREV 8.PREF 9.PREV"; break;
                    case 3:
                        ret = "(*).PREV 0.PREF (#).PREV"; break;
                    default:
                        ret = "<help>"; break;
                }
                break;
        }
        return ret;
    }
}
