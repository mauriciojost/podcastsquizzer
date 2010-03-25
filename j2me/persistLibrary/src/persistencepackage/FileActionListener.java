/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package persistencepackage;

/**
 *
 * @author Mauricio
 */
public interface FileActionListener {
    public void writeOperationReady(String id, String path, boolean operation_successfully);
}
