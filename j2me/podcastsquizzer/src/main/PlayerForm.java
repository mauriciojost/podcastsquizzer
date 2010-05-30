package main;

import javax.microedition.lcdui.*;
import javax.microedition.media.*;
import java.util.Vector;

import mediaservicespackage.*;
import persistencepackage.*;
import canvaspackage.*;
import java.util.Enumeration; 

/**
 * This class is intended to provide the user with an environment where to play
 * the listening, add and look for the marks.
 */
public class PlayerForm extends Canvas implements PlayerListener, Playerable {
    private final static int KEY_MISC_LEFT = -7;
    private final static int KEY_MISC_RIGHT = -6;
    //<editor-fold defaultstate="collapsed" desc=" About Modes ">                      
    private int mode = 0;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc=" Useful objects ">                      
    private Displayable previousDisplayable;
    private Display display;
    
    private TextPainter mainTextPainter;
    private TextPainter helpTextPainter;
    private TextPainter titleTextPainter;
    private Vector screenHandlersVector;
    
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc=" Auxiliar elements ">                      
    private Font fontSmall = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
    private Font fontMedium = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
    
    private String currentListeningPath;
    private String timeText = "0:00/0:00";
    private String messageTitle = "";
    private String helpText = "helpText";
    private String titleText = "titleText";
    
    //private Vector glossaryVector;
    
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
    private Image lastScreen = null;
    private PodcastsLoader podcastsLoader= null;
    //</editor-fold>
    
    //list = new List("list", Choice.IMPLICIT);
    //list.setCommandListener(this);
    
    
    
