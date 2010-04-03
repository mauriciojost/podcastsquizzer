
package canvaspackage;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;



public class Word{
    
    public static final char NON_SPECIAL_CHAR='A';
    public static final int NORMAL=0;
    public static final int BOLD=1;
    public static final int BOLD_BLUE=2;
    public static final int NORMAL_BLUE=3;
    public static final int NORMAL_RED=4;
    public static final int NORMAL_GREEN=5;
    
    private static Font baseFont = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
    private static Font boldMediumFont = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
    public static int baseColor = 0xFFFFFF;
    
    private char last_char;
    private String word;
    private int mode = NORMAL;
    
    public static void setBaseFont(Font font){
        baseFont = font;
        boldMediumFont = Font.getFont(baseFont.getFace(), Font.STYLE_BOLD, Font.SIZE_MEDIUM);
    }
    
    public Word(String w){
        char lc=NON_SPECIAL_CHAR;
        if (w.length()>0) {
            lc = (char)w.charAt(w.length()-1);
        }
        
        if ((lc==' ') || (lc =='\n')) {
            processModeAndWord(w.substring(0, w.length()-1));
            last_char = lc;
        } else {
            processModeAndWord(w.substring(0, w.length()));
            last_char = NON_SPECIAL_CHAR;
        }

    }
    
    public int getWidth(){
        Font font;
        font = this.getFont();
        return font.stringWidth(word + " ");
    }
    
    
    private void processModeAndWord(String w){
        try{
            if (w.charAt(0)=='*') {
                mode = BOLD_BLUE;
                word = w.substring(1,w.length());
            }else if (w.charAt(0)=='#') {
                mode = NORMAL_BLUE;
                word = w.substring(1,w.length());
            }else if (w.charAt(0)=='_') {
                mode = NORMAL_RED;
                word = w.substring(1,w.length());
            }else if (w.charAt(0)=='/') {
                mode = NORMAL_GREEN;
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
    
    
    
    public char getLastChar(){
        return this.last_char;
    }

    
    public void paintWord(Graphics g, int x, int y){
        //g.setFont(this.getFont());
        switch(mode){
            case BOLD_BLUE:
                g.setFont(boldMediumFont);    
                g.setColor(0x8888FF);    
                g.drawString(word, x, y, Graphics.TOP|Graphics.LEFT);
                g.setColor(baseColor);    
                g.setFont(baseFont);    
                break;
            case NORMAL_BLUE:
                g.setColor(0x8888FF);
                g.drawString(word, x, y, Graphics.TOP|Graphics.LEFT);
                g.setColor(baseColor);    
                g.setFont(baseFont);    
                break;
            case NORMAL_RED:
                g.setColor(0xFF8888);
                g.drawString(word, x, y, Graphics.TOP|Graphics.LEFT);
                g.setColor(baseColor);    
                g.setFont(baseFont);    
                break;
            case NORMAL_GREEN:
                g.setColor(0x88FF88);
                g.drawString(word, x, y, Graphics.TOP|Graphics.LEFT);
                g.setColor(baseColor);    
                g.setFont(baseFont);    
                break;
            default:
                g.drawString(word, x, y, Graphics.TOP|Graphics.LEFT);       
                break;
        }    
    }
    
    public Font getFont(){
        if (mode==Word.BOLD_BLUE) {
            return Word.boldMediumFont;         /* Only different font. */
        } else {
            return Word.baseFont;               /* Default font. */
        }
    }
    
}