/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import java.util.Vector;
import javax.microedition.lcdui.Display;
import javax.microedition.media.Player;
import persistencepackage.Tuple;
import tuplesshowerpackage.*;


/**
 *
 * @author Mauricio
 */
public class GlossaryScreenHandler implements ScreenHandler, TuplesShowerInterface{
    private Playerable player;
    private TupleRevelator tupleRevelator;
    private Iterator iterator;
    private Display display;
    private Tuple lastTuple;
    
    public GlossaryScreenHandler (Display display, Playerable player){
        this.display = display;
        this.player = player;
        this.tupleRevelator = new TupleRevelator(this);
    }
    
    public void setMainElement(Object glossary){
        this.iterator = new SequentialIterator((Vector)glossary);
    }
    
    public boolean keyPressed(int keyCode) {
        boolean catched=true;
        switch(keyCode){
            case '0':
                int rmod = tupleRevelator.nextMode();
                player.putTitleNms("REVELATION MODE ("+rmod+")", 1000);
                break;
            case '1': break;
            case '2': break;
            case '3': break;
            case '4': break;
            case '5': break;
            case '6': break;
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

    public void setValues(Tuple tuple) {
        String text;
        this.lastTuple = tuple;
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
    
}
