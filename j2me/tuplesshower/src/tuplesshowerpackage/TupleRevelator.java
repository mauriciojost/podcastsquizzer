/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tuplesshowerpackage;

import miscellaneouspackage.Tuple;


public class TupleRevelator {
    public static final int MODE_123 = 0;
    public static final int MODE_213 = 1;
    public static final int MODE_ALL = 2;
    public static final String[] MODE_NAME = {"A.AB.ABC","B.AB.ABC", "ABC.ABC.ABC"};
    public static final int AMOUNT_OF_MODES = 3;
    public static final String HIDEN_TEXT = "<hidden>";
    
    private TuplesShowerInterface tuplesshower;
    private Tuple currentTuple;
    private int mode = TupleRevelator.MODE_123;
    private int stage = 0; 
    private Tuple lastTuple = new Tuple("","","");
    
    public TupleRevelator(TuplesShowerInterface ts){
        this.tuplesshower = ts;
    }
    public void setTuple(Tuple tuple) {
        this.currentTuple = tuple;
        this.stage = 0;
        update(false);
    }
    public void setTupleTemporaryModeAndStage(Tuple tuple, int momentaneus_mode, int momentaneus_stage){
        int mode_temp_aux, stage_temp_aux;
        this.currentTuple = tuple;
        
        stage_temp_aux = this.stage;
        mode_temp_aux = this.mode;
        mode = momentaneus_mode;
        stage = momentaneus_stage;
        update(false);
        mode = mode_temp_aux;
        stage = stage_temp_aux;
    }
    
    public void setMode(int mode) {
        this.stage = 0;
        this.mode = Math.abs(mode) % TupleRevelator.AMOUNT_OF_MODES;
        this.update(false);
    }    
    
    
    public int getCurrentMode(){
        return this.mode;
    }
    public String getCurrentModeName(){
        return MODE_NAME[this.mode];
    }
    
    public int nextMode() {
        this.stage = 0;
        this.mode = (this.mode + 1) % TupleRevelator.AMOUNT_OF_MODES;
        this.update(false);
        return this.mode;
    }
    
    /**
     * Updates the output.
     * @param next if true, the update is performed with the next revelation.
     */
    public void update(boolean update_with_next_stage){
        Tuple tuple;
        
        if (this.currentTuple==null) {
            tuple = new Tuple("","","");
        }else{
            
            if (update_with_next_stage==true){
                this.stage++;
            }   
            
            switch (mode) {
                case TupleRevelator.MODE_123:
                    if (stage==0) {
                        tuple = new Tuple(this.currentTuple.getKey(),HIDEN_TEXT,HIDEN_TEXT);
                    } else if (stage==1) {
                        tuple = new Tuple(this.currentTuple.getKey(),this.currentTuple.getValue(),HIDEN_TEXT);
                    } else {
                        tuple = new Tuple(this.currentTuple.getKey(),this.currentTuple.getValue(),this.currentTuple.getExtra());
                    }
                    break;
                case TupleRevelator.MODE_213:
                    if (stage==0) {
                        tuple = new Tuple(HIDEN_TEXT,this.currentTuple.getValue(),HIDEN_TEXT);
                    } else if (stage==1) {
                        tuple = new Tuple(this.currentTuple.getKey(),this.currentTuple.getValue(),HIDEN_TEXT);
                    } else {
                        tuple = new Tuple(this.currentTuple.getKey(),this.currentTuple.getValue(),this.currentTuple.getExtra());
                    }
                    break;
                
                case TupleRevelator.MODE_ALL: /* As the default mode. */
                default:
                    tuple = new Tuple(this.currentTuple.getKey(),this.currentTuple.getValue(),this.currentTuple.getExtra());
                    break;
                
            }
        }
        
        this.lastTuple = tuple;
        this.tuplesshower.setValues(tuple);     
        
        
    }
    
    public void nextRevelation(){
        update(true);
    }
    
    public Tuple getLastTuple(){
        return this.lastTuple;
    }
}
