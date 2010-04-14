package canvaspackage;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public class Word{
    
    public static final char NON_SPECIAL_CHAR='A';
    public static final char NORMAL=0;
    public static final char BOLD_BLUE='*';
    public static final char NORMAL_BLUE='#';
    public static final char NORMAL_RED='_';
    public static final char NORMAL_GREEN='/';
    
    private static Font baseFont = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
    private static Font boldMediumFont = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
    private static int baseColor = 0xFFFFFF;
    
    private char last_char;
    private String word;
    private char mode = NORMAL;
    
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
            if (w.charAt(0)==BOLD_BLUE) {
                mode = BOLD_BLUE;
                word = w.substring(1,w.length());
            }else if (w.charAt(0)==NORMAL_BLUE) {
                mode = NORMAL_BLUE;
                word = w.substring(1,w.length());
            }else if (w.charAt(0)==NORMAL_RED) {
                mode = NORMAL_RED;
                word = w.substring(1,w.length());
            }else if (w.charAt(0)==NORMAL_GREEN) {
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
                checkAndSetDefaultColorAndFont(g);
                break;
            case NORMAL_BLUE:
                checkAndSetDefaultColorAndFont(g);
                g.setColor(0x8888FF);
                g.drawString(word, x, y, Graphics.TOP|Graphics.LEFT);
                checkAndSetDefaultColorAndFont(g);
                break;
            case NORMAL_RED:
                checkAndSetDefaultColorAndFont(g);
                g.setColor(0xFF8888);
                g.drawString(word, x, y, Graphics.TOP|Graphics.LEFT);
                checkAndSetDefaultColorAndFont(g);
                break;
            case NORMAL_GREEN:
                checkAndSetDefaultColorAndFont(g);
                g.setColor(0x88FF88);
                g.drawString(word, x, y, Graphics.TOP|Graphics.LEFT);
                checkAndSetDefaultColorAndFont(g);
                break;
            default:
                checkAndSetDefaultColorAndFont(g);
                g.drawString(word, x, y, Graphics.TOP|Graphics.LEFT);       
                break;
        }    
    }
    
    private void checkAndSetDefaultColorAndFont(Graphics g){
        if (g.getFont()!=baseFont){g.setFont(baseFont);}
        if (g.getColor()!=baseColor){g.setColor(baseColor);}
    }
    
    public Font getFont(){
        if (mode==Word.BOLD_BLUE) {
            return Word.boldMediumFont;         /* Only different font. */
        } else {
            return Word.baseFont;               /* Default font. */
        }
    }
    
    
}