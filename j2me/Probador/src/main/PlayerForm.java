/**
 * @author Mauri
 * 
 */

package main;

import javax.microedition.lcdui.*;
import javax.microedition.media.*;
import java.util.Vector;

import mediaservicespackage.*;
import persistencepackage.*;
import tuplesshowerpackage.*;
import canvaspackage.*;


/**
 * This class is intended to provide the user with an environment where to play
 * the listening, add and look for the marks.
 */
public class PlayerForm extends Canvas implements PlayerListener, TuplesShowerInterface {
    private Font font = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
    private Displayable previousDisplayable;
    private Display display;
    
    private Vector marksVector;
    
    private String instructionsText = 
            "0. Hide instructions\n"+ 
            "UP.\n"+ 
            "DOWN.\n"+ 
            "LEFT. Next mode. \n" +
            "RIGHT. Previous mode. \n" +
            "1. Return\n"+ 
            "2. Change showing mode\n"+ 
            "3. Add mark\n"+ 
            "4. Previous expression\n"+ 
            "5. Show\n"+ 
            "6. Next expression\n"+ 
            "7. Rewind\n"+ 
            "8. Play/Pause\n"+ 
            "9. Forward ";
    
    private String timeText = "0:00/0:00";
    private boolean showInstructions = false;
    
    private TextPainter textPainter;
    //private Key2Text key2Text; 
    
    private Tuple namesTuple = new Tuple ("","","");
    private Tuple valuesTuple = new Tuple ("","","");
    
    private String currentListeningPath;
    
    private TupleRevelator tupleRevelator;
    
    private Iterator iterator;
    
    private int yTranslation = 0;
    
    private Parser parser;
    
    private boolean stopThread = false;
            
    private int titleTimeCounter = -1;
    private String messageTitle = "";
    
    private boolean showMessageTitle = false;
    
    public PlayerForm (Display display, Displayable previous, Tuple names, Iterator iterator, Parser parser) {
        this.display = display;
        this.previousDisplayable = previous;
        
        int border = 10;
        
        Rectangle bounds = new Rectangle(border,border,this.getWidth()-(border*2), this.getHeight()-(border*2));
        this.textPainter = new TextPainter(font, bounds);
        //this.key2Text = new Key2Text();
        
        MediaServices.getMediaServices().setPlayerListener(this);
                
        this.setNames(names);
        this.setIterator(iterator);
        this.tupleRevelator = new TupleRevelator(this);
        
        this.marksVector = new Vector();
        
        this.parser = parser;
        
        new Thread(
        new Runnable() {
            public void run() {
                while(stopThread==false){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    if (titleTimeCounter>=0){
                        titleTimeCounter--;
                        showMessageTitle = true;
                    }else{
                        showMessageTitle = false;
                    }
                    buildTitle();
                }
            }
        }
        ).start();
        
    }
    
    public void setListening(String currentListeningPath) throws Exception{
        this.currentListeningPath = currentListeningPath;
        MediaServices.getMediaServices().load(currentListeningPath);
    }
    
    
    public String buildMainText(){
        String mainText =  
                (this.showInstructions?instructionsText:"0. Show instructions") + "\n" +
                "*" + namesTuple.getKey() + "\n" + 
                valuesTuple.getKey() + "\n" + 
                
                "*" + namesTuple.getValue() + "\n" + 
                valuesTuple.getValue() + "\n" + 
                
                "*" + namesTuple.getExtra() + "\n" + 
                valuesTuple.getExtra() + "\n";
        return mainText;
    }
    
    private void buildTitle(){
        String title;
        
        if (showMessageTitle) {
            title = this.messageTitle;
        } else {
            title = "MQ " + this.timeText;
        }
        
        this.setTitle(title);
        
    }
    
    public void paint(Graphics g) {
        g.setFont(font);
        
        g.setColor(0x000000);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(0xFFFFFF);
        g.translate(0, yTranslation);
        textPainter.paintTextComplex(g, buildMainText());        
    }
    
