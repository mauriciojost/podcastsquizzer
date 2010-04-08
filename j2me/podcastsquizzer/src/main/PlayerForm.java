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
import textboxpackage.TextBoxFormReadyListener;


/**
 * This class is intended to provide the user with an environment where to play
 * the listening, add and look for the marks.
 */
public class PlayerForm extends Canvas implements PlayerListener, Playerable {
    private final static int KEY_MISC_LEFT = -7;
    private final static int KEY_MISC_RIGHT = -6;
    //<editor-fold defaultstate="collapsed" desc=" About Modes ">                      
    public static final int MODE_GLOSSARY = 0;
    public static final int MODE_ESSAY_MARKS = 1;
    public static final int MODE_ANIMATED = 2;
    public static final int MODE_HELP = 3;
    public static final int MODES_NUMBER = 4;
    public  static final String[] modeName = {"QUIZ","MARK","ANIMATION", "HELP"};
    private int mode = MODE_GLOSSARY;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc=" Useful objects ">                      
    private Displayable previousDisplayable;
    private Display display;
    
    private ScreenHandler glossaryScreenHandler;
    private ScreenHandler listeningScreenHandler;
    private MarksManager marksManager;
    
    //private Iterator iterator;
    private Parser parser;
    private TextPainter mainTextPainter;
    private TextPainter helpTextPainter;
    private TextPainter titleTextPainter;
    //private TextBoxForm textBoxForm;
    
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc=" Auxiliar elements ">                      
    private Font fontSmall = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
    private Font fontMedium = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
    
    //private Tuple namesTuple = new Tuple ("","","");
    //private Tuple valuesTuple = new Tuple ("","","");
    
    private String currentListeningPath;
    private String timeText = "0:00/0:00";
    private String messageTitle = "";
    //private String mainText = "mainText";
    private String helpText = "helpText";
    private String titleText = "titleText";
    
    private Vector glossaryVector;
    
    private int yTranslation = 0;
    private int titleTimeCounter = -1;
    
    private int baseTimeForChange=0;
    
    private boolean stopThread = false;
    //private boolean showMessageTitle = false;        
    private boolean timeChangeInProgress = false;
    private int desiredTimeChange = 0;
    private boolean agileKey = false;
    private boolean goForwardFlag = false; /* Bandera para saber si la tecla de adelantamiento de audio está presionada. */
    private boolean goBackFlag = false; /* Idem con la tecla de retroceso. */
    private boolean stateBeforeMoving = false;
    private int backgroundColor = 0x000000;
    //</editor-fold>
    
    //list = new List("list", Choice.IMPLICIT);
    //list.setCommandListener(this);
    
    
    
