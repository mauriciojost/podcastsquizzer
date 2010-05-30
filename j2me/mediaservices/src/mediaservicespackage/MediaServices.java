package mediaservicespackage;

import java.io.InputStream;
import java.util.*;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.media.*;
import javax.microedition.media.control.*;

public class MediaServices implements Runnable{

    public final int UPDATE_RATE_MS = 333;
    private static MediaServices mediaServicesInstance = null;
    private Player player;
    private long length = 0;
    private int step = 10;
    //private PlayerListener playerListener;
    private Vector vectorPlayerListeners;
    public static final int TIME_FACTOR = 1000000;
    private String path;
    
    private MediaServices(){
        vectorPlayerListeners = new Vector();
        (new Thread(this, "Thread Media")).start();
    }
    
    public static MediaServices getMediaServices(){
        if (mediaServicesInstance == null){
            mediaServicesInstance = new MediaServices();
        }
        return mediaServicesInstance;
    }
    
    
    public void load(String path1) throws Exception {
        VolumeControl vc;
        
        
        this.path = path1;
        String path2 = path1;
            
        if (player != null) {
            player.deallocate();
        }
        
        try{
            /* Nokia S40 series. */
            player = Manager.createPlayer(path2); 
            player.realize();
        }catch(Exception ex){
            try{
                /* Sony. */
                FileConnection fc = (FileConnection)Connector.open(path2, Connector.READ);
                InputStream is = (InputStream)fc.openInputStream();
                player = Manager.createPlayer(is, "audio/mpeg"); /*"audio/x-wav"*/
                player.realize();
            }catch(Exception e){
                String[] prot = Manager.getSupportedProtocols(null);
                String p = "";
                int i;
                for (i=0;i<prot.length;i++) {
                    p = p + " " + prot[i];
                }
                throw new Exception(e.getMessage() + " Media error, supported protocols are " + p);
            }
        }

        
        
        this.length = player.getDuration();

        /*if (this.playerListener!=null){
            player.addPlayerListener(this.playerListener);
        }*/

        // get volume control for player and set volume to max
        vc = (VolumeControl) player.getControl("VolumeControl");
        if(vc != null) {
            vc.setLevel(100);
        }
        player.prefetch();

        this.play();
    }
        
  
    public void movePosition(int seconds_change) throws MediaException{
        long a, b;
        a = this.player.getMediaTime();
        b = (seconds_change*TIME_FACTOR);
        this.player.setMediaTime(a+ b);
    }
    
    public void setPosition(int seconds_time) throws MediaException{
        long b;
        b = (seconds_time*TIME_FACTOR);
        this.player.setMediaTime(b);
    
    }
    
    public void goBack() {
        try {
            this.player.setMediaTime(this.player.getMediaTime() - ((this.length/1000)*step));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void goForward() {
        try {
            this.player.setMediaTime(this.player.getMediaTime() + ((this.length/1000)*step));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    
    /**
     * Period of time of goBack/Forward. 
     * @param step 1-1000 (1 represents a 1/1000*duration step)
     */
    public void changeStep(int step){
        this.step = step;
    }
    
    public void playPause() throws Exception {
        
        if (this.player.getState()==player.STARTED) {
            this.player.stop();
        } else {
            this.player.start();
        }
    }
    
    
    
    public void play() throws Exception {
        
        if (this.player.getState()!=player.STARTED) {
            this.player.start();
        }
    }
    
    
    public void pause() throws Exception {
        
        if (this.player.getState()==player.STARTED) {
            this.player.stop();
        }
    }
    

    public void stopAll(){
        try{
        this.player.stop();
            player.deallocate();
        }catch(Exception e){e.printStackTrace();}
    }

    
    
    
    public boolean isItPlaying(){
        return (this.player.getState()==player.STARTED);
    }
    
    public long getDuration() {
        return this.length;
    }
    
    public long getDurationSeconds(){
        return this.length/MediaServices.TIME_FACTOR; 
    }
    
    public long getPosition() throws Exception {
        long time;
        String error = "Unable to get current time: ";
        try{
            time = this.player.getMediaTime();
            if (time != this.player.TIME_UNKNOWN) {
                return time;
            }else{
                throw new Exception(error);
            }
        }catch(Exception er){
            throw new Exception(error + er.getMessage());
        }
    }

    public long getPositionSeconds() throws Exception{
        return (this.getPosition()/MediaServices.TIME_FACTOR);
    }
    public void addPlayerListener(PlayerListener pl){
        this.vectorPlayerListeners.addElement(pl);
    }
    
    public String getCurrentPath(){
        return this.path;
    }

    public void run() {
        PlayerListener playerListener;
        Enumeration iterator;
        while (true){
            try { Thread.sleep(UPDATE_RATE_MS); } catch (InterruptedException ex) { ex.printStackTrace(); }
            iterator = this.vectorPlayerListeners.elements();
            while(iterator.hasMoreElements()){
                playerListener = (PlayerListener)iterator.nextElement();
                if ((playerListener!=null) && (this.player!=null)){
                    playerListener.playerUpdate(this.player, null, null);
                }
            }
        }   
    }
}
