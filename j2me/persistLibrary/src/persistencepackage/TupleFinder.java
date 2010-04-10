/**
 *
 * @author Mauri
 */

package persistencepackage;

import java.util.Enumeration;
import java.util.Vector;

public class TupleFinder {
    private Vector vector;
    private int lastIndex=0;
    private TupleComparator tupleComparator; 
    public TupleFinder(Vector vector){
        this.vector = vector;
        tupleComparator = new TupleComparator(Tuple.INDEX_EXTRA);
    }
    
    
    public Tuple lookFor(Tuple target) throws Exception{
        Enumeration e;
        Tuple tuple;
        int index=0;
                
        e = vector.elements();
        
        while (e.hasMoreElements()){
            tuple = (Tuple)e.nextElement();
            if (areTuplesEqual(tuple, target,2)==true){
                lastIndex = index;
                return tuple;
            }
            index++;
        }
        throw new Exception("Tuple not found.");
    }
    
    
    public Tuple lookForMoreAppropriate(int sec_actual) throws Exception{
        Tuple currentT=null, candidate=null;
        int diff;
        int index=-1;
        int mindiff=-1;
        int candidate_index=-1;
                
        vector.trimToSize();
            
        for(int i=0; i<vector.size();i++){
            index = (lastIndex+i)%vector.size();
            currentT = (Tuple)vector.elementAt(index);
            try {
                diff = tupleComparator.compareTupleElementAsTimeMarks(sec_actual, currentT);
                if (diff>=0){
                    candidate = currentT;
                    mindiff = diff;
                    candidate_index = index;
                }else{
                    break;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (candidate==null){
            throw new Exception("Tuple not found.");
        }
        this.lastIndex = candidate_index;
        return candidate;
        
    }
    
    public int getLastIndex(){
        return lastIndex;
    }
    
    private boolean areTuplesEqual(Tuple tuple, Tuple target, int index) {
        if ((target.getKey()!=null)&&(index==0)){
            return (target.getKey().compareTo(tuple.getKey())==0);
        }
        if ((target.getValue()!=null)&&(index==1)){
            return (target.getValue().compareTo(tuple.getValue())==0);
        }
        if ((target.getExtra()!=null)&&(index==2)){
            return (target.getExtra().compareTo(tuple.getExtra())==0);
        }
        return false;
    }
}
