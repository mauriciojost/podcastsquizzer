package persistencepackage;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import java.io.*;

public class FileServices {
    private static char fileSeparatorCh = 'A';
    
    private FileServices(){}
    
    public static String getMemoryCardPath(){
        /* 
         * Dirección interesante: http://www.forum.nokia.com/info/sw.nokia.com/id/bd70d77a-f82f-47ce-a4f8-ca3d868e60a8/MIDP_System_Properties_v1_1.zip.html 
         */
        return System.getProperty("fileconn.dir.memorycard");
    }
    
    public static synchronized String readTXTFile(String path1, boolean replace_odd_chars) throws Exception{
        String cadena = "";

        InputStream is; FileConnection fci;

        try{ fci = (FileConnection)Connector.open(correctURL(path1),Connector.READ);
        }catch(Exception e){throw new Exception("Error while opening the file... "+e.getMessage());}

        try{ is = (InputStream)fci.openInputStream();
        }catch(Exception e){throw new Exception("Error while reading the file... "+e.getMessage());}

        String cad; int datum;

        try{
            byte[] bytes = new byte[is.available()];
            is.read(bytes);
            cadena = new String(bytes);
            bytes = null;
            if (cadena.trim().length()!=0){
                return cadena;
            }
        }catch(Exception e){e.printStackTrace(); }

        try{
            while (true) {
                datum = is.read();
                if (datum!=-1){
                    if (replace_odd_chars){
                        cad = replaceOddChars(datum);
                    }else{
                        cad = "" + (char)datum;
                    }
                    
                   cadena = cadena + cad;
                }else{
                    break;
                }
            }

            is.close();
        }catch(IOException e){
            is.close();
            throw new Exception("Error while getting bytes... " + e.getMessage());
        }
        return cadena;
        
    }    
    
    public static String replaceOddCharsFromAText(String source){
        int i;
        int ch;
        String ret = "";
        for (i=0;i<source.length();i++){
            ch = source.charAt(fileSeparatorCh);
            ret = ret + String.valueOf(ch);
        }
        return ret;
    }
    
    private static String replaceOddChars(int datum){
        String cad;
        switch (datum){
            case '…':
            case '\u0085':
                cad = "...";
                break;
            case '“':
            case '”':
            case '\u0093':
            case '\u0094':
                cad = "\"";
                break;
            case '—':
                cad = "-";
            case '’':
            case '\u0092':
                cad = "'";
                break;
            default:
                cad = "" + (char)datum;
                break;   
        }
        return cad;
    }
    
    public static boolean writeTXTFile(String pat, byte[] dat) throws Exception { 
        return writeTXTFile(pat, dat, null, null);
    }
            
    public static boolean writeTXTFile(String path1, byte[] data1, FileActionListener fl1, String id1) throws Exception { 
        
        final byte[] data = data1;
        final String path = path1;
        final String id = id1;
        final FileActionListener fl = fl1;
        
        Runnable runnab = new Runnable() {
            public void run(){

                javax.microedition.io.Connection c = null; 
                java.io.OutputStream os = null; 

                /* Intento de escritura. */

                try{
                    c = Connector.open(FileServices.correctURL(path), Connector.READ_WRITE); 
                    FileConnection fc = (FileConnection) c; 
                    if (!fc.exists()) 
                        fc.create(); 
                    else 
                        fc.truncate(0); 

                    os = fc.openOutputStream(); 

                    for (int i=0;i<data.length;i++){
                        os.write(data[i]); 
                    }

                    os.flush(); 


                    /* Cierre del archivo. */
                    if (os != null) 
                        os.close(); 
                    if (c != null) 
                        c.close(); 
                    
                    if (fl!=null)
                        fl.writeOperationReady(id, path, true);
                    
                }catch(Exception e){
                    
                    try{
                        if (os != null) 
                            os.close(); 
                        if (c != null) 
                            c.close(); 
                    }catch(Exception er){}
                    
                    if (fl!=null)
                        fl.writeOperationReady(id, path, false);
                    
                }
            }
        };
        
        new Thread(runnab).start();
        
        return false;
    }
    
