package main;

import canvaspackage.Word;
import miscellaneouspackage.Tuple;
import java.util.Vector;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Image;
import javax.microedition.media.Player;
import mediaservicespackage.*;
import persistencepackage.*;
import textboxpackage.*;
import tuplesshowerpackage.*;

public class ListeningScreenHandler implements ScreenHandler, TuplesShowerInterface, FileActionListener, TextBoxFormReadyListener {
    private static final String EMPTY_STRING = Word.NORMAL_RED + "<empty>";
    private static final Tuple EMPTY_TUPLE = new Tuple(EMPTY_STRING,EMPTY_STRING,EMPTY_STRING);
    private static final int LISTENING_MODE_ANIMATION = 0;
    private static final int LISTENING_MODE_ESSAY = 1;
    private static final int LISTENING_NUM_OF_MODES = 2;
    private static final String[] modeNames = {"PLAYING","ESSAYING"};
    
    private Playerable player;
    private TupleRevelator tupleRevelator;
    private TextBoxForm textBoxForm;
    private Display display;
    private Tuple lastTuple = EMPTY_TUPLE;
    
    private MarksManager marksManager;
    private int mode = LISTENING_MODE_ANIMATION;
    private String mainText = "";
    
    /*
     * Ir para adelante o para atrás en los marks también cambia el tema. 
     * Los comentarios sólo funcionan si se detiene la reproducción. 
     * Ensayo?
     * No se debe mostrar nunca el tiempo. Eso debe estar oculto al usuario.
     */
    
    public ListeningScreenHandler(Display display, Playerable player, Parser parser){
        this.display = display;
        this.player = player;
        
        this.textBoxForm = new TextBoxForm(display, player.getDisplayable(), this);
        this.tupleRevelator = new TupleRevelator(this);

        this.setMainElement(new Vector());
        this.refreshScreen();
        
    }
    
    public String[] getKeysHelp() {
        String[] ret = {    
                        "(1)NADA      ",
                        "(2)MODE      ",
                        "(3)NADA      ",
                        "(4)KEY COMMENT",
                        "(5)VALUE COMMENT",
                        "(6)NEW MARK  ",
                        "(7)PREV      ",
                        "(8)APPLY MARK/REV. MODE",
                        "(9)NEXT      ",
                        "(*)          ",
                        "(0)SAVE      ",
                        "(#)          ",
                        "(ARROWS)AUDIO",
                        "(BT)CH. MODE "
                        };
        return ret;
    }
    
