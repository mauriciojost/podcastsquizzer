
package persistencepackage;

import javax.microedition.rms.*;

/**
 *
 * @author Mauricio
 */

public class RMSServices{
    
    private String name;
    private RecordStore rs;
    
    public RMSServices(String name) throws Exception {
        this.name = name;
        rs = RecordStore.openRecordStore(name,true);
        this.closeRecordStore();
    }
    
    public void addRecord(String data) throws Exception {
        if (rs==null) {
            rs = RecordStore.openRecordStore(name,true);
        }
        byte b[]=data.getBytes();
        rs.addRecord(b,0,b.length);
        this.closeRecordStore();

    }
    
    public void removeRecord(int recordID) throws Exception{
        if (rs==null) {
            rs = RecordStore.openRecordStore(name,true);
        }
        rs.deleteRecord(recordID);
        this.closeRecordStore();
    }
    
    
    public RecordEnumeration getRecords() throws Exception{
        RecordEnumeration re;
        rs = RecordStore.openRecordStore(name,true);
        re = rs.enumerateRecords(null, null, false);
        //rs.closeRecordStore();
        return re;
    }
    
    public void closeRecordStore() throws Exception{
        if (rs!=null) {
            rs.closeRecordStore();
            rs = null;
        }
    }
    
    public void resetRecordStore() throws Exception{
        this.closeRecordStore();
        try{
            RecordStore.deleteRecordStore(name);
        }catch(RecordStoreNotFoundException e){}
    }
    
}