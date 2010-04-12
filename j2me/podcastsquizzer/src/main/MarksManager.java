/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import miscellaneouspackage.Tuple;
import java.util.Vector;
//import miscellaneouspackage.Sorter;
import persistencepackage.*;

/**
 *
 * @author Mauricio
 */
public class MarksManager{
    private Vector marksVector;
    private TupleFinder tupleFinder;
    private int counter=0;
    private int currentTupleIndex=0;
    //private Sorter sorter;
    
    public MarksManager(Parser parser){
        //sorter = new Sorter(new TupleAsMarkComparator(Tuple.INDEX_EXTRA));
        setMarks(new Vector());
    }
    
    public void setMarks(Vector gv){
        this.marksVector = gv;
        /*try{
            sorter.sort(marksVector);
        }catch(Exception e){
            e.printStackTrace();
        }*/
        HybridFile.setMarksVector(marksVector);
        this.tupleFinder = new TupleFinder(marksVector);
    }
            
    public void saveMarks(FileActionListener fal, String id) throws Exception{
        getMarksVector();
        marksVector.trimToSize();
        //sorter.sort(marksVector);
        if (marksVector.size()!=0) {
            HybridFile.saveFile(fal, id);   
        }
    }
    
    public String addMarkNow(int seg) throws Exception{
        
        String text;
        text =  Parser.sec2hours(seg);
        
        try{
            getMark(seg);
            return "EXISTENT";
        }catch(Exception e){
            counter++;
            Tuple tup;
            tup = new Tuple( "Mark ("+ counter + ")","Comment or explanation." ,text);
            marksVector.insertElementAt(tup, this.currentTupleIndex);
            this.currentTupleIndex = marksVector.indexOf(tup);
            //sorter.sort(marksVector);
            e.printStackTrace();
            return text;
        }
        
    }
    
    public Tuple getMark(int sec) throws Exception{
        Tuple tuple;
        tuple = this.tupleFinder.lookFor(sec);
        //tuple = this.tupleFinder.lookForMoreAppropriate((int)sec);
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
        return new Tuple("","");
    }
    
    public Tuple getPrevious(boolean move) throws Exception{
        int cti = currentTupleIndex;
        if (marksVector!=null) {
            marksVector.trimToSize();
            
            cti = Math.max(0,(cti - 1));
            /*if (cti<0){
                cti = 0marksVector.size()-1;
            }*/
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
    
    public int getCurrentTupleIndex(){
        return this.currentTupleIndex;
    }
    
    public int getSize(){
        getMarksVector();
        return this.marksVector.size();
    }
    
    public Vector getMarksVector(){
        if (marksVector==null){
            setMarks(new Vector());
        }
        return marksVector;
    }
    
    public void applyTimeToCurrentMark(long sec) throws Exception{
        Tuple t;
        getMarksVector();
        t = getCurrent();
        t.setExtra(Parser.sec2hours(sec));
        /*try{
            sorter.sort(marksVector);
        }catch(Exception e){
            e.printStackTrace();
        }*/
    }

    
}
