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
    public TupleFinder(Vector vector){
        this.vector = vector;
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
    
    public int getLastIndex(){
        return lastIndex;
    }
    
    private boolean areTuplesEqual(Tuple tuple, Tuple target, int index) throws Exception{
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
