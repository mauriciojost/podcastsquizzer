
/**
 * @author Mauri
 * 
 */

package main;


import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import java.util.Vector;
import mediaservicespackage.*;
import tuplesshowerpackage.*;
import browserpackage.*;
import persistencepackage.*;


/** Main MIDlet.
 * It shows the main screens of the application. 
 */
public class Main extends MIDlet implements CommandListener, BrowserReadyListener {
    
    private boolean midletPaused = false;
    private Browser browser;        /* File browser form. */
    private PlayerForm playerForm;  /* Listening player form. */
    
    private Parser parser;          /* General text parser. */
    private RMSTuple rmsTuple;      /* Object that saves tuples by using RMS. */
    private String lastfilepath;    /* Path of the last listening loaded. */
    
    private final String 
            LAST_LISTENING_FILE_KEY = 
            "lastlisteningpath";    /* Key for the last listening path. */
    
    //<editor-fold defaultstate="collapsed" desc=" Generated Fields ">//GEN-BEGIN:|fields|0|
    private Command exitCommand;
    private Command browseGlossaryCommand;
    private Command playerCommand;
    private Command tuplesShowerCommand;
    private Command browseSongCommand;
    private Form form;
    private StringItem glossaryItem;
    private StringItem listeningItem;
    private StringItem otherItem;
    private Font smallFont;
    //</editor-fold>//GEN-END:|fields|0|

