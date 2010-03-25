/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package persistencepackage;

import java.util.Enumeration;
import java.util.Vector;


public class Parser {
    private Vector tabla;
    private String values_separator;
    private String line_separator = "\n";
    
    public Parser(String values_separator){
        this.values_separator = values_separator;
    }
    
    public Parser(String values_separator, String line_separator){
        this.values_separator = values_separator;
        this.line_separator = line_separator;
    }
    
    public Vector txt2vector(String text) {
        int previousnl = 0;
        int lastnl = 0;
        
        String current_line = "";
        
        
        tabla = new Vector();
        
        lastnl = text.indexOf(line_separator);
        while(lastnl != -1) {
            current_line = text.substring(previousnl, lastnl);
            
            previousnl = lastnl+1;
            lastnl = text.indexOf(line_separator, previousnl);
            tabla.addElement(parseLine(current_line));
        }
        
        return tabla;
    }
    
    public String vector2txt(Vector vector) {
        String text="";
        Enumeration iterator;
        Tuple tuple;
        
        vector.trimToSize();
        iterator = vector.elements();
        
        while(iterator.hasMoreElements()){
            tuple = (Tuple)iterator.nextElement();
            text = text + tuple.getString(this.values_separator, this.line_separator);
        }
        
        return text;
    }
    
    public Tuple parseLine(String line){
        int index;
        //boolean nuevo;
        Tuple pareja;
        
        index = line.indexOf(this.values_separator);
        if (index!=-1) {
            String key = line.substring(0, index).trim();
            String value = line.substring(index+1,line.length()).trim();
            
            index = value.indexOf(this.values_separator);
            if (index!=-1) {
                String extra;
                String valu;
                valu = value.substring(0, index).trim();
                extra = value.substring(index+1,value.length()).trim();
                pareja = new Tuple(key, valu, extra);
                //tabla.addElement(pareja);
                return pareja;
                //nuevo = true;
            }else{
                pareja = new Tuple(key, value);
                //tabla.addElement(pareja);
                return pareja;
                //nuevo = true;
            }
        } else {
            //nuevo = false;
            pareja = new Tuple(line, "");
            return pareja;
        }
        
        
        
        //return nuevo;
    }
    
    
    
        
    
    
    public static String sec2hours(long sec){
        long hor, min;
        String secStr = (sec%60<10? "0"+(sec%60) :""+(sec%60));
        hor = sec/3600;
        min = (sec/60)%60;
        
        return ((hor>9)?""+hor:"0"+hor) + ":" + ((min>9)?""+min:"0"+min) + ":" + secStr; /* 00:02:33 */
    }
    
    public static long hours2sec(String text) throws Exception{
        long ret=0; 
        Exception e = new Exception("Invalid time format: " + text);
        if (text.length()==8) {
            if ((text.charAt(2)==':')&&(text.charAt(5)==':')) {
                int horP = Integer.parseInt(text.substring(0,2));
                int minP = Integer.parseInt(text.substring(3,5));
                int segP = Integer.parseInt(text.substring(6,8));
                ret = (long)horP * 3600 + (long)minP * 60 + (long)segP;
                return ret;   
            } else {
                throw e;
            }
            
        }
        
        throw e;
        
    }
    
    
    
}
