/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import java.util.Vector;
import mediaservicespackage.MediaServices;
import persistencepackage.*;

/**
 *
 * @author Mauricio
 */
public class HybridFile {
    private static Vector marksVector;
    private static Parser marksVectorParser;
    private static Vector glossaryVector;
    private static Parser glossaryVectorParser;
    
    public static void setMarksVector(Vector mv, Parser mvp){
        marksVector = mv;
        marksVectorParser = mvp;
    }
    
    public static void setGlossaryVector(Vector gv, Parser gvp){
        glossaryVector = gv;
        glossaryVectorParser = gvp;
    }
    
    public static void saveFile(FileActionListener fal, String id) throws Exception{
        String text;
        text = marksVectorParser.vector2txt(marksVector) + "\n" + glossaryVectorParser.vector2txt(glossaryVector);
        String currentPath = MediaServices.getMediaServices().getCurrentPath();        
        String newFilePath = FileServices.getDirectory(currentPath) + FileServices.getFilenameWExtensionFromPath(currentPath) + ".txt";
        FileServices.writeTXTFile(newFilePath, text.getBytes(), fal, id);
    }
            
    
}
