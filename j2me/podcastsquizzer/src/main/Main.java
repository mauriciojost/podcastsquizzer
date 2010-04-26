package main;

import miscellaneouspackage.Tuple;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import java.util.Vector;
import mediaservicespackage.*;
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
    
    private final String
            DICTIONARY_FILE_KEY = 
            "dictionarypath";       /* Key for the dictionary path. */
    
    //<editor-fold defaultstate="collapsed" desc=" Generated Fields ">//GEN-BEGIN:|fields|0|
    private Command exitCommand;
    private Command browseGlossaryCommand;
    private Command playerCommand;
    private Command tuplesShowerCommand;
    private Command browseSongCommand;
    private Command loadLastListeningCommand;
    private Command browseTranscriptCommand;
    private Command saveLastListeningCommand;
    private Command browseAllCommand;
    private Command browseHybridCommand;
    private Command browseDictionaryCommand;
    private Form form;
    private StringItem hybridItem;
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
                parser);                    /* Creating a new PlayerForm. */
        
        try {
            rmsTuple = new RMSTuple("pq");  /* Loading RMSTuple to save features. */
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        this.playerCommandStatus(false);
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
        if (displayable == form) {//GEN-BEGIN:|7-commandAction|1|37-preAction
            if (command == browseAllCommand) {//GEN-END:|7-commandAction|1|37-preAction
                // write pre-action user code here
//GEN-LINE:|7-commandAction|2|37-postAction
                // write post-action user code here
                
                this.browser.setTitle("All");
                //this.browser.setExtension("mp3");   /* Show only mp3 files. */
                this.switchDisplayable(null,        /* Switching the form. */
                        this.browser.getDisplayable());
                this.playerCommandStatus(false);
            } else if (command == browseDictionaryCommand) {//GEN-LINE:|7-commandAction|3|45-preAction
                // write pre-action user code here
//GEN-LINE:|7-commandAction|4|45-postAction
                // write post-action user code here
                this.browser.setTitle("Dictionary");
                //this.browser.setExtension("txt");   /* Show only txt files. */
                this.switchDisplayable(null,        /* Switching the form. */
                        this.browser.getDisplayable());
            } else if (command == browseHybridCommand) {//GEN-LINE:|7-commandAction|5|43-preAction
                // write pre-action user code here
//GEN-LINE:|7-commandAction|6|43-postAction
                // write post-action user code here
                this.browser.setTitle("Hybrid");
                //this.browser.setExtension("txt");   /* Show only txt files. */
                this.switchDisplayable(null,        /* Switching the form. */
                        this.browser.getDisplayable());
                this.playerCommandStatus(false);
            } else if (command == exitCommand) {//GEN-LINE:|7-commandAction|7|19-preAction
                // write pre-action user code here
                exitMIDlet();//GEN-LINE:|7-commandAction|8|19-postAction
                // write post-action user code here
            } else if (command == loadLastListeningCommand) {//GEN-LINE:|7-commandAction|9|35-preAction
                // write pre-action user code here
//GEN-LINE:|7-commandAction|10|35-postAction
                // write post-action user code here
                
                this.loadLastListeningFile();       /* Load the last used Listening File. */
            } else if (command == playerCommand) {//GEN-LINE:|7-commandAction|11|24-preAction
                // write pre-action user code here
//GEN-LINE:|7-commandAction|12|24-postAction
                // write post-action user code here
                this.switchDisplayable(null,        /* Switching the form. */
                        this.playerForm);
            } else if (command == saveLastListeningCommand) {//GEN-LINE:|7-commandAction|13|39-preAction
                // write pre-action user code here
//GEN-LINE:|7-commandAction|14|39-postAction
                // write post-action user code here
                this.saveLastListeningFile();
            }//GEN-BEGIN:|7-commandAction|15|7-postCommandAction
        }//GEN-END:|7-commandAction|15|7-postCommandAction
        // write post-action user code here
    }//GEN-BEGIN:|7-commandAction|16|
    //</editor-fold>//GEN-END:|7-commandAction|16|



    
    

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
            form = new Form("PodcastsQuizzer", new Item[] { getListeningItem(), getHybridItem(), getOtherItem() });//GEN-BEGIN:|14-getter|1|14-postInit
            form.addCommand(getExitCommand());
            form.addCommand(getPlayerCommand());
            form.addCommand(getLoadLastListeningCommand());
            form.addCommand(getBrowseAllCommand());
            form.addCommand(getSaveLastListeningCommand());
            form.addCommand(getBrowseHybridCommand());
            form.addCommand(getBrowseDictionaryCommand());
            form.setCommandListener(this);//GEN-END:|14-getter|1|14-postInit
            // write post-init user code here
        }//GEN-BEGIN:|14-getter|2|
        return form;
    }
    //</editor-fold>//GEN-END:|14-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: hybridItem ">//GEN-BEGIN:|16-getter|0|16-preInit
    /**
     * Returns an initiliazed instance of hybridItem component.
     * @return the initialized component instance
     */
    public StringItem getHybridItem() {
        if (hybridItem == null) {//GEN-END:|16-getter|0|16-preInit
            // write pre-init user code here
            hybridItem = new StringItem("Hybrid File ", "");//GEN-BEGIN:|16-getter|1|16-postInit
            hybridItem.setFont(getSmallFont());//GEN-END:|16-getter|1|16-postInit
            // write post-init user code here
        }//GEN-BEGIN:|16-getter|2|
        return hybridItem;
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
            playerCommand = new Command("Player", Command.OK, -1);//GEN-LINE:|23-getter|1|23-postInit
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
            browseSongCommand = new Command("Browse Listening", Command.ITEM, 0);//GEN-LINE:|27-getter|1|27-postInit
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
            listeningItem = new StringItem("Listening File ", "");//GEN-BEGIN:|29-getter|1|29-postInit
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
            otherItem = new StringItem("Other files", "");//GEN-BEGIN:|30-getter|1|30-postInit
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

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: browseTranscriptCommand ">//GEN-BEGIN:|32-getter|0|32-preInit
    /**
     * Returns an initiliazed instance of browseTranscriptCommand component.
     * @return the initialized component instance
     */
    public Command getBrowseTranscriptCommand() {
        if (browseTranscriptCommand == null) {//GEN-END:|32-getter|0|32-preInit
            // write pre-init user code here
            browseTranscriptCommand = new Command("Browse Transcript", Command.ITEM, 0);//GEN-LINE:|32-getter|1|32-postInit
            // write post-init user code here
        }//GEN-BEGIN:|32-getter|2|
        return browseTranscriptCommand;
    }
    //</editor-fold>//GEN-END:|32-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: loadLastListeningCommand ">//GEN-BEGIN:|34-getter|0|34-preInit
    /**
     * Returns an initiliazed instance of loadLastListeningCommand component.
     * @return the initialized component instance
     */
    public Command getLoadLastListeningCommand() {
        if (loadLastListeningCommand == null) {//GEN-END:|34-getter|0|34-preInit
            // write pre-init user code here
            loadLastListeningCommand = new Command("Load Last Listening", Command.ITEM, -1);//GEN-LINE:|34-getter|1|34-postInit
            // write post-init user code here
        }//GEN-BEGIN:|34-getter|2|
        return loadLastListeningCommand;
    }
    //</editor-fold>//GEN-END:|34-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: browseAllCommand ">//GEN-BEGIN:|36-getter|0|36-preInit
    /**
     * Returns an initiliazed instance of browseAllCommand component.
     * @return the initialized component instance
     */
    public Command getBrowseAllCommand() {
        if (browseAllCommand == null) {//GEN-END:|36-getter|0|36-preInit
            // write pre-init user code here
            browseAllCommand = new Command("Browse All", Command.ITEM, 0);//GEN-LINE:|36-getter|1|36-postInit
            // write post-init user code here
        }//GEN-BEGIN:|36-getter|2|
        return browseAllCommand;
    }
    //</editor-fold>//GEN-END:|36-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: saveLastListeningCommand ">//GEN-BEGIN:|38-getter|0|38-preInit
    /**
     * Returns an initiliazed instance of saveLastListeningCommand component.
     * @return the initialized component instance
     */
    public Command getSaveLastListeningCommand() {
        if (saveLastListeningCommand == null) {//GEN-END:|38-getter|0|38-preInit
            // write pre-init user code here
            saveLastListeningCommand = new Command("Save as Last Listening", Command.ITEM, 0);//GEN-LINE:|38-getter|1|38-postInit
            // write post-init user code here
        }//GEN-BEGIN:|38-getter|2|
        return saveLastListeningCommand;
    }
    //</editor-fold>//GEN-END:|38-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: browseHybridCommand ">//GEN-BEGIN:|42-getter|0|42-preInit
    /**
     * Returns an initiliazed instance of browseHybridCommand component.
     * @return the initialized component instance
     */
    public Command getBrowseHybridCommand() {
        if (browseHybridCommand == null) {//GEN-END:|42-getter|0|42-preInit
            // write pre-init user code here
            browseHybridCommand = new Command("Browse Hybrid File", Command.ITEM, 0);//GEN-LINE:|42-getter|1|42-postInit
            // write post-init user code here
        }//GEN-BEGIN:|42-getter|2|
        return browseHybridCommand;
    }
    //</editor-fold>//GEN-END:|42-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: browseDictionaryCommand ">//GEN-BEGIN:|44-getter|0|44-preInit
    /**
     * Returns an initiliazed instance of browseDictionaryCommand component.
     * @return the initialized component instance
     */
    public Command getBrowseDictionaryCommand() {
        if (browseDictionaryCommand == null) {//GEN-END:|44-getter|0|44-preInit
            // write pre-init user code here
            browseDictionaryCommand = new Command("Browse Dictionary File", Command.ITEM, 0);//GEN-LINE:|44-getter|1|44-postInit
            // write post-init user code here
        }//GEN-BEGIN:|44-getter|2|
        return browseDictionaryCommand;
    }
    //</editor-fold>//GEN-END:|44-getter|2|

    public Display getDisplay () {
        return Display.getDisplay(this);
    }
    
    public void exitMIDlet() {    
        
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
        if (path==null)
            return;

        if (title.compareTo("Dictionary")==0){
            this.loadDictionaryFile(path);
        }else if (title.compareTo("Hybrid")==0){
            this.loadHybridText(path);
            this.playerCommandStatus(true);
        } else if (title.compareTo("All")==0) {
            String txtPath = "";
            String lisPath;

            lisPath = path;
            txtPath = FileServices.getDirectory(path) + FileServices.getFilenameWExtensionFromPath(path) + ".txt";

            this.loadListening(lisPath);
            this.loadHybridText(txtPath);

            this.playerCommandStatus(true);
        } 
            
    }

    private void loadHybridText(String path1){
        if (path1 != null){

            final String path = FileServices.correctURL(path1);

            Runnable runa = new Runnable(){

                public void run() {
                    String text = "";
                    Vector glossary, transcript;
                    String extension;
                    glossary = new Vector(); transcript = new Vector();
                    
                    try {
                        hybridItem.setText("Loading '"+path+"'..."); 
                        extension = FileServices.getExtensionFromPath(path).toUpperCase();
                        if (extension.compareTo("TXT")!=0)
                            throw new Exception("Invalid hybrid file (must be TXT and is "+ extension +").");
                        text = FileServices.readTXTFile(path, true);
                        parser.txt2vectors(text, glossary, transcript, "-");
                        playerForm.setTranscript(transcript);
                        playerForm.setGlossary(glossary);
                        //hybridItem.setText("OK: Hybrid fil successfully loaded ('"+FileServices.getStandardPath(path)+"').");
                        hybridItem.setText("OK: Hybrid fil successfully loaded ('"+path+"').");
                    } catch (Exception ex) {
                        //hybridItem.setText("CANNOT load hybrid file ('" + FileServices.getStandardPath(path) + "'). "+ ex.getMessage()+". A new one was created.");
                        hybridItem.setText("CANNOT load hybrid file ('" + path + "'). "+ ex.getMessage()+". A new one was created.");
                    }
                    
                }
            };

            Thread hibr = new Thread(runa, "Hilo de carga de archivo híbrido");
            hibr.setPriority(Thread.MAX_PRIORITY);
            hibr.start();
        }
    }
        
    private void loadDictionaryFile(String path1){
        if (path1 != null){

            final String path = FileServices.correctURL(path1);

            Runnable runa = new Runnable(){

                public void run() {
                   try{
                        
                        otherItem.setText("Loading '"+path+"'...");
                        playerForm.addDictionary(path);
                        rmsTuple.addTuple(new Tuple(DICTIONARY_FILE_KEY,path));    
                        rmsTuple.saveRMSTuple();
                        otherItem.setText("OK: dictionary file successfully loaded ('"+path+"').");
                    }catch(Exception e){
                        e.printStackTrace();
                        //otherItem.setText("CANNOT load dictionary file ('" + FileServices.getStandardPath(path) + "'). "+ e.getMessage());
                        otherItem.setText("CANNOT load dictionary file ('" + path + "'). "+ e.getMessage());
                    }
                }
            };
            Thread hibr = new Thread(runa, "Hilo de carga de diccionario");
            hibr.setPriority(Thread.MIN_PRIORITY);
            hibr.start();
            
        }
         
    }
    
    
    private void loadListening(String path){
        String extension;
        if (path != null){
            path = FileServices.correctURL(path);
            try{
                this.listeningItem.setText("Loading '"+path+"'..."); 
                extension = FileServices.getExtensionFromPath(path).toUpperCase();
                if (extension.compareTo("MP3")!=0)
                    throw new Exception("Invalid file for listening (must be MP3 and is "+ extension +").");
                MediaServices.getMediaServices().load(path);
                this.lastfilepath = path;
                this.listeningItem.setText("OK: MP3 file successfully loaded ('"+path+"').");
            }catch(Exception e){
                this.listeningItem.setText("CANNOT load MP3 file ('" +path+ "'). "+ e.getMessage());
            }
        }
    }
    
    /** 
     * It loads the last listening file choosen in the previous session. 
     * If no previous record exists, nothing will be done.
     */
    public void loadLastListeningFile(){ /* CHANGE TO "LOAD LAST ENVIRONMENT" */
        Tuple lfile;
        
        lfile = this.rmsTuple.getTupleByKey(LAST_LISTENING_FILE_KEY);
        if (lfile!=null){
            this.lastfilepath = lfile.getValue();
            this.browserReady("All", lastfilepath);
        }
        
        lfile = this.rmsTuple.getTupleByKey(this.DICTIONARY_FILE_KEY);
        if (lfile!=null){
            this.loadDictionaryFile(lfile.getValue());
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
    
    void playerCommandStatus(boolean status){
        if (status==true){
            this.getForm().addCommand(this.playerCommand);
        }else{
            this.getForm().removeCommand(this.playerCommand);
        }
    }
}
