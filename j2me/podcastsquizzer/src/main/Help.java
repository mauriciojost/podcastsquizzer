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
    
    

    public static String getIntructionsHelp(){
        return generalKeys;
    }
    
}
