/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import java.util.Vector;
import javax.microedition.lcdui.Display;
import javax.microedition.media.Player;
import persistencepackage.FileActionListener;
import persistencepackage.Tuple;
import textboxpackage.TextBoxForm;
import textboxpackage.TextBoxFormReadyListener;
import tuplesshowerpackage.*;


/**
 *
 * @author Mauricio
 */
public class GlossaryScreenHandler implements ScreenHandler, TuplesShowerInterface, TextBoxFormReadyListener, FileActionListener{
    private Playerable player;
    private TupleRevelator tupleRevelator;
    private Iterator iterator;
    private Display display;
    private Tuple lastTuple = new Tuple("","","");
    private TextBoxForm textBoxForm;
    private int cuentaTerminos = 0;
    
    public GlossaryScreenHandler (Display display, Playerable player){
        this.display = display;
        this.player = player;
        this.tupleRevelator = new TupleRevelator(this);
        this.textBoxForm = new TextBoxForm(display, player.getDisplayable(), this);
    }
    
    public void setMainElement(Object glossary){
        Vector gl = (Vector)glossary;
        this.iterator = new SequentialIterator(gl);
        HybridFile.setGlossaryVector(gl);
    }
    
    public boolean keyPressed(int keyCode) {
        boolean catched=true;
        switch(keyCode){
            case '0':
                player.putTitleNms("SAVING GLOSSARY...", 2000);    
                HybridFile.setGlossaryVector(iterator.getVector());
                try {
                    HybridFile.saveFile(this, "saveGlossary");
                } catch (Exception ex) {
                    player.putTitleNms("ERROR SAVING...", 10000);    
                    ex.printStackTrace();
                }
                break;
            case '1':
                tupleRevelator.nextMode();
                player.putTitleNms("REV. ("+tupleRevelator.getCurrentModeName()+")", 1000);
                break;
            case '2':
                try{
                    
                    Tuple tuple = new Tuple("/New /expression("+cuentaTerminos+")","Explanation", "Examples");
                    tuple.addGroupID("-");
                    cuentaTerminos++;
                    iterator.addNewTuple(tuple);
                    player.putTitleNms("NEW KEY", 1000);
                    tupleRevelator.setTuple(iterator.getCurrent());
                }catch(Exception e){
                    player.putTitleNms("ERROR ADDING...",1000);
                }
                break;
            

            case '3': break;
            
            
            case '4':
                
                this.textBoxForm.setTitle("KeyComment");
                try {
                    Tuple tuple = this.iterator.getCurrent().getACopy();
                    tuple.removeGroupID("-");   
                    this.textBoxForm.setText(tuple.getKey());
                } catch (Exception ex) {
                    this.textBoxForm.setText("");
                    ex.printStackTrace();
                }
                display.setCurrent(this.textBoxForm.getDisplayable());

                break;
                
            case '5':

                this.textBoxForm.setTitle("ValueComment");
                try {
                    this.textBoxForm.setText(this.iterator.getCurrent().getValue());
                } catch (Exception ex) {
                    this.textBoxForm.setText("");
                    ex.printStackTrace();
                }
                display.setCurrent(this.textBoxForm.getDisplayable());

                break;
            
            case '6':
                this.textBoxForm.setTitle("ExtraComment");
                try {
                    this.textBoxForm.setText(this.iterator.getCurrent().getExtra());
                } catch (Exception ex) {
                    this.textBoxForm.setText("");
                    ex.printStackTrace();
                }
                display.setCurrent(this.textBoxForm.getDisplayable());
                break;
            
            case '7':
                player.putTitleNms("PREVIOUS RECORD", 1000);
                if (iterator!=null)
                    tupleRevelator.setTuple(iterator.getPrevious());
                break;
            case '8': 
                player.putTitleNms("REVEAL", 1000);
                tupleRevelator.nextRevelation();
                break;
            case '9':
                player.putTitleNms("NEXT RECORD", 1000);
                if (iterator!=null)
                    tupleRevelator.setTuple(iterator.getNext());
                break;
            default:
                catched = false;
                break;
        }   
        return catched;
    }

    public void setValues(Tuple t) {
        String text;
        this.lastTuple = t;
        Tuple tuple = t.getACopy();
        tuple.removeGroupID("-");
        text =  "*Word: \n"+tuple.getKey()+" \n"+
                "*Explanation: \n"+tuple.getValue()+" \n"+
                "*Examples: \n"+tuple.getExtra()+" \n";
        player.setText(text);
    }

    public void playerUpdate(Player pl, String str, Object obj) {
        
    }

    public void refreshScreen() {
        this.setValues(lastTuple);
    }

    public String getName() {
        return "Glossary";
    }

    public void textBoxReady(String title, String text) {
        if (title.compareTo("ValueComment")==0){
            if (text!=null){
                try {
                    iterator.getCurrent().setValue(text);
                    this.tupleRevelator.setTuple(iterator.getCurrent());
                    player.putTitleNms("VALUE CHANGED", 1000);
                } catch (Exception ex) {
                    player.putTitleNms("ERROR CHANGING...", 1000);
                    ex.printStackTrace();
                }
            }
        } else if (title.compareTo("KeyComment")==0){
            if (text!=null){
                try {
                    iterator.getCurrent().setKey(text);
                    iterator.getCurrent().addGroupID("-");
                    this.tupleRevelator.setTuple(iterator.getCurrent());
                    player.putTitleNms("KEY CHANGED", 1000);
                } catch (Exception ex) {
                    player.putTitleNms("ERROR CHANGING...", 1000);
                    ex.printStackTrace();
                }    
            }
        } else if (title.compareTo("ExtraComment")==0){
            if (text!=null){
                try {
                    iterator.getCurrent().setExtra(text);
                    this.tupleRevelator.setTuple(iterator.getCurrent());
                    player.putTitleNms("EXTRA CHANGED", 1000);
                } catch (Exception ex) {
                    player.putTitleNms("ERROR CHANGING...", 1000);
                    ex.printStackTrace();
                }    
            }
        }
        
    }

    public void writeOperationReady(String id, String path, boolean operation_successfully) {
        if(operation_successfully && (id.compareTo("saveGlossary")==0)) {
            player.putTitleNms("GLOSSARY SAVED", 2000);
        }else{
            player.putTitleNms("GLOSSARY NOT SAVED", 2000);
        }
    }
    
}
