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
    //<editor-fold defaultstate="collapsed" desc=" About Modes ">                      
    private static final int MODE_TUPLES = 0;
    private static final int MODE_MARKS = 1;
    private static final int MODE_ANIMATED = 2;
    private static final int MODE_HELP = 3;
    private static final int MODES_NUMBER = 4;
    private static final String[] modeName = {"QUIZ","MARK","ANIMATION", "HELP"};
    private int mode = MODE_TUPLES;
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
    //<editor-fold defaultstate="collapsed" desc=" Auxiliar elements ">                      
    private Font font = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
    
    private Tuple namesTuple = new Tuple ("","","");
    private Tuple valuesTuple = new Tuple ("","","");
    
    private String currentListeningPath;
    private String timeText = "0:00/0:00";
    private String messageTitle = "";
    
    private int yTranslation = 0;
    private int titleTimeCounter = -1;
    
    private int baseTimeForChange=0;
    
    private boolean stopThread = false;
    private boolean showMessageTitle = false;        
    private boolean timeChangeInProgress = false;
    private int desiredTimeChange = 0;
    private boolean agileKey = false;
    private boolean goForwardFlag = false; /* Bandera para saber si la tecla de adelantamiento de audio está presionada. */
    private boolean goBackFlag = false; /* Idem con la tecla de retroceso. */
    
    //</editor-fold>
    
    public PlayerForm (Display display, Displayable previous, Parser parser) {
        //<editor-fold defaultstate="collapsed" desc=" General Initialization ">
        int border = 10;
        
        this.display = display;
        this.previousDisplayable = previous;
        
        this.textPainter = new TextPainter(font, new Rectangle(border,border,this.getWidth()-(border*2), this.getHeight()-(border*2)));
        MediaServices.getMediaServices().setPlayerListener(this);
        //this.iterator = new SequentialIterator(txtpath);
        this.tupleRevelator = new TupleRevelator(this);
        
        this.parser = parser;
        this.marksManager = new MarksManager(parser);
        this.changeMode(true);
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
                        
                        
                        if (titleTimeCounter==0){
                            if (timeChangeInProgress){
                                timeChangeInProgress = false;
                                try{
                                    //MediaServices.getMediaServices().movePosition(desiredTimeChange);
                                    MediaServices.getMediaServices().setPosition(baseTimeForChange + desiredTimeChange);
                                }catch(Exception e){}
                                desiredTimeChange = 0;
                            }
                        }
                        
                    }else{
                        showMessageTitle = false;
                    }
                    
                    
                    if (goForwardFlag){
                        desiredTimeChange+=2;
                        putTitle("MOVE "+ desiredTimeChange + " sec.", (float)0.5);
                    }
                    if (goBackFlag){
                        desiredTimeChange-=2;
                        putTitle("MOVE "+ desiredTimeChange + " sec.", (float)0.5);
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
        String mainText;
        
        if (mode==MODE_HELP){
            mainText = Help.getIntructionsHelp();
        }else{
            mainText =  
                "*" + namesTuple.getKey() + "\n" + 
                valuesTuple.getKey() + "\n" + 
                
                "*" + namesTuple.getValue() + "\n" + 
                valuesTuple.getValue() + "\n" + 
                
                "*" + namesTuple.getExtra() + "\n" + 
                valuesTuple.getExtra() + "\n";
        }
        return mainText;
    }
    
    private void buildTitle(){
        String title;
        
        if (showMessageTitle) {
            title = ">" + this.messageTitle;
        } else {
            title = PlayerForm.modeName[this.mode]+" " + this.timeText;
        }
        
        this.setTitle(title);
        
    }
    
    public void paint(Graphics g) {
        g.setFont(font);
        
        g.setColor(0x000000);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(0xFFFFFF);
        textPainter.setTranslation(yTranslation);
        textPainter.paintTextComplex(g, buildMainText());        
    }
    
    protected void keyReleased(int keyCode) {
        //switch (keyCode){
       //     case '1':
                this.goBackFlag=false;
       //         break;
       //     case '3':
                this.goForwardFlag=false;
       //         break;
        //}
        
    }
    
    protected void keyPressed(int keyCode) {
        agileKey = false;
        if (this.mode==MODE_TUPLES) {
            this.modeTuplesKeyPressed(keyCode);
        } else if (this.mode==MODE_MARKS) {
            this.modeMarksKeyPressed(keyCode);
        } else if (this.mode==MODE_ANIMATED) {
            this.modeAnimatedKeyPressed(keyCode);
        }else{
            this.modeDefaultKeyPressed(keyCode);
        }
        
        if (agileKey==false){
            this.buildTitle();
            this.repaint();
        }
    }

    private String changeMode(boolean initialize) {
        if (initialize) {
            this.mode = 0;
        }else{
            this.mode = (mode+1)%MODES_NUMBER;
        }
        switch(mode){
            case MODE_TUPLES:   this.namesTuple = new Tuple("Expression","Explanation","Examples"); break;
            case MODE_ANIMATED: this.namesTuple = new Tuple("Text","Comment","Time"); break;
            case MODE_MARKS:    this.namesTuple = new Tuple("Current","Coming",""); break;
            case MODE_HELP:     this.namesTuple = new Tuple("","",""); break;
            default:
                break;
        }
        this.valuesTuple = new Tuple("","","");
        this.yTranslation = 0;
        
        return PlayerForm.modeName[this.mode];
        
    }

    
    
    private void modeTuplesKeyPressed(int keyCode) {
        
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
        
        
    }
      
    private void modeMarksKeyPressed(int keyCode){
        this.tupleRevelator.setMode(TupleRevelator.MODE_ALL);
        
        switch(keyCode){

            case '4':
                try{
                    String mark = marksManager.addMarkNow(MediaServices.getMediaServices().getPositionSeconds());
                    agileKey = true;
                    this.putTitle("MARK: " + mark, 1);
                }catch(Exception e){
                    this.putTitle("ERROR ADDING MARK...",1);
                }
                break;
            case '5':
                this.putTitle("TO COMMENT...", 1);
                break;

            case '6': 
                try{
                    this.putTitle("SAVING MARKS...", 2);
                    marksManager.saveMarks();
                    agileKey = true;
                    this.putTitle("MARKS SAVED", 2);
                }catch(Exception e){
                    this.putTitle("MARKS NOT SAVED", 10);
                }
                break;

            case '7':
                try {
                    this.putTitle("PREVIOUS MARK", 1);

                    Tuple tupleScreen;
                    String curr = marksManager.getPrevious(true).getKey();  // Retroceder y obtener el nuevo actual.
                    String com = marksManager.getNext(false).getKey();     // Obtener el previo (sin retroceso).

                    tupleScreen = new Tuple(curr,com,"");

                    this.tupleRevelator.setTuple(tupleScreen);
                } catch (Exception ex) {
                    this.putTitle("MARK ERROR", 1);
                }
                break;
            case '8':
                try {
                    Tuple tupleScreen;
                    this.putTitle("APPLIED MARK " + this.timeText, 1);

                    String curr = marksManager.getNext(true).getKey();      // Avanzar y obtener el nuevo actua.
                    marksManager.applyTimeToCurrentMark(MediaServices.getMediaServices().getPositionSeconds());
                    String com = marksManager.getNext(false).getKey(); // Obtener el previo (sin retroceso).

                    tupleScreen = new Tuple(curr,com,"");
                    this.yTranslation = 0;
                    this.tupleRevelator.setTuple(tupleScreen);
                } catch (Exception ex) {
                    this.putTitle("MARK ERROR", 1);
                }
                break;
            case '9':
                try {
                    this.putTitle("NEXT MARK", 1);

                    Tuple tupleScreen;
                    String curr = marksManager.getNext(true).getKey();      // Avanzar y obtener el nuevo actua.
                    String com = marksManager.getNext(false).getKey(); // Obtener el previo (sin retroceso).

                    tupleScreen = new Tuple(curr,com,"");

                    this.tupleRevelator.setTuple(tupleScreen);
                } catch (Exception ex) {
                    this.putTitle("MARK ERROR", 1);
                }
                break;
            default:
                modeDefaultKeyPressed(keyCode);
                break;
        }
        
    }
    
    private void modeAnimatedKeyPressed(int keyCode){
        
        switch(keyCode){
            case '8':
                this.putTitle("REVELATION MODE", 1);
                this.tupleRevelator.nextMode();
                break;
            default:
                modeDefaultKeyPressed(keyCode);
                break;
        }
        
    }
    
    private void modeDefaultKeyPressed(int keyCode){
        if (keyCode>0){
            switch(keyCode){
                
                case '*':
                    goPreviousForm();
                    break;
                case '#':
                    String mn = this.changeMode(false);
                    this.putTitle("MODE " + mn, 1);
                    break;
                case '1':
                    try{
                        this.baseTimeForChange = (int)(MediaServices.getMediaServices().getPosition()/MediaServices.TIME_FACTOR);
                    }catch(Exception e){}
                    this.timeChangeInProgress = true;
                    this.desiredTimeChange = desiredTimeChange - 1;
                    this.putTitle("MOVE "+ this.desiredTimeChange + " sec.", (float)0.5);
                    this.goBackFlag=true;
                    agileKey = true;
                    break;
                case '2':
                    this.putTitle("PLAY/PAUSE", 1);
                    try{
                        MediaServices.getMediaServices().playPause(); 
                    }catch(Exception e){
                        this.putTitle("ERROR PLAY/PAUSE", 1);
                    }
                    agileKey = true;
                    break;
                case '3':
                    try{
                        this.baseTimeForChange = (int)(MediaServices.getMediaServices().getPosition()/MediaServices.TIME_FACTOR);
                    }catch(Exception e){}
                    this.timeChangeInProgress = true;
                    this.desiredTimeChange = desiredTimeChange + 1;
                    this.putTitle("MOVE "+ this.desiredTimeChange + " sec.", (float)0.5);
                    this.goForwardFlag=true;
                    agileKey = true;
                    break;
                default:
                    this.putTitle("NOT USED", 1);
                    break;
            }
        }else{
            switch(this.getGameAction(keyCode)){
                case PlayerForm.DOWN:
                    this.putTitle("DOWN LINE", 1);
                    yTranslation +=1;
                    break;
                case PlayerForm.UP:
                    this.putTitle("UP LINE", 1);
                    yTranslation = Math.max(yTranslation - 1,0);
                    break;
                    
                case PlayerForm.RIGHT:
                    this.putTitle("DOWN PAGE", 1);
                    yTranslation +=5;
                    break;
                case PlayerForm.LEFT:
                    this.putTitle("UP PAGE", 1);
                    yTranslation = Math.max(yTranslation - 5,0);
                    break;
                default:
                    this.putTitle("NOT USED", 1);
                    break;
            }
        }
    }
    
    
    public void playerUpdate(Player pl, String str, Object obj) {
        long current;
        Tuple t;
        
        current = (pl.getMediaTime()/MediaServices.TIME_FACTOR); 
        this.setTimeText(current, pl.getDuration()/MediaServices.TIME_FACTOR);    
        
        if (this.mode == PlayerForm.MODE_ANIMATED){
            try{
                t = this.marksManager.getMark(current);
                this.yTranslation = 0;
                this.setValues(t);
                this.repaint();
            }catch(Exception e){
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
        this.timeText = Parser.sec2hoursShort(time) + "/" + Parser.sec2hoursShort(duration);
        buildTitle();
    }
    
    
    /*public void setNames(Tuple names) {
        this.namesTuple = names;
    }*/
    public void setValues(Tuple values) {
        this.valuesTuple = values;
    }
    
    private void goPreviousForm(){
        display.setCurrent(previousDisplayable); /* Return. */
    }
    
    private void putTitle(String title, float seconds){
        this.titleTimeCounter = (int)(seconds*10);
        this.messageTitle = title;
    }
    //</editor-fold>
}

