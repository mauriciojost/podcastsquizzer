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
public class MarksManager{
    private Parser parser;
    private Vector marksVector;
    private TupleFinder tupleFinder;
    private int counter=0;
    private int currentTupleIndex=-1;
    
    public MarksManager(Parser parser){
        this.parser = parser;
        this.marksVector = new Vector();
        this.tupleFinder = new TupleFinder(marksVector);
    }

    public void setMarks(Vector gv){
        this.marksVector = gv;
        this.tupleFinder = new TupleFinder(marksVector);
    }
            
    public void saveMarks(FileActionListener fal, String id) throws Exception{
        String text;
        
        marksVector.trimToSize();
        if (marksVector.size()!=0) {
            text = parser.vector2txt(marksVector);
            String currentPath = MediaServices.getMediaServices().getCurrentPath();        
            String newFilePath = FileServices.getDirectory(currentPath) + FileServices.getFilenameWExtensionFromPath(currentPath) + "_.txt";
            FileServices.writeTXTFile(newFilePath, text.getBytes(), fal, id);
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
        currentTupleIndex = this.tupleFinder.getLastIndex();
        return tuple;
    }
    
    public Tuple getNext(boolean move) throws Exception{
        int cti = currentTupleIndex;
        if (marksVector!=null) {
            marksVector.trimToSize();
            cti = (cti + 1) % marksVector.size();
            
            if (move){
                currentTupleIndex = cti;
            }
            
            return (Tuple)marksVector.elementAt(cti);
        }
        return new Tuple("","");
    }
    
    public Tuple getPrevious(boolean move) throws Exception{
        int cti = currentTupleIndex;
        if (marksVector!=null) {
            marksVector.trimToSize();
            
            cti = (cti - 1);
            if (cti<0){
                cti = marksVector.size()-1;
            }
            if (move) {
                this.currentTupleIndex = cti;
            }
            return (Tuple)marksVector.elementAt(cti);
        }
        return new Tuple("","");
    }
    
    public Tuple getCurrent() throws Exception{
        if (marksVector!=null) {
            marksVector.trimToSize();
            if (this.currentTupleIndex<0){
                this.currentTupleIndex = 0;
            }
            if (this.currentTupleIndex>marksVector.size()-1){
                this.currentTupleIndex = marksVector.size()-1;
            }
            return (Tuple)marksVector.elementAt(this.currentTupleIndex);
        }
        throw new Exception("Empty vector.");
    }
    
    public void applyTimeToCurrentMark(long sec) throws Exception{
        Tuple t;
        t = getCurrent();
        t.setExtra(Parser.sec2hours(sec));
    }

    
}
