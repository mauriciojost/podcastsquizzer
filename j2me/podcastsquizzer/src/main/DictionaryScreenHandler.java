package main;

import canvaspackage.Key2Text;
import canvaspackage.Word;
import javax.microedition.media.Player;

public class DictionaryScreenHandler implements ScreenHandler, DictionaryListener{
    private Playerable player;
    private Dictionary dictionary;
    private Key2Text key2text;
    private String expressionBeingSearched = "";
    private String meaningOfTheExpression="";
    private String mainText = "";
    
    public DictionaryScreenHandler(Playerable player){
        this.player = player;
        key2text = new Key2Text();
        dictionary = new Dictionary(this);
    }
    
    public void setMainElement(Object main_element) throws Exception{
        dictionary.openDictionary((String)main_element);
    }
    
    public String getName() {
        return "Dictionary";
    }

    public boolean keyPressed(int keyCode) {
        meaningOfTheExpression = Word.NORMAL_RED+"<searching>";
        this.refreshScreen();

        if (keyCode=='*'){
            dictionary.findMeaning(expressionBeingSearched);
        }else{
            dictionary.findMeaning(expressionBeingSearched=key2text.newKey(keyCode).toUpperCase());
        }
        return ((keyCode>='0')&&(keyCode<='9'));
    }

    public void expressionFound(String meaning) {
        meaningOfTheExpression = "..." + meaning + "...";
        this.refreshScreen();
    }

    public void refreshScreen() {   
        try {
            this.setText(Word.BOLD_BLUE+ "Word: " + Word.BOLD_BLUE + expressionBeingSearched +  " \n" + highlightWord(meaningOfTheExpression, this.expressionBeingSearched));
        } catch (Exception ex) {
            if (Definitions.DEBUG_MODE==true){this.setText(ex.getMessage());}
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

    private String highlightWord(String base_text, String word_to_highlight){

        try{
            if (word_to_highlight.trim().length()==0){
                return base_text;
            }
            int ind = base_text.indexOf(word_to_highlight);
            if (ind!=-1){
                return "" + base_text.substring(0, ind) + (">>") + base_text.substring(ind, ind+word_to_highlight.length())+ ("<<") +base_text.substring(ind+word_to_highlight.length());
            }else{
                throw new Exception();
            }
        }catch(Exception e){
            return base_text;
        }
    }

    private void setText(String t){
        this.mainText = t;
        this.player.setText(this, t);
    }
    public String getMainStringToPaint() {
        return this.mainText;
    }
    
}
