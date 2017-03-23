package database;

import java.util.*;
import java.sql.*;

 /**
 * @author Gökçe Uludoğan
 * 
 */ 

//Connection manager is responsible for creating a pool of connections
//that can operate on a Database given.
public class DBConnectionManager {

    private Vector connectionPool = new Vector();
    private Database db;
    /**
     * Constructor with the database as a parameter. Initializes the connection pool
     * @param db 
     */
    public DBConnectionManager(Database db) {
        this.db = db;
        initializeConnectionPool();
    }
    /**
     * Adds new connection instance until the pool is full
     */
    private void initializeConnectionPool() {
        while (!checkIfConnectionPoolIsFull()) {
            //Adding new connection instance until the pool is full
            connectionPool.addElement(createNewConnectionForPool());
        }
        System.out.println("Pool of DB Connections is full and ready.");
    }

    /**
     * Creates a new Connection
     * @return new Connection
     */ 
    private Connection createNewConnectionForPool() {
        Connection connection = null;
        try {
            connection = db.createNewConnection();
        } catch (SQLException sqle) {
            System.err.println("SQLException while creating new connection: " + sqle);
            return null;
        } catch (ClassNotFoundException cnfe) {
            System.err.println("ClassNotFoundException while creating new connection: " + cnfe);
            return null;
        }
        return connection;
    }
    /**
    * Gets a Connection from the pool
    * @return the connection 
    */
    public synchronized Connection getConnectionFromPool() {
        Connection connection = null;

        //Check if there is a connection available. There are times when all the connections in the pool may be used up
        if (connectionPool.size() > 0) {
            connection = (Connection) connectionPool.firstElement();
            connectionPool.removeElementAt(0);
        }
        //Giving away the connection from the connection pool
        return connection;
    }
    /**
     * Check whether the pool is full or not
     * @return true if the pool is full 
     */

    private synchronized boolean checkIfConnectionPoolIsFull() {
        //This is were we define the size of the pool.
        //The number can change according to the needs of the application.
        final int MAX_POOL_SIZE = 5;
        //Check if the pool size
        if (connectionPool.size() < MAX_POOL_SIZE) {
            return false;
        }
        return true;
    }
    /**
     * Adds a connection from client back to the pool
     * @param connection 
     */
    public synchronized void returnConnectionToPool(Connection connection) {
        //Adding the connection from the client back to the connection pool
        connectionPool.addElement(connection);
    }
}