package tuplesshowerpackage;

import java.util.Vector;
import miscellaneouspackage.Tuple;

public interface Iterator {
    public Tuple getNext();
    public Tuple getCurrent();
    public Tuple getPrevious();
    public void reinitialize();
    public Vector getVector();
    public void addNewTuple(Tuple tuple);
    public int getCurrentIndex();
    public String setMode(int mode);
}
