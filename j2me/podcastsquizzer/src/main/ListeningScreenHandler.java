
package main;

import javax.microedition.lcdui.Display;
import javax.microedition.media.Player;
import mediaservicespackage.MediaServices;
import persistencepackage.FileActionListener;
import persistencepackage.Parser;
import persistencepackage.Tuple;
import textboxpackage.TextBoxForm;
import textboxpackage.TextBoxFormReadyListener;
import tuplesshowerpackage.TupleRevelator;
import tuplesshowerpackage.TuplesShowerInterface;


public class ListeningScreenHandler implements ScreenHandler, TuplesShowerInterface, FileActionListener, TextBoxFormReadyListener {
    private Playerable player;
    private TupleRevelator tupleRevelator;
    private TextBoxForm textBoxForm;
    private Display display;
    
    private MarksManager marksManager;
    
    public ListeningScreenHandler(Display display, Playerable player){
        this.display = display;
        this.player = player;
        this.marksManager = new MarksManager(new Parser("="));
        this.textBoxForm = new TextBoxForm(display, player.getDisplayable(), this);
        //public TextBoxForm(Display display, Displayable previous, TextBoxFormReadyListener listener){
    }
    
    public boolean keyPressed(int keyCode) {
        boolean catched=true;
        switch(keyCode){
            
            case '0': 
                try{
                    player.putTitle("SAVING MARKS...", 2);
                    marksManager.saveMarks(this, "saveMarks");
                    //agileKey = true;
                }catch(Exception e){
                    player.putTitle("MARKS NOT SAVED", 10);
                }
                break;
            
            case '1':
                try {
                    player.putTitle("PREVIOUS MARK", 1);

                    Tuple tupleScreen;
                    String curr = marksManager.getPrevious(true).getKey();  // Retroceder y obtener el nuevo actual.
                    String com = marksManager.getNext(false).getKey();     // Obtener el previo (sin retroceso).

                    tupleScreen = new Tuple(curr,com,"");

                    this.tupleRevelator.setTuple(tupleScreen);
                } catch (Exception ex) {
                    player.putTitle("MARK ERROR", 1);
                }
                break;
            case '2':
                try {
                    Tuple tupleScreen;
                    player.putTitle("APPLIED MARK", 1);

                    String curr = marksManager.getNext(true).getKey();      // Avanzar y obtener el nuevo actua.
                    marksManager.applyTimeToCurrentMark(MediaServices.getMediaServices().getPositionSeconds());
                    String com = marksManager.getNext(false).getKey(); // Obtener el previo (sin retroceso).

                    tupleScreen = new Tuple(curr,com,"");
                    
                    this.tupleRevelator.setTuple(tupleScreen);
                } catch (Exception ex) {
                    player.putTitle("MARK ERROR", 1);
                }
                break;
            case '3':
                try {
                    player.putTitle("NEXT MARK", 1);

                    Tuple tupleScreen;
                    String curr = marksManager.getNext(true).getKey();      // Avanzar y obtener el nuevo actua.
                    String com = marksManager.getNext(false).getKey(); // Obtener el previo (sin retroceso).

                    tupleScreen = new Tuple(curr,com,"");

                    this.tupleRevelator.setTuple(tupleScreen);
                } catch (Exception ex) {
                    player.putTitle("MARK ERROR", 1);
                }
                break;
                
                
                
            case '4':
                if (MediaServices.getMediaServices().isItPlaying()==false){
                    this.textBoxForm.setTitle("KeyComment");
                    try {
                        this.textBoxForm.setText(marksManager.getCurrent().getKey());
                    } catch (Exception ex) {
                        this.textBoxForm.setText("");
                        ex.printStackTrace();
                    }
                    display.setCurrent(this.textBoxForm.getDisplayable());
                }else{
                    player.putTitle("PLAYING...", 1);
                }
                break;
                
            case '5':
                if (MediaServices.getMediaServices().isItPlaying()==false){
                    this.textBoxForm.setTitle("ValueComment");
                    try {
                        this.textBoxForm.setText(marksManager.getCurrent().getValue());
                    } catch (Exception ex) {
                        this.textBoxForm.setText("");
                        ex.printStackTrace();
                    }
                    display.setCurrent(this.textBoxForm.getDisplayable());
                }else{
                    player.putTitle("PLAYING...", 1);
                }
                break;
            
            case '6':
                try{
                    String mark = marksManager.addMarkNow(MediaServices.getMediaServices().getPositionSeconds());
                    //agileKey = true;
                    player.putTitle("NEW MARK: " + mark, 1);
                }catch(Exception e){
                    player.putTitle("ERROR ADDING MARK...",1);
                }
                break;
            

                
            case '7':
                try {
                    if (MediaServices.getMediaServices().isItPlaying()==false){
                        player.putTitle("PREVIOUS MARK", 1);
                        this.tupleRevelator.setTuple(marksManager.getPrevious(true));
                    }else{
                        player.putTitle("PLAYING...", 1);
                    }
                } catch (Exception ex) {
                    player.putTitle("MARK ERROR", 1);
                }
                break;
            case '8':
                int rmod = this.tupleRevelator.nextMode();
                player.putTitle("REVELATION MODE ("+rmod+")", 1);
                break;
            case '9':
                try {
                    if (MediaServices.getMediaServices().isItPlaying()==false){
                        player.putTitle("NEXT MARK", 1);                    
                        this.tupleRevelator.setTuple(marksManager.getNext(true));
                    }else{
                        player.putTitle("PLAYING...", 1);
                    }
                } catch (Exception ex) {
                    player.putTitle("MARK ERROR", 1);
                }
                break;
                
            
            default:
                catched = false;
                break;
        }
        
        return catched;
    }

    public void setValues(Tuple tuple) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public void writeOperationReady(String id, String path, boolean operation_successfully) {
        if(operation_successfully && (id.compareTo("saveMarks")==0)) {
            player.putTitle("MARKS SAVED", 2);
        }else{
            player.putTitle("MARKS NOT SAVED", 2);
        }
    }
    
    public void textBoxReady(String title, String text) {
        if (title.compareTo("ValueComment")==0){
            if (text!=null){
                try {
                    marksManager.getCurrent().setValue(text);
                    this.tupleRevelator.setTuple(marksManager.getCurrent());
                    player.putTitle("VALUE CHANGED", 1);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                
            }
        } else if (title.compareTo("KeyComment")==0){
            if (text!=null){
                try {
                    marksManager.getCurrent().setKey(text);
                    this.tupleRevelator.setTuple(marksManager.getCurrent());
                    player.putTitle("KEY CHANGED", 1);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                
            }
        }
        
        player.repaint();
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
                player.repaint();
            }catch(Exception e){
                e.printStackTrace();
            }
        }   
    }

    public void setMainElement(Object me) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
