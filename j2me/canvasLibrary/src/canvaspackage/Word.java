
package canvaspackage;

import javax.microedition.lcdui.Font;



public class Word{
    
    public static final char NON_SPECIAL_CHAR='A';
    public static final int NORMAL=0;
    public static final int BOLD=1;
    
    private static Font baseFont = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
    private char last_char;
    private String word;
    private int mode;
    
    public static void setBaseFont(Font font){
        baseFont = font;
    }
    
    public Word(String w){
        char lc='A';
        if (w.length()>0) {
            lc = (char)w.charAt(w.length()-1);
        }
        
        if (lc==' ') {
            processWord(w.substring(0, w.length()-1));
            last_char = lc;
        } else if (lc =='\n') {
            processWord(w.substring(0, w.length()-1));
            last_char = lc;
        } else {
            processWord(w.substring(0, w.length()));
            last_char = NON_SPECIAL_CHAR;
        }

    }
    
    public int getWidth(){
        Font font;
        font = this.getFont();
        return font.stringWidth(word + " ");
    }
    
    public Font getFont(){
        if (mode==Word.BOLD) {
            //return Font.getFont(baseFont.getFace(), Font.STYLE_BOLD, baseFont.getSize());
            return Font.getFont(baseFont.getFace(), Font.STYLE_BOLD, Font.SIZE_MEDIUM);
        } else {
            return baseFont;
        }
    }
    
    private void processWord(String w){
        try{
            if (w.charAt(0)=='*') {
                mode = BOLD;
                word = w.substring(1,w.length());
            } else {
                mode = NORMAL;
                word = w.substring(0,w.length());
            }
        }catch(Exception e){
            mode = NORMAL;
            word = w.substring(0,w.length());
        }
    }
    
    public String getWord(){
        return word;
    }
    
    public int getMode(){
        return mode;
    }
    
    public char getLastChar(){
        return this.last_char;
    }
    
}