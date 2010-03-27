/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package textboxpackage;

import javax.microedition.lcdui.*;


/**
 *
 * @author Mauricio
 */
public class TextBoxForm implements CommandListener{
    private Display display;
    private Displayable previousDisplayable;
    private TextBoxFormReadyListener listener;
    private TextBox tbox;
    
    private Command backCommand;
    private Command okCommand;
    
    /** 
     * This browser selects a file, and returns to a previous Displayable when 
     * an existing Back command is selected. 
     * @param display current display. 
     * @param previous the Displayable where to return when the Back command is selected. 
     * @param listener the listener for the "selected file" event.
     */
    public TextBoxForm(Display display, Displayable previous, TextBoxFormReadyListener listener){
        
        this.tbox = new TextBox("TextBox", null, 1000, TextField.ANY);
        
        this.display = display; 
        this.previousDisplayable = previous;
        this.listener = listener;
        
        this.backCommand = new Command("Back", Command.BACK, 0);
        this.okCommand = new Command("OK", Command.OK, 0);
        
        this.tbox.setCommandListener(this);
        this.tbox.addCommand(okCommand);
        this.tbox.addCommand(backCommand);
        
    }
    
    public void commandAction(Command command, Displayable displayable) {
        
        if (displayable == this.tbox) {
            if (command == okCommand) {
                display.setCurrent(this.previousDisplayable);
                this.listener.textBoxReady(this.tbox.getTitle(), tbox.getString());
            } else if (command == backCommand) {
                display.setCurrent(this.previousDisplayable);
                this.listener.textBoxReady(this.tbox.getTitle(), null);
            }
        }
    }
        
    /** 
     * This title will be returned when the "choosen file" event occurs. 
     */
    public void setTitle(String text){
        this.tbox.setTitle(text);
    }
    
    public void setText(String text){
        this.tbox.setString(text);
    }
    
    public void setTitleAndText(String title, String initial_text){
        this.setTitle(title);
        this.setText(initial_text);
    }
    
    public Displayable getDisplayable(){
        return (Displayable)this.tbox;
    }
    
}
