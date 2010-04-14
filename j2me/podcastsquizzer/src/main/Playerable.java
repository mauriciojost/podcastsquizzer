package main;

import javax.microedition.lcdui.Displayable;

public interface Playerable{
    public void putTitleNms(String title, int mseconds);
    public void setText(String text);
    public Displayable getDisplayable();
    public void resetTranslation();
}
