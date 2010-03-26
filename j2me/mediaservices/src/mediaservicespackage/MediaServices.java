
package mediaservicespackage;

import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.VolumeControl;


/**
 *
 * @author Mauricio
 */
public class MediaServices implements Runnable{
    
    private static MediaServices mediaServicesInstance = null;
    private Player player;
    private long length = 0;
    private boolean playing = false;
    private int step = 10;
    private PlayerListener playerListener;
    public static final int TIME_FACTOR = 1000000;
    private String path;
    
    private MediaServices(){
        (new Thread(this)).start();
    }
    
    public static MediaServices getMediaServices(){
        if (mediaServicesInstance == null){
            mediaServicesInstance = new MediaServices();
        }
        return mediaServicesInstance;
    }
    
    
    public void load(String path1) throws Exception {
        VolumeControl vc;
        playing = false;
        
        //String path2 = /*FileServices.correctURL(path);*/ FileServices.getStandardPath(path1);
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
    }
        
    public void play(String path) throws Exception{
        
        this.load(path);
        
        player.start();
        playing = true;

    }
    
    public void setPosition(int seconds_change) throws MediaException{
        this.player.setMediaTime(this.player.getMediaTime() + (seconds_change*TIME_FACTOR));
    }
    
    public void goBack() {
        try {
            this.player.setMediaTime(this.player.getMediaTime() - ((this.player.getDuration()/1000)*step));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void goForward() {
        try {
            this.player.setMediaTime(this.player.getMediaTime() + ((this.player.getDuration()/1000)*step));
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
        if (playing == true) {
            this.player.stop();
            playing = false;
        } else {
            this.player.start();
            playing = true;
        }
    }
    
    public boolean isItPlaying(){
        return playing;
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
    public void setPlayerListener(PlayerListener pl){
        //if (this.player!=null) {
            this.playerListener = pl;
            //this.player.addPlayerListener(pl);
        //}
    }
    
    
    public String getCurrentPath(){
        return this.path;
    }

    public void run() {
        
        while (true){
            try { Thread.sleep(200); } catch (InterruptedException ex) { ex.printStackTrace(); }
            
            if ((this.playerListener!=null) && (this.player!=null)){
                this.playerListener.playerUpdate(this.player, "", null);
            }
        }   
    }
}
