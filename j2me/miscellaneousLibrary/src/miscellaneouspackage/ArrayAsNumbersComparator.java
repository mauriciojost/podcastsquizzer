package miscellaneouspackage;

public class ArrayAsNumbersComparator implements Comparator {
    private int index;
    public ArrayAsNumbersComparator(int index){
        this.index = index;
    }
    
    
    public boolean isComparable(Object obj){
        
        try{
            Integer[] entero = (Integer[])obj;
            return true;
        }catch(Exception e){
            return false;
        }
    }
    
    public int compare(Object x, Object y) throws Exception{
        int Xo=0;
        int Yo=0;
        
        Integer[] XoI = (Integer[])x;
        Integer[] YoI = (Integer[])y;
        
        Xo = XoI[index].intValue();
        Yo = YoI[index].intValue();
        
        return Xo-Yo;
    }
}
