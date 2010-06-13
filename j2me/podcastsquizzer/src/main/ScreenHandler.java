package main;

import javax.microedition.media.PlayerListener;

public interface ScreenHandler extends PlayerListener{
    public String getName();
    public String getMainStringToPaint();
    public boolean keyPressed(int keyCode);
    public void setMainElement(Object main_element) throws Exception;
    public void refreshScreen();
    public String getHelp();
    public String[] getKeysHelp();
}
