/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package textboxpackage;

/**
 *
 * @author Mauricio
 */
public interface TextBoxFormReadyListener {
    /** 
     * Tells the caller form what file has been selected.
     * 
     * @param title       Title of the textbox (used to give a description of the field required).
     * @param text        Returns the selected text (or null in the case given by an abort operation).
     */
    public void textBoxReady(String title, String text);
}
