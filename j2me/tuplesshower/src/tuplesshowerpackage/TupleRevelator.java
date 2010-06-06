package tuplesshowerpackage;

import canvaspackage.Word;
import miscellaneouspackage.Tuple;


public class TupleRevelator {
    private static final String EMPTY_STRING = Word.NORMAL_RED + "<empty>";
    private static final Tuple EMPTY_TUPLE = new Tuple(EMPTY_STRING,EMPTY_STRING,EMPTY_STRING);
    public static final int MODE_123 = 0;
    public static final int MODE_213 = 1;
    public static final int MODE_AC_ABC = 2;
    public static final int MODE_ALL = 3;

    public static final String[] MODE_NAME = {"A.AB.ABC","B.AB.ABC", "AC.ABC.ABC", "ABC.ABC.ABC"};
    public static final int AMOUNT_OF_MODES = 4;
    public static final String HIDEN_TEXT = Word.NORMAL_RED + "<hidden>";
    public static final String EMPTY_TEXT = Word.NORMAL_RED + "<empty>";
    
    private TuplesShowerInterface tuplesshower;
    private Tuple currentTuple;
    private int mode = TupleRevelator.MODE_AC_ABC;
    private int stage = 0; 
    private Tuple lastTuple = EMPTY_TUPLE;
    
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
            tuple = EMPTY_TUPLE;
        }else{
            
            if (update_with_next_stage==true){
                this.stage++;
            }   
            
            switch (mode) {
                case TupleRevelator.MODE_123:
                    if (stage==0) {
                        tuple = new Tuple(this.currentTuple.getKey(),hideIfItIsNotEmpty(this.currentTuple.getValue()),hideIfItIsNotEmpty(this.currentTuple.getExtra()));
                    } else if (stage==1) {
                        tuple = new Tuple(this.currentTuple.getKey(),this.currentTuple.getValue(),hideIfItIsNotEmpty(this.currentTuple.getExtra()));
                    } else {
                        tuple = new Tuple(this.currentTuple.getKey(),this.currentTuple.getValue(),this.currentTuple.getExtra());
                    }
                    break;

                case TupleRevelator.MODE_AC_ABC:
                    if (stage==0) {
                        tuple = new Tuple(this.currentTuple.getKey(), hideIfItIsNotEmpty(this.currentTuple.getValue()), this.currentTuple.getExtra());
                    } else {
                        tuple = new Tuple(this.currentTuple.getKey(),this.currentTuple.getValue(),this.currentTuple.getExtra());
                    }
                    break;
                case TupleRevelator.MODE_213:
                    if (stage==0) {
                        tuple = new Tuple(HIDEN_TEXT,this.currentTuple.getValue(),hideIfItIsNotEmpty(this.currentTuple.getExtra()));
                    } else if (stage==1) {
                        tuple = new Tuple(this.currentTuple.getKey(),this.currentTuple.getValue(),hideIfItIsNotEmpty(this.currentTuple.getExtra()));
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
        if (tuplesshower!=null){
            this.tuplesshower.setValues(tuple);     
        }
        
        
    }
    
    public void nextRevelation(){
        update(true);
    }
    
    public Tuple getLastTuple(){
        return this.lastTuple;
    }

    private String hideIfItIsNotEmpty(String str){
        if (str.trim().compareTo("")==0){
            return EMPTY_TEXT;
        }else{
            return HIDEN_TEXT;
        }
    }
}
