package miscellaneouspackage;

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
    
    
    public void sort(Vector vector){
        vector.trimToSize();
        this.quickSort(vector, 0, vector.size());
    }

    private int partition(Vector arr, int left, int right){
        int i = left, j = right;
        Object pivot = arr.elementAt((left + right) / 2);

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
