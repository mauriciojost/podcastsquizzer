package browserpackage;

import java.util.Vector;
import javax.microedition.lcdui.*;
//import org.netbeans.microedition.lcdui.pda.FileBrowser;
import persistencepackage.FileServices;

public class Browser implements CommandListener{
    private Display display;
    private Displayable previousDisplayable;
    private BrowserReadyListener listener;
    private FileBrowser fbrowser;
    private Command backCommand;
    
    /** 
     * This browser selects a file, and returns to a previous Displayable when 
     * an existing Back command is selected. 
     * @param display current display. 
     * @param previous the Displayable where to return when the Back command is selected. 
     * @param listener the listener for the "selected file" event.
     */
    public Browser(Display display, Displayable previous, BrowserReadyListener listener){
        this.fbrowser = new FileBrowser(display);
        
        this.display = display; 
        this.previousDisplayable = previous;
        this.listener = listener;
        
        this.backCommand = new Command("Back", Command.BACK, 0);
        
        this.fbrowser.setTitle("Browser");
        this.fbrowser.setCommandListener(this);
        this.fbrowser.addCommand(FileBrowser.SELECT_FILE_COMMAND);
        this.fbrowser.addCommand(backCommand);
        //this.fbrowser.setFilter("*.*");
    }
    
    public void commandAction(Command command, Displayable displayable) {
        
        if (displayable == this.fbrowser) {
            if (command == FileBrowser.SELECT_FILE_COMMAND) {
                display.setCurrent(this.previousDisplayable);
                this.listener.browserReady(this.fbrowser.getTitle(), this.correctURL(this.fbrowser.getSelectedFileURL()));
            } else if (command == backCommand) {
                display.setCurrent(this.previousDisplayable);
                this.listener.browserReady(this.fbrowser.getTitle(), null);
            }
        }
    }
        
    /** 
     * This title will be returned when the "choosen file" event occurs. 
     */
    public void setTitle(String text){
        this.fbrowser.setTitle(text);
    }
    
    private String correctURL(String path) {
        char fsep;
        String fsep3;
        fsep = FileServices.getFileSeparatorChar(path);
        fsep3 = "" + fsep + fsep + fsep;
        if (path.startsWith("file:"+fsep3)) {
            return path;
        } else {
            return "file:"+ fsep3 + path;
        }
    }
    
    public Displayable getDisplayable(){
        return (Displayable)this.fbrowser;
    }
    
    //public void setExtension(String extension){
    //    this.fbrowser.setFilter(extension);
    //}

    /**
     * Return the Enumeration of the files that match the filter in the given directory.
     */
    public Vector getCurrDirFiles(String currDirName, String filter) {
        return fbrowser.getCurrDirFiles(currDirName, filter);
    }

    public String getCurrentPath(){
        return fbrowser.getCurrentPath();
    }

    public void setCurrentPath(String path){
        if (path!=null){
            fbrowser.setCurrentPath(path);
        }
    }
}