    public static String correctURL(String path) {
        /*char sep;
        String sep3;
        
        sep = getFileSeparatorChar();
        sep3 = sep + sep + sep + "";
        if (path.startsWith("file:" + sep3)) {
            return path;
        } else {
            return "file:" + sep3 + path;
        }*/
        FileServices.setUpFileSeparatorCharacter(path);
        String rep = FileServices.fileSeparatorCh + "" + FileServices.fileSeparatorCh;
        int ind=0;

        try{
            while ((ind = path.indexOf(rep, ind))!=-1){
                if (path.charAt(ind+2)!=FileServices.fileSeparatorCh){
                    path = path.substring(0, ind) + path.substring(ind+1);
                }else{
                    ind = ind + 2;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return path;
    }
    
    /** Returns the directory given the whole path of a file. 
     * If path="file:\\\c:\directory\file.data" then
     * getDirectory(path) will return "file:\\\c:\directory\"
     * If the path is not correct and the directory cannot be
     * extracted, then a null value is returned. 
     * @param path whole path of a file (like "file:\\\c:\directory\file.data");
     */
    public static String getDirectory(String path){
        
        int last_sep; 
        char sep;
        
        //path = correctURL(path);
        sep = getFileSeparatorChar(path);
        
        last_sep = path.lastIndexOf(sep);
        if (last_sep!=-1){
            return path.substring(0,last_sep+1);
        }else{
            return null;
        }
    }
    
    
    
    /** Returns the filename given the whole path of a file. 
     * If path="file:\\\c:\directory\file.data" then
     * getFileName(path) will return "file.data"
     * If the path is not correct and the directory cannot be
     * extracted, then a null value is returned. 
     * @param path whole path of a file (like "file:\\\c:\directory\file.data");
     */
    public static String getFileName(String path){
        int last_sep;
        char sep;
        
        path = correctURL(path);
        sep = getFileSeparatorChar(path);
        
        last_sep = path.lastIndexOf(sep);
        if (last_sep!=-1){
            return path.substring(last_sep+1);
        }else{
            return null;
        }
    }
    
    
    
    /** Returns the standard path of a file. 
     * If path="file:\\\c:\directory\file.data" then
     * getStandardPath(path) will return "c:\directory\file.data"
     * If the path is not correct and the directory cannot be
     * extracted, then a null value is returned. 
     * @param path whole path of a file (like "file:\\\c:\directory\file.data");
     */
    public static String getStandardPath(String path){
        int last_sep;
        char sep;
        String sep3;
        path = correctURL(path);
        sep = getFileSeparatorChar(path);
        sep3 = "" + (char)sep + (char)sep + (char)sep;
        last_sep = path.indexOf(sep3) + 3;
        
        if (last_sep!=-1){
            return path.substring(last_sep);
        }else{
            return null;
        }
    }
    
    public static String getExtensionFromPath(String path){
        int last_sep;
        last_sep = path.lastIndexOf('.');
        
        if (last_sep!=-1){    
            return path.substring(Math.min(last_sep+1,path.length()));
        }else{
            return null;
        }
    }
    
    public static String getFilenameWExtensionFromPath(String path){
        return FileServices.getFileNameWExtensionFromName(FileServices.getFileName(path));
    }
    
    /** Returns just the name of the file: "name.mp3" -> "name".*/
    public static String getFileNameWExtensionFromName(String filename){
        int last_sep;
        last_sep = filename.lastIndexOf('.');
        
        if (last_sep!=-1){    
            return filename.substring(0,last_sep);
        }else{
            return null;
        }
    }
    
    public static char getFileSeparatorChar(String path){
        setUpFileSeparatorCharacter(path);
        return fileSeparatorCh;
    }
    
    private static void setUpFileSeparatorCharacter(String path){
        if (fileSeparatorCh=='A') {
            if (path.indexOf('\\')!=-1){
                fileSeparatorCh = '\\';
            }else{
                fileSeparatorCh = '/';
            }
        }
    }
}






