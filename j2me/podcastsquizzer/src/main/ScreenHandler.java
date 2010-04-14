package main;

import javax.microedition.media.PlayerListener;

public interface ScreenHandler extends PlayerListener{
    public String getName();
    public boolean keyPressed(int keyCode);
    public void setMainElement(Object main_element);
    public void refreshScreen();
    public String getHelp();
    public String[] getKeysHelp();
}
