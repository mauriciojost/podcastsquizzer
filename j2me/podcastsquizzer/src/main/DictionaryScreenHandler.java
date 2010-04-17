package main;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.media.Player;
import persistencepackage.FileServices;

public class DictionaryScreenHandler implements ScreenHandler{
    private static final int UNINITIALIZED_VALUE=-1;
    private String path;
    private int[] index;
    
    public DictionaryScreenHandler(){
        path = null;
    }
    
    
    private void initializeIndex(int[] index){
        int i;
        for(i=0; i<index.length; i++){
            index[i]=UNINITIALIZED_VALUE;
        }
    }
    
    private void createIndex(String path1) throws IOException{
        int current_pos=0, last_pos=0;
        String line="";
        final int GROUPS = (int)('Z'-'A')+1;
        index = new int[GROUPS];
        
        initializeIndex(index);
        
        FileConnection fci = (FileConnection)Connector.open(FileServices.correctURL(path1),Connector.READ);
        InputStream is = (InputStream)fci.openInputStream();
        String cad;
        int datum;
        
        try{
            while (true) {
                datum = is.read();
                if (datum!=-1){ 
                    if ((char)datum=='\n'){
                        processDictionaryLine(index, line, last_pos);
                        last_pos = current_pos;
                        line = new String();
                    }else{
                        cad = String.valueOf((char)datum);
                        line = line + cad;
                    }
                    current_pos++;
                }else{
                    break;
                }
            }

            is.close();
            
        }catch(IOException e){
            is.close();
            throw e;
        }
    }
    
    private void processDictionaryLine(int[] index, String line, int line_init_pos){
        char ch1=(char)-1;
        try{
            line.toUpperCase();
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
    
    public String getLineAt(int i) throws IOException{
        String line="";
        final int GROUPS = (int)('Z'-'A')+1;
        index = new int[GROUPS];
        
        //initializeIndex(index);
        FileConnection fci = (FileConnection)Connector.open(FileServices.correctURL(path),Connector.READ);
        InputStream is = (InputStream)fci.openInputStream();
        String cad;
        int datum;
        
        is.skip(i);
        
        try{
            while (true) {
                datum = is.read();
                if (datum!=-1){ 
                    if ((char)datum=='\n'){
                        is.close();
                        return line;
                    }else{
                        cad = String.valueOf((char)datum);
                        line = line + cad;
                    }
                }else{
                    break;
                }
            }
            is.close();
            return line;
        }catch(IOException e){
            is.close();
            throw e;
        }
    }
    
    public String getName() {
        return "Dictionary";
    }

    public boolean keyPressed(int keyCode) {
        return false;
    }

    public void setMainElement(Object main_element) {
        try {
            this.path = (String)main_element;
            createIndex(path);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void refreshScreen() {
        
    }

    public String getHelp() {
        //throw new UnsupportedOperationException("Not supported yet.");
        return "Ayuda";
    }

    public String[] getKeysHelp() {
        String[] a = {"Ayuda", "Ayuda"};
        return a;
    }

    public void playerUpdate(Player arg0, String arg1, Object arg2) {
        
    }

    
}
