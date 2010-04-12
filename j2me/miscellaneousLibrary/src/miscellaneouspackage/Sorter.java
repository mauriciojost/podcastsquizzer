package miscellaneouspackage;

import java.util.Enumeration;
import java.util.Vector;
/*
 * Thanks to:
 * http://www.algolist.net/Algorithms/Sorting/Quicksort
 * 
 */

public class Sorter {
    private Comparator comp; 
    public Sorter(Comparator comp){
        this.comp = comp;
    }
    
    public void sort(Vector vector) throws Exception{
        
        if (vector.size()>0){
            vector.trimToSize();
            Enumeration e = vector.elements();
            while (e.hasMoreElements()){
                if (comp.isComparable(e.nextElement())==false){
                    throw new Exception("Some elements are not comparable.");
                }
            }

            this.quickSort(vector, 0, vector.size()-1);
        }
    }

    private int partition(Vector arr, int left, int right){
        int i = left, j = right;
        Object pivot = arr.elementAt((left + right) / 2);

        try{
            while (i <= j) {
                while (comp.compare(arr.elementAt(i), pivot) < 0)
                    i++;
                while (comp.compare(arr.elementAt(j), pivot) > 0)
                    j--;
                if (i <= j) {
                    swap(arr, i, j);
                    i++;
                    j--;
                }
        }
        }catch(Exception e){
                e.printStackTrace();
            }

        return i;
    }
    
    private void swap(Vector vector, int i, int j){
        Object aux;
        aux = vector.elementAt(i);
        vector.setElementAt(vector.elementAt(j), i);
        vector.setElementAt(aux, j);
    }

    private void quickSort(Vector arr, int left, int right) {
        int index = partition(arr, left, right);
        if (left < index - 1)
            quickSort(arr, left, index - 1);
        if (index < right)
            quickSort(arr, index, right);
    }

}
