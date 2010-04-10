
package persistencepackage;

import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.rms.RecordEnumeration;

/**
 *
 * @author Mauricio
 */
public class RMSTuple {
    private RMSServices rmss;
    private Parser parser;
    private final String VALUES_SEPARATOR = "=";
    private final String LINE_SEPARATOR = " ";
    private Vector vector;
    
    public RMSTuple(String name) throws Exception{
        rmss = new RMSServices(name);
        this.parser = new Parser(VALUES_SEPARATOR, LINE_SEPARATOR);
        this.vector = new Vector();
        loadRMSS();
    }
    
    
    public void resetRMSTuple() throws Exception{
        this.vector.removeAllElements();
        this.rmss.resetRecordStore();
    }
    private void loadRMSS() throws Exception{
        RecordEnumeration re;
        byte[] b;
        String str;
        
        re = rmss.getRecords();
        while(re.hasNextElement()){
            b = re.nextRecord();
            str = new String(b);
            vector.addElement(parser.parseLine(str));
        }
        vector.trimToSize();
        rmss.closeRecordStore();
    }
    
    private void saveRMSS() throws Exception{
        Tuple t;
        Enumeration e = vector.elements();
        rmss.resetRecordStore();
        
        while(e.hasMoreElements()){
            t = (Tuple)e.nextElement();
            rmss.addRecord(t.getString(VALUES_SEPARATOR, LINE_SEPARATOR));
        }
        
    }
    
    public void addTuple(Tuple tuple){
        Tuple tfound;
        tfound = this.getTupleByKey(tuple.getKey());
        if (tfound==null) {
            this.vector.addElement(tuple); /* Not found. */
        }else{
            tfound.copyTheGivenTuple(tuple); /* Found */
        }
    }

    public Tuple cutTupleByKey(String key){

        Tuple t;
        Enumeration re = vector.elements();
        while(re.hasMoreElements()){
            t =(Tuple)re.nextElement();
            if (t.getKey().compareTo(key)==0){
                vector.removeElement(t);
                return t;
            }
        }
        return null;
    }
    
    public Tuple getTupleByKey(String key){
        Tuple t;
        Enumeration re = vector.elements();
        while(re.hasMoreElements()){
            t =(Tuple)re.nextElement();
            if (t.getKey().compareTo(key)==0){
                return t;
            }
        }
        return null;    
    }
    
    public void saveRMSTuple() throws Exception{
        this.saveRMSS();
    }
    
}
