package server;

import database.DBConnectionManager;
import database.Database;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Server part of the FISH
 *
 * @author gokce
 */
public class Server {
    /**
     * Creates the database and the server
     *
     * @param args
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        String dbms = "derby";
        String datasource = "FISHDB";
        String userName = "";
        String password = "";

        /*Initialisation of the Server*/
        //1. Creation of the Database.
        Database db = new Database(dbms, datasource, userName, password);

        //2. Creation of the Database Connection Manager.     
        DBConnectionManager dbConnectionManager = new DBConnectionManager(db);
        System.out.println("Database Connection Manager ready.");

        //3. Initialisation of the Database Tables and Prepared statements.
        Connection con = dbConnectionManager.getConnectionFromPool();
        db.createDatasource(con, "ONLINE_CLIENTS");
        db.createDatasource(con, "FILES");

        con.close();
        System.out.println("Database created successfully.");

        //4. Initialisation of the communication components: 
        //localhost:4444 is default if there are no arguments.
        int port = 4444;
        String host = "localhost";
        /*Read host and port from arguments*/
        if (args.length > 1) {
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("USAGE: java Server [hostname] [port] ");
                System.exit(0);
            }
            host = args[0];
            if (args[0].equalsIgnoreCase("-h") || args[0].equalsIgnoreCase("-help")) {
                System.out.println("USAGE: java Server [hostname] [port] ");
                System.exit(1);
            }
        }
        try {
            //create an IP address and the server's socket to this address and port 2222
            InetAddress addr = InetAddress.getByName(host);
            ServerSocket serversocket = new ServerSocket(port, 1000, addr);
            System.out.println("Server ready. Listening to incoming connections on " + host + ":" + port + ".");
            Socket clientsocket = serversocket.accept();
            new ConnectionHandler(clientsocket, db, dbConnectionManager.getConnectionFromPool()).start();
            //new PingHandler(db);
            while (true) {    // the main server's loop
                System.out.println("Server ready. Listening to incoming connections on " + host + ":" + port + ".");
                /*Socket*/
                clientsocket = serversocket.accept();
                new ConnectionHandler(clientsocket, db, dbConnectionManager.getConnectionFromPool()).start();

            }
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }
}