    public PlayerForm (Display display, Displayable previous, Parser parser, PodcastsLoader pl)  {
        //<editor-fold defaultstate="collapsed" desc=" General Initialization ">
        int vspace, vborder = 1, hborder = 1;
        
        vspace = fontMedium.getHeight()*1;
        this.display = display;
        this.previousDisplayable = previous;
        
        this.setFullScreenMode(true);
        lastScreen = Image.createImage(getWidth(), getHeight());
        Rectangle rec = new Rectangle(hborder,vborder,this.getWidth()-(hborder*2), this.getHeight()-(vborder*2));
        this.titleTextPainter = new TextPainter(fontMedium, rec.newSetHeight(vspace));
        this.titleTextPainter.setBackgroundColor(0x221111);
        Rectangle mainRec = rec.newSetY(vspace+2).newMoveHeight(-2*vspace-2);
        this.mainTextPainter = new TextPainter(fontSmall, mainRec);
        this.mainTextPainter.setBackgroundColor(0x111122);
        this.helpTextPainter = new TextPainter(fontSmall, rec.newSetHeight(vspace).newMoveY(mainRec.getHeigth()+vspace));
        this.helpTextPainter.setBackgroundColor(0x221111);
        this.helpTextPainter.setFontColor(0x999999);
        
        MediaServices.getMediaServices().addPlayerListener(this);
        

        HybridFile.setParser(parser);
        
        this.setCommandListener(null);
        
        this.screenHandlersVector = new Vector();
        this.addScreenHandler(new GlossaryScreenHandler(display, this));            /* Element 0. */
        this.addScreenHandler(new ListeningScreenHandler(display, this, parser));   /* Element 1. */
        
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
                    
                    if (goForwardFlag){
                        desiredTimeChange+=2;
                        putTitleNms("MOVE "+ desiredTimeChange + " sec.", 500);
                    }
                    if (goBackFlag){
                        desiredTimeChange-=2;
                        putTitleNms("MOVE "+ desiredTimeChange + " sec.", 500);
                    }
                    
                    if (titleTimeCounter>0){   /* Un resumen de mensaje tiene que ser mostrado. */
                        titleTimeCounter--;     /* Se consume el tiempo durante el cual se muestra. */
                        if (titleTimeCounter==1){ /* Cuando se está a punto de dejar de mostrar el mensaje... */
                            
                            if (timeChangeInProgress){
                                timeChangeInProgress = false;
                                try{
                                    //MediaServices.getMediaServices().movePosition(desiredTimeChange);
                                    int new_position;
                                    new_position = baseTimeForChange + desiredTimeChange;
                                    if (new_position < 0){
                                        requestActionForThePreStartOfTheCurrentSong();
                                    }else if (new_position>MediaServices.getMediaServices().getDurationSeconds()){
                                        requestActionForEndOfTheCurrentSong();
                                    }else{
                                        MediaServices.getMediaServices().setPosition(new_position);
                                        if(stateBeforeMoving==true){
                                            MediaServices.getMediaServices().play();
                                        }
                                    }
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                                desiredTimeChange = 0;
                            }
                            
                        }   
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
                int i=0;
                while(stopThread==false){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        //ex.printStackTrace();
                    }
                    i=(i>10000)?0:i+1;
                    buildHelpText(i);
                }
            }
        }
        ,"Help Thread").start();
        //</editor-fold>
        this.podcastsLoader = pl;
    }
    
    public synchronized void setGlossary(Vector gv) throws Exception{
        //this.glossaryVector = gv;
        
        Enumeration it = this.getScreenHandlerEnumerator();
        ScreenHandler current;
        while (it.hasMoreElements()){
            current = (ScreenHandler)it.nextElement();
            if (GlossaryScreenHandler.class.isInstance(current)){
                current.setMainElement(gv);
                break;
            }
                    
        }
    }
    
    public synchronized void setListening(String currentListeningPath) throws Exception{
        this.currentListeningPath = currentListeningPath;
        MediaServices.getMediaServices().load(currentListeningPath);
    }
    
    public void addDictionary(String dictionary_path) throws Exception{
        DictionaryScreenHandler nd = new DictionaryScreenHandler(this);
        nd.setMainElement(dictionary_path);
        this.addScreenHandler(nd);
    }

    
    //<editor-fold defaultstate="collapsed" desc=" Screen Handler Methods ">
    private synchronized void addScreenHandler(ScreenHandler sh){
        this.screenHandlersVector.addElement(sh);
        this.screenHandlersVector.trimToSize();
    }

    private synchronized ScreenHandler getScreenHandlerAt(int i){
        this.screenHandlersVector.trimToSize();
        return (ScreenHandler) this.screenHandlersVector.elementAt(i);
    }

    private synchronized int getAmountOfScreenHandler(){
        this.screenHandlersVector.trimToSize();
        return this.screenHandlersVector.size();
    }

    private synchronized Enumeration getScreenHandlerEnumerator(){
        this.screenHandlersVector.trimToSize();
        Enumeration it = this.screenHandlersVector.elements();
        return it;
    }
    //</editor-fold>

    public synchronized void setTranscript(Vector gv) throws Exception{
        
        Enumeration it = this.getScreenHandlerEnumerator();
        ScreenHandler current;
        while (it.hasMoreElements()){
            current = (ScreenHandler)it.nextElement();
            if (ListeningScreenHandler.class.isInstance(current)){
                current.setMainElement(gv);
                break;
            }   
        }
    }
    
    public void buildHelpText(int i){
        
        String[] helpKeys = ((ScreenHandler)(this.getScreenHandlerAt(this.mode))).getKeysHelp();
        helpText =            helpKeys[(i+0)%helpKeys.length] + " * ";
        helpText = helpText + helpKeys[(i+1)%helpKeys.length] + " * ";
        helpText = helpText + helpKeys[(i+2)%helpKeys.length];
        this.repaintIfNecessary();
    }
    
    private void buildTitle(){
        
        if (titleTimeCounter>0) { /* El mensaje debe ser mostrado aún. */
            titleText = ">" + this.messageTitle;
        } else {
            titleText = ((ScreenHandler)(this.getScreenHandlerAt(this.mode))).getName()+ " " + this.timeText;
        }
        this.repaintIfNecessary();
    }
    
    public void paint(Graphics g) {
        g.drawImage(lastScreen, 0, 0, Graphics.TOP|Graphics.LEFT);
    }
    
    protected void keyReleased(int keyCode) {
        this.goBackFlag=false;
        this.goForwardFlag=false;
    }
    
    protected void keyPressed(int keyCode) {
        agileKey = false;
        boolean catched=false;
        
        catched = ((ScreenHandler)this.getScreenHandlerAt(this.mode)).keyPressed(keyCode);
        
        if (catched==false){
            this.modeDefaultKeyPressed(keyCode);
        }
        
        if (agileKey==false){
            this.buildTitle();
            this.repaintIfNecessary();
        }
    }

    private String changeMode(boolean initialize) {
        if (this.getAmountOfScreenHandler()>0){
            if (initialize) {
                this.mode = 0;
            }else{
                this.mode = (mode+1)%this.getAmountOfScreenHandler();
            }
        
            this.resetTranslation();
            ScreenHandler currentSH = ((ScreenHandler)this.getScreenHandlerAt(this.mode));
            currentSH.refreshScreen();
            return currentSH.getName();
        }else{
            return "No screen handler.";
        }
    }

    //<editor-fold defaultstate="collapsed" desc=" Default Key Handling ">
    public void modeDefaultKeyPressed(int keyCode){

        switch(keyCode){

            case PlayerForm.KEY_MISC_LEFT:
                String mn = this.changeMode(false);
                this.putTitleNms("MODE " + mn, 1000);
                this.buildTitle();
                this.repaintIfNecessary();
                break;
            case PlayerForm.KEY_MISC_RIGHT:
                goPreviousForm();
                break;
            default:
                
                switch(this.getGameAction(keyCode)){
                    case PlayerForm.FIRE:
                        this.putTitleNms("PLAY/PAUSE", 1000);
                        try{
                            MediaServices.getMediaServices().playPause(); 
                        }catch(Exception e){
                            this.putTitleNms("ERROR PLAY/PAUSE", 1000);
                        }    
                        break;
                    case PlayerForm.DOWN:
                        this.putTitleNms("DOWN LINE", 1000);
                        yTranslation +=3;
                        break;
                    case PlayerForm.UP:
                        this.putTitleNms("UP LINE", 1000);
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
                        this.putTitleNms("MOVE "+ this.desiredTimeChange + " sec.", 500);
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
                        this.putTitleNms("MOVE "+ this.desiredTimeChange + " sec.", 500);
                        this.goBackFlag=true;
                        agileKey = true;
                        break;
                    default:
                        this.putTitleNms("NOT USED (" + keyCode + ", " + this.getGameAction(keyCode)+ ")", 1);
                        break;
                }
        }
    }
    
    //</editor-fold>
    
    public void playerUpdate(Player pl, String str, Object obj) {
        long current_sec;
        long duration_sec;
        
        current_sec =  (pl.getMediaTime()/MediaServices.TIME_FACTOR);
        duration_sec = (pl.getDuration() /MediaServices.TIME_FACTOR);
        this.setTimeText(current_sec, duration_sec);
        ((ScreenHandler)this.getScreenHandlerAt(this.mode)).playerUpdate(pl, str, new Integer((int)current_sec));
        if ((duration_sec>0) && (current_sec >= (duration_sec -1)) && (pl.getState()==Player.STARTED)){
            this.requestActionForEndOfTheCurrentSong();
        }
    }
    
    public void setTimeText(long time, long duration){
        this.timeText = Parser.sec2hoursShort(time) + "/" + Parser.sec2hoursShort(duration);
        buildTitle();
    }
      
    public synchronized void setText(String text){
        this.mainTextPainter.setText(text);
        this.repaintIfNecessary();
    }
    
    private void goPreviousForm(){
        display.setCurrent(previousDisplayable); /* Return. */
    }
    
    public void putTitleNms(String title, int mseconds) {
        this.messageTitle = title;
        this.titleTimeCounter = mseconds/100;
    }
    
    public Displayable getDisplayable(){
         return this;
    }
    
    public void resetTranslation(){
        this.yTranslation = 0;
    }
    
    public synchronized void repaintIfNecessary(){
        Graphics g = lastScreen.getGraphics();        
        
        titleTextPainter.paintText(g, this.titleText);
        mainTextPainter.setTranslation(yTranslation);
        mainTextPainter.paintTextComplex(g, true);
        helpTextPainter.paintText(g, this.helpText);
        this.repaint();
    }


    private void requestActionForEndOfTheCurrentSong(){
        this.podcastsLoader.loadPodcast(PodcastsLoader.NEXT);
    }

    private void requestActionForThePreStartOfTheCurrentSong(){
        this.podcastsLoader.loadPodcast(PodcastsLoader.PREVIOUS);
    }

}

