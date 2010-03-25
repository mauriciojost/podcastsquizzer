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
    private static final int MODE_NORMAL = 0;
    private static final int MODE_MARKS = 1;
    
    
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
    
    private int mode = MODE_NORMAL;
    
    private MarksManager marksManager;
    
    public PlayerForm (Display display, Displayable previous, Tuple names, Iterator iterator, Parser parser) {
        int border = 10;
        
        this.display = display;
        this.previousDisplayable = previous;
        Rectangle bounds = new Rectangle(border,border,this.getWidth()-(border*2), this.getHeight()-(border*2));
        this.textPainter = new TextPainter(font, bounds);
        MediaServices.getMediaServices().setPlayerListener(this);
        this.setNames(names);
        this.setIterator(iterator);
        this.tupleRevelator = new TupleRevelator(this);
        this.marksVector = new Vector();
        this.parser = parser;
        
        this.marksManager = new MarksManager(parser, marksVector);
        
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
        //g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(0xFFFFFF);
        g.translate(0, yTranslation);
        textPainter.paintTextComplex(g, buildMainText());        
    }
    
    protected void keyPressed(int keyCode) {
        
        this.putTitle("Not supported (" + keyCode +")", 1);
        
        if (this.mode==MODE_NORMAL)
            this.modeTuplesKeyPressed(keyCode);
        
        if (this.mode==MODE_MARKS)
            this.modeMarksKeyPressed(keyCode);
        
        
        this.repaint();
    }

    private void changeMode() {
       this.mode = (mode+1)%2;
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
     * MARKS MODE
     * ----- ----
     * ADD MARK             4
     * COMMENT MARK         5
     * SAVE MARKS           6
     *                      7
     *                      8
     *                      9
     * 
     * TUPLES MODE
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
                    if (iterator!=null)
                        this.tupleRevelator.setTuple(this.iterator.getPrevious());
                    this.putTitle("PREVIOUS RECORD", 1);
                    break;
                case '5': 
                    this.tupleRevelator.nextRevelation();
                    this.putTitle("REVEAL", 1);
                    break;
                case '6':
                    if (iterator!=null)
                        this.tupleRevelator.setTuple(this.iterator.getNext());
                    this.putTitle("NEXT RECORD", 1);
                    break;
                case '8':
                    this.tupleRevelator.nextMode();
                    this.putTitle("REVELATION MODE", 1);
                    break;
                default:
                    modeDefaultKeyPressed(keyCode);
                    break;
            }
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
                        this.putTitle("MARKS SAVED", 1);
                    }catch(IOException e){
                        this.putTitle("MARKS ERROR", 1);
                    }
                    break;    
                default:
                    modeDefaultKeyPressed(keyCode);
                    break;
            }
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
                    break;
                case '0':
                    showInstructions = !showInstructions; /* Show/hide instructions. */
                    break;
                
                    
                case '1':
                    MediaServices.getMediaServices().goBack(); 
                    this.putTitle("REWIND", 1);
                    break;
                case '2':
                    MediaServices.getMediaServices().playPause(); 
                    this.putTitle("PLAY/PAUSE", 1);
                    break;
                case '3':
                    MediaServices.getMediaServices().goForward(); 
                    this.putTitle("FORWARD", 1);
                    break;
                    
                default:
                    break;
            }
        }else{
            switch(this.getGameAction(keyCode)){
                case PlayerForm.DOWN:
                    yTranslation -=20;
                    this.putTitle("DOWN LINE", 1);
                    break;
                case PlayerForm.UP:
                    yTranslation +=20;
                    this.putTitle("UP LINE", 1);
                    break;
                    
                case PlayerForm.RIGHT:
                    yTranslation -=80;
                    this.putTitle("DOWN PAGE", 1);
                    break;
                case PlayerForm.LEFT:
                    yTranslation +=80;
                    this.putTitle("UP PAGE", 1);
                    break;
                default:
                    break;
            }
        }
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
    
    private void goPreviousForm(){
        display.setCurrent(previousDisplayable); /* Return. */
    }
    
    private void putTitle(String title, int seconds){
        this.titleTimeCounter = seconds*10;
        this.messageTitle = title;
    }
    
}


