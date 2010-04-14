package persistencepackage;

public interface FileActionListener {
    public void writeOperationReady(String id, String path, boolean operation_successfully);
}
