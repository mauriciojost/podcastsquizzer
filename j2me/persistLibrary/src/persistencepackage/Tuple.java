/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package persistencepackage;


public class Tuple {
    private String key;
    private String value;
    private String extra;
    
    public Tuple (String key, String value){
        this.key = key;
        this.value = value;
        this.extra = "";
    }
    
    public Tuple (String key, String value, String extra){
        this.key = key;
        this.value = value;
        this.extra = extra;
    }
    
    public String getExtra(){return extra;}
    public String getValue(){return value;}
    public String getKey(){return key;}
    public String getString(String vseparator, String lseparator){
        String vseparator2 = " " + vseparator + " ";
        if ((key.compareTo("")!=0) && (value.compareTo("")!=0) && (extra.compareTo("")!=0))
            return key + vseparator2 + value + vseparator2 + extra + lseparator;
        else if ((key.compareTo("")!=0) && (value.compareTo("")!=0))
            return key + vseparator2 + value + lseparator;
        else 
            return key + lseparator;
       
    }
    
    public void copy(Tuple t){
        this.key = t.getKey();
        this.value = t.getValue();
        this.extra = t.getExtra();
    }
}


