/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mediaservicespackage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.RecordControl;

/**
 *
 * @author Mauricio
 */
public class Recorder {
    private ByteArrayOutputStream last_recorded;
    private Player recorderP;    
    private Player playerP;
    private String last_content;
    
    public void record(){
        try{
            recorderP=Manager.createPlayer("capture://audio");
            
            
            recorderP.realize();
            
            RecordControl rc = (RecordControl)recorderP.getControl("RecordControl");
            last_content = rc.getContentType();
            
            last_recorded = new ByteArrayOutputStream();
            
            rc.setRecordStream(last_recorded);
            
            rc.startRecord();
            recorderP.start();
            Thread.sleep(5000);
            
            rc.commit();
            Thread.sleep(1000);
            
            recorderP.stop();
            recorderP.deallocate();
            
            recorderP = null;
            
            

        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void play()
    {
        try {
            byte[] recordedSoundArray = last_recorded.toByteArray();
            ByteArrayInputStream recordedInputStream = new ByteArrayInputStream(recordedSoundArray);
            playerP = Manager.createPlayer(recordedInputStream, last_content);
            playerP.prefetch();
            playerP.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (MediaException ex) {
            ex.printStackTrace();
        }
    }
}
