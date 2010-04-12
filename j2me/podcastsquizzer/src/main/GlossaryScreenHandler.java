
package main;

import canvaspackage.Word;
import miscellaneouspackage.Tuple;
import java.util.Vector;
import javax.microedition.lcdui.Display;
import javax.microedition.media.Player;
import persistencepackage.*;
import textboxpackage.*;
import tuplesshowerpackage.*;
import miscellaneouspackage.Sorter;
import miscellaneouspackage.TupleAsStringsComparator;


/**
 *
 * @author Mauricio
 */
public class GlossaryScreenHandler implements ScreenHandler, TuplesShowerInterface, TextBoxFormReadyListener, FileActionListener{
    private static final String EMPTY_STRING = Word.NORMAL_RED + "<empty>";
    private static final Tuple EMPTY_TUPLE = new Tuple(EMPTY_STRING,EMPTY_STRING,EMPTY_STRING);
    private Playerable player;
    private TupleRevelator tupleRevelator;
    private Iterator iterator;
    private Display display;
    private Tuple lastTuple = EMPTY_TUPLE;
    private TextBoxForm textBoxForm;
    private int cuentaTerminos = 0;
    private Sorter sorter;    
    
    public GlossaryScreenHandler (Display display, Playerable player){
        this.display = display;
        this.player = player;
        this.tupleRevelator = new TupleRevelator(this);
        this.textBoxForm = new TextBoxForm(display, player.getDisplayable(), this);
        sorter = new Sorter(new TupleAsStringsComparator(Tuple.INDEX_KEY));
        this.setMainElement(new Vector());
        this.refreshScreen();
    }
    
    public void setMainElement(Object glossary){
        Vector gl = (Vector)glossary;
        try{
            sorter.sort(gl);
        }catch(Exception e){
            player.putTitleNms("GLOSSARY NOT SORTED...", 1000);
            e.printStackTrace();
        }
        //this.iterator = new SequentialIterator(gl);
        this.iterator = new Shuffler(gl);
        HybridFile.setGlossaryVector(gl);
        this.lastTuple = iterator.getCurrent();
        this.setValues(lastTuple);
    }
    
    
    public String getHelp() {
        String ret = "*TUPLES *MODE (useful to have a vocabulary's quiz)"+ "\n" +
         "/7 . PREVIOUS TUPLE (shows the previous tuple of the quiz)"+ "\n" +
         "/8 . REVEAL (shows the following part of the tuple, depending on the 'tuple mode')"+ "\n" +
         "/9 . NEXT TUPLE"+ "\n" +
         "/0 . CHANGE TUPLE MODE (changes the 'tuple mode', so the order in which the parts of the tuple are shown will change)"+ "\n" +
         " "+ "\n";
        return ret;
    }

    public String[] getKeysHelp() {
        String ret[] = {"(1)CH. REVEAL MODE","(2)NEW TERM","(3)",
                        "(4)KEY COMMENT","(5)VALUE COMMENT","(6)EXTRA COMMENT",
                        "(7)PREVIOUS","(8)REVEAL","(9)NEXT",
                        "(*)","(0)SAVE","(#)",
                        "(ARROWS)AUDIO","(BT)CH. MODE"
        };
        return ret;       
    }
    
    public boolean keyPressed(int keyCode) {
        boolean catched=true;
        switch(keyCode){
            
            //<editor-fold defaultstate="collapsed" desc=" 0 SAVE">
            case '0':
                Vector vector;
                player.putTitleNms("SAVING GLOSSARY...", 2000);    
                vector = iterator.getVector();
                try{
                    sorter.sort(vector);
                }catch(Exception e){
                    player.putTitleNms("GLOSSARY NOT SORTED...", 1000);
                    e.printStackTrace();
                }
                HybridFile.setGlossaryVector(vector);
                try {
                    HybridFile.saveFile(this, "saveGlossary");
                } catch (Exception ex) {
                    player.putTitleNms("ERROR SAVING...", 10000);    
                    ex.printStackTrace();
                }
                break;
           
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc=" 1 CH. REV. MODE ">
            case '1':
                tupleRevelator.nextMode();
                player.putTitleNms("REV. ("+tupleRevelator.getCurrentModeName()+")", 1000);
                break;
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc=" 2 NEW ">
            case '2':
                try{
                    
                    Tuple tuple = new Tuple("New expression("+cuentaTerminos+")","Explanation", "Examples");
                    tuple.addGroupID("-");
                    cuentaTerminos++;
                    iterator.addNewTuple(tuple);
                    player.putTitleNms("NEW KEY", 1000);
                    tupleRevelator.setTuple(iterator.getCurrent());
                }catch(Exception e){
                    player.putTitleNms("ERROR ADDING...",1000);
                }
                break;
            

            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc=" 3 NADA ">
            case '3': break;
            
            
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc=" 4 KEY COMMENT ">
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
                
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc=" 5 VALUE COMMENT ">
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
            
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc=" 6 EXTRA COMMENT ">
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
            
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc=" 7 PREVIOUS ">
            case '7':
                player.putTitleNms("PREVIOUS RECORD", 1000);
                if (iterator!=null)
                    tupleRevelator.setTuple(iterator.getPrevious());
                break;
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc=" 8 REVEAL ">
            case '8': 
                player.putTitleNms("REVEAL", 1000);
                tupleRevelator.nextRevelation();
                break;
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc=" 9 NEXT ">
            case '9':
                player.putTitleNms("NEXT RECORD", 1000);
                if (iterator!=null)
                    tupleRevelator.setTuple(iterator.getNext());
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

    public void setValues(Tuple t) {
        String text;
        String current="0/0";
        Tuple tuple;
        try{
            current = this.iterator.getCurrentIndex()+1 +"/"+ this.iterator.getVector().size();
            this.lastTuple = t;
            tuple = t.getACopy();
            tuple.removeGroupID("-");
            
        }catch(Exception e){
            tuple = new Tuple("","");
            e.printStackTrace();
        }
        text =  Word.BOLD_BLUE+"Word("+current+"): \n"+tuple.getKey()+" \n"+
                Word.BOLD_BLUE+"Explanation: \n"+tuple.getValue()+" \n"+
                Word.BOLD_BLUE+"Examples: \n"+tuple.getExtra()+" \n";
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