    public PlayerForm (Display display, Displayable previous, Parser parser)  {
        //<editor-fold defaultstate="collapsed" desc=" General Initialization ">
        int vspace;
        int vborder = 1;
        int hborder = 1;
        
        vspace = fontMedium.getHeight()*1;
        this.display = display;
        this.previousDisplayable = previous;
        
        this.setFullScreenMode(true);
        
        Rectangle rec = new Rectangle(hborder,vborder,this.getWidth()-(hborder*2), this.getHeight()-(vborder*2));
        this.titleTextPainter = new TextPainter(fontMedium, rec.newSetHeight(vspace));
        this.titleTextPainter.setBackgroundColor(backgroundColor);
        Rectangle mainRec = rec.newSetY(vspace).newMoveHeight(-2*vspace);
        this.mainTextPainter = new TextPainter(fontSmall, mainRec);
        this.mainTextPainter.setBackgroundColor(backgroundColor);
        this.helpTextPainter = new TextPainter(fontSmall, rec.newSetHeight(vspace).newMoveY(mainRec.getHeigth()+vspace));
        this.helpTextPainter.setBackgroundColor(backgroundColor);
        
        MediaServices.getMediaServices().addPlayerListener(this);
        //this.iterator = new SequentialIterator(txtpath);
        //this.tupleRevelator = new TupleRevelator(this);
        
        this.parser = parser;
        this.marksManager = new MarksManager(parser);
        this.changeMode(true);
        
        this.setCommandListener(null);
        
        
        //this.textBoxForm = new TextBoxForm(display, this, this);
        
        
        glossaryScreenHandler = new GlossaryScreenHandler(display, this);
        listeningScreenHandler = new ListeningScreenHandler(display, this);
        
        
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
                    
                    if (goForwardFlag){
                        desiredTimeChange+=2;
                        putTitle("MOVE "+ desiredTimeChange + " sec.", (float)0.5);
                    }
                    if (goBackFlag){
                        desiredTimeChange-=2;
                        putTitle("MOVE "+ desiredTimeChange + " sec.", (float)0.5);
                    }
                    
                    if (titleTimeCounter>0){   /* Un resumen de mensaje tiene que ser mostrado. */
                        titleTimeCounter--;     /* Se consume el tiempo durante el cual se muestra. */
                        //showMessageTitle = true;
                        if (titleTimeCounter==1){ /* Cuando se está a punto de dejar de mostrar el mensaje... */
                            if (timeChangeInProgress){
                                timeChangeInProgress = false;
                                try{
                                    //MediaServices.getMediaServices().movePosition(desiredTimeChange);
                                    MediaServices.getMediaServices().setPosition(baseTimeForChange + desiredTimeChange);
                                    
                                    if(stateBeforeMoving==true){
                                        MediaServices.getMediaServices().play();
                                    }           
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                                desiredTimeChange = 0;
                            }
                        }   
                    }else{
                        //showMessageTitle = false;
                    }
                    buildTitle();
                }
            }
        }
        ,"Visual Thread").start();
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc=" Help Thread (2000ms) ">
        new Thread(
        new Runnable() {
            public void run() {
                while(stopThread==false){
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    buildHelpText();
                }
            }
        }
        ,"Help Thread").start();
        //</editor-fold>
    }
    
    public void setGlossary(Vector gv){
        this.glossaryVector = gv;
        glossaryScreenHandler.setMainElement(gv);
    }
    
    //public void setGlossaryVectorRandomly(Vector gv){
    //    this.iterator = new RandomIterator(gv);
    //}
    
    public void setListening(String currentListeningPath) throws Exception{
        this.currentListeningPath = currentListeningPath;
        MediaServices.getMediaServices().load(currentListeningPath);
    }
    
    public void setTranscript(Vector gv){
        this.marksManager.setMarks(gv);
    }
    
    public void buildHelpText(){
        helpText = Help.getKeysMeaningNext(mode);
        this.repaint();
    }
    
    /*
    public void buildMainText(){
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
        this.mainTextPainter.setText(this.mainText);
        this.repaint();
    }*/
    
    private void buildTitle(){
        
        if (titleTimeCounter>0) { /* El mensaje debe ser mostrado aún. */
            titleText = ">" + this.messageTitle;
        } else {
            titleText = PlayerForm.modeName[this.mode]+" " + this.timeText;
        }
        this.repaint();
    }
    
    public void paint(Graphics g) {
        g.setColor(backgroundColor);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        
        helpTextPainter.paintText(g, this.helpText);
        mainTextPainter.setTranslation(yTranslation);
        mainTextPainter.paintTextComplex(g);
        titleTextPainter.paintText(g, this.titleText);
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
        boolean catched=false;
        if (this.mode==MODE_GLOSSARY) {
            catched = this.glossaryScreenHandler.keyPressed(keyCode);
            //this.modeGlossaryKeyPressed(keyCode);
        } else if (this.mode==MODE_ESSAY_MARKS) {
            //this.modeEssayMarksKeyPressed(keyCode);
            catched = this.listeningScreenHandler.keyPressed(keyCode);
        } else if (this.mode==MODE_ANIMATED) {
            //this.modeAnimatedKeyPressed(keyCode);
            catched = this.listeningScreenHandler.keyPressed(keyCode);
        }
        
        if (catched==false){
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
            //case MODE_GLOSSARY:   this.setNames(new Tuple("Expression","Explanation","Examples")); break;
            //case MODE_ANIMATED: this.setNames(new Tuple("Text","Comment","Time")); break;
            //case MODE_ESSAY_MARKS:    this.setNames(new Tuple("Current","Coming","")); break;
            //case MODE_HELP:     this.setNames(new Tuple("","","")); break;
            default:
                break;
        }
        //this.tupleRevelator.setTuple(new Tuple("","",""));
        this.yTranslation = 0;
        
        return PlayerForm.modeName[this.mode];
        
    }

    
    
    
    public void modeDefaultKeyPressed(int keyCode){

        switch(keyCode){

            case PlayerForm.KEY_MISC_LEFT:
                String mn = this.changeMode(false);
                this.putTitle("MODE " + mn, 1);
                this.buildTitle();
                this.repaint();
                break;
            case PlayerForm.KEY_MISC_RIGHT:
                goPreviousForm();
                break;
            default:
                
                switch(this.getGameAction(keyCode)){
                    case PlayerForm.FIRE:
                        this.putTitle("PLAY/PAUSE", 1);
                        try{
                            MediaServices.getMediaServices().playPause(); 
                        }catch(Exception e){
                            this.putTitle("ERROR PLAY/PAUSE", 1);
                        }    
                        break;
                    case PlayerForm.DOWN:
                        this.putTitle("DOWN LINE", 1);
                        yTranslation +=3;
                        break;
                    case PlayerForm.UP:
                        this.putTitle("UP LINE", 1);
                        yTranslation = Math.max(yTranslation - 3,0);
                        break;

                    case PlayerForm.RIGHT:
                        try{
                            if (this.timeChangeInProgress==false){
                                this.stateBeforeMoving = MediaServices.getMediaServices().isItPlaying();
                            }
                            MediaServices.getMediaServices().pause();
                            this.baseTimeForChange = (int)(MediaServices.getMediaServices().getPosition()/MediaServices.TIME_FACTOR);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        this.timeChangeInProgress = true;
                        this.desiredTimeChange = desiredTimeChange + 1;
                        this.putTitle("MOVE "+ this.desiredTimeChange + " sec.", (float)0.5);
                        this.goForwardFlag=true;
                        agileKey = true;
                        break;
                    case PlayerForm.LEFT:
                        try{
                            if (this.timeChangeInProgress==false){
                                this.stateBeforeMoving = MediaServices.getMediaServices().isItPlaying();
                            }
                            MediaServices.getMediaServices().pause();
                            this.baseTimeForChange = (int)(MediaServices.getMediaServices().getPosition()/MediaServices.TIME_FACTOR);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        this.timeChangeInProgress = true;
                        this.desiredTimeChange = desiredTimeChange - 1;
                        this.putTitle("MOVE "+ this.desiredTimeChange + " sec.", (float)0.5);
                        this.goBackFlag=true;
                        agileKey = true;
                        break;
                    default:
                        this.putTitle("NOT USED (" + keyCode + ", " + this.getGameAction(keyCode)+ ")", 1);
                        break;
                }
        }

    }
        
    public void playerUpdate(Player pl, String str, Object obj) {
        long current;
        
        current = (pl.getMediaTime()/MediaServices.TIME_FACTOR); 
        this.setTimeText(current, pl.getDuration()/MediaServices.TIME_FACTOR);    
    
        if (this.mode == PlayerForm.MODE_ANIMATED){
            this.listeningScreenHandler.playerUpdate(pl, str, obj);
        }
        
    }
    
    public void initialize() {
        //if (iterator!=null) 
        //    iterator.reinitialize();
    }
    
    public void setTimeText(long time, long duration){
        this.timeText = Parser.sec2hoursShort(time) + "/" + Parser.sec2hoursShort(duration);
        buildTitle();
    }
      
    public void setText(String text){
        this.mainTextPainter.setText(text);
        this.repaint();
    }
    
    private void goPreviousForm(){
        display.setCurrent(previousDisplayable); /* Return. */
    }
    
    public void putTitle(String title, double seconds) {
        this.messageTitle = title;
        this.titleTimeCounter = (int)(seconds*10);
    }
    
    public Displayable getDisplayable(){
         return this;
    }
    
    public void resetTranslation(){
        this.yTranslation = 0;
    }

    
}

