package main;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import persistencepackage.FileServices;

public class Dictionary implements Runnable {
    public static final String NO_MATCH = "<no match>";
    private static final int UNINITIALIZED_VALUE=-1;

    private InputStream is;
    private int[] dictionaryIndex;
    private String path;
    private DictionaryListener dictionaryListener;
    private int currentThread=0;
    private String wordBeingSearched="";

    public Dictionary(DictionaryListener dl){
        this.dictionaryListener = dl;
    }

    public void setDictionaryFile(String dpath) throws Exception{
        this.path = dpath;
        try{
            this.openDictionary(this.path); //EL PROBLEMA ES QUE NUNCA SE CREA EL ARREGLO DEL ÍNDICE DEL DICCIONARIO... YO NO PUEDO MÁS! ME CANSÉ :)
        }catch(Exception e){
            throw new Exception("setDictionaryFile" + "->" + e.getMessage());
        }

        try{
           this.dictionaryIndex = this.createIndex();
        }catch(Exception e){
            throw new Exception("createIndex" + "->" + e.getMessage());
        }
    }

    private void openDictionary(String path) throws IOException{
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

    private synchronized String getLineMatchAt(String match, int current_thread){
        String line="";
        int i=7767;
        int j=3648;

        if (match==null)
            return NO_MATCH;

        if (match.length()<1)
            return NO_MATCH;

        //try{
            match = match.toUpperCase();

        //}catch(Exception e){return "match upper: " + match;}

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

        /*if (!is.markSupported()){
            return "<Error: Marks not supported...>";
        }*/

        
        try{
            is.reset();
            is.skip(j);
        }catch(Exception e){return "reset or skip error";}


        while(current_thread == this.currentThread){
            try{
                line = getNextLine(is).toUpperCase();
            }catch(Exception e){return "getNextLine error";}

            String line2,line3,line4,line5;
            if (line.startsWith(match)){
                try{
                    line2 = getNextLine(is).toUpperCase();
                    line3 = getNextLine(is).toUpperCase();
                    line4 = getNextLine(is).toUpperCase();
                    line5 = getNextLine(is).toUpperCase();

                }catch(Exception e){return "getNextLine getting error";}
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

    public void run() {
        this.currentThread++;
        String result = this.getLineMatchAt(this.wordBeingSearched, this.currentThread);
        this.dictionaryListener.expressionFound(result);
    }

    
    public void findMeaning(String expression){
        this.wordBeingSearched = expression;
        Thread ne = new Thread(this);
        ne.setPriority(Thread.MIN_PRIORITY);
        ne.start();

    }
}
