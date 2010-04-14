package canvaspackage;
//import javax.microedition.lcdui.*;

public class MCanvas /*extends Canvas */{
/*    private String mainText = "<>";
    private String commentText = "";
    private Font font = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
    private Displayable previousDisplayable;
    private Display display;
    private TextPainter textPainter;
    private Key2Text key2Text; 
    
    public MCanvas (Display display, Displayable previous) {
        this.display = display;
        this.previousDisplayable = previous;
        
        int border = 10;
        Rectangle bounds = new Rectangle(border,border,this.getWidth()-(border*2), this.getHeight()-(border*2));
        this.textPainter = new TextPainter(font, bounds);
        this.key2Text = new Key2Text();
    }
    
    public void setMainText(String text){
        this.mainText = text;
    }
    
    public void setCommentText(String text){
        this.commentText = text;
    }
    
    public void paint(Graphics g) {
        g.setFont(font);
        
        g.setColor(0x00FFFF);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(0x000000);
        textPainter.paintTextComplex(g, this.mainText);        
    }
    
    protected void keyPressed(int keyCode) {
        if (keyCode=='1') {
            display.setCurrent(previousDisplayable);
        } else {
            this.mainText = this.key2Text.newKey(keyCode);
            this.repaint();
        }    
    }
*/
}


