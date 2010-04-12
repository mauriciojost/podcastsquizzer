/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package miscellaneouspackage;


public class Tuple {
    public static final int INDEX_KEY=0;
    public static final int INDEX_VALUE=1;
    public static final int INDEX_EXTRA=2;
    private String[] tuple;
    
    public Tuple (String key, String value){
        this(key, value, "");
    }
    
    public Tuple (String key, String value, String extra){
        tuple = new String[3];
        tuple[INDEX_KEY]    = key;
        tuple[INDEX_VALUE]  = value;
        tuple[INDEX_EXTRA]  = extra;
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
    
    public void setKey(String a)    {tuple[INDEX_KEY]   = a;}
    public void setValue(String a)  {tuple[INDEX_VALUE] = a;}
    public void setExtra(String a)  {tuple[INDEX_EXTRA] = a;}
    public void setElement(int i, String data){tuple[Math.abs(i)%tuple.length]=data;}
    
    public String getKey()  {return tuple[INDEX_KEY];}
    public String getValue(){return tuple[INDEX_VALUE];}
    public String getExtra(){return tuple[INDEX_EXTRA];}
    public String getElement(int i){return tuple[Math.abs(i)%tuple.length];}
    
    public String getString(String vseparator, String lseparator){
        String vseparator2 = " " + vseparator + " ";
        
        reeplaceChars(tuple[INDEX_KEY]  , vseparator, lseparator);
        reeplaceChars(tuple[INDEX_VALUE], vseparator, lseparator);
        reeplaceChars(tuple[INDEX_EXTRA], vseparator, lseparator);
           
        if (hasNoData(tuple[INDEX_EXTRA])) {
            if (hasNoData(tuple[INDEX_VALUE])) {
                if (hasNoData(tuple[INDEX_KEY])){
                    return "" + lseparator;
                }else{
                    return tuple[INDEX_KEY] + lseparator;
                }
            }else{
                return tuple[INDEX_KEY] + vseparator2 + tuple[INDEX_VALUE] + lseparator;
            }
        } else { 
            return tuple[INDEX_KEY] + vseparator2 + tuple[INDEX_VALUE] + vseparator2 + tuple[INDEX_EXTRA] + lseparator;
        }
    }
    public Tuple getACopy(){
        return new Tuple(new String(getKey()), new String(getValue()), new String(getExtra()));
    }
    public void copyTheGivenTuple(Tuple t){
        this.setKey(    new String(t.getKey())  );
        this.setValue(  new String(t.getValue()));
        this.setExtra(  new String(t.getExtra()));
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
