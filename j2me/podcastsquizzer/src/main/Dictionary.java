package main;

import persistencepackage.FileServices;

public class Dictionary {
    protected static String NO_MATCH = "<Not found>";
    protected DictionaryListener dictionaryListener;
    protected int currentThread=0;
    protected String wordBeingSearchedNow="";
    private String dictionary;
    private int lastExpressionFoundedIndex=0;
    private String lastWord = "";

    public Dictionary(DictionaryListener dl){
        this.dictionaryListener = dl;
    }

    public void openDictionary(String path) throws Exception {
        path = "file:///E:/Spanish2.txt"; //hay que borrar esta línea. el problema está en el formato del archivo cuando este está bien en la parte de la raíz del filesystem.
        dictionary = FileServices.readTXTFile(path, false).toUpperCase();
    }

    public synchronized String getLinesMatchedAt(String word, int amount_of_lines) {
        if (word.toUpperCase().compareTo(this.lastWord)!=0){ /* If both words are different... */
            lastExpressionFoundedIndex = 0;
        }

        this.lastWord = word.toUpperCase();
        lastExpressionFoundedIndex = dictionary.indexOf(word, lastExpressionFoundedIndex);

        String ret = "";

        try{
            ret = dictionary.substring(lastExpressionFoundedIndex-20, lastExpressionFoundedIndex + amount_of_lines*20);
        }catch(Exception e){
            ret = NO_MATCH;
        }
        return ret;
    }
    


    public void findMeaning(String expression){
        this.wordBeingSearchedNow = expression.toUpperCase();
        Thread ne = new Thread(
            new Runnable(){
                public void run() {
                    currentThread++;
                    String result = getLinesMatchedAt("\n" + wordBeingSearchedNow, 2);
                    dictionaryListener.expressionFound(result);
                }
            }
        );
        ne.setPriority(Thread.MIN_PRIORITY);
        ne.start();
    }

}