    protected void keyPressed(int keyCode) {
        
        
        if (keyCode>0){
            switch(keyCode){
                case '0':
                    showInstructions = !showInstructions; /* Show/hide instructions. */
                    break;
                case '1':
                    goPreviousForm();
                    
                    break;
                case '2':
                    this.tupleRevelator.nextMode();
                    this.putTitle("Next mode", 1);
                    break;
                case '3':
                    this.addMarkNow();
                    break;
                case '7':
                    MediaServices.getMediaServices().goBack(); 
                    this.putTitle("<<<", 1);
                    break;
                case '8':
                    MediaServices.getMediaServices().playPause(); 
                    this.putTitle("PLAY/PAUSE", 1);
                    break;
                case '9':
                    MediaServices.getMediaServices().goForward(); 
                    this.putTitle(">>>", 1);
                    break;
                default:
                    break;
                case '6':
                    if (iterator!=null)
                        this.tupleRevelator.setTuple(this.iterator.getNext());
                    this.putTitle("Next tuple", 1);
                    break;
                case '4':
                    if (iterator!=null)
                        this.tupleRevelator.setTuple(this.iterator.getPrevious());
                    this.putTitle("Previous tuple", 1);
                    break;
                case '5': 
                    this.tupleRevelator.nextRevelation();
                    this.putTitle("Reveal", 1);
                    break;
            }
        }else{
            switch(this.getGameAction(keyCode)){
                case PlayerForm.DOWN:
                    yTranslation -=15;
                    this.putTitle("Down", 1);
                    break;
                case PlayerForm.UP:
                    yTranslation +=15;
                    this.putTitle("Up", 1);
                    break;
                default:
                    break;
            }
        }
        
        
        this.repaint();
        
        /*if (keyCode=='1') {
            display.setCurrent(previousDisplayable);
        } else {
            this.mainText = this.key2Text.newKey(keyCode);
            this.repaint();
        }*/    
    }

        
    public void setIterator(Iterator iterator) {
        this.iterator = iterator;
    }
    
    public void initialize() {
        if (iterator!=null) 
            iterator.reinitialize();
    }
    
    public void setTimeText(long time, long duration){
        this.timeText = Parser.sec2hours(time) + "/" + Parser.sec2hours(duration);
        buildTitle();
    }
    
    public void playerUpdate(Player pl, String str, Object obj) {
        this.setTimeText(pl.getMediaTime()/MediaServices.TIME_FACTOR, pl.getDuration()/MediaServices.TIME_FACTOR);
        
    }
    
    
    public void setNames(Tuple names) {
        this.namesTuple = names;
    }
    public void setValues(Tuple values) {
        this.valuesTuple = values;
    }
    public void addMarkNow(){
        String text;
        long seg;
        try{
            seg = MediaServices.getMediaServices().getPositionSeconds();
            text =  Parser.sec2hours(seg);
            this.marksVector.addElement(new Tuple(text,"",""));
            this.putTitle("Mark: " + text, 1);
        }catch(Exception e){
            this.putTitle("Error adding mark...",1);
        }
        
    }
    
    private void goPreviousForm(){
        this.saveMarks();
        display.setCurrent(previousDisplayable); /* Return. */
    }
    
    private void saveMarks(){
        String text;
        
        marksVector.trimToSize();
        if (marksVector.size()!=0) {
            text = this.parser.vector2txt(marksVector);
            try {

                String currentPath = MediaServices.getMediaServices().getCurrentPath();        
                String newFilePath = FileServices.getDirectory(currentPath) + FileServices.getFilenameWExtensionFromPath(currentPath) + "_.txt";

                FileServices.writeTXTFile(newFilePath, text.getBytes());
                this.putTitle("Marks saved.", 1);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    
    }
    
    private void putTitle(String title, int seconds){
        this.titleTimeCounter = seconds*10;
        this.messageTitle = title;
    }
    
}


