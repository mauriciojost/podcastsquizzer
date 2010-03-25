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
import java.io.IOException;


/**
 * This class is intended to provide the user with an environment where to play
 * the listening, add and look for the marks.
 */
public class PlayerForm extends Canvas implements PlayerListener, TuplesShowerInterface {
    //<editor-fold defaultstate="collapsed" desc=" About Modes ">                      
    private static final int MODE_NORMAL = 0;
    private static final int MODE_MARKS = 1;
    private static final int MODES_NUMBER = 2;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc=" Useful objects ">                      
    private Displayable previousDisplayable;
    private Display display;
    
    
    private MarksManager marksManager;
    private TupleRevelator tupleRevelator;
    private Iterator iterator;
    private Parser parser;
    private TextPainter textPainter;
    
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc=" User Instructions Strings ">                      
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
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc=" Auxiliar elements ">                      
    private Font font = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
    
    private Tuple namesTuple = new Tuple ("","","");
    private Tuple valuesTuple = new Tuple ("","","");
    
    private String currentListeningPath;
    private String timeText = "0:00/0:00";
    private String messageTitle = "";
    
    private int yTranslation = 0;
    private int titleTimeCounter = -1;
    private int mode = MODE_NORMAL;
    
    private boolean stopThread = false;
    private boolean showMessageTitle = false;        
    private boolean showInstructions = false;   
    //</editor-fold>
    
    public PlayerForm (Display display, Displayable previous, Tuple names, Parser parser) {
        //<editor-fold defaultstate="collapsed" desc=" General Initialization ">
        int border = 10;
        
        this.display = display;
        this.previousDisplayable = previous;
        
        this.textPainter = new TextPainter(font, new Rectangle(border,border,this.getWidth()-(border*2), this.getHeight()-(border*2)));
        MediaServices.getMediaServices().setPlayerListener(this);
        this.setNames(names);
        //this.iterator = new SequentialIterator(txtpath);
        this.tupleRevelator = new TupleRevelator(this);
        
        this.parser = parser;
        this.marksManager = new MarksManager(parser);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc=" Visual Thread (100ms) ">
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
        //</editor-fold>
    }
    
    public void setGlossaryVectorSequentially(Vector gv){
        this.iterator = new SequentialIterator(gv);
    }
    
    public void setGlossaryVectorRandomly(Vector gv){
        this.iterator = new RandomIterator(gv);
    }
    
    public void setListening(String currentListeningPath) throws Exception{
        this.currentListeningPath = currentListeningPath;
        MediaServices.getMediaServices().load(currentListeningPath);
    }
    
    public void setTranscript(Vector gv){
        this.marksManager.setMarks(gv);
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
            title = "MQ ("+this.mode+") " + this.timeText;
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
        
        this.putTitle("Not supported (" + keyCode +")", 1);
        
        if (this.mode==MODE_NORMAL) {
            this.modeTuplesKeyPressed(keyCode);
        } else if (this.mode==MODE_MARKS) {
            this.modeMarksKeyPressed(keyCode);
        }
        
        this.buildTitle();
        this.repaint();
    }

    private void changeMode() {
       this.mode = (mode+1)%MODES_NUMBER;
    }

