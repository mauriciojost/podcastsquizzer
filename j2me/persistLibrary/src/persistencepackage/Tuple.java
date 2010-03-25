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
        
        if (extra.compareTo("")==0) {
            if (value.compareTo("")==0) {
                if (key.compareTo("")==0){
                    return "" + lseparator;
                }else{
                    return key + lseparator;
                }
            }else{
                return key + vseparator2 + value + lseparator;
            }
        } else { 
            return key + vseparator2 + value + vseparator2 + extra + lseparator;
        }

            
       
    }
    
    public void copy(Tuple t){
        this.key = new String(t.getKey());
        this.value = new String(t.getValue());
        this.extra = new String(t.getExtra());
    }
}


