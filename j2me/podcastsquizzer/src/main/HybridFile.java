package main;

import java.util.Vector;
import mediaservicespackage.MediaServices;
import persistencepackage.*;

public class HybridFile {
    public static final String GLOSSARY_GROUP_SEPARATOR="Vocabulary Notes";
    private static Vector marksVector;
    private static Vector glossaryVector;
    private static Parser parser = new Parser("=");
    
    public static void setParser(Parser p){
        parser = p;
    }
    
    public static void setMarksVector(Vector mv){
        marksVector = mv;
    }
    
    public static void setGlossaryVector(Vector gv){
        glossaryVector = gv;
    }
    
    public static void saveFile(FileActionListener fal, String id) throws Exception{
        String text;
        text = parser.vector2txt(marksVector) + "\n\n\n"+ GLOSSARY_GROUP_SEPARATOR + "\n\n" + parser.vector2txt(glossaryVector);
        String currentPath = MediaServices.getMediaServices().getCurrentPath();        
        String newFilePath = FileServices.getDirectory(currentPath) + FileServices.getFilenameWExtensionFromPath(currentPath) + ".txt";
        FileServices.writeTXTFile(newFilePath, text.getBytes(), fal, id);
    }
            
    
}
