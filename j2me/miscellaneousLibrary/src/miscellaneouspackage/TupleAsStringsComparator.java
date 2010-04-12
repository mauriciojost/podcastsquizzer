
package miscellaneouspackage;

public class TupleAsStringsComparator implements Comparator {
    
    private int index;
    
    public TupleAsStringsComparator(int index_to_compare){
        this.index = index_to_compare;
    }
    
    public boolean isComparable(Object obj){
        
        try{
            if (((Tuple) obj).getElement(index) != null){
                return true;
            }else{
                return false;
            }
        }catch(Exception e){
            return false;
        }
    }
    
    public int compare(Object x, Object y) throws Exception{
        String Xo;
        String Yo;
        //try {
            Xo = ((Tuple)x).getElement(index);
            Yo = ((Tuple)y).getElement(index);
        //} catch (Exception ex) {
            //ex.printStackTrace();
        //}
        return Xo.compareTo(Yo);
    }
}
