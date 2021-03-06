package canvaspackage;

/**
 * This class should not be used by the user.
 * This was made in order to provide elements to the main classes of 
 * this package.
 */
public class Key2Text {
    public static int BACKSPACE_KEY = -3;
    private String text = "";
    private String textPendant = "";
    
    private int lastKey;
    private long lastPress; 
    private long timeTolerance = 500;
    private int pressNumber = 0;
    
    public String newKey(int keyCode){
        
        String ret;
        
        if (keyCode == BACKSPACE_KEY) { /* Removes a character. */
            text = text + textPendant;
            textPendant = "";
            try{
                text = text.substring(0, text.length()-1);
            }catch(Exception e){
                text = "";
            }
            return text; 
        }
        
        if ((keyCode == lastKey) &&(closeTimes(lastPress, System.currentTimeMillis())))  {
            /* Se repite la tecla y en tiempo de cambio. */
            try{
                textPendant = "" + (char)(KeysTranslator.key2Char(keyCode,pressNumber++));
                ret = text + textPendant;
            }catch(Exception e){
                textPendant = "";
                ret = text;
            }
            
        } else {
            /* Se aprieta otra tecla, o la misma pero tarde. */
            pressNumber=0;
            text = text + textPendant;
            try{
                textPendant = "" + (char)(KeysTranslator.key2Char(keyCode,pressNumber++));
                ret = text + textPendant;
            }catch(Exception e){
                textPendant = "";
                ret = text;
            }
        }
        
        lastKey = keyCode; 
        lastPress = System.currentTimeMillis();
        
        return ret;
    }
    
    private boolean closeTimes(long first, long last){
        if ((first + timeTolerance) > last) {
            return true;
        } else {
            return false;
        }
    }
    
}
