package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

 /**
 * @author Gökçe Uludoğan
 * 
 * @version 1.0
 * 
 * @since
 */ 

public class Database {

    private String datasource;
    private String dbms;

    /**
     * the prepared statement for adding online clients to db
     */
    public PreparedStatement addOnlineClient,

    /**
     *the prepared statement for removing online clients from db
     */
    removeOnlineClient,

    /**
     * the prepared statement for getting client with its name
     */
    getClientByName,

    /**
     * the prepared statement for getting all clients
     */
    getAllClients;

    /**
     * the prepared statement for getting all files
     */
    public PreparedStatement getAllFiles,

    /**
     *the prepared statement for getting files by name
     */
    getFilesByName,

    /**
     *the prepared statement for getting files by extension
     */
    getFilesByExtention,

    /**
     *the prepared statement for getting files by owner
     */
    getFilesByOwner;

    /**
     *the prepared statement for getting files with min size
     */
    public PreparedStatement getFilesByMinimumSize,

    /**
     *the prepared statement for getting files with max size
     */
    getFilesByMaximumSize,

    /**
     *the prepared statement for adding file to db
     */
    addFile,

    /**
     *the prepared statement for removing file from db
     */
    removeFile;

    /**
     *the prepared statement for removing all files of a client from db
     */
    public PreparedStatement removeAllFilesByClient,

    /**
     *the prepared statement for getting file by id
     */
    getFileByID;
    
    public PreparedStatement getSharedClients;
    public PreparedStatement updateShareClients;

    /**
     *Constructor of Database
     * @param dbms
     * @param datasource
     * @param username
     * @param password
     */
    public Database(String dbms, String datasource, String username, String password) {
        this.dbms = dbms;
        this.datasource = datasource;
    }

    /**
     * Connects to the database
     * @return Database Connection
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public Connection createNewConnection() throws ClassNotFoundException, SQLException {
        //Try to create a database.
        //Depending on the DBMS, use different connection Drivers:
        //Note that if a database already exists in the given location, the the connection uses the existing DB.
        //If the database does not exist, then a new with the given name is created
        if (dbms.equalsIgnoreCase("access")) {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            return DriverManager.getConnection("jdbc:odbc:" + datasource);
        } else if (dbms.equalsIgnoreCase("cloudscape")) {
            Class.forName("COM.cloudscape.core.RmiJdbcDriver");
            return DriverManager.getConnection(
                    "jdbc:cloudscape:rmi://localhost:1099/" + datasource + ";create=true;");
        } else if (dbms.equalsIgnoreCase("pointbase")) {
            Class.forName("com.pointbase.jdbc.jdbcUniversalDriver");
            return DriverManager.getConnection(
                    "jdbc:pointbase:server://localhost:9092/" + datasource + ",new", "PBPUBLIC", "PBPUBLIC");
        } else if (dbms.equalsIgnoreCase("derby")) {
            Class.forName("org.apache.derby.jdbc.ClientXADataSource");
            return DriverManager.getConnection(
                    "jdbc:derby://localhost:1527/" + datasource + ";create=true");
        } else if (dbms.equalsIgnoreCase("mysql")) {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/" + datasource, "root", "javajava");
        } else {
            return null;
        }
    }

    /**
     * Creates DataSources, namely, the tables
     * @param connection
     * @param tableName
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void createDatasource(Connection connection, String tableName) throws ClassNotFoundException, SQLException {
        //Create a new connection to a database with given name and DBMS:
        boolean exist = false;
        int tableNameColumn = 3;
        DatabaseMetaData dbm = connection.getMetaData();
        //Checks to see if the "ACCOUNT" table is already created.
        //If it exists, then does nothing and returns.
         Statement statement = connection.createStatement();
        for (ResultSet rs = dbm.getTables(null, null, null, null); rs.next();) {
            if (rs.getString(tableNameColumn).equals(tableName)) {
                //statement.executeUpdate("DROP TABLE " + tableName);
                exist = true;
                rs.close();
                break;
            }
        }
        //If it does not exist then it creates the table:
        if (!exist) {
            //Creates the new statement:
           
            //And executes it:
            if (tableName.equals("ONLINE_CLIENTS")) {
                
                statement.executeUpdate("CREATE TABLE " + tableName + " (USERNAME VARCHAR(32) PRIMARY KEY, IP VARCHAR(32), PORT INTEGER, SHARE INTEGER)");
            } else if (tableName.equals("FILES")) {
                
                statement.executeUpdate("CREATE TABLE " + tableName + " (ID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                        + " FILENAME VARCHAR(32)," + " EXTENSION VARCHAR(32), SIZE INTEGER, OWNER VARCHAR(32), FOREIGN KEY(OWNER) REFERENCES ONLINE_CLIENTS(USERNAME))");
           
            }
        }

    }

    /**
     *  Initializes the prepared statements
     * @param connection
     * @throws SQLException
     */
        public void prepareStatements(Connection connection) throws SQLException {
        //Statements that query on the ONLINE_CLIENTS table:
        addOnlineClient = connection.prepareStatement("INSERT INTO ONLINE_CLIENTS VALUES (?, ?, ?, ?)");
        removeOnlineClient = connection.prepareStatement("DELETE FROM ONLINE_CLIENTS WHERE USERNAME = ? AND IP = ? AND PORT = ?");
        getClientByName = connection.prepareStatement("SELECT * FROM ONLINE_CLIENTS WHERE USERNAME = ?");
        getAllClients = connection.prepareStatement("SELECT * FROM ONLINE_CLIENTS");
        getSharedClients = connection.prepareStatement("SELECT * FROM ONLINE_CLIENTS WHERE SHARE = 1");
        updateShareClients = connection.prepareStatement("UPDATE ONLINE_CLIENTS SET SHARE = ? WHERE USERNAME = ? AND IP = ? AND PORT = ?");
//Starements that query on the FILES table:
        
        getAllFiles = connection.prepareStatement("SELECT * FROM FILES");
        getFilesByName = connection.prepareStatement("SELECT * FROM FILES WHERE FILENAME = ?");
        getFilesByExtention = connection.prepareStatement("SELECT * FROM FILES WHERE EXTENSION = ?");
        getFilesByOwner = connection.prepareStatement("SELECT * FROM FILES WHERE OWNER = ?");
        getFilesByMinimumSize = connection.prepareStatement("SELECT * FROM FILES WHERE SIZE<?");
        getFilesByMinimumSize = connection.prepareStatement("SELECT * FROM FILES WHERE SIZE>?");
        addFile = connection.prepareStatement("INSERT INTO FILES (FILENAME, EXTENSION, SIZE, OWNER) VALUES (?, ?, ?, ?)");
        removeFile = connection.prepareStatement("DELETE FROM FILES WHERE FILENAME = ? AND EXTENSION = ? AND OWNER = ?");
        removeAllFilesByClient = connection.prepareStatement("DELETE FROM FILES WHERE OWNER = ?");
        getFileByID = connection.prepareStatement("SELECT * FROM FILES WHERE ID = ?");
        }
        
    
}