    public boolean keyPressed(int keyCode) {
        boolean catched=true;
        switch(keyCode){
            //<editor-fold defaultstate="collapsed" desc=" 0 SAVE ">
            case '0': 
                try{
                    player.putTitleNms("SAVING MARKS...", 2000);
                    
                    marksManager.saveMarks(this, "saveMarks", player.getPathFileWithExtension(".txt"));
                }catch(Exception e){
                    player.putTitleNms("MARKS NOT SAVED", 10000);
                    if (Definitions.DEBUG_MODE==true){
                        this.setText(e.getMessage());
                    }
                    e.printStackTrace();
                }
                break;
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc=" 1 NADA ">
            case '1':
                break;
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc=" 2 MODE ">
            case '2':
                
                player.putTitleNms(this.updateScreenAccordingToMode(true), 2000);
                break;
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc=" 3 NADA ">
            case '3':
                break;
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc=" 4 KEY COMMENT ">
            case '4':
                try{
                    MediaServices.getMediaServices().pause();
                }catch(Exception e){
                    if (Definitions.DEBUG_MODE==true){this.setText(e.getMessage());}
                    e.printStackTrace();
                }
                this.textBoxForm.setTitle("Transcript");
                try {
                    this.textBoxForm.setText(marksManager.getCurrent().getKey());
                } catch (Exception ex) {
                    this.textBoxForm.setText("");
                    if (Definitions.DEBUG_MODE==true){
                        this.setText(ex.getMessage());
                    }
                    ex.printStackTrace();
                }
                display.setCurrent(this.textBoxForm.getDisplayable());

                break;
                
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc=" 5 VALUE COMMENT ">
            case '5':
                try {
                    MediaServices.getMediaServices().pause();
                }catch(Exception e){
                    if (Definitions.DEBUG_MODE==true){
                        this.setText(e.getMessage());
                    }
                    e.printStackTrace();
                }
                this.textBoxForm.setTitle("Add comment");
                try {
                    this.textBoxForm.setText(marksManager.getCurrent().getValue());
                } catch (Exception ex) {
                    this.textBoxForm.setText("");
                    if (Definitions.DEBUG_MODE==true){
                        this.setText(ex.getMessage());
                    }
                    ex.printStackTrace();
                }
                display.setCurrent(this.textBoxForm.getDisplayable());

                break;
            
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc=" 6 NEW MARK ">
            case '6':
                try{
                    String mark = marksManager.addMarkNow((int)MediaServices.getMediaServices().getPositionSeconds());
                    this.refreshScreen();
                    player.putTitleNms("NEW MARK: " + mark, 1000);
                }catch(Exception e){
                    player.putTitleNms("ERROR ADDING MARK...",1000);
                    if (Definitions.DEBUG_MODE==true){
                        this.setText(e.getMessage());
                    }
                    e.printStackTrace();
                }
                break;
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc=" 7 PREV ">
            case '7':
                switch(mode){
                    case LISTENING_MODE_ESSAY:
                        
                        try {
                            player.putTitleNms("PREVIOUS MARK", 1000);
                            marksManager.getPrevious(true);  
                        } catch (Exception ex) {
                            player.putTitleNms("MARK ERROR", 1000);
                            if (Definitions.DEBUG_MODE==true){
                                this.setText(ex.getMessage());
                            }
                            ex.printStackTrace();
                        }
                        break;
                    case LISTENING_MODE_ANIMATION:
                        try {
                        
                            player.putTitleNms("PREVIOUS MARK", 1000);
                            Tuple prev = marksManager.getPrevious(true);
                            try{
                                MediaServices.getMediaServices().setPosition(Parser.hours2sec(prev.getExtra()));
                            }catch(Exception e){e.printStackTrace();}
                        } catch (Exception ex) {
                            player.putTitleNms("MARK ERROR", 1000);
                            if (Definitions.DEBUG_MODE==true){
                                this.setText(ex.getMessage());
                            }
                            ex.printStackTrace();
                        }
                        break;
                }
                this.refreshScreen();
                break;
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc=" 8 APPLY MARK/NEXT MODE REVELATION ">
            case '8':
                switch(mode){
                    case LISTENING_MODE_ESSAY:
                        try {
                            player.putTitleNms("APPLIED MARK", 1000);
                            player.resetTranslation();
                            marksManager.applyTimeToCurrentMark(MediaServices.getMediaServices().getPositionSeconds());
                            marksManager.getNext(true);  
                            this.refreshScreen();
                        } catch (Exception ex) {
                            player.putTitleNms("MARK ERROR", 1000);
                            if (Definitions.DEBUG_MODE==true){
                                this.setText(ex.getMessage());
                            }
                            ex.printStackTrace();
                        }
                        break;
                    case LISTENING_MODE_ANIMATION:
                        this.tupleRevelator.nextMode();
                        player.putTitleNms("REV. ("+this.tupleRevelator.getCurrentModeName()+")", 1000);
                        break;
                }
                break;
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc=" 9 NEXT ">
            case '9':
                switch(mode){
                    case LISTENING_MODE_ESSAY:
                        try {
                            player.putTitleNms("NEXT MARK", 1000);
                            marksManager.getNext(true);
                        } catch (Exception ex) {
                            player.putTitleNms("MARK ERROR", 1000);
                            if (Definitions.DEBUG_MODE==true){
                                this.setText(ex.getMessage());
                            }
                            ex.printStackTrace();
                        }
                        break;
                    case LISTENING_MODE_ANIMATION:
                        try {
                            player.putTitleNms("NEXT MARK", 1000);                    
                            Tuple next = marksManager.getNext(true);
                            try{
                                MediaServices.getMediaServices().setPosition(Parser.hours2sec(next.getExtra()));
                            }catch(Exception e){e.printStackTrace();}
                        } catch (Exception ex) {
                            player.putTitleNms("MARK ERROR", 1000);
                            if (Definitions.DEBUG_MODE==true){
                                this.setText(ex.getMessage());
                            }
                            ex.printStackTrace();
                        }
                        break;
                }
                this.refreshScreen();
                break;
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc=" DEFAULT ">
            default:
                catched = false;
                break;
            //</editor-fold>  
        }
        
        return catched;
    }

