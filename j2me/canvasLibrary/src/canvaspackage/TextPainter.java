package canvaspackage;

import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/**
 * This class provides the user with the ability to write a text in a graphic, 
 * but using the text wrap feature.
 * @author Mauricio
 */

public class TextPainter {

    private Rectangle bounds;   /* Bounds of the painting area. */
    private Font font;          /* Font used to paint the text. */
    private int baseLine = 0;   /* Line that's going to be printed first. */
    
    public TextPainter(Font font, Rectangle bounds){
        this.font = font;
        this.bounds = bounds;
        Word.setBaseFont(font);
    }
    
    public void setTranslation(int yline){
        this.baseLine = yline;
    }
    
    public void paintTextComplex(Graphics g, String cText){
        int row = 0, aux;
        //int currentColor;
        Vector words, lines, line;
        words = breakDownWords(cText);
        cText = null;
        
        lines = breakDownLines(words, bounds.getWidth());
        words = null;
        
        Enumeration iterator = lines.elements();
        
        /*currentColor = g.getColor();
        g.setColor(0x333333);
        g.fillRect(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeigth());
        g.setColor(0x111111);
        g.drawRect(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeigth());
        g.setColor(currentColor);*/
        
        int line_number = 0;
        
        while(iterator.hasMoreElements()){
            line = (Vector)iterator.nextElement();
            if (line_number>=this.baseLine) {
                aux = paintLine(g,line,row);
                row += aux+2;
            }
            line_number++;
        }
        
       
    }
    
    public Vector breakDownLines(Vector words, int max_width){
        int width_counter=0, width_max;
        Word word;
        Vector lines = new Vector();
        Vector line = new Vector();
        
        lines.addElement(line);
        width_max = bounds.getWidth();
        
        words.trimToSize();
        Enumeration enumer = words.elements();
        
        while(enumer.hasMoreElements()){    
            
            word = (Word)enumer.nextElement();
            width_counter = width_counter + word.getWidth();
            line.addElement(word);
            
            if (width_counter>width_max){
                line.removeElement(word);
                line.trimToSize();
                
                line = new Vector();    
                lines.addElement(line);
                line.addElement(word);
                width_counter = word.getWidth();
            }
            if(word.getLastChar()=='\n'){
                line.trimToSize();
                line = new Vector();
                lines.addElement(line);
                width_counter = 0;
            }
        }
        line.trimToSize();
        lines.trimToSize();
        return lines;
    }
    
    /** Breaks down the text given. The breaking down process is done by using 
     * as separator both ' ' and '\n' characters. 
     * Each word returned is in
     */
    public Vector breakDownWords(String text){
        char ch;
        Vector words_vector = new Vector();
        int begin=0;
        for (int i=0;i<text.length();i++){
            ch = text.charAt(i);
            if ((ch==' ') || (ch=='\n')){
                words_vector.addElement(new Word(text.substring(begin, Math.min(i+1,text.length()))));
                begin = i+1;
            }
        }
        words_vector.addElement(new Word(text.substring(begin, text.length())));
        words_vector.trimToSize();
        return words_vector;
    }
    
    public int paintLine(Graphics g, Vector line, int row){
        int basex = 0, max_height=0;
        Word word;
        
        Enumeration iterator = line.elements();
        while (iterator.hasMoreElements()){
            word = (Word)iterator.nextElement();
            g.setFont(word.getFont());
            max_height = Math.max(max_height, word.getFont().getHeight());
            
            g.drawString(word.getWord(), bounds.getX()+basex, bounds.getY() + row, Graphics.TOP|Graphics.LEFT);
            basex += word.getWidth();
        }
        return max_height;
    }
}

