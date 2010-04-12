package tuplesshowerpackage;

import miscellaneouspackage.Tuple;
import javax.microedition.lcdui.*;
import persistencepackage.*;

public class TuplesShower implements CommandListener, TuplesShowerInterface {
      
    private Tuple currentTuple;
    private Iterator iterator;
    private Font smallerFont;
    private TupleRevelator tupleRevelator;
    private Display display;
    private Displayable previousDisplayable;
    
    private Command backCommand;
    
    private Command previousCommand;
    private Command nextCommand;
    private Command showCommand;
    private Command switchCommand;
    
    private Form tsForm;
    private StringItem firstItem;
    private StringItem secondItem;
    private StringItem thirdItem;

    
    public TuplesShower(Display display, Displayable previousDisplayable, Tuple names) {    
        this(display, previousDisplayable, names, null);
    }
    
    
    public TuplesShower(Display display, Displayable previousDisplayable, Tuple names, Iterator iterator) {    
        this.getForm();
        this.setNames(names);
        this.setIterator(iterator);
        this.tupleRevelator = new TupleRevelator(this);
        this.display = display;
        this.previousDisplayable = previousDisplayable;
    }
    
    public void setIterator(Iterator iterator) {
        this.iterator = iterator;
    }
    
    public void initialize() {
        if (iterator!=null) 
            iterator.reinitialize();
    }
    public void setNames(Tuple names) {
        this.getFirstItem().setLabel(names.getKey());
        this.getSecondItem().setLabel(names.getValue());
        this.getThirdItem().setLabel(names.getExtra());
    }
    public void setValues(Tuple values) {
        this.getFirstItem().setText(values.getKey());
        this.getSecondItem().setText(values.getValue());
        this.getThirdItem().setText(values.getExtra());
    }
    public void commandAction(Command command, Displayable displayable) {                                               
        
        if (displayable == tsForm) {
            if (command == nextCommand) {         
                if (iterator!=null)
                    this.tupleRevelator.setTuple(this.iterator.getNext());
            } else if (command == this.previousCommand) {                                          
                if (iterator!=null)
                    this.tupleRevelator.setTuple(this.iterator.getPrevious());
            } else if (command == this.switchCommand) {                                          
                this.tupleRevelator.nextMode();
            } else if (command == showCommand) {                                          
                this.tupleRevelator.nextRevelation();
            } else if (command == this.backCommand) {                                          
                display.setCurrent(this.previousDisplayable);
            }                                                  
        }        
    }                                
    
    public Tuple getCurrentTuple() {
        if (this.currentTuple == null) {
            this.currentTuple = new Tuple("","");
        }
        return this.currentTuple;
    }
    public void setCurrentTuple(Tuple tuple){
        this.currentTuple = tuple;
    }
    public Command getExitCommand() {
        if (backCommand == null) {                                 
            backCommand = new Command("Exit", Command.EXIT, 0);                                   
        }                         
        return backCommand;
    }   
    public Form getForm() {
        if (tsForm == null) {                                 
            tsForm = new Form("Bienvenido", new Item[] { getFirstItem(), getSecondItem(), getThirdItem()});                                    
            tsForm.addCommand(getNextCommand());
            tsForm.addCommand(getPreviousCommand());
            tsForm.addCommand(getShowCommand());
            tsForm.addCommand(getSwitchCommand());
            tsForm.addCommand(getBackCommand());
            tsForm.setCommandListener(this);                                  
        }                         
        return tsForm;
    }
    public StringItem getFirstItem() {
        if (firstItem == null) {                                 
            firstItem = new StringItem("First", "", Item.PLAIN);                                    
            firstItem.setFont(this.getSmallFont());                                  
        }                         
        return firstItem;
    }
    public Command getNextCommand() {
        if (nextCommand == null) {                                 
            nextCommand = new Command("Next", Command.BACK, 3);                                   
        }                         
        return nextCommand;
    }
    public Command getPreviousCommand() {
        if (previousCommand == null) {                                 
            previousCommand = new Command("Previous", Command.BACK, 3);                                   
        }                         
        return previousCommand;
    }
    public Command getBackCommand() {
        if (backCommand == null) {                                 
            backCommand = new Command("Back", Command.BACK, 3);                                   
        }                         
        return backCommand;
    }
    public StringItem getSecondItem() {
        if (secondItem == null) {                                 
            secondItem = new StringItem("Second", "");                                    
            secondItem.setFont(this.getSmallFont());                                  
        }                         
        return secondItem;
    }
    public Command getShowCommand() {
        if (showCommand == null) {                                 
            showCommand = new Command("Show", Command.OK, 3);                                   
        }                         
        return showCommand;
    }
    public Command getSwitchCommand() {
        if (switchCommand == null) {                                 
            switchCommand = new Command("Switch", Command.EXIT, 0);                                   
        }                         
        return switchCommand;
    }
    public Font getSmallFont() {
        if (smallerFont == null) {                                 
            smallerFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        }                         
        return smallerFont;
    }
    public StringItem getThirdItem() {
        if (thirdItem == null) {                                 
            thirdItem = new StringItem("Third", "");                                    
            thirdItem.setFont(this.getSmallFont());                                  
        }                         
        return thirdItem;
    }
    
    public Displayable getDisplayable(){
        return (Displayable)this.getForm();
    }
    
}
