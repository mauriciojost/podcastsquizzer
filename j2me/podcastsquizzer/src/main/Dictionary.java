package main;

import persistencepackage.FileServices;

public class Dictionary {
    protected static String NO_MATCH = "<Not found>";
    protected DictionaryListener dictionaryListener;
    protected int currentThread=0;
    protected String wordBeingSearchedNow="";
    private String dictionary;
    private int lastExpressionFoundedIndex=-1;
    private String lastWord = "";

    public Dictionary(DictionaryListener dl){
        this.dictionaryListener = dl;
    }

    public void openDictionary(String path) throws Exception {
        dictionary = FileServices.readTXTFile(FileServices.correctURL(path), false).toUpperCase();
    }

    public synchronized String getLinesMatchedAt(String word, int amount_of_lines) {
        if (word.toUpperCase().compareTo(this.lastWord)!=0){ /* If both words are different... */
            lastExpressionFoundedIndex = -1;
        }

        this.lastWord = word.toUpperCase();
        lastExpressionFoundedIndex = dictionary.indexOf(word, lastExpressionFoundedIndex+1);

        String ret = "";

        try{
            ret = dictionary.substring(lastExpressionFoundedIndex-amount_of_lines*10, lastExpressionFoundedIndex + amount_of_lines*20);
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
                    String result = getLinesMatchedAt(wordBeingSearchedNow, 4);
                    dictionaryListener.expressionFound(result);
                }
            }
        );
        ne.setPriority(Thread.MIN_PRIORITY);
        ne.start();
    }

}
