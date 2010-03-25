/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import java.io.IOException;
import java.util.Vector;
import mediaservicespackage.MediaServices;
import persistencepackage.*;

/**
 *
 * @author Mauricio
 */
public class MarksManager {
    private Parser parser;
    private Vector marksVector;
    private TupleFinder tupleFinder;
    private int counter=0;
    
    public MarksManager(Parser parser){
        this.parser = parser;
        this.marksVector = new Vector();
        this.tupleFinder = new TupleFinder(marksVector);
    }

    public void setMarks(Vector gv){
        this.marksVector = gv;
        this.tupleFinder = new TupleFinder(marksVector);
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
        marksVector.addElement(new Tuple("Mark ("+ this.counter++ + ")","<comment, or explanation>",text));
        return text;
    }
    
    public Tuple getMark(long seg) throws Exception{
        String text;
        Tuple tuple;
        text =  Parser.sec2hours(seg);
        tuple = this.tupleFinder.lookFor(new Tuple(null,null,text));
        return tuple;
    }
}