    /** 
     * GENERAL KEYS
     * ------- ----
     * RETURN               *
     * CHANGE MODE          # 
     * S/HIDE INSTRUCTIONS  0
     * REWIND               1
     * PLAY/STOP            2
     * FORWARD              3
     * UP   PAGE            LEFT
     * DOWN PAGE            RIGHT
     * UP   LINE            UP
     * DOWN LINE            DOWN
     * 
     * MARKS MODE (1)
     * ----- ----
     * ADD MARK             4
     * COMMENT MARK         5
     * SAVE MARKS           6
     *                      7
     *                      8
     *                      9
     * 
     * TUPLES MODE (0)
     * ------ ----
     * PREVIOUS TUPLE       4
     * REVEAL               5
     * NEXT TUPLE           6
     *                      7
     * CHANGE TUPLE MODE    8
     *                      9
     * 
    
     */
    private void modeTuplesKeyPressed(int keyCode) {
        if (keyCode>0){
            switch(keyCode){
                case '4':
                    this.putTitle("PREVIOUS RECORD", 1);
                    if (iterator!=null)
                        this.tupleRevelator.setTuple(this.iterator.getPrevious());
                    break;
                case '5': 
                    this.putTitle("REVEAL", 1);
                    this.tupleRevelator.nextRevelation();
                    break;
                case '6':
                    this.putTitle("NEXT RECORD", 1);
                    if (iterator!=null)
                        this.tupleRevelator.setTuple(this.iterator.getNext());
                    break;
                case '8':
                    this.putTitle("REVELATION MODE", 1);
                    this.tupleRevelator.nextMode();
                    break;
                default:
                    modeDefaultKeyPressed(keyCode);
                    break;
            }
        }else{
            modeDefaultKeyPressed(keyCode);
        }
    }
    private void modeMarksKeyPressed(int keyCode){
        if (keyCode>0){
            switch(keyCode){
                
                case '4':
                    try{
                        String mark = marksManager.addMarkNow(MediaServices.getMediaServices().getPositionSeconds());
                        this.putTitle("MARK: " + mark, 1);
                    }catch(Exception e){
                        this.putTitle("ERROR ADDING MARK...",1);
                    }
                    break;
                case '5':
                    /* COMMENT MARK */
                    break;
                        
                case '6': 
                    try{
                        marksManager.saveMarks();
                        this.putTitle("MARKS SAVED", 3);
                    }catch(IOException e){
                        this.putTitle("MARKS ERROR", 3);
                    }
                    break;    
                default:
                    modeDefaultKeyPressed(keyCode);
                    break;
            }
        }else{
            modeDefaultKeyPressed(keyCode);
        }
    }
    private void modeDefaultKeyPressed(int keyCode){
        if (keyCode>0){
            switch(keyCode){
                
                case '*':
                    goPreviousForm();
                    break;
                case '#':
                    this.changeMode();
                    this.putTitle("MODE CHANGED", 1);
                    break;
                case '0':
                    this.putTitle("SHOW/HIDE INSTR.", 1);
                    showInstructions = !showInstructions; /* Show/hide instructions. */
                    break;
                
                    
                case '1':
                    this.putTitle("REWIND", 1);
                    MediaServices.getMediaServices().goBack(); 
                    break;
                case '2':
                    this.putTitle("PLAY/PAUSE", 1);
                    MediaServices.getMediaServices().playPause(); 
                    break;
                case '3':
                    this.putTitle("FORWARD", 1);
                    MediaServices.getMediaServices().goForward(); 
                    break;
                    
                default:
                    break;
            }
        }else{
            switch(this.getGameAction(keyCode)){
                case PlayerForm.DOWN:
                    this.putTitle("DOWN LINE", 1);
                    yTranslation -=20;
                    break;
                case PlayerForm.UP:
                    this.putTitle("UP LINE", 1);
                    yTranslation +=20;
                    break;
                    
                case PlayerForm.RIGHT:
                    this.putTitle("DOWN PAGE", 1);
                    yTranslation -=80;
                    break;
                case PlayerForm.LEFT:
                    this.putTitle("UP PAGE", 1);
                    yTranslation +=80;
                    break;
                default:
                    break;
            }
        }
    }
    
    
    //<editor-fold defaultstate="collapsed" desc=" Simple methods ">
    /*public void setIterator(Iterator iterator) {
        this.iterator = iterator;
    }*/
    
    public void initialize() {
        if (iterator!=null) 
            iterator.reinitialize();
    }
    
    public void setTimeText(long time, long duration){
        this.timeText = Parser.sec2hours(time) + "/" + Parser.sec2hours(duration);
        buildTitle();
    }
    
    public void playerUpdate(Player pl, String str, Object obj) {
        long current;
        Tuple t;
        
        current = pl.getMediaTime()/MediaServices.TIME_FACTOR;
        this.setTimeText(current, pl.getDuration()/MediaServices.TIME_FACTOR);    
        try{
            t = this.marksManager.getMark(current);
            this.setValues(t);
            this.repaint();
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void setNames(Tuple names) {
        this.namesTuple = names;
    }
    public void setValues(Tuple values) {
        this.valuesTuple = values;
    }
    
    private void goPreviousForm(){
        display.setCurrent(previousDisplayable); /* Return. */
    }
    
    private void putTitle(String title, int seconds){
        this.titleTimeCounter = seconds*10;
        this.messageTitle = title;
    }
    //</editor-fold>
}


