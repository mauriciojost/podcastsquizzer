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
public class MarksManager implements FileActionListener{
    private Parser parser;
    private Vector marksVector;
    private TupleFinder tupleFinder;
    private int counter=0;
    private int currentTupleIndex=-1;
    private boolean successfullySaved = false;
    
    public MarksManager(Parser parser){
        this.parser = parser;
        this.marksVector = new Vector();
        this.tupleFinder = new TupleFinder(marksVector);
    }

    public void setMarks(Vector gv){
        this.marksVector = gv;
        this.tupleFinder = new TupleFinder(marksVector);
    }
            
    public void saveMarks() throws Exception{
        String text;
        
        marksVector.trimToSize();
        if (marksVector.size()!=0) {
            text = parser.vector2txt(marksVector);
            String currentPath = MediaServices.getMediaServices().getCurrentPath();        
            String newFilePath = FileServices.getDirectory(currentPath) + FileServices.getFilenameWExtensionFromPath(currentPath) + "_.txt";
            FileServices.writeTXTFile(newFilePath, text.getBytes(), this, "saveMarks");
            
            Thread.sleep(2000);
            if (this.successfullySaved==false){
                throw new Exception("Marks not saved yet...");
            }
            
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
    
    public Tuple getNext() throws Exception{
        
        if (marksVector!=null) {
            marksVector.trimToSize();
            this.currentTupleIndex = (this.currentTupleIndex + 1) % marksVector.size();
            return (Tuple)marksVector.elementAt(this.currentTupleIndex);
        }
        return new Tuple("","");
    }
    
    public Tuple getPrevious() throws Exception{
        if (marksVector!=null) {
            marksVector.trimToSize();
            this.currentTupleIndex = (this.currentTupleIndex - 1);
            if (this.currentTupleIndex<0){
                this.currentTupleIndex = marksVector.size()-1;
            }
            return (Tuple)marksVector.elementAt(this.currentTupleIndex);
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
        return new Tuple("","");
    }
    
    public void applyTimeToCurrentMark(long sec) throws Exception{
        Tuple t;
        t = getCurrent();
        t.setExtra(Parser.sec2hours(sec));
    }

    public void writeOperationReady(String id, String path, boolean operation_successfully) {
        this.successfullySaved = operation_successfully;
    }
}
