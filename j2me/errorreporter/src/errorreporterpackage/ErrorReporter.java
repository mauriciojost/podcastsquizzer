/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package errorreporterpackage;

import canvaspackage.MCanvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;

public class ErrorReporter extends MCanvas {
    private String errorText = "<error>";
    private String commentText = "";
            
    
    public void addError(String error){
        this.errorText = this.errorText + " | " + error; 
        super.setMainText(errorText);
        try {
            Thread.sleep(1000000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    public ErrorReporter(Display display, String error){
        super(display, (Displayable)null);
        this.setErrorText(error);
    }
    
    public void setErrorText(String text){
        this.errorText = text;
    }
    
}
