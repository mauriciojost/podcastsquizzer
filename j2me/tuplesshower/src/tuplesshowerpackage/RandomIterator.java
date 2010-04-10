/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tuplesshowerpackage;

import java.util.Random;
import java.util.Vector;
import persistencepackage.Tuple;

/**
 *
 * @author Mauricio
 */
public class RandomIterator implements Iterator {
    private Vector vector;
    private Vector vector_original;
    
    public RandomIterator(Vector vector){
        
        this.vector_original = vector;
        this.copyVectors();
    }
    
    private void copyVectors() {
        int i;
        this.vector = new Vector();
        for (i=0;i<vector_original.size();i++) {
            this.vector.addElement(vector_original.elementAt(i));
        }
    }
    
    public Tuple getNext() {
        Random rnd = new Random();
        Boolean correcto = Boolean.FALSE;
        Tuple tuple;
        
        do{
            try{                
                int posicion = rnd.nextInt(this.vector.size());
                tuple = (Tuple)vector.elementAt(posicion);
                vector.removeElementAt(posicion);
                vector.trimToSize();
                correcto = Boolean.TRUE;
            }catch(Exception e){
                tuple = new Tuple ("","","");
                correcto = Boolean.FALSE;
                this.reinitialize();
            }
        }while((correcto==Boolean.FALSE)&&(vector.size()>1));
        
        return tuple;
    }

    public Tuple getPrevious() {
        return new Tuple("Not available.","Not available.","Not available.");
    }

    public void reinitialize() {
        this.copyVectors();
    }

    public Tuple getCurrent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Vector getVector() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addNewTuple(Tuple tuple) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}












