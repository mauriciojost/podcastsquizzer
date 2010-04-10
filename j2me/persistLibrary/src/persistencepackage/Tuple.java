/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package persistencepackage;


public class Tuple {
    private String[] tuple;
    
    public Tuple (String key, String value){
        this(key, value, "");
    }
    
    public Tuple (String key, String value, String extra){
        tuple = new String[3];
        tuple[0] = key;
        tuple[1] = value;
        tuple[2] = extra;
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
        tuple[2] = a;
    }
    public void setValue(String a){
        tuple[1] = a;
    }
    public void setKey(String a){
        tuple[0] = a;
    }
    public String getExtra(){return tuple[2];}
    public String getValue(){return tuple[1];}
    public String getKey(){return tuple[0];}
    public String getString(String vseparator, String lseparator){
        String vseparator2 = " " + vseparator + " ";
        
        
        
        reeplaceChars(tuple[2], vseparator, lseparator);
        reeplaceChars(tuple[0], vseparator, lseparator);
        reeplaceChars(tuple[1], vseparator, lseparator);
        
        if (hasNoData(tuple[2])) {
            if (hasNoData(tuple[1])) {
                if (hasNoData(tuple[0])){
                    return "" + lseparator;
                }else{
                    return tuple[0] + lseparator;
                }
            }else{
                return tuple[0] + vseparator2 + tuple[1] + lseparator;
            }
        } else { 
            return tuple[0] + vseparator2 + tuple[1] + vseparator2 + tuple[2] + lseparator;
        }
    }
    
    public Tuple getACopy(){
        return new Tuple(new String(getKey()), new String(getValue()), new String(getExtra()));
    }
    public void copyTheGivenTuple(Tuple t){
        this.setKey(new String(t.getKey()));
        this.setValue(new String(t.getValue()));
        this.setExtra(new String(t.getExtra()));
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


