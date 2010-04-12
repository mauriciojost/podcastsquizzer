package persistencepackage;

import miscellaneouspackage.Tuple;
import miscellaneouspackage.Comparator;


public class TupleAsMarkComparator implements Comparator {
    
    private int index;
    
    public TupleAsMarkComparator(int index_to_compare){
        this.index = index_to_compare;
    }
    
    
    public int compareTupleElementAsTimeMarks(int sec_actual, Tuple set_target) throws Exception {
        int tupM=0, tarM=0;
        tupM = sec_actual;
        tarM = Parser.hours2sec(set_target.getElement(index));
        return (tupM- tarM);        
        
    }

    public boolean isComparable(Object obj){
        
        try{
            Parser.hours2sec(((Tuple) obj).getElement(index));
            return true;
        }catch(Exception e){
            return false;
        }
    }
    
    public int compare(Object x, Object y) throws Exception{
        int Xo=0;
        int Yo=0;
        //try {
            Xo = Parser.hours2sec(((Tuple) x).getElement(index));
            Yo = Parser.hours2sec(((Tuple)y).getElement(index));
        //} catch (Exception ex) {
            //ex.printStackTrace();
        //}
        return Xo-Yo;
    }
}
