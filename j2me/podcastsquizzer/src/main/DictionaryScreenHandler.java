package main;

import canvaspackage.Key2Text;
import canvaspackage.Word;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.media.Player;
import persistencepackage.FileServices;

public class DictionaryScreenHandler implements ScreenHandler{
    private static final int UNINITIALIZED_VALUE=-1;
    private static final String NO_MATCH = "<no match>";
    private Playerable player;
    private String path;
    private int[] dictionaryIndex;
    private Key2Text key2text;
    private InputStream is;
    private String currentWord = "";
    
    public DictionaryScreenHandler(Playerable player){
        path = null;
        this.player = player;
        key2text = new Key2Text();
    }
    
    public void setMainElement(Object main_element) throws Exception{
        this.path = (String)main_element;
        try{
            
            this.openDictionary(this.path); //EL PROBLEMA ES QUE NUNCA SE CREA EL ARREGLO DEL ÍNDICE DEL DICCIONARIO... YO NO PUEDO MÁS! ME CANSÉ :)

        }catch(Exception e){
            throw new Exception("openDictionary" + "->" + e.getMessage());
        }

        try{
           this.dictionaryIndex = this.createIndex();
        }catch(Exception e){
            throw new Exception("createIndex" + "->" + e.getMessage());
        }
    }
    
    private void openDictionary(String path) throws IOException{
        FileConnection fci;
        path = "file:///E:/Spanish.txt"; hay que borrar esta línea. el problema está en el formato del archivo cuando este está bien en la parte de la raíz del filesystem.
        String pathc = FileServices.correctURL(path);
        
        try{
            fci = (FileConnection)Connector.open(pathc,Connector.READ);
        }catch(IOException e){
            throw new IOException("Connector.open, path: '"+ pathc +"'" + "->" + e.getMessage());
        }
        try{
            is = (InputStream)fci.openInputStream();
        }catch(IOException e){
            throw new IOException("fci.openInputStream" + "->" + e.getMessage());
        }
        try{
            is.mark(1);
        }catch(Exception e){
            throw new IOException("is.mark(1)" + "->" + e.getMessage());
        }
        
    }
    
    private int[] createIndex() throws IOException{
        int current_pos=0, last_pos=0;
        int[] index;
        String line="";
        final int GROUPS = (int)('Z'-'A')+1;
        index = new int[GROUPS];
        
        this.initializeIndex(index);
        
        
        String cad;
        int datum;
        
        
        while ((datum = is.read())!=-1) {
            if ((char)datum=='\n'){         /* End of the line? */
                processDictionaryLine(index, line, last_pos);
                last_pos = current_pos;
                line = new String();
            }else{
                cad = String.valueOf((char)datum);
                line = line + cad;
            }
            current_pos++;
        }
        return index;


    }
    
    private void initializeIndex(int[] index){
        for(int i=0; i<index.length; i++)
            index[i]=UNINITIALIZED_VALUE;
    }
    
    private void processDictionaryLine(int[] index, String line, int line_init_pos){
        char ch1=(char)(-1);
        try{
            line = line.toUpperCase();
            ch1 = line.charAt(0);
            if ((ch1>='A') && (ch1<='Z')){
                if (index[ch1-'A']==UNINITIALIZED_VALUE){
                    index[ch1-'A']=line_init_pos+1;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        
    }
        
    public String getLineMatchAt(String match){
        String line="";
        int i=7767;
        int j=3648;
        
        if (match==null)
            return NO_MATCH;
        
        if (match.length()<1)
            return NO_MATCH;
        
        try{
            match = match.toUpperCase();

        }catch(Exception e){return "match upper: " + match;}

        char jj=435;
        try{
            
            jj = match.charAt(0);
            i =  jj - 'A';
        }catch(Exception e){return "43match: " + match + " i="+i + " jj="+jj + " " +e.getMessage();}
        
        try{
            if (i>=this.dictionaryIndex.length){
                return NO_MATCH + ", match: " + match;
            }
            
            j = dictionaryIndex[i];

        }catch(Exception e){return "2match: " + match + " i="+i + " j="+j +" length: "+dictionaryIndex.length +"\n" + e.getMessage();}
        
        if (!is.markSupported()){
            return "<Error: Marks not supported...>";
        }
        
        try{
            is.reset();
        }catch(Exception e){return "reset";}
        try{
            is.skip(j);
        }catch(Exception e){return "skip";}
        

        while(true){
            try{
                line = getNextLine(is).toUpperCase();
            }catch(Exception e){return "getNextLine";}
            
            String line2,line3,line4,line5;
            if (line.startsWith(match)){
                try{
                    line2 = getNextLine(is).toUpperCase();
                    line3 = getNextLine(is).toUpperCase();
                    line4 = getNextLine(is).toUpperCase();
                    line5 = getNextLine(is).toUpperCase();
                    
                }catch(Exception e){return "getNextLine";}
                return line + "\n" + line2 + "\n" + line3 + "\n" + line4 + "\n" + line5;
            }
            try{
                if (match.charAt(0)!=line.charAt(0)){
                    return NO_MATCH + " (match=" + line.charAt(0) + ")";
                }
            }catch(Exception e){
                e.printStackTrace();
                break;
            }
        }
        return NO_MATCH + "112";

    }
    
    private String getNextLine(InputStream is) throws IOException{
        String cad, line="";
        int datum;
        
        while ((datum = is.read())!=-1) {
            if ((char)datum=='\n'){
                return line;
            }else{
                cad = String.valueOf((char)datum);
                line = line + cad;
            }
        }
        return "";
    }
    
    public String getName() {
        return "Dictionary";
    }

    public boolean keyPressed(int keyCode) {
        currentWord = key2text.newKey(keyCode);
        this.refreshScreen();
        
        return ((keyCode>='0') && (keyCode<='9'));
    }

    public void refreshScreen() {
        String match = NO_MATCH;
        //try{
            match = this.getLineMatchAt(currentWord);
        //}catch(Exception e){
        //    match = "ERROR111 " + e.getMessage();
        //}
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
