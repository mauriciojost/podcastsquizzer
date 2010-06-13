package main;

import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;

public interface Playerable{
    public void putTitleNms(String title, int mseconds);
    public void setText(ScreenHandler sh, String text);
    public void setBackgroundImage(Image img);
    public String getPathFileWithExtension(String addedExtension);
    public Displayable getDisplayable();
    public void resetTranslation();
}
