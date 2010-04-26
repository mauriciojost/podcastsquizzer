package main;

import persistencepackage.FileServices;

public class MemoryDictionary extends Dictionary{
    private String dictionary;

    public MemoryDictionary(DictionaryListener dl){
        super(dl);
    }

    public void openDictionary(String path) throws Exception {
        path = "file:///E:/Spanish2.txt"; //hay que borrar esta línea. el problema está en el formato del archivo cuando este está bien en la parte de la raíz del filesystem.
        dictionary = FileServices.readTXTFile(path, false);
    }

    public String getLinesMatchedAt(String match, int amount_of_lines, int current_thread) {
        int indice = dictionary.indexOf(match);
        String ret = "";

        try{
            ret = dictionary.substring(indice, indice + amount_of_lines*20);
        }catch(Exception e){
            ret = NO_MATCH;
        }

        return ret ;
    }
}
