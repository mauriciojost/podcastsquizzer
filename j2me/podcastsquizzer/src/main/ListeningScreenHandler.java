
package main;

import java.util.Vector;
import javax.microedition.lcdui.Display;
import javax.microedition.media.Player;
import mediaservicespackage.*;
import persistencepackage.*;
import textboxpackage.*;
import tuplesshowerpackage.*;

public class ListeningScreenHandler implements ScreenHandler, TuplesShowerInterface, FileActionListener, TextBoxFormReadyListener {
    private Playerable player;
    private TupleRevelator tupleRevelator;
    private TextBoxForm textBoxForm;
    private Display display;
    private Tuple lastTuple = new Tuple("","","");;
    
    private MarksManager marksManager;
    
    /*
     * Ir para adelante o para atrás en los marks también cambia el tema. 
     * Los comentarios sólo funcionan si se detiene la reproducción. 
     * Ensayo?
     * No se debe mostrar nunca el tiempo. Eso debe estar oculto al usuario.
     */
    
    public ListeningScreenHandler(Display display, Playerable player, Parser parser){
        this.display = display;
        this.player = player;
        this.marksManager = new MarksManager(parser);
        this.textBoxForm = new TextBoxForm(display, player.getDisplayable(), this);
        this.tupleRevelator = new TupleRevelator(this);
        //public TextBoxForm(Display display, Displayable previous, TextBoxFormReadyListener listener){
    }
    