    /**
     * The Main constructor.
     */
    public Main() {
        browser = new Browser(
                this.getDisplay(), 
                this.getForm(), 
                this);                      /* Creating browser. */
        parser = new Parser("=");           /* Creating parser. */
        playerForm = new PlayerForm(
                this.getDisplay(), 
                this.getForm(), 
                new Tuple("English", "Explanation", "Examples"), 
                parser);                    /* Creating a new PlayerForm. */
        
        try {
            rmsTuple = new RMSTuple("pq");  /* Loading RMSTuple to save features. */
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        this.loadLastListeningFile();       /* Load the last used Listening File. */
        
    }

    //<editor-fold defaultstate="collapsed" desc=" Generated Methods ">//GEN-BEGIN:|methods|0|
    //</editor-fold>//GEN-END:|methods|0|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: initialize ">//GEN-BEGIN:|0-initialize|0|0-preInitialize
    /**
     * Initilizes the application.
     * It is called only once when the MIDlet is started. The method is called before the <code>startMIDlet</code> method.
     */
    private void initialize() {//GEN-END:|0-initialize|0|0-preInitialize
        // write pre-initialize user code here
//GEN-LINE:|0-initialize|1|0-postInitialize
        // write post-initialize user code here
    }//GEN-BEGIN:|0-initialize|2|
    //</editor-fold>//GEN-END:|0-initialize|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: startMIDlet ">//GEN-BEGIN:|3-startMIDlet|0|3-preAction
    /**
     * Performs an action assigned to the Mobile Device - MIDlet Started point.
     */
    public void startMIDlet() {//GEN-END:|3-startMIDlet|0|3-preAction
        // write pre-action user code here
        switchDisplayable(null, getForm());//GEN-LINE:|3-startMIDlet|1|3-postAction
        // write post-action user code here
    }//GEN-BEGIN:|3-startMIDlet|2|
    //</editor-fold>//GEN-END:|3-startMIDlet|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: resumeMIDlet ">//GEN-BEGIN:|4-resumeMIDlet|0|4-preAction
    /**
     * Performs an action assigned to the Mobile Device - MIDlet Resumed point.
     */
    public void resumeMIDlet() {//GEN-END:|4-resumeMIDlet|0|4-preAction
        // write pre-action user code here
//GEN-LINE:|4-resumeMIDlet|1|4-postAction
        // write post-action user code here
    }//GEN-BEGIN:|4-resumeMIDlet|2|
    //</editor-fold>//GEN-END:|4-resumeMIDlet|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: switchDisplayable ">//GEN-BEGIN:|5-switchDisplayable|0|5-preSwitch
    /**
     * Switches a current displayable in a display. The <code>display</code> instance is taken from <code>getDisplay</code> method. This method is used by all actions in the design for switching displayable.
     * @param alert the Alert which is temporarily set to the display; if <code>null</code>, then <code>nextDisplayable</code> is set immediately
     * @param nextDisplayable the Displayable to be set
     */
    public void switchDisplayable(Alert alert, Displayable nextDisplayable) {//GEN-END:|5-switchDisplayable|0|5-preSwitch
        // write pre-switch user code here
        Display display = getDisplay();//GEN-BEGIN:|5-switchDisplayable|1|5-postSwitch
        if (alert == null) {
            display.setCurrent(nextDisplayable);
        } else {
            display.setCurrent(alert, nextDisplayable);
        }//GEN-END:|5-switchDisplayable|1|5-postSwitch
        // write post-switch user code here
    }//GEN-BEGIN:|5-switchDisplayable|2|
    //</editor-fold>//GEN-END:|5-switchDisplayable|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: commandAction for Displayables ">//GEN-BEGIN:|7-commandAction|0|7-preCommandAction
    /**
     * Called by a system to indicated that a command has been invoked on a particular displayable.
     * @param command the Command that was invoked
     * @param displayable the Displayable where the command was invoked
     */
    public void commandAction(Command command, Displayable displayable) {//GEN-END:|7-commandAction|0|7-preCommandAction
        // write pre-action user code here
        if (displayable == form) {//GEN-BEGIN:|7-commandAction|1|22-preAction
            if (command == browseGlossaryCommand) {//GEN-END:|7-commandAction|1|22-preAction
                // write pre-action user code here
//GEN-LINE:|7-commandAction|2|22-postAction
                // write post-action user code here
                this.browser.setTitle("Glossary"); 
                this.browser.setExtension("txt");   /* Show only txt files. */
                this.switchDisplayable(null,        /* Switching the form. */
                        this.browser.getDisplayable());
                
            } else if (command == browseSongCommand) {//GEN-LINE:|7-commandAction|3|28-preAction
                // write pre-action user code here
//GEN-LINE:|7-commandAction|4|28-postAction
                // write post-action user code here
                this.browser.setTitle("Listening");
                this.browser.setExtension("mp3");   /* Show only mp3 files. */
                this.switchDisplayable(null,        /* Switching the form. */
                        this.browser.getDisplayable());
            } else if (command == exitCommand) {//GEN-LINE:|7-commandAction|5|19-preAction
                // write pre-action user code here
                exitMIDlet();//GEN-LINE:|7-commandAction|6|19-postAction
                // write post-action user code here
            } else if (command == playerCommand) {//GEN-LINE:|7-commandAction|7|24-preAction
                // write pre-action user code here
//GEN-LINE:|7-commandAction|8|24-postAction
                // write post-action user code here
                this.switchDisplayable(null,        /* Switching the form. */
                        this.playerForm);
            }//GEN-BEGIN:|7-commandAction|9|7-postCommandAction
        }//GEN-END:|7-commandAction|9|7-postCommandAction
        // write post-action user code here
    }//GEN-BEGIN:|7-commandAction|10|
    //</editor-fold>//GEN-END:|7-commandAction|10|


    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: exitCommand ">//GEN-BEGIN:|18-getter|0|18-preInit
    /**
     * Returns an initiliazed instance of exitCommand component.
     * @return the initialized component instance
     */
    public Command getExitCommand() {
        if (exitCommand == null) {//GEN-END:|18-getter|0|18-preInit
            // write pre-init user code here
            exitCommand = new Command("Exit", Command.EXIT, 0);//GEN-LINE:|18-getter|1|18-postInit
            // write post-init user code here
        }//GEN-BEGIN:|18-getter|2|
        return exitCommand;
    }
    //</editor-fold>//GEN-END:|18-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: form ">//GEN-BEGIN:|14-getter|0|14-preInit
    /**
     * Returns an initiliazed instance of form component.
     * @return the initialized component instance
     */
    public Form getForm() {
        if (form == null) {//GEN-END:|14-getter|0|14-preInit
            // write pre-init user code here
            form = new Form("PodcastsQuizzer by M. Jost", new Item[] { getGlossaryItem(), getListeningItem(), getOtherItem() });//GEN-BEGIN:|14-getter|1|14-postInit
            form.addCommand(getExitCommand());
            form.addCommand(getBrowseGlossaryCommand());
            form.addCommand(getPlayerCommand());
            form.addCommand(getBrowseSongCommand());
            form.setCommandListener(this);//GEN-END:|14-getter|1|14-postInit
            // write post-init user code here
        }//GEN-BEGIN:|14-getter|2|
        return form;
    }
    //</editor-fold>//GEN-END:|14-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: glossaryItem ">//GEN-BEGIN:|16-getter|0|16-preInit
    /**
     * Returns an initiliazed instance of glossaryItem component.
     * @return the initialized component instance
     */
    public StringItem getGlossaryItem() {
        if (glossaryItem == null) {//GEN-END:|16-getter|0|16-preInit
            // write pre-init user code here
            glossaryItem = new StringItem("Glossary File", "");//GEN-BEGIN:|16-getter|1|16-postInit
            glossaryItem.setFont(getSmallFont());//GEN-END:|16-getter|1|16-postInit
            // write post-init user code here
        }//GEN-BEGIN:|16-getter|2|
        return glossaryItem;
    }
    //</editor-fold>//GEN-END:|16-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: browseGlossaryCommand ">//GEN-BEGIN:|21-getter|0|21-preInit
    /**
     * Returns an initiliazed instance of browseGlossaryCommand component.
     * @return the initialized component instance
     */
    public Command getBrowseGlossaryCommand() {
        if (browseGlossaryCommand == null) {//GEN-END:|21-getter|0|21-preInit
            // write pre-init user code here
            browseGlossaryCommand = new Command("Browse Glossary", Command.ITEM, 0);//GEN-LINE:|21-getter|1|21-postInit
            // write post-init user code here
        }//GEN-BEGIN:|21-getter|2|
        return browseGlossaryCommand;
    }
    //</editor-fold>//GEN-END:|21-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: playerCommand ">//GEN-BEGIN:|23-getter|0|23-preInit
    /**
     * Returns an initiliazed instance of playerCommand component.
     * @return the initialized component instance
     */
    public Command getPlayerCommand() {
        if (playerCommand == null) {//GEN-END:|23-getter|0|23-preInit
            // write pre-init user code here
            playerCommand = new Command("Player", Command.ITEM, -1);//GEN-LINE:|23-getter|1|23-postInit
            // write post-init user code here
        }//GEN-BEGIN:|23-getter|2|
        return playerCommand;
    }
    //</editor-fold>//GEN-END:|23-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: tuplesShowerCommand ">//GEN-BEGIN:|25-getter|0|25-preInit
    /**
     * Returns an initiliazed instance of tuplesShowerCommand component.
     * @return the initialized component instance
     */
    public Command getTuplesShowerCommand() {
        if (tuplesShowerCommand == null) {//GEN-END:|25-getter|0|25-preInit
            // write pre-init user code here
            tuplesShowerCommand = new Command("Shower", Command.ITEM, 0);//GEN-LINE:|25-getter|1|25-postInit
            // write post-init user code here
        }//GEN-BEGIN:|25-getter|2|
        return tuplesShowerCommand;
    }
    //</editor-fold>//GEN-END:|25-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: browseSongCommand ">//GEN-BEGIN:|27-getter|0|27-preInit
    /**
     * Returns an initiliazed instance of browseSongCommand component.
     * @return the initialized component instance
     */
    public Command getBrowseSongCommand() {
        if (browseSongCommand == null) {//GEN-END:|27-getter|0|27-preInit
            // write pre-init user code here
            browseSongCommand = new Command("Browse Listening", Command.ITEM, -1);//GEN-LINE:|27-getter|1|27-postInit
            // write post-init user code here
        }//GEN-BEGIN:|27-getter|2|
        return browseSongCommand;
    }
    //</editor-fold>//GEN-END:|27-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: listeningItem ">//GEN-BEGIN:|29-getter|0|29-preInit
    /**
     * Returns an initiliazed instance of listeningItem component.
     * @return the initialized component instance
     */
    public StringItem getListeningItem() {
        if (listeningItem == null) {//GEN-END:|29-getter|0|29-preInit
            // write pre-init user code here
            listeningItem = new StringItem("Listening File", "");//GEN-BEGIN:|29-getter|1|29-postInit
            listeningItem.setFont(getSmallFont());//GEN-END:|29-getter|1|29-postInit
            // write post-init user code here
        }//GEN-BEGIN:|29-getter|2|
        return listeningItem;
    }
    //</editor-fold>//GEN-END:|29-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: otherItem ">//GEN-BEGIN:|30-getter|0|30-preInit
    /**
     * Returns an initiliazed instance of otherItem component.
     * @return the initialized component instance
     */
    public StringItem getOtherItem() {
        if (otherItem == null) {//GEN-END:|30-getter|0|30-preInit
            // write pre-init user code here
            otherItem = new StringItem("Other File", "");//GEN-BEGIN:|30-getter|1|30-postInit
            otherItem.setFont(getSmallFont());//GEN-END:|30-getter|1|30-postInit
            // write post-init user code here
        }//GEN-BEGIN:|30-getter|2|
        return otherItem;
    }
    //</editor-fold>//GEN-END:|30-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: smallFont ">//GEN-BEGIN:|31-getter|0|31-preInit
    /**
     * Returns an initiliazed instance of smallFont component.
     * @return the initialized component instance
     */
    public Font getSmallFont() {
        if (smallFont == null) {//GEN-END:|31-getter|0|31-preInit
            // write pre-init user code here
            smallFont = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL);//GEN-LINE:|31-getter|1|31-postInit
            // write post-init user code here
        }//GEN-BEGIN:|31-getter|2|
        return smallFont;
    }
    //</editor-fold>//GEN-END:|31-getter|2|

