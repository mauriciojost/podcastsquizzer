/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import javax.microedition.media.PlayerListener;

/**
 *
 * @author Mauricio
 */
public interface ScreenHandler extends PlayerListener{
    public String getName();
    public boolean keyPressed(int keyCode);
    public void setMainElement(Object main_element);
    public void refreshScreen();
            
}
