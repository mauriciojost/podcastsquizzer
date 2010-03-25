/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import java.io.IOException;
import java.util.Vector;
import mediaservicespackage.MediaServices;
import persistencepackage.FileServices;
import persistencepackage.Parser;
import persistencepackage.Tuple;

/**
 *
 * @author Mauricio
 */
public class MarksManager {
    private Parser parser;
    private Vector marksVector;
    
    public MarksManager(Parser parser, Vector marksVector){
        this.parser = parser;
    }

    public void saveMarks() throws IOException{
        String text;
        
        marksVector.trimToSize();
        if (marksVector.size()!=0) {
            text = parser.vector2txt(marksVector);

            String currentPath = MediaServices.getMediaServices().getCurrentPath();        
            String newFilePath = FileServices.getDirectory(currentPath) + FileServices.getFilenameWExtensionFromPath(currentPath) + "_.txt";
            FileServices.writeTXTFile(newFilePath, text.getBytes());

        }
    
    }
    
    public String addMarkNow(long seg) throws Exception{
        String text;
        text =  Parser.sec2hours(seg);
        marksVector.addElement(new Tuple("","","["+text+"]"));
        return text;
    }
    
}
