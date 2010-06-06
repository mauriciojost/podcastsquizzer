package main;

import javax.microedition.lcdui.Displayable;

public interface Playerable{
    public void putTitleNms(String title, int mseconds);
    public void setText(String text);
    public String getPathFileWithExtension(String addedExtension);
    public Displayable getDisplayable();
    public void resetTranslation();
}
