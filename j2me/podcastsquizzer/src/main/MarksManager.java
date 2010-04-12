/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import canvaspackage.Word;
import miscellaneouspackage.Tuple;
import java.util.Vector;
//import miscellaneouspackage.Sorter;
import persistencepackage.*;

/**
 *
 * @author Mauricio
 */
public class MarksManager{
    private static final String EMPTY_STRING = Word.NORMAL_RED + "<empty>";
    private static final Tuple EMPTY_TUPLE = new Tuple(EMPTY_STRING,EMPTY_STRING,EMPTY_STRING);
    private Vector marksVector;
    private TupleFinder tupleFinder;
    private int counter=0;
    private int currentTupleIndex=0;
    
    public MarksManager(Vector gv){
        this.setMarks(gv);
    }
    
    private void setMarks(Vector gv){
        this.marksVector = gv;
        HybridFile.setMarksVector(marksVector);
        this.tupleFinder = new TupleFinder(marksVector);
    }
            
    public void saveMarks(FileActionListener fal, String id) throws Exception{
        if (this.marksVector==null){
            throw new Exception("Marks Vector not loaded.");
        }
        marksVector.trimToSize();
        if (marksVector.size()!=0) {
            HybridFile.saveFile(fal, id);   
        }
    }
    
    public String addMarkNow(int seg) throws Exception{
        
        String text;
        
        if (this.marksVector==null){
            throw new Exception("Marks Vector not loaded.");
        }
        
        text =  Parser.sec2hours(seg);
        
        try{
            getMark(seg);
            return "EXISTENT";
        }catch(Exception e){
            counter++;
            Tuple tup;
            tup = new Tuple( "Mark ("+ counter + ")","Comment or explanation." ,text);
            marksVector.insertElementAt(tup, Math.max(0,this.currentTupleIndex));
            this.currentTupleIndex = marksVector.indexOf(tup);
            e.printStackTrace();
            return text;
        }
        
    }
    
    public Tuple getMark(int sec) throws Exception{
        Tuple tuple;
        tuple = this.tupleFinder.lookFor(sec);
        currentTupleIndex = this.tupleFinder.getIndex(tuple);
        return tuple;
    }
    
    public Tuple getNext(boolean move) throws Exception{
        int cti = currentTupleIndex;
        if (marksVector!=null) {
            marksVector.trimToSize();
            cti = Math.min((cti + 1) , marksVector.size()-1);
            
            if (move){
                currentTupleIndex = cti;
            }
            
            return (Tuple)marksVector.elementAt(cti);
        }
        return EMPTY_TUPLE;
    }
    
    public Tuple getPrevious(boolean move) throws Exception{
        int cti = currentTupleIndex;
        if (marksVector!=null) {
            marksVector.trimToSize();
            
            cti = Math.max(0,(cti - 1));
            if (move) {
                this.currentTupleIndex = cti;
            }
            return (Tuple)marksVector.elementAt(cti);
        }
        return EMPTY_TUPLE;
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
            if (marksVector.size()==0){
                this.currentTupleIndex = 0;
            }
            return (Tuple)marksVector.elementAt(this.currentTupleIndex);
        }else{
            return EMPTY_TUPLE;
        }
    }
    
    public int getCurrentTupleIndex(){
        return this.currentTupleIndex;
    }
    
    public int getSize(){
        if (this.marksVector==null){
            return 0;
        }
        
        return this.marksVector.size();
    }
    
    
    
    public void applyTimeToCurrentMark(long sec) throws Exception{
        if (this.marksVector==null){
            throw new Exception("Marks Vector not loaded.");
        }
        
        Tuple t;
        t = getCurrent();
        t.setExtra(Parser.sec2hours(sec));        
    }

}
