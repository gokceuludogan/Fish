package client;

import file.SharedFile;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * Abstraction for list of file results
 * @author gokce
 */
public class ResultList implements Serializable {

    private transient ResultSet s;
    List<SharedFile> list = new ArrayList<SharedFile>();
    /**
     * Constructor with SQL ResultSet
     * @param s the sql result set
     * @throws SQLException 
     */
    public ResultList(ResultSet s) throws SQLException {
        this.s = s;
        execute();
    }
    /**
     * Takes the result set and changes it to a list of SharedFiles
     * @return List of SharedFiles
     * @throws SQLException 
     */
    private List execute() throws SQLException {
        while (s.next()) { // process results one row at a time.
            int ID = s.getInt("ID");
            String fileName = s.getString("FILENAME");
            String extension = s.getString("EXTENSION");
            int size = s.getInt("SIZE");
            String owner = s.getString("OWNER");
            SharedFile sharedFile = new SharedFile(ID, fileName, extension, size, owner);
            list.add(sharedFile);
        }
        return list;
    }
    /**
     * Gets SharedFile in the index  
     * @param index
     * @return SharedFile
     */

    public SharedFile get(int index) {
        return list.get(index);
    }
    /**
     * Deletes SharedFile in the index
     * @param index
     * @return new version of list without SharedFile with the given index
     */
    public SharedFile delete(int index){
        return list.remove(index);
    }
    /**
     * 
     * @return the size of the result list 
     */
    public int size() {
        return list.size();
    }
}