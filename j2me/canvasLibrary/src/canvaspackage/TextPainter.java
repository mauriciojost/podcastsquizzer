package canvaspackage;

import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 * This class provides the user with the ability to write a text in a graphic, 
 * but using the text wrap feature.
 */
public class TextPainter {

    private Rectangle bounds;   /* Bounds of the painting area. */
    private Font smallestFont = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
    private Font font;          /* Font used to paint the text. */
    private int baseLine = 0;   /* Line that's going to be printed first. */
    private int backgroundColor = 0x101010;
    private int borderColor = 0x7777FF;
    private int fontColor = 0xFFFFFF;
    private int linesPerPage=0;
    private Vector lines;
    private boolean repaintRequired = true;
            
    public Rectangle getBounds(){
        return bounds;
    }
    
    public TextPainter(Font font, Rectangle bounds){
        this.font = font;
        this.bounds = bounds;
        Word.setBaseFont(font);
        linesPerPage = bounds.getHeigth()/smallestFont.getHeight();
        this.setText(" \n ");
    }
    
    public void setBackgroundColor(int color){
        this.backgroundColor = color;
        repaintRequired = true;
    }
    
    public void setBorderColor(int color){
        this.borderColor = color;
        repaintRequired = true;
    }
    public void setFontColor(int color){
        this.fontColor = color;
        repaintRequired = true;
    }
    
    public void setTranslation(int yline){
        if (yline!=baseLine){
            this.baseLine = yline;
            repaintRequired = true;
        }
    }
    
    private void paintBackground(Graphics g){
        g.setColor(backgroundColor);
        g.fillRect(bounds.getX(), bounds.getY(), bounds.getWidth()/*-1*/, bounds.getHeigth()/*-1*/);
    }
    private void paintBorder(Graphics g){
        g.setColor(borderColor);
        g.drawRect(bounds.getX(), bounds.getY(), bounds.getWidth()/*-1*/, bounds.getHeigth()/*-1*/);
    }
    
    public void paintText(Graphics g, String text, int color){
        paintBackground(g);
        g.setFont(this.font);
        g.setColor(color);
        g.drawString(text, bounds.getX(), bounds.getY(), Graphics.TOP|Graphics.LEFT);
        paintBorder(g);
    }
    
    public void paintText(Graphics g, String text){
        this.paintText(g, text, this.fontColor);
    }
    
    public void setText(String cText){
        Vector words;
        words = breakDownWords(cText); cText = null;
        lines = breakDownLines(words, bounds.getWidth()); words = null;
        repaintRequired = true;
        
    }

    public boolean repaintIsRequired(boolean avoidable_painting){
        return ((repaintRequired == true)||(avoidable_painting==false));
    }


    public boolean paintTextComplex(Graphics g, boolean avoidable_painting, Image background){
        int row, aux;
        int currentColor;
        Vector line;
        Enumeration iterator;
        boolean repainted=false;
        
        if (repaintRequired == true || avoidable_painting==false){ /* If it's required because of a change, or, it's not-avoidable. */
            row = 0;
            
            iterator = lines.elements();

            currentColor = g.getColor();

            paintBackground(g);
            if (background!=null){
                g.drawImage(background, bounds.getX(), bounds.getY(), 0);
            }
            int line_number = 0, lines_painted=0;

            while(iterator.hasMoreElements()){
                line = (Vector)iterator.nextElement();

                if (lines_painted>=this.linesPerPage){
                    break;
                }

                if (line_number>=this.baseLine) {
                    aux = paintLine(g,line,row);
                    row += aux+2;
                    lines_painted++;
                }
                line_number++;
            }
            
            paintBorder(g);
            g.setColor(currentColor);
            repainted = true;
        }
        repaintRequired = false;
        return repainted;
    }
    
    public Vector breakDownLines(Vector words, int max_width){
        int width_counter=0, width_max;
        Word word;
        lines = new Vector();
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
            if ((ch==' ') || (ch=='\n') || (ch=='\t')){
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
            max_height = Math.max(max_height, word.getFont().getHeight());
            word.paintWord(g, bounds.getX()+basex, bounds.getY()+row);
            
            basex += word.getWidth();
        }
        
        return max_height;
    }
    
    
}

