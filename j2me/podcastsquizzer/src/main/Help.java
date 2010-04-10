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
    private static final int ROWS_OF_KEYS=5;
    private static int stage=0;
    
    
    private static String generalKeys = "*GENERAL *KEYS" + "\n" +
         "/MISCLEFT . RETURN (returns to the previous screen)"+ "\n" +
         "/MISCRIGHT . CHANGE MODE (switches the #mode )"+ "\n" +
         "/LEFT . REWIND (audio rewind action)"+ "\n" +
         "/MAIN . PLAY/STOP ( audio )"+ "\n" +
         "/RIGHT . FORWARD (audio forwarding action)"+ "\n" +
         "/UP .    UP SCREEN"+ "\n" +
         "/DOWN .  DOWN SCREEN"+ "\n" +
         " "+ "\n";
    private static String tuplesKeys = "*TUPLES *MODE (useful to have a vocabulary's quiz)"+ "\n" +
         "/7 . PREVIOUS TUPLE (shows the previous tuple of the quiz)"+ "\n" +
         "/8 . REVEAL (shows the following part of the tuple, depending on the 'tuple mode')"+ "\n" +
         "/9 . NEXT TUPLE"+ "\n" +
         "/0 . CHANGE TUPLE MODE (changes the 'tuple mode', so the order in which the parts of the tuple are shown will change)"+ "\n" +
         " "+ "\n";
    private static String marksKeys = "*MARKS *MODE (useful to manage marks/comments of pieces of the current podcast)"+ "\n" +
         
         "/7 . MOVE BACK (shows the previous mark)"+ "\n" +
         "/8 . ADD THIS MARK HERE (changes the time of the current mark)"+ "\n" +
         "/9 . MOVE FORWARD (shows the following mark)"+ "\n" +
         " "+ "\n" ;

    private static String animatedKeys = "*ANIMATED *MODE (useful to reproduce the listening and see the marks together)"+ "\n" +
         "/4 . EDIT TRANSCRIPT (edit the transcript of the current mark)"+ "\n" +
         "/5 . EDIT COMMENT (edit the comment of the current mark)"+ "\n" +
         "/6 . ADD MARK (adds a new empty mark in the current time, or replaces the existing one)"+ "\n" +   
         
         "/7 . MOVE BACK (shows the previous mark)"+ "\n" +
         "/8 . APPLY THIS MARK HERE (changes the time of the current mark)"+ "\n" +
         "/9 . MOVE FORWARD (shows the following mark)"+ "\n" +
         
         "/0 . SAVE ALL MARKS (save all the marks into a file)"+ "\n";
    

    public static String getIntructionsHelp(){
        return generalKeys + tuplesKeys + marksKeys + animatedKeys;
    }
    
    public static String getKeysMeaningNext(int mode){
        String ret = "";
        stage = (stage + 1) % ROWS_OF_KEYS;
        
        switch (mode){
            case PlayerForm.MODE_GLOSSARY:
                switch (stage){
                    case 0:
                        ret = "(1)   (2)   (3)"; break;
                    case 1:
                        ret = "(4)   (5)   (6)"; break;
                    case 2:
                        ret = "(7).PREV (8).REVE (9).NEXT"; break;
                    case 3:
                        ret = "(*)  (0).SWIT  (#)"; break;
                    case 4:
                        ret = "(ARR).AUDIO (BT).MODE"; break;
                    default:
                        ret = "<help>"; break;
                }
                break;
            
            case PlayerForm.MODE_LISTENING:
                switch (stage){
                    case 0:
                        ret = "(1)   (2)   (3)"; break;
                    case 1:
                        ret = "(4).EDTR (5).EDCM (6).NEW"; break;
                    case 2:
                        ret = "(7).PREV (8).APPL (9).NEXT"; break;
                    case 3:
                        ret = "(*)  (0).SAVE  (#)"; break;
                    case 4:
                        ret = "(ARR).AUDIO (BT).MODE"; break;
                    default:
                        ret = "<help>"; break;
                }
                break;
            default:
                ret = "";
                break;
        }
        return ret;
    }
}
