package main;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import persistencepackage.FileServices;

public class FileDictionary extends Dictionary{
    public FileDictionary(DictionaryListener dl){
        super(dl);
    }
    
    private static final int UNINITIALIZED_VALUE=-1;

    private InputStream is;
    private int[] dictionaryIndex;



    public void openDictionary(String path) throws Exception{
        FileConnection fci;
        path = "file:///E:/Spanish2.txt"; //hay que borrar esta línea. el problema está en el formato del archivo cuando este está bien en la parte de la raíz del filesystem.
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
        try{
           this.dictionaryIndex = this.createIndex();
        }catch(Exception e){
            throw new Exception("createIndex" + "->" + e.getMessage());
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

    public synchronized String getLinesMatchedAt(String match, int amount_of_lines,int current_thread){
        String line="";
        int ind_primera_letra=7767;
        int pos_archivo_prim_letra=3648;

        if (match==null)
            return NO_MATCH;

        if (match.length()<1)
            return NO_MATCH;
        
        match = match.toUpperCase();
        ind_primera_letra =  match.charAt(0) - 'A';

        try{
            pos_archivo_prim_letra = dictionaryIndex[ind_primera_letra];
        }catch(Exception e){return "2match: " + match + " ind_primera_letra="+ind_primera_letra + " pos_archivo_prim_letra="+pos_archivo_prim_letra +" length: "+dictionaryIndex.length +"\n" + e.getMessage();}

        try{
            is.reset();
            is.skip(pos_archivo_prim_letra);
        }catch(Exception e){return "reset or skip error";}


        while(current_thread == this.currentThread){
            try{
                line = line + getNextLine(is).toUpperCase();    /* Get this line to see if there is a match. */
            }catch(Exception e){return "getNextLine error";}
            if (line.startsWith(match)){                        /* Is there a match? */
                try{
                    for (int r=1;r<amount_of_lines;r++){        /* Yes! Return this line and a few more. */
                        line = line + getNextLine(is).toUpperCase();
                    }
                }catch(Exception e){
                    return line;
                }
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
}
