/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package browserpackage;

/**
 *
 * @author Mauricio
 */
public interface BrowserReadyListener {
    /** 
     * Tells the caller form what file has been selected.
     * 
     * @param title       Title of the browser (used to give a description of the file).
     * @param path        Returns the selected path (or null in the case given by an abort operation).
     */
    public void browserReady(String title, String path);
}
