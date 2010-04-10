/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import javax.microedition.lcdui.Displayable;

/**
 *
 * @author Mauricio
 */
public interface Playerable {
    public void putTitleNms(String title, int mseconds);
    public void setText(String text);
    public void repaint();
    public Displayable getDisplayable();
    public void resetTranslation();
}
