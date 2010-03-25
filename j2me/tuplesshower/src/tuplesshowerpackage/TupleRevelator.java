/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tuplesshowerpackage;

import persistencepackage.Tuple;


public class TupleRevelator {
    public static final int MODE_123 = 0;
    public static final int MODE_213 = 1;
    public static final int MODE_ALL = 2;
    public static final int AMOUNT_OF_MODES = 3;
    
    private TuplesShowerInterface tuplesshower;
    private Tuple currentTuple;
    private int mode = TupleRevelator.MODE_123;
    private int stage = 0; 
    
    public TupleRevelator(TuplesShowerInterface ts){
        this.tuplesshower = ts;
    }
    public void setTuple(Tuple tuple) {
        this.currentTuple = tuple;
        this.stage = 0;
        this.nextRevelation();
    }
    public void setMode(int mode) {
        this.mode = mode;
    }    
    public void nextMode() {
        this.mode = (this.mode + 1) % TupleRevelator.AMOUNT_OF_MODES;
    }
    public void nextRevelation(){
        Tuple tuple;
        
        if (this.currentTuple==null) {
            tuple = new Tuple("","","");
        }else{
            
            switch (mode) {
                case TupleRevelator.MODE_123:
                    if (stage==0) {
                        tuple = new Tuple(this.currentTuple.getKey(),"","");
                    } else if (stage==1) {
                        tuple = new Tuple(this.currentTuple.getKey(),this.currentTuple.getValue(),"");
                    } else {
                        tuple = new Tuple(this.currentTuple.getKey(),this.currentTuple.getValue(),this.currentTuple.getExtra());
                    }
                    break;
                case TupleRevelator.MODE_213:
                    if (stage==0) {
                        tuple = new Tuple("",this.currentTuple.getValue(),"");
                    } else if (stage==1) {
                        tuple = new Tuple(this.currentTuple.getKey(),this.currentTuple.getValue(),"");
                    } else {
                        tuple = new Tuple(this.currentTuple.getKey(),this.currentTuple.getValue(),this.currentTuple.getExtra());
                    }
                    break;
                case TupleRevelator.MODE_ALL:
                    tuple = new Tuple(this.currentTuple.getKey(),this.currentTuple.getValue(),this.currentTuple.getExtra());
                    break;
                default:
                    tuple = new Tuple ("","","");
                    break;
            }
        }
        
        this.tuplesshower.setValues(tuple);     
        this.stage++;
    }
    
}
