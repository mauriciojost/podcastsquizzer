package main;

import canvaspackage.Key2Text;
import canvaspackage.Word;
import javax.microedition.media.Player;

public class DictionaryScreenHandler implements ScreenHandler, DictionaryListener{
    private Playerable player;
    private Dictionary fileDictionary;
    private Key2Text key2text;
    private String currentWord = "";
    private String match="";
    
    public DictionaryScreenHandler(Playerable player){
        this.player = player;
        key2text = new Key2Text();
        fileDictionary = new Dictionary(this);
    }
    
    public void setMainElement(Object main_element) throws Exception{
        fileDictionary.setDictionaryFile((String)main_element);
    }
    
    public String getName() {
        return "Dictionary";
    }

    public boolean keyPressed(int keyCode) {
        currentWord = key2text.newKey(keyCode);

        //try{
        fileDictionary.findMeaning(currentWord);
        match = "<searching>";
        this.refreshScreen();
        //}catch(Exception e){
        //    match = "ERROR111 " + e.getMessage();
        //}

        
        return ((keyCode>='0') && (keyCode<='9'));
    }

    public void expressionFound(String meaning) {
        match = meaning;
        this.refreshScreen();
    }

    public void refreshScreen() {   
        try {
            player.setText(Word.BOLD_BLUE+ "Word: " + Word.BOLD_BLUE + currentWord +  " \n" + match);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getHelp() {
        return "Ayuda";
    }

    public String[] getKeysHelp() {
        String[] a = {"Ayuda", "Ayuda"};
        return a;
    }

    public void playerUpdate(Player arg0, String arg1, Object arg2) {
        
    }

    
}
