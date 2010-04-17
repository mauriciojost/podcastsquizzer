package main;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.media.Player;
import miscellaneouspackage.Tuple;
import persistencepackage.FileActionListener;
import persistencepackage.FileServices;
import textboxpackage.TextBoxFormReadyListener;
import tuplesshowerpackage.TuplesShowerInterface;

public class DictionaryScreenHandler implements ScreenHandler, TuplesShowerInterface, TextBoxFormReadyListener, FileActionListener{
    private static final int UNINITIALIZED_VALUE=-1;
    private String path;
    
    public DictionaryScreenHandler(String dict_path){
        path = dict_path;
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
        int[] index = new int[GROUPS];
        
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
                        cad = String.valueOf(datum);
                        line.concat(cad);
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
        char ch1;
        line.toUpperCase();
        ch1 = line.charAt(0);
        
        if ((ch1>='A') && (ch1<='Z')){
            if (index[ch1-'A']==UNINITIALIZED_VALUE){
                index[ch1-'A']=line_init_pos;
            }
        }
    }
    
    public String getName() {
        return "Dictionary";
    }

    public boolean keyPressed(int keyCode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setMainElement(Object main_element) {
        
    }

    public void refreshScreen() {
        
    }

    public String getHelp() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String[] getKeysHelp() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void playerUpdate(Player arg0, String arg1, Object arg2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setValues(Tuple tuple) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void textBoxReady(String title, String text) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void writeOperationReady(String id, String path, boolean operation_successfully) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