    public String updateScreenAccordingToMode(boolean change_mode){
        if (change_mode){
            mode = (mode + 1) % LISTENING_NUM_OF_MODES;
        }
        switch(mode){
            case LISTENING_MODE_ANIMATION:
                try {
                    lastTuple = marksManager.getCurrent();
                } catch (Exception e) {
                    if (Definitions.DEBUG_MODE==true){
                        this.setText(e.getMessage());
                    }
                    e.printStackTrace();
                }
                break;
            case LISTENING_MODE_ESSAY:
                String applied="", coming="";
                try{
                    applied = marksManager.getPrevious(false).getKey();
                    coming = marksManager.getCurrent().getKey();
                    //coming = marksManager.getNext(false).getKey(); 
                }catch(Exception e){
                    if (Definitions.DEBUG_MODE==true){
                        this.setText(e.getMessage());
                    }
                    e.printStackTrace();
                }
                lastTuple = new Tuple(applied,coming,"");
                break;
        }
        this.setValues(lastTuple);
        return getModeName(mode);
    }
    
    public String getModeName(int mode){
        return modeNames[Math.abs(mode)%LISTENING_NUM_OF_MODES];
    }
    
    public void setValues(Tuple tuple) {
        String str="";
        this.lastTuple = tuple;
        String current = this.marksManager.getCurrentTupleIndex()+1 +"/"+ this.marksManager.getSize();
        if (mode==LISTENING_MODE_ANIMATION){
            str =   Word.BOLD_BLUE+"<"+getModeName(mode)+">\n" + 
                    Word.BOLD_BLUE+"Transcript("+current+") \n" + 
                    tuple.getKey() + 
                    "\n"+Word.BOLD_BLUE+"Comment \n" + 
                    tuple.getValue() +
                    "\n"+Word.BOLD_BLUE+"Time \n" + 
                    tuple.getExtra();
        }else if (mode==LISTENING_MODE_ESSAY){
            str =   Word.BOLD_BLUE+"<"+getModeName(mode)+">\n" +
                    Word.BOLD_BLUE+"Current "+Word.BOLD_BLUE+"(already "+Word.BOLD_BLUE+"applied)("+current+") \n" + 
                    tuple.getKey() + "\n" + 
                    Word.BOLD_BLUE+"Next "+Word.BOLD_BLUE+"(press "+Word.BOLD_BLUE+"when "+Word.BOLD_BLUE+"it's "+Word.BOLD_BLUE+"heard)("+current+") \n" + 
                    tuple.getValue()+ "\n" ;    
        }
        this.setText(str);
    }
    
    


    public void writeOperationReady(String id, String path, boolean operation_successfully) {
        if(operation_successfully && (id.compareTo("saveMarks")==0)) {
            player.putTitleNms("MARKS SAVED", 2000);
        }else{
            player.putTitleNms("MARKS NOT SAVED", 2000);
        }
    }
    