    public String[] getKeysHelp() {
        String[] ret = {    
                        "(1)PREVIOUS","(2)APPLY","(3)NEXT",
                        "(4)KEY COMMENT","(5)VALUE COMMENT","(6)NEW MARK",
                        "(7)PREV","(8)REV. MODE","(9)NEXT",
                        "(*)","(0)SAVE","(#)",
                        "(ARROWS)AUDIO","(BT)CH. MODE"
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
                    
                    marksManager.saveMarks(this, "saveMarks");
                }catch(Exception e){
                    player.putTitleNms("MARKS NOT SAVED", 10000);
                    e.printStackTrace();
                }
                break;
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc=" 1 PREV ">
            case '1':
                try {
                    player.putTitleNms("PREVIOUS MARK", 1000);

                    Tuple tupleScreen;
                    String curr = marksManager.getPrevious(true).getKey();  // Retroceder y obtener el nuevo actual.
                    String com = marksManager.getNext(false).getKey();     // Obtener el previo (sin retroceso).

                    tupleScreen = new Tuple(curr,com,"");

                    this.tupleRevelator.setTuple(tupleScreen);
                } catch (Exception ex) {
                    player.putTitleNms("MARK ERROR", 1000);
                    ex.printStackTrace();
                }
                break;
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc=" 2 APPLY ">
            case '2':
                try {
                    Tuple tupleScreen;
                    player.putTitleNms("APPLIED MARK", 1000);

                    String curr = marksManager.getNext(true).getKey();      // Avanzar y obtener el nuevo actua.
                    marksManager.applyTimeToCurrentMark(MediaServices.getMediaServices().getPositionSeconds());
                    String com = marksManager.getNext(false).getKey(); // Obtener el previo (sin retroceso).

                    tupleScreen = new Tuple(curr,com,"");
                    
                    this.tupleRevelator.setTuple(tupleScreen);
                } catch (Exception ex) {
                    player.putTitleNms("MARK ERROR", 1000);
                    ex.printStackTrace();
                }
                break;
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc=" 3 NEXT ">
            case '3':
                try {
                    player.putTitleNms("NEXT MARK", 1000);

                    Tuple tupleScreen;
                    String curr = marksManager.getNext(true).getKey();      // Avanzar y obtener el nuevo actua.
                    String com = marksManager.getNext(false).getKey(); // Obtener el previo (sin retroceso).

                    tupleScreen = new Tuple(curr,com,"");

                    this.tupleRevelator.setTuple(tupleScreen);
                } catch (Exception ex) {
                    player.putTitleNms("MARK ERROR", 1000);
                    ex.printStackTrace();
                }
                break;
                
                
                
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc=" 4 KEYC ">
            case '4':
                try{
                    MediaServices.getMediaServices().pause();
                }catch(Exception e){e.printStackTrace();}
                this.textBoxForm.setTitle("KeyComment");
                try {
                    this.textBoxForm.setText(marksManager.getCurrent().getKey());
                } catch (Exception ex) {
                    this.textBoxForm.setText("");
                    ex.printStackTrace();
                }
                display.setCurrent(this.textBoxForm.getDisplayable());

                break;
                
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc=" 5 VALUEC ">
            case '5':
                try {
                    MediaServices.getMediaServices().pause();
                }catch(Exception e){e.printStackTrace();}
                this.textBoxForm.setTitle("ValueComment");
                try {
                    this.textBoxForm.setText(marksManager.getCurrent().getValue());
                } catch (Exception ex) {
                    this.textBoxForm.setText("");
                    ex.printStackTrace();
                }
                display.setCurrent(this.textBoxForm.getDisplayable());

                break;
            
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc=" 6 NEWMARK ">
            case '6':
                try{
                    String mark = marksManager.addMarkNow(MediaServices.getMediaServices().getPositionSeconds());
                    //agileKey = true;
                    player.putTitleNms("NEW MARK: " + mark, 1000);
                }catch(Exception e){
                    player.putTitleNms("ERROR ADDING MARK...",1000);
                }
                break;
            

                
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc=" 7 PREV ">
            case '7':
                try {
                    
                    
                    player.putTitleNms("PREVIOUS MARK", 1000);
                    Tuple prev = marksManager.getPrevious(true);
                    this.tupleRevelator.setTuple(prev);
                    
                    try{
                        MediaServices.getMediaServices().setPosition(Parser.hours2sec(prev.getExtra()));
                    }catch(Exception e){e.printStackTrace();}
                    
                } catch (Exception ex) {
                    player.putTitleNms("MARK ERROR", 1000);
                    ex.printStackTrace();
                }
                break;
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc=" 8 REV.MODE. ">
            case '8':
                this.tupleRevelator.nextMode();
                player.putTitleNms("REV. ("+this.tupleRevelator.getCurrentModeName()+")", 1000);
                break;
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc=" 9 NEXT ">
            case '9':
                try {
                    
                    player.putTitleNms("NEXT MARK", 1000);                    
                    Tuple next = marksManager.getNext(true);
                    this.tupleRevelator.setTuple(next);
                    
                    try{
                        MediaServices.getMediaServices().setPosition(Parser.hours2sec(next.getExtra()));
                    }catch(Exception e){e.printStackTrace();}
                        
                } catch (Exception ex) {
                    player.putTitleNms("MARK ERROR", 1000);
                    ex.printStackTrace();
                }
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

    public void setValues(Tuple tuple) {
        String str;
        this.lastTuple = tuple;
        str =   "*Key \n" + 
                tuple.getKey() + 
                "\n*Value \n" + 
                tuple.getValue() +
                "\n*Extra \n" + 
                tuple.getExtra();
        
        this.player.setText(str);
    }


    public void writeOperationReady(String id, String path, boolean operation_successfully) {
        if(operation_successfully && (id.compareTo("saveMarks")==0)) {
            player.putTitleNms("MARKS SAVED", 2000);
        }else{
            player.putTitleNms("MARKS NOT SAVED", 2000);
        }
    }
    
    public void textBoxReady(String title, String text) {
        if (title.compareTo("ValueComment")==0){
            if (text!=null){
                try {
                    marksManager.getCurrent().setValue(text);
                    this.tupleRevelator.setTuple(marksManager.getCurrent());
                    player.putTitleNms("VALUE CHANGED", 1000);
                } catch (Exception ex) {
                    player.putTitleNms("ERROR CHANGING...", 1000);
                    ex.printStackTrace();
                }
                
            }
        } else if (title.compareTo("KeyComment")==0){
            if (text!=null){
                try {
                    marksManager.getCurrent().setKey(text);
                    this.tupleRevelator.setTuple(marksManager.getCurrent());
                    player.putTitleNms("KEY CHANGED", 1000);
                } catch (Exception ex) {
                    player.putTitleNms("ERROR CHANGING...", 1000);
                    ex.printStackTrace();
                }
                
            }
        }
        
    }

    public void playerUpdate(Player pl, String str, Object obj){
        
        long current;
        Tuple t;
        
        current = (pl.getMediaTime()/MediaServices.TIME_FACTOR); 
        
        if (MediaServices.getMediaServices().isItPlaying()){
            try{
                t = this.marksManager.getMark(current);
                this.player.resetTranslation();
                this.tupleRevelator.setTuple(t);
                //player.repaint();
            }catch(Exception e){
                e.printStackTrace();
            }
        }   
    }

    public void setMainElement(Object vector) {
        this.marksManager.setMarks((Vector)vector);
    }

    public void refreshScreen() {
        this.setValues(this.lastTuple);
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

    
}