    public Display getDisplay () {
        return Display.getDisplay(this);
    }
    
    public void exitMIDlet() {    
        this.saveLastListeningFile();
        switchDisplayable (null, null);
        destroyApp(true);
        notifyDestroyed();
    }
    
    public void startApp() {
        if (midletPaused) {
            resumeMIDlet ();
        } else {
            initialize ();
            startMIDlet ();
        }
        midletPaused = false;
    }
    
    public void pauseApp() {
        midletPaused = true;
    }
    
    public void destroyApp(boolean unconditional) {
    
    }
    
    /**
     * It's called whenever the Browser returns to this form with its Back
     * command.
     * @param title         name or ID to recognise the purpose's file of this Browser's call. 
     * @param path          path for the choosen file. null if an abort operation was selected. 
     */
    public void browserReady(String title, String path) {
        if (title.compareTo("Glossary")==0) {
            String text = "";
            Vector vector;
            if (path != null){
                try {
                    text = FileServices.readTXTFile(path);
                    this.glossaryItem.setText("TXT file successfully loaded: '"+path+"'.");
                } catch (Exception ex) {
                    this.glossaryItem.setText("Error while loading TXT file: '"+path+"'. " + ex.getMessage());
                }
                vector = this.parser.txt2vector(text);
                //this.ts.setIterator(new SequentialIterator(vector));
                this.playerForm.setGlossaryVectorSequentially(vector);
            }
        }
        if (title.compareTo("Listening")==0) {
            if (path != null){
                try{
                    String txtPath = "";
                    String traPath = "";
                    MediaServices.getMediaServices().load(path);
                    this.listeningItem.setText("MP3 file successfully loaded ("+path+").");
                    this.lastfilepath = path;
                    txtPath = FileServices.getDirectory(path) + FileServices.getFilenameWExtensionFromPath(path) + ".txt";
                    traPath = FileServices.getDirectory(path) + FileServices.getFilenameWExtensionFromPath(path) + "_.txt";
                    this.browserReady("Glossary", txtPath);
                    this.browserReady("Transcript", traPath);
                }catch(Exception e){
                    this.listeningItem.setText("Error while loading MP3 file: '" + path + "'. "+ e.getMessage());
                }
            }
        }
        
        if (title.compareTo("Transcript")==0) {
            String text = "";
            Vector vector;
            if (path != null){
                try {
                    text = FileServices.readTXTFile(path);
                    this.otherItem.setText("Transcript file successfully loaded ("+path+").");
                } catch (Exception ex) {
                    this.otherItem.setText("Error while loading transcript file: '" + path + "'. "+ ex.getMessage());
                }
                vector = this.parser.txt2vector(text);
                this.playerForm.setTranscript(vector);
            }
        }
            
    }
    
    
    /** 
     * It loads the last listening file choosen in the previous session. 
     */
    public void loadLastListeningFile(){
        Tuple lfile;
        
        lfile = this.rmsTuple.getTupleByKey(LAST_LISTENING_FILE_KEY);
        if (lfile!=null){
            this.lastfilepath = lfile.getValue();
            this.browserReady("Listening", lastfilepath);
        }
    }
    
    /** 
     * It saves the last listening file choosen in this session. 
     */
    public void saveLastListeningFile(){
        this.rmsTuple.addTuple(new Tuple(LAST_LISTENING_FILE_KEY,this.lastfilepath));
        try {
            this.rmsTuple.saveRMSTuple();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
   
}