    public void textBoxReady(String title, String text) {
        if (title.compareTo("Add comment")==0){
            if (text!=null){
                try {
                    marksManager.getCurrent().setValue(text);
                    this.tupleRevelator.setTuple(marksManager.getCurrent());
                    player.putTitleNms("VALUE CHANGED", 1000);
                } catch (Exception e) {
                    player.putTitleNms("ERROR CHANGING...", 1000);
                    if (Definitions.DEBUG_MODE==true){
                        this.setText(e.getMessage());
                    }
                    e.printStackTrace();
                }
                
            }
        } else if (title.compareTo("Transcript")==0){
            if (text!=null){
                try {
                    marksManager.getCurrent().setKey(text);
                    this.tupleRevelator.setTuple(marksManager.getCurrent());
                    player.putTitleNms("KEY CHANGED", 1000);
                } catch (Exception e) {
                    player.putTitleNms("ERROR CHANGING...", 1000);
                    if (Definitions.DEBUG_MODE==true){
                        this.setText(e.getMessage());
                    }
                    e.printStackTrace();
                }
                
            }
        }
    }

    public void playerUpdate(Player pl, String str, Object obj){
        int current;
        Tuple t;
        current = ((Integer)obj).intValue();
        if ((MediaServices.getMediaServices().isItPlaying()) && (this.mode==LISTENING_MODE_ANIMATION)){
            try{
                t = this.marksManager.getMark(current);
                this.player.resetTranslation();
                this.tupleRevelator.setTuple(t);
            }catch(Exception e){
                //if (Definitions.DEBUG_MODE==true){this.setText(e.getMessage());}
                e.printStackTrace();
            }
        }   
    }

    public void setMainElement(Object vector) {
        lastTuple=EMPTY_TUPLE;
        //this.marksManager.setMarks((Vector)vector);
        marksManager = new MarksManager((Vector)vector);
        this.refreshScreen();
    }

    public void refreshScreen() {
        this.updateScreenAccordingToMode(false);
    }

    public String getName() {
        return "Listening";
    }

    public String getHelp() {
        String marksKeys = "*MARKS *MODE (useful to manage marks/comments of pieces of the current podcast)"+ "\n" +
         
         "/7 . MOVE BACK (shows the previous mark)"+ "\n" +
         "/8 . ADD THIS MARK HERE (changes the time of the current mark)"+ "\n" +
         "/9 . MOVE FORWARD (shows the following mark)"+ "\n" +
         " "+ "\n" ;

        String animatedKeys = "*ANIMATED *MODE (useful to reproduce the listening and see the marks together)"+ "\n" +
         "/4 . EDIT TRANSCRIPT (edit the transcript of the current mark)"+ "\n" +
         "/5 . EDIT COMMENT (edit the comment of the current mark)"+ "\n" +
         "/6 . ADD MARK (adds a new empty mark in the current time, or replaces the existing one)"+ "\n" +   
         
         "/7 . MOVE BACK (shows the previous mark)"+ "\n" +
         "/8 . APPLY THIS MARK HERE (changes the time of the current mark)"+ "\n" +
         "/9 . MOVE FORWARD (shows the following mark)"+ "\n" +
         
         "/0 . SAVE ALL MARKS (save all the marks into a file)"+ "\n";
         return marksKeys + animatedKeys;
    }

    private void setText(String t){
        this.mainText = t;
        this.player.notifyChangeInScreenHandlerText(this);
    }

    public String getMainStringToPaint() {
        return this.mainText;
    }

    public Image getBackgroundImage() {
        Image backgroundImage=null;
        try {
            backgroundImage = Image.createImage("/help_listening.png");
        } catch (Exception ex) {
            player.putTitleNms("ERROR IMAGE...", 1000);
            if (Definitions.DEBUG_MODE==true){this.setText(ex.getMessage());}
            ex.printStackTrace();
        }
        return backgroundImage;
    }

}
