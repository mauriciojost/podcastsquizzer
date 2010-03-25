/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package canvaspackage;


/**
 * This class should not be used by the user.
 * This was made in order to provide elements to the main classes of 
 * this package.
 * @author Mauricio
 */
public class KeysTranslator {
    public static char key2Char(int tecla, int veces){
        switch (tecla){
            case '0': 
                return ' ';
            case '1':
                
                veces = veces % 3;
                switch(veces) {
                    case 0: return '-';
                    case 1: return '?';
                    case 2: return '\'';
                }
                
            case '2':
                veces = veces % 3;
                switch(veces) {
                    case 0: return 'a';
                    case 1: return 'b';
                    case 2: return 'c';
                }
                
            case '3':
                veces = veces % 3;
                switch(veces) {
                    case 0: return 'd';
                    case 1: return 'e';
                    case 2: return 'f';
                }

            case '4':
                veces = veces % 3;
                switch(veces) {
                    case 0: return 'g';
                    case 1: return 'h';
                    case 2: return 'i';
                }

            case '5':
                veces = veces % 3;
                switch(veces) {
                    case 0: return 'j';
                    case 1: return 'k';
                    case 2: return 'l';
                }

            case '6':
                veces = veces % 3;
                switch(veces) {
                    case 0: return 'm';
                    case 1: return 'n';
                    case 2: return 'o';
                }

            case '7':
                veces = veces % 4;
                switch(veces) {
                    case 0: return 'p';
                    case 1: return 'q';
                    case 2: return 'r';
                    case 3: return 's';
                }

            case '8':
                veces = veces % 3;
                switch(veces) {
                    case 0: return 't';
                    case 1: return 'u';
                    case 2: return 'v';
                }

            case '9':
                veces = veces % 4;
                switch(veces) {
                    case 0: return 'w';
                    case 1: return 'x';
                    case 2: return 'y';
                    case 3: return 'z';
                }

            default: 
                return ' ';
        }
    }

}
