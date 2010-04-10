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
    
    private void reeplaceChars(String str, String newc, String oldc){
        if (str!=null)
            str.replace(oldc.charAt(0), newc.charAt(0));
        
        /* En el caso de utilizar separadores de más de un caracter traería problemas... */
    }
    
    private boolean hasNoData(String a){
        if (a==null) 
            return true;
        else
            return (a.compareTo("")==0);
    }
    
    public void setExtra(String a){
        this.extra = a;
    }
    public void setValue(String a){
        this.value = a;
    }
    public void setKey(String a){
        this.key = a;
    }
    public String getExtra(){return extra;}
    public String getValue(){return value;}
    public String getKey(){return key;}
    public String getString(String vseparator, String lseparator){
        String vseparator2 = " " + vseparator + " ";
        
        
        
        reeplaceChars(extra, vseparator, lseparator);
        reeplaceChars(key, vseparator, lseparator);
        reeplaceChars(value, vseparator, lseparator);
        
        if (hasNoData(extra)) {
            if (hasNoData(value)) {
                if (hasNoData(key)){
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
    
    public Tuple getACopy(){
        return new Tuple(new String(this.key), new String(this.value), new String(this.extra));
    }
    public void copyTheGivenTuple(Tuple t){
        this.key = new String(t.getKey());
        this.value = new String(t.getValue());
        this.extra = new String(t.getExtra());
    }
    
    public boolean belongsToGroup(String group_id){
        return this.getKey().startsWith(group_id);
    }
    public Tuple removeGroupID(String group_id){
        if (belongsToGroup(group_id)){
            this.setKey(this.getKey().substring(group_id.length()));
        }
        return this;
    }
    public Tuple addGroupID(String group_id){
        if (!belongsToGroup(group_id)){
            this.setKey(group_id + this.getKey());
        }
        return this;
    }
    
}


