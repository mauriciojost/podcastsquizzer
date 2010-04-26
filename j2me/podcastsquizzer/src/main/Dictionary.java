package main;

import java.io.IOException;



public abstract class Dictionary implements Runnable {
    protected static String NO_MATCH = "<Not found>";
    protected DictionaryListener dictionaryListener;
    protected int currentThread=0;
    protected String wordBeingSearched="";

    public Dictionary(DictionaryListener dl){
        this.dictionaryListener = dl;
    }

    public abstract void openDictionary(String path) throws Exception;
    public abstract String getLinesMatchedAt(String match, int amount_of_lines, int current_thread);


    public void run() {
        this.currentThread++;
        String result = this.getLinesMatchedAt(this.wordBeingSearched, 5, this.currentThread);
        this.dictionaryListener.expressionFound(result);
    }


    public void findMeaning(String expression){
        this.wordBeingSearched = expression;
        Thread ne = new Thread(this);
        ne.setPriority(Thread.MIN_PRIORITY);
        ne.start();
    }
}
